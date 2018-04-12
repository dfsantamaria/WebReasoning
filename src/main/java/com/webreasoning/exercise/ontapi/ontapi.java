/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.ontapi;

import java.io.File;
import java.io.FileNotFoundException;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import ru.avicomp.ontapi.OntManagers;
import ru.avicomp.ontapi.OntologyModel;

/**
 *
 * @author danie
 */
public class ontapi
  {
    /**
 *
 * @author Daniele Francesco Santamaria 
 */
   public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException
     {
         /*
            From OWL-API to Jena
         */
        OWLOntologyManager manager = OntManagers.createONT();   
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLOntology ontology=manager.loadOntologyFromOntologyDocument(new File("ontologies/esempio.owl"));
       
    /* String iri="http://www.w3.org/2009/08/skos-reference/skos.rdf";
     IRI docIRI = IRI.create(iri); 
     OWLOntology ontology = manager.loadOntologyFromOntologyDocument(docIRI);*/
    
    OpenlletReasoner reasonerP = OpenlletReasonerFactory.getInstance().createReasoner(ontology);    
    reasonerP.precomputeInferences();
    System.out.println(reasonerP.isConsistent());
    reasonerP.getKB().realize();
    reasonerP.getKB().printClassTree();
    /*
      We now convert the Jena Model
    */
    System.out.println("JENA");
    Model model = ((OntologyModel)ontology).asGraphModel();   
   
    /*
     SPARQL
    */
     try (QueryExecution qexec = QueryExecutionFactory.create(QueryFactory
            .create("SELECT ?x ?y WHERE { ?x ?z ?y }"), model)) {
        ResultSet res = qexec.execSelect();
        while (res.hasNext()) {
            System.out.println(res.next());
        }
    }
           
  }
}
  
