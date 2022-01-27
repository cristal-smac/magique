package chap4;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class PongSynchroSkill extends MagiqueDefaultSkill { // c'est un service 

    public PongSynchroSkill(Agent a){ super(a); }

    public void pong(Integer val) {
	// asknow returns an object and then return value must be cast
	boolean answer = ((Boolean) askNow("arret")).booleanValue();

        if (!answer) {
           System.out.println("Pong"+val);
           perform("incr");
	   // val must be converted into an Integer since args must be object
           perform("ping",new Integer(val.intValue()+1));
        }
    }
}
