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
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
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
public class Diametro
  {
    public static void main (String [] args) throws IOException, OWLOntologyCreationException, ParserConfigurationException, SAXException, OWLOntologyStorageException
     {
        OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        OWLOntology ontology=manager.loadOntologyFromOntologyDocument(new File("ontologie/reperti.owl"));
        File documentFile= new File("ontologie/reperti-out.owl");        
        OWLDataFactory dataFactory=ontology.getOWLOntologyManager().getOWLDataFactory();
        String iri="http://www.dmi.unict.it/ontoceramic/reperti.owl#";
        OWLClass reperto= dataFactory.getOWLClass(iri+"Reperto");
        List<OWLObjectPropertyAssertionAxiom> out= new ArrayList(); 
        ontology.logicalAxioms().filter(axiom-> axiom.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION)).filter( s -> ((OWLObjectPropertyAssertionAxiom)s).getProperty().getNamedProperty().getIRI().getFragment().equals("haDiametro__")).forEach(x->out.add((OWLObjectPropertyAssertionAxiom)x));
       // out.forEach(System.out::println);
        
        for(OWLObjectPropertyAssertionAxiom s: out)
          {                
             
             OWLNamedIndividual sub=dataFactory.getOWLNamedIndividual(s.getSubject().toStringID());
             OWLObjectProperty prop=dataFactory.getOWLObjectProperty(s.getProperty().getNamedProperty().toStringID());
             OWLNamedIndividual obj=dataFactory.getOWLNamedIndividual(s.getObject().toStringID());
              
             OWLNamedIndividual misurazione=dataFactory.getOWLNamedIndividual(sub.getIRI().getIRIString()+"MisurazioneDiametro");
             OWLAxiom misAx=dataFactory.getOWLObjectPropertyAssertionAxiom( 
                       dataFactory.getOWLObjectProperty(iri+"haMisurazione"), sub, misurazione);
             ontology.add(misAx);
           
             misAx=dataFactory.getOWLClassAssertionAxiom(dataFactory.getOWLClass(iri+"MisurazioneDiametro"), misurazione);
             ontology.add(misAx);
             
            ontology.add(dataFactory.getOWLObjectPropertyAssertionAxiom(
                         dataFactory.getOWLObjectProperty(iri+"haTipoMisurazione"), misurazione, dataFactory.getOWLNamedIndividual(iri+"diametro")));
             
            
             ontology.add(dataFactory.getOWLObjectPropertyAssertionAxiom(
                         dataFactory.getOWLObjectProperty(iri+"haUnitàDiMisura"), misurazione, dataFactory.getOWLNamedIndividual(iri+"centimetro")));
             
            
             String value=s.getObject().toStringID().trim();
             value=value.substring(value.indexOf("#")+1);
            
             if(value.startsWith("Ø"))
                 value=value.substring(1);
             if(value.startsWith("Cm"))
                 value=value.substring(2);
             value=value.replace(",",".");
             if(value.contains("-"))
                 value=value.substring(value.indexOf("-")+1);           
             
             try
                {
                   double dval=Double.valueOf(value);
                   ontology.add(dataFactory.getOWLDataPropertyAssertionAxiom(
                         dataFactory.getOWLDataProperty(iri+"haValoreMisura"), misurazione, dataFactory.getOWLLiteral(dval)));
                } 
              catch (NumberFormatException e)
                {                   
                  System.out.print("Error on ");
                  System.out.println(value);
                  System.out.println(s.getSubject().toStringID());
                  System.out.println(s.getObject().toStringID());
                  System.out.println(s.getProperty().getNamedProperty().toStringID());
                  System.out.println("---");  
                }                       
             
             OWLEntityRemover entR= new OWLEntityRemover(ontology);
             obj.accept(entR);
             prop.accept(entR);
             dataFactory.getOWLObjectProperty(iri+"haDiametro__").accept(entR);
             manager.applyChanges(entR.getChanges());
             entR.reset();
               
          }
//        
//        File inputFile = new File("input/input.xml");
//        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(inputFile);
//        doc.getDocumentElement().normalize();
//        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
//        
//         NodeList nList = doc.getElementsByTagName("student");
//        
//         OWLClass student= dataFactory.getOWLClass(iri+"Student");  
//         OWLDataProperty hasName=dataFactory.getOWLDataProperty(iri+"hasName");
//         OWLDataProperty hasSurname=dataFactory.getOWLDataProperty(iri+"hasSurname");
//         OWLDataProperty hasNickname=dataFactory.getOWLDataProperty(iri+"hasSurname");
//         OWLDataProperty hasMark=dataFactory.getOWLDataProperty(iri+"hasMark");
//         OWLDataProperty hasRollNum=dataFactory.getOWLDataProperty(iri+"hasRollNum");
//         
//         manager.addAxiom(ontology, dataFactory.getOWLDeclarationAxiom(student));
//         manager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(hasName, OWL2Datatype.XSD_STRING));
//         manager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(hasSurname, OWL2Datatype.XSD_STRING));
//         manager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(hasNickname, OWL2Datatype.XSD_STRING));
//         manager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(hasMark, OWL2Datatype.XSD_INT));
//         manager.addAxiom(ontology, dataFactory.getOWLDataPropertyRangeAxiom(hasRollNum, OWL2Datatype.XSD_INT));
//         
//         
//         
//        
//        for (int temp = 0; temp < nList.getLength(); temp++)
//         {
//            Node nNode = nList.item(temp);
//            //System.out.println("\nCurrent Element :" + nNode.getNodeName());
//            
//            if (nNode.getNodeType() == Node.ELEMENT_NODE)
//             {
//                Element eElement = (Element) nNode;
//                String name=eElement.getElementsByTagName("firstname").item(0).getTextContent();
//                String surname=eElement.getElementsByTagName("lastname").item(0).getTextContent();
//                String rollno=eElement.getAttribute("rollno");
//                String nickname= eElement.getElementsByTagName("nickname").item(0).getTextContent();
//                String mark=eElement.getElementsByTagName("marks").item(0).getTextContent();                
//                OWLNamedIndividual ind= dataFactory.getOWLNamedIndividual(IRI.create(iri+"#"+name+"_"+surname+"_"+nickname));
//                manager.addAxiom(ontology, dataFactory.getOWLClassAssertionAxiom(student,ind));
//                manager.addAxiom(ontology, dataFactory.getOWLDataPropertyAssertionAxiom(hasName, ind, dataFactory.getOWLLiteral(name, OWL2Datatype.XSD_STRING)));
//                manager.addAxiom(ontology, dataFactory.getOWLDataPropertyAssertionAxiom(hasSurname, ind, dataFactory.getOWLLiteral(surname, OWL2Datatype.XSD_STRING)));
//                manager.addAxiom(ontology, dataFactory.getOWLDataPropertyAssertionAxiom(hasNickname, ind, dataFactory.getOWLLiteral(nickname, OWL2Datatype.XSD_STRING)));
//                manager.addAxiom(ontology, dataFactory.getOWLDataPropertyAssertionAxiom(hasRollNum, ind, dataFactory.getOWLLiteral(rollno, OWL2Datatype.XSD_INT)));                
//                manager.addAxiom(ontology, dataFactory.getOWLDataPropertyAssertionAxiom(hasMark, ind, dataFactory.getOWLLiteral(mark, OWL2Datatype.XSD_INT)));
//                
//                
//               
//               
//             }
//             
//         }
        manager.saveOntology(ontology, IRI.create(documentFile.toURI()));
     }
  }
