package aima.test.core.unit.learning;

import aima.test.core.unit.learning.framework.AttributeTest;
import aima.test.core.unit.learning.framework.DataSetTest;
import aima.test.core.unit.learning.framework.ExampleTest;
import aima.test.core.unit.learning.inductive.DecisionListTest;
import aima.test.core.unit.learning.inductive.DecisionTreeTest;
import aima.test.core.unit.learning.learners.*;
import aima.test.core.unit.learning.neural.NeuralNetworkTest;
import aima.test.core.unit.learning.neural.PerceptronTest;
import aima.test.core.unit.learning.reinforcement.agent.PassiveADPAgentTest;
import aima.test.core.unit.learning.reinforcement.agent.PassiveTDAgentTest;
import aima.test.core.unit.learning.reinforcement.agent.QLearningAgentTest;
import aima.test.core.unit.learning.reinforcement.agent.ReinforcementLearningAgentTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AttributeTest.class,
    DataSetTest.class,
    ExampleTest.class,
    DecisionListTest.class,
    DecisionTreeTest.class,
    AdaBoostLearnerTest.class,
    DecisionListLearnerTest.class,
    DecisionTreeLearnerTest.class,
    MajorityLearnerTest.class,
    NeuralNetworkLearnerTest.class,
    StumpLearnerTest.class,
    WeightedMajorityLearnerTest.class,
    NeuralNetworkTest.class,
    PerceptronTest.class,
    PassiveADPAgentTest.class,
    PassiveTDAgentTest.class,
    QLearningAgentTest.class,
    ReinforcementLearningAgentTest.class
})
public class LearningTestSuite {
}
