/*
 * @copyright Copyright 2012 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package aima.core.search.nondeterministic;

import aima.core.agent.Action;
import aima.core.search.framework.Metrics;
import java.util.Set;

/**
 * Implements an AND-OR search tree with conditional plans according to the
 * algorithm explained on pages 135-136 of AIMAv3.
 * @author Andrew Brown
 */
public class AndOrSearch {

    protected int expandedNodes;

    /**
     * Searches through state space and returns a conditional plan for the 
     * given problem. The conditional plan is a list of either an action or
     * an if-then construct (consisting of a list of states and consequent
     * actions). The final product, when printed, resembles the contingency
     * plan on page 134.
     * 
     * This function is equivalent to the following on page 136:
     * 
     * <pre><code>
     * function AND-OR-GRAPH-SEARCH(problem) returns a conditional plan, or failure
     *  OR-SEARCH(problem.INITIAL-STATE, problem, [])
     * </pre></code>
     * 
     * @param problem
     * @return
     * @throws Exception 
     */
    public Plan search(NondeterministicProblem problem) throws Exception {
        return this.or_search(problem.getInitialState(), problem, new Plan());
    }

    /**
     * Returns a conditional plan or null on failure; this function is equivalent
     * to the following on page 136:
     * 
     * <pre><code>
     * function OR-SEARCH(state, problem, path) returns a conditional plan, or failure
     *  if problem.GOAL-TEST(state) then return the empty plan
     *  if state is on path then return failure
     *  for each action in problem.ACTIONS(state) do
     *      plan = AND-SEARCH(RESULTS(state, action), problem, [state|path])
     *      if plan != failure then return [action|plan]
     *  return failure
     * </pre></code>
     * 
     * @param state
     * @param problem
     * @param path
     * @return 
     */
    public Plan or_search(Object state, NondeterministicProblem problem, Plan path) {
        // do metrics
        this.expandedNodes++;
        // check goals
        if (problem.isGoalState(state)) {
            return new Plan();
        }
        // check loops
        if (path.contains(state)) {
            return null;
        }
        // check every possible action at an OR node
        for (Action action : problem.getActionsFunction().actions(state)) {
            Plan plan = this.and_search(problem.getResultsFunction().results(state, action), problem, path.prepend(state));
            if (plan != null) {
                return plan.prepend(action);
            }
        }
        // default return
        return null;
    }

    /**
     * Returns a conditional plan or null on failure; this function is equivalent
     * to the following on page 136:
     * 
     * <pre><code>
     * function AND-SEARCH(states, problem, path) returns a conditional plan, or failure
     *  for each s_i in states do
     *      plan_i = OR-SEARCH(s_i, problem, path)
     *      if plan_i == failure then return failure
     *  return [if s_1 then plan_1 else ... if s_n-1 then plan_n-1 else plan_n]
     * </pre></code>
     * 
     * @param states
     * @param problem
     * @param path
     * @return 
     */
    public Plan and_search(Set<Object> states, NondeterministicProblem problem, Plan path) {
        // do metrics
        this.expandedNodes++;
        // check every possible outcome from an AND node
        IfThen if_then = new IfThen();
        for (Object s : states) {
            Plan plan = this.or_search(s, problem, path);
            if_then.add(s, plan);
            if (plan == null) {
                return null;
            }
        }
        // default return
        return new Plan(if_then);
    }

    /**
     * Returns all the metrics of the node expander.
     * @return all the metrics of the node expander.
     */
    public Metrics getMetrics() {
        Metrics result = new Metrics();
        result.set("expandedNodes", this.expandedNodes);
        return result;
    }
}
