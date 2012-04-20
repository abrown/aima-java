package aima.core.learning.framework;

import java.util.ArrayList;

/**
 * Represents a learning example; according to page 699, in a Boolean decision
 * tree it is the pair (x, y), where "x is a vector of values for the input
 * attributes and y is a single boolean output value". This definition has been
 * abstracted to produce this generic class.
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class Example<E> implements Cloneable {

    /**
     * The input attributes for this example
     */
    ArrayList<Attribute> inputAttributes = new ArrayList<Attribute>();
    /**
     * The output value for this example
     */
    E outputValue;

    /**
     * Constructor
     */
    public Example() {
    }

    /**
     * Constructor
     *
     * @param inputAttributes
     * @param outputValue
     */
    public Example(ArrayList<Attribute> inputAttributes, E outputValue) {
        this.inputAttributes = inputAttributes;
        this.outputValue = outputValue;
    }

    /**
     * Adds an attribute to this example
     *
     * @param attribute
     */
    public void add(Attribute attribute) {
        this.inputAttributes.add(attribute);
    }

    /**
     * Returns the specified attribute by name
     *
     * @param attributeName
     * @return
     */
    public Attribute get(String attributeName) {
        for (Attribute a : inputAttributes) {
            if (a.getName().equals(attributeName)) {
                return a;
            }
        }
        throw new RuntimeException("Could not find attribute: " + attributeName);
    }

    /**
     * Set the output value
     *
     * @param outputValue
     */
    public void setOutput(E outputValue) {
        this.outputValue = outputValue;
    }

    /**
     * Returns the output value
     *
     * @return
     */
    public E getOutput() {
        return (E) this.outputValue;
    }

    /**
     * Returns the list of attributes
     *
     * @return
     */
    public Attribute[] getAttributes() {
        return this.inputAttributes.toArray(new Attribute[0]);
    }

    /**
     * Returns string representation
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(inputAttributes.toString());
        s.append(" --> ");
        s.append(outputValue);
        return s.toString();
    }

    /**
     * Auto-generate hash code
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.inputAttributes != null ? this.inputAttributes.hashCode() : 0);
        hash = 41 * hash + (this.outputValue != null ? this.outputValue.hashCode() : 0);
        return hash;
    }

    /**
     * Auto-generate equals()
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Example<E> other = (Example<E>) obj;
        if (this.inputAttributes != other.inputAttributes && (this.inputAttributes == null || !this.inputAttributes.equals(other.inputAttributes))) {
            return false;
        }
        if (this.outputValue != other.outputValue && (this.outputValue == null || !this.outputValue.equals(other.outputValue))) {
            return false;
        }
        return true;
    }

    /**
     * Clone
     *
     * @return
     */
    @Override
    public Example<E> clone() {
        Example<E> copy = new Example();
        for (Attribute a : this.getAttributes()) {
            copy.add(a.clone());
        }
        copy.setOutput(this.outputValue);
        return copy;
    }
}
