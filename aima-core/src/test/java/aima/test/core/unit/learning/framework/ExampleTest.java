package aima.test.core.unit.learning.framework;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.Example;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * ExampleTest
 * @author Andrew Brown
 */
public class ExampleTest {

    Example<Boolean> example1;
    Example<Integer> example2;

    @Before
    public void setUp() {
        // horse example
        this.example1 = new Example();
        this.example1.setOutput(true);
        this.example1.add(new Attribute<Boolean>("Four-legged", true));
        this.example1.add(new Attribute<Boolean>("Hairy", true));
        this.example1.add(new Attribute<Boolean>("Gallops", true));
        // addition example
        this.example2 = new Example();
        this.example2.setOutput(3);
        this.example2.add(new Attribute<Integer>("First-operand", 1));
        this.example2.add(new Attribute<String>("Operator", "+"));
        this.example2.add(new Attribute<Integer>("Second-operand", 2));
    }

    /**
     * Demonstrate basic usage of Example
     */
    @Test
    public void testBasics() {
        Assert.assertEquals(true, this.example1.get("Hairy").getValue());
        Assert.assertEquals("Operator", this.example2.getAttributes()[1].getName());
        Assert.assertTrue(3 == this.example2.getOutput());
    }

    /**
     * Test equals()
     */
    @Test
    public void testEquals() {
        // not a horse
        Example<Boolean> not_horse = new Example();
        not_horse.setOutput(false);
        not_horse.add(new Attribute<Boolean>("Four-legged", true));
        not_horse.add(new Attribute<Boolean>("Hairy", true));
        not_horse.add(new Attribute<Boolean>("Gallops", false));
        Assert.assertNotSame(not_horse, this.example1);
        //is a horse
        Example<Boolean> is_horse = new Example();
        is_horse.setOutput(true);
        is_horse.add(new Attribute<Boolean>("Four-legged", true));
        is_horse.add(new Attribute<Boolean>("Hairy", true));
        is_horse.add(new Attribute<Boolean>("Gallops", true));
        Assert.assertEquals(is_horse, this.example1);

    }
}
