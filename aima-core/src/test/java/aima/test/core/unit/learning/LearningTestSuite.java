package aima.test.core.unit.learning;

import aima.test.core.unit.learning.framework.AttributeTest;
import aima.test.core.unit.learning.framework.DataSetTest;
import aima.test.core.unit.learning.framework.ExampleTest;
import aima.test.core.unit.learning.inductive.DLTestTest;
import aima.test.core.unit.learning.inductive.DecisionListTest;
import aima.test.core.unit.learning.learners.DecisionTreeTest;
import aima.test.core.unit.learning.learners.EnsembleLearningTest;
import aima.test.core.unit.learning.learners.LearnerTests;
import aima.test.core.unit.learning.neural.NeuralNetworkTest;
import aima.test.core.unit.learning.neural.PerceptronTest;
import aima.test.core.unit.learning.reinforcement.agent.PassiveADPAgentTest;
import aima.test.core.unit.learning.reinforcement.agent.PassiveTDAgentTest;
import aima.test.core.unit.learning.reinforcement.agent.QLearningAgentTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({DataSetTest.class, ExampleTest.class, AttributeTest.class,
    DecisionListTest.class, DLTestTest.class, DecisionTreeTest.class,
    EnsembleLearningTest.class, LearnerTests.class,
    NeuralNetworkTest.class, PerceptronTest.class,
    PassiveADPAgentTest.class, PassiveTDAgentTest.class,
    QLearningAgentTest.class})
public class LearningTestSuite {
}
