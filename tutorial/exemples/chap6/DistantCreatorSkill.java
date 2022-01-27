package chap6;
import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;
import fr.lifl.magique.skill.*;
public class DistantCreatorSkill extends MagiqueDefaultSkill {

    public DistantCreatorSkill(Agent a){ super(a); }
    
    public void distantCreate(String pingPlatform, 
                              String pongPlatform) {

	String PMAN = Platform.PLATFORMMAGIQUEAGENTNAME;
     String pingPlatformAgent = PMAN+"@"+pingPlatform;
     connectTo(pingPlatformAgent);
     String pingName = (String) askNow(pingPlatformAgent,
                                       "createAgent","ping");
     perform(pingPlatformAgent,"connectAgentToBoss",
                               pingName,getName());
     perform(pingName,"learnSkill", "chap6.PingSkill", 
                                    getName(), Boolean.TRUE);
     
     String pongPlatformAgent = PMAN+"@"+pongPlatform;
     connectTo(pongPlatformAgent);
     String pongName = (String) askNow(pongPlatformAgent,
                                       "createAgent","pong");
     perform(pongPlatformAgent,"connectAgentToBoss",
                               pongName,getName());     
     perform(pongName,"learnSkill", "chap6.PongSkill", 
                      getName(), Boolean.TRUE);    
 
     perform("ping");
    }   
} // DistantCreatorSkill
