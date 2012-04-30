package aima.core.learning.neural;

/**
 * Provides a contract for activation functions
 *
 * @author Andrew Brown
 */
public interface ActivationFunctionInterface {

    /**
     * Activate (or not) the perceptron with the given signal
     *
     * @param signal
     * @return
     */
    double activate(double signal);

    /**
     * Get derivative of this ActivationFunction with the given signal
     *
     * @param signal
     * @return
     */
    double derivate(double signal);
}
