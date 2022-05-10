/**
 * Answer.java
 * <p>
 * <p>
 * Created: Mon Oct 26 08:43:37 1998
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.util;

/** Multiple answers to a question are stored in an
 * AnswerVector. <tt>Answer</tt> objects represent elements of this
 * vector.
 * @see AnswerVector */
public class Answer {

    /** the value of the answer*/
    private final Object value;
    /** the name of who gives the answer*/
    private final String answerer;
    /** the length of the path used to reach the answerer */
    private final int pathLength;

    public Answer(Object value, String answerer, int pathLength) {
        this.value = value;
        this.answerer = answerer;
        this.pathLength = pathLength;
    }

    /** returns the value of the answer
     * @return the value of the answer */
    public Object getValue() {
        return value;
    }

    /** returns the name of the answerer
     * @return the name of the answerer */
    public String getAnswerer() {
        return answerer;
    }

    /** returns the path used to reach  the answerer
     * @return the path used to reach  the answerer */
    public int getPathLength() {
        return pathLength;
    }

} // Answer
