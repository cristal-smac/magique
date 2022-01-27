import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class PongImp extends AbstractMagiqueMain {
    // args[0]=super hostName:port + args[1] = verbose level 
    public void theRealMain(String[] args) {
        if (args.length==2)
            Agent.setVerboseLevel(Integer.parseInt(args[1]));

        Agent a = createAgent("pong");
        a.addSkill(new PongSkill(a));
        a.connectToBoss("super@"+args[0]);
    }
}
