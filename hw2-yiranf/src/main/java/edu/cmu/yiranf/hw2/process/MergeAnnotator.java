package edu.cmu.yiranf.hw2.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import com.aliasi.chunk.ConfidenceChunker;

import edu.cmu.yiranf.hw2.types.CandidateToken;
import edu.cmu.yiranf.hw2.types.GeneType;
import edu.cmu.yiranf.hw2.util.IntPair;

/**
 * MergeAnnotator retrieve these candidate tokens from abner and lingpipe, compare their results and
 * mark some of these tokens as the gene according to scores.
 * 
 * @author fyr
 *
 */
public class MergeAnnotator extends JCasAnnotator_ImplBase {
  static ConfidenceChunker chunker;

  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
  }

  /**
   * @param JCas
   * @return void
   * @throws AnalysisEngineProcessException
   * 
   *           This function merges the result.
   */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub

    Map<IntPair, Double> abnerTokens = new HashMap<IntPair, Double>();
    Map<IntPair, Double> lingpipeTokens = new HashMap<IntPair, Double>();

    Iterator annotationIter = aJCas.getAnnotationIndex(CandidateToken.type).iterator();
    while (annotationIter.hasNext()) {
      CandidateToken annot = (CandidateToken) annotationIter.next();
      if (annot.getProcessID() == 1) {
        abnerTokens.put(new IntPair(annot.getSt(), annot.getEd()), annot.getScore());
        // System.out.println("#1 add");
      } else {
        lingpipeTokens.put(new IntPair(annot.getSt(), annot.getEd()), annot.getScore());
      }
    }

    Iterator it = lingpipeTokens.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<IntPair, Double> pairs = (Map.Entry<IntPair, Double>) it.next();

      double conf = pairs.getValue();

      if (abnerTokens.get((IntPair) pairs.getKey()) != null) {
        conf += abnerTokens.get((IntPair) pairs.getKey());
      } else {
        conf -= 0.2;
      }

      if (conf < 0.63)
        continue;

      GeneType annotation = new GeneType(aJCas);
      annotation.setSt(pairs.getKey().getX());
      annotation.setEd(pairs.getKey().getY());
      annotation.addToIndexes();
    }
  }
}
