package aima.test.core.unit.learning.learners;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.DataSetFactory;
import aima.core.learning.inductive.DecisionTree;
import aima.core.learning.learners.DecisionTreeLearner;
import aima.core.util.Util;

/**
 * @author Ravi Mohan
 * 
 */
public class DecisionTreeTest {

    private static final String YES = "Yes";
    private static final String NO = "No";

    @Test
    public void testReturnOutputValue() {
        // setup
        DecisionTree one = new DecisionTree(null);
        DecisionTree two = new DecisionTree(null);
        String three = "...";
        one.addBranch("two", two);
        two.addLeaf("three", three);
        // test
    }

    @Test
    public void testActualDecisionTreeClassifiesRestaurantDataSetCorrectly()
            throws Exception {
        DecisionTreeLearner learner = new DecisionTreeLearner(
                createActualRestaurantDecisionTree(), "Unable to clasify");
        int[] results = learner.test(DataSetFactory.getRestaurantDataSet());
        Assert.assertEquals(12, results[0]);
        Assert.assertEquals(0, results[1]);
    }

    @Test
    public void testInducedDecisionTreeClassifiesRestaurantDataSetCorrectly()
            throws Exception {
        DecisionTreeLearner learner = new DecisionTreeLearner(
                createInducedRestaurantDecisionTree(), "Unable to clasify");
        int[] results = learner.test(DataSetFactory.getRestaurantDataSet());
        Assert.assertEquals(12, results[0]);
        Assert.assertEquals(0, results[1]);
    }

    @Test
    public void testStumpCreationForSpecifiedAttributeValuePair()
            throws Exception {
        DataSet ds = DataSetFactory.getRestaurantDataSet();
        List<String> unmatchedValues = new ArrayList<String>();
        unmatchedValues.add(NO);
        DecisionTree dt = DecisionTree.getStumpFor(ds, "alternate", YES, YES,
                unmatchedValues, NO);
        Assert.assertNotNull(dt);
    }

    @Test
    public void testStumpCreationForDataSet() throws Exception {
        DataSet ds = DataSetFactory.getRestaurantDataSet();
        List<DecisionTree> dt = DecisionTree.getStumpsFor(ds, YES,
                "Unable to classify");
        Assert.assertEquals(26, dt.size());
    }

    @Test
    public void testStumpPredictionForDataSet() throws Exception {
        DataSet ds = DataSetFactory.getRestaurantDataSet();

        List<String> unmatchedValues = new ArrayList<String>();
        unmatchedValues.add(NO);
        DecisionTree tree = DecisionTree.getStumpFor(ds, "hungry", YES, YES,
                unmatchedValues, "Unable to Classify");
        DecisionTreeLearner learner = new DecisionTreeLearner(tree,
                "Unable to Classify");
        int[] result = learner.test(ds);
        Assert.assertEquals(5, result[0]);
        Assert.assertEquals(7, result[1]);
    }

    //
    // PRIVATE METHODS
    //
    private static DecisionTree createInducedRestaurantDecisionTree() {
        // from AIMA 2nd ED
        // Fig 18.6
        // friday saturday node
        DecisionTree frisat = new DecisionTree(null);
        frisat.addLeaf(Util.YES, Util.YES);
        frisat.addLeaf(Util.NO, Util.NO);

        // type node
        DecisionTree type = new DecisionTree(null);
        type.addLeaf("French", Util.YES);
        type.addLeaf("Italian", Util.NO);
        type.addBranch("Thai", frisat);
        type.addLeaf("Burger", Util.YES);

        // hungry node
        DecisionTree hungry = new DecisionTree(null);
        hungry.addLeaf(Util.NO, Util.NO);
        hungry.addBranch(Util.YES, type);

        // patrons node
        DecisionTree patrons = new DecisionTree(null);
        patrons.addLeaf("None", Util.NO);
        patrons.addLeaf("Some", Util.YES);
        patrons.addBranch("Full", hungry);

        return patrons;
    }

    private static DecisionTree createActualRestaurantDecisionTree() {
        // from AIMA 2nd ED
        // Fig 18.2

        // raining node
        DecisionTree raining = new DecisionTree(null);
        raining.addLeaf(Util.YES, Util.YES);
        raining.addLeaf(Util.NO, Util.NO);

        // bar node
        DecisionTree bar = new DecisionTree(null);
        bar.addLeaf(Util.YES, Util.YES);
        bar.addLeaf(Util.NO, Util.NO);

        // friday saturday node
        DecisionTree frisat = new DecisionTree(null);
        frisat.addLeaf(Util.YES, Util.YES);
        frisat.addLeaf(Util.NO, Util.NO);

        // second alternate node to the right of the diagram below hungry
        DecisionTree alternate2 = new DecisionTree(null);
        alternate2.addBranch(Util.YES, raining);
        alternate2.addLeaf(Util.NO, Util.YES);

        // reservation node
        DecisionTree reservation = new DecisionTree(null);
        frisat.addBranch(Util.NO, bar);
        frisat.addLeaf(Util.YES, Util.YES);

        // first alternate node to the left of the diagram below waitestimate
        DecisionTree alternate1 = new DecisionTree(null);
        alternate1.addBranch(Util.NO, reservation);
        alternate1.addBranch(Util.YES, frisat);

        // hungry node
        DecisionTree hungry = new DecisionTree(null);
        hungry.addLeaf(Util.NO, Util.YES);
        hungry.addBranch(Util.YES, alternate2);

        // wait estimate node
        DecisionTree waitEstimate = new DecisionTree(null);
        waitEstimate.addLeaf(">60", Util.NO);
        waitEstimate.addBranch("30-60", alternate1);
        waitEstimate.addBranch("10-30", hungry);
        waitEstimate.addLeaf("0-10", Util.YES);

        // patrons node
        DecisionTree patrons = new DecisionTree(null);
        patrons.addLeaf("None", Util.NO);
        patrons.addLeaf("Some", Util.YES);
        patrons.addBranch("Full", waitEstimate);

        return patrons;
    }
}
