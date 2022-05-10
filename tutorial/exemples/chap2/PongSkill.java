package chap2;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class PongSkill extends MagiqueDefaultSkill {
    public PongSkill(Agent a){ super(a); }
    
    public void pong() {
       System.out.println("pong");
       // requête sur la compétence ping de "agentPing"
       perform("agentPing","ping");
    }  
} // PongSkill
