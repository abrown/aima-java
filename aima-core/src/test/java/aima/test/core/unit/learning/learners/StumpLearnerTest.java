package aima.test.core.unit.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.learners.StumpLearner;
import aima.test.core.unit.learning.framework.DataSetTest;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/**
 * StumpLearnerTest
 *
 * @author Andrew Brown
 */
public class StumpLearnerTest {

    /**
     * 
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
        StumpLearner learner = new StumpLearner();
        // train
        learner.train(restaurantData);
        // test
        int[] results = learner.test(restaurantData);
        double proportionCorrect = results[0] / restaurantData.size();
        System.out.println(proportionCorrect);
        Assert.assertTrue(proportionCorrect > 0.95);
    }
}