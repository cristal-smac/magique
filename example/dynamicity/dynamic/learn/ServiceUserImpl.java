
/**
 * ServiceUserImpl.java
 *
 *
 * @author JC Routier
 * @version
 */
package dynamic.learn;

import fr.lifl.magique.*;
import fr.lifl.magique.util.*;
import fr.lifl.magique.platform.*;

import java.util.*;

public class ServiceUserImpl extends AbstractMagiqueMain
{
    public void theRealMain(String[] args) 
    {
	if (args.length==2)
	    Agent.setVerboseLevel(new Integer(args[1]).intValue());

	AcquAgent a = (AcquAgent) createAgent("fr.lifl.magique.AcquAgent","ServiceUser");	

	// chose the decision criterion for learning skill : see DefaultLearningLog*
	a.perform("changeAnswerLogFactory", new AnswerLogFactoryWrapper(new DefaultLearningLogFactory()));

	a.addSkill(new ServiceUserSkill(a));
	a.connectToBoss("sup1@"+args[0]);

	
	// to get the name of "natural" answerer to request for service
	Request q = a.ask("service", new Object[]{a.getName(), new Date()});
	while (! a.isAnswerReceived(q)) {
	    try { Thread.sleep(20); } catch(Exception e) {}
	}
	Answer theAnswer = a.returnAnswer(q);
	String answerer = theAnswer.getAnswerer();

	// make some requests for service
	for(int i=0;i< 18;i++) {
	    System.out.print("Service required ...");	    
	    a.askNow("service", new Object[]{a.getName(), new Date()});
	}
	

	a.askNow("disconnectFromAll");
	System.out.println("disconnected from all other agents");	
	for(int i=0;i< 10;i++) {
	    System.out.print("Service required ...");	    
	    a.askNow("service", new Object[]{a.getName(), new Date()});
	}


    }

} // ServiceUserImpl
