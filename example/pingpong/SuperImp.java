import fr.lifl.magique.*;

public class SuperImp extends AbstractMagiqueMain {
			      //args[0] = verbose level
	private Agent a;
    public  void theRealMain(String[] args) {
        if (args.length==1)
            Agent.setVerboseLevel(Integer.parseInt(args[0]));

        a = createAgent("super");
		a.perform("ping", 0);
    }
}
