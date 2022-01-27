/**
 * RandomPlayerImp.java
 *
 *
 * Created: Fri Apr 30 11:01:15 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
import fr.lifl.magique.*;

public class RandomPlayerImp extends AbstractMagiqueMain {

    public void theRealMain (String[] args) {
	if (args.length==1)
	    Agent.setVerboseLevel(new Integer(args[0]).intValue());

	Agent randomPlayer = createAgent("randomPlayer");
	randomPlayer.addSkill(new RandomPlayerSkill(randomPlayer));

       	randomPlayer.connectToBoss("bank@HOSTNAME:4444");
	randomPlayer.start();
    }

} // RandomPlayerImp
