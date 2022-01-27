
/**
 * MagiqueActionSkill
 *
 *
 * Created: Tue Nov 30 14:45:48 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */

package fr.lifl.magique.skill; 
import fr.lifl.magique.*;

/** this is the class to inherit from to create an action skill. It
 *    corresponds to proactive part of the agent. No other public
 *    method than <tt>action</tt> should appear in a class that
 *    extends this. */
public abstract class MagiqueActionSkill extends MagiqueDefaultSkill {
 
    /** @param myAgent is to keep a reference on the agent this
     * service is belonging to */
    public MagiqueActionSkill(Agent myAgent){
	super(myAgent);
    }

    public abstract void action();

} // MagiqueActionSkill
 
