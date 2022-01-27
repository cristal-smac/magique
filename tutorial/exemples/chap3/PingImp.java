package chap3;
import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class PingImp extends AbstractMagiqueMain {
   // args[0] = super host name:port + args[1] = verbose level
   public void theRealMain(String[] args)   {
      if (args.length==2)
         Agent.setVerboseLevel(new Integer(args[1]).intValue());
   
      Agent a = createAgent("ping");
      a.addSkill(new PingSkill(a));
   
      a.connectToBoss("super@"+args[0]);
   }
}
