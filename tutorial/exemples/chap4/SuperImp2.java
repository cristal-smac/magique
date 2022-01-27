package chap4;
import fr.lifl.magique.*;

public class SuperImp2 extends AbstractMagiqueMain {
    public void  theRealMain(String[] args) {
        if (args.length==1)
            Agent.setVerboseLevel(Integer.parseInt(args[0]));

        Agent a = createAgent("sup");
        a.addSkill(new CalculCompletSynchrone(a));
    }
    
} // SuperImp
