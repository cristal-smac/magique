package chap5;
import fr.lifl.magique.*;
//args[0] = supermath hostName
public class MultiplierImp extends AbstractMagiqueMain {
  public void theRealMain(String[] args) {
    if (args.length == 2) {
       Agent.setVerboseLevel(Integer.parseInt(args[1]));
    }  
    Agent multiplier = createAgent("multiplier");
    multiplier.addSkill(new chap5.MultiplierSkill());
    multiplier.connectToBoss("supermath@"+args[0]);
  }
}
