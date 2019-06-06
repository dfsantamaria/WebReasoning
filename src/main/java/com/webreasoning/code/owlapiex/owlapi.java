/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.code.owlapiex;

import java.io.FileNotFoundException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import static org.semanticweb.owlapi.model.AxiomType.CLASS_ASSERTION;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
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
import org.semanticweb.owlapi.model.OWLSubObjectPropertyOfAxiom;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.vocab.OWLFacet;
import static org.semanticweb.owlapi.vocab.OWLFacet.MIN_INCLUSIVE;

/**
 *
 * @author Daniele Francesco Santamaria
 */
public class owlapi 
{    
   public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException
    {
      IRI IOR = IRI.create("http://dmi.unict.webreasoning/examples.owl");
      OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
      OWLOntology o = manager.createOntology(IOR);
      OWLDataFactory datafactory = o.getOWLOntologyManager().getOWLDataFactory();
      //OWL Class
      OWLClass person = datafactory.getOWLClass(IOR+"#Person");
      OWLClass student = datafactory.getOWLClass(IOR+"#Student");
      // Entities declaration
      OWLDeclarationAxiom declaration = datafactory.getOWLDeclarationAxiom(person);      
      ChangeApplied changes=o.add(declaration);
      if(changes==ChangeApplied.SUCCESSFULLY)
          System.out.println("\n Declaration added: " + declaration.toString()+"\n");
      o.add(datafactory.getOWLDeclarationAxiom(student));
      
      
      //OWLProperty
      OWLObjectProperty hasFriend= datafactory.getOWLObjectProperty(IOR+"#hasFriend");
      o.add(datafactory.getOWLDeclarationAxiom(hasFriend));
      OWLObjectProperty knows= datafactory.getOWLObjectProperty(IOR+"#knows");
      o.add(datafactory.getOWLDeclarationAxiom(knows));
      //OWLDataProperty
      OWLDataProperty hasName= datafactory.getOWLDataProperty(IOR+"#hasName");
      o.add(datafactory.getOWLDeclarationAxiom(hasName));
      //man.addAxiom(o, declaration);
      //Individual
      OWLIndividual dsf= datafactory.getOWLNamedIndividual(IOR+"#Daniele");
      o.add(datafactory.getOWLDeclarationAxiom( (OWLEntity) dsf));
      //Class assertion
      o.add(datafactory.getOWLClassAssertionAxiom(person, dsf));
      
      //Annotation
      OWLAnnotation commentP = datafactory.getOWLAnnotation(datafactory.getRDFSComment(), datafactory.getOWLLiteral("Class representing persons", "en"));
      OWLAxiom ax1 = datafactory.getOWLAnnotationAssertionAxiom(person.getIRI(), commentP);
      o.add(ax1);
      //OWLSubclasses
      OWLSubClassOfAxiom sub=datafactory.getOWLSubClassOfAxiom(student, person);
      o.add(sub);
      //OWLSubproperty
      OWLSubObjectPropertyOfAxiom subp=datafactory.getOWLSubObjectPropertyOfAxiom(hasFriend, knows);
      o.add(subp);
      //Domain restriction
      o.add(datafactory.getOWLObjectPropertyDomainAxiom(knows, person));
      //Data property range
      o.add(datafactory.getOWLDataPropertyRangeAxiom(hasName, datafactory.getStringOWLDatatype()));
      OWLDataRange rg= datafactory.getOWLDataOneOf(datafactory.getOWLLiteral("ab"),datafactory.getOWLLiteral("b"));
      //Restrictions
      OWLDataProperty hasAge= datafactory.getOWLDataProperty(IOR+"#hasAge");
      o.add(datafactory.getOWLDeclarationAxiom(hasAge));
      o.add(datafactory.getOWLDataPropertyRangeAxiom(hasAge, datafactory.getIntegerOWLDatatype()));
      OWLLiteral eighteenConstant = datafactory.getOWLLiteral(18);    
      OWLFacet facet = MIN_INCLUSIVE;
      OWLDataRange intGreaterThan18 = datafactory.getOWLDatatypeRestriction(datafactory.getIntegerOWLDatatype(), 
              facet, eighteenConstant);
      OWLClassExpression thingsWithAgeGreaterOrEqualTo18 = datafactory.getOWLDataSomeValuesFrom(hasAge, 
              intGreaterThan18);
      OWLClass adult = datafactory.getOWLClass(IOR + "#Adult");
      OWLClassExpression adultP=datafactory.getOWLObjectIntersectionOf(adult, person);
      OWLSubClassOfAxiom ax = datafactory.getOWLSubClassOfAxiom(adultP, thingsWithAgeGreaterOrEqualTo18);
      o.add(ax);
        
      //AddAxiom addAxiom=new AddAxiom(o,ax);
      //addAxiom.isAddAxiom();
      
      o.logicalAxioms().forEach(System.out::println);
      o.axioms().forEach(System.out::println); 
      //Filtering Ontology
      System.out.println("Filtering ontology");
      o.signature().filter(element -> !element.isBuiltIn() &&
                                      element.getIRI().getRemainder().orElse("").startsWith("P") &&
                                      "Class".equals(element.getEntityType().getName())
                          ).forEach(element ->  System.out.println(element.toStringID()));   
      
      System.out.println("Filtering axioms");                               
      o.logicalAxioms().filter(element -> element.isOfType(CLASS_ASSERTION)
                               ).forEach(element ->  System.out.println(((OWLClassAssertionAxiom) element).getIndividual()));   

      System.out.println("Filtering axioms");                               
      o.logicalAxioms().filter(element -> element.isOfType(CLASS_ASSERTION)
                               ).forEach(element -> ((((OWLClassAssertionAxiom) element)).componentsWithoutAnnotations()
                                       ).forEach(el -> System.out.println(el.toString())));
                 
      System.out.println("Filtering axioms");                               
      o.logicalAxioms().filter(element -> element.isOfType(CLASS_ASSERTION)
                               ).forEach(element -> ((((OWLClassAssertionAxiom) element)).componentsWithoutAnnotations()
                                       ).filter(elem -> (elem instanceof OWLClass)).forEach(el -> System.out.println(((OWLClass) el).toString())));
      
      System.out.println("Filtering axioms");                               
      o.axioms(dsf).forEach(element ->  System.out.println(element));
      
      System.out.println("Filtering axioms");                               
      o.axioms(CLASS_ASSERTION).forEach(element ->  System.out.println(element.getClassExpression().asOWLClass().toString()));
      
      
      OWLIndividual ind2= datafactory.getOWLNamedIndividual(IOR+"#Frank"); 
      AddAxiom axiomChange = new AddAxiom(o, datafactory.getOWLDeclarationAxiom( (OWLEntity) ind2));
      if(axiomChange.isAxiomChange() &&  axiomChange.isAddAxiom() &&
              manager.applyChange(axiomChange)== ChangeApplied.SUCCESSFULLY)
          System.out.println("Axiom added successfully");      
      
    }
    
}
