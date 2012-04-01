package aima.test.core.unit.learning.inductive;

import java.util.List;

import aima.core.learning.framework.DataSet;
import aima.core.learning.inductive.DecisionListTest;
import aima.core.learning.inductive.DLTestFactory;

/**
 * @author Ravi Mohan
 * 
 */
public class MockDLTestFactory extends DLTestFactory {

	private List<DecisionListTest> tests;

	public MockDLTestFactory(List<DecisionListTest> tests) {
		this.tests = tests;
	}

	@Override
	public List<DecisionListTest> createDLTestsWithAttributeCount(DataSet ds, int i) {
		return tests;
	}
}
