
/**
 * TeamInfo
 *
 *
 * Created: Fri Oct 16 16:05:41 1998
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.util;
import fr.lifl.magique.*;
import java.util.*;

/** stores vector of names of all the agents occuring in the hierarchy
 * under some agent (the one maps in the Team object) and vector of
 * names of all the methods that can be achieved by one of these
 * agents
 *
 * @see fr.lifl.magique.util.Team */
public class TeamInfo  implements java.io.Serializable {
    
    /** the vector of the names of the agents 
     * @serial
     */
    private Vector names;
    /** the vector of the names of the methods      
     * @serial
     */
    private Vector methods;
    
    /**
     * @param names the vector of the names of the agents 
     * @params methods the vector of the names of the mathods
     */
    public TeamInfo(Vector names,Vector methods) {	
	this.names = names;
	this.methods = methods;
    }

    /** returns the names 
     * @return the names 
     */
    public synchronized Vector getNames() {return names; }
    /** returns the methods
     * @return the methods
     */
    public synchronized Vector getMethods() {return methods; }

    /** adds a name to the already known names 
     * @param name the name to add
     */
    public synchronized void addName (String name) {
	if (!names.contains(name)) {
	    //	    System.out.println("add name "+name);
	    names.addElement(name);
	}
    }
    /** adds a set of names to the already known names 
     * @param name the names to add
     */
    public void addNames (Vector names) {
	for (Enumeration enu=names.elements();enu.hasMoreElements();) {
	    addName((String) enu.nextElement());
	}
    }
    /** adds a method to the already known methods
     * @param name the method to add
     */
    public synchronized void addMethod (String method) {
	if (!methods.contains(method)) {
	    //	    System.out.println("add method "+method);
	    methods.addElement(method);
	}
    }
    /** adds a set of methods to the already known methods
     * @param name the methods to add
     */
    public void addMethods (Vector methods) {
	for (Enumeration enu=methods.elements();enu.hasMoreElements();) {
	    addMethod((String) enu.nextElement());
	}
    }

    
} // TeamInfo
 
