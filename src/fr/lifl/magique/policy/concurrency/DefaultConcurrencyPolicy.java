
/**
 * DefaultConcurrencyPolicy.java
 *
 *
 * Created: Thu Sep 30 11:58:09 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */

package fr.lifl.magique.policy.concurrency;
import fr.lifl.magique.policy.ConcurrencyPolicy;
import fr.lifl.magique.util.*;

/** this is the default concurrency policy, answer is considered as
 *   being received as soon as one answer has been received, the *
 *   selected answer is the first one */
public class DefaultConcurrencyPolicy implements ConcurrencyPolicy {
    
    public DefaultConcurrencyPolicy() {	
    }
    
    /** select the first answer of <em>theAnswer</em> (must be non
     *empty, this is assured from <tt>isAnswerReceived</tt>)
     *
     * @return THE slected answer
     */
    public Answer selectAnswer(AnswerVector theAnswers) {
	return (Answer) theAnswers.firstElement();
    }
    /** returns <em>true</em> if  if at least one answer has been received
     *
     * @return <em>true</em> if at least one answer has been received 
     */
    public boolean isAnswerReceived(AnswerVector theAnswers) {
	return !theAnswers.isEmpty();
    }
    
} // DefaultConcurrencyPolicy
