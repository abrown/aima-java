package aima.core.learning.learners;

import java.util.ArrayList;
import java.util.List;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.knowledge.CurrentBestLearning;
import aima.core.learning.knowledge.FOLDataSetDomain;
import aima.core.learning.knowledge.FOLExample;
import aima.core.learning.knowledge.Hypothesis;
import aima.core.logic.fol.inference.FOLOTTERLikeTheoremProver;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.parsing.ast.Sentence;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class CurrentBestLearner implements Learner {

    /**
     * Artificial Intelligence A Modern Approach (3rd Edition): Figure 19.2, page
     * 771.<br/>
     * <br/>
     * <pre>
     * function CURRENT-BEST-LEARNING(examples, h) returns a hypothesis or fail
     * 
     *   if examples is empty then
     *      return h
     *   e &lt;- FIRST(examples)
     *   if e is consistent with h then
     *      return CURRENT-BEST-LEARNING(REST(examples), h)
     *   else if e is a false positive for h then
     *     for each h' in specializations of h consistent with examples seen so far do
     *       h'' &lt;- CURRENT-BEST-LEARNING(REST(examples), h')
     *       if h'' != fail then return h''
     *   else if e is a false negative for h then
     *     for each h' in generalization of h consistent with examples seen so far do
     *       h'' &lt;- CURRENT-BEST-LEARNING(REST(examples), h')
     *       if h'' != fail then return h''
     *   return fail
     * </pre>
     * 
     * Figure 19.2 The current-best-hypothesis learning algorithm. It searches for a
     * consistent hypothesis that fits all the examples and backtracks when no
     * consistent specialization/generalization can be found. To start the
     * algorithm, any hypothesis can be passed in; it will be specialized or
     * generalized as needed.
     */
    public Hypothesis currentBestLearning(DataSet examples, Hypothesis h) {
        if (examples.size() == 0) {
            return h;
        }
        FOLExample e = (FOLExample) examples.getExample(0); // get first example
        if (isConsistentWith(e, h)) {
            return currentBestLearning(examples.remove(e), h);
    
        }
        else if( isFalsePositive(e, h)) {
            for(Hypothesis h1 : specialize(h, examples)){
                Hypothesis h2 = currentBestLearning(examples.remove(e), h1);
                if( h2 != null ) return h2;
            }
        }
        else if( isFalseNegative(e, h)){
            for(Hypothesis h1 : generalize(h, examples)){
                Hypothesis h2 = currentBestLearning(examples.remove(e), h1);
                if( h2 != null ) return h2;
            }
        }
        return null;
    }
    
    /**
     * Determines whether the example is consistent with the hypothesis
     * @param example
     * @param hypothesis
     * @return 
     */
    public boolean isConsistentWith(FOLExample example, Hypothesis hypothesis){
        Sentence classification = example.toClassification();
        Sentence description = example.toDescription();
        /**
         * @todo determine whether example satisfies hypothesis; currently
         * unknown what avenue to take on this
         */
        return false;
    }
    
    public boolean isFalsePositive(Example example, Hypothesis hypothesis){
        /**
         * @todo see isConsistentWith()...
         */
        return false;
    }
    
    public boolean isFalseNegative(Example example, Hypothesis hypothesis){
        /**
         * @todo see isConsistentWith()...
         */
        return false;
    }
    
    public Hypothesis[] specialize(Hypothesis hypothesis, DataSet examples){
        /**
         * @todo
         */
        return null;
    }
    
    public Hypothesis[] generalize(Hypothesis hypothesis, DataSet examples){
        /**
         * @todo 
         */
        return null;
    }
    
//    private String trueGoalValue = null;
//    private FOLDataSetDomain folDSDomain = null;
//    private FOLKnowledgeBase kb = null;
//    private Hypothesis currentBestHypothesis = null;

    //
    // PUBLIC METHODS
    //
//    public CurrentBestLearner(String trueGoalValue) {
//        this.trueGoalValue = trueGoalValue;
//    }
    
//
//    //
//    // START-Learner
//    public void train(DataSet ds) {
//        folDSDomain = new FOLDataSetDomain(ds.specification, trueGoalValue);
//        List<FOLExample> folExamples = new ArrayList<FOLExample>();
//        int egNo = 1;
//        for (Example e : ds.examples) {
//            folExamples.add(new FOLExample(folDSDomain, e, egNo));
//            egNo++;
//        }
//
//        // Setup a KB to be used for learning
//        kb = new FOLKnowledgeBase(folDSDomain, new FOLOTTERLikeTheoremProver(
//                1000, false));
//
//        CurrentBestLearning cbl = new CurrentBestLearning(folDSDomain, kb);
//
//        currentBestHypothesis = cbl.currentBestLearning(folExamples);
//    }
//
//    public String predict(Example e) {
//        String prediction = "~" + e.targetValue();
//        if (null != currentBestHypothesis) {
//            FOLExample etp = new FOLExample(folDSDomain, e, 0);
//            kb.clear();
//            kb.tell(etp.getDescription());
//            kb.tell(currentBestHypothesis.getHypothesis());
//            InferenceResult ir = kb.ask(etp.getClassification());
//            if (ir.isTrue()) {
//                if (trueGoalValue.equals(e.targetValue())) {
//                    prediction = e.targetValue();
//                }
//            } else if (ir.isPossiblyFalse() || ir.isUnknownDueToTimeout()) {
//                if (!trueGoalValue.equals(e.targetValue())) {
//                    prediction = e.targetValue();
//                }
//            }
//        }
//
//        return prediction;
//    }
//
//    public int[] test(DataSet ds) {
//        int[] results = new int[]{0, 0};
//
//        for (Example e : ds.examples) {
//            if (e.targetValue().equals(predict(e))) {
//                results[0] = results[0] + 1;
//            } else {
//                results[1] = results[1] + 1;
//            }
//        }
//        return results;
//    }
//    // END-Learner
//    //
}
