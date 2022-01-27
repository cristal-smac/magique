package chap6;
import fr.lifl.magique.*;

public class SuperImp extends AbstractMagiqueMain {

   // args[0] = verboseLevel **  args[1] = ping Host
   public  void theRealMain(String[] args) {
   
      Agent.setVerboseLevel(Integer.parseInt(args[0]));
   
      Agent a = createAgent("super");
      a.perform("ping@"+args[1], "learnSkill", 
                "chap6.PingSkill", a.getName(), Boolean.TRUE);   
      a.perform("ping");
   }
}
