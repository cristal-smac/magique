
/**
 * AnswerLogImpl.java
 *
 *
 * Created: Mon Oct 08 13:47:51 2001
 *
 * @author <a href="mailto:routier@lifl.fr">Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;



public class AnswerLogImpl extends AnswerLog {

    private static final int THRESHHOLD = 8;

    public AnswerLogImpl (){
	super();
    }


    public boolean isAcquaintanceCreationPossible(String answerer) {	
	return ((Integer) logInfo.get(answerer)).intValue() > THRESHHOLD;
    }

    public boolean isLearningAccurate(String answerer) {
	return false;
    }
}// AnswerLogImpl
