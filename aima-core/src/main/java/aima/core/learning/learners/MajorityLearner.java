package aima.core.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.util.Util;
import java.util.ArrayList;

/**
 * Not directly explained in AIMAv3, but indirectly referenced by the majority
 * function, page 731, and the randomized weighted majority algorithm, page 758.
 * Used by PLURALITY-VALUE in DecisionTreeLearner. This learner simply finds the
 * most common result for the example set and adopts it as its own.
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class MajorityLearner<E> implements Learner {

    public E result;

    /**
     * Train this learner to return the most common example output.
     *
     * @param examples
     */
    @Override
    public void train(DataSet examples) {
        ArrayList<E> results = new ArrayList<E>();
        for (Example e : examples) {
            results.add((E) e.getOutput());
        }
        this.result = Util.mode(results);
    }

    /**
     * Predict an outcome for this example; return the most common value seen
     * thus far
     *
     * @param e
     * @return
     */
    @Override
    public E predict(Example e) {
        if (this.result == null) {
            throw new RuntimeException("MajorityLearner has not yet been trained.");
        }
        return this.result;
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
            if (e.getOutput().equals(this.result)) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        return results;
    }
}