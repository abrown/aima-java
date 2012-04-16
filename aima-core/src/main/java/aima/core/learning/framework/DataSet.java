package aima.core.learning.framework;

import java.util.HashMap;
import java.util.Iterator;
import aima.core.util.Util;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Provides methods for representing a set of learning examples
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class DataSet implements Iterable<Example> {

    public ArrayList<Example> examples;

    /**
     * Constructor
     */
    public DataSet() {
        examples = new ArrayList<Example>();
    }

    /**
     * Adds an example to the set
     *
     * @param e
     */
    public void add(Example e) {
        examples.add(e);
    }

    /**
     * Returns the size of the set
     *
     * @return
     */
    public int size() {
        return examples.size();
    }

    /**
     * Returns the example given an index
     *
     * @param number
     * @return
     */
    public Example getExample(int index) {
        return examples.get(index);
    }

    /**
     * Removes the example given from the data set @todo remove this
     *
     * @param e
     * @return
     */
    public DataSet remove(Example e) {
        DataSet ds = new DataSet();
        for (Example eg : examples) {
            if (!(e.equals(eg))) {
                ds.add(eg);
            }
        }
        return ds;
    }
    
    /**
     * Find the examples that match the given attribute name/value pair
     * @param attributeName
     * @param attributeValue
     * @return 
     */
    public DataSet find(String attributeName, Object attributeValue){
        DataSet ds = new DataSet();
        for (Example e : examples) {
            if (e.get(attributeName).getValue().equals(attributeValue)) {
                ds.add(e);
            }
        }
        return ds;
    }

    /**
     * Return all possible attributes (a name/value pair) in the given example
     * set; by using a HashSet, no duplicates should exist.
     *
     * @return
     */
    public HashSet<Attribute> getPossibleAttributes() {
        HashSet<Attribute> possibleAttributes = new HashSet<Attribute>();
        for (Example e : this) {
            for (Attribute a : e.getAttributes()) {
                possibleAttributes.add(a);
            }
        }
        return possibleAttributes;
    }

    /**
     * Split the set by attribute; return a map from attribute value to example
     * set.
     *
     * @param attributeName
     * @return
     */
    public HashMap<Object, DataSet> splitBy(String attributeName) {
        HashMap<Object, DataSet> results = new HashMap<Object, DataSet>();
        for (Example e : this.examples) {
            Object attributeValue = e.get(attributeName).getValue();
            if (results.containsKey(attributeValue)) {
                results.get(attributeValue).add(e);
            } else {
                DataSet ds = new DataSet();
                ds.add(e);
                results.put(attributeValue, ds);
            }
        }
        return results;
    }
    
    /**
     * Return each value for this attribute in the example set
     * @param attributeName
     * @return 
     */
    public HashSet<Object> getValuesOf(String attributeName){
        HashSet<Object> values = new HashSet<Object>();
        for(Example e : this.examples){
            values.add(e.get(attributeName).getValue());
        }
        return values;
    }

    /**
     * Calculate entropy of an attribute in the example set; see description of
     * process on page 704, AIMAv3. Used by DecisionTreeLearner. Does not use
     * Util.information() as this method is vaguely named and does not fully
     * conform to formula on page 704.
     *
     * @return
     */
    public double getEntropyOf(String attributeName) {
        HashMap<Object, Integer> distribution = new HashMap<Object, Integer>();
        // count number of values v_k for variable V, page 704
        for (Example e : examples) {
            Object value = e.get(attributeName).getValue();
            if (distribution.containsKey(value)) {
                distribution.put(value, distribution.get(value) + 1);
            } else {
                distribution.put(value, 1);
            }
        }
        // normalize probability distribution, see page 493
        double[] normalizedDistribution = new double[distribution.keySet().size()];
        Iterator<Integer> iter = distribution.values().iterator();
        for (int i = 0; i < normalizedDistribution.length; i++) {
            normalizedDistribution[i] = iter.next();
        }
        normalizedDistribution = Util.normalize(normalizedDistribution);
        // calculate entropy H(V), page 704		
        double total = 0.0;
        for (double d : normalizedDistribution) {
            total += d * Util.log2(d);
        }
        return -1.0 * total;
    }

    /**
     * Calculate information gain--the expected reduction in entropy--after 
     * testing the given attribute; see page 704, AIMAv3. Used in
     * DecisionTreeLearner.
     * @param attributeName
     * @return 
     */
    public double getInformationGainOf(String attributeName) {
        HashMap<Object, DataSet> attributeValueMap = this.splitBy(attributeName);
        double totalSize = this.examples.size();
        double remainder = 0.0;
        for (Object attributeValue : attributeValueMap.keySet()) {
            // @todo "matchingValueSize" does not conform exactly to "p_k + p_n" described on page 704
            double matchingValueSize = attributeValueMap.get(attributeValue).size();
            remainder += (matchingValueSize / totalSize) * attributeValueMap.get(attributeValue).getEntropyOf(attributeName);
        }
        return this.getEntropyOf(attributeName) - remainder;
    }

    /**
     * Overrides equals() to compare with uncast Object
     *
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
        DataSet other = (DataSet) o;
        return examples.equals(other.examples);
    }

    /**
     * For use in equals()
     *
     * @return
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * Iterator
     *
     * @return
     */
    @Override
    public Iterator<Example> iterator() {
        return examples.iterator();
    }

    /**
     * Copies this set to a new object
     *
     * @return
     */
    public DataSet copy() {
        DataSet ds = new DataSet();
        for (Example e : examples) {
            ds.add(e);
        }
        return ds;
    }
}
