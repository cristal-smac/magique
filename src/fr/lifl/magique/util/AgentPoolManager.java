/**
 * AgentPoolManager.java
 * <p>
 * <p>
 * Created: Mon Oct 29 10:45:12 2001
 *
 * @author <a href="mailto: "Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;

import fr.lifl.magique.Agent;

import java.util.HashMap;

public class AgentPoolManager {

    private final HashMap poolMap = new HashMap();
    private final Agent myAgent;
    private String[] hosts = null;

    public AgentPoolManager(Agent myAgent) {
        this.myAgent = myAgent;
    }

    public AgentPoolManager(Agent myAgent, String[] hosts) {
        this.myAgent = myAgent;
        this.hosts = hosts;
    }


    public void setPoolHosts(String[] hosts) {
        this.hosts = hosts;
    }

    public void createNewPool(String skillName, String serviceName, int sizeOfPool) {
        if (hosts == null) {
            hosts = new String[]{myAgent.getPlatform().getName()};
        }
        AgentPool ap = new AgentPool(myAgent, skillName, serviceName, sizeOfPool, hosts);
        poolMap.put(skillName, ap);
    }

    public AgentPool getPool(String skillName) {
        return (AgentPool) poolMap.get(skillName);
    }

    public boolean hasAPool(String skillName) {
        return poolMap.containsKey(skillName);
    }
}// AgentPoolManager
