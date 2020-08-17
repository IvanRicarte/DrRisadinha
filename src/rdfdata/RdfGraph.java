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

import bloggerdata.Blog;
import java.util.Collection;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.URIref;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import risadinha.Referencia;
import risadinha.RisadinhaPost;

/**
 * Classe que representa um grafo RDF de publicações do blog.
 *
 * @author karii
 * @author ricarte at ft.unicamp.br
 */
public class RdfGraph {

    static Logger log = LogManager.getRootLogger();

    private final Model model;
    private Resource resourceBlog;

    /**
     * Inicializa namespaces e modelo default para o repositório.
     */
    public RdfGraph() {
        model = ModelFactory.createDefaultModel();
        model.setNsPrefix("dct", DCTerms.NS);
        model.setNsPrefix("foaf", Namespaces.NS_FOAF);
        model.setNsPrefix("sioc", Namespaces.NS_SIOC);
        model.setNsPrefix("rdfs", Namespaces.NS_RDFS);
        model.setNsPrefix("rdf", Namespaces.NS_RDF);
        model.setNsPrefix("owl", Namespaces.NS_OWL);
        model.setNsPrefix("ris", Namespaces.NS_RISADINHA);
        model.setNsPrefix("dbp", Namespaces.NS_DBPEDIA);
    }

    /**
     * Inicializa dados do blog no grafo.
     *
     * @param blog Referência para o blog.
     * @return Modelo RDF criado.
     */
    public Model addBlog(Blog blog) {
        try {
            resourceBlog = model.createResource(blog.getUrl());
            addStatement(resourceBlog, RDF.type, model.createResource(Namespaces.NS_SIOC + "Forum"));
            addStatement(resourceBlog, DCTerms.identifier, model.createTypedLiteral(blog.getId()));
            addLiteral(resourceBlog, DCTerms.title, blog.getName());
            addLiteral(resourceBlog, DCTerms.description,
                    "Dados abertos derivados do blog Fale com o Dr. Risadinha");
            addLiteral(resourceBlog, DCTerms.modified, blog.getLastUpdated().toString());
            addLiteral(resourceBlog, DCTerms.issued, blog.getPublished().toString());
            addLiteral(resourceBlog, DCTerms.language, "pt-BR");
            addLiteral(resourceBlog, DCTerms.source, blog.getSelfLink());
            addLiteral(resourceBlog, DCTerms.license, "http://creativecommons.org/licenses/by/4.0/");
        } catch (Exception ex) {
            log.warn("Add blog " + blog.getName(), ex);
        }
        return model;
    }

    /**
     * Adiciona uma publicação ao grafo RDF.
     *
     * @param post A publicação do blog Dr Risadinha a ser adicionada.
     * @param personsRdf Subgrafo com dados dos colaboradores do Projeto Dr Risadinha.
     * @return Modelo com a publicação adicionada.
     */
    public Model addPost(RisadinhaPost post, Model personsRdf) {
        Resource resourcePost;

        resourcePost = model.createResource(Namespaces.NS_RISADINHA + post.getId());
        addStatement(resourcePost, RDF.type, model.createResource(Namespaces.NS_SIOC + "Post"));
        try {
            addStatement(resourcePost, DCTerms.isPartOf, resourceBlog);
            addStatement(resourcePost, DCTerms.identifier, model.createTypedLiteral(post.getId()));
            addLiteral(resourcePost, DCTerms.title, post.getTitle());
            addLiteral(resourcePost, DCTerms.issued, post.getPublishedDate().toString());
            addLiteral(resourcePost, DCTerms.modified, post.getUpdatedDate().toString());
            addLiteral(resourcePost, DCTerms.abstract_, post.getMensagemCurta());
            addLiteral(resourcePost, model.createProperty(Namespaces.NS_SIOC, "content"), post.getMensagem());
            addLiteral(resourcePost, DCTerms.source, post.getUrl());
            addLiteral(resourcePost, model.createProperty(Namespaces.NS_FOAF, "depiction"), post.getImageUrl());
            addLiteral(resourcePost, DCTerms.language, "pt-BR");

            // autores
            Collection<String> autores = post.getAutor();
            if (autores.isEmpty()) {
                log.warn("Sem autor identificado: " + post.getTitle());
            } else {
                for (String autor : autores) {
                    addPerson(resourcePost, DCTerms.creator, autor, personsRdf);
                }
            }
            // revisores
            Collection<String> revisores = post.getRevisores();
            if (revisores.isEmpty()) {
                log.warn("Sem revisor identificado:" + post.getTitle());
            } else {
                for (String revisor : revisores) {
                    addPerson(resourcePost, DCTerms.contributor, revisor, personsRdf);
                }
            }

            Collection<String> referencias = post.getReferencia();
            for (String reference : referencias) {
                Referencia ref = new Referencia(reference);
                Resource refRes = model.createResource();
                model.add(model.createStatement(refRes, DCTerms.creator, ref.getSource()));
                model.add(model.createStatement(refRes, DCTerms.description, ref.getDescription()));
                String refUrl = ref.getUrl();
                if (refUrl.length() > 0)
                    model.add(model.createStatement(refRes, DCTerms.source, refUrl));
                String refDate = ref.getUpdated();
                if (refDate.length() > 0)
                    model.add(model.createStatement(refRes, DCTerms.date, refDate));
                String refAccessed = ref.getAccessed();
                if (refAccessed.length() > 0)
                    model.add(model.createStatement(refRes, DCTerms.available, refAccessed));
                model.add(model.createStatement(resourcePost, DCTerms.references, refRes));
            }

            addSubject(resourcePost, post.getLabels());
        } catch (Exception ex) {
            log.warn(post.getTitle(), ex);
        }

        return model;
    }

    private void addStatement(Resource resource, Property property, RDFNode value) {
        model.add(
                model.createStatement(resource, property, value));
    }

    private void addLiteral(Resource resource, Property property, String value) {
        model.add(model.createStatement(resource, property, model.createLiteral(value)));
    }

    private void addPerson(Resource resource, Property role, String person, Model personsRdf) {
        final int threshold = 15;
        FuzzyScore fuzz = new FuzzyScore(Locale.getDefault());
        Property name = personsRdf.getProperty(Namespaces.NS_FOAF, "name");

        ResIterator it = personsRdf.listSubjectsWithProperty(name, person);
        if (it.hasNext()) {
            model.add(model.createStatement(resource, role, it.nextResource()));
        } else {
            int score = 0;
            int newscore;
            NodeIterator nit = personsRdf.listObjectsOfProperty(name);
            RDFNode best = null;
            while (nit.hasNext()) {
                RDFNode node = nit.nextNode();
                newscore = fuzz.fuzzyScore(person, node.toString());
                if (newscore > score) {
                    score = newscore;
                    best = node;
                }
            }
            if (score >= threshold) {
                ResIterator it2 = personsRdf.listSubjectsWithProperty(name, best);
                model.add(model.createStatement(resource, role, it2.nextResource()));
            } else {
                Resource newPerson = model.createResource(Namespaces.NS_RISADINHA
                        + URIref.encode(StringUtils.stripAccents(person)));
                model.add(model.createStatement(newPerson, RDF.type, model.createResource(Namespaces.NS_FOAF + "Person")));
                model.add(model.createStatement(newPerson, name, person));
                model.add(model.createStatement(resource, role, newPerson));
            }
        }
    }

    private void addSubject(Resource resourcePost, String[] labels) {
        Resource resourceLabel;
        if (labels != null) {
            for (String label : labels) {
                String subject = label.toLowerCase().replaceAll("\u200b", "");
                subject = StringUtils.deleteWhitespace(subject);
                subject = StringUtils.stripAccents(subject);
                subject = StringUtils.remove(subject, ".-");
                resourceLabel = model.createResource(Namespaces.NS_RISADINHA
                        + subject);
                model.add(model.createStatement(resourcePost, DCTerms.subject, resourceLabel));
                model.add(model.createLiteralStatement(resourceLabel, RDFS.label, label));
                log.info("\t Subject label: " + label);
            }
        }
    }
}
