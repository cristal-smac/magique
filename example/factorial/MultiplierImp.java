/**
 * MultiplierImp.java
 *
 *
 * Created: Mon Oct 26 14:12:17 1998
 *
 * @author Jean-Christophe Routier
 * @version
 */
import fr.lifl.magique.*;

public class MultiplierImp extends AbstractMagiqueMain {
    
    public void theRealMain (String[] args) {
	Agent.setVerboseLevel(2);

	Agent agent = createAgent(args[0]);
	agent.addSkill(new MultiplierSkill(agent));
	agent.connectToBoss("fact@"+args[1]);
    }

} // MultiplierAgent
