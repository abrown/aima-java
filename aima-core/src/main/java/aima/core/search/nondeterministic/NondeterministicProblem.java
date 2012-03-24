/*
 * @copyright Copyright 2012 Andrew Brown. All rights reserved.
 * @license GNU/GPL.
 */
package aima.core.search.nondeterministic;
import aima.core.search.framework.*;

/**
 * Non-deterministic problems may have multiple results for a given state and
 * action; this class handles these results by extending Problem and replacing
 * ResultFunction (one result) with ResultsFunction (a set of results).
 * @author Andrew Brown
 */
public class NondeterministicProblem extends Problem {

    protected ResultsFunction resultsFunction;

    /**
     * Constructor
     * @param initialState
     * @param actionsFunction
     * @param resultFunction
     * @param goalTest 
     */
    public NondeterministicProblem(Object initialState, ActionsFunction actionsFunction,
            ResultsFunction resultsFunction, GoalTest goalTest) {
        this(initialState, actionsFunction, resultsFunction, goalTest, new DefaultStepCostFunction());
    }

    /**
     * Constructor
     * @param initialState
     * @param actionsFunction
     * @param resultsFunction
     * @param goalTest
     * @param stepCostFunction 
     */
    public NondeterministicProblem(Object initialState, ActionsFunction actionsFunction,
            ResultsFunction resultsFunction, GoalTest goalTest, StepCostFunction stepCostFunction) {
        this.initialState = initialState;
        this.actionsFunction = actionsFunction;
        this.resultsFunction = resultsFunction;
        this.goalTest = goalTest;
        this.stepCostFunction = stepCostFunction;
    }

    /**
     * Unsupported constructor
     */
    public NondeterministicProblem(Object initialState, ActionsFunction actionsFunction,
            ResultFunction resultFunction, GoalTest goalTest) {
        throw new UnsupportedOperationException("Use ResultsFunction for non-deterministic problems.");
    }

    /**
     * Unsupported constructor
     */
    public NondeterministicProblem(Object initialState, ActionsFunction actionsFunction,
            ResultFunction resultFunction, GoalTest goalTest,
            StepCostFunction stepCostFunction) {
        throw new UnsupportedOperationException("Use ResultsFunction for non-deterministic problems.");
    }

    /**
     * Returns the description of what each action does.
     * @return the description of what each action does.
     */
    public ResultFunction getResultFunction() {
        throw new UnsupportedOperationException("Use getResultsFunction() for non-deterministic problems.");
    }

    /**
     * Returns the description of what each action does.
     * @return the description of what each action does.
     */
    public ResultsFunction getResultsFunction() {
        return this.resultsFunction;
    }
}
