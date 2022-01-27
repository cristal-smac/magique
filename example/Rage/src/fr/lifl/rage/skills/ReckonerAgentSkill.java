package fr.lifl.rage.skills;

import fr.lifl.rage.*;
import fr.lifl.rage.wrappers.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class ReckonerAgentSkill extends MagiqueDefaultSkill {

    /**
     * The task handled by this agent.
     */
    protected Task task;

    public ReckonerAgentSkill(Agent a) {
	super(a);
    }

    /**
     * Set the task for this agent. 
     */
    public Boolean setTask(WrapperTask wrapperTask) {
	task = wrapperTask.getTask();

	if (task == null)
	    return Boolean.FALSE;

	boolean result = task.init();

	task.setAgent(getMyAgent());
	
	if (result) {
	    task.launch();
	    return Boolean.TRUE;
	}

       	return Boolean.FALSE;
    }

    /**
     * Return <code>true</code> if the task is finished.
     */
    public Boolean isTaskFinished() {
	if (task == null || task.finished())
	    return Boolean.TRUE;

	return Boolean.FALSE;
    }

    /**
     * Kill the task.
     */
    public void killTask(String cond) {
	if (task.mustDie(cond))
	    task.die();
    }
}

