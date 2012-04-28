package aima.test.core.unit.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.learners.DecisionListLearner;
import aima.test.core.unit.learning.framework.DataSetTest;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/**
 * DecisionListLearnerTest
 *
 * @author Andrew Brown
 */
public class DecisionListLearnerTest {

    DataSet restaurantData;
    DecisionListLearner learner;

    /**
     * Constructor
     */
    public DecisionListLearnerTest() {
        // get restaurant data
        this.restaurantData = null;
        try {
            this.restaurantData = DataSetTest.loadRestaurantData();
        } catch (IOException e) {
            Assert.fail("Could not load restaurant data from URL.");
        }
        // setup decision list learner
        this.learner = new DecisionListLearner();
        // train
        this.learner.train(this.restaurantData);
    }

    /**
     * Test a decision list with the restaurant data
     */
    @Test
    public void testTrainedDecisionList() {
        // test
        int[] results = this.learner.test(this.restaurantData);
        double proportionCorrect = (double) results[0] / this.restaurantData.size();
        Assert.assertTrue(proportionCorrect > 0.95);
    }

    /**
     * Demonstrate toString() format
     */
    @Test
    public void showToString() {
        System.out.println("DecisionList: ");
        System.out.println(this.learner.getDecisionList());
        System.out.println();
    }
}