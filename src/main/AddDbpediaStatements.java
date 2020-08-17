/*
 * The MIT License
 *
 * Copyright 2020 Prof. Ivan L. M. Ricarte, FT-UNICAMP.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rdfdata.RdfRepository;

/**
 * @author ricarte at ft.unicamp.br
 */
public class AddDbpediaStatements {

    static Logger log = LogManager.getRootLogger();

    private static boolean isPureLatin1(String v) {
        return Charset.forName("ISO-8859-1").newEncoder().canEncode(v);
    }

    public static void main(String[] args) {
        String service = "http://dbpedia.org/sparql";
        String queryStr;
        String propFile;
        if (args.length < 1) {
            propFile = "risadinha.properties";
        } else {
            propFile = args[0];
        }
        log.info("============= Adding DBpedia statements ==============");
        Properties blogProp = new Properties();
        try {
            blogProp.load(new FileInputStream(propFile));
        } catch (IOException ex) {
            log.warn(ex.getLocalizedMessage());
        }
        RdfRepository dataset = new RdfRepository(blogProp.getProperty("repositorydir"));
        Model model = dataset.getModel();
        dataset.beginRead();
        NodeIterator dbSubjects = model.listObjectsOfProperty(OWL.sameAs);
        dataset.commit();
        while (dbSubjects.hasNext()) {
            Resource subjNode = (Resource) dbSubjects.nextNode();
            String subject = subjNode.toString();
            queryStr = "SELECT ?p ?o WHERE { \n<"
                    + subject + "> ?p ?o . \n"
                    + "filter(!isLiteral(?o) || (langMatches(lang(?o), 'en') || lang(?o)='pt' || lang(?o)='')) \n}";
            log.info("Querying for: " + subject);
            Query query = QueryFactory.create(queryStr);
            ResultSet resp;
            Property property;
            Resource dbProp;
            RDFNode object;
            QuerySolution sol;
            try (QueryExecution qexec = QueryExecutionFactory.sparqlService(service, query)) {
                resp = qexec.execSelect();
                while (resp.hasNext()) {
                    sol = resp.next();
                    dbProp = (Resource) sol.get("p");
                    property = model.createProperty(dbProp.getNameSpace(), dbProp.getLocalName());
                    object = sol.get("o");
                    String objectLabel = object.toString();
                    if (isPureLatin1(objectLabel)) {
                        log.info("Adding (" + subject + "," + property.getLocalName()
                                + "," + objectLabel + ")");

                        dataset.beginWrite();
                        model.add(
                                model.createStatement(subjNode, property, object));
                        dataset.commit();
                    }
                }
                qexec.close();
            } catch (Exception ex) {
                log.warn(ex.getLocalizedMessage());
                //dataset.commit();
            }
        }
        log.info("============= Ended DBpedia statements ===============");
    }

}
