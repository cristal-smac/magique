package fr.lifl.rage.demos.anerouge;

import java.util.*;
import java.math.BigInteger;
import fr.lifl.rage.*;
import fr.lifl.rage.demos.anerouge.graph.*;

public class Phase2Factory implements TaskFactory {

    protected HashSet states;
    protected Shape shape;
    protected HashMap symbolToPieces;
    protected int counter;
    protected Iterator iterator;

    public Phase2Factory(Shape shape, 
			 HashSet states,
			 HashMap symbolToPieces) {
	
	this.states = states;
	this.shape = shape;
	this.symbolToPieces = symbolToPieces;
	this.counter = 0;
	this.iterator = states.iterator();
    }

    public boolean hasNext() {
	return iterator.hasNext();
    }

    public Task next() {
	Phase2Task task = null;
	
	if (hasNext()) {
	    String positionAsString = (String) iterator.next();
	    task = new Phase2Task(getFactoryID(), 
				  "Task_"+ counter++,
				  shape, symbolToPieces,
				  positionAsString);

	    System.err.println(task.getTaskID() + " compute state " + 
			     positionAsString); 
	}

	return task;
    }

    public String getFactoryID() {
	return "Phase2Factory";
    }
}
