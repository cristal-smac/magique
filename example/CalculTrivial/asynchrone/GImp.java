package chap4;
import fr.lifl.magique.*;

public class GImp extends AbstractMagiqueMain {    
    // args[0] = sup host name :port | arsg[1] = verbose level
    public void theRealMain(String[] args) {
        if (args.length==2)
            Agent.setVerboseLevel(Integer.parseInt(args[1]));

        Agent a = createAgent("g");

        a.addSkill(new CalculTrivial());
        a.connectToBoss("sup@"+args[0]);
    }
} // FImp
