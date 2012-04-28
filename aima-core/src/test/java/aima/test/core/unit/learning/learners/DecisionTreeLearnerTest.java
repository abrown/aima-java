package aima.test.core.unit.learning.learners;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.learners.DecisionTreeLearner;
import aima.test.core.unit.learning.framework.DataSetTest;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;

/**
 * DecisionTreeLearnerTest
 *
 * @author Andrew Brown
 */
public class DecisionTreeLearnerTest {

    DataSet restaurantData;
    DecisionTreeLearner learner;

    /**
     * Constructor
     */
    public DecisionTreeLearnerTest() {
        // get restaurant data
        this.restaurantData = null;
        try {
            this.restaurantData = DataSetTest.loadRestaurantData();
        } catch (IOException e) {
            Assert.fail("Could not load restaurant data from URL.");
        }
        // setup decision list learner
        this.learner = new DecisionTreeLearner();
        // train
        learner.train(this.restaurantData);
    }

    /**
     * Test whether importance function, getMostImportantAttribute(), actually
     * finds the same attribute as that found in AIMAv3, page 702
     */
    @Test
    public void testImportance() {
        // get attribute names
        ArrayList<String> attributes = new ArrayList<String>();
        for (Attribute a : this.restaurantData.getExample(0).getAttributes()) {
            attributes.add(a.getName());
        }
        // get most important attribute according to pages 700-702, AIMAv3
        String attributeName = this.learner.getMostImportantAttribute(attributes, restaurantData);
        Assert.assertEquals("Pat", attributeName);
    }

    /**
     * Test a decision tree with the restaurant data
     */
    @Test
    public void testTrainedDecisionTree() {
        // test
        int[] results = this.learner.test(this.restaurantData);
        double proportionCorrect = (double) results[0] / this.restaurantData.size();
        Assert.assertTrue(proportionCorrect > 0.95);
    }
    
    /**
     * Demonstrate toString() format
     */
    @Test
    public void showToString(){
        System.out.println("DecisionTree: ");
        System.out.println(this.learner.getDecisionTree());
        System.out.println();
    }
}
