package chap4;
import fr.lifl.magique.*;

public class PingSynchroImp extends AbstractMagiqueMain {
   //args[0] = super hostname :port |arsg[1] = verbose level    
   public void theRealMain(String[] args) {
       if (args.length==2)
           Agent.setVerboseLevel(Integer.parseInt(args[1]));

       Agent a = createAgent("ping");
       a.addSkill(new PingSynchroSkill(a));
       a.connectToBoss("super@"+args[0]);
   }
}
