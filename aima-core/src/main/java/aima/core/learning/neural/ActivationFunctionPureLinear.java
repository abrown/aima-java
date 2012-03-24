package aima.core.learning.neural;

import java.io.Serializable;

/**
 * @author Ravi Mohan
 * 
 */
public class ActivationFunctionPureLinear implements ActivationFunctionInterface, Serializable {

    public double activate(double parameter) {
        return parameter;
    }

    public double derivate(double parameter) {
        return 1;
    }
}
