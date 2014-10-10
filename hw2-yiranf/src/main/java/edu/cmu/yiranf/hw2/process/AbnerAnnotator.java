package edu.cmu.yiranf.hw2.process;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.yiranf.hw2.types.CandidateToken;
import edu.cmu.yiranf.hw2.types.GeneType;

/**
 * AbnerAnnotator works as the first analysis engine of the system. It only accepts the context of the sentence
 * and return an array of tokens 
 * @author fyr
 *
 */
public class AbnerAnnotator extends JCasAnnotator_ImplBase {
  static final int PROCESS_ID = 1;
  static final double ABNER_SCORE = 0.2;
  
  AbnerGeneDetector mAbner;
  
  public AbnerAnnotator() {
    mAbner = new AbnerGeneDetector();
  }
  
  /**
   * @param JCas
   * @return void
   * @throws AnalysisEngineProcessException
   * 
   * This function call the abner engine to detect the gene name entity.
   * It is the core function of the 1st AE of this system.
   */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    String sts = aJCas.getDocumentText();
    String[] tokens = mAbner.process(sts);
    
    for (String token : tokens) {
      int st = sts.indexOf(token);
      if (st < 0 || st + token.length() > sts.length()) continue;
      CandidateToken annotation = new CandidateToken(aJCas);
      
      annotation.setSt(sts.indexOf(token));
      annotation.setEd(sts.indexOf(token) + token.length());
      annotation.setProcessID(PROCESS_ID);
      annotation.setScore(ABNER_SCORE);
      annotation.addToIndexes();
    }
  }
}
