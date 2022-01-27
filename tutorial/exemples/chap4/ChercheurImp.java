package chap4;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class ChercheurImp extends AbstractMagiqueMain{   
   public void theRealMain(String[] args) {
       if (args.length!=3) {
         System.out.println("ChercheurImp takes 'nom' and "+
                            "'superviseur host:port' and "+
                            "'verbose level' as arguments");
         System.exit(1);
       }
       Agent.setVerboseLevel(Integer.parseInt(args[2]));
       Agent a = createAgent(args[0]);

       a.addSkill(new ChercheDiv());
       a.connectToBoss("collecteur@"+args[1]);
   }   
} // ChercheurImp
