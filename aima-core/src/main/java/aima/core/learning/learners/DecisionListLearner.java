package aima.core.learning.learners;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.inductive.DecisionList;
import aima.core.learning.inductive.DecisionListTest;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Uses DECISION-LIST-LEARNING algorithm, page 717, AIMAv3, to build a series of
 * tests (DecisionListTest); the chain of tests is used to predict outcomes for
 * new examples.
 *
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Andrew Brown
 */
public class DecisionListLearner implements Learner {

    private DecisionList decisionList;
    private HashSet<Attribute> possibleAttributes;

    /**
     * Constructor
     */
    public DecisionListLearner() {
    }

    /**
     * Return the decision list for this learner
     *
     * @return
     */
    public DecisionList getDecisionList() {
        return this.decisionList;
    }

    /**
     * Implement DECISION-LIST-LEARNING, page 717, AIMAv3 <br/>
     * <pre>
     * function DECISION-LIST-LEARNING(examples) returns a decision list, or failure
     *  if examples is empty then return the trivial decision list "No"
     *  t &lt;- a test that matches a nonempty subset examples_t of examples, such that the members of examples_t are all positive or negative
     *  if there is no such t then return failure
     *  if the examples in examples_t are positive then o &lt- Yes else o &lt;- No
     *  return a decision list with initial test t and outcome o and remaining tests given by DECISION-LIST-LEARNING(examples - examples_t)
     * </pre>
     *
     * @param examples
     * @return
     */
    public DecisionList decisionListLearning(DataSet examples) {
        if (examples.size() == 0) {
            return new DecisionList();
        }
        // find test that returns largest list of positive or negative results
        DecisionListTest t = this.getFirstClassifyingTest(examples);
        // check t
        if (t == null) {
            return new DecisionList();
        }
        // 
        DataSet matched = t.getMatchingExamples(examples);
        // set positive
        if (matched.examples.get(0).getOutput() != null) {
            t.setOutput(true);
        } // set negative
        else {
            t.setOutput(false);
        }
        // return
        DecisionList list = new DecisionList();
        list.add(t);
        return list.mergeWith(this.decisionListLearning(t.getNonMatchingExamples(examples)));
    }

    /**
     * Return first test that classifies a subset of examples returning the same
     * value
     *
     * @param examples
     * @return
     */
    private DecisionListTest getFirstClassifyingTest(DataSet examples) {
        // attempt all single attribute tests
        for (Attribute a : examples.getPossibleAttributes()) {
            DecisionListTest test = new DecisionListTest();
            test.add(a);
            DataSet matched = test.getMatchingExamples(examples);
            if (matched.size() > 0 && this.allExamplesHaveSameOutput(matched)) {
                test.setOutput(matched.getExample(0).getOutput());
                return test;
            }
        }
        // return
        return null;
    }

    /**
     * Test whether all of the examples in an example set return the same output
     *
     * @param matched
     * @return
     */
    private boolean allExamplesHaveSameOutput(DataSet matched) {
        Object value = matched.getExample(0).getOutput();
        for (Example e : matched.examples) {
            if (!e.getOutput().equals(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Trains the decision list given a set of examples
     *
     * @param examples
     */
    @Override
    public void train(DataSet examples) {
        this.decisionList = this.decisionListLearning(examples);
    }

    /**
     * Predict an outcome for the given example
     *
     * @param e
     * @return the example's output value, or null
     */
    @Override
    public Object predict(Example e) {
        if (this.decisionList == null) {
            throw new RuntimeException("DecisionListLearner has not yet been trained with a data set.");
        }
        // predict and return
        return this.decisionList.predict(e);
    }

    /**
     * Returns the accuracy of the hypothesis on the specified set of examples
     *
     * @param examples the test data set.
     * @return the accuracy of the hypothesis on the specified set of examples
     * as an array like [#correct, #incorrect]
     */
    @Override
    public int[] test(DataSet examples) {
        int[] results = new int[]{0, 0};
        for (Example e : examples) {
            if (e.getOutput().equals(this.decisionList.predict(e))) {
                results[0]++;
            } else {
                results[1]++;
            }
        }
        return results;
    }
}
//
//    /**
//     * Get matching score
//     *
//     * @param examples
//     * @param test
//     * @return
//     */
//    private int getScore(DataSet examples, DecisionListTest test) {
//        int matching = 0;
//        int nonmatching = 0;
//        // match against all examples
//        for (Example e : examples) {
//            if (test.matches(e)) {
//                matching++;
//            } else {
//                nonmatching++;
//            }
//        }
//        // return highest score
//        return (matching >= nonmatching) ? matching : nonmatching;
//    }
//
//    /**
//     * Attempts to find the smallest test that matches a uniformly classified
//     * subset (page 717, AIMAv3) by producing all k-tests and sorting them; the
//     * score to sort by is calculated by dividing the subset size by the number
//     * of conjuncts in the test (thus prefering small tests).
//     *
//     * @param examples
//     * @param max_test_size
//     * @return
//     */
//    private DecisionListTest getSmallestClassifyingTest(final DataSet examples, final int max_test_size) {
//        // produce all tests
//        Iterator<DecisionListTest> iterator = this.getTestIterator(examples, max_test_size);
//        ArrayList<DecisionListTest> list = new ArrayList<DecisionListTest>();
//        while (iterator.hasNext()) {
//            DecisionListTest _t = iterator.next();
//            list.add(_t);
//        }
//        // sort
//        Collections.sort(list, new Comparator<DecisionListTest>() {
//
//            /**
//             * Store scores by hash code
//             */
//            HashMap<Integer, Integer> scores = new HashMap<Integer, Integer>();
//
//            /**
//             * Compare tests
//             */
//            @Override
//            public int compare(DecisionListTest a, DecisionListTest b) {
//                return this.getScore(a) - this.getScore(b);
//            }
//
//            /**
//             * Calculate a score for a DecisionListTest; the score is the max of
//             * matching and non-matching examples from the example set, divided
//             * by the number of conjuncts in the test
//             *
//             * @param a
//             */
//            private int getScore(DecisionListTest a) {
//                if (!this.scores.containsKey(a.hashCode())) {
//                    int matching = 0;
//                    int nonmatching = 0;
//                    // match against all examples
//                    for (Example e : examples) {
//                        if (a.matches(e)) {
//                            matching++;
//                        } else {
//                            nonmatching++;
//                        }
//                    }
//                    // save highest score
//                    int max = (matching >= nonmatching) ? matching : nonmatching;
//                    this.scores.put(a.hashCode(), max);
//                }
//                return (int) this.scores.get(a.hashCode()) / a.size();
//            }
//        });
//        // return
//        return list.get(0); //@todo should this be first or last?
//    }
//
//    /**
//     * Returns possible DecisionListTests as an iterator; combines attributes in
//     * all combinations starting with 1 per test, 2 per test, etc., up to k per
//     * test.
//     *
//     * @param examples
//     * @return
//     */
//    private Iterator<Attribute> getAttributeIterator(final DataSet examples) {
//        return new Iterator<Attribute>() {
//
//            int max_size;
//            int attribute = 0;
//            int value = 0;
//            HashMap<String, Object[]> values;
//            ArrayList<String> attributes;
//
//            /**
//             * Constructor
//             */
//            public void Iterator() {
//                this.max_size = k;
//                // create attributes
//                for (Attribute a : examples.getExample(0).getAttributes()) {
//                    this.attributes.add(a.getName());
//                }
//                // create values
//                for (String attributeName : this.attributes) {
//                    HashSet<Object> possible_values = new HashSet<Object>();
//                    for (Example e : examples) {
//                        possible_values.add(e.getOutput());
//                    }
//                    this.values.put(attributeName, possible_values.toArray());
//                }
//            }
//
//            /**
//             * Return whether more attributes can be created
//             */
//            @Override
//            public boolean hasNext() {
//                if (this.attribute >= this.attributes.size()) {
//                    String attributeName = this.attributes.get(this.attribute);
//                    if (this.value >= this.values.get(attributeName).length) {
//                        return false;
//                    }
//                }
//                return true;
//            }
//
//            /**
//             * Return next possible Attribute
//             *
//             */
//            @Override
//            public Attribute next() {
//                // create attributes
//                String attributeName = this.attributes.get(this.attribute);
//                Object attributeValue = this.values.get(attributeName)[this.value];
//                Attribute a = new Attribute(attributeName, attributeValue);
//                // cycle
//                this.value++;
//                if (this.value >= this.values.get(attributeName).length) {
//                    this.value = 0;
//                    this.attribute++;
//                    if (this.attribute >= this.attributes.size()) {
//                        this.attribute = 0;
//                    }
//                }
//                // return
//                return a;
//            }
//
//            @Override
//            public void remove() {
//                throw new UnsupportedOperationException();
//            }
//        };
//    }
//
//    private ArrayList<DecisionListTest> getPossibleTests(DataSet examples, int k) {
//        if (possibleTests == null) {
//
//            possibleTests = new ArrayList<DecisionListTest>();
//            Attribute[] possibleAttributes = examples.getPossibleAttributes().toArray(new Attribute[0]);
//            // create tests of up to k attribute size:
//            for (int h = 1; h <= k; h++) {
//                DecisionListTest test = new DecisionListTest();
//                // loop through possibles...
//                for (int i = 0; i < possibleAttributes.length; i++) {
//                    // ...picking h attributes each time
//                    for (int j = 0; j < h; j++) {
//                        int index = i + j;
//                        if (index < possibleAttributes.length) {
//                            test.add(possibleAttributes[index]);
//                        }
//                    }
//                }
//                // add to possible Tests
//            }
//        }
//        return possibleTests;
//    }
//
//    private DecisionListTest getValidTest(List<DecisionListTest> possibleTests, DataSet ds) {
//        for (DecisionListTest test : possibleTests) {
//            DataSet matched = test.getMatchingExamples(ds);
//            if (!(matched.size() == 0)) {
//                if (allExamplesHaveSameOutput(matched)) {
//                    return test;
//                }
//            }
//
//        }
//        return null;
//    }
//
//    public List<DecisionListTest> createDLTestsWithAttributeCount(DataSet ds, int i) {
//        if (i != 1) {
//            throw new RuntimeException(
//                    "For now DLTests with only 1 attribute can be craeted , not"
//                    + i);
//        }
//        List<String> nonTargetAttributes = ds.getNonTargetAttributes();
//        List<DecisionListTest> tests = new ArrayList<DecisionListTest>();
//        for (String ntAttribute : nonTargetAttributes) {
//            List<String> ntaValues = ds.getPossibleAttributeValues(ntAttribute);
//            for (String ntaValue : ntaValues) {
//
//                DecisionListTest test = new DecisionListTest();
//                test.add(ntAttribute, ntaValue);
//                tests.add(test);
//
//            }
//        }
//        return tests;
//    }
//}
//
///**
// * Represents all possible tests that can be created with up to k attributes.
// *
// * @author Andrew Brown
// */
//class AllPossibleTests implements Iterable<DecisionListTest> {
//
//    public int max_test_size = 3;
//
//    /**
//     * Constructor
//     */
//    public AllPossibleTests() {
//    }
//
//    /**
//     * Constructor
//     */
//    public AllPossibleTests(int max_test_size) {
//        this.max_test_size = max_test_size;
//    }
//
//    /**
//     * Return test iterator
//     */
//    public Iterator<DecisionListTest> iterator() {
//        return new TestIterator();
//    }
//
//    class TestIterator implements Iterator<DecisionListTest> {
//
//        HashSet<Attribute> cache = new HashSet<Attribute>();
//        int index = 0;
//
//        public boolean hasNext() {
//            return true;
//        }
//
//        @Override
//        public DecisionListTest next() {
//            DecisionListTest t = new DecisionListTest();
//
//            return t;
//        }
//
//        @Override
//        public void remove() {
//            throw new UnsupportedOperationException();
//        }
//    }
//}
//
///**
// * Represents all possible attributes that can be created from the given example
// * set.
// *
// * @author Andrew Brown
// */
//class AllPossibleAttributes implements Iterable<Attribute> {
//
//    DataSet examples;
//
//    /**
//     * Constructor
//     *
//     * @param examples
//     */
//    public AllPossibleAttributes(DataSet examples) {
//        this.examples = examples;
//    }
//
//    /**
//     * Creates every possible attribute for this example set
//     */
//    @Override
//    public Iterator<Attribute> iterator() {
//        return new AttributeIterator();
//    }
//
//    /**
//     * Makes the class Iterable
//     */
//    class AttributeIterator implements Iterator<Attribute> {
//
//        int attribute = 0;
//        int value = 0;
//        HashMap<String, Object[]> values;
//        ArrayList<String> attributes;
//
//        /**
//         * Constructor
//         */
//        public void Iterator() {
//            // create attributes
//            for (Attribute a : AllPossibleAttributes.this.examples.getExample(0).getAttributes()) {
//                this.attributes.add(a.getName());
//            }
//            // create values
//            for (String attributeName : this.attributes) {
//                HashSet<Object> possible_values = new HashSet<Object>();
//                for (Example e : AllPossibleAttributes.this.examples) {
//                    possible_values.add(e.getOutput());
//                }
//                this.values.put(attributeName, possible_values.toArray());
//            }
//        }
//
//        /**
//         * Return whether more attributes can be created
//         */
//        @Override
//        public boolean hasNext() {
//            if (this.attribute >= this.attributes.size()) {
//                String attributeName = this.attributes.get(this.attribute);
//                if (this.value >= this.values.get(attributeName).length) {
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        /**
//         * Return next possible Attribute
//         *
//         */
//        @Override
//        public Attribute next() {
//            // create attributes
//            String attributeName = this.attributes.get(this.attribute);
//            Object attributeValue = this.values.get(attributeName)[this.value];
//            Attribute a = new Attribute(attributeName, attributeValue);
//            // cycle
//            this.value++;
//            if (this.value >= this.values.get(attributeName).length) {
//                this.value = 0;
//                this.attribute++;
//                if (this.attribute >= this.attributes.size()) {
//                    this.attribute = 0;
//                }
//            }
//            // return
//            return a;
//        }
//
//        @Override
//        public void remove() {
//            throw new UnsupportedOperationException();
//        }
//    }
//}
