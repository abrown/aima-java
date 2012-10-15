package aima.test.core.unit.learning.learners;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.learners.NeuralNetworkLearner;
import aima.core.learning.neural.ActivationFunctionHardLimit;
import aima.core.util.Util;
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

    DataSet irisData;
    NeuralNetworkLearner learner;

    public NeuralNetworkLearnerTest() {
        // get iris data
        DataSet _irisData = null;
        try {
            _irisData = DataSetTest.loadIrisData();
        } catch (IOException e) {
            Assert.fail("Could not load iris data from URL.");
        }
        // normalize
        int size = _irisData.size();
        double[] sepalLength = new double[size];
        double[] sepalWidth = new double[size];
        double[] petalLength = new double[size];
        double[] petalWidth = new double[size];
        for (int i = 0; i < _irisData.size(); i++) {
            sepalLength[i] = (Double) _irisData.getExample(i).get("sepal-length").getValue();
            sepalWidth[i] = (Double) _irisData.getExample(i).get("sepal-width").getValue();
            petalLength[i] = (Double) _irisData.getExample(i).get("petal-length").getValue();
            petalWidth[i] = (Double) _irisData.getExample(i).get("petal-width").getValue();
        }
        sepalLength = Util.normalize(sepalLength);
        sepalWidth = Util.normalize(sepalWidth);
        petalLength = Util.normalize(petalLength);
        petalWidth = Util.normalize(petalWidth);
        // setup iris data     
        this.irisData = new DataSet();
        for (int i = 0; i < _irisData.size(); i++) {
            Example e = new Example();
            // set input
            Double[] input = {sepalLength[i], sepalWidth[i], petalLength[i], petalWidth[i]};
            // set output
            Double[] output = {0.0, 0.0, 0.0, 0.0};
            if (_irisData.getExample(i).getOutput().equals("setosa")) {
                output = new Double[]{1.0, 0.0, 0.0, 0.0};
            } else if (_irisData.getExample(i).getOutput().equals("versicolor")) {
                output = new Double[]{0.0, 1.0, 0.0, 0.0};
            } else if (_irisData.getExample(i).getOutput().equals("virginica")) {
                output = new Double[]{0.0, 0.0, 1.0, 0.0};
            }
            // create example
            e.add(new Attribute<Double[]>("INPUT", input));
            e.setOutput(output);
            this.irisData.add(e);
        }

        // setup decision list learner
        this.learner = new NeuralNetworkLearner(3, 4, new ActivationFunctionHardLimit(), 0.1, 0.95);
    }

    /**
     * Trains a decision list with the restaurant data and tests its
     * effectiveness
     */
    @Test
    public void trainAndTest() {
        // train
        this.learner.train(irisData);
        // test
        int[] results = learner.test(irisData);
        double proportionCorrect = results[0] / irisData.size();
        System.out.println(proportionCorrect);
        Assert.assertTrue(proportionCorrect > 0.95);
    }
}