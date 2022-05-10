/**
 * DefautlLearningLog.java
 * <p>
 * <p>
 * Created: Mon Oct 29 15:06:39 2001
 *
 * @author <a href="mailto: "Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;

public class DefaultLearningLog extends AnswerLog {

    private static final int THRESHHOLD = 8;

    public DefaultLearningLog() {
        super();
    }


    public boolean isAcquaintanceCreationPossible(String answerer) {
        return false;
    }

    public boolean isLearningAccurate(String answerer) {
        return ((Integer) logInfo.get(answerer)).intValue() > THRESHHOLD;
    }
}// DefaultLearningLog
