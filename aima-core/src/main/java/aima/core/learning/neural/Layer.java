package aima.core.learning.neural;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Neural network layer
 *
 * @author Andrew Brown
 */
public class Layer implements Iterable<Perceptron>, Serializable {

    /**
     * List of perceptrons in this layer
     */
    public ArrayList<Perceptron> perceptrons;

    /**
     * Constructor
     *
     * @param count
     * @param g
     */
    public Layer(int count, ActivationFunctionInterface g, double sensitivity) {
        // setup
        this.perceptrons = new ArrayList<Perceptron>();
        // create perceptrons
        for (int i = 0; i < count; i++) {
            this.perceptrons.add(new Perceptron(g, sensitivity));
        }
    }

    /**
     * Connects a layer to another layer; every perceptron is connected to every
     * perceptron in the next layer
     *
     * @param downstream
     */
    public void connectTo(Layer downstream) {
        for (Perceptron a : this.perceptrons) {
            for (Perceptron b : downstream.perceptrons) {
                a.addOutput(b);
            }
        }
    }

    /**
     * Sends initial input data into the network
     *
     * @param input
     * @throws SizeDifferenceException
     */
    public void in(DataList input) throws SizeDifferenceException, WrongSizeException {
        if (input.size() != this.size()) {
            throw new SizeDifferenceException("Dataset size (" + input.size() + ") and Layer size (" + this.size() + ") do not match");
        }
        // send to perceptrons
        for (int i = 0; i < this.perceptrons.size(); i++) {
            this.perceptrons.get(i).in(input.get(i));
        }
    }

    /**
     * Receives final output data from the network
     *
     * @return
     */
    public DataList out() {
        DataList output = new DataList(this.perceptrons.size());
        // wait until all processing is complete
        boolean complete;
        do {
            complete = true;
            for (int i = 0; i < this.perceptrons.size(); i++) {
                if (!this.perceptrons.get(i).isComplete()) {
                    complete = false;
                }
            }
        } while (!complete);
        // get values
        for (Perceptron p : this.perceptrons) {
            output.add(p.result);
        }
        // return
        return output;
    }

    /**
     * Backpropagates errors from the given correct result
     *
     * @param expected_output
     * @throws SizeDifferenceException a
     */
    public void backpropagate(DataList expected_output) throws SizeDifferenceException {
        if (expected_output.size() != this.size()) {
            throw new SizeDifferenceException("Data size (" + expected_output.size() + ") and Layer size (" + this.size() + ") do not match");
        }
        // send to upstream perceptrons
        for (int i = 0; i < this.perceptrons.size(); i++) {
            this.perceptrons.get(i).backpropagate_in(expected_output.get(i));
        }
    }

    /**
     * Returns the number of perceptrons in this layer
     *
     * @return
     */
    public int size() {
        return this.perceptrons.size();
    }

    /**
     * Makes the layer Iterable
     *
     * @return
     */
    @Override
    public LayerIterator iterator() {
        return new LayerIterator();
    }

    /**
     * Iterator for the layer
     */
    private class LayerIterator implements Iterator<Perceptron> {

        /**
         * Tracks the location in the list
         */
        private int index = 0;

        /**
         * Checks whether the list is empty or ended
         *
         * @return
         */
        @Override
        public boolean hasNext() {
            return (this.index < Layer.this.perceptrons.size());
        }

        /**
         * Returns the next element
         *
         * @return
         */
        @Override
        public Perceptron next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            Perceptron next = Layer.this.perceptrons.get(this.index);
            this.index++;
            return next;
        }

        /**
         * Removes an element; not supported in this implementation
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}