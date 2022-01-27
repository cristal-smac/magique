
import fr.lifl.magique.*;

//args[0] = supermath hostName
public class AdderImp extends AbstractMagiqueMain{
  public void theRealMain(String[] args) {
    if (args.length == 2) {
       Agent.setVerboseLevel(Integer.parseInt(args[1]));
    }  
    Agent adder = createAgent("adder");
    adder.addSkill(new AdderSkill());
    adder.connectToBoss("supermath@"+args[0]);
  }
}
