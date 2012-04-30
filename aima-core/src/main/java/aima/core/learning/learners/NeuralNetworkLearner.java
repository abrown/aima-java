package aima.core.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.neural.*;
import java.util.Arrays;

/**
 *
 * @author Andrew Brown
 */
public class NeuralNetworkLearner implements Learner {

    NeuralNetwork network;
    double target_error_rate;

    /**
     * Constructor
     *
     * @param hypotheses
     * @param weights
     */
    public NeuralNetworkLearner(int number_of_layers, int perceptrons_per_layer, ActivationFunctionInterface function, double sensitivity, double target_error_rate) {
        this.network = new NeuralNetwork(number_of_layers, perceptrons_per_layer, function, sensitivity);
        this.target_error_rate = target_error_rate;
    }

    /**
     * Train the network to classify inputs; inputs must be passed as a Double[]
     * in the "INPUT" attribute of each Example; likewise, the expected output
     * must be passed as a Double[] in the Example output.
     *
     * @param examples
     */
    @Override
    public void train(DataSet examples) {
        // check
        Example first = examples.getExample(0);
        if (!(first.get("INPUT").getValue() instanceof Double[])) {
            throw new RuntimeException("NeuralNetworkLearner inputs must be passed as a Double[] in the \"INPUT\" attribute");
        }
        if (!(first.getOutput() instanceof Double[])) {
            throw new RuntimeException("NeuralNetworkLearner output must be set to a Double[]");
        }
        // convert example set
        DataList[] inputs = new DataList[examples.size()];
        DataList[] outputs = new DataList[examples.size()];
        for (int i = 0; i < examples.size(); i++) {
            inputs[i] = new DataList((Double[]) examples.getExample(i).get("INPUT").getValue());
            outputs[i] = new DataList((Double[]) examples.getExample(i).getOutput());
        }
        // train
        try {
            this.network.train(inputs, outputs, this.target_error_rate);
        } catch (SizeDifferenceException e) {
            throw new RuntimeException(e);
        } catch (WrongSizeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Predict an outcome for this example; return the most common value seen
     * thus far
     *
     * @param e
     * @return
     */
    @Override
    public Double[] predict(Example e) {
        // check
        if (!(e.get("INPUT").getValue() instanceof Double[])) {
            throw new RuntimeException("NeuralNetworkLearner inputs must be passed as a Double[] in the \"INPUT\" attribute");
        }
        DataList in = new DataList((Double[]) e.get("INPUT").getValue());
        // use network
        try {
            DataList out = this.network.use(in);
            return out.toArray();
        } catch (SizeDifferenceException ex) {
            throw new RuntimeException(ex);
        } catch (WrongSizeException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the accuracy of the neural network on the specified set of
     * examples
     *
     * @param test_set the test data set.
     * @return the accuracy of the hypothesis on the specified set of examples
     * as an array like [#correct, #incorrect]
     */
    @Override
    public int[] test(DataSet test_set) {
        // check
        Example first = test_set.getExample(0);
        if (!(first.get("INPUT").getValue() instanceof Double[])) {
            throw new RuntimeException("NeuralNetworkLearner inputs must be passed as a Double[] in the \"INPUT\" attribute");
        }
        if (!(first.getOutput() instanceof Double[])) {
            throw new RuntimeException("NeuralNetworkLearner output must be set to a Double[]");
        }
        // test
        int[] results = new int[]{0, 0};
        for (Example e : test_set) {
            System.out.println(Arrays.toString(this.predict(e)));
            if (Arrays.equals((Double[]) e.getOutput(), this.predict(e))) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        // return
        return results;
    }
}
