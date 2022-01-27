
/**
 * ConsoleSkill.java
 *
 *
 * Created: Wed Nov 03 09:26:32 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.system;
import fr.lifl.magique.*;

public class ConsoleSkill extends fr.lifl.magique.skill.DefaultSkill{

    private String consoleAgent;

    /** @param myAgent the agent i belong to
     * @param consoleAgent the name of the agent who manage the console 
     */
    public ConsoleSkill(AtomicAgent myAgent, String consoleAgent) {
	super(myAgent);
	this.consoleAgent = consoleAgent;
	this.init();
    }



    private void init() {
	removeSkill("display(java.lang.String)");
	getMyAgent().addSkill(new ConsoleDisplaySkill(getMyAgent(),consoleAgent));
    }

    /** connect the agent who has this skill to the consoleAgent */
    public void connectToConsole() {
	perform("connectTo",new Object[]{consoleAgent});
    }

} // ConsoleSkill
