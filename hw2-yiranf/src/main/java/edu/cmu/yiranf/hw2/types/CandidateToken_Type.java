
/* First created by JCasGen Tue Oct 07 20:22:14 EDT 2014 */
package edu.cmu.yiranf.hw2.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Wed Oct 08 08:11:00 EDT 2014
 * @generated */
public class CandidateToken_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CandidateToken_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CandidateToken_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CandidateToken(addr, CandidateToken_Type.this);
  			   CandidateToken_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CandidateToken(addr, CandidateToken_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = CandidateToken.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.yiranf.hw2.types.CandidateToken");
 
  /** @generated */
  final Feature casFeat_st;
  /** @generated */
  final int     casFeatCode_st;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getSt(int addr) {
        if (featOkTst && casFeat_st == null)
      jcas.throwFeatMissing("st", "edu.cmu.yiranf.hw2.types.CandidateToken");
    return ll_cas.ll_getIntValue(addr, casFeatCode_st);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSt(int addr, int v) {
        if (featOkTst && casFeat_st == null)
      jcas.throwFeatMissing("st", "edu.cmu.yiranf.hw2.types.CandidateToken");
    ll_cas.ll_setIntValue(addr, casFeatCode_st, v);}
    
  
 
  /** @generated */
  final Feature casFeat_ed;
  /** @generated */
  final int     casFeatCode_ed;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public int getEd(int addr) {
        if (featOkTst && casFeat_ed == null)
      jcas.throwFeatMissing("ed", "edu.cmu.yiranf.hw2.types.CandidateToken");
    return ll_cas.ll_getIntValue(addr, casFeatCode_ed);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEd(int addr, int v) {
        if (featOkTst && casFeat_ed == null)
      jcas.throwFeatMissing("ed", "edu.cmu.yiranf.hw2.types.CandidateToken");
    ll_cas.ll_setIntValue(addr, casFeatCode_ed, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public CandidateToken_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_st = jcas.getRequiredFeatureDE(casType, "st", "uima.cas.Integer", featOkTst);
    casFeatCode_st  = (null == casFeat_st) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_st).getCode();

 
    casFeat_ed = jcas.getRequiredFeatureDE(casType, "ed", "uima.cas.Integer", featOkTst);
    casFeatCode_ed  = (null == casFeat_ed) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_ed).getCode();

  }
}



    