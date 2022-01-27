package fr.lifl.rage.demos.pi;

import fr.lifl.rage.*;

public class PiFactory implements TaskFactory {
    
    private String factoryID;
    private static int counter = 0;
    private int niter;

    public PiFactory(int niter) {
	this.factoryID = "PiFactory";
	this.niter = niter;
    }

    public String getFactoryID() {
	return factoryID;
    }

    public boolean hasNext() {
	return true;
    }

    public Task next() {
	PiTask task = new PiTask(factoryID, "PiTask" + counter++, niter);
	return task;
    }
    
}
