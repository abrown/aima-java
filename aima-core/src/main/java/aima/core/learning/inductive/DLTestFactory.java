package aima.core.learning.inductive;

import java.util.ArrayList;
import java.util.List;

import aima.core.learning.framework.DataSet;

/**
 * @author Ravi Mohan
 * 
 */
public class DLTestFactory {

	public List<DecisionListTest> createDLTestsWithAttributeCount(DataSet ds, int i) {
		if (i != 1) {
			throw new RuntimeException(
					"For now DLTests with only 1 attribute can be craeted , not"
							+ i);
		}
		List<String> nonTargetAttributes = ds.getNonTargetAttributes();
		List<DecisionListTest> tests = new ArrayList<DecisionListTest>();
		for (String ntAttribute : nonTargetAttributes) {
			List<String> ntaValues = ds.getPossibleAttributeValues(ntAttribute);
			for (String ntaValue : ntaValues) {

				DecisionListTest test = new DecisionListTest();
				test.add(ntAttribute, ntaValue);
				tests.add(test);

			}
		}
		return tests;
	}
}
