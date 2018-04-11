/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.ontapi;

import java.io.File;
import java.io.FileNotFoundException;
import openllet.owlapi.PelletReasonerFactory;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import ru.avicomp.ontapi.OntManagers;

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
        OWLOntologyManager manager = OntManagers.createONT();   
        OWLDataFactory factory = manager.getOWLDataFactory();
        OWLOntology ontology=manager.loadOntologyFromOntologyDocument(new File("ontologies/esempio.owl"));
       
    /* String iri="http://www.w3.org/2009/08/skos-reference/skos.rdf";
     IRI docIRI = IRI.create(iri); 
     OWLOntology ontology = manager.loadOntologyFromOntologyDocument(docIRI);*/
   
    OWLReasonerFactory reasonerFactoryP = PelletReasonerFactory.getInstance();
    OWLReasoner reasonerP = reasonerFactoryP.createReasoner(ontology, new SimpleConfiguration());    
    reasonerP.precomputeInferences();
    System.out.println(reasonerP.isConsistent());
   
  }
  }
