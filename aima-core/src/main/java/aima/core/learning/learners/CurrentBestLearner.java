package aima.core.learning.learners;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.knowledge.FOLExample;
import aima.core.learning.knowledge.FOLHypothesis;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.FOLOTTERLikeTheoremProver;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;

/**
 * Implements the CURRENT-BEST-LEARNING algorithm from page 771, AIMAv3. The
 * algorithm works by creating hypotheses (in essence, first order logic
 * sentences) that are compared with examples (also sentences). In this class,
 * we use Hypothesis to represent the FOL hypotheses and FOLExample to convert
 * the common Example class to FOL.
 *
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 */
public class CurrentBestLearner implements Learner {

    /**
     * The predicate we are seeking to learn how to classify; e.g. "WillWait" on
     * page 770, AIMAv3
     */
    private String classificationPredicate;
    /**
     * The current best hypothesis that fits the example set
     */
    private FOLHypothesis bestHypothesis;
    /**
     * The complete domain of all predicates and constants used in this data set
     */
    private FOLDomain domain;
    /**
     * The knowledge base
     */
    private FOLKnowledgeBase kb;

    /**
     * Constructor
     *
     * @param classificationPredicate the predicate we are seeking to learn how
     * to classify; e.g. "WillWait"
     */
    public CurrentBestLearner(String classificationPredicate) {
        this.classificationPredicate = classificationPredicate;
    }

    /**
     * Artificial Intelligence A Modern Approach (3rd Edition): Figure 19.2,
     * page 771.<br/> <br/>
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
     * Figure 19.2 The current-best-hypothesis learning algorithm. It searches
     * for a consistent hypothesis that fits all the examples and backtracks
     * when no consistent specialization/generalization can be found. To start
     * the algorithm, any hypothesis can be passed in; it will be specialized or
     * generalized as needed.
     */
    public FOLHypothesis currentBestLearning(DataSet examples, FOLHypothesis h) {
        if (examples.size() == 0) {
            return h;
        }
        FOLExample e = (FOLExample) examples.getExample(0); // get first example
        if (isConsistentWith(e, h)) {
            return currentBestLearning(examples.remove(e), h);
        } else if (isFalsePositive(e, h)) {
            for (FOLHypothesis h1 : specialize(h, examples)) {
                FOLHypothesis h2 = currentBestLearning(examples.remove(e), h1);
                if (h2 != null) {
                    return h2;
                }
            }
        } else if (isFalseNegative(e, h)) {
            for (FOLHypothesis h1 : generalize(h, examples)) {
                FOLHypothesis h2 = currentBestLearning(examples.remove(e), h1);
                if (h2 != null) {
                    return h2;
                }
            }
        }
        return null;
    }

    /**
     * Determine whether the example is consistent with the hypothesis
     *
     * @param example
     * @param hypothesis
     * @return
     */
    public boolean isConsistentWith(FOLExample example, FOLHypothesis hypothesis) {
        if (hypothesis == null) {
            return false;
        }
        // setup knowledge base
        this.kb.clear(); // removes all previous sentences
        this.kb.tell(example.toDescription());
        this.kb.tell(this.bestHypothesis);
        // infer
        InferenceResult result = kb.ask(example.toClassification());
        // return
        if (result.isTrue() && example.getOutput() != null) {
            return true;
        }
        return false;
    }

    /**
     * Determine whether the hypothesis returns a false positive
     *
     * @param example
     * @param hypothesis
     * @return
     */
    public boolean isFalsePositive(FOLExample example, FOLHypothesis hypothesis) {
        if (hypothesis == null) {
            return false;
        }
        // setup knowledge base
        this.kb.clear(); // removes all previous sentences
        this.kb.tell(example.toDescription());
        this.kb.tell(this.bestHypothesis);
        // infer
        InferenceResult result = kb.ask(example.toClassification());
        // return
        if (result.isTrue() && example.getOutput() == null) {
            return true;
        }
        return false;
    }

    /**
     * Determine whether the hypothesis returns a false negative
     *
     * @param example
     * @param hypothesis
     * @return
     */
    public boolean isFalseNegative(FOLExample example, FOLHypothesis hypothesis) {
        if (hypothesis == null) {
            return false;
        }
        // setup knowledge base
        this.kb.clear(); // removes all previous sentences
        this.kb.tell(example.toDescription());
        this.kb.tell(this.bestHypothesis);
        // infer
        InferenceResult result = kb.ask(example.toClassification());
        // return
        if (!result.isTrue() && example.getOutput() != null) {
            return true;
        }
        return false;
    }

    /**
     * Return
     *
     * @param hypothesis
     * @param examples
     * @return
     */
    public FOLHypothesis[] specialize(FOLHypothesis hypothesis, DataSet examples) {
        /**
         * @todo
         */
        return null;
    }

    public FOLHypothesis[] generalize(FOLHypothesis hypothesis, DataSet examples) {
        /**
         * @todo
         */
        return null;
    }

    /**
     * Train this learner to correctly classify the set of examples
     *
     * @param examples
     */
    @Override
    public void train(DataSet examples) {
        // setup
        this.domain = this.getDomain(examples);
        this.kb = new FOLKnowledgeBase(this.domain, new FOLOTTERLikeTheoremProver(1000, false));
        // run CURRENT-BEST-LEARNING
        this.bestHypothesis = currentBestLearning(examples, null);
    }

    /**
     * Predict an outcome based on the currently-held best hypothesis
     *
     * @param example
     * @return
     */
    @Override
    public String predict(Example example) {
        // example must be a FOLExample
        FOLExample e = (FOLExample) example;
        // check if the hypothesis is valid
        if (this.bestHypothesis == null) {
            NotSentence n = new NotSentence(new Predicate(this.classificationPredicate, e.toConstant()));
            return n.toString();
        }
        // setup knowledge base
        this.kb.clear(); // removes all previous sentences
        this.kb.tell(e.toDescription());
        this.kb.tell(this.bestHypothesis);
        // infer
        InferenceResult result = kb.ask(e.toClassification());
        // return
        if (result.isTrue()) {
            return e.getOutput().toString();
        } else {
            // possibly: result.isPossiblyFalse(), result.isUnknownDueToTimeout()
            return null;
        }
    }

    /**
     * Returns an array of matches and failures, like [#correct, #incorrect].
     *
     * @param examples
     * @return
     */
    @Override
    public int[] test(DataSet examples) {
        // setup
        int[] results = new int[]{0, 0};
        // loop through example set
        for (Example e : examples) {
            String predicted = this.predict(e);
            String actual = e.getOutput().toString(); // @todo make this comparison a real object comparison, not a string comparison
            if (predicted.equals(actual)) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        // return
        return results;
    }

    /**
     * Creates the FOL domain based on the set of examples; @todo potential
     * issue when the data set does not contain all constants/predicates
     *
     * @param examples
     * @return
     */
    private FOLDomain getDomain(DataSet examples) {
        // setup
        this.domain = new FOLDomain();
        for (Example e : examples) {
            // add output value as a constant
            this.domain.addConstant(e.getOutput().toString());
            // loop through attributes
            for (Attribute a : e.getAttributes()) {
                // add each attribute name as a predicate
                this.domain.addPredicate(a.getName());
                // add each attribute value as a constant
                this.domain.addConstant(a.getValue().toString());
            }
        }
        // return
        return this.domain;
    }
}
