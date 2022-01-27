package fr.lifl.rage;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import fr.lifl.magique.*;
import fr.lifl.rage.wrappers.*;

/**
 *  Extend this class to create the <code>Task</code> objects for your 
 *  application and override the methods <code>compute</code>, 
 *  <code>finished</code> and <code>percent</code>.
 *
 *  We also wrap the main methods of the magique agent class, thus you can use 
 *  these methods without 
 *  having to add the <code>myAgent</code> before. This allows to lighten the 
 *  code.
 *
 *  @author Boulanger Patrice
 *  @see Task State
 */
public abstract class AbstractTask implements Task {  

    protected String factoryId;
    protected String name;

    transient private Agent myAgent;
    transient protected Thread thread;
    transient protected boolean isSuspended;
    transient protected boolean wasKilled;

    /**
     * Create a new AbstractTask
     */
    public AbstractTask() {
	// Nothing 
    }

    /**
     * Create a new AbstractTask 
     *
     * @param name The name of the <code>Task</code> object. Each 
     * <code>Task</code> provided by a <code>TaskFactory</code> must have an
     * unique name.
     * @see TaskFactoryAdapter#getNewTaskName()
     */
    public AbstractTask(String factoryId, String name) {
	this.factoryId = factoryId;
	this.name = name;
    }

    public void setAgent(Agent agent) {
	myAgent = agent;
    }

    public void launch() {
	isSuspended = false;
	wasKilled = false;
	(new AbstractTaskThread()).start();
    }

    public boolean suspend() {
	isSuspended = true;
	return isSuspended;
    }

    abstract public void compute();
    abstract public boolean finished();
    abstract public double percent();
    
    /**
     * A default <code>mustDie</code> method which returns always 
     * <code>false</code>.
     */
    public boolean mustDie(Object cond) {
	return false;
    }

    public void die() {
	this.wasKilled = true;
	this.isSuspended = true;
    }

    /**
     * A default <code>init</code> method which simply returns 
     * <code>true</code>. Thus, only tasks which need special initialization
     * have to override this method.
     *
     * @return always <code>true</code>.
     */
    public boolean init() {
	return true;
    }

    /** 
     *  Returns the result of a computed task. Fields which have the 
     *  <code>transient</code> modifier are not save in the result.
     *
     *  @return the result of the ended task.
     *  @see Result
     *  @see ResultAdapter
     */    
    public Result getResult() {
	Result result = (Result) new ResultAdapter();
	Class current = getClass();
	
	while (!current.equals(Object.class)) {
	    Field[] fields = current.getDeclaredFields();

	    for (int i=0; i < fields.length; i++) {
		try {
		    //boolean access = fields[i].isAccessible();
		    //fields[i].setAccessible(true);
		    
		    // N'ajouter que les champs avec le modificateur public
		    if (Modifier.isPublic(fields[i].getModifiers())) 
			result.put(fields[i].getType(), 
				   fields[i].getName(), 
				   fields[i].get(this));
		    
		    //fields[i].setAccessible(access);
		} catch (IllegalAccessException e){ 
		    // e.printStackTrace();
		}
	    }
	    current = current.getSuperclass();
	}

	result.setTaskID(getTaskID());
	return result;
    }
    
    public void setFactoryID(String factoryId) {
	this.factoryId = factoryId;
    }
    
    public String getFactoryID() {
	return this.factoryId;
    }

    public int getTimeout() {
	return 60*5; // 5 minutes par defaut
    }

    /**
     * The task identificator is the concatenation of the factory 
     * identificator of the <code>TaskFactory</code> which provides 
     * the task and of the name of the task.
     *
     * @return the task identificator in the format 
     *         <I>factoryID</I>.<I>task_name</I>
     */
    public String getTaskID() {
	return this.factoryId + "." + this.name;
    }

    //
    // Wrap the mains methods of the magique agent class.
    //
    protected Request ask(String request) {
	return myAgent.ask(request);
    }

    protected Request ask(String request, Object[] params) {
	return myAgent.ask(request, params);
    }

    protected Object askNow(String request) {
	return myAgent.askNow(request);
    }
    
    protected Object askNow(String request, Object[] params) {
	return myAgent.askNow(request, params);
    }

    protected void perform(String request) {
	myAgent.perform(request);
    }

    protected void perform(String request, Object[] params) {
	myAgent.perform(request, params);
    }
    
    class AbstractTaskThread extends Thread {
	public void run() {
	    while(!finished() && !wasKilled) {
		if (isSuspended) {
		    synchronized(this) {
			while(isSuspended) 
			    try {
				wait();
			    } catch (InterruptedException e) {
				e.printStackTrace();
			    }
		    }
		}
		compute();		    
	    }

	    if (!wasKilled) {
		WrapperResult result = new WrapperResult(getResult());
		myAgent.perform("addResult", new Object[]{ result });
	    }
	}
    }
}







