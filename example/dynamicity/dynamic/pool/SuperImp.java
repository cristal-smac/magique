
/**
 * SuperImp.java
 *
 *
 * @author JC Routier
 * @version
 */
package dynamic.pool;

import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

import java.util.*;


public class SuperImp  extends AbstractMagiqueMain {

    public void theRealMain(String[] args) 
    {

	    System.out.println("args 0 (opt) is number of service user agents (1 by default)");
	    System.out.println("args 1 (opt) is verbose level");

	if (args.length>=2)
	    Agent.setVerboseLevel(Integer.parseInt(args[1]));

	Agent sup = createAgent("super");
	Agent sup1 = createAgent("sup1");
	sup1.connectToBoss("super");
  	Agent sup2 = createAgent("sup2");
  	sup2.connectToBoss("super");


	// agents for noise : they represent other agents in the MAS
	// this agents make requests that are exchanged and use the
	// "sup*" agents ressources These agents go by pair. 
	//One connected to sup1, other to sup2 and they simply play
	//ping-pong
	int nbOfServiceUserAgents = 1;
	if (args.length>=1)
	    nbOfServiceUserAgents = Integer.parseInt(args[0]);

	System.out.println("\n =====\n"+nbOfServiceUserAgents +" service user agent(s) created"+"\n =====");
	


	Agent serviceUser = createAgent("serviceUser");
	serviceUser.addSkill(new ServiceUserSkill(serviceUser));
	serviceUser.connectToBoss("sup1");

	ArrayList list = new ArrayList();

	for(int i = 0; i<nbOfServiceUserAgents; i++) {
	    Agent a = createAgent("pinger"+i);
	    a.addSkill(new PongSkill(a));
	    a.connectToBoss("sup1");
	    list.add(a);
	}


	serviceUser.askNow("service", new Object[]{serviceUser.getName(), new Date()});

	Iterator it;
	Date avant;
	Date apres;

	System.out.println("avant");
	avant = new Date();
	HashMap sentQuestions;
	for (int i = 0; i < 6; i++) {
	    sentQuestions = new HashMap();
	    Date avd = new Date();
	    for(int j=0; j<3; j++) {
		it = list.iterator();
		while(it.hasNext()) {
		    Agent a = (Agent) it.next();
		    sentQuestions.put(a.ask("ping", new Object[]{a.getName()}),a);
		}
	    }
	    while (!sentQuestions.isEmpty()) {
		Iterator itsq = sentQuestions.keySet().iterator();
		while (itsq.hasNext()) {
		    Request r = (Request) itsq.next();
		    Agent a = (Agent) sentQuestions.get(r);
		    if (a.isAnswerReceived(r)) {
			itsq.remove();
		    }
		}
	    }
	    Date apd = new Date();
	    System.out.println("   >>>>>   "+(apd.getTime()-avd.getTime()));	    
	    serviceUser.perform("service", new Object[]{serviceUser.getName(), new Date()});
	}


	apres = new Date();
	System.out.println("\napres    ==> "+(apres.getTime()-avant.getTime()));
	       
	sup.askNow("createNewPool",new Object[]{"ping(java.lang.String)","test.pool.PingSkill"});

	System.out.println(" Pool is created \n");
	try { Thread.sleep(2000); } catch(Exception e) {} 
	System.out.println("\n Pool has been created \n");


	
	avant = new Date();
	System.out.println("avant");
	for (int i = 0; i < 6; i++) {
	    sentQuestions = new HashMap();
	    Date avd = new Date();
	    for(int j=0; j<3; j++) {
		it = list.iterator();
		while(it.hasNext()) {
		    Agent a = (Agent) it.next();
		    sentQuestions.put(a.ask("ping", new Object[]{a.getName()}),a);
		}
	    }
	    while (!sentQuestions.isEmpty()) {
		Iterator itsq = sentQuestions.keySet().iterator();
		while (itsq.hasNext()) {
		    Request r = (Request) itsq.next();
		    Agent a = (Agent) sentQuestions.get(r);
		    if (a.isAnswerReceived(r)) {
			itsq.remove();
		    }
		}
	    }
	    Date apd = new Date();
	    System.out.println("   >>>>>   "+(apd.getTime()-avd.getTime()));	    

	    serviceUser.perform("service", new Object[]{serviceUser.getName(), new Date()});
	}
	apres = new Date();
	System.out.println("\napres    ==> "+(apres.getTime()-avant.getTime()));
	
	
    }


} // SuperImp
