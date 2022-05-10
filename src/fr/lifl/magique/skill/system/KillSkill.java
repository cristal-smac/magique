/**
 * KillSkill.java
 * <p>
 * <p>
 * Created: Thu Dec 16 14:39:55 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.system;

import fr.lifl.magique.AbstractAgent;
import fr.lifl.magique.Agent;
import fr.lifl.magique.AtomicAgent;
import fr.lifl.magique.SkillAlreadyAcquiredException;
import fr.lifl.magique.skill.DefaultSkill;

/** If an agent must be stopped, this skill provides the abilities to do this safely.
 * A ful hierarchy can be killed if its boss invokes <tt>killHierarchy</tt>.
 */
public class KillSkill extends DefaultSkill {

    public KillSkill(AtomicAgent myAgent) {
        super(myAgent);
        try {
            myAgent.addSkill(new DisconnectionSkill(myAgent));
        } catch (SkillAlreadyAcquiredException e) {
        }
    }

    /** agent dies after safe disconnections (disconnectionSkill() is required)
     * and server 
     */
    public void disconnectAndDie() {
        AbstractAgent.verbose(3, getName() + " disconnectAndDie");
        askNow("disconnectFromAll");
        die();
    }

    /** agent dies 
     */
    public void die() {
        AbstractAgent.verbose(3, getName() + " die");
        getMyAgent().getListener().halt();
        getMyAgent().getPlatform().removeAgent(getName());
        Agent.verbose(2, getName() + " has died");
    }


} // KillSkill
