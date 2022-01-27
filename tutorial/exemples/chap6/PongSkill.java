package chap6;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class PongSkill extends MagiqueDefaultSkill {
    public PongSkill(Agent a){ super(a); }
    
    public void pong() {
        System.out.println("pong");
        perform("ping"); // requête sur la compétence ping d'un agent "anonyme"
    }  
} // PongSkill
