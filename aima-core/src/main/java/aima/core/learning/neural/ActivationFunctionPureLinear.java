package aima.core.learning.neural;

import java.io.Serializable;

/**
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class ActivationFunctionPureLinear implements ActivationFunctionInterface, Serializable {

    @Override
    public double activate(double parameter) {
        return parameter;
    }

    @Override
    public double derivate(double parameter) {
        return 1;
    }
}
