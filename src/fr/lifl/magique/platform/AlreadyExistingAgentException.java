/**
 * AlreadyExistingAgentException.java
 * <p>
 * <p>
 * Created: Tue Jan 25 2000
 *
 * @author JC Routier
 * @version
 */
package fr.lifl.magique.platform;

public class AlreadyExistingAgentException extends RuntimeException {

    private String message;

    public AlreadyExistingAgentException(String agentName) {
        message = agentName;
        System.err.println("Already existing Agent : " + agentName);
    }

} // AlreadyExistingAgentException
