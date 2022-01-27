package fr.lifl.rage.shells;

import java.awt.*;
import java.awt.event.*;
import fr.lifl.rage.*;
import fr.lifl.rage.skills.*;
import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;

public abstract class GenericShell extends Frame implements ActionListener {

    protected TextField t_boss;
    protected Button b_connect;    
    protected Agent myAgent;
    protected Platform platform;

    public GenericShell(String name, int port) {
	super(name);

	platform = new Platform(port);

        setLayout(new BorderLayout());
	
	t_boss = new TextField(30);
	b_connect = new Button("Connect to Boss");
	b_connect.addActionListener(this);

	Panel p = new Panel(new FlowLayout());
	p.add(t_boss);
	p.add(b_connect);

	add(p, BorderLayout.NORTH);		
    }

    public GenericShell(String name) {
	this(name, 4444);
    }
    
    public void setAgent(Agent a) {
	myAgent = a;
	platform.addAgent(myAgent);
    }

    public Agent getAgent() {
	return myAgent;
    }

    public void connect() {
	if (myAgent != null) 
	    myAgent.askNow("connectToBoss", new Object[] {t_boss.getText()});
    }
    
}
