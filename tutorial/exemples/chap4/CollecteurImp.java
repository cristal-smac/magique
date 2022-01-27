package chap4;
import fr.lifl.magique.*;

public class CollecteurImp extends AbstractMagiqueMain {
   public  void theRealMain(String[] args) {
     if (args.length == 2) {
       Agent.setVerboseLevel(Integer.parseInt(args[1]));
     }
     
     Agent a = createAgent("collecteur");
     
     a.addSkill(new TestPrimal(a));
     
     // on attend que tout le monde soit arrivé !
     System.out.println("\n\nTapez <Entree> quand tous "+
                        "les chercheurs seront lances");
     try { System.in.read(); } 
         catch(java.io.IOException e) {}
     System.out.println("ok, c'est parti !!");
     
     // test d'un nombre
     Integer nb;
     if (args.length >=1) {
         nb = new Integer(args[0]);
     } else {
         nb = new Integer(217483647);
     }
     Boolean rep = (Boolean) a.askNow("prem",nb);
     System.out.println(nb + " Premier ? : " + rep);
   }    
} // CollecteurImp
