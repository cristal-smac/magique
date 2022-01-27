/**
 * GoImp.java
 *
 *
 * Created: Mon Oct 26 14:12:17 1998
 *
 * @author Jean-Christophe Routier
 * @version
 */
import fr.lifl.magique.*;

public class GoImp extends AbstractMagiqueMain {
    
    public void theRealMain (String[] args)  {
	Agent agent = createAgent("starter");
	agent.connectTo("fact@"+args[0]);	

	System.out.println(args[1]+"! = "+agent.askNow("fact@"+args[0],"factorielle",new java.math.BigInteger(args[1])));

    }

} // GoAgent
