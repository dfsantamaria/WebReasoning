/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.owlapiex;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.stream.Stream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.SimpleIRIMapper;
/**
 *
 * @author Daniele Francesco Santamaria 
 */
public class IRIMAP
  {
   public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException, IOException
    {      
      OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
      File output =  new File("ontologie/localOntology.owl"); 
      IRI documentIRI = IRI.create(output);
      IRI remoteIRI=IRI.create("https://protege.stanford.edu/ontologies/pizza/pizza.owl");
      SimpleIRIMapper mapper = new SimpleIRIMapper(remoteIRI, documentIRI);
      manager.getIRIMappers().add(mapper);
      OWLOntology ontology = manager.loadOntology(remoteIRI);
      manager.saveOntology(ontology,  new OWLXMLDocumentFormat());
     
    }
  }
