package fr.lifl.rage.demos.twodd;

import java.awt.geom.Point2D;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;

public class Test {
    
    public static void resolveD(double[][] _C, double[] _B)
    {
	DenseDoubleMatrix2D C, D, B;
	C = new DenseDoubleMatrix2D(_C);
	B = new DenseDoubleMatrix2D(_C.length, 1);
	for (int i=0; i < _C.length; i++) {
	    B.setQuick(i,0, _B[i]);
	}	
	Algebra algebra = new Algebra();
	D = (DenseDoubleMatrix2D) algebra.solve(C, B);
	System.out.println(D);
    }
    
    public static void main(String[] args) {

	Point2D.Double[] points = new Point2D.Double[] {
	    new Point2D.Double( 1, -1),
	    new Point2D.Double( 1,  1),
	    new Point2D.Double(-1,  1),
	    new Point2D.Double(-1, -1)
	};

	Polygone polygone = new Polygone(points); 

	Force[] forces = new Force[4];

	forces[0] = new Force(1, 0);
	forces[1] = new Force(0, 1);
	forces[2] = new Force(-1,0);
	forces[3] = new Force(0,-1);

	int[] segments = new int[4];
	
	segments[0] = 1;
	segments[1] = 1;
	segments[2] = 1;
	segments[3] = 1;

	PointValue[] result = Segmentation.segmente(polygone, forces, segments);

	for(int i = 0; i < result.length; i ++) {
	    System.out.print(i+": Position ("+result[i].point.x+", "+result[i].point.y+")\t");
	    System.out.print("Normale [ "+result[i].point.nx+", " + result[i].point.ny+")\t");
	    System.out.print("Length = " + result[i].point.length+"\t");
	    System.out.println("Force ("+result[i].force.getX()+", "+result[i].force.getY()+")");
	}

	Twodd twodd = new Twodd(result);
	twodd.computeInfluenceCoefficients();
	System.out.println("-----------------------------------------------------------------------------------------");
	System.out.println("------ C matrix    D vector    B vector -------------------------------------------------");
	System.out.println("-----------------------------------------------------------------------------------------");
	twodd.dump();
	System.out.println("-----------------------------------------------------------------------------------------");
	System.out.println("------ Resolution of D vector -----------------------------------------------------------");
	System.out.println("-----------------------------------------------------------------------------------------");
	resolveD(twodd.C, twodd.B);

    }    
}
