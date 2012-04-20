package aima.test.core.unit.learning.framework;

import aima.core.learning.framework.Attribute;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * AttributeTest
 * @author Andrew Brown
 */
public class AttributeTest {

    Attribute<String> attribute1;
    Attribute<Boolean> attribute2;
    Attribute<Double[]> attribute3;

    @Before
    public void setUp() {
        this.attribute1 = new Attribute("Height", "72 inches");
        this.attribute2 = new Attribute("WillRain?", false);
        this.attribute3 = new Attribute("List", new Double[]{0.1, 0.34, 0.5});
    }

    /**
     * Demonstrate usage of basic methods
     */
    @Test
    public void testBasics() {
        int expected = 837;
        Attribute<Integer> number = new Attribute("Random", 0);
        // set value from string
        number.setValue("837");
        Assert.assertTrue(number.getValue() == expected);
        // set name
        number.setName("Non-random");
        Assert.assertEquals("Non-random", number.getName());
    }
    
    /**
     * Demonstrate isValid() usage
     */
    @Test
    public void testIsValid(){
        Assert.assertEquals(false, this.attribute1.isValid(null));
        Assert.assertEquals(true, this.attribute1.isValid("..."));
    }

    /**
     * Test equals()
     */
    @Test
    public void testEquals() {
        Attribute<String> same = new Attribute<String>("Height", "72 inches");
        Assert.assertTrue(this.attribute1.equals(same));
        Attribute<String> not_same = new Attribute<String>("Length", "72 inches");
        Assert.assertTrue(!this.attribute1.equals(not_same));
        Attribute<String> not_same2 = new Attribute<String>("Height", "235 inches");
        Assert.assertTrue(!this.attribute1.equals(not_same2));
    }
}
