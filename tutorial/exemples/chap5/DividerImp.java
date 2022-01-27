package chap5;
import fr.lifl.magique.*;
//args[0] = supermath hostName
public class DividerImp extends AbstractMagiqueMain{
  public void theRealMain(String[] args) {
    if (args.length == 2) {
       Agent.setVerboseLevel(Integer.parseInt(args[1]));
    }  
    Agent divider = createAgent("divider");
    divider.addSkill(new chap5.DividerSkill());
    divider.connectToBoss("supermath@"+args[0]);
  }
}
