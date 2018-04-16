/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.owlapiex;

import java.io.FileNotFoundException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
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
      OWLClass person = df.getOWLClass(IOR+"#Person");
      OWLDeclarationAxiom da = df.getOWLDeclarationAxiom(person);
      o.add(da); 
      o.logicalAxioms().forEach(System.out::println);
      o.axioms().forEach(System.out::println);       
    }
    
}
