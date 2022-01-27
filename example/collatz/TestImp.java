
/**
 * TestImp.java
 *
 *
 * Created: Tue Mar 23 16:32:35 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */

import fr.lifl.magique.*;

import java.util.Vector;

public class TestImp extends AbstractMagiqueMain {
    
    public void theRealMain (String[] args) {
	if (args.length>=1)
	    Agent.setVerboseLevel(new Integer(args[0]).intValue());
	
	Agent superviseur = createAgent("dieu"); 

	Agent ange = createAgent("ange"); 
	Agent multiplier = createAgent("multiplier");
	Agent divider = createAgent("divider");
	Agent parity = createAgent("parity");
       	Agent collatz = createAgent("Collatz");    
     	

	collatz.addSkill(new CollatzSkill(collatz));
	divider.addSkill(new DividerSkill());
	parity.addSkill(new ParitySkill());
	multiplier.addSkill(new MultiplierSkill());

	// établissement des connections - connections are established
	ange.connectToBoss("dieu");
	multiplier.connectToBoss("ange");
	divider.connectToBoss("ange");
	parity.connectToBoss("ange");
	collatz.connectToBoss("dieu");

	Agent adder = createAgent("adder");
	adder.connectToBoss("ange");
	adder.addSkill(new AdderSkill());
 
	// proactive agent is started
	Integer x;
	if (args.length <2) {
	    x = new Integer(17);
	}
	else {
	    x = new Integer(args[1]);
	}
	collatz.perform("conjecture",x);

    }

} // TestImp
