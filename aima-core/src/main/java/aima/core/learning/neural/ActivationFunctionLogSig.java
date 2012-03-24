package aima.core.learning.neural;

import java.io.Serializable;

/**
 * @author Ravi Mohan
 * 
 */
public class ActivationFunctionLogSig implements ActivationFunctionInterface, Serializable {

    public double activate(double parameter) {
        return 1.0 / (1.0 + Math.pow(Math.E, (-1.0 * parameter)));
    }

    public double derivate(double parameter) {
        // parameter = induced field
        // e == activation
        double e = 1.0 / (1.0 + Math.pow(Math.E, (-1.0 * parameter)));
        return e * (1.0 - e);
    }
}
