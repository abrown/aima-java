package aima.core.search.nondeterministic;

import aima.core.agent.Action;
import aima.core.agent.State;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Andrew Brown
 */
public class Path extends LinkedList<Object> {

    /**
     * Constructor
     *
     * @param states
     */
    public void Path(Object... states) {
        for (int i = 0; i < states.length; i++) {
            this.add(states[i]);
        }
    }

    /**
     * Appends a step to the plan and returns itself
     *
     * @param step
     * @return
     */
    public Path append(Object state) {
        this.offerLast(state);
        return this;
    }

    /**
     * Prepends a step to the plan and returns itself
     *
     * @param step
     * @return
     */
    public Path prepend(Object state) {
        this.offerFirst(state);
        return this;
    }
}
