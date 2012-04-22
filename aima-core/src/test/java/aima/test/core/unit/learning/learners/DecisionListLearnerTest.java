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

    /**
     * Trains a decision list with the restaurant data and tests its
     * effectiveness
     */
    @Test
    public void trainAndTest() {
        // get restaurant data
        DataSet restaurantData = null;
        try {
            restaurantData = DataSetTest.loadRestaurantData();
        } catch (IOException e) {
            Assert.fail("Could not load restaurant data from URL.");
        }
        // setup decision list learner
        DecisionListLearner learner = new DecisionListLearner();
        // train
        learner.train(restaurantData);
        // test
        int[] results = learner.test(restaurantData);
        double proportionCorrect = results[0] / restaurantData.size();
        Assert.assertTrue(proportionCorrect > 0.95);
    }
}