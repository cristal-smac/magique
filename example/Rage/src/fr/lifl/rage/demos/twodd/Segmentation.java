package fr.lifl.rage.demos.twodd;

import java.awt.geom.*;

public class Segmentation {

    static PointValue[] segmente(Polygone polygone, 
				 Force[] forces, 
				 int[] segments) 
    {	
	// Tabs must have the number of points
	if (polygone.numberOfPoints() != forces.length || 
	    polygone.numberOfPoints() != segments.length) 
	{
	    return new PointValue[0];
	}
	
	int n = 0;	
	for(int i = 0; i < segments.length; n += segments[i++]);
	
	PointValue[] result = new PointValue[n];	

	int p1 = 0, p2;
	int index = 0;

	for(int s = 0; s < polygone.numberOfPoints(); s ++) {
	    p2 = (p1 == polygone.numberOfPoints()-1) ? 0 : (p1+1);
	    
	    Point2D from = polygone.get(p1);
	    Point2D to = polygone.get(p2);

	    double dX = to.getX() - from.getX();
	    double dY = to.getY() - from.getY();
	 
	    double length = Math.sqrt(dX*dX + dY*dY);	    
	    
	    for(int i = 0; i < segments[p1]; i ++) {		
		double x, y, nx, ny;

		// coordonnées du (i+1)ème point
		x = from.getX() + ((2*i+1) * dX) / (2 * segments[p1]);
		y = from.getY() + ((2*i+1) * dY) / (2 * segments[p1]);

		// coordonnées du vecteur normal (normalisé)
		nx = -dY / length;
		ny = dX / length;

		Point p = new Point(x, y, length/segments[p1], nx, ny);
		
		result[index ++] = new PointValue(p, forces[p1]);
	    }
	    
	    p1 = p2;
	}

	return result;		    
    }
}
