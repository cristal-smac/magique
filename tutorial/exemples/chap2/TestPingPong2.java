package chap2;

import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class TestPingPong2 extends AbstractMagiqueMain {

    public void theRealMain(String args[])  {
        // création des agents par la plate-forme
        Agent sup = createAgent("superviseur");
        Agent ping = createAgent("agentPing");
        Agent pong = createAgent("agentPong");
    
        // raccordement à la plateforme
        ping.addSkill(new chap2.PingSkill(ping));
        pong.addSkill(new chap2.PongSkill(pong));

        // connexion des agents à sup en tant que superviseur
        ping.connectToBoss("superviseur"); 
        pong.connectToBoss("superviseur"); 

        // lancement du jeu
        sup.perform("ping"); 
        // ou ping.perform("ping") ou pong.perform("ping")
    }

} // TestPingPong2
