/**
 * ConcurrencyPolicy.java
 * <p>
 * <p>
 * Created: Thu Sep 30 11:12:47 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.policy;

import fr.lifl.magique.util.Answer;
import fr.lifl.magique.util.AnswerVector;

/** interface for policy of concurrency, you must defined when an
 answer is considered to have been received and which answer must
 be selected among all those received */
public interface ConcurrencyPolicy {

    /** select the answer to be considered as THE answer among all the
     * received answers
     *
     * @return THE slected answer
     * @see fr.lifl.magique.util.Answer
     */
    Answer selectAnswer(AnswerVector theAnswers);

    /** returns <em>true</em> if the asnwer can be considered as being
     * received according to the content of <em>theAnswers</em>
     *
     * @return <em>true</em> if at least one answer has been received 
     *
     * @see fr.lifl.magique.util.Answer
     */
    boolean isAnswerReceived(AnswerVector theAnswers);

} // ConcurrencyPolicy
