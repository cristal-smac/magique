package fr.lifl.rage.shells;

import java.awt.*;
import java.awt.event.*;
import fr.lifl.magique.*;
import fr.lifl.rage.*;
import fr.lifl.rage.skills.*;

public class RepositoriesManagerShell extends GenericShell {

    protected Agent myAgent;
    protected Button b_start;
    protected Label l_card;
    
    public RepositoriesManagerShell(int port) {
	super("RepositoriesManager", port);

	myAgent = new Agent("RepositoriesManager");
	myAgent.addSkill(new fr.lifl.rage.skills.RepositoriesManagerSkill(myAgent));
	setAgent(myAgent);

	b_start = new Button("Start skill");
	b_start.addActionListener(this);
	add(b_start, BorderLayout.SOUTH);
	
	l_card = new Label("Number of objects in repository", Label.CENTER);
	add(l_card, BorderLayout.CENTER);

	setAgent(myAgent);

	pack();
	show();
    }
    
    public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();

	if (source == b_connect) 
	    connect();

	if (source == b_start) {
	    myAgent.perform("startRepositoriesManagerSkill");
	    new ManagerThread().start();
	}
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

	//Agent.setVerboseLevel(3);

	new RepositoriesManagerShell(port);	
    }

    class ManagerThread extends Thread {
	
	public void run() {
	    Integer nelements;

	    while(true) {
		try {
		    Thread.sleep(1000);
		} catch(InterruptedException err) {
		}
	    
		nelements = (Integer) myAgent.askNow("numberOfElements");
		l_card.setText(nelements + " element(s) in repository");
	    }
	}
    }

}
