/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.owlapiex;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.stream.Stream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

/**
 *
 * @author Daniele Francesco Santamaria 
 */
public class main
  {
   public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException
    {
       OWLOntologyManager manager= OWLManager.createOWLOntologyManager(); //create the manager    
       OWLOntology ontology=manager.loadOntologyFromOntologyDocument(new File("ontologie/E1G1.owl"));
       
    /* String iri="http://www.w3.org/2009/08/skos-reference/skos.rdf";
     IRI docIRI = IRI.create(iri); 
     OWLOntology ontology = manager.loadOntologyFromOntologyDocument(docIRI);*/
    OWLDataFactory datafact=manager.getOWLDataFactory();
    /*
     Import Ontology
    */
    OWLImportsDeclaration importDeclaration=datafact.getOWLImportsDeclaration(IRI.create("http://www.w3.org/2009/08/skos-reference/skos.rdf"));    
    manager.applyChange(new AddImport(ontology, importDeclaration));
    /*
      Checks the imports set
    */    
     Stream<OWLImportsDeclaration> impDecs = ontology.importsDeclarations();
     impDecs.forEach(a-> System.out.println(a.getIRI().toString()));  
     
     
      /*Instead of Java Lambdas*/
      System.out.println("Array instead of Stream");
      impDecs = ontology.importsDeclarations(); //build a new producer
      Object[] arr= impDecs.toArray();
      for (Object arr1 : arr)
         {
           System.out.println(((OWLImportsDeclaration) arr1).getIRI().toString());
         }
     
             manager.saveOntology(ontology);     
 
    
    }
  }
