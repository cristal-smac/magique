package chap4;
import fr.lifl.magique.*;

public class SuperImp extends AbstractMagiqueMain {
   public void theRealMain(String[] args) {
     if (args.length==1)
        Agent.setVerboseLevel(Integer.parseInt(args[0]));
     
     Agent a= createAgent("super");
     // args must be Object => 10 is converted into an Integer
     a.perform("ping",new Integer(10));    
   }
} 
