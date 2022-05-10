/**
 * ConsoleAgent.java
 * <p>
 * <p>
 * Created: Fri Nov 05 09:59:13 1999
 *
 * @author fabien Niquet
 * @version
 */
package fr.lifl.magique.gui.execute;

import fr.lifl.magique.gui.descriptor.Initialiser;
import fr.lifl.magique.gui.descriptor.LinkToDaemon;
import fr.lifl.magique.gui.descriptor.SkillDescriptor;
import fr.lifl.magique.gui.draw.GraphicAgent;
import fr.lifl.magique.gui.file.ConfigReader;
import fr.lifl.magique.platform.Platform;

import javax.swing.*;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A thread that execute a configuration
 *
 * @version 1.0 1/4/00
 * @author Fabien Niquet
 */

class CreateAgentThread extends Thread {
    private final ConfigReader reader;
    private final Hashtable computers;
    private final Deployement deployer;
    private final Configuration conf;
    private final ConsoleAgent agent;

    public CreateAgentThread(ConsoleAgent agent, ConfigReader reader, Hashtable computers, Deployement deployer, Configuration conf) {
        this.reader = reader;
        this.agent = agent;
        this.computers = computers;
        this.deployer = deployer;
        this.conf = conf;
    }

    public void run() {

        Vector agents = reader.getAgents();
        Platform platform = this.agent.getPlatform();
        for (Enumeration e = agents.elements(); e.hasMoreElements(); ) { //verify computers
            GraphicAgent graph_agent = (GraphicAgent) e.nextElement();
            String computer = this.getComputer(graph_agent.getComputer(), computers);

            Boolean res = platform.ping(computer);
            if (!res.booleanValue()) {
                JOptionPane.showMessageDialog(null, "An error has occured when trying to contact " + computer, "Error", JOptionPane.ERROR_MESSAGE);
                deployer.kill(conf.getName());
                return;
            }
            this.agent.haveAnError("Contacting " + computer + " => OK");
        }
        for (Enumeration e = agents.elements(); e.hasMoreElements(); ) {
            GraphicAgent graph_agent = (GraphicAgent) e.nextElement();
            String computer = this.getComputer(graph_agent.getComputer(), computers);
            try {
                Object[] params = new Object[4];
                params[0] = graph_agent.getName();
                params[1] = graph_agent.getClasse();
                params[2] = graph_agent.getClassPath().replace(System.getProperty("file.separator").charAt(0), '.');
                params[3] = this.agent.getName();
                try {
                    this.agent.connectTo("AgentCreator@" + computer);
                } catch (Throwable t) {
                }

                String res = (String) this.agent.askNow("AgentCreator@" + computer, "createAgent", params);
                if (res == "") {
                    JOptionPane.showMessageDialog(null, "An error has occured when trying to create " + graph_agent.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                    deployer.kill(conf.getName());
                    return;
                }
                deployer.addError("L'agent cr�� est:" + res);
                try {
                    this.agent.connectTo(res);
                } catch (Throwable t) {
                }
                //		    graph_agent.setComputer(Name.noShortName(res));
                //Gestion des INITS
                for (Enumeration inits = graph_agent.getIninialisersObject(); inits.hasMoreElements(); ) {
                    Initialiser initialiser = (Initialiser) inits.nextElement();
                    if (!initialiser.getArgument().equals("")) {
                        Object[] args = new Object[1];
                        args[0] = initialiser.getObjectArgument();
                        String initname = initialiser.getFunctionName();
                        if (args[0] == null) {
                            deployer.addError("Error while trying to init " + graph_agent.getName() + "@" + computer + ":" + initname);
                        } else {
                            this.agent.askNow(graph_agent.getName() + "@" + computer, initname, args);
                        }
                    }
                }
            } catch (Throwable t) {
                JOptionPane.showMessageDialog(null, "An error has occured when trying to contact " + computer,
                        "Error", JOptionPane.ERROR_MESSAGE);

                deployer.kill(conf.getName());
            }
        }

        for (Enumeration e = agents.elements(); e.hasMoreElements(); ) {
            GraphicAgent graph_agent = (GraphicAgent) e.nextElement();
            String computer = this.getComputer(graph_agent.getComputer(), computers);
            //    this.agent.perform(graph_agent.getName()+"@"+computer,"start");

            Vector skills = graph_agent.getSkills();
            //AJOUTS DES COMPETENCES !!!
            Object[] param2 = new Object[1];

            param2[0] = this.agent.getName();
            //      		this.agent.perform(graph_agent.getName()+"@"+computer,"addASkill","fr.lifl.magique.skill.system.ConsoleSkill",Boolean.TRUE,param2);
            this.agent.askNow(graph_agent.getName() + "@" + computer, "addASkill", "fr.lifl.magique.skill.system.ConsoleSkill", Boolean.TRUE, param2);
            //	this.agent.perform(graph_agent.getName()+"@"+computer,"addASkill","fr.lifl.magique.skill.system.KillSkill",Boolean.TRUE);
            for (Enumeration ee = skills.elements(); ee.hasMoreElements(); ) {
                SkillDescriptor skill = (SkillDescriptor) ee.nextElement();
                Vector skillargs = skill.getArguments();
                if (skillargs == null) {
                    this.agent.haveAnError("Error while trying to instance the skill " + skill.getClasse() + " to " + graph_agent.getName());
                } else {
                    if (skillargs.size() == 0) {
                        String from = agent.getName(); //Platform.PLATFORMMAGIQUEAGENTNAME+"@"+this.agent.getPlatform().getName();
                        this.agent.perform(graph_agent.getName() + "@" + computer, "learnSkill", skill.getClasse(), from);
                        // this.agent.perform(graph_agent.getName()+"@"+computer,"addASkill",skill.getClasse());
                    } else {
                        Object[] params = null;
                        if (skill.isDefaultSkill()) { //defaultSkill
                            if (skillargs.size() - 1 == 0) {
                                String from = agent.getName(); //Platform.PLATFORMMAGIQUEAGENTNAME+"@"+this.agent.getPlatform().getName();
                                this.agent.perform(graph_agent.getName() + "@" + computer, "learnSkill", skill.getClasse(), from, Boolean.TRUE);
                                //				    this.agent.perform(graph_agent.getName()+"@"+computer,"addASkill",skill.getClasse(),new Boolean("true"));
                            } else {

                                params = new Object[skillargs.size() - 1];
                                for (int i = 1; i < skillargs.size(); i++) {

                                    params[i - 1] = skillargs.elementAt(i);
                                }
                                String from = agent.getName(); //Platform.PLATFORMMAGIQUEAGENTNAME+"@"+this.agent.getPlatform().getName();
                                this.agent.perform(graph_agent.getName() + "@" + computer, "learnSkill", skill.getClasse(), from, new Boolean("true"), params);
                                //				this.agent.perform(graph_agent.getName()+"@"+computer,"addASkill",skill.getClasse(),new Boolean("true"),params);
                            }
                        } else {               //pas defaultSkill !!!
                            params = new Object[skillargs.size() - 1];
                            for (int i = 0; i < skillargs.size(); i++) {
                                params[i] = skillargs.elementAt(i);
                            }
                            String from = agent.getName(); //Platform.PLATFORMMAGIQUEAGENTNAME+"@"+this.agent.getPlatform().getName();
                            this.agent.perform(graph_agent.getName() + "@" + computer, "learnSkill", skill.getClasse(), from, new Boolean("false"), params);
                            //			   this.agent.perform(graph_agent.getName()+"@"+computer,"addASkill",skill.getClasse(),new Boolean("false"),params);
                        }
                    }

                }
            }

            //AJOUT DES LIENS !!!
            if (graph_agent.GetParent() != null) {
                Object[] param = new Object[1];
                param[0] = graph_agent.GetParent().getName() + "@" + getComputer(((GraphicAgent) graph_agent.GetParent()).getComputer(), computers);
                this.agent.perform(graph_agent.getName() + "@" + computer, "connectToBoss", param);
            }
            for (Enumeration ee = graph_agent.getSpecialParent().elements(); ee.hasMoreElements(); ) {
                GraphicAgent direct_agent = (GraphicAgent) ee.nextElement();
                Object[] param = new Object[1];
                param[0] = direct_agent.getName() + "@" + getComputer(direct_agent.getComputer(), computers);
                //	       param[1]=this.getName();
                this.agent.perform(graph_agent.getName() + "@" + computer, "connectTo", param);
                this.agent.haveAnError("Adding connection between " + graph_agent.getName() + " and " + direct_agent.getName());
            }
        }
	    /*	for (Enumeration e=agents.elements();e.hasMoreElements();) {
	    GraphicAgent graph_agent=(GraphicAgent) e.nextElement();	 		     
	    String computer=this.getComputer(graph_agent.getComputer(),computers);
	    this.agent.perform(graph_agent.getName()+"@"+computer,"start");
	    this.agent.haveAnError("starting "+ graph_agent.getName());
	    }*/

        conf.setDeployed();
    }

    public void haveAnError(String message) {
        deployer.addError(message);
    }

    private String getComputer(String computername, Hashtable computers) {
        String computer = computername;
        if (computername.equals("127.0.0.1")) {
            try {
                computer = InetAddress.getLocalHost().getHostAddress();
            } catch (Throwable t) {
            }
        }

        String res = null;
        LinkToDaemon link = (LinkToDaemon) computers.get(computer);
        if (link == null)
            res = computer + ":4444";
        else
            res = link.getName() + ":" + link.getPort();
        return res;
    }
}

