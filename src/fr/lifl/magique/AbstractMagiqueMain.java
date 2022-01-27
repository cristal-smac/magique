/**
 * MagiqueMain.java
 *
 *
 * Created: Tue Feb 20 09:59:53 2001
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique; 
import fr.lifl.magique.Agent;
import fr.lifl.magique.platform.Platform;


public abstract class AbstractMagiqueMain  {
    
    protected Platform platform;

    public void setPlatform(Platform p) {
	this.platform = p;
    }

    /** returns the platform of this JVM
     * @return the platform of this JVM
     */
    public Platform getPlatform() {
	return platform;
    }

    public AtomicAgent createAgent(String agentClassName, String agentName, Object[] args) {
	return platform.createAgent(agentClassName, agentName, args);
    }

    public AtomicAgent createAgent(String agentClassName, String agentName) {
	return platform.createAgent(agentClassName, agentName);
    }

    public Agent createAgent(String agentName) {
	return platform.createAgent(agentName);
    }


    public abstract void theRealMain(String[] args);
    
} // MagiqueMain
