
/**
 * TestImp.java
 *
 *
 * Created: Fri Feb 04 21:50:00 2000
 *
 * @author Philippe MATHIEU
 * @version
 */
import fr.lifl.magique.*;

public class TestImp extends AbstractMagiqueMain {
    public void theRealMain(String[] args) {
	if (args.length==1)
	    Agent.setVerboseLevel(new Integer(args[0]).intValue());

	Agent sup = createAgent("sup");
	sup.addSkill(new CalculComplet(sup));

	Agent f = createAgent("f");
	f.addSkill(new CalculTrivial());
	f.connectToBoss("sup");

	Agent g = createAgent("g");
	g.addSkill(new CalculTrivial());
	g.connectToBoss("sup");

	sup.perform("complet");
    }
    
} // TestImp
