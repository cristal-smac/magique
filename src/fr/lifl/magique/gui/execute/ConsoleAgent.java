/**
 * ConsoleAgent.java
 * <p>
 * <p>
 * Created: Fri Nov 05 09:59:13 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.gui.execute;

import fr.lifl.magique.Agent;
import fr.lifl.magique.gui.draw.GraphicAgent;
import fr.lifl.magique.gui.file.ConfigReader;
import fr.lifl.magique.gui.skills.ClassLoaderSkill;
import fr.lifl.magique.skill.system.KillSkill;
import fr.lifl.magique.util.Name;

import java.util.Enumeration;
import java.util.Hashtable;


public class ConsoleAgent extends Agent {
    Deployement deployer;
    String confName;

    /**
     * @param name le nom de la configuration (et de l'agent)
     * @param deployer l'objet Deployement
     */
    public ConsoleAgent(String name,
                        Deployement deployer) {
        super(name);
        this.confName = name;
        this.deployer = deployer;
        try {
            addSkill(new KillSkill(this));
        } catch (Throwable t) {
        }
        try {
            addSkill(new ClassLoaderSkill(this));
        } catch (Throwable t) {
        }
    }

    public String getConfName() {
        return confName;
    }


    public void action() {
    }

    /**
     * Creates all agents
     *
     * @param reader the ConfigReader of the configuration to launch
     * @param computers an Hashtable that contains LinkToDaemon
     * @param conf The Configuration
     */


    /** create all the  agent on its host */
    public void createAllAgents(ConfigReader reader, Hashtable computers, Configuration conf) {
        new CreateAgentThread(this, reader, computers, deployer, conf).start();
    }


    public void haveAMessage(String agentName, String message) {
        deployer.addMessageTo(Name.getShortName(agentName), message);
    }

    public void haveAnError(String message) {
        deployer.addError(message);
    }


    /**
     * Kill a Configuration
     *
     * @param conftokill the configuration to kill

     */

    public void killConf(Configuration conftokill) {
        for (Enumeration e = conftokill.getRoots(); e.hasMoreElements(); ) {
            GraphicAgent agentroot = (GraphicAgent) e.nextElement();
            //	String roottokill=agentroot.getExecutionName();
            String roottokill = agentroot.getName();
            try {
                if (!roottokill.equals("")) {
                    this.perform(roottokill, "killHierarchy");
                }
            } catch (Throwable t) {
                System.out.println(roottokill + "refuse de mourir !");
            }
        }
        conftokill.kill();
    }

}
