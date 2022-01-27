import fr.lifl.magique.*;

public class SuperImp  extends AbstractMagiqueMain{
    public void theRealMain (String[] args) {
      if (args.length == 1) {
         Agent.setVerboseLevel(Integer.parseInt(args[0]));
      }  	
      Agent superviseur = createAgent("super"); 
    }
} // SuperImp
