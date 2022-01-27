package fr.lifl.magique.gui.skills;
import java.util.*;
import java.lang.*;
import java.lang.reflect.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.platform.*;


public class AgentCreator extends AtomicAgent {
    public AgentCreator() {
	super("AgentCreator");
	try {
	    addSkill(new ClassLoaderSkill(this));
	} catch(Throwable t){}
	try {
	    addSkill(new AgentCreatorSkill(this));
	} catch(Throwable t){System.out.println("Erreur dans l ajout de la competence agentCreatorSkill"); }
    }
}


