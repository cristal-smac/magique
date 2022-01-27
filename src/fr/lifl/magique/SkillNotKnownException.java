
/**
 * SkillNotKnownException.java
 *
 *
 * Created: Tue Feb 08 16:10:57 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique;

/** This exception is thrown when an agent tries to remove a not known skill
 */

public class SkillNotKnownException extends RuntimeException {   

    public SkillNotKnownException (String methodName) {
	super("Skill not known : "+methodName);
    }
 
} // SkillNotKnownException
