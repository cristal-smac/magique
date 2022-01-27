package fr.lifl.rage.demos.anerouge;

import java.util.*;
import java.math.BigInteger;
import fr.lifl.rage.*;
import fr.lifl.rage.demos.anerouge.graph.*;

public class Phase1Task extends AbstractTask {

    public BigInteger startBound;
    public BigInteger stopBound;
    public HashSet states;

    private BigInteger current;
    private Board board;
    private Shape shape;
    private String pieces;
    private HashMap symbolToPieces;	
    private Permutation permutation;
    private boolean isFinished = false;

    public Phase1Task(String factoryId, String taskId, 
		      BigInteger startBound, BigInteger stopBound,
		      Shape shape, String pieces, HashMap symbolToPieces) {
	super(factoryId, taskId);

	System.err.println(getTaskID() + ": from " + startBound + " to " +
			   stopBound);

	this.startBound = startBound;
	this.stopBound = stopBound;
	this.shape = shape;
	this.pieces = pieces;
	this.symbolToPieces = symbolToPieces;
	
	this.states = new HashSet();	
    }

    public boolean init() {
	board = new Board(shape, "Ane Rouge");   
	ArrayList array = new ArrayList(pieces.length());

	for(int i = 0; i < pieces.length(); i ++) 
	    array.add(new Integer(pieces.substring(i, i+1)));

    	permutation = new Permutation(array);
	
	return true;
    }

    public double percent() {
	long total = stopBound.subtract(startBound).longValue();
	
	return (current.subtract(startBound).longValue() * 100.0) / total;
    }
    
    public boolean finished() {
	return isFinished;
    }

    public void compute() {
	ArrayList array;
	int invalid = 0;

	current = startBound;       

	System.out.println(getTaskID() + ": started");

	do {
	    array = permutation.nieme(current.longValue());
	    if (board.validate(arrayListToString(array), symbolToPieces)) 
		states.add(arrayListToString(array));
	    else
		invalid ++;

	    current = current.add(BigInteger.ONE);
	} while(current.compareTo(stopBound) < 0);
	
	isFinished = true;

	System.out.println(getTaskID() + ": done"); // + " invalid = " + invalid);

    }

    protected String arrayListToString(ArrayList array) {
	StringBuffer result = new StringBuffer();
	
	for(int i = 0; i < array.size(); i++)
	    result.append(""+array.get(i));
	
	return result.toString();
    }
}
