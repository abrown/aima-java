package aima.core.learning.framework;

/**
 * Defines an attribute of an example; attributes are the key-value pairs that
 * make up an Example. See pages 699 and 700 of AIMAv3.
 *
 * @author Ravi Mohan
 * @author Andrew Brown
 */
public class Attribute<E> implements Cloneable {

    String name;
    E value;

    /**
     * Constructor
     *
     * @param name
     * @param value
     */
    public Attribute(String name, E value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Constructor
     *
     * @param value
     */
    public Attribute(E value) {
        this.name = "Unnamed Attribute";
        this.value = value;
    }

    /**
     * Sets the attribute name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the attribute name
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the attribute value
     *
     * @param value
     */
    public void setValue(E value) {
        this.value = value;
    }

    /**
     * Sets the attribute value auto-magically from a String to the generic
     * type.
     *
     * @param value
     */
    public void setValue(String value) {
        try {
            Class type = this.value.getClass();
            this.value = (E) type.getDeclaredConstructor(String.class).newInstance(value);
        } catch (Exception e) {
            this.value = null;
        }
    }

    /**
     * Returns the attribute value
     *
     * @return
     */
    public E getValue() {
        return (E) this.value;
    }

    /**
     * Determines whether the attribute value is a valid one; override this
     * method in descendants
     *
     * @param value
     * @return
     */
    public boolean isValid(E value) {
        if (value != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a string representation of the value of this attribute
     *
     * @return
     */
    @Override
    public String toString() {
        return this.value.toString();
    }

    /**
     * Auto-generate hash code
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 71 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    /**
     * Auto-generate equals()
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Attribute<E> other = (Attribute<E>) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    /**
     * Clone
     *
     * @return
     */
    @Override
    public Attribute<E> clone() {
        Attribute<E> copy = new Attribute(this.getName(), this.getValue());
        return copy;
    }
}
