package aima.core.learning.knowledge;

import aima.core.learning.framework.Attribute;
import aima.core.learning.framework.Example;
import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.parsing.ast.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Extends Example to work with CURRENT-BEST-LEARNING (page 771, AIMAv3). This
 * class converts the attributes in an Example to Predicates in a first order
 * logic sentence.
 *
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 */
public class FOLExample extends Example {

    public String classificationPredicate = "UnknownClassifier";

    /**
     * Returns the constant representing this example; uses hashCode() as it is
     * the most convenient pattern in Java for uniquely representing this
     * object.
     *
     * @return
     */
    public Constant toConstant() {
        return new Constant("X_" + this.hashCode());
    }

    /**
     * Returns the classification sentence for this example; e.g.
     * "WillWait(X_83478)" or "NOT WillWait(X_83478)".
     *
     * @param classificationPredicate
     * @return
     */
    public Sentence toClassification() {
        List<Term> terms = new ArrayList<Term>();
        terms.add(this.toConstant());
        Sentence classification = new Predicate(this.classificationPredicate, terms);
        if (this.getOutput() == null) {
            classification = new NotSentence(classification);
        }
        return classification;
    }

    /**
     * Returns the Example as a first order logic sentence; e.g.
     * "Patrons(X_1234, Full) AND NOT Hungry(X_1234)".
     *
     * @return
     */
    public Sentence toDescription() {
        // collect predicates for this example
        List<Sentence> predicates = new ArrayList<Sentence>();
        for (Attribute a : this.getAttributes()) {
            // create predicate terms
            Sentence predicate = null;
            List<Term> terms = new ArrayList<Term>();
            terms.add(this.toConstant());
            // single value predicate
            if (a.getValue() instanceof Boolean) {
                boolean value = (Boolean) a.getValue();
                if (value) {
                    // do nothing; e.g. Predicate(X_1385)
                    predicate = new Predicate(a.getName(), terms);
                } else {
                    // create not-sentence
                    predicate = new Predicate(a.getName(), terms);
                    predicate = new NotSentence(predicate);
                }
            } // multi-value predicate
            else {
                Constant value = new Constant(a.getValue().toString());
                terms.add(value);
                predicate = new Predicate(a.getName(), terms);
            }
            // add predicate
            predicates.add(predicate);
        }
        // compile as ConnectedSentence
        Sentence output = null;
        if (predicates.size() > 1) {
            output = new ConnectedSentence(Connectors.AND, predicates.get(0), predicates.get(1));
            for (int i = 2; i < predicates.size(); i++) {
                output = new ConnectedSentence(Connectors.AND, output, predicates.get(i));
            }
        } else {
            output = predicates.get(0);
        }
        // return
        return output;
    }
}
