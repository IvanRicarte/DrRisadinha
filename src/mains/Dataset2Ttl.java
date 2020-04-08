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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import rdfdata.RdfRepository;

/**
 * @author ricarte at ft.unicamp.br
 */
public class Dataset2Ttl {

    static Logger log = Logger.getLogger(Dataset2Ttl.class);

    public static void main(String[] args) {
        log.info("============= Converting to TTL ==============");
        try {
            Properties blogProp = new Properties();
            blogProp.load(new FileInputStream("risadinha.properties"));
            RdfRepository dataset = new RdfRepository(blogProp.getProperty("repositorydir"));
            dataset.export(blogProp.getProperty("turtlefilename"));
        } catch (IOException ex) {
            log.error("Durante conversão para RDF", ex);
        }
        log.info("============= Exported TTL ===============");
    }
}
