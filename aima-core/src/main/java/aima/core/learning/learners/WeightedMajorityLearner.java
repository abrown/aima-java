package aima.core.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import java.util.HashMap;

/**
 * Implied by and used in ADABOOST, this learner, an ensemble hypothesis, is "a
 * weighted-majority combination of all the K hypothesis, each weighted
 * according to how well it performed on the training set" page 749, AIMAv3.
 *
 * @author Andrew Brown
 */
public class WeightedMajorityLearner implements Learner {

    private Learner[] hypotheses;
    private double[] weights;

    /**
     * Constructor
     *
     * @param hypotheses
     * @param weights
     */
    public WeightedMajorityLearner(Learner[] hypotheses, double[] weights) {
        this.hypotheses = hypotheses;
        this.weights = weights;
    }
    
    /**
     * Return hypotheses
     * @return 
     */
    public Learner[] getHypotheses(){
        return this.hypotheses;
    }

    /**
     * Train this learner to return the most common example output.
     *
     * @param examples
     */
    @Override
    public void train(DataSet examples) {
        throw new RuntimeException("WeightedMajorityLearner can only be trained by AdaBoostLearner.train().");
    }

    /**
     * Predict an outcome for this example; return the most common value seen
     * thus far
     *
     * @param e
     * @return
     */
    @Override
    public <T> T predict(Example e) {
        if (this.hypotheses == null || this.weights == null) {
            throw new RuntimeException("WeightedMajorityLearner has not yet been trained.");
        }
        // ask each hypothesis
        HashMap<T, Double> values = new HashMap<T, Double>();
        for (int i = 0; i < this.weights.length; i++) {
            T value = this.hypotheses[i].predict(e);
            double weight = this.weights[i];
            double total = 0.0;
            if (values.containsKey(value)) {
                total = values.get(value);
            }
            values.put(value, total + weight);
        }
        // find weighted majority
        T majorityOutput = this.hypotheses[0].predict(e);
        double maxTotal = Double.NEGATIVE_INFINITY;
        for (T value : values.keySet()) {
            double total = values.get(value);
            if (total > maxTotal) {
                majorityOutput = value;
                maxTotal = total;
            }
        }
        // return
        return majorityOutput;
    }

    /**
     * Returns the accuracy of the hypothesis on the specified set of examples
     *
     * @param test_set the test data set.
     * @return the accuracy of the hypothesis on the specified set of examples
     * as an array like [#correct, #incorrect]
     */
    @Override
    public int[] test(DataSet test_set) {
        int[] results = new int[]{0, 0};
        for (Example e : test_set) {
            if (e.getOutput().equals(this.predict(e))) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        return results;
    }
}