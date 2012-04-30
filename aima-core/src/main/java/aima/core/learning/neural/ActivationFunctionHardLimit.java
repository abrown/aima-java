package aima.core.learning.neural;

import java.io.Serializable;

/**
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class ActivationFunctionHardLimit implements ActivationFunctionInterface, Serializable {

    @Override
    public double activate(double parameter) {
        if (parameter < 0.0) {
            return 0.0;
        } else {
            return 1.0;
        }
    }

    @Override
    public double derivate(double parameter) {
        return 1.0; // TODO: mathematically incorrect, but works with backpropagation
    }
}
