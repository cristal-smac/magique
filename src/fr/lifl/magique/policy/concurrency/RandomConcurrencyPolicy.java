
/**
 * RandomConcurrencyPolicy.java
 *
 *
 * Created: Fri Oct 01 14:37:09 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.policy.concurrency;
import fr.lifl.magique.policy.ConcurrencyPolicy;
import fr.lifl.magique.util.*;
import java.lang.Math;

/** this is very a simple concurrency policy, answer is considered as
 *   being received as soon as <em>minNumberOfAnswers</em> answer has
 *   been received, the selected answer is randomly chosen 
 */

public class RandomConcurrencyPolicy implements ConcurrencyPolicy {
    
    int minNumberOfAnswers;

    public RandomConcurrencyPolicy(int minNumberOfAnswers) {
	this.minNumberOfAnswers = minNumberOfAnswers;
    }
    
    /** select randomly an answer among those arrived
     *
     * @return THE slected answer
     */
    public Answer selectAnswer(AnswerVector theAnswers) {
	int alea = ((int) (Math.random()*1024)) % minNumberOfAnswers;
	fr.lifl.magique.Agent.verbose(1," answer "+alea+" has been selected");
	return (Answer) theAnswers.elementAt(alea);
    }
    /** returns <em>true</em> if at least minNumberOfAnswers answers
     * have been received
     *
     * @return <em>true</em> if at least one answer has been received */
    public boolean isAnswerReceived(AnswerVector theAnswers) {
	return (theAnswers.size()>= minNumberOfAnswers);
    }


} // RandomConcurrencyPolicy
