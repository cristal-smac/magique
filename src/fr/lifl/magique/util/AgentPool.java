
/**
 * AgentPool.java
 *
 *
 * Created: Mon Oct 29 09:36:47 2001
 *
 * @author <a href="mailto: "Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;

import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

import java.util.*;

/** manages a pool of agent for a given service, these agents will be
 * sollicited by their boss one by one to perform a given service
 * their are specialized for. Thus boss burden is lighten for this
 * skill.  */

public class AgentPool {

    private List pool = new ArrayList();
    private Iterator browser;
    private Agent boss;
    private String[] hosts;

    public AgentPool (Agent boss, String skillName, String serviceName, int sizeOfPool, String[] hosts){
	this.boss = boss;
	this.hosts = hosts;
	createPool(sizeOfPool,skillName,serviceName);
    }

    private void createPool(int sizeOfPool, String skillName, String serviceName) {
	int hostIndex = 0;
	
	//	Platform platform = boss.getPlatform();
	for(int i = 0; i< sizeOfPool;i++) {
	    System.out.println("agent "+i+" of pool for "+skillName+"/"+serviceName+" is created");

	    String myPlatformAgent = Platform.PLATFORMMAGIQUEAGENTNAME+"@"+Name.noShortName(boss.getName());
	    String aName = Name.getShortName(boss.getName())+"-sub4skill-"+new StringTokenizer(skillName,"(").nextToken()+"-"+i;
	    String a = (String) boss.askNow(myPlatformAgent,"createDistantAgentAndConnectToBoss",aName,hosts[(hostIndex++)%hosts.length], boss.getName());

	    try {
		Thread.sleep(1000);
	    }
	    catch(Exception e) {}

	    boss.perform(a,"addASkill",serviceName,Boolean.TRUE);
	    System.out.println("learn "+serviceName+" to "+a+" -- ");
	    System.out.println((String) boss.askNow(a,"getMyBoss"));
	    
	    
//  	    Agent a = platform.createAgent(Name.getShortName(boss.getName())+"-sub4skill-"+skillName+"-"+i);
//  	    a.connectToBoss(boss.getName());
//  	    a.addSkill(serviceName, new Object[]{a});

	    pool.add(a);
	}
	browser = pool.iterator();
    }

    private String nextAgentName() {
	if (!browser.hasNext()) {
	    browser = pool.iterator();
	}
	return ((String) browser.next());
    }

    public void processRequest(Request r) {
	boss.send(nextAgentName(),r);
    }
}// AgentPool
