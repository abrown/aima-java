package aima.core.learning.learners;
import aima.core.learning.framework.Attribute;
import java.util.List;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.inductive.DecisionListTest;
import aima.core.learning.inductive.DecisionList;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Uses DECISION-LIST-LEARNING algorithm, page 717, AIMAv3, to build a series
 * of tests (DecisionListTest); the chain of tests is used to predict outcomes
 * for new examples.
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Andrew Brown
 */
public class DecisionListLearner implements Learner {

    private DecisionList decisionList;
    private ArrayList<DecisionListTest> possibleTests;

    /**
     * Constructor
     */
    public DecisionListLearner() {
    }

    /**
     * Return the decision list for this learner
     * @return
     */
    public DecisionList getDecisionList() {
        return decisionList;
    }
    
    /**
     * Implement DECISION-LIST-LEARNING, page 717, AIMAv3
     * <br/>
     * <pre>
     * function DECISION-LIST-LEARNING(examples) returns a decision list, or failure
     *  if examples is empty then return the trivial decision list "No"
     *  t &lt;- a test that matches a nonempty subset examples_t of examples, such that the members of examples_t are all positive or negative
     *  if there is no such t then return failure
     *  if the examples in examples_t are positive then o &lt- Yes else o &lt;- No
     *  return a decision list with initial test t and outcome o and remaining tests given by DECISION-LIST-LEARNING(examples - examples_t)
     * </pre>
     * @param examples
     * @return 
     */
    public DecisionList decisionListLearning(DataSet examples) {
        if (examples.size() == 0) {
            return new DecisionList();
        }
        List<DecisionListTest> possibleTests = testFactory.createDLTestsWithAttributeCount(examples, 1);
        DecisionListTest test = getValidTest(possibleTests, examples);
        if (test == null) {
            return new DecisionList();
        }
        // at this point there is a test that classifies some subset of examples
        // with the same target value
        DataSet matched = test.getMatchingExamples(examples);
        DecisionList list = new DecisionList();
        list.add(test, matched.getExample(0).targetValue());
        return list.mergeWith(decisionListLearning(test.getNonMatchingExamples(examples)));
    }
    
    private ArrayList<DecisionListTest> getPossibleTests(DataSet examples, int k){
        if( possibleTests == null){
            
            possibleTests = new ArrayList<DecisionListTest>();
            Attribute[] possibleAttributes = examples.getPossibleAttributes().toArray(new Attribute[0]);
            // create tests of up to k attribute size:
            for(int h = 1; h <= k; h++){
                DecisionListTest test = new DecisionListTest();
                // loop through possibles...
                for(int i = 0; i < possibleAttributes.length; i++){
                    // ...picking h attributes each time
                    for(int j = 0; j < h; j++){
                        int index = i + j;
                        if( index < possibleAttributes.length ) test.add(possibleAttributes[index]);
                    }
                }
                // add to possible Tests
            }            
        }
        return possibleTests;
    }
    
    private DecisionListTest getValidTest(List<DecisionListTest> possibleTests, DataSet ds) {
        for (DecisionListTest test : possibleTests) {
            DataSet matched = test.getMatchingExamples(ds);
            if (!(matched.size() == 0)) {
                if (allExamplesHaveSameTargetValue(matched)) {
                    return test;
                }
            }

        }
        return null;
    }

    private boolean allExamplesHaveSameTargetValue(DataSet matched) {
        // assumes at least i example in dataset
        String targetValue = matched.getExample(0).targetValue();
        for (Example e : matched.examples) {
            if (!(e.targetValue().equals(targetValue))) {
                return false;
            }
        }
        return true;
    }
    
    
    /**
     * Trains the decision list given a set of examples
     * @param examples
     */
    @Override
    public void train(DataSet examples) {
        this.decisionList = decisionListLearning(examples);
    }

    /**
     * Predict an outcome for the given example
     * @param e
     * @return the example's output value, or null
     */
    @Override
    public Object predict(Example e) {
        if (decisionList == null) {
            throw new RuntimeException("DecisionListLearner has not yet been trained with a data set.");
        }
        // predict and return
        if( decisionList.predict(e) ){
            return e.getOutput();
        }
        else{
            return null;
        }
    }

    /**
     * Returns the accuracy of the hypothesis on the specified set of examples
     * @param examples the test data set.
     * @return the accuracy of the hypothesis on the specified set of examples as an array like [#correct, #incorrect]
     */
    @Override
    public int[] test(DataSet examples) {
        int[] results = new int[]{0, 0};
        for (Example e : examples) {
            if (e.getOutput().equals(decisionList.predict(e))) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        return results;
    }



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
