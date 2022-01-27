package chap4;
import fr.lifl.magique.*;

public class PongSynchroImp extends AbstractMagiqueMain {
    // args[0] = super host name :port | arsg[1] = verbose level
    public void theRealMain(String[] args) {
        if (args.length==2)
            Agent.setVerboseLevel(Integer.parseInt(args[1]));

        Agent a = createAgent("pong");
        a.addSkill(new PongSynchroSkill(a));
        a.connectToBoss("super@"+args[0]);
    }
}
