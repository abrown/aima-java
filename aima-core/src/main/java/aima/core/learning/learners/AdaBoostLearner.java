package aima.core.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.util.Util;
import aima.core.util.datastructure.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Implements ADABOOST algorithm from page 751, AIMAv3.
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class AdaBoostLearner implements Learner {

    private List<Learner> learners;
    private DataSet dataSet;
    private double[] exampleWeights;
    private HashMap<Learner, Double> learnerWeights;

    /**
     * Constructor
     *
     * @param learners
     * @param examples
     */
    public AdaBoostLearner(List<Learner> learners, DataSet examples) {
        this.learners = learners;
        this.dataSet = examples;
        initializeExampleWeights(examples.size());
        initializeHypothesisWeights(learners.size());
    }

    /**
     * Train the learners @todo make this look like page 751; create adaBoost()
     * <pre><code>
     * function ADABOOST(examples, L, K) returns a weighted-majority hypothesis
     *  inputs: examples, a set of N labeled examples (x_1, y_1), ..., (x_N, y_N)
     *      L, a learning algorithm
     *      K, the number of hypothesis in the ensemble
     *  local variables: w, a vector of N example weights, initially 1/N
     *      h, a vector of K hypotheses
     *      z, a vector of K hypothesis weights
     *
     *  for k = 1 to K do
     *      h[k] &lt;- L(examples, w)
     *      error &lt;- 0
     *      for j = 1 to N do
     *          if h[k](x_j) != y_j then error &lt;- error + w[j]
     *      for j = 1 to N do
     *          if h[k](x_j) = y_j then w[j] &lt;- w[j] . error/(1 - error)
     *      w &lt;- NORMALIZE(w)
     *      z[k] &lt;- log (1-error)/error
     *  return WEIGHTED-MAJORITY(h, z)
     * </code></pre>
     *
     * @param examples
     */
    @Override
    public void train(DataSet examples) {
        this.initializeExampleWeights(examples.size());
        for (Learner learner : this.learners) {
            learner.train(examples);
            double error = this.calculateError(examples, learner);
            if (error < 0.0001) {
                break;
            }
            this.adjustExampleWeights(examples, learner, error);
            double newHypothesisWeight = learnerWeights.get(learner)
                    * Math.log((1.0 - error) / error);
            learnerWeights.put(learner, newHypothesisWeight);
        }
    }

    /**
     * Predict
     *
     * @param e
     * @return
     */
    @Override
    public Object predict(Example e) {
        return this.weightedMajority(e);
    }

    /**
     * Test
     *
     * @param examples
     * @return
     */
    @Override
    public int[] test(DataSet examples) {
        int[] results = new int[]{0, 0};
        for (Example e : examples) {
            if (e.getOutput().equals(this.predict(e))) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        return results;
    }

    /**
     * @todo explain this
     *
     * @param e
     * @return
     */
    private Object weightedMajority(Example e) {
        HashSet<Object> values = new HashSet<Object>();
        for (Example _e : this.dataSet) {
            values.add(_e.getOutput());
        }
        ArrayList<Object> _values = new ArrayList<Object>(values);
        Table<Object, Learner, Double> table = this.createTargetValueLearnerTable(_values, e);
        return this.getTargetValueWithTheMaximumVotes(_values, table);
    }

    /**
     * @todo explain this
     *
     * @param targetValues
     * @param e
     * @return
     */
    private Table<Object, Learner, Double> createTargetValueLearnerTable(
            List<Object> targetValues, Example e) {
        // create a table with target-attribute values as rows and learners as
        // columns and cells containing the weighted votes of each Learner for a
        // target value
        // Learner1 Learner2 Laerner3 .......
        // Yes 0.83 0.5 0
        // No 0 0 0.6

        Table<Object, Learner, Double> table = new Table<Object, Learner, Double>(
                targetValues, learners);
        // initialize table
        for (Learner l : learners) {
            for (Object s : targetValues) {
                table.set(s, l, 0.0);
            }
        }
        for (Learner learner : learners) {
            String predictedValue = learner.predict(e);
            for (Object v : targetValues) {
                if (predictedValue.equals(v)) {
                    table.set(v, learner, table.get(v, learner)
                            + learnerWeights.get(learner) * 1);
                }
            }
        }
        return table;
    }

    /**
     * @todo explain this
     * @param targetValues
     * @param table
     * @return 
     */
    private Object getTargetValueWithTheMaximumVotes(List<Object> targetValues,
            Table<Object, Learner, Double> table) {
        Object targetValueWithMaxScore = targetValues.get(0);
        double score = scoreOfValue(targetValueWithMaxScore, table, learners);
        for (Object value : targetValues) {
            double scoreOfValue = scoreOfValue(value, table, learners);
            if (scoreOfValue > score) {
                targetValueWithMaxScore = value;
                score = scoreOfValue;
            }
        }
        return targetValueWithMaxScore;
    }

    /**
     * @explain this
     * @param size 
     */
    private void initializeExampleWeights(int size) {
        if (size == 0) {
            throw new RuntimeException(
                    "cannot initialize Ensemble learning with Empty Dataset");
        }
        double value = 1.0 / (1.0 * size);
        exampleWeights = new double[size];
        for (int i = 0; i < size; i++) {
            exampleWeights[i] = value;
        }
    }

    /**
     * @todo explain this
     * @param size 
     */
    private void initializeHypothesisWeights(int size) {
        if (size == 0) {
            throw new RuntimeException(
                    "cannot initialize Ensemble learning with Zero Learners");
        }

        learnerWeights = new HashMap<Learner, Double>();
        for (Learner le : learners) {
            learnerWeights.put(le, 1.0);
        }
    }

    /**
     * @todo explain this
     * @param ds
     * @param l
     * @return 
     */
    private double calculateError(DataSet ds, Learner l) {
        double error = 0.0;
        for (int i = 0; i < ds.examples.size(); i++) {
            Example e = ds.getExample(i);
            if (!(l.predict(e).equals(e.getOutput()))) {
                error = error + exampleWeights[i];
            }
        }
        return error;
    }

    /**
     * @todo explain this
     * @param ds
     * @param l
     * @param error 
     */
    private void adjustExampleWeights(DataSet ds, Learner l, double error) {
        double epsilon = error / (1.0 - error);
        for (int j = 0; j < ds.examples.size(); j++) {
            Example e = ds.getExample(j);
            if ((l.predict(e).equals(e.getOutput()))) {
                exampleWeights[j] = exampleWeights[j] * epsilon;
            }
        }
        exampleWeights = Util.normalize(exampleWeights);
    }

    /**
     * @todo explain this
     * @param targetValue
     * @param table
     * @param learners
     * @return 
     */
    private double scoreOfValue(Object targetValue,
            Table<Object, Learner, Double> table, List<Learner> learners) {
        double score = 0.0;
        for (Learner l : learners) {
            score += table.get((String) targetValue, l);
        }
        return score;
    }
}
