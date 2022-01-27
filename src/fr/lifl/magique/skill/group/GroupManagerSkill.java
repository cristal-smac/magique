
/**
 * GroupManagerSkill.java
 *
 *
 * Created: Wed Dec 08 13:05:48 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.group;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.util.*;
//import fr.lifl.magique.skill.system.*;
import java.util.*;

public class GroupManagerSkill extends MagiqueDefaultSkill {
    
    public GroupManagerSkill(Agent agent) {
	super(agent);
    }
    
    /** create a new groupe managed by this manager
     *
     * @param groupName name of created group
     *
     *@return name of the agent containing
     */
    public String createGroup(String groupName) {
	Agent agent = new Agent(groupName);
	try {
	    agent.addSkill(new GroupSkill(agent));
	    //	    agent.addSkill(new DisconnectionSkill(agent));
	}
	catch(SkillAlreadyAcquiredException e) {}
	connectToBoss(getName());
	return agent.getName();
    }
    
    /** returns the enumeration of all groups managed by this agent
     */
    public Enumeration getAllGroups(){
	return ((Team) askNow("getMyTeam")).getMembers();
    }

//      public void addToGroup(String GroupName, String agentName) {
//  	perform(GroupName,"addToGroup",agentName);
//      }

//      public void removeFromGroup(String groupName, String agentName) {
//  	perform(groupName,"removeFromGroup",agentName);
//      }


} // GroupManagerSkill
