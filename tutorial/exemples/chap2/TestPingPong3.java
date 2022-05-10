package chap2;
import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class TestPingPong3 extends AbstractMagiqueMain {
  public void theRealMain(String args[])   {
  
     // création des agents par la plate-forme
     Agent superPing = createAgent("superPing");
     Agent pong = createAgent("agentPong");
     
     // raccordement à la plateforme
     superPing.addSkill(new chap2.PingSkill2(superPing));
     superPing.setAction(new chap2.SuperPingAction(superPing));
     pong.addSkill(new chap2.PongSkill2(pong));
     
     // connexion de pong à superPing en tant que superviseur
     pong.connectToBoss("superPing"); 
     
     // lancement du jeu
     superPing.start();
  }
} // TestPingPong3

