package fr.lifl.rage;

import java.util.*;

/**
 * Select platform agents in the same order they were added. 
 * After to have selected the last one, return to the beginning.
 */
public class RoundSelector extends Selector {

    protected int index = 0;

    /**
     * Create a new <code>RoundSelector</code>.
     */
    public RoundSelector(Collection c) {
	super(c);
    }
    
    /**
     * Create a new <code>RoundSelector</code>.
     */
    public RoundSelector() {
	super();
    }

    /**
     * Returns the platform to use.
     *
     * @param task the task to dispatch.
     * @return the next platform to use.
     */
    public String getPlatform(Task task) {
	if (platformAgents.isEmpty())
	    return null;
	
	return (String) platformAgents.get((index++) % platformAgents.size());
    }
}




