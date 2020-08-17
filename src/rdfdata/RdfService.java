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

import java.util.ArrayList;
import java.util.List;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static rdfdata.RdfService.log;

/**
 * Classe utilitária para realização de consultas a repositórios externos
 * usando o mecanismo SERVICE de SPARQL.
 * @author karii
 * @author ricarte at ft.unicamp.br
 */
public class RdfService {

    static Logger log = LogManager.getRootLogger();

    /**
     * Consulta à DBpedia por recursos relacionados ao marcador e que tenham
     * como tipo (RDF.type) 'term' (http://dbpedia.org/ontology/ + term).
     * @param recursoSubject O marcador a ser buscado.
     * @param term  O termo pertencente à ontologia de DBpedia.
     * @return Lista de recursos da DBpedia relacionados ao marcador passado.
     */
    public static List<Resource> queryDBPediaOntology(String recursoSubject, String term) {

        String serviceStr = "http://dbpedia.org/sparql";

        String queryStr = "PREFIX dbpedia-pt: <http://pt.dbpedia.org/resource/>\r\n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\r\n"
                + "select distinct ?recursoSubject where {\n"
                + "?recursoSubject owl:sameAs dbpedia-pt:" + recursoSubject + ".\n"
                + "?recursoSubject a dbo:" + term +" .} LIMIT 5";
        Query query = QueryFactory.create(queryStr);
        List<Resource> resultados = new ArrayList<>();

        //Começa a execução remota
        try (
                QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceStr, query)) {
            //Coloca timeout do dbpedia
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

            //Executa a query
            ResultSet rs = qexec.execSelect();
            //ResultSetFormatter.out(System.out, rs, query);
            while (rs.hasNext()) {
                resultados.add(rs.next().getResource("recursoSubject"));
            }
            qexec.close();
        } catch (Exception ex) {
            log.warn(recursoSubject, ex);
        }
        return resultados;
    }

    /**
     * Consulta genérica a serviço externo.
     * @param serviceStr Identificação (URL) do serviço.
     * @param queryStr String de consulta SPARQL.
     * @return Modelo com triplas RDF.
     */
    public static Model queryDBpedia(String serviceStr, String queryStr) {
        Query query = QueryFactory.create(queryStr);
        Model resultados = null;

        //Começa a execução remota
        try (
                QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceStr, query)) {
            //Coloca timeout do dbpedia
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

            //Executa a query
            resultados = qexec.execConstruct();
            qexec.close();
        } catch (Exception ex) {
            log.warn(serviceStr + " : " + queryStr + " : ", ex);
        }
        return resultados;
    }    

 /**
     * Consulta à DBpedia por recursos relacionados ao marcador e que tenham
     * como tipo (RDF.type) owl:Thing.
     * @param recursoSubject O marcador a ser buscado.
     * @return Lista de recursos da DBpedia relacionados ao marcador passado.
     */
    public static List<Resource> queryDBPediaThing(String recursoSubject) {

        String serviceStr = "http://dbpedia.org/sparql";

        String queryStr = "PREFIX dbpedia-pt: <http://pt.dbpedia.org/resource/>\r\n"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>\r\n"
                + "PREFIX dbo: <http://dbpedia.org/ontology/>\r\n"
                + "select distinct ?recursoSubject where {\n"
                + "?recursoSubject owl:sameAs dbpedia-pt:" + recursoSubject + ".\n"
                + "?recursoSubject a owl:Thing .} LIMIT 5";
        Query query = QueryFactory.create(queryStr);
        List<Resource> resultados = new ArrayList<>();

        //Começa a execução remota
        try (
                QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceStr, query)) {
            //Coloca timeout do dbpedia
            ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

            //Executa a query
            ResultSet rs = qexec.execSelect();
            //ResultSetFormatter.out(System.out, rs, query);
            while (rs.hasNext()) {
                resultados.add(rs.next().getResource("recursoSubject"));
            }
            qexec.close();
        } catch (Exception ex) {
            log.warn(recursoSubject, ex);
        }
        return resultados;
    }
}