/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.owlapiex;


import java.io.File;
import java.io.IOException;
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
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
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
public class Example
  {
    public static void main (String [] args) throws IOException, OWLOntologyCreationException, ParserConfigurationException, SAXException, OWLOntologyStorageException
     {
        OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        File documentFile= new File("ontologie/ontologyFromXML.owl");
        documentFile.createNewFile();
        IRI iri= IRI.create("http://www.dmi.webreasoning.xmlexecitation.owl#");
        OWLOntologyID id=new OWLOntologyID( iri, IRI.create(iri+"/1.0"));
        OWLOntology ontology=manager.createOntology(id);
        OWLDataFactory dataFactory=ontology.getOWLOntologyManager().getOWLDataFactory();
        
        File inputFile = new File("input/input.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        
         NodeList nList = doc.getElementsByTagName("student");
        
         OWLClass student= dataFactory.getOWLClass(iri+"Student");  
         OWLDataProperty hasName=dataFactory.getOWLDataProperty(iri+"hasName");
         OWLDataProperty hasSurname=dataFactory.getOWLDataProperty(iri+"hasSurname");
         OWLDataProperty hasNickname=dataFactory.getOWLDataProperty(iri+"hasSurname");
         OWLDataProperty hasMark=dataFactory.getOWLDataProperty(iri+"hasMark");
         OWLDataProperty hasRollNum=dataFactory.getOWLDataProperty(iri+"hasRollNum");
         
         manager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(student));
         manager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(hasName, OWL2Datatype.XSD_STRING));
         manager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(hasSurname, OWL2Datatype.XSD_STRING));
         manager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(hasNickname, OWL2Datatype.XSD_STRING));
         manager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(hasMark, OWL2Datatype.XSD_INT));
         manager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(hasRollNum, OWL2Datatype.XSD_INT));
         
         
         
        
        for (int temp = 0; temp < nList.getLength(); temp++)
         {
            Node nNode = nList.item(temp);
            //System.out.println("\nCurrent Element :" + nNode.getNodeName());
            
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
             {
                Element eElement = (Element) nNode;
                String name=eElement.getElementsByTagName("firstname").item(0).getTextContent();
                String surname=eElement.getElementsByTagName("lastname").item(0).getTextContent();
                String rollno=eElement.getAttribute("rollno");
                String nickname= eElement.getElementsByTagName("nickname").item(0).getTextContent();
                String mark=eElement.getElementsByTagName("marks").item(0).getTextContent();                
                OWLNamedIndividual ind= dataFactory.getOWLNamedIndividual(IRI.create(iri+"#"+name+"_"+surname+"_"+nickname));
                manager.addAxiom(ontology, dataFactory.getOWLClassAssertionAxiom(student,ind));
                manager.addAxiom(ontology, dataFactory.getOWLDataPropertyAssertionAxiom(hasName, ind, dataFactory.getOWLLiteral(name, OWL2Datatype.XSD_STRING)));
                manager.addAxiom(ontology, dataFactory.getOWLDataPropertyAssertionAxiom(hasSurname, ind, dataFactory.getOWLLiteral(surname, OWL2Datatype.XSD_STRING)));
                manager.addAxiom(ontology, dataFactory.getOWLDataPropertyAssertionAxiom(hasNickname, ind, dataFactory.getOWLLiteral(nickname, OWL2Datatype.XSD_STRING)));
                manager.addAxiom(ontology, dataFactory.getOWLDataPropertyAssertionAxiom(hasRollNum, ind, dataFactory.getOWLLiteral(rollno, OWL2Datatype.XSD_INT)));                
                manager.addAxiom(ontology, dataFactory.getOWLDataPropertyAssertionAxiom(hasMark, ind, dataFactory.getOWLLiteral(mark, OWL2Datatype.XSD_INT)));
                
                
               
               
             }
             
         }
        manager.saveOntology(ontology, IRI.create(documentFile.toURI()));
     }
  }
