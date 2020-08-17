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

/**
 * Namespaces utilizados na codificação RDF das publicações do blog.
 * Alguns desses namespaces já estão embutidos no pacote Jena.
 * @author ricarte
 */
public interface Namespaces {
    String NS_RISADINHA = "http://drrisadinha.org.br/";
    String NS_DC = "http://purl.org/dc/elements/1.1/";
    String NS_DCT = "http://purl.org/dc/terms/";
    String NS_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    String NS_RDFS = "http://www.w3.org/2000/01/rdf-schema#";
    String NS_SIOC = "http://rdfs.org/sioc/ns#";
    String NS_FOAF = "http://xmlns.com/foaf/0.1/";
    String NS_DCMI = "http://purl.org/dc/dcmitype/"; 
    String NS_OWL = "http://www.w3.org/2002/07/owl#";
    String NS_DBPEDIA = "http://dbpedia.org/resource/";
}
