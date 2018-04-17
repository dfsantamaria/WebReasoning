/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.owlapiex;

import java.io.FileNotFoundException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 *
 * @author danie
 */
public class owlapi 
{    
   public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException
    {
      IRI IOR = IRI.create("http://dmi.unict.webreasoning/examples");
      OWLOntologyManager man = OWLManager.createOWLOntologyManager();
      OWLOntology o = man.createOntology(IOR);
      OWLDataFactory df = o.getOWLOntologyManager().getOWLDataFactory();
      //OWL Class
      OWLClass person = df.getOWLClass(IOR+"#Person");
      OWLDeclarationAxiom da = df.getOWLDeclarationAxiom(person);      
      o.add(da);
      //OWLProperty
      OWLObjectProperty hasFriend= df.getOWLObjectProperty(IOR+"#hasFriend");
      o.add(df.getOWLDeclarationAxiom(hasFriend));
      //OWLDataProperty
      OWLDataProperty hasName= df.getOWLDataProperty(IOR+"#hasName");
      o.add(df.getOWLDeclarationAxiom(hasName));
      //man.addAxiom(o, da);
      //Individual
      OWLIndividual dsf= df.getOWLNamedIndividual(IOR+"#Daniele");
      o.add(df.getOWLDeclarationAxiom( (OWLEntity) dsf));
      //Annotation
      OWLAnnotation commentP = df.getOWLAnnotation(df.getRDFSComment(), df.getOWLLiteral("Class representing persons", "en"));
      OWLAxiom ax1 = df.getOWLAnnotationAssertionAxiom(person.getIRI(), commentP);
      o.add(ax1);
      
      o.logicalAxioms().forEach(System.out::println);
      o.axioms().forEach(System.out::println);       
    }
    
}
