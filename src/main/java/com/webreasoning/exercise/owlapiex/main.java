/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.owlapiex;
import java.io.File;
import java.io.FileNotFoundException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
/**
 *
 * @author Daniele Francesco Santamaria 
 */
public class main
  {
   public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException
    {
       OWLOntologyManager manager= OWLManager.createOWLOntologyManager(); //create the manager    
       OWLOntology ontology=manager.loadOntologyFromOntologyDocument(new File("ontologie/E1G1.owl"));
    /* String iri="http://www.w3.org/2009/08/skos-reference/skos.rdf";
     IRI docIRI = IRI.create(iri); 
     OWLOntology ontology = manager.loadOntologyFromOntologyDocument(docIRI);*/
    }
  }
