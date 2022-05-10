import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class PongSkill extends MagiqueDefaultSkill {
    public PongSkill(Agent a) {
        super(a);
    }

    public void pong(Integer i) {
        //      System.out.println("pong "+i);
        perform("display", new Object[]{"pong " + i});
        // requête sur la compétence ping d'un agent "anonyme"
        perform("ping", Integer.valueOf(i+1) );
    }
} // PongSkill
