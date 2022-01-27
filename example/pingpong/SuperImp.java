import fr.lifl.magique.*;

public class SuperImp extends AbstractMagiqueMain {
			      //args[0] = verbose level
    public  void theRealMain(String[] args) {
        if (args.length==1)
            Agent.setVerboseLevel(Integer.parseInt(args[0]));

        Agent a = createAgent("super");
	//a.addSkill(new PongSkill(a));
        a.perform("ping", new Integer(0));
    }
}
