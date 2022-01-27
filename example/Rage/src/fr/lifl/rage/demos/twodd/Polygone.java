package fr.lifl.rage.demos.twodd;

import java.awt.geom.*;

public class Polygone implements java.io.Serializable {

    protected Point2D[] points;

    public Polygone(Point2D points[]) {
	this.points = points;	    	    
    }

    public Point2D[] getPoints() {
	return this.points;
    }

    public Point2D get(int index) {
	return points[index];
    }

    public int numberOfPoints() {
	return points.length;
    }
}
