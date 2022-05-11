/**
 * ConnectionToBossSkill.java
 * <p>
 * <p>
 * Created: Tue Jan 25 13:40:25 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.magique;

import fr.lifl.magique.Agent;
import fr.lifl.magique.Request;
import fr.lifl.magique.skill.MagiqueDefaultSkill;
import fr.lifl.magique.skill.system.ConnectionRefusedException;
import fr.lifl.magique.util.Name;
import fr.lifl.magique.util.TeamInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

/** Skill used to connect the gifted agent to another one (who should
 * know the "same" skill - in fact a skil with the same public
 * signature) as its boss.
 *
 * Agenda is updated. And team of the boss too.
 *
 * The <tt>connectionToBossAuthorized</tt> method tells if the connection is
 * accepted by the boss (default is always TRUE) 
 *
 * @see fr.lifl.magique.skill.system.DisconnectionSkill*/
public class ConnectionToBossSkill extends MagiqueDefaultSkill {

    public ConnectionToBossSkill(Agent myAgent) {
        super(myAgent);
    }

    /** connect me to the agent whose name is <em>agent</em> as my boss, this
     * name must be of the form shortname@hostname:port but if the
     * agent belongs to the same platform as me, shortName is
     *
     * @param bossName the agent i connect to
     */
    public void connectionToBoss(String bossName) throws ConnectionRefusedException {
        try {
            // construct the right address for my boss
            String bossShortName = Name.getShortName(bossName);
            String bossAddress;
            String bossPort;
            Boolean success;
            if (bossShortName.equals(bossName)) {
                bossAddress = InetAddress.getLocalHost().getHostAddress();
                bossPort = "" + getMyAgent().getPlatform().getPort();
            } else {
                bossAddress = InetAddress.getByName(Name.getHostName(bossName)).getHostAddress();
                bossPort = Name.getPort(bossName);
            }
            bossName = bossShortName + "@" + bossAddress + ":" + bossPort;
            Agent.verbose(0, ">>> bossName : " + bossName);
            //
            getMyAgent().addAgenda(bossName);

            Boolean acknowledgement = (Boolean) askNow(bossName, "connectionToBossRequired", getName());

            if (acknowledgement.booleanValue()) {
                askNow("setMyBoss", new Object[]{bossName});
                Agent.verbose(2, getName() + " my boss " + getMyBoss());
                getMyAgent().sendUnsentRequestToBoss();
            } else {
                getAgenda().removeAgent(bossName);
                throw new ConnectionRefusedException(bossName);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /** an agent wants me to be his boss, i can accept or not
     *
     * @param theOther the agent who wants to connect to me
     */
    public Boolean connectionToBossRequired(String theOther) {
        Boolean accept = connectionToBossAuthorized(theOther);
        if (accept.booleanValue()) {
            getMyAgent().addAgenda(theOther);
        }
        Vector whoHeKnows = (Vector) askNow(theOther,"knownNames");
        if (!getAgenda().containsKey(theOther)) {
            getMyAgent().addAgenda(theOther);
        }
        Vector hisMethodsNames = (Vector) askNow(theOther,"knownMethods");
        askNow("addToMyTeam",new Object[]{theOther,new TeamInfo(whoHeKnows,hisMethodsNames)});
        return accept;
    }

    /** tells if <em>agentName</em> is authorized to connect to me
     *
     * @return here is default : TRUE
     */
    private Boolean connectionToBossAuthorized(String agentName) {
        return Boolean.TRUE;
    }

    /** returns the length of the path to reach the given agent when
     * using the hierarchy
     * @param agentName the full name of the agent to reach
     * @return the length of the path to reach given agent when using the hierarchy */
    public Integer pathLength(String agentName) {
        Request r = createQuestion("getName");
        ask(agentName, r);
        return Integer.valueOf(returnAnswer(r).getPathLength());
    }

} // ConnectionToBossSkill
