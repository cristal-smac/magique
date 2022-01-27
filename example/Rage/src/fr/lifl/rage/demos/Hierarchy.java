import java.io.*;
import java.util.*;

import fr.lifl.rage.*;
import fr.lifl.rage.skills.*;

import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public class Hierarchy {

    public static void main(String[] args) {
	String bossName;
	int port;

	//Agent.setVerboseLevel(3);

	BufferedReader reader = new BufferedReader
	    (new InputStreamReader(System.in));

	System.out.print("Enter name of the boss agent [Boss]: ");
	try {
	    String s = reader.readLine();
	    bossName = s.length() != 0 ? s : "Boss";
	} catch(IOException err) {
	    System.err.println("read: " + err);
	    bossName = "Boss";
	}

	System.out.print("Enter the port of the platform [4444]: ");
	try {
	    String s = reader.readLine();
	    port = Integer.parseInt(s);
	} catch(Exception err) {
	    port = 4444;
	} 
	
	System.out.println("Building Rage hierarchy ...");

	// Platform
	Platform platform = new Platform(port);
	System.out.println("Platform " + platform.getName() + " launched");

	System.out.println("Creating agents. Please wait ...");

	// Boss agent
	Agent boss = new Agent(bossName);
	boss.addSkill(new BossSkill(boss));
	platform.addAgent(boss);
	
	// Task Dispatcher
	Agent taskDispatcher = new Agent("TaskDispatcher");
	taskDispatcher.addSkill(new TaskDispatcherSkill(taskDispatcher));
	platform.addAgent(taskDispatcher);
	taskDispatcher.connectToBoss(bossName);

	// Repositories Manager
	Agent repositoriesManager = new Agent("RepositoriesManager");
	repositoriesManager.addSkill(new RepositoriesManagerSkill(repositoriesManager));
	platform.addAgent(repositoriesManager);
	repositoriesManager.connectToBoss(bossName);

	// Platform Manager
	Agent platformManager = new Agent("PlatformManager");
	platformManager.addSkill(new PlatformManagerSkill(platformManager)); 
	platform.addAgent(platformManager);
	platformManager.connectToBoss(bossName);

	System.out.println(" -*-*-*-*-*-*-*- Agents launched -*-*-*-*-*-*-*-");
	for(Enumeration e = platform.getMyAgents().keys(); 
	    e.hasMoreElements(); )
	    System.out.println("   " + (String) e.nextElement());
	System.out.println(" -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
	    
	System.out.println("Hierarchy built !! Boss name is " + 
			   boss.getName());
		
	boss.perform("startPlatformManagerSkill");

	System.out.println("Start Repositories Manager ...");
	boss.perform("startRepositoriesManagerSkill");	
    }
}
           
