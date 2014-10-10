/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.cmu.yiranf.hw2.consumer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.collection.base_cpm.CasObjectProcessor;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;

import edu.cmu.yiranf.hw2.types.GeneType;
import edu.cmu.yiranf.hw2.types.Sentence;

/**
 * The casPrinter works as the CAS consumer of this system. It receives information from 2 sources:
 * 1. Get sentence id and the original sentences from class collectionReader. 2. Get the start point
 * and end point of the Gene Name from class geneDetectorAnnotator.
 * 
 * casPrinter retrieve the name entities according to the positions provided by the AE engine,
 * delete these extra spaces and print out these information to the output file in specific format.
 * 
 * @author fyr
 *
 */
public class CasPrinter extends CasConsumer_ImplBase implements CasObjectProcessor {
  private File mOutFile;

  private FileWriter mFileWriter;

  public CasPrinter() {
  }

  public void initialize() throws ResourceInitializationException {

    // Create the output file
    mOutFile = new File("hw2-yiranf.out");
    try {
      mFileWriter = new FileWriter(mOutFile);
    } catch (IOException e) {
      throw new ResourceInitializationException(e);
    }
  }

  public synchronized void processCas(CAS aCAS) throws ResourceProcessException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }

    // get the sentence id and context
    Iterator it = jcas.getAnnotationIndex(Sentence.type).iterator();
    String sid = "";
    String originText = "";
    if (it.hasNext()) {
      Sentence st = (Sentence) it.next();
      sid = st.getSentenceId();
      originText = st.getSentence();
    }

    Iterator annotationIter = jcas.getAnnotationIndex(GeneType.type).iterator();
    while (annotationIter.hasNext()) {
      GeneType annot = (GeneType) annotationIter.next();

      // Calculate spaces before the NE
      int cur = 0;
      int countSpc1 = 0;
      while (cur <= (annot.getSt()))
        if (originText.charAt(cur++) == ' ')
          countSpc1++;

      // Calculate spaces inside the NE
      cur = annot.getSt();
      int countSpc2 = 0;
      while (cur < (annot.getEd()))
        if (originText.charAt(cur++) == ' ')
          countSpc2++;

      try {
        // Get the id and gene name entities and output to the file
        mFileWriter.write(sid + "|");
        mFileWriter.write((annot.getSt() - countSpc1) + " "
                + (annot.getEd() - countSpc1 - countSpc2 - 1) + "|");
        mFileWriter.write((originText.substring(annot.getSt(), annot.getEd())) + '\n');
        mFileWriter.flush();
      } catch (IOException e) {
        throw new ResourceProcessException(e);
      }
    }
  }

  public void destroy() {
    if (mFileWriter != null) {
      try {
        mFileWriter.close();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
}
