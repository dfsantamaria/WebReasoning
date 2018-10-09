/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.owlapiex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.util.SimpleIRIMapper;

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
         
         IRI pizzaIri=IRI.create("https://protege.stanford.edu/ontologies/pizza/pizza.owl");
         OWLOntology pizzaOntology=manager.loadOntologyFromOntologyDocument(pizzaIri);
                                            
         IRI pizzaOIri= IRI.create(pizzaOntology.getOntologyID().getOntologyIRI().get().toString()+"/pizza.owl"); 
         
         SimpleIRIMapper mapper = new SimpleIRIMapper(IRI.create(pizzaOntology.getOntologyID().getOntologyIRI().get().toString()),pizzaIri);
         manager.getIRIMappers().add(mapper);
                               
         
         OWLImportsDeclaration importsDeclaration=dataFactory.getOWLImportsDeclaration(pizzaIri);
         AddImport addImportAddAxiom= new AddImport(ontology, importsDeclaration);
         manager.applyChange(addImportAddAxiom);        
         Stream<OWLImportsDeclaration> imp= ontology.importsDeclarations();
         
         imp.forEach( streamob -> System.out.println("Imported: "+streamob.getIRI()));
           
         
         OWLClass sicilianPizza= dataFactory.getOWLClass(iri+"SicilianPizza");
        
         OWLDeclarationAxiom oda= dataFactory.getOWLDeclarationAxiom(sicilianPizza);
         
         
         
         ChangeApplied ca= ontology.add(oda);
         if(ca.equals(ca.SUCCESSFULLY))
             System.out.println("ok");
         manager.addAxiom(ontology, oda);
         
         OWLAxiom oax= dataFactory.getOWLSubClassOfAxiom(sicilianPizza, dataFactory.getOWLClass(pizzaOIri+"#Pizza"));
         manager.addAxiom(ontology, oax);
        
         
         ontology.axioms().forEach( ax -> {  System.out.println(ax.toString());        
         
                                           });
           
         OWLObjectProperty prop=dataFactory.getOWLObjectProperty(iri+"pizzaEater");
         OWLObjectPropertyRangeAxiom ax1= 
                 dataFactory.getOWLObjectPropertyRangeAxiom(prop,
                                                                 dataFactory.getOWLObjectUnionOf(
                                                                     dataFactory.getOWLClass(pizzaOIri+"#Pizza"), 
                                                                     dataFactory.getOWLClass(pizzaOIri+"#PizzaTopping"),
                                                                      dataFactory.getOWLClass(pizzaOIri+"#PizzaBase"))                                                                                      
                                                            );
         ontology.add(ax1);
         
         System.out.println("Filtering Axioms");
         List<OWLObjectSomeValuesFrom> out= new ArrayList();         
         Stream<OWLLogicalAxiom> axiomSet= Stream.concat(ontology.logicalAxioms(), pizzaOntology.logicalAxioms());         
         axiomSet.filter(axiom-> axiom.isOfType(AxiomType.SUBCLASS_OF)).forEach((x) ->
              x.getAxiomWithoutAnnotations().components().filter(elem -> (elem instanceof OWLObjectSomeValuesFrom)).forEach(
                      res-> out.add( (OWLObjectSomeValuesFrom)res)));
         
         out.forEach(System.out::println);
                     
               
         
         manager.saveOntology(ontology, new OWLXMLDocumentFormat(), IRI.create(documentFile.toURI()));
       }
  }
