package chap6;
import fr.lifl.magique.*;

public class SuperCreatorImp extends AbstractMagiqueMain {

    // args[0] = verboseLevel **  args[1] = ping Host
    public  void theRealMain(String[] args) {
	
        Agent a = createAgent("super");
        a.addSkill(new chap6.CreatorSkill(a));
        a.perform("create");
   }

}
