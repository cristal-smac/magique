
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

	Agent chef = new Agent("super");
	Agent ping = new Agent("ping");
	Agent pong = new Agent("pong");

	platform.addAgent(chef);
	platform.addAgent(ping);
	platform.addAgent(pong);

	ping.addSkill(new PingSkill(ping));
	pong.addSkill(new PongSkill(pong));

	ping.connectToBoss("super");
	pong.connectToBoss("super");

	chef.perform("ping", 0);
    }

}// TestImp
