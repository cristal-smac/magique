package fr.lifl.rage.demos.anerouge.graph;

import java.io.*;

public class Shape implements Serializable
{
    private static int counter = 0;
    protected final boolean[][] shape;
    protected final int id;

    public Shape(boolean[][] shape)
    {
	this.shape = shape;
	this.id = counter++;
    }

    public boolean get(int x, int y)
    {
	return shape[y][x];
    }
    
    public int sizeX()
    {
	return shape[0].length;
    }

    public int sizeY()
    {
	return shape.length;
    }

    // Convenient way to create rectangular pieces
    public static Shape makeRectangularShape(int x, int y)
    {
	boolean[][] shape = new boolean[y+2][x+2];
	for (int iy=0; iy < y+2; iy++)
	    for (int ix=0; ix < x+2; ix++)
		if ((ix == 0) || (iy == 0) ||
		    (ix == x+1) || (iy == y+1))
		    shape[iy][ix] = false;
		else shape[iy][ix] = true;
	return new Shape(shape);
    }

}
