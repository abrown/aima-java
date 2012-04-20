package aima.test.core.unit.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.learners.NeuralNetworkLearner;
import aima.core.learning.neural.ActivationFunctionPureLinear;
import aima.test.core.unit.learning.framework.DataSetTest;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Test;

/**
 * NeuralNetworkLearner
 *
 * @author Andrew Brown
 */
public class NeuralNetworkLearnerTest {

    /**
     * Trains a decision list with the restaurant data and tests its
     * effectiveness
     */
    @Test
    public void trainAndTest() {
        // get restaurant data
        DataSet irisData = null;
        try {
            irisData = DataSetTest.loadIrisData();
        } catch (IOException e) {
            Assert.fail("Could not load restaurant data from URL.");
        }
        // setup decision list learner
        NeuralNetworkLearner learner = new NeuralNetworkLearner(2, 4, new ActivationFunctionPureLinear(), 0.1, 0.95);
        // train
        learner.train(irisData);
        // test
        int[] results = learner.test(irisData);
        double proportionCorrect = results[0] / irisData.size();
        System.out.println(proportionCorrect);
        Assert.assertTrue(proportionCorrect > 0.95);
    }
}