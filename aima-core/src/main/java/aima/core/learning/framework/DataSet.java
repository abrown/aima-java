package aima.core.learning.framework;

import java.util.HashMap;
import java.util.Iterator;
import aima.core.util.Util;
import java.util.ArrayList;

/**
 * Provides methods for representing a set of learning examples
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
     * @param e 
     */
    public void add(Example e) {
        examples.add(e);
    }

    /**
     * Returns the size of the set
     * @return 
     */
    public int size() {
        return examples.size();
    }

    /**
     * Returns the example given an index
     * @param number
     * @return 
     */
    public Example getExample(int index) {
        return examples.get(index);
    }

    /**
     * Removes the example given from the data set
     * @todo remove this
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
     * Returns the distribution
     * @todo explain better...
     * @return 
     */
    public double getInformationFor(String attributeName) {
        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        for (Example e : examples) {
            String val = e.get(attributeName).toString();
            if (counts.containsKey(val)) {
                counts.put(val, counts.get(val) + 1);
            } else {
                counts.put(val, 1);
            }
        }
        double[] data = new double[counts.keySet().size()];
        Iterator<Integer> iter = counts.values().iterator();
        for (int i = 0; i < data.length; i++) {
            data[i] = iter.next();
        }
        data = Util.normalize(data);
        return Util.information(data);
    }

    /**
     * Splits the set by attribute
     * @param attributeName
     * @return 
     */
    public HashMap<String, DataSet> splitByAttribute(String attributeName) {
        HashMap<String, DataSet> results = new HashMap<String, DataSet>();
        for (Example e : examples) {
            String val = e.get(attributeName).toString();
            if (results.containsKey(val)) {
                results.get(val).add(e);
            } else {
                DataSet ds = new DataSet();
                ds.add(e);
                results.put(val, ds);
            }
        }
        return results;
    }

    /**
     * Overrides equals() to compare with uncast Object
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
     * @return 
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * Iterator
     * @return 
     */
    public Iterator<Example> iterator() {
        return examples.iterator();
    }

    /**
     * Copies this set to a new object
     * @return 
     */
    public DataSet copy() {
        DataSet ds = new DataSet();
        for (Example e : examples) {
            ds.add(e);
        }
        return ds;
    }

//    /**
//     * @todo explain better
//     * @param parameterName
//     * @return 
//     */
//    public double calculateGainFor(String parameterName) {
//        HashMap<String, DataSet> hash = splitByAttribute(parameterName);
//        double totalSize = examples.size();
//        double remainder = 0.0;
//        for (String parameterValue : hash.keySet()) {
//            double reducedDataSetSize = hash.get(parameterValue).examples.size();
//            remainder += (reducedDataSetSize / totalSize)
//                    * hash.get(parameterValue).getInformationFor();
//        }
//        return getInformationFor() - remainder;
//    }
//    /**
//     * Returns the attribute names
//     * @return 
//     */
//    public List<String> getAttributeNames() {
//        return specification.getAttributeNames();
//    }
//
//    /**
//     * Returns the target attribute name
//     * @return 
//     */
//    public String getTargetAttributeName() {
//        return specification.getTarget();
//    }
//
//    /**
//     * Returns a new empty data set
//     * @todo move to factory
//     * @return 
//     */
//    public DataSet emptyDataSet() {
//        return new DataSet(specification);
//    }
//
//    /**
//     * Sets the specification for this object
//     * @todo remove this
//     * @param specification the specification to set. USE SPARINGLY for testing 
//     * etc .. makes no semantic sense
//     */
//    public void setSpecification(DataSetSpecification specification) {
//        this.specification = specification;
//    }
//
//    /**
//     * Wrapper for specification.getPossibleAttributeValues()
//     * @todo remove this
//     * @param attributeName
//     * @return 
//     */
//    public List<String> getPossibleAttributeValues(String attributeName) {
//        return specification.getPossibleAttributeValues(attributeName);
//    }
//
//    /**
//     * Filters the set, returning only the results that match the given 
//     * attribute name and value
//     * @todo rename as filter()
//     * @param attributeName
//     * @param attributeValue
//     * @return 
//     */
//    public DataSet matchingDataSet(String attributeName, String attributeValue) {
//        DataSet ds = new DataSet(specification);
//        for (Example e : examples) {
//            if (e.getAttributeValueAsString(attributeName).equals(
//                    attributeValue)) {
//                ds.add(e);
//            }
//        }
//        return ds;
//    }
//
//    /**
//     * Returns all attributes that are not the target
//     * @return 
//     */
//    public List<String> getNonTargetAttributes() {
//        return Util.removeFrom(getAttributeNames(), getTargetAttributeName());
//    }
}
