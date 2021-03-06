/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webreasoning.code.jena;

import java.io.File;
import java.io.InputStream;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

/**
 *
 * @author Daniele Francesco Santamaria
 */
public class jenaExample2
  {
    public static void main(String[] args)
      {
        // create an empty model
        Model model = ModelFactory.createDefaultModel();
        // use the FileManager to find the input file
        String inputFileName="ontologie"+File.separator+"esempioJ.rdf";
        InputStream in = FileManager.get().open(inputFileName);
         if (in == null) {
                    throw new IllegalArgumentException(
                                 "File: " + inputFileName + " not found");
      }
      // read the RDF/XML file
      model.read(in, null);
      // write it to standard out
      model.write(System.out);
      }
  }
