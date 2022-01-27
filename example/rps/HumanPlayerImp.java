
/**
 * HumanPlayerImp.java
 *
 *
 * Created: Fri Apr 30 11:01:15 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
import fr.lifl.magique.*;

public class HumanPlayerImp extends AbstractMagiqueMain  {

    public void theRealMain (String[] args)  {
	if (args.length==1)
	    Agent.setVerboseLevel(new Integer(args[0]).intValue());

	Agent humanPlayer = createAgent("humanPlayer");
	humanPlayer.addSkill(new HumanPlayerSkill(humanPlayer));

       	humanPlayer.connectToBoss("bank@HOSTNAME:4444");
	humanPlayer.start();
    }

} // HumanPlayerImp
