package fr.lifl.rage.skills;

import java.util.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.platform.*;
import fr.lifl.rage.*;
import fr.lifl.rage.wrappers.*;

public class TaskDispatcherSkill extends MagiqueDefaultSkill {

    /**
     * List of the tasks waiting to be distributed.
     */
    protected List waiting;

    /**
     * Tasks which have been dispatched.
     */
    protected List running;

    /**
     * Create a new <code>TaskDispatcherSkill</code>.
     */
    public TaskDispatcherSkill(Agent a) {
	super(a);

	this.waiting = Collections.synchronizedList(new ArrayList());
	this.running = Collections.synchronizedList(new ArrayList());
    }

    /**
     * Return the name of the <code>TaskDispatcher</code> agent, so
     * <code>PlatformManager</code> can be dynamically added without
     * any administration.
     */
    public String taskDispatcherName() {
	return getMyAgent().getName();
    }

    /**
     * Adds a new <code>Task</code> to the dispatcher.
     *
     * @param wrapperTask the wrapper containing the new <code>Task</code>
     *        to add.
     */
    public void addNewTask(WrapperTask wrapperTask) {
	waiting.add(wrapperTask.getTask());
    }

    /**
     * Returns the number of tasks waiting on the dispatcher.
     */
    public Integer waitingTasks() {
	return new Integer(waiting.size());
    }

    /**
     * Returns the number of dispatched tasks.
     */
    public Integer runningTasks() {
	return new Integer(running.size());
    }

    /**
     * Return the next task to compute.
     */
    public Task getNextTask() {
	Task task = null;

	if (waiting.size() > 0) {
	    task = (Task) waiting.remove(0); 
	    running.add(task);
	}
	
	return task;
    }

    /**
     * Indicates that this task is finished.
     * (enlève la tache des taches actives)
     * @param taskId the identificator of this <code>Task</code>.
     */
    public void completedTask(String taskId) {
	removeTaskFromList(running, taskId);
    }

    private void removeTaskFromList(List l, String taskId) {
	Iterator it;
	Task t;

	for(it = l.iterator(); it.hasNext(); ) {
	    t = (Task) it.next();
	    if (t.getTaskID().equals(taskId))
		break;
	}
	
	if (it != null) 
	    it.remove();
    }
}
