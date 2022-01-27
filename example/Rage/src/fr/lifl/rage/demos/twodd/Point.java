package fr.lifl.rage.demos.twodd;

public class Point implements java.io.Serializable {

    public double x;
    public double y;
    public double nx;
    public double ny;
    public double length;

    public Point(double x, double y, double length, double nx, double ny) {
	this.x = x;
	this.y = y;
	this.nx = nx;
	this.ny = ny;
	this.length = length;
    }
}
