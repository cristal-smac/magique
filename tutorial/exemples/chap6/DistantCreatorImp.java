package chap6;
import fr.lifl.magique.*;

public class DistantCreatorImp extends AbstractMagiqueMain {

  //args[0]=verboseLevel|args[1]=ping Host|args[2]=pong host
  public  void theRealMain(String[] args) {
    
    Agent.setVerboseLevel(Integer.parseInt(args[0]));
    
    Agent a = createAgent("distantSuper");
    a.addSkill(new chap6.DistantCreatorSkill(a));     
    a.perform("distantCreate", new Object[]{args[1],args[2]});
  }
}
