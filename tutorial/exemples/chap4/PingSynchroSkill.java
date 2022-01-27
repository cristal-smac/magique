package chap4;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class PingSynchroSkill extends MagiqueDefaultSkill {

   public PingSynchroSkill(Agent a){ super(a); }

   public void ping(Integer val) {
     // asknow returns an object then cast is needed
     boolean answer = ((Boolean) askNow("arret")).booleanValue();

     if (!answer) {
        System.out.println("Ping"+val);
        perform("incr");
        // args must be object => Integer and not int
        perform("pong",new Integer(val.intValue()+1));
     }
   }
}
