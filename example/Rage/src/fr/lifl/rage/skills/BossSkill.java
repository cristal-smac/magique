package fr.lifl.rage.skills;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.rage.wrappers.*;

public class BossSkill extends MagiqueDefaultSkill {

    public BossSkill(Agent a) {
	super(a);
    }
    
    public void informations() {
	fr.lifl.magique.util.Team team = getMyTeam();
	java.util.Vector methods = team.getMethods();
	
	System.out.println("Known methods :");
	for(int i = 0; i < methods.size(); i++) {
	    System.out.println((String)methods.get(i));
	}
    }	
}
