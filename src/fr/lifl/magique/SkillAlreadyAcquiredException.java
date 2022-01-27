
/**
 * SkillAlreadyAcquiredException.java
 *
 *
 * Created: Mon Oct 11 15:53:06 1999
 * 
 * @author Yann.Secq
 * @version
 */
package fr.lifl.magique;

/** This exception is thrown when an agent tries to learn a skill who
 *  contains a public method with the same signature as another
 *  laready known by the agent */
public class SkillAlreadyAcquiredException extends RuntimeException {    

    public SkillAlreadyAcquiredException(String methodName) {
	super("Method already known : "+methodName);
    }
    
} // SkillAlreadyAcquiredException
