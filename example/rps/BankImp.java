
/**
 * BankImp.java
 *
 *
 * Created: Fri Apr 30 11:01:15 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
import fr.lifl.magique.*;

public class BankImp extends AbstractMagiqueMain {

    public void theRealMain (String[] args) {
	if (args.length==1)
	    Agent.setVerboseLevel(new Integer(args[0]).intValue());
	
	Agent bank = createAgent("bank");
	bank.addSkill(new BankSkill(bank));
       	bank.connectToBoss("super@HOSTNAME:4444");
	bank.start();
    }

} // BankImp
