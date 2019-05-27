/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.ontapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.parameters.Imports;
import ru.avicomp.ontapi.OntManagers;
import ru.avicomp.ontapi.OntologyManager;
import ru.avicomp.ontapi.OntologyModel;
import ru.avicomp.ontapi.jena.OntModelFactory;
import ru.avicomp.ontapi.jena.model.OntGraphModel;

/**
 *
 * @author danie
 */
public class ontapi2
  {
    /**
 *
 * @author Daniele Francesco Santamaria 
 */
   public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException
     {    
    /*
      From Jena
    */
        OntologyManager managerJ = OntManagers.createONT();
        OntGraphModel modelJ = OntModelFactory.createModel();
        try 
           {
             InputStream in = FileManager.get().open("ontologies"+File.separator+"esempio.owl");        
             modelJ.read(in, null);   
            } 
         catch (JenaException je) 
              {   System.err.println("Error");        
              }   
    
          OntologyModel ontology = managerJ.addOntology(modelJ.getBaseGraph());
          ontology.axioms(Imports.INCLUDED).forEach(System.out::println);
          OWLOntology owlOnt=(OWLOntology) ontology;
          System.out.println(owlOnt.getAxiomCount());
  }
}