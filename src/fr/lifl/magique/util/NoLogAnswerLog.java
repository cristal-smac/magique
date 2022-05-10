/**
 * NoLogAnswerLog.java
 * <p>
 * <p>
 * Created: Mon Oct 08 13:47:51 2001
 *
 * @author <a href="mailto:routier@lifl.fr">Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;

/** case you want nothing to be done
 */

public class NoLogAnswerLog extends AnswerLog {

    public NoLogAnswerLog() {
        super();
    }

    public boolean isAcquaintanceCreationPossible(String answerer) {
        return false;
    }

    public boolean isLearningAccurate(String answerer) {
        return false;
    }
}// NoLogAnswerLog
