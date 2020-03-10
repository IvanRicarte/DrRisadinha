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
package mains;

import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.apache.log4j.Logger;
import rdfdata.RdfRepository;
import rdfdata.RdfService;

/**
 * @author ricarte at ft.unicamp.br
 */
public class AddDbpediaConnections {

    static Logger log = Logger.getLogger(AddDbpediaConnections.class);

    public static void main(String[] args) {
        log.info("============= Adding DBpedia labels ==============");
        RdfRepository dataset = new RdfRepository("MyDataset");
        Model blogRdf = dataset.getModel();
        dataset.beginRead();
        NodeIterator subjects = blogRdf.listObjectsOfProperty(DCTerms.subject);
        dataset.commit();
        Resource node;
        String label;
        while (subjects.hasNext()) {
            node = (Resource) subjects.nextNode();
            dataset.beginRead();
            label = blogRdf.getProperty(node, RDFS.label).getLiteral().getString();
            dataset.commit();
            List<Resource> recursoDBPedia = RdfService.queryDBPediaDisease(removerEspaco(label));
            if (!recursoDBPedia.isEmpty()) {
                Resource dbpediaLabel = recursoDBPedia.get(0);
                log.info(label + " same as " + dbpediaLabel);
                dataset.beginWrite();
                blogRdf.add(blogRdf.createStatement(node, OWL.sameAs, dbpediaLabel));
                dataset.commit();
            }
        }
        log.info("============= Ended DBpedia labels ===============");
    }

    private static String removerEspaco(String string) {
        string = string.replaceAll("\u200b", "");
        string = string.replaceAll(" ", "");
        return string;
    }
}
