package fr.lifl.rage.demos.pi;

import java.util.*;
import fr.lifl.rage.*;

public class PiTask extends AbstractTask {

    public Double pi;

    private int currentIter;
    private int total;
    private int inner;    
    private int niter;

    public PiTask(String factoryId, String taskId, int niter) {
	super(factoryId, taskId);

	total = 0;
	inner = 0;
	this.niter = niter;
    }

    public void compute() {
	double x, y, tmp;
	int n = 0;

	for(currentIter = 0; currentIter < niter; currentIter ++) {
	    x = Math.random();
	    y = Math.random();

	    if (java.lang.Math.sqrt(x*x + y*y) <= 1.0)
		inner ++;
	}

        tmp = (double)inner / (double)niter;
	tmp *= 4;

	pi = new Double(tmp);

	//	System.out.println(getTaskID() + ": PI = " + tmp);
    }

    public boolean finished() {
	return currentIter == niter;
    }

    public double percent() {
	return (double)(currentIter * 100) / (double)niter;
    }
}
