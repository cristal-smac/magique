/**
 * AnswerVector.java
 * <p>
 * <p>
 * Created: Thu Sep 23 11:19:37 1999
 *
 * @author Jean-Christophe Routier
 * @version It is used to store multiple answers to a given question. It is
 * useful for asynchronous ans concurrent questions. Question that are
 * waiting for answer(s) are stored in a <tt>QuestionTable</tt>
 * @see Questiontable
 * @see Answer
 */

/** It is used to store multiple answers to a given question. It is
 * useful for asynchronous ans concurrent questions. Question that are
 * waiting for answer(s) are stored in a <tt>QuestionTable</tt>
 *
 * @see Questiontable
 * @see Answer*/

package fr.lifl.magique.util;

import java.util.Vector;

public class AnswerVector extends Vector {

    private boolean concurrent = false;

    /** the answer chosen as THE answer among all th received answers
     * to the question this vector is associated to */
    private Answer theAnswer;

    public AnswerVector(boolean concurrent) {
        super();
        this.concurrent = concurrent;
    }

    /** @return <em>true</em> if it is a concurrent request */
    public boolean isConcurrent() {
        return concurrent;
    }

    /** returns the answer chosen as THE answer among all th received
     * answers to the question this vector is associated to
     *
     * @return the answer chosen as THE answer among all th received
     * answers to the question this vector is associated to */
    public Answer getTheAnswer() {
        return theAnswer;
    }

    /** ser <tt>theAnswer</tt>
     */
    public void setTheAnswer(Answer answer) {
        theAnswer = answer;
    }

    /** adds an answer to the vector 
     * @param answer the answer to be added
     */
    public void addAnswer(Answer answer) {
        addElement(answer);
    }

    /** returns the number of answers received
     *
     * @return the number of answers received
     */
    public int getNumberOfAnswers() {
        return size();
    }

    /** gets the value of the answer (which may have been chosen among many)
     *
     * @return the value of the answer (which may have been chosen among many)
     */
    public Object getAnswerValue() {
        return theAnswer.getValue();
    }

} // AnswerVector
