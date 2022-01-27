/**
 * DisconnectionSkill.java
 *
 *
 * Created: Fri Oct 29 16:18:13 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.system;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.util.*;

import java.util.*;

/** Skills with ability for disconnection between agent
 *
 * @see fr.lifl.magique.skill.system.ConnectionSkill
*/
public class DisconnectionSkill extends DefaultSkill {

    public DisconnectionSkill(AtomicAgent myAgent) {
	super(myAgent);
    }

    /** disconnects me from another agent : disconnection is safe in the
     * sense that no request are lost during disconnection : every
     * request who leaved me or my boss arrived to its its
     * destination. BUT nothing is said about what become the answers
     * to questions I have asked or have been asked to
     * answer... 
     *
     * @param theOther name of the agent i disconnect from
     * @see fr.lifl.magique.Agent#connectTo
     *  */
    public Boolean askForDisconnectionFrom(String theOther) {	
	Agent.verbose(3,getName()+" askForDisconnectionFrom "+theOther); 
	theOther = getMyAgent().getAgenda().getFullName(theOther);
	askNow(theOther,"acknowledgeDisconnection",getName());	
	perform(theOther,"removeFromAgenda",getName() , Boolean.TRUE);
	// pause to be sure "theOther" has removed me from its agenda
	// asKnow can not be used since, I disappear from agend and
	// then answer can not comme back... an I would become blocked
	try {
	    Thread.sleep(100); 
	}
	catch (Exception e) {
	    e.printStackTrace();
	}

	disconnect(theOther);
	return Boolean.TRUE;
    }

    
    /** disconect from everyone */
    public void disconnectFromAll() {
	// disconnection from other acquaintances
	for(Enumeration known = getAgenda().keys();known.hasMoreElements();) {
	    String name = (String) known.nextElement();
	    askNow("askForDisconnectionFrom",new Object[]{name});
	}
    }

    /** remove <em>theOther</em> from my agenda (due to a disconnection).
     * 
     * @param theOther name of the agent to remove from my agenda
     * @param flag not used here
     */
    public void removeFromAgenda(String theOther, 
				 Boolean flag) {
	Agent.verbose(3,getName()+" remove "+ theOther+" from agenda");	
	getMyAgent().getAgenda().removeAgent(theOther);
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
	Agent.verbose(3,getName()+ " acknowledge disconnection "+ theOther); 
	return Boolean.TRUE;
    }
 
    /** finishes the disconnection : theOther isremoved from my agenda
     * and cancalcomm is closed
     *
     * @param theOther name of the agent i disconnect from */
    private void disconnect(String theOther) {
	removeFromAgenda(theOther, Boolean.FALSE);
	Agent.verbose(3,getName()+ " disconnection from "+ theOther+" done"); 
    }

    

} // DisconnectionSkill.java
 
