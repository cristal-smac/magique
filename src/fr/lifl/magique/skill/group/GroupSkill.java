/**
 * GroupSkill.java
 * <p>
 * <p>
 * Created: Wed Dec 08 13:06:05 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.group;

import fr.lifl.magique.Agent;
import fr.lifl.magique.Request;
import fr.lifl.magique.skill.MagiqueDefaultSkill;
import fr.lifl.magique.util.Name;

import java.util.Enumeration;
import java.util.Vector;

/** skill for a "group agent" */
public class GroupSkill extends MagiqueDefaultSkill {

    private final Vector myMembers = new Vector();

    public GroupSkill(Agent agent) {
        super(agent);
    }

    /** add an agent to this group
     *
     * @param agentName the agent to be added
     */
    private synchronized void addToGroup(String agentName) {
        myMembers.addElement(agentName);
    }

    /** remove an agent from this group
     *
     * @param agentName the agent to be removed
     */
    public void removeFromGroup(String agentName) {
        myMembers.removeElement(agentName);
    }

    /** returns the enumeration of the members of this group
     *
     *@return the enumeration of the members of this group
     */
    public Enumeration getGroupMembers() {
        return myMembers.elements();
    }

    /** broadcast a request (with a prioi no answer) to all the
     * members of this group
     * @param  request the request to be broadcasted
     */
    public void broadcastToGroup(Request request) {
        for (Enumeration enu = myMembers.elements();
             enu.hasMoreElements(); ) {
            perform((String) enu.nextElement(), (Request) request.clone());
        }
    }

    /** broadcast a request (with a prioi no answer) to all the
     * members of this group but <tt>sender</tt>
     *
     * @param  the sender who have sent the request
     * @param  request the request to be broadcasted
     */
    public void broadcastToOthers(String sender, Request request) {
        for (Enumeration enu = myMembers.elements();
             enu.hasMoreElements(); ) {
            String memberName = (String) enu.nextElement();
            if (!memberName.equals(sender)) {
                perform(memberName, (Request) request.clone());
            }
        }
    }

    /** accept a new agent as a new member to ths group
     *
     * @param agentName the new agent
     */
    public Boolean join(String agentName) {
        String groupHostName = Name.getHostName(getName());
        String myAgentHostName = Name.getHostName(agentName);
        if (groupHostName.equals(myAgentHostName)) {
            askNow(agentName, "connectTo", getMyAgent());
        } else {
            askNow(agentName, "connectTo", getName());
        }
        addToGroup(agentName);
        return Boolean.TRUE;
    }


} // GroupSkill
