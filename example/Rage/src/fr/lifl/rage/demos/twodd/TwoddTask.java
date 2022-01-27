package fr.lifl.rage.demos.twodd;

import fr.lifl.rage.*;

public class TwoddTask extends AbstractTask {

    public int start, end;
    public double[][] coeff;

    protected int idx;
    protected PointValue[] pointsValues;

    public TwoddTask(String factoryId, String taskId, int start, int end) {
	super(factoryId, taskId);
	this.start = this.idx = start;
	this.end = end;
    }

    public boolean int() {
	// Initialiser coeff
	
	pointsValues = (PointValue[]) askNow("getSharedData", 
					     new Object[] {"pointsValues"});
	return pointsValues != null;
    }

    public boolean finished() {
	return idx < end;
    }

    public double percent() {
	return (double)(100 * (idx-start)) / (double)(end-start);
    }

    public void compute() {
	PointValue point = pointsValues[idx];
	
	// Calculer 4 coefficients

	idx ++;
    }	    
}
