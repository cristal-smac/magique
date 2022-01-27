package chap7;
import fr.lifl.magique.*;

public class Disconnect2 extends AbstractMagiqueMain{

    // args[0] = verbose level  - args[1] = boss platform 
    public void theRealMain(String[] args) {

      Agent.setVerboseLevel(Integer.parseInt(args[0]));
      
      Agent ag = createAgent("agent2");	
      ag.connectToBoss("agent1@"+args[1]);
      ag.perform("tralala");
      pause(1);
      
      System.out.println("deconnection");	
      ag.perform("askForDisconnectionFromMyBoss");
      pause(2);
      
      System.out.println("reconnection");	
      ag.connectToBoss("agent1@"+args[1]);
      ag.perform("tralala");
      pause(3);
      
      System.out.println("mort de l'agent");	
      ag.perform("disconnectAndDie");
      pause(4);
      
      System.out.println("re``creation'' de l'agent");	
      ag = createAgent("agent2");	
      ag.connectToBoss("agent1@"+args[1]);
      
      ag.perform("tralala");
      pause(5);
      
      System.out.println("arret plate-forme");
      
      ag.getPlatform().stop();
    }

    private void pause(int index) {
      	try {
      	    System.out.println("dodo "+index);
      	    Thread.sleep(1000);
      	    System.out.println("reveil "+index);
      	} catch (Exception e) { e.printStackTrace(); }

    }
}// Disconnect2
