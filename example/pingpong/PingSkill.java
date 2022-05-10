import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class PingSkill extends MagiqueDefaultSkill {
    public PingSkill(Agent a) {
        super(a);
    }

    public void ping(Integer i) {
        //System.out.println("ping "+ i);
        perform("display", new Object[]{"ping " + i});
        // requ�te sur la comp�tence pong d'un agent "anonyme"
        perform("pong", new Integer(i.intValue() + 1));
    }
} // PingSkill
