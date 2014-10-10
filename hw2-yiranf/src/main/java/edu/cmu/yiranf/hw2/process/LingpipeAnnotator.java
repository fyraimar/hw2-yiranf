package edu.cmu.yiranf.hw2.process;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import edu.cmu.yiranf.hw2.types.CandidateToken;
import edu.cmu.yiranf.hw2.util.IntPair;

/**
 * LingpipeAnnotator works as the analysis engine of the system. It only accepts the context of the
 * sentence and return the position of each gene name entities with a score and a processid '2'.
 * 
 * @author fyr
 *
 */
public class LingpipeAnnotator extends JCasAnnotator_ImplBase {
  private static final int PROCESS_ID = 2;

  private static ConfidenceChunker sChunker;

  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);

    try {
      sChunker = (ConfidenceChunker) AbstractExternalizable.readResourceObject(
              LingpipeAnnotator.class, (String) aContext.getConfigParameterValue("GeneModelFile"));
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param text
   * @return Map<Intpair, Double>
   * @throws Exception
   * 
   *           Use Lingpipe to get a map of candidate tokens with scores.
   */
  public Map<IntPair, Double> detect(String text) throws Exception {
    Map<IntPair, Double> tokens = new HashMap<IntPair, Double>();

    char[] cs = text.toCharArray();
    Iterator<Chunk> it = sChunker.nBestChunks(cs, 0, cs.length, 80);
    while (it.hasNext()) {
      Chunk chunk = it.next();
      double conf = Math.pow(2.0, chunk.score());
      int start = chunk.start();
      int end = chunk.end();

      tokens.put(new IntPair(start, end), conf);
    }

    return tokens;
  }

  /**
   * @param JCas
   * @return void
   * @throws AnalysisEngineProcessException
   * 
   *           This function calls the lingpipeDetector to detect the gene name entity. It is the
   *           core function in the annotator of this system.
   */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    String sts = aJCas.getDocumentText();

    try {
      Map<IntPair, Double> tokens = detect(sts);

      Iterator it = tokens.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<IntPair, Double> pairs = (Map.Entry<IntPair, Double>) it.next();

        CandidateToken annotation = new CandidateToken(aJCas);
        annotation.setSt(pairs.getKey().getX());
        annotation.setEd(pairs.getKey().getY());
        annotation.setProcessID(PROCESS_ID);
        annotation.setScore(pairs.getValue());
        annotation.addToIndexes();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
