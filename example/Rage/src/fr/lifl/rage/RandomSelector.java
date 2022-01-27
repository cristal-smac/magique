package fr.lifl.rage;

import java.util.*;

/**
 * Select platform agents randomly.
 */
public class RandomSelector extends Selector {

    /**
     * Pseudo random numbers generator.
     */
    protected Random random;

    /**
     * Create a new <code>RandomSelector</code>.
     */
    public RandomSelector(Collection c, long seed) {
	super(c);
	random = new Random(seed);
    }
    
    /**
     * Create a new <code>RandomSelector</code>.
     */
    public RandomSelector(long seed) {
	super();
	random = new Random(seed);
    }

    /**
     * Returns the platform agent to use.
     *
     * @param task the task to dispatch.
     * @return the next platform agent to use.
     */
    public String getPlatform(Task task) {
	if (platformAgents.isEmpty())
	    return null;

	return (String) platformAgents.get(random.nextInt(platformAgents.size()));
    }
}


