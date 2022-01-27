
/**
 * DisplaySkill.java
 *
 *
 * Created: Fri Nov 05 08:29:16 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.system;
import fr.lifl.magique.*;

/** this is the default display method used by an agent 
 */
public class DisplaySkill implements Skill {

    /** display <em>message</em> in the standard output stream
     * @param message the message to be displayed
     */
    public void display(java.lang.String message) {
	fr.lifl.magique.AbstractAgent.verbose(-10000,message);
    }
    
} // DisplaySkill
