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
import java.util.Locale;
import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import rdfdata.Namespaces;
import rdfdata.RdfRepository;

/**
 * @author ricarte at ft.unicamp.br
 */
public class QueryPersons {

    public static void main(String[] args) {
        RdfRepository dataset = new RdfRepository("MyDataset");
        System.out.println("Repositório modificado em " + 
                dataset.getLastModifiedDate("http://www.drrisadinha.org.br/"));
        List<String> persons = dataset.getPerson();

//        Model personsRdf = ModelFactory.createDefaultModel();
//        personsRdf.read("colaboradores.ttl");
        Model personsRdf = dataset.getModel();
        Property name = personsRdf.getProperty(Namespaces.NS_FOAF, "name");
        
        FuzzyScore fuzz = new FuzzyScore(Locale.getDefault());
        for (String person : persons) {
            dataset.beginRead();
            ResIterator it = personsRdf.listSubjectsWithProperty(name, person);
            dataset.end();
            if (it.hasNext()) {
                while (it.hasNext()) {
                    System.out.println("Localizou " + person + " como " + it.nextResource().getLocalName());
                }
            } else {
                int score = 0;
                int newscore;
                dataset.beginRead();
                NodeIterator nit = personsRdf.listObjectsOfProperty(name);
                dataset.end();
                RDFNode best = null;
                while (nit.hasNext()) {
                    RDFNode node = nit.nextNode();
                    newscore = fuzz.fuzzyScore(person, node.toString());
                    if (newscore > score) {
                        score = newscore;
                        best = node;
                    }
                }
                dataset.beginRead();
                ResIterator it2 = personsRdf.listSubjectsWithProperty(name, best);
                dataset.end();
                if (it2.hasNext()) {
                    System.out.println("Melhor match para " + person + ": " + it2.next().getLocalName() +
                            " com score " + score);
                } else 
                    System.out.println("Nenhum match para " + person);
            }
        }
    }
}
