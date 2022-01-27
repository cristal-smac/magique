
/**
 * ConsoleDisplaySkill.java
 *
 *
 * Created: Fri Nov 05 08:29:16 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.system;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

/** this is the display used with console method used by an agent 
 */
public class ConsoleDisplaySkill extends DefaultSkill {

    private String consoleAgent;

    /** @param myAgent the agent i belong to
     * @param consoleAgent the name of the agent who manage the console 
     */
    public ConsoleDisplaySkill(AtomicAgent myAgent, String consoleAgent) {
	super(myAgent);
	this.consoleAgent = consoleAgent;
    }


    /** ConsoleDisplay <em>message</em> in the standard output stream
     * @param message the mesage to be ConsoleDisplayed
     */
    public void display(String message) {
	Object[] params = {  getName(), message };
	perform(consoleAgent,"haveAMessage",params);
    }
    
} // ConsoleDisplaySkill
