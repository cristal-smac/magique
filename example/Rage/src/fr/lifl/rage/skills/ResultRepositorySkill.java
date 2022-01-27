package fr.lifl.rage.skills;

import com.db4o.*;
import java.util.*;
import fr.lifl.rage.*;
import fr.lifl.rage.wrappers.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class ResultRepositorySkill extends MagiqueDefaultSkill {
    
    protected ObjectContainer container;

    /**
     * Creates a new <code>ResultRepositorySkill</code> which will use
     * the file <code>filename</code> to store <code>Result</code> objects.
     *
     * @param a the agent which hold the new skill.
     * @param filename the file to store results.
     */
    public ResultRepositorySkill(Agent a, String filename) {
	super(a);

	Db4o.echo(true);
	System.out.println("Db4o version string: " + Db4o.version());
	System.out.println("Container file name is: " + filename);

	try {
	    container = Db4o.openFile(filename);
	} catch(Exception err) {
	    System.err.println("ResultRepositorySkill: cannot open " +
				filename);
	    System.err.println(err);
	}
    }

    /**
     * Creates a new <code>ResultRepositorySkill</code>. The file used to store
     * <code>Result</code> objects is named "rage.container" and is placed in
     * the current directory.
     *
     * @param a the agent which hold the new skill.
     */
    public ResultRepositorySkill(Agent a) {
	this(a, "rage.container");
    }

    /**
     * Remove all objects in the container
     */
    public void cleanUp() {
	ObjectSet set = container.get(null); // get all elements
	
	while(set.hasNext()) 
	    container.delete(set.next());
    }
    
    /**
     * Get a set of <code>Result</code> objects which match the template
     * result passed as parameter.
     * 
     * <B>Note:</B> only publics members are evaluated.
     *
     * @param templateResult 
     */
    public Set getResult(WrapperResult templateResult, boolean remove) {
	Result res = templateResult.getResult();
	ObjectSet oset = container.get(res);
	Set set = new HashSet(oset.size());

	oset.reset();
	while(oset.hasNext()) {
	    Object o = oset.next();
	    
	    container.activate(o);
	    set.add(o);
	    if (remove)
		container.delete(o);
	}

	return set;
    }
    
    /**
     * Equivalent to <code>getResult(templateResult, false)</code>.
     */
    public Set getResult(WrapperResult templateResult) {
	return getResult(templateResult, false);
    }

    public Set getAndDeleteAll() {
	return getResult(new WrapperResult(null), true);
    }

    /**
     * Retrieves all <code>Result</code> objects stored in the container.
     */
    public Set getAll() {
	// In Db4o documentation:
	// " ... calling get(null) returns all objects stored in the container"
	// (javadoc, interface ObjectContainer, method get)
	//
        return getResult(new WrapperResult(null));
    }

    /**
     * Adds a new <code>Result</code> to this repository
     *
     * @param wrapperResult the result to add.
     */
    public void addResult(WrapperResult wrapperResult) {	
	Result result = wrapperResult.getResult();
 	
	// s'assurer que le resultat n'est pas deja connu 
	// (cf timeout du TaskDispatcher et relancement)
	if (!container.isStored(result)) {
	    container.set(result);
	    perform("completedTask", new Object[] { result.getTaskID() });
	}
    }
	
    /**
     * Test if a particular template <code>Result</code> is stored 
     * in this container.
     *
     * @return true if the passed result is stored.
     */
    public Boolean isStored(WrapperResult result) {
	if (container.isStored(result.getResult()))
	    return Boolean.TRUE;

	return Boolean.FALSE;
    }

    /**
     * Return the number of elements contained in the repository.
     */
    public Integer numberOfElements() {
	return new Integer((container.get(null)).size());
    }
}
