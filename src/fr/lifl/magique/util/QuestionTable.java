/**
 * QuestionTable.java
 * <p>
 * <p>
 * Created: Fri Oct 23 16:15:53 1998
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.util;

import fr.lifl.magique.Agent;
import fr.lifl.magique.Request;
import fr.lifl.magique.policy.ConcurrencyPolicy;
import fr.lifl.magique.policy.concurrency.DefaultConcurrencyPolicy;

import java.util.Hashtable;

/** such a table associates a question name (<em>String</em>) with an
 * <em>AnswerVector</em> object. So an <em>Agent</em> can know when the answer
 * to a question has arrived and what it is, and multiple answer can be managed.
 * @see AnswerVector
 * @author JC Routier routier@lifl.fr */
public class QuestionTable extends Hashtable {

    /** the policy for concurrency of my agent */
    private ConcurrencyPolicy theConcurrencyPolicy;

    /** */
    public QuestionTable() {
        super();
        this.theConcurrencyPolicy = new DefaultConcurrencyPolicy();
    }

    public QuestionTable(ConcurrencyPolicy theConcurrencyPolicy) {
        super();
        this.theConcurrencyPolicy = theConcurrencyPolicy;
    }

    /** set the concurrency policy */
    public void setConcurrencyPolicy(ConcurrencyPolicy theConcurrencyPolicy) {
        Agent.verbose(3, "questionTable, concurrency policy has changed");
        this.theConcurrencyPolicy = theConcurrencyPolicy;
    }

    /** add a question to the table
     * @param question the question to add */
    public void newQuestion(Request question) {
        put(question.getName(), new AnswerVector(question.isConcurrent()));
    }

    /** give the answer vector associated to the question
     * @param question the question you want the answer to
     * @return the answer vector associated to the question
     */
    public AnswerVector getAnswerVector(String question) {
        return ((AnswerVector) get(question));
    }

    /** give the answer to the question (this may have been selected
     * among multiple answers)
     *
     * @param question the question you want the answer to
     * @return the answer vector associated to the question */
    public Answer getTheAnswer(String question) {
        return getAnswerVector(question).getTheAnswer();
    }

    /** tells if the answer to the question has been received
     * @param question the question you want to test
     * @return <em>true</em> if the anwsered has been received
     */
    public boolean isAnswerReceived(String question) {
        AnswerVector theAnswers = getAnswerVector(question);
        if (theAnswers.isConcurrent())
            return theConcurrencyPolicy.isAnswerReceived(theAnswers);
        else
            return !theAnswers.isEmpty();
    }

    /** give the answer to the question (this may have been selected
     * among multiple answers if question was aconcurrent one)
     *
     * @param question the question you want the answer to
     * @return the answer vector associated to the question */
    public synchronized Answer selectTheAnswer(String question) {

        while (!isAnswerReceived(question)) {
            try {
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AnswerVector theAnswers = getAnswerVector(question);
        Answer answer;
        if (theAnswers.isConcurrent()) {
            answer = theConcurrencyPolicy.selectAnswer(theAnswers);
        } else
            answer = (Answer) theAnswers.firstElement();
        theAnswers.setTheAnswer(answer);
        return answer;
    }

    /** an answer to <em>question</em> has been received, this method
     * adds it to the vector of answer associated to the question
     * @param question the question that has been answered
     * @param answer the value of the answer to <em>question</em>
     * @param answerer the full name of the agent who answered to <em>question</em>
     * @param pathLength the length of the path to reach the answerer
     * @return no return value
     */
    public synchronized void setAnAnswer(String question, Object answer, String answerer, int pathLength) {
        if (containsKey(question)) {
            getAnswerVector(question).addAnswer(new Answer(answer, answerer, pathLength));
        }

        notifyAll();
    }

    /** remove a question entry from the table
     * @param question the question to remove
     * @return no return value
     */
    public synchronized void removeQuestion(String question) {
        remove(question);
    }
} // QuestionTable
