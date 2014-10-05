package edu.cmu.yiranf.hw2.process;

import java.util.Iterator;
import java.util.Map;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.yiranf.hw2.types.GeneType;

/**
 * geneDetectorAnnotator works as the analysis engine of the system. It only accepts the context of the sentence
 * and return the position of each gene name entities.
 * @author fyr
 *
 */
public class geneDetectorAnnotator extends JCasAnnotator_ImplBase {
  //lingpipeDetector ner;
  abnerGeneDetector abner;
  
  public geneDetectorAnnotator() {
     //ner = new lingpipeDetector();
    abner = new abnerGeneDetector();
  }
  
  /**
   * @param JCas
   * @return void
   * @throws AnalysisEngineProcessException
   * 
   * This function calls the lingpipeDetector to detect the gene name entity.
   * It is the core function in the annotator of this system.
   */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    String sts = aJCas.getDocumentText();  
    String[] tokens = abner.process(sts);
    
    for (String token : tokens) {
      int st = sts.indexOf(token);
      if (st < 0 || st + token.length() > sts.length()) continue;
      GeneType annotation = new GeneType(aJCas);
      System.out.println(sts.indexOf(token));
      annotation.setSt(sts.indexOf(token));
      annotation.setEd(sts.indexOf(token) + token.length());
      
      annotation.addToIndexes();
    }
    /*
    try {
      Map<Integer, Integer> tokens = ner.detect(sts);
      
      Iterator it = tokens.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<Integer, Integer> pairs = (Map.Entry<Integer, Integer>)it.next();
        GeneType annotation = new GeneType(aJCas);

        annotation.setSt((Integer)pairs.getKey());
        annotation.setEd((Integer)pairs.getValue());
        annotation.addToIndexes();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    */
  }
}
