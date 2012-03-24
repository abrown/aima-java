package aima.core.learning.neural;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests perceptron functionality
 * @author andrew
 */
public class PerceptronTest {

    Perceptron p;

    @Before
    public void setUp() throws Exception {
        this.p = new Perceptron(new ActivationFunctionHardLimit(), 1.0);
        // create inputs
        for (int i = 0; i < 10; i++) {
            Perceptron input = new Perceptron(new ActivationFunctionHardLimit(), 1.0);
            this.p.addInput(input, 0.2);
        }
        // create outputs
        for (int j = 0; j < 10; j++) {
            Perceptron output = new Perceptron(new ActivationFunctionHardLimit(), 1.0);
            this.p.addOutput(output);
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInDouble() throws WrongSizeException {
        Perceptron unconnected = new Perceptron(new ActivationFunctionPureLinear(), 1.0);
        double expected = 0.25;
        unconnected.in(expected);
        double actual = unconnected.result;
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testInPerceptronDouble() throws WrongSizeException {
        for (Perceptron input : this.p.inputs) {
            input.in(0.11);
        }
        double expected = 1.0;
        double actual = this.p.result;
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testOut() {
        // setup 
        this.p.outputs.get(5).weights.put(this.p, 1.0);
        this.p.out(-0.4); // negative numbers on a hard limit activation function return 0
        // test
        double expected = 0.0;
        double actual = this.p.outputs.get(5).result;
        assertEquals(expected, actual, 0.0);
    }
    
    /**
     * Demonstrates training a single perceptron; training for 0.1 results
     * in far more iterations than training for 0.9 below; TODO
     * @throws WrongSizeException 
     */
    @Test
    public void testBackpropagationLow() throws WrongSizeException{
        Perceptron input = new Perceptron(new ActivationFunctionPureLinear(), 1.0);
        Perceptron output = new Perceptron(new ActivationFunctionPureLinear(), 1.0);
        input.addOutput(output);
        // train
        double expected = 0.1;
        double actual = 0.0;
        int count = 0;
        while( Math.abs(expected - actual) > 0.00001 ){
            count++;
            input.in(expected);
            actual = output.result;
            output.backpropagate_in(expected);
        }
        // test
        System.out.println("After "+count+" training iterations.");
        assertEquals(expected, actual, 0.0001);
    }
    
    /**
     * Demonstrates training a single perceptron; use of 0.9 is much faster
     * than above
     * @throws WrongSizeException 
     */
    @Test
    public void testBackpropagationHigh() throws WrongSizeException{
        Perceptron input = new Perceptron(new ActivationFunctionPureLinear(), 1.0);
        Perceptron output = new Perceptron(new ActivationFunctionPureLinear(), 1.0);
        input.addOutput(output);
        // train
        double expected = 0.9;
        double actual = 0.0;
        int count = 0;
        while( Math.abs(expected - actual) > 0.00001 ){
            count++;
            input.in(expected);
            actual = output.result;
            output.backpropagate_in(expected);
        }
        // test
        System.out.println("After "+count+" training iterations.");
        assertEquals(expected, actual, 0.0001);
    }
}