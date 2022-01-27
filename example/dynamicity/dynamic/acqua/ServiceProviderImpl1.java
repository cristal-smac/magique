
/**
 * ServiceProviderImp.java
 *
 *
 * @author JC Routier
 * @version
 */
package dynamic.acqua;

import fr.lifl.magique.*;
import fr.lifl.magique.util.*;
import fr.lifl.magique.platform.*;

public class ServiceProviderImpl1 extends AbstractMagiqueMain
{
    public void theRealMain(String[] args) 
    {
	if (args.length==2)
	    Agent.setVerboseLevel(new Integer(args[1]).intValue());

	Agent a = (Agent) createAgent("fr.lifl.magique.AcquAgent","Provider1");
	a.perform("changeAnswerLogFactory", new AnswerLogFactoryWrapper(new DefaultAnswerLogFactory()));
	a.addSkill(new ServiceSkill(a));

	a.connectToBoss("sup2@"+args[0]);


    }



} // ServiceProviderImpl1
