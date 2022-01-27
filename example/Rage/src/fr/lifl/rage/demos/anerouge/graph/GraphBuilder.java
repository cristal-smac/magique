package fr.lifl.rage.demos.anerouge.graph;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class GraphBuilder implements java.io.Serializable {
    
    protected HashMap symbolToPieces;
    protected String seed;
    protected Board board;

    protected FastStack unexploredStates;
    protected HashMap graph;
    protected HashSet tabooStates;
    protected CompactGraph compactGraph;

    public GraphBuilder(Board board, String positionAsString, 
			HashMap symbolToPieces) {
	this.symbolToPieces = symbolToPieces;
	this.seed = positionAsString;
	this.board = board;
	this.unexploredStates = new FastStack(1500);
	this.graph = new HashMap(40000);
	this.tabooStates = new HashSet();
    }

    protected void init() {
	board.validate(seed, symbolToPieces);
	unexploredStates.add(board.getState());
	board.reset();
    }

    protected void dumpMemory() {
	Runtime r = Runtime.getRuntime();
	long total = r.totalMemory() ; // / 1024 ;
	long free  = r.freeMemory() ; // / 1024 ;
	System.out.println("Total (kB): "+total+" Free : "+free+
			   " Used : "+(total-free));
    }

    protected void info() {
	if (graph.size() % 3000 == 0) {
	    System.out.println(" ("+unexploredStates.size()+" ?, "+
			       graph.size()+" !)");
	    this.dumpMemory();
	    if (graph.size() % 4000 == 0) System.gc();
	} 
    }

    protected String getNextUnexploredState() {
	return (String) unexploredStates.remove();
    }

    public void addToTabooList(String state) {
	this.tabooStates.add(state);
    }

    public boolean start() {	
	String currentState;
	String[] states;
	State cState = null;
	boolean firstState = true;
	int counter = 0;
	int lastCheck = 0;

	this.init();

	while (unexploredStates.size() > 0) {
		currentState = this.getNextUnexploredState();
		board.validate(currentState, symbolToPieces);
		states = board.getNeighbours();

		// Creation of the graph of State objects
		if (graph.containsKey(currentState)) {
		    cState = (State) graph.get(currentState);
		} else {
		    cState = new State(currentState, counter ++);
		    if (firstState) {
			firstState = false;
			cState.setDepth((short) 0);
		    }
		    graph.put(currentState, cState);
		}
		
		// Updating the neighbours of the current state
		for (int i=0; i<states.length; i++) {
		    State neighbour = null;
		    
		    if (graph.containsKey(states[i])) {
			neighbour = (State) graph.get(states[i]);
		    } else {
			neighbour = new State(states[i], counter ++);
			graph.put(states[i], neighbour);
			unexploredStates.add(states[i]);
		    }

		    cState.addNeighbour(neighbour);
		    neighbour.addNeighbour(cState);
		    neighbour.setDepth((short) (cState.getDepth()+1));
		}
		
		if (lastCheck % 100 == 0) {
		    Iterator it = tabooStates.iterator();

		    while(it.hasNext()) {
			String state = (String) it.next();

			if (graph.containsKey(state)) {
			    System.err.println("Computation aborted !");
			    graph.clear();
			    System.gc();
			    return false;
			}
		    }
		}
		
		lastCheck ++;
	}

	System.out.println(graph.size() + " states  in the graph");
	System.out.println("Translating ... ");
	compactGraph = new CompactGraph(graph);
	System.out.println("done");

	graph.clear();
	System.gc();
	
	return true;
    }

    public boolean containsState(String state) {
	return graph.containsKey(state);
    }

    public CompactGraph getCompactGraph() {
	return this.compactGraph;
    }
}
