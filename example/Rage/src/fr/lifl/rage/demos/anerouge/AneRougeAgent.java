package fr.lifl.rage.demos.anerouge;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.platform.*;

public class AneRougeAgent {

    public static void main(String[] args) throws Exception {
	String bossName = null;
	int port = 4444, numOfTasks;

	if (args.length < 1) {
	    System.err.println("usage: AneRougeAgent bossName [port]");
	    System.exit(0);
	}

	bossName = args[0];

	if (args.length > 1) 
	    try {
		port = Integer.parseInt(args[1]);
	    } catch(NumberFormatException err) {
		System.err.println("Invalid port number !");
		System.exit(0);
	    }
	    
	Platform platform = new Platform(port);

	Agent arAgent = new Agent("AneRougeManager");
	arAgent.addSkill(new AneRougeSkill(arAgent));

	platform.addAgent(arAgent);

	arAgent.connectToBoss(bossName);
	arAgent.perform("startSkill", new Object[] { "040310304" } );
	//arAgent.perform("startSkill", new Object[] { "1310" });
    }
}
