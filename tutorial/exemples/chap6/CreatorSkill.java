package chap6;
import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;
import fr.lifl.magique.skill.*;
public class CreatorSkill extends MagiqueDefaultSkill {

    public CreatorSkill(Agent a){ super(a); }
    
    public void create() {
      Platform myPlatform = getPlatform();
      
      Agent ping = myPlatform.createAgent("ping");
      ping.addSkill(new chap6.PingSkill(ping));
      myPlatform.addAgent(ping);
      ping.connectToBoss(getName());
      
      Agent pong = myPlatform.createAgent("pong");
      pong.addSkill(new chap6.PongSkill(pong));
      myPlatform.addAgent(pong);
      pong.connectToBoss(getName());
      
      perform("ping");
    }    
} // CreatorSkill
