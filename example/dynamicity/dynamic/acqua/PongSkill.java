
/**
 * PongSkill.java
 *
 *
 * @author JC Routier
 * @version
 */
package dynamic.acqua;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

// service PongSkill
public class PongSkill extends MagiqueDefaultSkill
{
    public PongSkill(Agent a){super(a);}

    /** @param agentName name of agent that play with me */
    public void pong(String agentName) 
    {
	//	perform("display",new Object[]{getName()+" plays pong with "+agentName});
	try{
	    Thread.sleep(10);
	}
	catch(Exception e) {}
	askNow(agentName,"ping",getName());
    }
} // PongSkill
