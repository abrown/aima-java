package aima.core.learning.inductive;

import aima.core.learning.framework.Example;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the decision list object used by DECISION-LIST-LEARNING on page
 * 717, AIMAv3. For DECISION-LIST-LEARNING, see DecisionListLearner.
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class DecisionList {

    /**
     * "A decision list consists of a series of tests", page 715, AIMAv3
     */
    private List<DecisionListTest> tests;

    /**
     * Constructor
     */
    public DecisionList() {
        this.tests = new ArrayList<DecisionListTest>();
    }

    /**
     * Make prediction based on the decision list tests
     *
     * @param example
     * @return true on match; false otherwise
     */
    public <T> T predict(Example example) {
        // test size
        if (this.tests.isEmpty()) {
            return null;
        }
        // test each outcome
        for (DecisionListTest test : this.tests) {
            if (test.matches(example)) {
                return (T) test.getOutput();
            }
        }
        // default
        return null;
    }

    /**
     * Add a test to the decision list
     *
     * @param test
     * @param outcome
     */
    public void add(DecisionListTest test) {
        this.tests.add(test);
    }
    
    /**
     * Return number of tests in this decision list
     * @return 
     */
    public int size(){
        return this.tests.size();
    }

    /**
     * Merge this decision list with another
     *
     * @param list
     * @return
     */
    public DecisionList mergeWith(DecisionList list) {
        DecisionList merged = new DecisionList();
        // add these
        for (DecisionListTest test : this.tests) {
            merged.add(test);
        }
        // add those
        for (DecisionListTest test : list.tests) {
            merged.add(test);
        }
        // return
        return merged;
    }

    /**
     * Return string representation
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("IF ");
        for (DecisionListTest test : this.tests) {
            s.append(test);
            s.append(" THEN ");
            s.append(test.getOutput());
            s.append(" ELSE ");
        }
        s.append("null");
        return s.toString();
    }
}
