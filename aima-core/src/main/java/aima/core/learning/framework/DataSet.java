package aima.core.learning.framework;

import aima.core.learning.data.DataResource;
import aima.core.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Provides methods for representing a set of learning examples
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class DataSet implements Iterable<Example>, Cloneable {

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
     * Removes the example given from the data set
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
     *
     * @param attributeName
     * @param attributeValue
     * @return
     */
    public DataSet find(String attributeName, Object attributeValue) {
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
    public <T> HashMap<T, DataSet> splitBy(String attributeName) {
        HashMap<T, DataSet> results = new HashMap<T, DataSet>();
        for (Example e : this.examples) {
            T attributeValue = (T) e.get(attributeName).getValue();
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
     * Return each distinct value for this attribute in the example set
     *
     * @param attributeName
     * @return
     */
    public <T> HashSet<T> getValuesOf(String attributeName) {
        HashSet<T> values = new HashSet<T>();
        for (Example e : this.examples) {
            values.add((T) e.get(attributeName).getValue());
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
     * Calculate entropy of output values in the example set; see 
     * getEntropyOf() for details.
     *
     * @return
     */
    public double getEntropyOfOutput(){
        HashMap<Object, Integer> distribution = new HashMap<Object, Integer>();
        // count number of values v_k for variable V, page 704
        for (Example e : examples) {
            Object value = e.getOutput();
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
     *
     * @param attributeName
     * @return
     */
    public double getInformationGainOf(String attributeName) {
        HashMap<Object, DataSet> attributeValueMap = this.splitBy(attributeName);
        double totalSize = this.examples.size();
        double remainder = 0.0;
        for (Object attributeValue : attributeValueMap.keySet()) {
            // this may not look exactly like page 704, but is equivalent:
            double matchingValueSize = attributeValueMap.get(attributeValue).size();
            double outputEntropy = attributeValueMap.get(attributeValue).getEntropyOfOutput();
            remainder += (matchingValueSize / totalSize) * outputEntropy;
        }
        return this.getEntropyOfOutput() - remainder;
    }

    /**
     * Returns a set of examples from a given CSV file and a sample Example; CSV
     * file should be located in the "aima/core/learning/data" directory.
     *
     * @param filename
     * @param sample
     * @param separator
     * @return
     * @throws Exception
     */
    public static DataSet loadFrom(URL url, String separator, Example sample) throws IOException {
        DataSet ds = new DataSet();
        // check cache
        InputStream stream;
        if( DataResource.isCached(url.getFile()) ){
            stream = DataResource.get(url.getFile());
        }
        else{
            stream = url.openStream();
        }
        // loop through stream lines
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
            ds.add(DataSet.loadLine(line, separator, sample));
        }
        reader.close();
        // save to cache if necessary
        if( !DataResource.isCached(url.getFile()) ){
            DataResource.put(url.getFile(), url.openStream());
        }
        // return
        return ds;
    }

    
    
    /**
     * Returns an Example from a CSV line
     *
     * @param line
     * @param separator
     * @param sample
     * @return
     */
    private static <T> Example<T> loadLine(String line, String separator, Example<T> sample) {
        // set attributes
        String[] parts = line.split(separator);
        Example<T> new_example = sample.clone();
        for (int i = 0; i < sample.inputAttributes.size(); i++) {
            if (i < parts.length) {
                Attribute<?> old_attribute = (Attribute) sample.inputAttributes.get(i);
                Attribute<?> new_attribute = old_attribute.clone();
                new_attribute.setValue(parts[i].trim());
                new_example.inputAttributes.remove(old_attribute);
                new_example.add(new_attribute);
            }
        }
        // set output value; uses reflection to match the generic outputValue type
        try {
            String last_field = parts[parts.length - 1].trim();
            T value = (T) sample.outputValue.getClass().getDeclaredConstructor(String.class).newInstance(last_field);
            new_example.setOutput(value);
        } catch (Exception e) {
            new_example.outputValue = null;
        }
        // return
        return new_example;
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
     * Clone
     *
     * @return
     */
    @Override
    public DataSet clone() {
        DataSet copy = new DataSet();
        for (Example e : this) {
            copy.add(e.clone());
        }
        return copy;
    }

    /**
     * Return string representation
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("DataSet:[\n");
        for (Example e : this.examples) {
            s.append(" ");
            s.append(e);
            s.append(",\n");
        }
        s.append("]");
        return s.toString();
    }
}
