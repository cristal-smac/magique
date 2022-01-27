package chap4;
import fr.lifl.magique.*;

public class SuperImp3 extends AbstractMagiqueMain {
    public void theRealMain(String[] args) {
        if (args.length==1)
            Agent.setVerboseLevel(Integer.parseInt(args[0]));

        Agent a = createAgent("sup");
        a.addSkill(new CalculCompletAsynchrone(a));
    }
    
} // SuperImp
