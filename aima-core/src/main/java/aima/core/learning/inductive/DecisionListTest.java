package aima.core.learning.inductive;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Represents a single test in a decision list, see figure 18.10, page 716,
 * AIMAv3
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class DecisionListTest<E> implements Iterable<Attribute> {

    /**
     * The test is a conjunction; every attribute in the test is a conjunct
     * that, when all proven true, it returns true.
     */
    private LinkedList<Attribute> conjuncts;
    
    /**
     * The output for this test; forms to Object
     */
    private E output;

    /**
     * Constructor
     */
    public DecisionListTest() {
        this.conjuncts = new LinkedList<Attribute>();
    }

    /**
     * Add a conjunct to the test
     *
     * @param attribute
     * @param decision
     */
    public void add(Attribute attribute) {
        this.conjuncts.add(attribute);
    }
    
    /**
     * Set the output for this test.
     */
    public void setOutput(E output){
        this.output = output;
    }
    
    /**
     * Return the output value; used when this test matches an example
     * @return 
     */
    public E getOutput(){
        return this.output;
    }

    /**
     * Test an example for matching
     *
     * @param e
     * @return
     */
    public boolean matches(Example example) {
        for (Attribute conjunct : this.conjuncts) {
            Attribute exampleAttribute = example.get(conjunct.getName());
            if (!conjunct.equals(exampleAttribute)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return matching examples from an example set
     *
     * @param examples
     * @return
     */
    public DataSet getMatchingExamples(DataSet examples) {
        DataSet matched = new DataSet();
        for (Example e : examples) {
            if (matches(e)) {
                matched.add(e);
            }
        }
        return matched;
    }

    /**
     * Return non-matching examples from an example set
     *
     * @param examples
     * @return
     */
    public DataSet getNonMatchingExamples(DataSet examples) {
        DataSet unmatched = new DataSet();
        for (Example e : examples) {
            if (!matches(e)) {
                unmatched.add(e);
            }
        }
        return unmatched;
    }
    
    /**
     * Test whether a conjunct exists
     *
     * @param attribute
     * @return
     */
    public boolean contains(Attribute attribute) {
        return this.conjuncts.contains(attribute);
    }

    /**
     * Return number of conjuncts in this test
     *
     * @return
     */
    public int size() {
        return this.conjuncts.size();
    }

    /**
     * Return string representation
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[");
        if (this.conjuncts.size() > 0) {
            Attribute last = this.conjuncts.peekLast();
            for (Attribute conjunct : this.conjuncts) {
                s.append(conjunct.getName());
                s.append("=");
                s.append(conjunct.getValue());
                if (!conjunct.equals(last)) {
                    s.append(" AND ");
                }
            }
        } else {
            s.append("true");
        }
        // decision
        s.append("]");
        // return
        return s.toString();
    }

    /**
     * Determines whether two tests are equal; that is if they test the same
     * attributes.
     *
     * @param other
     * @return
     */
    public boolean equals(DecisionListTest other) {
        for (Attribute a : this) {
            if (!other.contains(a)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Make the test iterable over its attributes/conjuncts
     *
     * @return
     */
    @Override
    public Iterator<Attribute> iterator() {
        return this.conjuncts.iterator();
    }
}
