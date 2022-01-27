
/**
 * PingSkill.java
 *
 *
 * @author JC Routier
 * @version
 */
package dynamic.learn;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

// service PingSkill
public class PingSkill extends MagiqueDefaultSkill
{
    public PingSkill(Agent a){super(a);}

    /** @param agentName name of agent that play with me */
    public void ping(String agentName) 
    {
	//	perform("display",new Object[]{getName()+" plays ping with "+agentName});
	try{
	    Thread.sleep(10);
	}
	catch(Exception e) {}
	askNow(agentName,"pong",getName());
    }
} // PingSkill
