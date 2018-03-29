package com.webreasoning.exercise.owlapiex;
import java.io.File;
import java.io.FileNotFoundException;    

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.HermiT.Reasoner;
/**
 *
 * @author Daniele Francesco Santamaria 
 */
public class query
  {
   public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException
     {
       OWLOntologyManager manager= OWLManager.createOWLOntologyManager(); //create the manager    
       OWLOntology ontology=manager.loadOntologyFromOntologyDocument(new File("ontologie/E1G1.owl"));
       
    /* String iri="http://www.w3.org/2009/08/skos-reference/skos.rdf";
     IRI docIRI = IRI.create(iri); 
     OWLOntology ontology = manager.loadOntologyFromOntologyDocument(docIRI);*/
    OWLDataFactory datafact=manager.getOWLDataFactory();
    Configuration config= new Configuration();
    Reasoner reasoner= new Reasoner(config, ontology);
    reasoner.classifyClasses();
    reasoner.classifyDataProperties();
    reasoner.classifyObjectProperties();
    System.out.println(reasoner.isConsistent());  
    
  }
  }
