package fr.lifl.rage.demos.pi;

import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;
import fr.lifl.rage.*;
import fr.lifl.rage.wrappers.*;

public class PiAgent {
    
    public static void main(String args[]) {
	if (args.length < 1) {
	    System.err.println("Usage: PiAgent boss platform_port");
	    System.exit(0);
	}
	
	int port = 4444;
	if (args.length == 2)
	    try {
		port = Integer.parseInt(args[1]);
	    } catch(NumberFormatException err) {
		System.err.println(err);
		port = 4444;
	    }

	// Platform creation
	Platform p = new Platform(port);

	// Agent creation
        Agent piAgent = new Agent("PiAgent");
	
	p.addAgent(piAgent);
	piAgent.connectToBoss(args[0]);

	piAgent.addSkill(new PiAgentSkillBench(piAgent));
	piAgent.perform("startPiAgentSkill");

	//piAgent.addSkill(new PiAgentSkill(piAgent));

	//piAgent.setVerboseLevel(4);
    }
}
