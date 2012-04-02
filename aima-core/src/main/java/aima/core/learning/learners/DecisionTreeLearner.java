package aima.core.learning.learners;

import java.util.Iterator;
import java.util.List;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.inductive.DecisionTreeLeaf;
import aima.core.learning.inductive.DecisionTree;
import aima.core.util.Util;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class DecisionTreeLearner implements Learner {

    private DecisionTree tree;
    private String defaultValue;

    public DecisionTreeLearner() {
        this.defaultValue = "Unable To Classify";

    }

    // used when you have to test a non induced tree (eg: for testing)
    public DecisionTreeLearner(DecisionTree tree, String defaultValue) {
        this.tree = tree;
        this.defaultValue = defaultValue;
    }

    //
    // START-Learner
    /**
     * Induces the decision tree from the specified set of examples
     * 
     * @param ds
     *            a set of examples for constructing the decision tree
     */
    @Override
    public void train(DataSet ds) {
        List<String> attributes = ds.getNonTargetAttributes();
        this.tree = decisionTreeLearning(ds, attributes,
                new DecisionTreeLeaf(defaultValue));
    }

    @Override
    public String predict(Example e) {
        return (String) tree.predict(e);
    }

    @Override
    public int[] test(DataSet ds) {
        int[] results = new int[]{0, 0};

        for (Example e : ds.examples) {
            if (e.targetValue().equals(tree.predict(e))) {
                results[0] = results[0] + 1;
            } else {
                results[1] = results[1] + 1;
            }
        }
        return results;
    }

    // END-Learner
    //
    /**
     * Returns the decision tree of this decision tree learner
     * 
     * @return the decision tree of this decision tree learner
     */
    public DecisionTree getDecisionTree() {
        return tree;
    }

    /**
     * Implements DECISION-TREE-LEARNING function from page 702 of AIMAv3:
     * 
     * <pre><code>
     * function DECISION-TREE-LEARNING(examples, attributes, parent_examples) returns a tree
     *  if examples is empty then return PLURALITY-VALUE(parent_examples)
     *  else if all examples have the same classification then return the classification
     *  else if attributes is empty then return PLURALITY-VALUE(examples)
     *  else
     *      A = argmax_(a|attributes) IMPORTANCE(a, examples)
     *      tree = a new decision tree with root test A
     *      for each value v_k of A do
     *          exs = {e : e|examples and e.A = v_k}
     *          subtree = DECISION-TREE-LEARNING(exs, attributes - A, examples)
     *          add a branch to tree with label (A = v_k) and subtree subtree
     *      return tree
     * </code></pre>
     * @param ds
     * @param attributeNames
     * @param defaultTree
     * @return 
     */
    private DecisionTree decisionTreeLearning(DataSet ds,
            List<String> attributeNames, DecisionTreeLeaf defaultTree) {
        if (ds.size() == 0) {
            return defaultTree;
        }
        if (allExamplesHaveSameClassification(ds)) {
            return new DecisionTreeLeaf(ds.getExample(0).targetValue());
        }
        if (attributeNames.size() == 0) {
            return majorityValue(ds);
        }
        String chosenAttribute = chooseAttribute(ds, attributeNames);

        DecisionTree tree = new DecisionTree(chosenAttribute);
        DecisionTreeLeaf m = majorityValue(ds);

        List<String> values = ds.getPossibleAttributeValues(chosenAttribute);
        for (String v : values) {
            DataSet filtered = ds.matchingDataSet(chosenAttribute, v);
            List<String> newAttribs = Util.removeFrom(attributeNames,
                    chosenAttribute);
            DecisionTree subTree = decisionTreeLearning(filtered, newAttribs, m);
            tree.addNode(v, subTree);

        }

        return tree;
    }

    private DecisionTreeLeaf majorityValue(DataSet ds) {
        Learner learner = new MajorityLearner();
        learner.train(ds);
        return new DecisionTreeLeaf(learner.predict(ds.getExample(0)));
    }

    private String chooseAttribute(DataSet ds, List<String> attributeNames) {
        double greatestGain = 0.0;
        String attributeWithGreatestGain = attributeNames.get(0);
        for (String attr : attributeNames) {
            double gain = ds.calculateGainFor(attr);
            if (gain > greatestGain) {
                greatestGain = gain;
                attributeWithGreatestGain = attr;
            }
        }

        return attributeWithGreatestGain;
    }

    private boolean allExamplesHaveSameClassification(DataSet ds) {
        String classification = ds.getExample(0).targetValue();
        Iterator<Example> iter = ds.iterator();
        while (iter.hasNext()) {
            Example element = iter.next();
            if (!(element.targetValue().equals(classification))) {
                return false;
            }

        }
        return true;
    }
}
