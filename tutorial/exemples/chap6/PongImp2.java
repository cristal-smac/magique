package chap6;

import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class PongImp2 extends AbstractMagiqueMain {
    // args[1] = verbose level | args[0]= boss host name
    public void theRealMain(String[] args) {
	if (args.length==2)
	    Agent.setVerboseLevel(Integer.parseInt(args[0]));

	Agent a = createAgent("Pong");	
	a.addSkill(new PongSkill2(a));
	a.connectToBoss("super@"+args[0]);
    }
} // PongImp2
