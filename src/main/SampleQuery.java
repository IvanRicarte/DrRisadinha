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

import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rdfdata.Namespaces;

/**
 *
 * @author ricarte
 */
public class SampleQuery {
    static Logger log = LogManager.getLogger(SampleQuery.class);
    public static void main(String[] args) {
        q2();
    }

    static void q1() {
                String queryString = "PREFIX dc: <" + Namespaces.NS_DCT + ">\n"
                + "PREFIX owl: <" + OWL.getURI() + ">\n"
                + "SELECT ?t ?dbs WHERE { \n"
                + "?id dc:title ?t .\n"
                + "?id dc:subject ?s .\n"
                + "?s owl:sameAs ?dbs .\n"
                + "}";
        Dataset dataset = TDBFactory.createDataset("MyDataset");
        Query q = QueryFactory.create(queryString);
        QueryExecution qx = QueryExecutionFactory.create(q, dataset);
        log.info("Query: " + qx.getQuery().toString());        
        ResultSet results = qx.execSelect();
        log.info("Results: ");
        List<String> vars = results.getResultVars();
        for (String var : vars)
            log.info(var);
        int i = 0;
        while (results.hasNext()) {
            QuerySolution ans = results.next();
            log.info("Resultado " + ++i);
            System.out.println(ans.get("t").toString() + " : " + ans.get("dbs").toString());
        }
    }
    
        static void q2() {
                String queryString = "PREFIX dc: <" + Namespaces.NS_DCT + ">\n"
                + "PREFIX owl: <" + OWL.getURI() + ">\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
                + "prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
                + "SELECT ?t ?icd WHERE { \n"
                + "?id dc:title ?t .\n"
                + "?id dc:subject ?s .\n"
                + "?s owl:sameAs ?dbs .\n"
                + "?dbs dbo:icd10 ?icd .\n"
                + "}";
        Dataset dataset = TDBFactory.createDataset("MyDataset");
        Query q = QueryFactory.create(queryString);
        QueryExecution qx = QueryExecutionFactory.create(q, dataset);
        log.info("Query: " + qx.getQuery().toString());        
        ResultSet results = qx.execSelect();
        log.info("Results: ");
        List<String> vars = results.getResultVars();
        for (String var : vars)
            log.info(var);
        int i = 0;
        while (results.hasNext()) {
            QuerySolution ans = results.next();
            RDFNode result = ans.get("t");
            System.out.println("("+ ++i +") " + 
                    ans.get("t").toString() + " : " + 
                    ans.get("icd").toString()
                    );
        }
    }
}
