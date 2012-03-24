package aima.core.learning.neural;

import java.util.ArrayList;

/**
 * Extends an ArrayList to simplify network input and output; with this class,
 * we avoid multi-dimensional arrays and simplify creation of network inputs.
 * @author andrew
 */
public class DataList extends ArrayList<Double> {

    /**
     * Difference between doubles under which they will still be considered equal
     */
    public static double tolerance = 0.000001;

    /**
     * Constructor; pass elements as parameters
     * @example
     * DataList d = new DataList(0.1, 0.2, 0.3, 0.4);
     * @param set 
     */
    public DataList(Double... values) {
        for (Double d : values) {
            this.add(d);
        }
    }

    /**
     * Constructor
     * @param set 
     */
    public DataList(double[] set) {
        for (double d : set) {
            this.add(new Double(d));
        }
    }

    /**
     * Constructor
     * @param set 
     */
    public DataList(ArrayList<Double> set) {
        for (Double d : set) {
            this.add(d);
        }
    }

    /**
     * Constructor
     * @param size 
     */
    public DataList(int size) {
        super(size);
    }

    /**
     * Calculate error rate between two datasets
     * @param actual
     * @param expected
     * @return
     * @throws SizeDifferenceException 
     */
    public static double getErrorRate(DataList actual, DataList expected) throws SizeDifferenceException {
        if (actual.size() != expected.size()) {
            throw new SizeDifferenceException("To compare datasets, they must be the same size");
        }
        // see page 708: "define the error rate of actual hypothesis as the proportion of mistakes it makes"
        int errors = 0;
        for (int i = 0; i < actual.size(); i++) {
            if (Math.abs(actual.get(i) - expected.get(i)) < DataList.tolerance) {
                errors++;
            }
        }
        // return
        return errors / actual.size();
    }
}
