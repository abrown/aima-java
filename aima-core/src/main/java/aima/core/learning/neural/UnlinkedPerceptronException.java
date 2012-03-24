package aima.core.learning.neural;

/**
 * Thrown when a perceptron is ordered to send a signal, but does not
 * know where to send it to
 * @author andrew
 */
public class UnlinkedPerceptronException extends Exception {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -2507202395120358393L;

    /**
     * Creates a new instance of <code>UnlinkedPerceptronException</code> without detail message.
     */
    public UnlinkedPerceptronException() {
    }

    /**
     * Constructs an instance of <code>UnlinkedPerceptronException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public UnlinkedPerceptronException(String msg) {
        super(msg);
    }
}
