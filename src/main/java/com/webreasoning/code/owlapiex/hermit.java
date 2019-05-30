/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.code.owlapiex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.semanticweb.HermiT.Configuration;
import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.InferredAxiomGenerator;
import org.semanticweb.owlapi.util.InferredClassAssertionAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDataPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredDisjointClassesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentDataPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredEquivalentObjectPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredIndividualAxiomGenerator;
import org.semanticweb.owlapi.util.InferredInverseObjectPropertiesAxiomGenerator;
import org.semanticweb.owlapi.util.InferredObjectPropertyCharacteristicAxiomGenerator;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.InferredSubClassAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubDataPropertyAxiomGenerator;
import org.semanticweb.owlapi.util.InferredSubObjectPropertyAxiomGenerator;

/**
 *
 * @author Daniele Francesco Santamaria
 */
public class hermit
  {
    public static void main(String[] args) throws Exception {
    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File("ontologie/E1G1.owl"));
    OWLDataFactory df = manager.getOWLDataFactory();
       
    
    Configuration configuration = new Configuration();
    configuration.ignoreUnsupportedDatatypes = true;  
    ReasonerFactory rf = new ReasonerFactory();

    OWLReasoner reasoner = rf.createReasoner(ontology, configuration);
    boolean consistencyCheck = reasoner.isConsistent(); 
    if (consistencyCheck) 
      { 
        reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY,
        InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_HIERARCHY,
        InferenceType.DATA_PROPERTY_HIERARCHY, InferenceType.OBJECT_PROPERTY_ASSERTIONS);

        List<InferredAxiomGenerator<? extends OWLAxiom>> generators = new ArrayList<>();
        generators.add(new InferredSubClassAxiomGenerator());
        generators.add(new InferredClassAssertionAxiomGenerator());
        generators.add(new InferredDataPropertyCharacteristicAxiomGenerator());
        generators.add(new InferredEquivalentClassAxiomGenerator());
        generators.add(new InferredEquivalentDataPropertiesAxiomGenerator());
        generators.add(new InferredEquivalentObjectPropertyAxiomGenerator());
        generators.add(new InferredInverseObjectPropertiesAxiomGenerator());
        generators.add(new InferredObjectPropertyCharacteristicAxiomGenerator());

        // NOTE: InferredPropertyAssertionGenerator significantly slows down
        // inference computation
        generators.add(new org.semanticweb.owlapi.util.InferredPropertyAssertionGenerator());
        generators.add(new InferredSubClassAxiomGenerator());
        generators.add(new InferredSubDataPropertyAxiomGenerator());
        generators.add(new InferredSubObjectPropertyAxiomGenerator());        
        
        List<InferredIndividualAxiomGenerator<? extends OWLIndividualAxiom>> individualAxioms = new ArrayList<>();
        generators.addAll(individualAxioms);
        generators.add(new InferredDisjointClassesAxiomGenerator());
        
        InferredOntologyGenerator iog = new InferredOntologyGenerator(reasoner, generators);
        OWLOntology inferredAxiomsOntology = manager.createOntology(IRI.create("http://www.dmi.unict.it/webreasoning/2017/exercise/1G1InfHermit"));
        iog.fillOntology(df, inferredAxiomsOntology);        
        System.out.println("Axioms: "+ontology.getAxiomCount());     
        System.out.println("Inferred Axioms: "+inferredAxiomsOntology.getAxiomCount());
        File infFile=new File("ontologie/E1G1_infHermit.owl");
        manager.saveOntology(inferredAxiomsOntology, new OWLXMLDocumentFormat(), IRI.create(infFile.toURI()));
    } // End if consistencyCheck
    else {
        System.out.println("Inconsistent input Ontology, Please check the OWL File");
    }
}
  }
