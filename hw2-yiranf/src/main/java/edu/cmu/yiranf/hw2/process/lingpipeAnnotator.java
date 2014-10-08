package edu.cmu.yiranf.hw2.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.yiranf.hw2.types.CandidateToken;
import edu.cmu.yiranf.hw2.types.GeneType;
import edu.cmu.yiranf.hw2.util.intPair;

/**
 * geneDetectorAnnotator works as the analysis engine of the system. It only accepts the context of the sentence
 * and return the position of each gene name entities.
 * @author fyr
 *
 */
public class lingpipeAnnotator extends JCasAnnotator_ImplBase {
  lingpipeDetector ner;
  
  public lingpipeAnnotator() {
    ner = new lingpipeDetector();
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
    
    Map<intPair, Double> abnerTokens = new HashMap<intPair, Double>();
    Iterator annotationIter = aJCas.getAnnotationIndex(CandidateToken.type).iterator();
    while (annotationIter.hasNext()) {
      CandidateToken annot = (CandidateToken) annotationIter.next();
      abnerTokens.put(new intPair(annot.getSt(), annot.getEd()), 1.0);
    }

    try {
      Map<intPair, Double> tokens = ner.detect(sts);
      
      Iterator it = tokens.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<intPair, Double> pairs = (Map.Entry<intPair, Double>)it.next();
        
        double conf = pairs.getValue();
        if (abnerTokens.get((intPair)pairs.getKey()) != null) {
          conf += abnerTokens.get((intPair)pairs.getKey());
        }
        else {
          conf -= 0.1;
        }
        
        if (conf < 0.63) continue;
        
        GeneType annotation = new GeneType(aJCas);
        annotation.setSt(pairs.getKey().getX());
        annotation.setEd(pairs.getKey().getY());
        annotation.addToIndexes();
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
