package fr.lifl.magique.gui.execute;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import bsh.*;
import java.net.*;
import fr.lifl.magique.gui.file.*;
import fr.lifl.magique.gui.util.*;
import fr.lifl.magique.platform.*;
import fr.lifl.magique.*;
import fr.lifl.magique.gui.draw.*;import fr.lifl.magique.gui.descriptor.*;

import java.lang.reflect.Method;

public class Deployement {
    private Hashtable HConfig;
    private JComponent desktop;
    private JMenu active;   
    private Platform platform;
    private ConsoleAgent console;
    FluxFrame error;
    Interpreter interprete;


    /**
     * Creates the Deployement Interface 
     *
     * @param active The JMenu used to launch a configuration
     * @param desktop the JComponent that draws the Execution
     * @param platform the platform with the ConsoleAgent
     * @param error the FluxFrame where the message are displayed
     * @param interprete The Beanshell Interpreter
     */
    public Deployement (JMenu active, JComponent desktop,Platform platform,FluxFrame error,Interpreter interprete) {
	HConfig=new Hashtable();
	this.desktop=desktop;
	this.active=active; 	
	this.platform=platform;
	this.error=error;
	this.interprete=interprete;
//  	try {
//  	    Method m  = fr.lifl.magique.util.ClassUtil.getMethod(this.platform.getClass(),
//  								 "createAgent",
//  								 new String[]{"java.lang.String",
//  									      "java.lang.String",
//  									      "[Ljava.lang.Object;"});
//  	    System.out.println("******************** "+m);
	    
//  	    console = (ConsoleAgent) m.invoke(this.platform, 
//  					      new Object[]{"fr.lifl.magique.gui.execute.ConsoleAgent",
//  							   "ConsoleAgent",
//  							   new Object[]{this}});
//  	}
//  	catch(Exception e) { e.printStackTrace(); }
	
	console = (ConsoleAgent) platform.createAgent("fr.lifl.magique.gui.execute.ConsoleAgent","ConsoleAgent",new Object[]{this});  
     	//platform.addAgent(console);	
	console.start();	

    }
    public void addError(String text) {
	error.addText(text);
    }
   /**
     * Execute a new Configuration
     *
     * @param input The BufferedReader on a configuration 
     * @param confname The name of the configuration to launch
     * @param computers an hashtable that contains LinkToDaemon that allows to find the port of a computer.
    
     */


    public boolean newConfig(BufferedReader input,String confName,Hashtable computers)throws IOException {
	boolean continu=true;
	ConfigReader reader=new ConfigReader();
	try {
	    reader.load(input);
	}catch (Throwable t) {
	    JOptionPane.showMessageDialog(null, "An error has occured during the connexion",
					  "Error", JOptionPane.ERROR_MESSAGE);
	    return false;
	}	 
	try {	  
	reader.isReadyToLaunch();
	}catch(IOException err) {throw err;}
  
	String name= getNewConfName(confName);
	Configuration configuration = new Configuration(name,
					  reader.getAgents(),
					  this.getActive(),
					  this.getDesktop());
	
	synchronized (HConfig) {
	    HConfig.put(name,configuration);   
	} 	
 	console.createAllAgents(reader,computers,configuration);	
	Vector agents=reader.getAgents();
	for (Enumeration e=agents.elements();e.hasMoreElements();) {
	    GraphicAgent agent=(GraphicAgent) e.nextElement();
	    String longname=agent.getName();
	    String computer=this.getComputer(agent.getComputer(),computers);
	    String shortName=new String(longname);
	    int index=longname.lastIndexOf('-');
	    if (index!=-1)
		shortName=shortName.substring(index+1);
	    if (!shortName.equals("agent")) {
//	CHANGE 2004 setVariable remplacé par set
			try {
				interprete.set(shortName,longname+"@"+computer);
			} catch (EvalError err) {err.printStackTrace(); }
	}
	}






	return true;
    }  

    public void addMessageTo (String to,String message) {
	for (Enumeration e=HConfig.keys();e.hasMoreElements();) {
	    String conf=(String) e.nextElement();
	    if (to.startsWith(conf)) 
		((Configuration) HConfig.get(conf)).traiteMessage(to,message); 
	}
    } 
    public Agent getMyAgent () {
	return console;
    }
	    
    public String getNewConfName (String confName) {
	String name=null;
	if (confName.equals("New configuration*"))
	    name="NoName";
	else {
	    name=confName.substring(confName.lastIndexOf(System.getProperty("file.separator"))+1);          
	    }
	String namesauf=new String(name);
	int compteur=2;
	synchronized(HConfig) {
	    while (HConfig.containsKey(name)) {
		name=new String(namesauf+compteur);
		compteur++;
	    }
	}
	return name;
    }
public JComponent getDesktop() { return desktop; }
    public JMenu getActive() { return active; }
    
    public void reset() {
	synchronized(HConfig) {
	    for (Enumeration e=HConfig.elements();e.hasMoreElements();) {
                Configuration conf=(Configuration) e.nextElement();
	        console.killConf(conf);
	    }
	    HConfig.clear();
	}
    }
    public Vector getReadyConfigs () {
	Vector res=new Vector();
	synchronized (HConfig) {
	    for (Enumeration e=HConfig.elements();e.hasMoreElements();) {
		Configuration Conf=(Configuration) e.nextElement();
                if (Conf.isDeployed()) res.addElement(Conf.getName());
	    }
	}
	return res;
    }
    public Enumeration getConfigs () {
       // Vector res=new Vector();
        return HConfig.keys();
    
    }
    public Vector getAgentsIn (String name) {
	synchronized(HConfig) {
	    Configuration conf=(Configuration) HConfig.get(name);
	    if (conf!=null)
		return conf.getStatusFrames() ;
	}
	return null;
    }
    public void reverseAgentVisible (String name) {
	synchronized(HConfig) {
	    for (Enumeration e=HConfig.elements();e.hasMoreElements();) 
		((Configuration) e.nextElement()).reverseAgentVisible(name);
	}
    }
    public void setConfigVisible(String name,boolean show) {  
	synchronized (HConfig) {
	    Configuration conf=(Configuration)HConfig.get(name);
	    if (conf!=null) {     
		conf.setVisible(show);
	    }
     }
    }
    public void showAllConfiguration(boolean show) {
	synchronized (HConfig) {
	    for (Enumeration e=HConfig.elements();e.hasMoreElements();) {
		Configuration conf=(Configuration)e.nextElement();
		conf.setVisible(show);
	    }
	}
    }
    public void setAllVisible(String name,boolean show) {
	synchronized (HConfig) {
	    Configuration conf=(Configuration)HConfig.get(name);
	    if (conf!=null) {     
		conf.setHidden(!show);
	    }
	}
    }
    
    public void kill(String name) {
	synchronized (HConfig) {
	    Configuration conf=(Configuration)HConfig.get(name);
	    if (conf!=null) { 
		console.killConf(conf);
		HConfig.remove(name);
	    }
	}
    }
    
    public void execute(String name) {
	synchronized (HConfig) {
	   Configuration conf=(Configuration)HConfig.get(name);
	    if (conf!=null)  
		conf.launch();
	}
	if (getReadyConfigs().isEmpty())  active.setEnabled(false);
    }


    private String getComputer(String computername,Hashtable computers) {
	String computer=computername;
	if (computername.equals("127.0.0.1")) {
	    try {
		computer=InetAddress.getLocalHost().getHostAddress();
	    }catch (Throwable t) {}
	}

	String res=null;
	LinkToDaemon link=(LinkToDaemon) computers.get(computer);	
	if (link==null)
	    res=computer+":4444";
	else
	    res=link.getName()+":"+link.getPort();
	return res;
    }
} 


