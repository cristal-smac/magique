package chap6;
import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class PongImp extends AbstractMagiqueMain {

   // args[0] = verboseLevel **  args[1] = super Host+port
   public void theRealMain(String[] args) {

      Agent.setVerboseLevel(Integer.parseInt(args[0]));

      Agent a = createAgent("pong");
      a.connectToBoss("super@"+args[1]);
      a.perform("learnSkill",
                new Object[]{"chap6.PongSkill",
                             "super@"+args[1], Boolean.TRUE});
   }
}
