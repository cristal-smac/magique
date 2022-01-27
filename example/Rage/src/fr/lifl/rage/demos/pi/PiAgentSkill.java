package fr.lifl.rage.demos.pi;

import java.util.*;
import fr.lifl.rage.*;
import fr.lifl.rage.wrappers.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class PiAgentSkill extends MagiqueDefaultSkill {
    
    private TaskFactory factory;
    private GUI gui;

    public PiAgentSkill(Agent a) {
	super(a);
	this.gui = new GUI(a);
    }

    public void startPiAgentSkill(Integer n) {
	factory = new PiFactory(1000);
	new PiAgentThread(n.intValue()).start();
	System.out.println("PiAgent thread launched for " + 
			   n.intValue() + " tasks.") ;
    }

    public void getPiResult() {
	Set theResults = (Set) askNow("getAll");
	int nres = theResults.size();
	double pi = 0;

	if (nres == 0) {
	    gui.setInfo("No result found in repository");
	    return;
	}

	gui.setInfo(nres + " results found");
	
	Iterator it = theResults.iterator();
	while(it.hasNext()) {
	    Result res = (Result) it.next();
	    	    
	    Double d = (Double) res.get(Double.class, "pi");
	    pi = pi + d.doubleValue();
	}

	pi = pi / nres;
	gui.setInfo("Result PI = " + pi);
    }

    // Problems with jikes/javac 1.2.x
    public void performWrapper(String s, Object[] tab){
	super.perform(s, tab);
    }

    class PiAgentThread extends Thread {
	
	private int numTask;
	
	public PiAgentThread(int numTask) {
	    this.numTask = numTask;
	}
	
	public void run() {
	    int n = 0;

	    System.out.println("Dispatch " + 
			       numTask + 
			       " tasks ...");

	    while(n < numTask) {
		// No need to check if there is more task
		// (see PiFactory code)
		Task t = factory.next();
		
		performWrapper("addNewTask", 
			       new Object[] {new WrapperTask(t)});
		n ++;		    
	    }
	    
	    System.out.println(n + " tasks dispatched.");
	}   
    }
}

