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
import bloggerdata.Post;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.log4j.Logger;
import risadinha.RisadinhaPost;

/**
 * Repositório local para o armazenamento de dados em RDF.
 *
 * @author Karina Hagiwara
 * @author ricarte at ft.unicamp.br
 */
public class RdfRepository {

    private final Dataset dataset;
    static Logger log = Logger.getLogger(RdfRepository.class);

    /**
     * Cria um repositório RDF no diretório especificado, incorporando no 
     * modelo default os dados dos colaboradores do Projeto Dr Risadinha.
     * 
     * @param dir Diretório de localização do repositório.
     */
    public RdfRepository(String dir) {
        dataset = TDBFactory.createDataset(dir);
        Model personsRdf = ModelFactory.createDefaultModel();
        personsRdf.read("colaboradores.ttl");
        add(personsRdf);
    }

    /**
     * Adiciona um modelo com triplas RDF ao modelo default do repositório.
     * @param model O modelo a ser adicionado.
     */
    public void add(Model model) {
        Model modelTdb = dataset.getDefaultModel();
        beginWrite();
        modelTdb.add(model);
        commit();
    }

    /**
     * Inicia transação de leitura.
     */
    public void beginRead() {
        dataset.begin(ReadWrite.READ);
    }

    /**
     * Inicia transação de escrita.
     */
    public void beginWrite() {
        dataset.begin(ReadWrite.WRITE);
    }

    /**
     * Conclui uma transação.
     */
    public void commit() {
        dataset.commit();
    }

    /** 
     * Encerra operações no repositório.
     */
    public void end() {
        dataset.end();
    }

    /**
     * Executa consulta SPARQL sobre os dados do repositório.
     * @param qs1 A consulta SPARQL.
     */
    public void query(String qs1) {
        try (QueryExecution qExec = QueryExecutionFactory.create(qs1, dataset)) {
            ResultSet rs = qExec.execSelect();
            ResultSetFormatter.out(rs);
        } catch (Exception ex) {
            log.warn(qs1, ex);
        }
    }
    
    /**
     * Obter modelo com triplas RDF do repositório.
     * @return O modelo.
     */
    public Model getModel() {
        beginRead();
        Model model = dataset.getDefaultModel();
        end();
        return model; 
    }

    /**
     * Exporta triplas RDF para um arquivo em formato Turtle.
     * @param filename Nome do arquivoa ser criado.
     */
    public void export(String filename) {
        OutputStream out;
        try {
            out = new FileOutputStream(filename);
        } catch (FileNotFoundException ex) {
            log.warn("Exporting to " + filename, ex);
            out = System.out;
        }
        beginRead();
        RDFDataMgr.write(out, dataset.getDefaultModel(), RDFFormat.TURTLE_PRETTY);
        end();
    }

    private List<String> queryList(String qs1, String str) {
        List<String> resultados = new ArrayList<>();
        beginRead();
        try (QueryExecution qExec = QueryExecutionFactory.create(qs1, dataset)) {
            ResultSet rs = qExec.execSelect();
            while (rs.hasNext()) {
                resultados.add(rs.next().get(str).toString());
            }
        } catch (Exception ex) {
            log.warn(qs1 + " ; " + str, ex);
        }
        end();
        return resultados;
    }

    /**
     * Verifica se existe no conjunto de dados a identificação do blog.
     * @param id
     * @return True se o identificador do blog já existe no conjunto de dados RDF.
     */
    public boolean blogExists(String id) {
        return queryList("SELECT ?identifier "
                + "WHERE { "
                + "  <http://drrisadinha.org/BlogDrRisadinha> <http://purl.org/dc/terms/identifier> ?identifier ."
                + "}  ", "identifier").contains(id);
    }

    /**
     * Adiciona ao repositório todos os dados do blog e de suas publicações.
     * @param blog O objeto que contém toda a informação sobre o blog.
     */
    public void create(Blog blog) {
        int count = blog.getNumberOfPosts();
        try {
            RdfGraph graph = new RdfGraph();
            add(graph.addBlog(blog));
            List<Post> postList = blog.getPosts();
            Model personsRdf = getPersonsFromTtl();
            for (Post original : postList) {
                RisadinhaPost post = new RisadinhaPost(original);
                add(graph.addPost(post, personsRdf));
                log.info("#" + count-- + ": " + post.getTitle());
            }
        } catch (IOException ex) {
            log.warn("While adding " + count, ex);
        }
    }

    /**
     * Obtém lista de pessoas no blog.
     * @return Lista de nomes de pessoas (autores ou revisores).
     */
    public List<String> getPerson() {
        String query = "SELECT ?name " + "WHERE { "
                + "  ?o <http://xmlns.com/foaf/0.1/name> ?name ."
                + "  ?o a <http://xmlns.com/foaf/0.1/Person>" + "}  ";
        List<String> resultados = this.queryList(query, "name");
        return resultados;
    }

    /**
     * Obtém data de última atualização registrada no repositório.
     * @param uri URI do recurso associado ao blog no repositório.
     * @return String com data de última modificação.
     */
    public String getLastModifiedDate(String uri) {
        beginRead();
        String date = dataset.getDefaultModel().getResource(uri).getProperty(DCTerms.modified).getString();
        end();
        return date;
    }

    private Model getPersonsFromTtl() {
        Model personsRdf = ModelFactory.createDefaultModel();
        personsRdf.read("colaboradores.ttl");
        return personsRdf;
    }
}
