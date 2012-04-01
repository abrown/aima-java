package aima.core.learning.inductive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import aima.core.learning.framework.Example;

/**
 * Implements the decision list object used by DECISION-LIST-LEARNING on 
 * page 717, AIMAv3.
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class DecisionList {

    /**
     * Possible outcomes
     */
    public static final boolean MATCH = true;
    public static final boolean FAILURE = false;
    /**
     * "A decision list consists of a series of tests", page 715, AIMAv3
     */
    private List<DecisionListTest> tests;
    /**
     * Outcomes
     */
    private HashMap<DecisionListTest, Boolean> outcomes;

    /**
     * Constructor
     */
    public DecisionList() {
        this.tests = new ArrayList<DecisionListTest>();
        this.outcomes = new HashMap<DecisionListTest, Boolean>();
    }

    /**
     * Make prediction based on the decision list tests
     * @param example
     * @return true on match; false otherwise
     */
    public boolean predict(Example example) {
        // test size
        if (this.tests.isEmpty()) {
            return DecisionList.FAILURE;
        }
        // test each outcome
        for (DecisionListTest test : this.tests) {
            if (test.matches(example)) {
                return DecisionList.MATCH;
            }
        }
        // default
        return DecisionList.FAILURE;
    }

    /**
     * Add a test to the decision list
     * @param test
     * @param outcome 
     */
    public void add(DecisionListTest test) {
        tests.add(test);
    }

    /**
     * Merge this decision list with another
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
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("IF ");
        for (DecisionListTest test : this.tests) {
            s.append(test);
            s.append(" THEN true ELSE ");
        }
        s.append("false");
        return s.toString();
    }
}
