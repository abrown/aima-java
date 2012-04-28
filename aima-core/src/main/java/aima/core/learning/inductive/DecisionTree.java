package aima.core.learning.inductive;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.Example;
import aima.core.util.Util;
import java.util.HashMap;

/**
 * Represents a decision tree, figure 18.2, page 699, AIMAv3. The book 
 * describes a Boolean decision tree, where attributes are compared with 
 * examples to predict a decision. This structure has been implemented with 
 * the classes Attribute, Example, and DecisionTreeLeaf. Because decision
 * trees are not necessarily boolean (see Bshouty et al, "On Learning Decision
 * Trees with Large Output Domains", <a href="http://people.clarkson.edu/~ttamon/ps-dir/btw-alg98-dt.pdf">
 * http://people.clarkson.edu/~ttamon/ps-dir/btw-alg98-dt.pdf</a>), this class
 * has been implemented with generics; for a boolean decision tree, simply
 * instantiate DecisionTree&lt;Boolean&gt;.
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class DecisionTree<E> {

    /**
     * Attribute to test at this node
     */
    private Attribute attribute;
    /**
     * Outcomes branching from this node
     */
    private HashMap<E, DecisionTree> branches = new HashMap<E, DecisionTree>();

    /**
     * Constructor
     */
    public DecisionTree() {
    }

    /**
     * Constructor
     * 
     */
    public DecisionTree(Attribute attribute) {
        this.attribute = attribute;
    }

    /**
     * Get this node's attribute, see framework.Attribute for usage
     * @return 
     */
    public Attribute getAttribute() {
        return this.attribute;
    }

    /**
     * Set this node's attribute
     * @param attribute 
     */
    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    /**
     * Get the branching nodes from this node
     * @return 
     */
    public HashMap<E, DecisionTree> getBranches() {
        return this.branches;
    }

    /**
     * Add a leaf branching from this node
     * @param attributeValue
     * @param decision 
     */
    public <F> void addLeaf(E value, F decision) {
        this.branches.put(value, new DecisionTreeLeaf<F>(decision));
    }

    /**
     * Add a node branching from this node; every node is an instance of
     * DecisionTree
     * @param value
     * @param tree 
     */
    public void addBranch(E value, DecisionTree tree) {
        this.branches.put(value, tree);
    }

    /**
     * Make prediction based on an example
     * @param e
     * @return 
     */
    public E predict(Example e) {
        // setup
        String attributeName = this.getAttribute().getName();
        E attributeValue = (E) e.get(attributeName).getValue();
        // match
        if (this.branches.containsKey(attributeValue)) {
            return (E) this.branches.get(attributeValue).predict(e); // continue down tree
        } else {
            throw new RuntimeException("No branch for value: " + attributeValue);
        }
    }

    /**
     * Return string representation
     * @return 
     */
    @Override
    public String toString() {
        return toString(0, new StringBuilder());
    }

    /**
     * Return formatted string representation
     * @param depth
     * @param buf
     * @return 
     */
    public String toString(int depth, StringBuilder s) {
        if (this.getAttribute().getName() != null) {
            s.append("[");
            s.append(this.getAttribute().getName());
            s.append("]");
            s.append("\n");
            for (E attributeValue : this.branches.keySet()) {
                s.append(Util.ntimes("  ", depth + 1));
                s.append("|- ");
                s.append(attributeValue);
                s.append(" -> ");
                DecisionTree child = this.branches.get(attributeValue);
                if( child instanceof DecisionTreeLeaf){
                    s.append(child);
                    s.append("\n");
                }
                else{
                    s.append("\n");
                    s.append(Util.ntimes("  ", depth + 1));
                    s.append(child.toString(depth + 1, new StringBuilder()));
                }
            }
        }
        return s.toString();
    }
}
