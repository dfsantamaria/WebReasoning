package com.webreasoning.code.owlapiex;
import java.io.File;
import java.io.FileNotFoundException;    
import openllet.owlapi.OpenlletReasoner;
import openllet.owlapi.OpenlletReasonerFactory;

import org.semanticweb.HermiT.Configuration;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.Reasoner.ReasonerFactory;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
/**
 *
 * @author Daniele Francesco Santamaria 
 */
public class Pellet
  {
   public static void main(String[] args) throws FileNotFoundException, OWLOntologyCreationException, OWLOntologyStorageException
     {
       OWLOntologyManager manager= OWLManager.createOWLOntologyManager(); //create the manager    
       OWLOntology ontology=manager.loadOntologyFromOntologyDocument(new File("ontologie/E1G1.owl"));
       
    /* String iri="http://www.w3.org/2009/08/skos-reference/skos.rdf";
     IRI docIRI = IRI.create(iri); 
     OWLOntology ontology = manager.loadOntologyFromOntologyDocument(docIRI);*/
   
    /*
    OWLDataFactory datafact=manager.getOWLDataFactory();
    Configuration config= new Configuration();
    Reasoner Pellet= new Reasoner(config, ontology);
    Pellet.classifyClasses();
    Pellet.classifyDataProperties();
    Pellet.classifyObjectProperties();
    System.out.println(Pellet.isConsistent());  */
    
    
    OpenlletReasoner reasonerP = OpenlletReasonerFactory.getInstance().createReasoner(ontology);    
    reasonerP.precomputeInferences();
    System.out.println(reasonerP.isConsistent());
    reasonerP.getKB().realize();
    reasonerP.getKB().printClassTree();
    
    InferredOntologyGenerator iog = new InferredOntologyGenerator(reasonerP);    
    OWLOntology inferredOntology = manager.createOntology(IRI.create("http://www.dmi.unict.it/webreasoning/2017/exercise/1G1InfPellet"));   
    iog.fillOntology(manager.getOWLDataFactory(), inferredOntology);
    System.out.println("Axioms: "+ontology.getAxiomCount());     
    System.out.println("Inferred Axioms: "+inferredOntology.getAxiomCount());
    File infFile=new File("ontologie/E1G1_inf.owl");
    manager.saveOntology(inferredOntology, new OWLXMLDocumentFormat(), IRI.create(infFile.toURI()));
    
      
    
  }
  }
