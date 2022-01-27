package fr.lifl.rage.demos.anerouge;

import java.util.*;
import fr.lifl.rage.*;
import fr.lifl.rage.demos.anerouge.graph.*;

public class Phase2Task extends AbstractTask {

    public CompactGraph compactGraph;

    protected Board board;
    protected Shape shape;
    protected HashMap symbolToPieces;
    protected String position;
    protected boolean isFinished;
    protected int current;
    protected GraphBuilder gb;

    public Phase2Task(String factoryId, String taskId,
		      Shape shape, HashMap symbolToPieces, 
		      String position) {
	super(factoryId, taskId);
		
	this.shape = shape;
	this.symbolToPieces = symbolToPieces;
	this.position = position;
	this.isFinished = false;
	this.current = 0;
    }

    public boolean init() {
	this.board = new Board(shape, "Ane Rouge");
	System.out.println(getTaskID() + " initialized !");
	return true;
    }

    public double percent() {
	return 0.0;
    }

    public boolean finished() {
	return isFinished;
    }

    public void compute() {
	gb = new GraphBuilder(board, position, symbolToPieces);
	if (gb.start())
	    compactGraph = gb.getCompactGraph();
	else 
	    die();

	isFinished = true;
    }

    public boolean mustDie(Object cond) {
	String state = (String) cond;
	
	if (gb.containsState(state)) {
	    System.err.println("Task " + getTaskID() + "must die !");
	    return true;
	} else
	    gb.addToTabooList(state);
	
	return false;
    }	
}
