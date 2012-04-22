package aima.core.learning.learners;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.inductive.DecisionTree;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Used by ADA-BOOST as a hypothesis, page 750, AIMAv3.
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class StumpLearner extends DecisionTreeLearner {
    
    /**
     * Constructor
     */
    public StumpLearner(){
        super();
    }

    /**
     * Train the stump; these stumps branch to all possible values for a
     * randomly chosen attribute; the leaves are set as the most common output
     * found for the branch's attribute value; uses MajorityLearner
     *
     * @param examples
     */
    @Override
    public void train(DataSet examples) {
        // randomly select attribute
        int attributeSpace = examples.getExample(0).getAttributes().length;
        int chosenAttribute = (int) Math.floor(Math.random() * attributeSpace);
        // randomly select example to train to
        int exampleSpace = examples.size();
        int chosenExample = (int) Math.floor(Math.random() * exampleSpace);
        // create tree
        Example e = examples.getExample(chosenExample);
        Attribute a = e.getAttributes()[chosenAttribute].clone();
        a.setValue(null);
        this.trainedTree = new DecisionTree(a);
        // create leaves
        HashMap<Object, DataSet> leafSets = examples.splitBy(a.getName());
        for(Object attributeValue : leafSets.keySet()){
            MajorityLearner ml = new MajorityLearner();
            ml.train(leafSets.get(attributeValue));
            this.trainedTree.addLeaf(attributeValue, ml.result);
        }
    }
}
