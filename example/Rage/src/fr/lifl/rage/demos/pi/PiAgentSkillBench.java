package fr.lifl.rage.demos.pi;

import java.util.*;
import fr.lifl.rage.*;
import fr.lifl.rage.wrappers.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class PiAgentSkillBench extends MagiqueDefaultSkill {
    
    public PiAgentSkillBench(Agent a) {
	super(a);
    }

    public void startPiAgentSkill() {
	TaskFactory factory;
	int[] iterations = { 1000, 10000, 20000, 30000, 40000, 
			     50000, 60000, 70000, 80000, 100000 };

	for(int i = 0; i < 10; i ++) {
	    factory = new PiFactory(iterations[i]);
	    
	    for(int ntasks = 1000; ntasks < 10000; ntasks += 1000) {

		for(int j = 0; j < ntasks; j ++) {
		    Task t = factory.next();
		
		    perform("addNewTask", 
			    new Object[] {new WrapperTask(t)});
		}

		System.err.println(ntasks + " tasks for " + iterations[i] +
				   " iterations dispatched");

		Integer n;
		long start = System.currentTimeMillis();

		while(true) {
		    n = (Integer) askNow("numberOfElements");
		    if (n.intValue() == ntasks)
			break;
		}

		long end = System.currentTimeMillis();
		System.out.println(ntasks + " " + 
				   iterations[i] + " " + 
				   (end-start)/1000);

		perform("cleanUp");
		//System.gc();
	    }
	}   
    }
}

