package aima.core.learning.framework;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a learning example; according to page 699, in a Boolean decision 
 * tree it is the pair (x, y), where "x is a vector of values for the input 
 * attributes and y is a single boolean output value". This definition has been
 * abstracted to produce this generic class.
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class Example<E> {

    /**
     * The input attributes for this example
     */
    List<Attribute> inputAttributes = new LinkedList<Attribute>();
    
    /**
     * The output value for this example
     */
    E outputValue;
    
    /**
     * @todo remove this
     */
    private Attribute targetAttribute;

    /**
     * Constructor
     * @param attributes
     * @param targetAttribute 
     */
    public Example(List<Attribute> attributes,
            Attribute targetAttribute) {
        this.inputAttributes = attributes;
        this.targetAttribute = targetAttribute;
    }
    
    /**
     * Returns the specified attribute by name
     * @param attributeName
     * @return 
     */
    public Attribute get(String attributeName){
        for(Attribute a : inputAttributes){
            if( a.getName().equals(attributeName) ) return a;
        }
        throw new RuntimeException("Could not find attribute: " + attributeName);
    }

    /**
     * @todo replace this with get(attributeName).getValue();
     * @param attributeName
     * @return 
     */
    public String getAttributeValueAsString(String attributeName) {
        return inputAttributes.get(attributeName).valueAsString();
    }

    /**
     * @todo replace this with get(attributeName).getValue();
     * @param attributeName
     * @return 
     */
    public double getAttributeValueAsDouble(String attributeName) {
        Attribute attribute = inputAttributes.get(attributeName);
        if (attribute == null || !(attribute instanceof NumericAttribute)) {
            throw new RuntimeException(
                    "cannot return numerical value for non numeric attribute");
        }
        return ((NumericAttribute) attribute).valueAsDouble();
    }

    /**
     * Returns string representation
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
     * @todo remove this
     * @return 
     */
    public String targetValue() {
        return getAttributeValueAsString(targetAttribute.name());
    }

    /**
     * Overrides equality test
     * @param o
     * @return 
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass())) {
            return false;
        }
        Example other = (Example) o;
        return inputAttributes.equals(other.inputAttributes);
    }

    /**
     * For use with equals()
     * @return 
     */
    @Override
    public int hashCode() {
        return inputAttributes.hashCode();
    }

    /**
     * @todo remove this
     * @param attrValueToNumber
     * @return 
     */
    public Example numerize(
            HashMap<String, HashMap<String, Integer>> attrValueToNumber) {
        HashMap<String, Attribute> numerizedExampleData = new HashMap<String, Attribute>();
        for (String key : inputAttributes.keySet()) {
            Attribute attribute = inputAttributes.get(key);
            if (attribute instanceof StringAttribute) {
                int correspondingNumber = attrValueToNumber.get(key).get(
                        attribute.valueAsString());
                NumericAttributeSpecification spec = new NumericAttributeSpecification(
                        key);
                numerizedExampleData.put(key, new NumericAttribute(
                        correspondingNumber, spec));
            } else {// Numeric Attribute
                numerizedExampleData.put(key, attribute);
            }
        }
        return new Example(numerizedExampleData,
                numerizedExampleData.get(targetAttribute.name()));
    }
}
