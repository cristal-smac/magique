/**
 * GroupManagerSkill.java
 * <p>
 * <p>
 * Created: Wed Dec 08 13:05:48 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.group;

import fr.lifl.magique.Agent;
import fr.lifl.magique.SkillAlreadyAcquiredException;
import fr.lifl.magique.skill.MagiqueDefaultSkill;
import fr.lifl.magique.util.Team;

import java.util.Enumeration;

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
        } catch (SkillAlreadyAcquiredException e) {
        }
        connectToBoss(getName());
        return agent.getName();
    }

    /** returns the enumeration of all groups managed by this agent
     */
    public Enumeration getAllGroups() {
        return ((Team) askNow("getMyTeam")).getMembers();
    }
} // GroupManagerSkill
