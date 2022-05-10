/**
 * DisconnectionSkill.java
 * <p>
 * <p>
 * Created: Fri Oct 29 16:18:13 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.magique;

import fr.lifl.magique.Agent;
import fr.lifl.magique.skill.MagiqueDefaultSkill;
import fr.lifl.magique.util.Name;

import java.util.Enumeration;

/** Skills with ability for disconnection between agent (maybe boss)
 *
 * @see fr.lifl.magique.skill.system.ConnectionSkill
 * @see fr.lifl.magique.skill.system.ConnectionToBossSkill
 **/
public class DisconnectionSkill extends MagiqueDefaultSkill {

    public DisconnectionSkill(Agent myAgent) {
        super(myAgent);
    }

    /** disconnects me from my boss : disconnection is safe in the
     * sense that no request are lost during disconnection : every
     * request who leaved me or my boss arrived to its its
     * destination. BUT nothing is said about what become the answers
     * to questions I have asked or have been asked to
     * answer... Reconnection through <tt>connectToBoss</tt> method can
     * be performed (and then problem of answers is solved)
     *
     * @see fr.lifl.magique.Agent#connectToBoss
     *  */
    public Boolean askForDisconnectionFromMyBoss() {
        String theOther = getMyBoss();
        Agent.verbose(3, getName() + " askForDisconnectionFromMyBoss " + theOther);
        theOther = getAgenda().getFullName(theOther);
        askNow(theOther, "acknowledgeDisconnection", getName());

        perform(theOther, "removeAgentFromAcquaintances", getName());

        perform(theOther, "removeFromAgenda", getName(), Boolean.TRUE);
        // pause to be sure "theOther" has removed me from its agenda
        // asKnow can not be used since, I disappear from agend and
        // then answer can not comme back... an I would become blocked
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        askNow("setMyBoss", new Object[]{""});
        disconnect(theOther);
        return Boolean.TRUE;
    }


    /** disconnects me from another agent (not my boss) : disconnection is safe in the
     * sense that no request are lost during disconnection : every
     * request who leaved me or my boss arrived to its its
     * destination. BUT nothing is said about what become the answers
     * to questions I have asked or have been asked to
     * answer... Reconnection through <tt>connectToBoss</tt> method can
     * be performed (and then problem of answers is solved)
     *
     * @param theOther name of the agent i disconnect from
     * @see fr.lifl.magique.Agent#connectTo
     *  */
    public Boolean askForDisconnectionFrom(String theOther) {
        Agent.verbose(3, getName() + " askForDisconnectionFrom " + theOther);
        theOther = getAgenda().getFullName(theOther);
        askNow(theOther, "acknowledgeDisconnection", getName());

        perform(theOther, "removeAgentFromAcquaintances", getName());

        perform(theOther, "removeFromAgenda", getName(), Boolean.FALSE);
        // pause to be sure "theOther" has removed me from its agenda
        // asKnow can not be used since, I disappear from agenda and
        // then answer can not comme back... an I would become blocked
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

        disconnect(theOther);
        return Boolean.TRUE;
    }


    /** disconect from everyone : acquaintances, team and boss */
    public void disconnectFromAll() {
        // disconnection from boss
        if (!isBigBoss()) {
            askNow("askForDisconnectionFromMyBoss");
        }
        // disconnection from other acquaintances
        for (Enumeration known = getAgenda().keys(); known.hasMoreElements(); ) {
            String name = (String) known.nextElement();
            if (!Name.getShortName(name).equals(fr.lifl.magique.platform.Platform.PLATFORMMAGIQUEAGENTNAME)) {
                askNow("askForDisconnectionFrom", new Object[]{name});
                if (getMyTeam() != null && getMyTeam().containsKey(name)) {
                    getMyTeam().remove(name);
                }
            }
        }
    }


    /** disconnect from all agents coming from given platform 
     * @param platformName platform to disconnect from
     */
    public void disconnectFromPlatform(String platformName) {
        // disconnection from other acquaintances
        for (Enumeration known = getAgenda().keys(); known.hasMoreElements(); ) {
            String name = (String) known.nextElement();
            if (Name.noShortName(name).equals(platformName)) {
                if (getMyBoss().equals(name)) {
                    askNow("setMyBoss", new Object[]{""});
                }
                Agent.verbose(3, getName() + " disconnect from " + name + " (platform lost)");
                askNow("removeFromAgenda", new Object[]{name, Boolean.FALSE});
                //  		    askNow("askForDisconnectionFrom",new Object[]{name});
                if (getMyTeam() != null && getMyTeam().containsKey(name)) {
                    getMyTeam().remove(name);
                }
            }
        }

    }


    /** remove <em>theOther</em> from my agenda (due to a disconnection).
     *
     * @param theOther name of the agent to remove from my agenda
     * @param fromBoss <em>true</em> iff disconnection from boss
     * intitiator of the disconnection 
     */
    public void removeFromAgenda(String theOther,
                                 Boolean fromBoss) {
        Agent.verbose(3, getName() + " remove " + theOther + " from agenda");
        getAgenda().removeAgent(theOther);
        // i must update my team info
    }

    /** performed by an agent when another agent (<em>theOther</em>)
     * asked for a disconnection from him
     *
     * @param theOther name of the agent who disconnect from me
     * @return <tt>Boolean.TRUE</tt> to tell i agree and am ready for
     * safe disconnection 
     */
    public Boolean acknowledgeDisconnection(String theOther) {
        Agent.verbose(3, getName() + " acknowledge disconnection " + theOther);
        if (isBoss() &&
                getMyTeam().containsKey(theOther)) {
            getMyTeam().remove(theOther);
        } else if (getMyBoss().equals(theOther)) {
            askNow("setMyBoss", new Object[]{""});
        }
        return Boolean.TRUE;
    }

    /** finishes the disconnection : theOther isremoved from my agenda
     * and cancalcomm is closed
     *
     * @param theOther name of the agent i disconnect from */
    private void disconnect(String theOther) {
        removeFromAgenda(theOther, Boolean.FALSE);

        perform("removeAgentFromAcquaintances", new Object[]{theOther});

        Agent.verbose(3, getName() + " disconnection from " + theOther + " done");
    }


} // DisconnectionSkill.java
 
