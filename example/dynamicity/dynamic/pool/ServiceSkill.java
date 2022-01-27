
/**
 * ServiceSkill.java
 *
 *
 * @author JC Routier
 * @version
 */
package dynamic.pool;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

import java.util.*;

public class ServiceSkill extends MagiqueDefaultSkill
{
    public ServiceSkill(Agent a){super(a);}

    public void service(String agentName, Date sendDate) 
    {
	try {
	    Thread.sleep(100); // time needed to perform service
	}
	catch(Exception e) {}
	perform("display",new Object[]{"service done"});
	askNow(agentName,"payService",sendDate);
	//askNow("payService",sendDate);
    }
} // ServiceSkill
