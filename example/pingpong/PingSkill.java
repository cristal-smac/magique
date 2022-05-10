import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class PingSkill extends MagiqueDefaultSkill {
    public PingSkill(Agent a) {
        super(a);
    }

    public void ping(Integer i) {
        //System.out.println("ping "+ i);
        perform("display", new Object[]{"ping " + i});
        // requête sur la compétence pong d'un agent "anonyme"
        perform("pong", Integer.valueOf(i+1) );
    }
} // PingSkill
