
/**
 * PingSkill.java
 *
 *
 * @author JC Routier
 * @version
 */
package dynamic.pool;

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
	System.out.print("(");
	long debut = System.currentTimeMillis();
	while (System.currentTimeMillis()-debut < 4000) {
	    // nop
	}
	
	System.out.print(")");
	askNow(agentName,"pong",getName());
    }
} // PingSkill
