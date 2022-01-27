package fr.lifl.rage.shells;

import java.awt.*;
import java.awt.event.*;
import fr.lifl.magique.*;
import fr.lifl.rage.*;
import fr.lifl.rage.skills.*;

public class TaskDispatcherShell extends GenericShell {

    protected Agent myAgent;

    public TaskDispatcherShell(int port) {
	super("TaskDispatcher", port);

	myAgent = new Agent("TaskDispatcher");
	myAgent.addSkill(new fr.lifl.rage.skills.TaskDispatcherSkill(myAgent));
	setAgent(myAgent);
	
	pack();
	show();
    }

    public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();

	if (source == b_connect) 
	    connect();	
    }


    public static void main(String[] args) {
	int port = 4444;

	if (args.length > 0) {
	    try {
		port = Integer.parseInt(args[0]);
	    } catch(Exception err) {
		System.err.println("invalid port !");
		System.exit(0);
	    }
	}

	//Agent.setVerboseLevel(3);

	new TaskDispatcherShell(port);	
    }
}
