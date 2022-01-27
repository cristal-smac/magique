package fr.lifl.rage.demos.twodd;

import fr.lifl.rage.*;

public class TwoddFactory extends TaskFactory {

    protected PointValue[] pointsValues;
    protected int start;
    protected int incr = 10;

    public TwoddFactory(PointValue[] pointsValues) {
	this.pointsValues = pointsValues;
	this.start = 0;
    }

    public boolean hasNext() {
	return start < pointsValues.length;
    }

    public Task next() {
	Task task = null;

	if (hasNext()) {
	    int end = (start + incr < pointsValues.length) ? 
		(start + incr) : 
		pointsValues.length;

	    task = new TwoddTask(getFactoryID(), "Twodd"+start, 
				 start, end);
	    start = end;
	}

	return task;
    }

    public String getFactoryID() {
	return "TwoddFactory";
    }
}
