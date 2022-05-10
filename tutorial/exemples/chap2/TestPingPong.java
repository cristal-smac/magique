package chap2;
import fr.lifl.magique.*;

public class TestPingPong extends AbstractMagiqueMain {
    public void theRealMain(String args[]) {
        // création des agents par la plate-forme
        Agent ping = createAgent("agentPing");
        Agent pong = createAgent("agentPong");
    
        // acquisition de leur compétence
        ping.addSkill(new chap2.PingSkill(ping));
        pong.addSkill(new chap2.PongSkill(pong));

        // connexion des agents (automatiquement mutuelle)
        ping.connectTo("agentPong"); 

        // lancement du jeu
        ping.perform("ping");
    }

} // TestPingPong
