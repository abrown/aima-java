package aima.test.core.unit.learning.neural;

import java.io.File;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import aima.core.learning.neural.*;

/**
 * Tests neural network functionality
 * @author andrew
 */
public class NeuralNetworkTest {

    NeuralNetwork n;

    @Before
    public void setUp() {
        this.n = new NeuralNetwork(3, 20, new ActivationFunctionHardLimit(), 1.0);
    }

    /**
     * Example use of NeuralNetwork
     */
    @Test
    public void testUse() throws Exception {
        DataList input = new DataList(0.0, 0.1, 0.2, 0.3, 0.4, 0.5,
                0.6, 0.7, 0.8, 0.9, 0.10, 0.11, 0.12, 0.13, 0.14,
                0.15, 0.16, 0.17, 0.18, 0.19);
        // use
        DataList result = this.n.use(input);
        System.out.println(result.toString());
        // test...
    }

    /**
     * Tests saving to file
     */
    @Test
    public void testSave() throws Exception {
        File file = new File("test.txt");
        file.createNewFile();
        NeuralNetwork.save(file, this.n);
        System.out.println("Bytes written: " + file.length());
        if (file.length() < 10) {
            fail("No file data written");
        }
        file.delete();
    }

    /**
     * Tests loading from file
     */
    @Test
    public void testLoad() throws Exception {
        File file = new File("test2.txt");
        file.createNewFile();
        NeuralNetwork.save(file, this.n);
        NeuralNetwork actual = NeuralNetwork.load(file);
        assertEquals(this.n.layers.size(), actual.layers.size());
        assertEquals(this.n.layers.get(0).perceptrons.get(0).weights,
                actual.layers.get(0).perceptrons.get(0).weights);
        file.delete();
    }

    /**
     * Demonstrates the functionality of NeuralNetwork by implementing the
     * example two-bit adder from page 729, AIMA 3ed.
     * @throws SizeDifferenceException
     * @throws WrongSizeException 
     */
    @Test
    public void testBackPropagationAdder() throws SizeDifferenceException, WrongSizeException {
        // setup
        NeuralNetwork adder = new NeuralNetwork(3, 2, new ActivationFunctionHardLimit(), 1.0);
        DataList[] inputs = {
            new DataList(0.0, 0.0),
            new DataList(0.0, 1.0),
            new DataList(1.0, 0.0),
            new DataList(1.0, 1.0)
        };
        DataList[] outputs = {
            new DataList(0.0, 0.0),
            new DataList(0.0, 1.0),
            new DataList(0.0, 1.0),
            new DataList(1.0, 0.0)
        };
        // train
        int iterations = adder.train(inputs, outputs, 0.01);
        // test
        double expected = 0.01;
        double actual = adder.test(inputs, outputs);
        System.out.println("Error rate: " + actual + " after " + iterations + " iterations");
        if (actual > expected) {
            fail("Error rate " + actual + " must be less than " + expected);
        }
    }
}
