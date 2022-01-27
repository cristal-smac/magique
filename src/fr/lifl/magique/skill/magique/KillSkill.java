
/**
 * KillSkill.java
 *
 *
 * Created: Thu Dec 16 14:39:55 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.magique;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

import java.util.*;

/** If an agent must be stopped, this skill provides the abilities to do this safely.
 * A ful hierarchy can be killed if its boss invokes <tt>killHierarchy</tt>.
 */
public class KillSkill extends MagiqueDefaultSkill {
    
    public KillSkill(Agent myAgent) {
	super(myAgent);
	try {
	    myAgent.addSkill(new DisconnectionSkill(myAgent));
	}
	catch(SkillAlreadyAcquiredException e) {}
    }

    /** agent dies after safe disconnections (disconnectionSkill() is required)
     * and server 
     */ 
    public void disconnectAndDie() {
	getMyAgent().verbose(3,getName()+" disconnectAndDie");
	askNow("disconnectFromAll");
	askNow("askForDisconnectionFrom",new Object[]{fr.lifl.magique.platform.Platform.PLATFORMMAGIQUEAGENTNAME});
	getMyAgent().getListener().halt();
	getMyAgent().getPlatform().removeAgent(getName());
	Agent.verbose(2,getName()+" has died");	
    }
    

    /** agent dies 
     */ 
    public void die() {
	getMyAgent().verbose(3,getName()+" die");
	getMyAgent().getListener().halt();
	getMyAgent().getPlatform().removeAgent(getName());
	Agent.verbose(2,getName()+" has died");	
    }

    /** kill all the agents in the hierarchy below "myAgent"
     */
    public void killHierarchy() {
	getMyAgent().verbose(3,getName()+" killHierarchy");
	if (isBoss()) {
	    for(Enumeration e = getMyTeam().getMembers();
		e.hasMoreElements();)
		perform((String)e.nextElement(),"killHierarchy");	    
	}
	while (isBoss()) {
	    try {
		Thread.sleep(500);
	    }	
	    catch(Throwable t) {}
	}
	disconnectAndDie();
    }
    
} // KillSkill
