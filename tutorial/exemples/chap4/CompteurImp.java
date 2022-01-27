package chap4;
import fr.lifl.magique.*;

public class CompteurImp extends AbstractMagiqueMain {
    // args[0] = super host name :port | arsg[1] = verbose level
    public void theRealMain(String[] args) {
        if (args.length==2)
            Agent.setVerboseLevel(Integer.parseInt(args[0]));

        Agent a = createAgent("compteur");
        a.addSkill(new Compteur());

        a.connectToBoss("super@"+args[0]);
    }
}
