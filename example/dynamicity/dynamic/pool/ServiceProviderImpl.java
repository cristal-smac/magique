
/**
 * ServiceProviderImp.java
 *
 *
 * Created: Fri Nov 19 09:07:41 1999
 *
 * @author JC Routier
 * @version
 */
package dynamic.pool;

import fr.lifl.magique.*;
import fr.lifl.magique.util.*;
import fr.lifl.magique.platform.*;

public class ServiceProviderImpl extends AbstractMagiqueMain
{
    public void theRealMain(String[] args) 
    {
	if (args.length==2)
	    Agent.setVerboseLevel(new Integer(args[1]).intValue());

	AcquAgent a = (AcquAgent) createAgent("fr.lifl.magique.AcquAgent","Provider");
	a.addSkill(new ServiceSkill(a));
	a.addSkill(new PingSkill(a));


	// change here and replace the string here by the IP address of the platform you lanch
	// write platformIPHost:portHost
	a.setPoolHosts(new String[]{"172.16.12.155:4444","172.16.12.162:4444","172.16.12.160:4444"});


	a.connectToBoss("sup2@"+args[0]);


    }



} // ServiceProviderImpl
