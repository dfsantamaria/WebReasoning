/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.code.owlapiex;

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
 * @author Daniele Francesco Santamaria
 */
public class ExerciseWB 
  {
     public static void main (String[]args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException 
       {
           
         OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
         //Creanting a new, initial empty, ontology
         File documentFile= new File("ontologie/ontologyExample.owl");
         documentFile.createNewFile();
         IRI iri= IRI.create("http://www.dmi.webreasoning.execitation.owl#");
         OWLOntologyID id=new OWLOntologyID( iri, IRI.create(iri+"/1.0"));
         OWLOntology ontology=manager.createOntology(id);
         
         //OWLDataFactory  
         OWLDataFactory dataFactory=ontology.getOWLOntologyManager().getOWLDataFactory();
         //Alternative: OWLDataFactory dataFactory=manager.getOWLDataFactory();   
         
         IRI pizzaIri=IRI.create("https://protege.stanford.edu/ontologies/pizza/pizza.owl");
         OWLOntology pizzaOntology=manager.loadOntologyFromOntologyDocument(pizzaIri);
           
         //IRI Mapper
         IRI pizzaOntologyIri= IRI.create(pizzaOntology.getOntologyID().getOntologyIRI().get().toString()+"/pizza.owl");          
         SimpleIRIMapper mapper = new SimpleIRIMapper(IRI.create(pizzaOntology.getOntologyID().getOntologyIRI().get().toString()),pizzaIri);
         manager.getIRIMappers().add(mapper);                               
         
         
         
         //Importing Ontologies
         OWLImportsDeclaration importsDeclaration=dataFactory.getOWLImportsDeclaration(pizzaIri);                  
         AddImport addImportAddAxiom= new AddImport(ontology, importsDeclaration);
         manager.applyChange(addImportAddAxiom); 
         //Exploring the import declaration
         Stream<OWLImportsDeclaration> imp= ontology.importsDeclarations();         
         imp.forEach(streamobj -> System.out.println("Imported: "+streamobj.getIRI()));           
         //Manipulating ontologies
         OWLClass sicilianPizza= dataFactory.getOWLClass(iri+"SicilianPizza");        
         OWLDeclarationAxiom oda= dataFactory.getOWLDeclarationAxiom(sicilianPizza);   
         ChangeApplied ca= ontology.add(oda);
         if(ca.equals(ca.SUCCESSFULLY))
             System.out.println("ok");
         manager.addAxiom(ontology, oda);
         
         OWLAxiom oax= dataFactory.getOWLSubClassOfAxiom(sicilianPizza, dataFactory.getOWLClass(pizzaOntologyIri+"#Pizza"));
         manager.addAxiom(ontology, oax);        
         
         ontology.axioms().forEach( ax -> {
                                            System.out.println(ax.toString()); 
                                          });
           
         OWLObjectProperty prop=dataFactory.getOWLObjectProperty(iri+"pizzaEater");
         OWLObjectPropertyRangeAxiom ax1= 
                 dataFactory.getOWLObjectPropertyRangeAxiom(prop,
                                                                 dataFactory.getOWLObjectUnionOf(dataFactory.getOWLClass(pizzaOntologyIri+"#Pizza"), 
                                                                     dataFactory.getOWLClass(pizzaOntologyIri+"#PizzaTopping"),
                                                                      dataFactory.getOWLClass(pizzaOntologyIri+"#PizzaBase"))                                                                                      
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
