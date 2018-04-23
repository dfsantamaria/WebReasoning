/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.ontapi;

import java.io.File;
import java.io.InputStream;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.RDF;

/**
 *
 * @author danie
 */
public class jenaExample3
  {
    public static void main(String[] args)
      {
        String mr3URI = "http://mr3.sourceforge.net#"; // URI da utilizzare per le risorse dell'ontologia.  
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
        // use the FileManager to find the input file
        //String inputFileName="D:" + File.separator + "Dati" + File.separator + "Documents"  + File.separator + 
                             //"NetBeansProjects"+ File.separator + "ONTAPI"+File.separator+"esempioJ.rdf";
          String inputFileName= "MarriedW.rdf";
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
        System.out.println("Esecuzione della query.");
        String queryString = "PREFIX mr3: <"+mr3URI+">"+
                             "PREFIX rdfs: <"+RDFS.getURI()+">"+
                             "PREFIX rdf: <"+RDF.getURI()+">"+
                             "CONSTRUCT {?individual rdf:type ?class}"+ 
                             "WHERE {?individual mr3:hasMaidenName ?name."+
                                       "mr3:hasMaidenName rdfs:domain ?class." 
                             + "}" ;
        Query query = QueryFactory.create(queryString); //Creiamo la query.
        QueryExecution qexec = QueryExecutionFactory.create(query, model); 
        Model constructModel = qexec.execConstruct(model);
        
        System.out.println("Risultato di construct = " + constructModel.toString());
        System.out.println("Risultato di construct = " + model.toString());
             
      }
  }
