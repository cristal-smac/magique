package fr.lifl.magique.gui.skills;

import fr.lifl.magique.AtomicAgent;


public class AgentCreator extends AtomicAgent {
    public AgentCreator() {
        super("AgentCreator");
        try {
            addSkill(new ClassLoaderSkill(this));
        } catch (Throwable t) {
        }
        try {
            addSkill(new AgentCreatorSkill(this));
        } catch (Throwable t) {
            System.out.println("Erreur dans l ajout de la competence agentCreatorSkill");
        }
    }
}


