package aima.core.learning.learners;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.inductive.DecisionList;
import aima.core.learning.inductive.DecisionListTest;
import java.util.HashSet;

/**
 * Uses DECISION-LIST-LEARNING algorithm, page 717, AIMAv3, to build a series of
 * tests (DecisionListTest); the chain of tests is used to predict outcomes for
 * new examples.
 *
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Andrew Brown
 */
public class DecisionListLearner implements Learner {

    private DecisionList decisionList;
    private HashSet<Attribute> possibleAttributes;

    /**
     * Constructor
     */
    public DecisionListLearner() {
    }

    /**
     * Return the decision list for this learner
     *
     * @return
     */
    public DecisionList getDecisionList() {
        return this.decisionList;
    }

    /**
     * Implement DECISION-LIST-LEARNING, page 717, AIMAv3 <br/>
     * <pre>
     * function DECISION-LIST-LEARNING(examples) returns a decision list, or failure
     * if examples is empty then return the trivial decision list "No"
     * t &lt;- a test that matches a nonempty subset examples_t of examples, such that the members of examples_t are all positive or negative
     * if there is no such t then return failure
     * if the examples in examples_t are positive then o &lt- Yes else o &lt;- No
     * return a decision list with initial test t and outcome o and remaining tests given by DECISION-LIST-LEARNING(examples - examples_t)
     * </pre>
     *
     * @param examples
     * @return
     */
    public DecisionList decisionListLearning(DataSet examples) {
        if (examples.size() == 0) {
            return new DecisionList();
        }
        // find test that returns largest list of positive or negative results
        DecisionListTest t = this.getLargestClassifyingTest(examples);
        // check t
        if (t == null) {
            return new DecisionList();
        }
        //
        DataSet matched = t.getMatchingExamples(examples);
        // set positive
        if (matched.examples.get(0).getOutput() != null) {
            t.setOutput(matched.examples.get(0).getOutput());
        } // set negative
        else {
            t.setOutput(null);
        }
        // return
        DecisionList list = new DecisionList();
        list.add(t);
        return list.mergeWith(this.decisionListLearning(t.getNonMatchingExamples(examples)));
    }

    /**
     * Return first test that classifies a subset of examples returning the same
     * value
     * @todo examine 2-, 3-, k-attribute tests
     *
     * @param examples
     * @return
     */
    private DecisionListTest getLargestClassifyingTest(DataSet examples) {
        // attempt all single attribute tests
        for (Attribute a : examples.getPossibleAttributes()) {
            DecisionListTest test = new DecisionListTest();
            test.add(a);
            DataSet matched = test.getMatchingExamples(examples);
            if (matched.size() > 0 && this.allExamplesHaveSameOutput(matched)) {
                test.setOutput(matched.getExample(0).getOutput());
                return test;
            }
        }
        // return
        return null;
    }

    /**
     * Test whether all of the examples in an example set return the same output
     *
     * @param matched
     * @return
     */
    private boolean allExamplesHaveSameOutput(DataSet matched) {
        Object value = matched.getExample(0).getOutput();
        for (Example e : matched.examples) {
            if (!e.getOutput().equals(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Trains the decision list given a set of examples
     *
     * @param examples
     */
    @Override
    public void train(DataSet examples) {
        this.decisionList = this.decisionListLearning(examples);
    }

    /**
     * Predict an outcome for the given example
     *
     * @param e
     * @return the example's output value, or null
     */
    @Override
    public Object predict(Example e) {
        if (this.decisionList == null) {
            throw new RuntimeException("DecisionListLearner has not yet been trained with a data set.");
        }
        // predict and return
        return this.decisionList.predict(e);
    }

    /**
     * Returns the accuracy of the hypothesis on the specified set of examples
     *
     * @param examples the test data set.
     * @return the accuracy of the hypothesis on the specified set of examples
     * as an array like [#correct, #incorrect]
     */
    @Override
    public int[] test(DataSet examples) {
        int[] results = new int[]{0, 0};
        for (Example e : examples) {
            if (e.getOutput().equals(this.decisionList.predict(e))) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        return results;
    }
}