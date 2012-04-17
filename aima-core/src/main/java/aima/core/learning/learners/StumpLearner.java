package aima.core.learning.learners;

import aima.core.learning.framework.DataSet;
import aima.core.learning.inductive.DecisionTree;

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
     * Constructor
     *
     * @param tree
     */
    public StumpLearner(DecisionTree tree) {
        super(tree);
    }

    /**
     * Train the stump; this does nothing because no training is needed
     *
     * @param ds
     */
    @Override
    public void train(DataSet ds) {
        // do nothing: the stump is not inferred from the dataset
    }
}
