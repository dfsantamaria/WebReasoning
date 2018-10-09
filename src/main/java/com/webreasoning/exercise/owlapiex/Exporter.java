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
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import uk.ac.manchester.cs.owl.owlapi.OWLClassAssertionAxiomImpl;

/**
 *
 * @author Daniele Santamaria
 */
public class Exporter
  {
    public static void main (String [] args) throws IOException, OWLOntologyCreationException, ParserConfigurationException, SAXException, OWLOntologyStorageException
     {
        OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        manager.loadOntologyFromOntologyDocument(new File("ontologie/reperti-origin.owl"));
        OWLOntology ontology=manager.loadOntologyFromOntologyDocument(new File("ontologie/reperti-catalog.owl"));
                       
        OWLDataFactory dataFactory=ontology.getOWLOntologyManager().getOWLDataFactory();
        String iri="http://www.dmi.unict.it/ontoceramic/reperti.owl#";
        OWLClass reperto= dataFactory.getOWLClass(iri+"Reperto");
        List<OWLIndividual> out= new ArrayList(); 
        ontology.logicalAxioms().filter(axiom-> axiom.isOfType(AxiomType.CLASS_ASSERTION)).filter(t->((OWLClassAssertionAxiom)t).getClassExpression().getClassExpressionType()==ClassExpressionType.OWL_CLASS).filter(d->((OWLClassAssertionAxiom)d).getClassExpression().asOWLClass().getIRI().toString().endsWith("Misurazione")).forEach(s->out.add( ((OWLClassAssertionAxiom)s).getIndividual()));
          // filter(d->((OWLClassAssertionAxiom)d).getClassExpression().asOWLClass().getIRI().getFragment()=="Misurazione")      
        for(OWLIndividual s: out)
             System.out.println(s.toStringID());
               
          

        manager.saveOntology(ontology);
        
     }
  }
