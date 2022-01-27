package fr.lifl.rage.skills;

import java.util.*;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.platform.*;

import fr.lifl.rage.*;
import fr.lifl.rage.wrappers.*;

//
//  (1) Verifie s'il existe des taches en attentes pour les distribuer;
//  (2) Gere les timeout sur les taches deja distribuees.
//
public class TaskDispatcherThread extends Thread {
	
    private TaskDispatcherSkill skill;

    public TaskDispatcherThread(TaskDispatcherSkill skill) {
	this.skill = skill;
    }

    private boolean dispatch(Task task, String agent) {	    
	if (agent == null) 
	    return false;

	WrapperTask wt = new WrapperTask(task);	
	Boolean dispatched = (Boolean) skill.threadAskNow(agent, 
							  "handleTask", 
							  new Object[] { wt });
	if (dispatched.booleanValue()) {
	    skill.getTime().put(task.getTaskID(), 
				skill.getCalendar().getTime());
	    skill.getRunning().add(task);		
	    return true;
	} else {
	    return false;
	}
    }
	
    public void run() {
	System.out.println("TaskDispatcher started ...");

	while(true) {				
	    // distribue les taches en attente.
	    while(!skill.getWaiting().isEmpty()) {
		Task task = (Task) skill.getWaiting().get(0);
		String agent = skill.getSelector().getPlatform(task);
		
		if (dispatch(task, agent)) {
		    skill.getWaiting().remove(0);
		}
		
	    } 
		
	    // verifie les timeout des taches actives.
	    if (skill.getTimeout() > 0) {
		if (!skill.getRunning().isEmpty()) {
		    for(int i = 0; i < skill.getRunning().size(); i++) {
			Task task = (Task) skill.getRunning().get(i);
			    Date date = (Date) skill.getTime().get(task.getTaskID());
			    long now = skill.getCalendar().getTime().getTime();
			    
			    if (now - date.getTime() > skill.getTimeout()) {
				// timeout depasse
				skill.getRunning().remove(i);
				skill.getTime().remove(task.getTaskID());
				String agent = skill.getSelector().getPlatform(task);
				
				if (!dispatch(task, agent))
				    skill.getWaiting().add(task);
			    }
		    }
		}
	    }
	}
    }
}
