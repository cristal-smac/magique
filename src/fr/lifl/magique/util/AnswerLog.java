
/**
 * AnswerLog.java
 *
 *
 * Created: Mon Oct 08 11:18:59 2001
 *
 * @author <a href="mailto: "Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;
import java.util.*;

public abstract class AnswerLog {
    protected Map logInfo;

    public AnswerLog (){
	logInfo = new HashMap();
    }


    /** an answer from answerer has been received
     */
    public void update(String answerer) {
	if (logInfo.containsKey(answerer)) {
	    logInfo.put(answerer,new Integer(((Integer) logInfo.get(answerer)).intValue()+1));
	}
	else {
	    logInfo.put(answerer,new Integer(1));
	}
    }
    /** remove log concerning <em>answerer</em> */
    public void removeAnswerer(String answerer) {
	//	if (logInfo.containsKey(answerer)) System.out.println("                            remove "+answerer);		
	logInfo.remove(answerer);
    }


    /** check wether the criterion for acquaintance creation, according
     * to log info for answerer, is reached */
    public abstract boolean isAcquaintanceCreationPossible(String answerer);


    /** check whether the criterion for skill acquisition from answerer source is reached, done accroding to log info */
    public abstract boolean isLearningAccurate(String answerer);


}// AnswerLog
