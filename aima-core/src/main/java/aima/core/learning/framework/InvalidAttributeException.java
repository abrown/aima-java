package aima.core.learning.framework;

/**
 * Thrown when a value does not meet the attribute-validity requirements
 * @author Andrew Brown
 */
public class InvalidAttributeException extends Exception {

    /**
     * Creates a new instance of <code>InvalidAttributeException</code> without detail message.
     */
    public InvalidAttributeException() {
    }

    /**
     * Constructs an instance of <code>InvalidAttributeException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InvalidAttributeException(String msg) {
        super(msg);
    }
}
