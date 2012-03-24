package aima.core.learning.neural;

/**
 * Thrown when the inputs to a perceptron fall outside allowed bounds,
 * i.e. 0.0 and 1.0.
 * @author andrew
 */
public class WrongSizeException extends Exception {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -6640902913211094231L;

    /**
     * Creates a new instance of <code>WrongSizeException</code> without detail message.
     */
    public WrongSizeException() {
    }

    /**
     * Constructs an instance of <code>WrongSizeException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public WrongSizeException(String msg) {
        super(msg);
    }
}
