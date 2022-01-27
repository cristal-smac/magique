package chap6;

import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class SuperImp2  extends AbstractMagiqueMain {

   public void theRealMain(String[] args) {
     if (args.length==1)
       Agent.setVerboseLevel(new Integer(args[0]).intValue());
   
     Agent sup = createAgent("super");
     sup.perform("ping",new UnObject(new Ball("la balle")), 
                 new UnObject(new AnotherBall("autre balle")));
   }
} // SuperImp2
