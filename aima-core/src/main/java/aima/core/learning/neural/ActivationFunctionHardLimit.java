package aima.core.learning.neural;

import java.io.Serializable;

/**
 * @author Ravi Mohan
 * 
 */
public class ActivationFunctionHardLimit implements ActivationFunctionInterface, Serializable {

    public double activate(double parameter) {
        if (parameter < 0.0) {
            return 0.0;
        } else {
            return 1.0;
        }
    }

    public double derivate(double parameter) {
        return 1.0; // TODO: mathematically incorrect, but works with backpropagation
    }
}
