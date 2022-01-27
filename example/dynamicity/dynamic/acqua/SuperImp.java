
/**
 * SuperImp.java
 *
 *
 * @author JC Routier
 * @version
 */
package dynamic.acqua;

import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class SuperImp  extends AbstractMagiqueMain {

    public void theRealMain(String[] args) 
    {
	if (args.length>=2)
	    Agent.setVerboseLevel(Integer.parseInt(args[1]));

	Agent sup = createAgent("super");
	Agent sup1 = createAgent("sup1");
	sup1.connectToBoss("super");
	Agent sup2 = createAgent("sup2");
	sup2.connectToBoss("super");


	// agents for noise : they represent other agents in the MAS
	// this agents make requests that are exchanged and use the
	// "sup*" agents ressources These agents go by pair. 
	//One connected to sup1, other to sup2 and they simply play
	//ping-pong
	int nbOfPairsOfNoisyAgents = 0;
	if (args.length>=2)
	    nbOfPairsOfNoisyAgents = Integer.parseInt(args[0]);

	System.out.println("\n =====\n"+nbOfPairsOfNoisyAgents +" pairs of agents created to simulate load due to other agents"+"\n =====");
	

	for(int i = 0; i<nbOfPairsOfNoisyAgents; i++) {
	    Agent ping = createAgent("noisyPing"+i);
	    ping.addSkill(new PingSkill(ping));
	    ping.connectToBoss("sup1");
	    Agent pong = createAgent("noisyPong"+i);
	    pong.addSkill(new PongSkill(pong));
	    pong.connectToBoss("sup2");
	    ping.perform(pong.getName(),"pong",ping.getName());
	}


    }


} // SuperImp
