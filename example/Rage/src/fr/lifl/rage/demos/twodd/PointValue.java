package fr.lifl.rage.demos.twodd;

public class PointValue implements java.io.Serializable {

    public Point point;
    public Force force;

    public PointValue(Point point, Force force) {
	this.point = point;
	this.force = force;
    }
}
