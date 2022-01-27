package fr.lifl.rage.demos.twodd;

import java.io.*;
import fr.lifl.rage.*;
import fr.lifl.rage.wrappers.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

public class TwoddManagerSkill extends MagiqueDefaultSkill {

    protected Polygone polygone;
    protected Force[] forces;
    protected int[] segments;

    protected PointValue[] pointsValues = null;

    DenseDoubleMatrix2D coefMatrix;
    DenseDoubleMatrix2D	forcesVector;
    DenseDoubleMatrix2D	discontinuitiesVector;

    public TwoddManagerSkill(Agent a) {
	super(a);
    }

    public Object getData(String key) {
	if (key == "pointsValue") 
	    return pointsValues;
	
	return null;
    }

    public void phase1Skill(String file) {
	System.out.println(" ----- Phase 1 started");
	System.out.print("Reading data in " + file + " ... ");

	try {	    
	    FileInputStream fis = new FileInputStream(file);
	    ObjectInputStream ois = new ObjectInputStream(fis);

	    polygone = (Polygone) ois.readObject();
	    forces = (Force[]) ois.readObject();
	    segments = (int[]) ois.readObject();

	    fis.close();
	} catch(IOException err) {
	    err.printStackTrace();
	} catch(ClassNotFoundException err2) {
	    err2.printStackTrace();
	}

	System.out.println("done");

	System.out.print("Segmentation ... ");
	pointsValues = Segmentation.segmente(polygone, forces, segments);
	System.out.println("done");

	System.out.println("Phase 1 done");
    }

    public void phase2Skill() {
	System.out.println(" ----- Phase 2 started");
	System.out.print("Sending tasks ... ");

	TaskFactory factory = new TwoddFactory(pointsValues);
	int numTasks = 0;

	while(factory.hasNext()) {
	    Task task = factory.next();
	    
	    perform("addNewTask", new Object[] { new WrapperTask(task) });
	    numTasks ++;
	}
	
	System.out.println("done");
	System.out.println("Waiting the results (" + numTasks + ") ...");
	
	int numResults = 0, i;

	do {
	    Thread.yield();
	    
	    Set results = (Set) askNow("getAndDeleteAll");
	    numResults += results.size();

	    // zoli affichage
	    for(i = 0; i < (n.intValue()*50/numTasks); i++)
		System.out.print("|");
	    
	    for( ; i < 50; i ++)
		System.out.print("-");

	    for(i = 0 ; i < 50; i ++)
		System.out.print("\b");
	    
	    for(Iterator it = results.iterator(); it.hasNext(); ) {
		Result r = (Result) results.next();
		
		// Ranger dans la matrice coefMatrix
	    }
	} while(numResults < numTasks);
	
	System.out.println("Phase 2 done");
    }

    public void phase3Skill() {
	System.out.println(" ----- Phase 3 started");

	// Fabriquer la matrice des forces
	forcesVector = new DenseDoubleMatrix2D(coefMatrix.length, 1);
	for(int i = 0; i < coefMatrix.length/2; i ++) {
	    forcesVector.setQuick(2*i, 0, forces[i].tangentielle);
	    forcesVector.setQuick(2*i+1, 0, forces[i].normale);
	}

	System.out.print("Resolving the system ... ");

	discontinuitiesVector = (DenseDoubleMatrix2D) algebra.solve(coefMatrix, 
								    forcesVector);
	System.out.println("done");
    }
}
