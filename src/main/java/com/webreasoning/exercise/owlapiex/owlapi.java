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
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.OWLSubDataPropertyOfAxiom;
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.vocab.OWLFacet;
import static org.semanticweb.owlapi.vocab.OWLFacet.MIN_INCLUSIVE;

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
      OWLClass student = df.getOWLClass(IOR+"#Student");
      OWLDeclarationAxiom da = df.getOWLDeclarationAxiom(person);      
      o.add(da);
      o.add(df.getOWLDeclarationAxiom(student));
      //OWLProperty
      OWLObjectProperty hasFriend= df.getOWLObjectProperty(IOR+"#hasFriend");
      o.add(df.getOWLDeclarationAxiom(hasFriend));
      OWLObjectProperty knows= df.getOWLObjectProperty(IOR+"#knows");
      o.add(df.getOWLDeclarationAxiom(knows));
      //OWLDataProperty
      OWLDataProperty hasName= df.getOWLDataProperty(IOR+"#hasName");
      o.add(df.getOWLDeclarationAxiom(hasName));
      //man.addAxiom(o, da);
      //Individual
      OWLIndividual dsf= df.getOWLNamedIndividual(IOR+"#Daniele");
      o.add(df.getOWLDeclarationAxiom( (OWLEntity) dsf));
      //Class assertion
      o.add(df.getOWLClassAssertionAxiom(person, dsf));
      
      //Annotation
      OWLAnnotation commentP = df.getOWLAnnotation(df.getRDFSComment(), df.getOWLLiteral("Class representing persons", "en"));
      OWLAxiom ax1 = df.getOWLAnnotationAssertionAxiom(person.getIRI(), commentP);
      o.add(ax1);
      //OWLSubclasses
      OWLSubClassOfAxiom sub=df.getOWLSubClassOfAxiom(student, person);
      o.add(sub);
      //OWLSubproperty
      OWLSubObjectPropertyOfAxiom subp=df.getOWLSubObjectPropertyOfAxiom(hasFriend, knows);
      o.add(subp);
      //Domain restriction
      o.add(df.getOWLObjectPropertyDomainAxiom(knows, person));
      //Data property range
      o.add(df.getOWLDataPropertyRangeAxiom(hasName, df.getStringOWLDatatype()));
      OWLDataRange rg= df.getOWLDataOneOf(df.getOWLLiteral("ab"),df.getOWLLiteral("b"));
      //Restrictions
      OWLDataProperty hasAge= df.getOWLDataProperty(IOR+"#hasAge");
      o.add(df.getOWLDeclarationAxiom(hasAge));
      o.add(df.getOWLDataPropertyRangeAxiom(hasAge, df.getIntegerOWLDatatype()));
      OWLLiteral eighteenConstant = df.getOWLLiteral(18);    
      OWLFacet facet = MIN_INCLUSIVE;
      OWLDataRange intGreaterThan18 = df.getOWLDatatypeRestriction(df.getIntegerOWLDatatype(), 
              facet, eighteenConstant);
      OWLClassExpression thingsWithAgeGreaterOrEqualTo18 = df.getOWLDataSomeValuesFrom(hasAge, 
              intGreaterThan18);
      OWLClass adult = df.getOWLClass(IOR + "#Adult");
      OWLClassExpression adultP=df.getOWLObjectIntersectionOf(adult, person);
      OWLSubClassOfAxiom ax = df.getOWLSubClassOfAxiom(adultP, thingsWithAgeGreaterOrEqualTo18);
      o.add(ax);
      
      
      o.logicalAxioms().forEach(System.out::println);
      o.axioms().forEach(System.out::println); 
      //Filtering Ontology
      System.out.println("Filtering ontology");
      o.signature().filter(element -> !element.isBuiltIn() &&
                                      element.getIRI().getRemainder().orElse("").startsWith("P") &&
                                      "Class".equals(element.getEntityType().getName())
                          ).forEach(element ->  System.out.println(element.toStringID()));                                   
                                    
      
    }
    
}
