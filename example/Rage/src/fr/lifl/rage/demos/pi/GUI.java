package fr.lifl.rage.demos.pi;

import java.awt.*;
import java.awt.event.*;
import fr.lifl.magique.*;

public class GUI extends Frame implements ActionListener {
	
    private Label l_info;
    private TextField t_numTask;
    private Button b_start;
    private Button b_result;
    private Button b_quit;
    
    private Agent piAgent;
    
    public GUI(Agent piAgent) {
	super("PI");
	
	this.piAgent = piAgent;
	
	Panel p = new Panel(new FlowLayout());
	
	b_start = new Button("Start");
	b_start.addActionListener(this);
	p.add(b_start);
	
	b_result = new Button("Current result");
	b_result.addActionListener(this);
	p.add(b_result);
	
	b_quit =  new Button("Quit");
	b_quit.addActionListener(this);
	p.add(b_quit);
	
	add(p, BorderLayout.SOUTH);	    
	
	l_info = new Label("Press the start button ...");
	l_info.setAlignment(Label.CENTER);

	add(l_info, BorderLayout.CENTER);
	
	p = new Panel(new FlowLayout());
	p.add(new Label("Number of tasks :"));
	t_numTask = new TextField(5);
	p.add(t_numTask);

	add(p, BorderLayout.NORTH);

	this.pack();
	this.show();
    }
    
    public void setInfo(String msg) {
	l_info.setText(msg == null ? "" : msg);
    }
    
    public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();
	
	if (source == b_quit) {
	    piAgent.perform("disconnectFromAll");

	    try {
		Thread.currentThread().sleep(5000);
	    } catch (InterruptedException ie) {
		ie.printStackTrace();
	    }

	    System.exit(0);
	}
	
	if (source == b_start) {
	    int n;

	    try {
		n = Integer.parseInt(t_numTask.getText());
	    } catch(NumberFormatException err) {
		setInfo("Invalid number !");
		return;
	    }

	    piAgent.perform("startPiAgentSkill", 
			    new Object[] { new Integer(n) });	    
	    b_start.setEnabled(false);
	}
	
	if (source == b_result) {
	    setInfo("Get result. Please wait ...");
	    piAgent.perform("getPiResult");
	}
    }
}
