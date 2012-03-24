package aima.core.learning.framework;

/**
 * Describes a model for learning based on a set of examples; see page 693-694
 * in AIMAv3 for further explanation. This model does not specify the types 
 * of feedback (unsupervised, reinforcement, supervised) used to train the 
 * learner.
 * @todo should implement aima.core.agent.Agent
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public interface Learner {

    /**
     * Trains the learner given a set of examples
     * @param training_set the training data set 
     */
    void train(DataSet training_set);

    /**
     * Returns the outcome predicted for the specified example
     * @param example an example
     * @return the outcome predicted for the specified example
     */
    String predict(Example example);

    /**
     * Returns the accuracy of the hypothesis on the specified set of examples
     * @param test_set the test data set.
     * @return the accuracy of the hypothesis on the specified set of examples
     */
    int[] test(DataSet test_set);
}
