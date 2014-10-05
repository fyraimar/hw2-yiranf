/*
 * LingPipe v. 4.1.0
 * Copyright (C) 2003-2011 Alias-i
 *
 * This program is licensed under the Alias-i Royalty Free License
 * Version 1 WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Alias-i
 * Royalty Free License Version 1 for more details.
 *
 * You should have received a copy of the Alias-i Royalty Free License
 * Version 1 along with this program; if not, visit
 * http://alias-i.com/lingpipe/licenses/lingpipe-license-1.txt or contact
 * Alias-i, Inc. at 181 North 11th Street, Suite 401, Brooklyn, NY 11211,
 * +1 (718) 290-9170.
 */
package edu.cmu.yiranf.hw2.process;

import com.aliasi.chunk.Chunk;

import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class wraps the lingpipe detector under the Alias-i Royalty Free License.
 * 
 * It implements a method that can detect the gene names in the sentence using the linepipe's api and a gene name directory.
 * The model file is stored in "src/main/resources/data/ne-en-bio-genetag.HmmChunker".
 * @author fyr
 *
 */
public class lingpipeDetector {
  static File modelFile;
  static ConfidenceChunker chunker;
  
  public lingpipeDetector() {
    modelFile = new File("src/main/resources/data/ne-en-bio-genetag.HmmChunker");
    try {
      chunker = (ConfidenceChunker) AbstractExternalizable.readObject(modelFile);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /**
   * 
   * @param String text
   * @return Map<Integer, Integer>
   * @throws Exception
   * 
   * This function calls the Linepipe Api to detect the gene names in a given String.
   * It returns a map containing pairs of start and end positions of each gene name.
   * The algorithm compare the differences between the name in the sentence and the gene type model and get a confidence value.
   * This function outputs entities whose confidence values are above log(0.63). This specific value has the best performance during the test.
   */
  public Map<Integer, Integer> detect(String text) throws Exception {
    Map<Integer, Integer> tokens = new HashMap<Integer, Integer>();
    
    char[] cs = text.toCharArray();
    Iterator<Chunk> it = chunker.nBestChunks(cs, 0, cs.length, 80);
    while (it.hasNext()) {
      Chunk chunk = it.next();
      double conf = Math.pow(2.0,chunk.score());
      int start = chunk.start();
      int end = chunk.end();
      if (conf > 0.63) {
        tokens.put(start, end);
      }
    }
    
    return tokens;
  }
}
