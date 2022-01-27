/**
 * Super.java
 *
 *
 * Created: Mon Oct 26 14:12:17 1998
 *
 * @author Jean-Christophe Routier
 * @version
 */
import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class Super extends Agent {
   
    //constructeurs
    public Super(String name) {
	super(name);
    }   

    public static void main(String[] args) {
	Platform p = new Platform();
	Agent agent = new Super("super");
	p.addAgent(agent);
	
	agent.start();
    }
    
} // Super
