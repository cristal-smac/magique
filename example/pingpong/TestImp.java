
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

	Agent chef = createAgent("super");
	Agent ping = createAgent("ping");
	Agent pong = createAgent("pong");

	ping.addSkill(new PingSkill(ping));
	pong.addSkill(new PongSkill(pong));
	
	ping.connectToBoss("super");
	pong.connectToBoss("super");

	chef.perform("ping");
    }

}// TestImp
