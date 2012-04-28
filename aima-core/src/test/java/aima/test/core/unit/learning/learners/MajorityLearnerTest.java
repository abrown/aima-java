package aima.test.core.unit.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.learners.MajorityLearner;
import aima.test.core.unit.learning.framework.DataSetTest;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/**
 * MajorityLearnerTest
 *
 * @author Andrew Brown
 */
public class MajorityLearnerTest {

    DataSet restaurantData;
    MajorityLearner learner;

    /**
     * Constructor
     */
    public MajorityLearnerTest() {
        // get restaurant data
        this.restaurantData = null;
        try {
            this.restaurantData = DataSetTest.loadRestaurantData();
        } catch (IOException e) {
            Assert.fail("Could not load restaurant data from URL.");
        }
        // setup decision list learner
        this.learner = new MajorityLearner();
        // train
        this.learner.train(this.restaurantData);
    }

    /**
     * Test a majority learner with the restaurant data
     */
    @Test
    public void testTrainedMajorityLearner() {
        // test
        int[] results = this.learner.test(this.restaurantData);
        double proportionCorrect = (double) results[0] / this.restaurantData.size();
        // restaurant set has exactly half "Yes" and half "No", page 700
        Assert.assertEquals(0.5, proportionCorrect, 0.001);
    }
}