package chap6;
import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class PingImp extends AbstractMagiqueMain { 

    // args[0] = verboseLevel **  args[1] = super Host
    public void theRealMain(String[] args) {
	
        Agent.setVerboseLevel(Integer.parseInt(args[0]));
        Agent a = createAgent("ping");
        a.connectToBoss("super@"+args[1]);
    }

}
