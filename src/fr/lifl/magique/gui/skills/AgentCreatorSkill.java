/**
 * A skill that allows the agent to create another Agent
 *
 * @version 1.0 1/4/00
 * @author Fabien Niquet
 */

 
package fr.lifl.magique.gui.skills;
import java.util.*;
import java.lang.*;
import java.lang.reflect.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.platform.*;



public class AgentCreatorSkill extends DefaultSkill {

    public AgentCreatorSkill(AtomicAgent agent) {
	super(agent);
    }
    

    /**
      * Constructs another Agent
      * @param name the name of the new Agent
      * @param classe the class of the new Agent
      * @param path the path for the class for the new Agent
      * @param errorAgent the name of the ConsoleAgent
      */
  public String createAgent (String name, String classe, String path, String errorAgent) {  
      connectTo(errorAgent);
        try {	    
	    Platform platform= this.getMyAgent().getPlatform();
	    AtomicAgent agent = platform.createAgent(classe,name);
	    return agent.getName();
	} catch(Throwable t) {
	    perform(errorAgent,"haveAnError",new String("Error: Unable to create "+name+": Class "+classe+" not found !"));
	    
	    return "";
	}
  }

    /**
      * Constructs another Agent
      * @param name the name of the new Agent
      * @param classe the class of the new Agent
      * @apram args arguyments for the constructor
      * @param path the path for the class for the new Agent
      * @param errorAgent the name of the ConsoleAgent
      */
  public String createAgent (String name, String classe, Object[] args, String path, String errorAgent) {  
      connectTo(errorAgent);
        try {	    
	    Platform platform= this.getMyAgent().getPlatform();
	    AtomicAgent agent = platform.createAgent(classe,name,args);
	    return agent.getName();
	} catch(Throwable t) {
	    perform(errorAgent,"haveAnError",new String("Error: Unable to create "+name+": Class "+classe+" not found !"));
	    
	    return "";
	}
  }

}

