package chap2;
import fr.lifl.magique.*;

public class BasicAgentImp extends AbstractMagiqueMain {
   public void theRealMain(String args[])  {  
      Agent firstAgent = createAgent("monAgent");
      firstAgent.setAction(new MyAgentAction(firstAgent));
      firstAgent.start();
   }

   // si pas d'utilisation du main de fr.lifl.magique.Start:
   public static void main(String args[])  {  
      fr.lifl.magique.Start.go("chap2.BasicAgentImp");
   }

}
