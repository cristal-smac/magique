package chap2;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class PingSkill extends MagiqueDefaultSkill {
    public PingSkill(Agent a){ super(a); }
    
    public void ping() {
       System.out.println("ping");
       // requête sur la compétence pong de "agentPong"
       perform("agentPong","pong");
    }  
} // PingSkill
