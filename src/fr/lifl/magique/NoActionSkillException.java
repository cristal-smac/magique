
/**
 * NoActionSkillException.java
 *
 *
 * Created: Wed Feb 09 10:24:57 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique;

/** This exception is thrown when an agent tries to "start", and has
 *   no action defined (must be defined with "setAction()" learn a
 *   skill who 
 * 
 * @see fr.lifl.magique#start()
 * @see fr.lifl.magique#setAction(Object)
*/

public class NoActionSkillException extends RuntimeException {
    
    public NoActionSkillException(String agentName) {
	super("no action skill is defined for "+agentName);
    }
    
} // NoActionSkillException
