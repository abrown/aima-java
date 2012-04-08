package aima.core.learning.knowledge;
import aima.core.logic.fol.Connectors;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Hypothesis; per page 769, AIMAv3, should be in the form:
 * <pre>
 * FORALL v (Classification(v) &lt;=&gt; 
 *  (Description1(v) AND Description2(v, Constant1))
 *  OR (Description1(v) AND Description3(v))
 *  OR ...
 * )
 * </pre>
 * @author Ciaran O'Reilly
 * @author
 */
public class Hypothesis extends QuantifiedSentence{

    /**
     * Constructor
     * @param classificationPredicate 
     */
    public Hypothesis(String classificationPredicate) {
        super("FORALL", buildVariables(), buildSentence(classificationPredicate));
    }
    
    /**
     * Return "r"; see page 769, AIMAv3
     * @return 
     */
    private static List<Variable> buildVariables(){
        ArrayList<Variable> variables = new ArrayList<Variable>();
        variables.add(new Variable("r"));
        return variables;
    }
    
    /**
     * Return starting sentence; e.g. "FORALL r WillWait(r) <=> true"
     * @param predicate
     * @return 
     */
    private static Sentence buildSentence(String predicate){
        // build predicate
        ArrayList<Term> terms = new ArrayList<Term>();
        terms.add(new Variable("r"));
        Predicate p = new Predicate(predicate, terms);
        // build sentence
        ConnectedSentence sentence = new ConnectedSentence(Connectors.BICOND, p, null); // @todo: this is probably incorrect
        // return
        return sentence;
    }
}
