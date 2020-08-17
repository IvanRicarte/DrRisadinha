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
package rdfdata;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author ricarte at ft.unicamp.br
 */
public class DbpediaConnections {

    static Logger log = LogManager.getRootLogger();

    RdfRepository dataset;
    Model blogRdf;

    public DbpediaConnections() {
        Properties blogProp = new Properties();
        try {
            blogProp.load(new FileInputStream("risadinha.properties"));
        } catch (IOException ex) {
            log.warn(ex.getLocalizedMessage());
        }
        dataset = new RdfRepository(blogProp.getProperty("repositorydir"));
        blogRdf = dataset.getModel();
    }

    public NodeIterator getSubjects() {
        dataset.beginRead();
        NodeIterator subjects = blogRdf.listObjectsOfProperty(DCTerms.subject);
        dataset.commit();
        return subjects;
    }

    public String getLabel(Resource node) {
        dataset.beginRead();
        String label = blogRdf.getProperty(node, RDFS.label).getLiteral().getString();
        dataset.commit();
        return label;
    }

    private Resource searchThing(String label) {
        Resource dbpediaLabel = null;
        List<Resource> recursoDBPedia;
        recursoDBPedia = RdfService.queryDBPediaThing(label);
        if (!recursoDBPedia.isEmpty()) {
            dbpediaLabel = recursoDBPedia.get(0);
        }
        return dbpediaLabel;
    }

    private Resource searchCategory(String label, String dbCategory) {
        Resource dbpediaLabel = null;
        List<Resource> recursoDBPedia;
        recursoDBPedia = RdfService.queryDBPediaOntology(label, dbCategory);
        if (!recursoDBPedia.isEmpty()) {
            dbpediaLabel = recursoDBPedia.get(0);
        }
        return dbpediaLabel;
    }

    public Resource search(String label) {
        Resource dbpediaNode;
        dbpediaNode = searchCategory(label, "Disease");
        if (dbpediaNode != null) {
            return dbpediaNode;
        }
        dbpediaNode = searchCategory(label, "AnatomicalStructure");
        if (dbpediaNode != null) {
            return dbpediaNode;
        }
        dbpediaNode = searchCategory(label, "ChemicalCompound");
        if (dbpediaNode != null) {
            return dbpediaNode;
        }
        dbpediaNode = searchCategory(label, "Food");
        if (dbpediaNode != null) {
            return dbpediaNode;
        }
        dbpediaNode = searchThing(label);

        return dbpediaNode;
    }

    public void add(Resource node, Resource dbpediaLabel) {
        dataset.beginWrite();
        blogRdf.add(blogRdf.createStatement(node, OWL.sameAs, dbpediaLabel));
        dataset.commit();
    }




}
