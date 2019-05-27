/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.code.jena;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/**
 *
 * @author Daniele Francesco Santamaria
 */
public class Married {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // TODO code application logic here
        
        String mr3URI = "http://mr3.sourceforge.net#"; // URI da utilizzare per le risorse dell'ontologia. 
                                          
        String inputFileName= "ontologie"+File.separator+"MarriedWJena2.rdf";
        
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
        // use the FileManager to find the input file
        InputStream in = FileManager.get().open(inputFileName);
         if (in == null) {
                    throw new IllegalArgumentException(
                                 "File: " + inputFileName + " not found");
         }
         // read the RDF/XML file
         model.read(in, null);
         // write it to standard out
         
         model.write(System.out);
         //Valutiamo la seguente query SPARQL
         in.close();
         //Valutiamo la seguente query SPARQL
         System.out.println("Esecuzione della seconda query.");
         String queryString  = "PREFIX mr3: <"+mr3URI+">"+
                               "PREFIX rdfs: <"+RDFS.getURI()+">"+
                               "PREFIX rdf: <"+RDF.getURI()+">"+
                               "CONSTRUCT {mr3:hasMaidenName rdfs:domain ?C."+
                                         "?individual rdf:type ?C}" +
                              "WHERE {?individual mr3:hasMaidenName ?name."+
                                       "mr3:hasMaidenName rdfs:domain ?D." +
                                       "?D rdfs:subClassOf ?C ."
                              + "}" ;
         
          
         Query query = QueryFactory.create(queryString); //Creiamo la query.         
         QueryExecution qexec = QueryExecutionFactory.create(query, model);    
         
         Model m = qexec.execConstruct();  
         System.out.println("Risultato di construct = " + m.toString());
         
//       
         System.out.println("Risultato di construct = " + m.toString());
         model.close();
//         qexec.close(); 
    }
    
}
