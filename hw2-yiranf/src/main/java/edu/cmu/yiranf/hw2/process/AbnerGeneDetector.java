package edu.cmu.yiranf.hw2.process;

import abner.*;

public class AbnerGeneDetector {
  Tagger t;
  
  public AbnerGeneDetector() {
    System.out.println("Running abner.");
    t = new Tagger();
  }
  
  public String[] process(String sentence) {
    String[][] ents = t.getEntities(sentence);
    String[] ret = new String[ents[0].length];
    
    for (int i = 0; i < ents[0].length; i++) {
      ret[i] = ents[0][i];
      //System.out.println(ents[0][i]);
    }
    
    return ret;
  }
}
