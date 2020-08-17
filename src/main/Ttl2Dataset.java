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
import java.util.Properties;
import org.apache.jena.rdf.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rdfdata.RdfRepository;

/**
 * @author ricarte at ft.unicamp.br
 */
public class Ttl2Dataset {

    static Logger log = LogManager.getRootLogger();

    public static void main(String[] args) {
        log.info("============= Converting from TTL ==============");
        
        try {
            Properties blogProp = new Properties();
            blogProp.load(new FileInputStream("risadinha.properties"));
            RdfRepository dataset = new RdfRepository(blogProp.getProperty("repositorydir"));
            Model model = dataset.getModel();
            String filename = blogProp.getProperty("turtlefilename");
            dataset.beginWrite();
            model = model.read(filename);
            log.info("Imported " + model.size() + " statements from " + filename);
            dataset.commit();
        } catch (IOException ex) {
            log.error("Durante conversão para RDF", ex);
        }
        
        log.info("============= Imported TTL ===============");
    }
    
}
