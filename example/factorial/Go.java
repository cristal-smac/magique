
/**
 * Go.java
 *
 *
 * Created: Tue Jan 19 21:49:40 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class Go extends Agent {
    
    public Go(String name) { 
	super(name); 
    }

    public void action() {
	System.out.print("started ");
	perform("go");
	System.out.println("done");	
	perform("disconnectAndDie");
    }
    
    public static void main (String[] args) throws java.io.IOException {
	Platform p = new Platform();
	Go go = new Go("go");
	p.addAgent(go);
	go.perform("connectToBoss","super@HOSTNAME:4444");

	go.start();	
    }

    
} // Go
