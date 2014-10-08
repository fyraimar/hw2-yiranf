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
package edu.cmu.yiranf.hw2.process;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import edu.cmu.yiranf.hw2.types.Sentence;

/**
 * The collectionReader reads the original text data from input file.
 * 
 * When required, it sends one line to the pipeline:
 * 1. Split the original line and send the line id directly to the consumer.
 * 2. Send the line context without the id to the annotator.
 * 
 * In order to reduce the I/O time, this class read the whole input file and store every line in a List.
 * When there is a request, it checks whether there's remaining line in the list, then put the next line
 * to the cas object if available.
 * 
 * @author fyr
 *
 */
public class CollectionReader extends CollectionReader_ImplBase {
  public static final String INPUT_PATH = "InputDirectory";
  private ArrayList<String> mSentences;
  private int mCurrentIndex;

  public void initialize() throws ResourceInitializationException {
    mSentences = new ArrayList<String>();
    BufferedReader br;
    
    try {
      br = new BufferedReader(new FileReader(((String) getConfigParameterValue(INPUT_PATH))));
      String line = br.readLine();
      while (line != null) {
        //System.out.println(mCurrentIndex);
        mSentences.add(line);
        line = br.readLine();
      }
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    mCurrentIndex = 0;
  }

  public boolean hasNext() {
    return mCurrentIndex < mSentences.size();
  }

  public void getNext(CAS aCAS) throws IOException, CollectionException {
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new CollectionException(e);
    }

    //Store information for Annotator
    String text = mSentences.get(mCurrentIndex++);
    jcas.setDocumentText(text.substring(15, text.length()));
    
    //Store information for consumer
    Sentence st = new Sentence(jcas);
    st.setSentenceId(text.substring(0, 14));
    st.setSentence(text.substring(15, text.length()));
    st.addToIndexes();
  }

  public void close() throws IOException {
  }

  public Progress[] getProgress() {
    return new Progress[] { new ProgressImpl(mCurrentIndex, mSentences.size(), Progress.ENTITIES) };
  }

  public int getNumberOfDocuments() {
    return mSentences.size();
  }

}
