package fr.lifl.rage.shells;

import java.awt.*;
import java.awt.event.*;
import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;
import fr.lifl.rage.*;
import fr.lifl.rage.skills.*;

public class PlatformManagerShell extends GenericShell {

    protected Agent myAgent;
    protected Button b_name;
    protected TextField t_name;

    public PlatformManagerShell(int port) {
	super("PlatformManager", port);

	myAgent = new Agent("PlatformManager");
	myAgent.addSkill(new fr.lifl.rage.skills.PlatformManagerSkill(myAgent));
	setAgent(myAgent);

	Panel p = new Panel();
	
	t_name = new TextField(20);
	p.add(t_name);

	b_name = new Button("Set name");
	b_name.addActionListener(this);
	p.add(b_name);

	add(p, BorderLayout.CENTER);

	pack();
	show();
    }
    
    public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();

	if (source == b_connect) {
	    connect();
	    myAgent.perform("startPlatformManagerSkill");
	}

	if (source == b_name) {
	    String tmp = t_name.getText();

	    if (tmp.length() > 0)
		myAgent.setName(tmp);
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

	new PlatformManagerShell(port);
    }

}
