package chap7;
import fr.lifl.magique.*;

public class Disconnect1 extends AbstractMagiqueMain {
    // args[0] = verbose level
    public void theRealMain(String[] args) {
      
      Agent.setVerboseLevel(Integer.parseInt(args[0]));
      
      Agent ag = createAgent("agent1");
      ag.addSkill(new chap7.UnSkill());
      System.out.println("\n \n");	
    }

}// Disconnect1
