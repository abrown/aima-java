package aima.core.learning.inductive;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import aima.core.learning.framework.Example;

/**
 * Implements the decision list object used by DECISION-LIST-LEARNING on 
 * page 717, AIMAv3; this implementation is generalized by using generics
 * so the DecisionListTest can be extended to more than just boolean
 * outcomes.
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class DecisionList<E>{
    
	private String positive, negative;

        /**
         * "A decision list consists of a series of tests", page 715, AIMAv3
         */
	private List<DecisionListTest> tests;

	private HashMap<DecisionListTest, String> testOutcomes;

	public DecisionList(String positive, String negative) {
		this.positive = positive;
		this.negative = negative;
		this.tests = new ArrayList<DecisionListTest>();
		testOutcomes = new HashMap<DecisionListTest, String>();
	}

	public String predict(Example example) {
		if (tests.size() == 0) {
			return negative;
		}
		for (DecisionListTest test : tests) {
			if (test.matches(example)) {
				return testOutcomes.get(test);
			}
		}
		return negative;
	}

	public void add(DecisionListTest test, String outcome) {
		tests.add(test);
		testOutcomes.put(test, outcome);
	}

	public DecisionList mergeWith(DecisionList dlist2) {
		DecisionList merged = new DecisionList(positive, negative);
		for (DecisionListTest test : tests) {
			merged.add(test, testOutcomes.get(test));
		}
		for (DecisionListTest test : dlist2.tests) {
			merged.add(test, dlist2.testOutcomes.get(test));
		}
		return merged;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (DecisionListTest test : tests) {
			buf.append(test.toString() + " => " + testOutcomes.get(test)
					+ " ELSE \n");
		}
		buf.append("END");
		return buf.toString();
	}
}
