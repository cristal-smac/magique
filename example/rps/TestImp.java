import java.util.*;
import java.lang.*;

import fr.lifl.magique.*;

class TestImp extends AbstractMagiqueMain
{
    public void theRealMain(String args[])
    {
	if (args.length==1)
	    Agent.setVerboseLevel(new Integer(args[0]).intValue());

	Agent b = createAgent("bank");
	b.addSkill(new BankSkill(b));

	Agent p1 = createAgent("p1");
	p1.addSkill(new HumanPlayerSkill(p1));

	Agent p2 = createAgent("p2");
	p2.addSkill(new RandomPlayerSkill(p2));

	p1.connectToBoss("bank");
	p2.connectToBoss("bank");
	
	b.start();	
    }
}
