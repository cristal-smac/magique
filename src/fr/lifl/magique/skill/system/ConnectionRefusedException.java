
/**
 * ConnectionRefusedException.java
 *
 *
 * Created: Tue Jan 25 13:50:39 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.system;

public class ConnectionRefusedException extends RuntimeException {

    public ConnectionRefusedException(String agent) {
	super();
	System.err.println(agent+" refuses connection ");
    }
    
} // ConnectionRefusedException
