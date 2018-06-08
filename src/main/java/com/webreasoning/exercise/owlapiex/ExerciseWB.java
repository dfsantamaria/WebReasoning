/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.owlapiex;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 *
 * @author danie
 */
public class ExerciseWB 
  {
     public static void main (String[]args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException 
       {
         OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
         File documentFile= new File("ontologie/ontologyExample.owl");
         documentFile.createNewFile();
         IRI iri= IRI.create("http://www.dmi.webreasoning.execitation.owl#");
         OWLOntologyID id=new OWLOntologyID( iri, IRI.create(iri+"/1.0"));
         OWLOntology ontology=manager.createOntology(id);
         
         //OWLDataFactory dataFactory=manager.getOWLDataFactory();
        
         OWLDataFactory dataFactory=ontology.getOWLOntologyManager().getOWLDataFactory();
         
         OWLImportsDeclaration importsDeclaration=dataFactory.getOWLImportsDeclaration(IRI.create("https://protege.stanford.edu/ontologies/pizza/pizza.owl"));
         AddImport addImportAddAxiom= new AddImport(ontology, importsDeclaration);
         manager.applyChange(addImportAddAxiom);        
         manager.saveOntology(ontology, new OWLXMLDocumentFormat(), IRI.create(documentFile.toURI()));
         
         Stream<OWLImportsDeclaration> imp= ontology.importsDeclarations();
         imp.forEach(streamOb -> System.out.println(streamOb.getIRI()));
         
       }
  }
