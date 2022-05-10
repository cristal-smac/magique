/**
 * AgentPool.java
 * <p>
 * <p>
 * Created: Mon Oct 29 09:36:47 2001
 *
 * @author <a href="mailto: "Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;

import fr.lifl.magique.Agent;
import fr.lifl.magique.Request;
import fr.lifl.magique.platform.Platform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/** manages a pool of agent for a given service, these agents will be
 * sollicited by their boss one by one to perform a given service
 * their are specialized for. Thus boss burden is lighten for this
 * skill.  */

public class AgentPool {

    private final List pool = new ArrayList();
    private Iterator browser;
    private final Agent boss;
    private final String[] hosts;

    public AgentPool(Agent boss, String skillName, String serviceName, int sizeOfPool, String[] hosts) {
        this.boss = boss;
        this.hosts = hosts;
        createPool(sizeOfPool, skillName, serviceName);
    }

    private void createPool(int sizeOfPool, String skillName, String serviceName) {
        int hostIndex = 0;

        for (int i = 0; i < sizeOfPool; i++) {
            System.out.println("agent " + i + " of pool for " + skillName + "/" + serviceName + " is created");

            String myPlatformAgent = Platform.PLATFORMMAGIQUEAGENTNAME + "@" + Name.noShortName(boss.getName());
            String aName = Name.getShortName(boss.getName()) + "-sub4skill-" + new StringTokenizer(skillName, "(").nextToken() + "-" + i;
            String a = (String) boss.askNow(myPlatformAgent, "createDistantAgentAndConnectToBoss", aName, hosts[(hostIndex++) % hosts.length], boss.getName());

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }

            boss.perform(a, "addASkill", serviceName, Boolean.TRUE);
            System.out.println("learn " + serviceName + " to " + a + " -- ");
            System.out.println((String) boss.askNow(a, "getMyBoss"));

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
        boss.send(nextAgentName(), r);
    }
}// AgentPool
