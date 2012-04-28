package aima.core.learning.inductive;

import aima.core.learning.framework.Example;
import aima.core.util.Util;

/**
 * Represents a leaf on the decision tree; no attribute testing is performed, 
 * this class merely returns a value. See colored boxes in figure 18.2, page
 * 699, AIMAv3.
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class DecisionTreeLeaf<E> extends DecisionTree<E> {

    /**
     * Represents the value of the decision; generic for use with multiple
     * types of trees.
     */
    private E value;

    /**
     * Constructor
     * @param value 
     */
    public DecisionTreeLeaf(E value) {
        this.value = value;
    }

    /**
     * Cannot add a leaf to this leaf.
     * @param attributeValue
     * @param decision 
     */
    @Override
    public <F> void addLeaf(E value, F decision) {
        throw new RuntimeException("Cannot add a leaf to DecisionTreeLeaf.");
    }

    /**
     * Cannot add a node to this leaf.
     * @param attributeValue
     * @param tree 
     */
    @Override
    public void addBranch(E value, DecisionTree node) {
        throw new RuntimeException("Cannot add a node to DecisionTreeLeaf.");
    }

    /**
     * Return the value of this leaf
     * @param e
     * @return 
     */
    @Override
    public E predict(Example e) {
        return value;
    }

    /**
     * Return string representation
     * @return 
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Return formatted string representation
     * @param depth
     * @param buf
     * @return 
     */
    @Override
    public String toString(int depth, StringBuilder buf) {
        buf.append(Util.ntimes("  ", depth + 1));
        buf.append("DECISION -> ");
        buf.append(value);
        buf.append("\n");
        return buf.toString();
    }
}
