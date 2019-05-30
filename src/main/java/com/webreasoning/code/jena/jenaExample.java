package com.webreasoning.code.jena;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.SomeValuesFromRestriction;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;

//import ru.avicomp.ontapi.jena.vocabulary.RDF;

/**
 *
 * @author Daniele Francesco Santamaria
 */
public class jenaExample
  {

    /**
     *
     * @param args
     */
    public static void main(String[] args)
      {
        
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null); // creazione di un'ontologia OWL 1.1 vuota.        
        OntDocumentManager dm = m.getDocumentManager(); //creazione del gestore del documento ontologico.
        FileManager fm=dm.getFileManager(); //file manager   che gestisce l'accesso alla sorgente fisica.        
        String unictIRI = "http://dmi.unict.it/ontologia#"; // IRI da utilizzare per le risorse dell'ontologia.
         m.setNsPrefix("unict",unictIRI); 
        OntClass studenti = m.createClass( unictIRI + "Studenti"); // Creazione della classe Studenti
        OntClass corsi = m.createClass( unictIRI + "Corsi"); // Creazione della classe Corsi
        ObjectProperty segueCorso = m.createObjectProperty( unictIRI + "segueCorso" ); //Creaazione della OP segueCorso.
        DatatypeProperty haCognome = m.createDatatypeProperty(unictIRI + "haCognome"); //Creazione della DP haCognome.        
        segueCorso.addRange(corsi); //Range di segueCorso
       
        //Restrizione Esistenziale
        SomeValuesFromRestriction rst = m.createSomeValuesFromRestriction( null, segueCorso, corsi); 
        OntClass braviStudenti= m.createClass(unictIRI + "braviStudenti");
        braviStudenti.addSuperClass(rst);
        braviStudenti.addSuperClass(studenti);
        
        Individual dfsantamaria= m.createIndividual(unictIRI + "dfsantamaria", studenti); //Creazione di una istanza di Studenti
        Individual webreasoning= m.createIndividual(unictIRI + "webreasoning", corsi); //Creazione del corso di WebReasoning
        
        m.add(dfsantamaria, haCognome, ResourceFactory.createStringLiteral("Santamaria")); //Asserzione di datatipo
        m.add(dfsantamaria, segueCorso, webreasoning); //Assertione di OP
        
        //Stampiamo a schermo tutti gli statements della base di conoscenza
        for (Iterator it = m.listStatements(); it.hasNext(); )
         {
           Statement currentStatement=(Statement) it.next();
           System.out.println(currentStatement.toString());
         }       
        
        //Salviamo l'ontologia
        FileWriter out = null;
        try 
          {                    
              out = new FileWriter( "ontologie"+File.separator+"esempio-out.owl" );
              m.write( out, "RDF/XML" );
              out.close();
          }
        catch (IOException ignore) 
          {
              System.err.println("Something goes wrong on writing file");
          }
        
        //Valutiamo la seguente query SPARQL
        System.out.println("Esecuzione della query.");
        String queryString = "PREFIX unict: <"+unictIRI+">"+
                             "PREFIX rdf: <"+RDF.getURI()+">"+
                             "SELECT ?individual ?cognome "+ 
                             "WHERE {?individual rdf:type unict:Studenti."+
                                       "?individual unict:haCognome ?cognome." 
                             + "}" ;
        Query query = QueryFactory.create(queryString); //Creiamo la query.
        QueryExecution qexec = QueryExecutionFactory.create(query, m); 
        ResultSet rs = qexec.execSelect() ; //Eseguiamo la query            
        ResultSetFormatter.out(System.out, rs, query); //Stampa formattata dei risultati.    
             /*
                Attenzione. ResultSetFormatter "consuma" l'iteratore dei risultati. 
                Quindi dobbiamo rivalutare la query prima di procedere.
             */
        qexec = QueryExecutionFactory.create(query, m); 
        rs = qexec.execSelect() ; 
        System.out.println("Esecuzione della Query.");//Operiamo con i risultati
        while(rs.hasNext()) //iteriamo sui risultati
         {                
          QuerySolution qs = rs.nextSolution() ;    //la singola soluzione      
          RDFNode nodeRes = qs.get("individual"); //accediamo al nodo RDF match della variabile ?individual
          System.out.println("URI della risorsa trovata: "+nodeRes); //stampiamone l'URI
          
          //oppure accediamo al risultato come oggetto di tipo Resource
          Resource z = (Resource) qs.getResource("individual");
          System.out.println("Nome della risorsa trovata: "+ z.getLocalName());
          //stampiamo il cognome. Operao sul nodo RDFS.         
          nodeRes= qs.get("cognome");
          String resCognome= nodeRes.asLiteral().toString();
          System.out.println("Cognome dello Studente: "+resCognome);                 
         }             
         qexec.close();
         
         UpdateAction.parseExecute(
            "DELETE { ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#AnnotationProperty> } " +
            "INSERT { ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#ObjectProperty> } " +
            "WHERE { ?x <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#AnnotationProperty> }",
            m);
        // System.out.println(VCARD.getURI().toString());
      
      }
    
       
    
    /* Trick on OWL 2 specification
       MaxCardinalityRestriction restriction = model.createMaxCardinalityRestriction(null, existingDataProperty, 1);                                               
       restriction.removeAll(OWL.cardinality);
       restriction.addLiteral(OWL2.maxQualifiedCardinality, 1);
       restriction.addProperty(OWL2.onDataRange, XSD.xstring);
       itemClass.addSuperClass(restriction);
    */
  }
