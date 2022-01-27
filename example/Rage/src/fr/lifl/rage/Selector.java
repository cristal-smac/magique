package fr.lifl.rage;

import java.io.*;
import java.util.*;

/**
 * Object from this class are used by the <code>TaskDispatcher</code> 
 * agent to select a particular platform among a set of available 
 * platforms, given a <code>Task</code>.
 *
 * Application which needs a specific way to dispatch tasks have to 
 * provide a specific <code>Selector</code>.
 *
 * @author Patrice Boulanger
 */
public abstract class Selector implements Serializable {

    /**
     * Contains all the available platform agents. 
     */
    protected ArrayList platformAgents;

    /**
     * Create a new <code>Selector</code>.
     */
    public Selector(Collection c) {
	platformAgents = new ArrayList(c);
    }
    
    /**
     * Create a new <code>Selector</code>.
     */
    public Selector() {
	platformAgents = new ArrayList();
    }

    /**
     * Add a new platform to this <code>Selector</code>.
     *
     * @param platform the name of the platform in the format 
     *        <em>name@host:port</em>
     */
    public void add(String platform) {
	platformAgents.add(platform);
    }

    /**
     * Remove a platform from this <code>Selector</code>.
     *
     * @param platform the name of the platform in the format 
     *        <em>name@host:port</em>
     */
    public void remove(String platform) {
	int i;

	for(i = 0; i < platformAgents.size(); i ++) {
	    String p = (String) platformAgents.get(i);
	    if (p.equals(platform)) 
		break;
	}

	if (i < platformAgents.size())
	    platformAgents.remove(i);
    }

    /**
     * Returns an array containing all the platform agents knew by this 
     * <code>Selector</code>
     */
    public String[] toArray() {
	String[] s = (String[]) platformAgents.toArray();
	return s;
    }

    /**
     * Returns the platform to use.
     *
     * @param task the task to dispatch.
     */
    public abstract String getPlatform(Task task);
}



