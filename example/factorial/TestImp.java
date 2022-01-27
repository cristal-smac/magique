
/**
 * TestImp.java
 *
 *
 * Created: Fri Feb 04 21:50:00 2000
 *
 * @author Philippe MATHIEU
 * @version
 */
import fr.lifl.magique.*;

public class TestImp extends AbstractMagiqueMain {
    public void theRealMain(String[] args) {

	if (args.length>=1)
	    Agent.setVerboseLevel(new Integer(args[0]).intValue());

	Agent fact = createAgent("Fact");
	fact.addSkill(new FactorialSkill(fact));

	Agent mult1 = createAgent("mult1");
	mult1.addSkill(new MultiplierSkill(mult1));
	mult1.connectToBoss("Fact");

	Agent mult2 = createAgent("mult2");
	mult2.addSkill(new MultiplierSkill(mult2));
	mult2.connectToBoss("Fact");

	Agent go = createAgent("go");
	go.connectToBoss("Fact");
	
	System.out.println(args[1]+"! = "+go.askNow("factorielle",new java.math.BigInteger(args[1])));
	


    }
    
} // TestImp
