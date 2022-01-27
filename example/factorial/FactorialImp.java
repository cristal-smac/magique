/**
 * FactorialImp.java
 *
 *
 * Created: Mon Oct 26 14:12:17 1998
 *
 * @author Jean-Christophe Routier
 * @version
 */
import fr.lifl.magique.*;

public class FactorialImp extends AbstractMagiqueMain {
    
    public void theRealMain (String[] args)  {
	Agent.setVerboseLevel(Integer.parseInt(args[0]));

	Agent agent = createAgent("fact");
	agent.addSkill(new FactorialSkill(agent));
    }

} // FactorialAgent
