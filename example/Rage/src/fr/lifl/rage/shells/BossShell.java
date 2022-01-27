package fr.lifl.rage.shells;

import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;
import fr.lifl.rage.*;
import fr.lifl.rage.skills.*;

public class BossShell {

    protected Agent myAgent;
    protected Platform platform;

    public BossShell(int port) {
	platform = new Platform(port);

	myAgent = new Agent("Boss");
	myAgent.addSkill(new fr.lifl.rage.skills.BossSkill(myAgent));

	platform.addAgent(myAgent);

	System.out.println("Boss hierarchy launched.");
	System.out.println("Boss name is " + myAgent.getName());
    }
    
    public static void main(String args[]) {
	int port = 4444;

	if (args.length > 0) {
	    try {
		port = Integer.parseInt(args[0]);
	    } catch(Exception err) {
		System.err.println("invalid port !");
		System.exit(0);
	    }
	}

	//	Agent.setVerboseLevel(3);

	new BossShell(port);
    }

}
