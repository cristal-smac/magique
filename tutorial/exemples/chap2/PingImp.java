package chap2;
import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class PingImp extends AbstractMagiqueMain { 
    // args[0]=super hostName:port + args[0] = verbose level 
    public void theRealMain(String[] args) {
        if (args.length==2)
            Agent.setVerboseLevel(Integer.parseInt(args[1]));

        Agent a = createAgent("ping");
	a.addSkill(new chap2.PingSkill2(a));
        a.connectToBoss("super@"+args[0]);
    }
}
