
/**
 * ServiceUserImpl.java
 *
 *
 * @author JC Routier
 * @version
 */
package dynamic.acqua;

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

	a.perform("changeAnswerLogFactory", new AnswerLogFactoryWrapper(new DefaultAnswerLogFactory()));

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
	
	// kill the "natural answerer"
	a.perform(answerer,"disconnectAndDie");
	System.out.println(">>>> death of "+answerer);

	// pause 
	try {
	    Thread.sleep(1000);
	}
	catch(Exception e) {}

	// other service provide should be used now
	for(int i=0;i< 18;i++) {
	    System.out.print("Service required ...");	    
	    a.askNow("service", new Object[]{a.getName(), new Date()});
	}
	

    }

} // ServiceUserImpl
