package edu.cmu.yiranf.hw2.process;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.ConfidenceChunker;
import com.aliasi.util.AbstractExternalizable;

import edu.cmu.yiranf.hw2.types.CandidateToken;
import edu.cmu.yiranf.hw2.types.GeneType;
import edu.cmu.yiranf.hw2.util.IntPair;

/**
 * geneDetectorAnnotator works as the analysis engine of the system. It only accepts the context of the sentence
 * and return the position of each gene name entities.
 * @author fyr
 *
 */
public class LingpipeAnnotator extends JCasAnnotator_ImplBase {
  static ConfidenceChunker chunker;
  
  public void initialize(UimaContext aContext) throws ResourceInitializationException {
    super.initialize(aContext);
    
    try {
      System.out.println((String) aContext.getConfigParameterValue("GeneModelFile"));
      chunker = (ConfidenceChunker) AbstractExternalizable.readResourceObject(LingpipeAnnotator.class, (String) aContext.getConfigParameterValue("GeneModelFile"));
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  
  public Map<IntPair, Double> detect(String text) throws Exception {
    Map<IntPair, Double> tokens = new HashMap<IntPair, Double>();
    
    char[] cs = text.toCharArray();
    Iterator<Chunk> it = chunker.nBestChunks(cs, 0, cs.length, 80);
    while (it.hasNext()) {
      Chunk chunk = it.next();
      double conf = Math.pow(2.0,chunk.score());
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
   * This function calls the lingpipeDetector to detect the gene name entity.
   * It is the core function in the annotator of this system.
   */
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    String sts = aJCas.getDocumentText();
    
    Map<IntPair, Double> abnerTokens = new HashMap<IntPair, Double>();
    Iterator annotationIter = aJCas.getAnnotationIndex(CandidateToken.type).iterator();
    while (annotationIter.hasNext()) {
      CandidateToken annot = (CandidateToken) annotationIter.next();
      abnerTokens.put(new IntPair(annot.getSt(), annot.getEd()), 1.0);
    }

    try {
      Map<IntPair, Double> tokens = detect(sts);
      
      Iterator it = tokens.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<IntPair, Double> pairs = (Map.Entry<IntPair, Double>)it.next();
        
        double conf = pairs.getValue();
        if (abnerTokens.get((IntPair)pairs.getKey()) != null) {
          conf += abnerTokens.get((IntPair)pairs.getKey());
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
