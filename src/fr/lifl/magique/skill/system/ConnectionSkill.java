
/**
 * ConnectionSkill.java
 *
 *
 * Created: Tue Jan 25 13:40:25 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.system;

import fr.lifl.magique.*;
import fr.lifl.magique.util.*;
import fr.lifl.magique.skill.*;
import java.net.*;
import java.util.*;

/** Skill used to connect the gifted agent to another one (who should
 * know the "same" skill - in fact a skil with the same public
 * signature).
 *
 * Agenda is updated.
 *
 * The <tt>connectionAuthorized</tt> method tells if the connection is
 * accepted (default is always TRUE) */
public class ConnectionSkill extends DefaultSkill {
    
    public ConnectionSkill(AtomicAgent myAgent) {
	super(myAgent);
    }
    
    /** connect me to the agent whose name is <em>agent</em>, this
     * name must be of the form shortname@hostname:port but if the
     * agent belongs to the same platform as me, shortName is
     * sufficient.
     *
     * @param agent the agent i connect to */
    public void connectionTo(String agent) throws ConnectionRefusedException {
	try {
	    // construct the right address for my acquaintance
	    String agentShortName = Name.getShortName(agent);	
	    String agentAddress;
	    String agentPort;
	    if (agentShortName.equals(agent)) {
		agentAddress = InetAddress.getLocalHost().getHostAddress();
		agentPort = ""+getMyAgent().getPlatform().getPort();
	    }
	    else { 
		agentAddress = InetAddress.getByName(Name.getHostName(agent)).getHostAddress();		
		agentPort = Name.getPort(agent);
	    }
	    agent = agentShortName+"@"+agentAddress+":"+agentPort;
	    
	    getMyAgent().addAgenda(agent);
 
	    Boolean acknowledgement = (Boolean) askNow(agent,"connectionRequired",getName());

	    if (acknowledgement.booleanValue()) {
	    }
	    else {
		getMyAgent().getAgenda().removeAgent(agent);
		throw new ConnectionRefusedException(agent);
	    }
	}
	catch (UnknownHostException e) {
	    e.printStackTrace();
	}
    }

    /** an agent wants to connect to me, i can accept or not
     * 
     * @param theOther the agent who wants to connect to me
     */
    public Boolean connectionRequired(String theOther) {
	Boolean accept = connectionAuthorized(theOther);
	if (accept.booleanValue()) {
	    getMyAgent().addAgenda(theOther);
	}
	return accept;
    }

    /** tells if <em>agentName</em> is authorized to connect to me
     * 
     * @return here is default : TRUE
     */
    private Boolean connectionAuthorized(String agentName) {
	return Boolean.TRUE;
    }

} // ConnectionSkill
