package chap2;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class PingSkill2 extends MagiqueDefaultSkill {
   public PingSkill2(Agent a){ super(a); }
    
   public void ping() {
     System.out.println("ping");
     // requête sur la compétence pong d'un agent "anonyme"
     perform("pong"); 
   }  
} // PingSkill
