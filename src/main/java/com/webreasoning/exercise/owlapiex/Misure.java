/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.exercise.owlapiex;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
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
public class Misure
  {
    public static void main (String [] args) throws IOException, OWLOntologyCreationException, ParserConfigurationException, SAXException, OWLOntologyStorageException
     {
        OWLOntologyManager manager=OWLManager.createOWLOntologyManager();
        OWLOntology ontology=manager.loadOntologyFromOntologyDocument(new File("ontologie/reperti-out-diametro.owl"));
        File documentFile= new File("ontologie/reperti-out-misure.owl");        
        OWLDataFactory dataFactory=ontology.getOWLOntologyManager().getOWLDataFactory();
        String iri="http://www.dmi.unict.it/ontoceramic/reperti.owl#";
        OWLClass reperto= dataFactory.getOWLClass(iri+"Reperto");
        List<OWLObjectPropertyAssertionAxiom> out= new ArrayList(); 
        ontology.logicalAxioms().filter(axiom-> axiom.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION)).filter( s -> ((OWLObjectPropertyAssertionAxiom)s).getProperty().getNamedProperty().getIRI().getFragment().equals("haDimensione__")).forEach(x->out.add((OWLObjectPropertyAssertionAxiom)x));
       
        PrintStream log = new PrintStream(new FileOutputStream("ontologie/log.txt"));
                
        for(OWLObjectPropertyAssertionAxiom s: out)
          {                
             
             OWLNamedIndividual sub=dataFactory.getOWLNamedIndividual(s.getSubject().toStringID());
             OWLObjectProperty prop=dataFactory.getOWLObjectProperty(s.getProperty().getNamedProperty().toStringID());
             OWLNamedIndividual obj=dataFactory.getOWLNamedIndividual(s.getObject().toStringID());
             
             String value=obj.asOWLNamedIndividual().getIRI().getIRIString();
             value=value.substring(value.indexOf("#")+1);
             
             if(!value.contains("n.d."))
               {
                 
                 if(value.startsWith("cm")||value.startsWith("Cm"))
                   {
                     value=value.substring(2);
                     value=value.toLowerCase();
                     value=value.replace("cm", "");
                     value=value.replace(",",".");
                     
                                          
                     StringTokenizer token=new StringTokenizer(value,"x");
                     //System.out.println(token.countTokens());
                     for(int i=0; i<token.countTokens();i++)
                       {
                           String val=token.nextToken();
                           val=val.replace("s","").replace("i", "");
                           if(val.endsWith("."))
                               val=val.substring(0, val.length()-1);
                           if(val.startsWith("."))
                               val=val.substring(1, val.length());
                           //System.out.println(val);
                           OWLNamedIndividual misurazione;
                           OWLAxiom misAx;
                           String name="";
                           switch (i)
                             {
                                   case 0:  name="Altezza"; break;
                                   case 1:  name="Larghezza"; break;
                                   case 2:  name="Profondità"; break;
                                   case 3: name="Diametro"; break;
                                   default: name="Altro"; break;
                             }     
                           misurazione=dataFactory.getOWLNamedIndividual(sub.getIRI().getIRIString()+"Misurazione"+name);
                           misAx=dataFactory.getOWLObjectPropertyAssertionAxiom( 
                           dataFactory.getOWLObjectProperty(iri+"haMisurazione"), sub, misurazione);
                           ontology.add(misAx);  
                           misAx=dataFactory.getOWLClassAssertionAxiom(dataFactory.getOWLClass(iri+"Misurazione"+name), misurazione);
                           ontology.add(misAx);
                           ontology.add(dataFactory.getOWLObjectPropertyAssertionAxiom(
                              dataFactory.getOWLObjectProperty(iri+"haTipoMisurazione"), misurazione, dataFactory.getOWLNamedIndividual(iri+name.toLowerCase())));
                           ontology.add(dataFactory.getOWLObjectPropertyAssertionAxiom(
                             dataFactory.getOWLObjectProperty(iri+"haUnitàDiMisura"), misurazione, dataFactory.getOWLNamedIndividual(iri+"centimetro")));
 
                           try
                             {
                                   double dval=Double.valueOf(val);
                                   ontology.add(dataFactory.getOWLDataPropertyAssertionAxiom(
                                   dataFactory.getOWLDataProperty(iri+"haValoreMisura"), misurazione, dataFactory.getOWLLiteral(dval)));
                             } 
                            catch (NumberFormatException e)
                              {           
                               log.print(s.getSubject().toStringID()+" ");
                               log.print(s.getObject().toStringID()+" ");
                               log.print(s.getProperty().getNamedProperty().toStringID());
                               log.println("");  
                              }
                           
                       }
                     
                  
                     
                     
                   }
                 else
                   {                     
                     log.print(s.getSubject().toStringID()+ " ");
                     log.print(s.getObject().toStringID()+" ");
                     log.print(s.getProperty().getNamedProperty().toStringID());
                     log.println("");                     
                   }
               }
               OWLEntityRemover entR= new OWLEntityRemover(ontology);
               obj.accept(entR);
               prop.accept(entR);
               dataFactory.getOWLObjectProperty(iri+"haDimensione__").accept(entR);
               dataFactory.getOWLClass(iri+"Dimensione__").accept(entR);
               dataFactory.getOWLClass(iri+"Diametro__").accept(entR);
               manager.applyChanges(entR.getChanges());
               entR.reset();
             
             
             
//             OWLNamedIndividual misurazione=dataFactory.getOWLNamedIndividual(sub.getIRI().getIRIString()+"MisurazioneDiametro");
//             OWLAxiom misAx=dataFactory.getOWLObjectPropertyAssertionAxiom( 
//                       dataFactory.getOWLObjectProperty(iri+"haMisurazione"), sub, misurazione);
//             ontology.add(misAx);
//           
//             misAx=dataFactory.getOWLClassAssertionAxiom(dataFactory.getOWLClass(iri+"MisurazioneDiametro"), misurazione);
//             ontology.add(misAx);
//             
//            ontology.add(dataFactory.getOWLObjectPropertyAssertionAxiom(
//                         dataFactory.getOWLObjectProperty(iri+"haTipoMisurazione"), misurazione, dataFactory.getOWLNamedIndividual(iri+"diametro")));
//             
//            
//             ontology.add(dataFactory.getOWLObjectPropertyAssertionAxiom(
//                         dataFactory.getOWLObjectProperty(iri+"haUnitàDiMisura"), misurazione, dataFactory.getOWLNamedIndividual(iri+"centimetro")));
//             
//            
//             String value=s.getObject().toStringID().trim();
//             value=value.substring(value.indexOf("#")+1);
//            
//             if(value.startsWith("Ø"))
//                 value=value.substring(1);
//             if(value.startsWith("Cm"))
//                 value=value.substring(2);
//             value=value.replace(",",".");
//             if(value.contains("-"))
//                 value=value.substring(value.indexOf("-")+1);           
//             
//             try
//                {
//                   double dval=Double.valueOf(value);
//                   ontology.add(dataFactory.getOWLDataPropertyAssertionAxiom(
//                         dataFactory.getOWLDataProperty(iri+"haValoreMisura"), misurazione, dataFactory.getOWLLiteral(dval)));
//                } 
//              catch (NumberFormatException e)
//                {                   
//                  System.out.print("Error on ");
//                  System.out.println(value);
//                  System.out.println(s.getSubject().toStringID());
//                  System.out.println(s.getObject().toStringID());
//                  System.out.println(s.getProperty().getNamedProperty().toStringID());
//                  System.out.println("---");  
//                }                       
//             

               
          }

        manager.saveOntology(ontology, IRI.create(documentFile.toURI()));
     }
  }
