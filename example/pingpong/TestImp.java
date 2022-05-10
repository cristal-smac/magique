
/**
 * TestImp.java
 *
 *
 * Created: Wed Jun 13 11:56:10 2001
 *
 * @author <a href="mailto: "Jean-Christophe Routier</a>
 * @version
 */
import fr.lifl.magique.*;

public class TestImp extends AbstractMagiqueMain {

    public void theRealMain(String args[]) {

	if (args.length==1)
	    Agent.setVerboseLevel(Integer.parseInt(args[0]));
	
	Agent chef = createAgent("super");
	Agent ping = createAgent("agentPing");
	Agent pong = createAgent("agentPong");

	ping.addSkill(new PingSkill(ping));
	pong.addSkill(new PongSkill(pong));
	
	ping.connectToBoss("super");
	pong.connectToBoss("super");

	ping.perform("ping",0);
    }

}// TestImp
