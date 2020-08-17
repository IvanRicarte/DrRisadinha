/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.Set;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rdfdata.DbpediaConnections;

/**
 *
 * @author irica
 */
public class AddDbpediaConnections {

    static Logger log = LogManager.getRootLogger();

    public static String normalize(String string) {
        String normal = string.replaceAll(" ", "_");
        return normal;
    }

    public static String singular(String string) {
        String single = null;
        if (string.endsWith("ais")) {
            single = string.replaceAll("ais$", "al");
            return single;
        }
        if (string.endsWith("ens")) {
            single = string.replaceAll("ens$", "em");
            return single;
        }
        if (string.endsWith("os")) {
            single = string.replaceAll("os$", "o");
            return single;
        }
        if (string.endsWith("res")) {
            single = string.replaceAll("res$", "r");
            return single;
        }
        if (string.endsWith("s")) {
            single = string.replaceAll("s$", "");
            return single;
        }

        return single;
    }

    public static void main(String[] args) {
        log.info("============= Adding DBpedia labels ==============");
        DbpediaConnections dbpc = new DbpediaConnections();
        Set<RDFNode> subjects = dbpc.getSubjects().toSet();
        log.info("Processing " + subjects.size() + " labels");
        int count = 0;
        for (RDFNode node : subjects) {
            Resource rnode = (Resource) node;
            String label = normalize(dbpc.getLabel(rnode));
            log.info("Search for " + label);
            Resource dbpedia = dbpc.search(label);
            if (dbpedia != null) {
                dbpc.add(rnode, dbpedia);
                ++count;
                log.info(">> Connected to " + dbpedia.getURI());
            } else if (label.endsWith("s")) {
                String slabel = singular(label);
                if (slabel != null) {
                    log.info(".. Search again for " + slabel);
                    dbpedia = dbpc.search(slabel);
                    if (dbpedia != null) {
                        dbpc.add(rnode, dbpedia);
                        ++count;
                        log.info(">> Connected to " + dbpedia.getURI());
                    } else {
                        log.info("-- No match");
                    }
                } else {
                    log.info("-- No match");
                }
            } else {
                log.info("-- No match");
            }
        }
        log.info("============= Connected " + count + " DBpedia labels ===============");
    }
}
