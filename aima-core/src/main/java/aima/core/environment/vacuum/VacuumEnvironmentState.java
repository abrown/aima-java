package aima.core.environment.vacuum;

import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a state in the Vacuum World
 *
 * @author Ciaran O'Reilly
 */
public class VacuumEnvironmentState implements EnvironmentState {

    private Map<String, VacuumEnvironment.LocationState> state;
    private Map<Agent, String> agentLocations;

    /**
     * Constructor
     */
    public VacuumEnvironmentState() {
        state = new LinkedHashMap<String, VacuumEnvironment.LocationState>();
        agentLocations = new LinkedHashMap<Agent, String>();
    }

    /**
     * Constructor
     *
     * @param locAState
     * @param locBState
     */
    public VacuumEnvironmentState(VacuumEnvironment.LocationState locAState,
            VacuumEnvironment.LocationState locBState) {
        this();
        state.put(VacuumEnvironment.LOCATION_A, locAState);
        state.put(VacuumEnvironment.LOCATION_B, locBState);
    }

    /**
     * Returns the agent location
     *
     * @param a
     * @return
     */
    public String getAgentLocation(Agent a) {
        return agentLocations.get(a);
    }

    /**
     * Sets the agent location
     *
     * @param a
     * @param location
     */
    public void setAgentLocation(Agent a, String location) {
        agentLocations.put(a, location);
    }

    /**
     * Returns the location state
     *
     * @param location
     * @return
     */
    public VacuumEnvironment.LocationState getLocationState(String location) {
        return state.get(location);
    }

    /**
     * Sets the location state
     *
     * @param location
     * @param s
     */
    public void setLocationState(String location,
            VacuumEnvironment.LocationState s) {
        state.put(location, s);
    }

    /**
     * Tests equality against another VacuumEnvironmentState
     *
     * @param s
     * @return
     */
    public boolean equals(VacuumEnvironmentState s) {
        if (!this.state.equals(s.state)) {
            return false;
        } else if (!this.agentLocations.equals(s.agentLocations)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Tests equality against another VacuumEnvironmentState; necessary for
     * AND-OR search to work
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        return this.equals((VacuumEnvironmentState) o);
    }

    /**
     * Returns a string representation of the environment
     *
     * @return
     */
    public String toString() {
        return this.state.toString();
    }
}