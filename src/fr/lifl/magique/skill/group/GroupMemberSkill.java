
/**
 * GroupMemberSkill.java
 *
 *
 * Created: Wed Dec 08 13:25:35 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.group;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.skill.system.*;
    
/** skill for an agent who wants to be able to belong to a skill
 */
public class GroupMemberSkill extends DefaultSkill {
    
    public GroupMemberSkill(Agent agent) {
	super(agent);
	try {
	    agent.addSkill(new DisconnectionSkill(agent));
	}
	catch(SkillAlreadyAcquiredException e) {}
    }
   
    /** join a group
     *
     * @param groupName name of the group i join to
     *
     * @return <tt>Boolean.TRUE</tt> iff join has suceeded
     */
    public Boolean joinGroup(String groupName) {
	Boolean joinAccepted = (Boolean) askNow(groupName,"join",getName());
	if (joinAccepted.booleanValue()) {
	    perform(groupName,"addToGroup",getName());
	}
	return joinAccepted;
    }
    
    /* send a request to other members of a group (i belong to). A
     * priori, here request should not require an answer
     *
     * @param groupName name of the group
     * @param request the request i send to others */
    public void sendToOtherGroupMembers(String groupName,Request request) {
	perform(groupName,"broadcastToOthers",getName(),request);
    }

    /** leaves the given group
     *
     * @param groupName the name of the groupe i am leaving */
    public void leaveGroup(String groupName) {
	perform(groupName,"removeFromGroup",getName());
	perform("askForDisconnectionFromMyBoss");
    }

} // GroupMemberSkill
