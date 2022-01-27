package fr.lifl.rage.skills;

import java.util.*;
import fr.lifl.rage.*;
import fr.lifl.rage.wrappers.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.platform.*;

public class PlatformManagerSkill extends MagiqueDefaultSkill {

    /**
     * The shared datas.
     */
    protected HashMap sharedDatas;

    /**
     * The task dispatcher name.
     */
    protected String taskDispatcherName;

    /**
     * Number of reckoner agents launched on this platform. Default is 1.
     */
    protected int numAgents = 10;

    /**
     * The reckoner agents.
     */
    protected ArrayList agents;

    /**
     * Create a new <code>PlatformManagerSkill</code> 
     */
    public PlatformManagerSkill(Agent a) {
	super(a);
	sharedDatas = new HashMap();
    }

    public void startPlatformManagerSkill() {
	Platform p = getMyAgent().getPlatform();

	this.agents = new ArrayList(numAgents);

	for(int i = 0; i < numAgents; i ++) {
	    Agent a = new Agent("ReckonerAgent" + i);

	    a.addSkill(new ReckonerAgentSkill(a));
	    p.addAgent(a);
	    a.connectToBoss(getMyAgent().getName());
	    agents.add(a);
	}
	
	System.out.println(getName() + ": " + numAgents + " agents launched");

	new PlatformManagerThread().start();
    }
    
    public void killTask(String cond) {
	for(int i = 0; i < agents.size(); i ++) 
	    perform(((Agent)(agents.get(i))).getName(), 
		    "killTask", 
		    new Object[] {cond});    
    }
    
    public Object getSharedData(String key) {
	if (sharedDatas.containsKey(key)) 
	    return sharedDatas.get(key);
	
	// Objet non trouve. Demander au TaskManager.
	Object o = askNow("getData", new Object[] {key});
	System.out.println(getName() + ": " + key + " in cache");
	return o;
    }

    public void setNumberOfAgents(Integer numAgents) {
	this.numAgents = numAgents.intValue();
    }

    public Integer getNumberOfAgents() {
	return new Integer(numAgents);
    }

    class PlatformManagerThread extends Thread {   	

	public void run() {
	    ArrayList questions = new ArrayList();
	    Boolean rep;
	    Request req;
	    Agent ag;

	    // Attend qu'un task dispatcher se pointe
	    String taskDispatcherName = (String) 
		askNow("taskDispatcherName");

	    System.err.println(getMyAgent().getName()+ 
			       ": my TaskDispatcher is " + 
			       taskDispatcherName);

	    while(true) {	        
		// A changer éventuellement en sleep(xx);
		Thread.yield();

		// Interroger les agents pour savoir s'ils ont fini
		for(int i = 0; i < agents.size(); i ++) {
		    ag = (Agent) agents.get(i);
		    questions.add(ask(ag.getName(), "isTaskFinished"));
		}

		while(!questions.isEmpty()) 
		    for(int i = 0; i < questions.size(); i ++) {
			req = (Request) questions.get(i);
			
			if (isAnswerReceived(req)) {
			    Boolean b = (Boolean) req.getAnswer();

			    if (b.booleanValue()) {
				String answerer = req.getAnswerer();
				Task task = (Task) askNow(taskDispatcherName, 
							  "getNextTask");
				if (task != null) {
				    perform(answerer, 
					    "setTask", 
					    new Object[]{new WrapperTask(task)});
				}
			    }
			    
			    questions.remove(i);
			}				
		    }
	    }
	}    				
    }
}
