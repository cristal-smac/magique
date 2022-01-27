package fr.lifl.rage.demos.anerouge;

import java.util.*;
import java.math.BigInteger;
import fr.lifl.rage.*;
import fr.lifl.rage.demos.anerouge.graph.*;

public class Phase1Factory implements TaskFactory {

    protected HashMap symbolToPieces;
    protected HashSet states;
    protected Shape shape;
    protected String pieces;
    protected Permutation permutation;
    protected BigInteger numOfPerm;
    protected BigInteger currentPerm;
    protected BigInteger increment;

    public Phase1Factory(Shape shape,
			 String pieces, 
			 HashMap symbolToPieces) {
	this.shape = shape;
	this.pieces = pieces;
       	this.symbolToPieces = symbolToPieces;
	
	ArrayList array = new ArrayList(pieces.length());
	for(int i = 0; i < pieces.length(); i ++)
	    array.add(new Integer(pieces.substring(i, i+1)));

	this.permutation = new Permutation(array);
	this.numOfPerm = permutation.getNumberOfPermutationsWithDoubloons();
	this.currentPerm = BigInteger.ZERO;
	this.increment = numOfPerm.multiply(BigInteger.ONE).divide(new BigInteger("100"));
	if (this.increment.compareTo(BigInteger.ONE) < 0)
	    this.increment = BigInteger.ONE;
    }

    public boolean hasNext() {
	return currentPerm.compareTo(numOfPerm) < 0;
    }
    
    public Task next() {
	Phase1Task task = null;

	if (hasNext()) {
	    BigInteger sup = currentPerm.add(increment).compareTo(numOfPerm) > 0 ? numOfPerm : currentPerm.add(increment);

	    task = new Phase1Task(getFactoryID(),
				  "Task_"+currentPerm.longValue(),
				  currentPerm, 
				  sup, 
				  shape, 
				  pieces,
				  symbolToPieces);

	    currentPerm = currentPerm.add(increment);
	}

	return task;	
    }
   
    public String getFactoryID() {
	return "Phase1Factory";
    }    

    protected String arrayListToString(ArrayList array) {
	StringBuffer result = new StringBuffer();

	for(int i=0; i<array.size(); i++)
	    result.append(""+array.get(i));

	return result.toString();
    }

}
