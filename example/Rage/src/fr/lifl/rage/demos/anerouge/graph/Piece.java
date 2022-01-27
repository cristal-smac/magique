package fr.lifl.rage.demos.anerouge.graph;

import java.io.*;
import java.awt.*;
import java.util.*;

public class Piece implements Serializable
{

    private static int index = 0;

    // The shape of this piece
    protected Shape shape;
    protected final String id;
    // The position of the left upper corner of the shape
    public int x, y;
    public int cx = 1, cy = 1;
    // Stores the modifiers to be applicated to the current
    // position to check the ability for a piece to move
    protected Point[] north, south, east, west;
    // Stores the modifiers to be applicated to the current
    // position to check the possibility of the piece to be
    // put on the board
    protected Point[] body;

    public Piece clone(int xx, int yy)
    {
	Piece clone = new Piece(shape, id);
	clone.x = xx; clone.y = yy;
	return clone;
    }

    public Piece(Shape shape, String id)
    {
	this.shape = shape;
	this.id = id+(index++);
	body();
	northAndSouth();
	easthAndWest();
    }

    public Shape getShape()
    {
	return shape;
    }

    protected void body()
    {
	ArrayList points = new ArrayList();
	for (int y=1; y < sizeY(); y++)
	    for (int x=1; x < sizeX(); x++)
		if (shape.get(x, y))
		    points.add(new Point(x, y));
	body = new Point[points.size()];
	for (int i=0; i < points.size(); i++)
	    body[i] = (Point) points.get(i);
    }

    public int x()
    {
	return sizeX()-1;
    }

    public int y()
    {    
	return sizeY()-1;
    }
    protected int sizeX()
    {
	return shape.sizeX()-1;
    }

    protected int sizeY()
    {
	return shape.sizeY()-1;
    }

    protected void easthAndWest()
    {
	ArrayList east = new ArrayList(), west = new ArrayList();
	for (int y=1; y < sizeY(); y++)
	    for (int x=1; x < sizeX(); x++)
		if (shape.get(x, y))
		    {
			if (!shape.get(x-1, y))
			    east.add(new Point(x-1, y));
			if (!shape.get(x+1, y))
			    west.add(new Point(x+1, y));
		    }
	this.east = new Point[east.size()];
	for (int i=0; i < east.size(); i++)
	    this.east[i] = (Point) east.get(i);
	this.west = new Point[west.size()];
	for (int i=0; i < west.size(); i++)
	    this.west[i] = (Point) west.get(i);
    }

    protected void northAndSouth()
    {
	ArrayList north = new ArrayList(), south = new ArrayList();
	for (int y=1; y < sizeY(); y++)
	    for (int x=1; x < sizeX(); x++)
		if (shape.get(x, y))
		    {
			if (!shape.get(x, y-1))
			    north.add(new Point(x, y-1));
			if (!shape.get(x, y+1))
			    south.add(new Point(x, y+1));
		    }
	this.north = new Point[north.size()];
	for (int i=0; i < north.size(); i++)
	    this.north[i] = (Point) north.get(i);
	this.south = new Point[south.size()];
	for (int i=0; i < south.size(); i++)
	    this.south[i] = (Point) south.get(i);
    }
    
    public String shortId()
    {
	return this.id.substring(0,1);
    }

    public String getId()
    {
	return this.id;
    }

    public void remove(Board board, Piece newPiece)
    {
	for( int i=0; i<body.length; i++ )
	    board.set(body[i].x-1+x, body[i].y-1+y, null);
    }

    public void remove(Board board)
    {
	//System.out.println(shortId()+" removed from ("+x+","+y+")");
	for( int i=0; i<body.length; i++ )
	    {
		Piece newEmpty = board.EMPTY.clone(body[i].x-1+x, 
						   body[i].y-1+y);
		board.set(body[i].x-1+x, body[i].y-1+y, newEmpty);
	    }
    }
    
    protected Point[] getFrontier(Point offset)
    {
	if (offset.x == 0)
	{
	    if (offset.y == 1) return south;
	    else return north;
	}
	if (offset.x == 1) return west;
	return east;
    }

    public boolean move(Board board, Point offset)
    {
	//System.out.println("BEFORE MOVE");
	//System.out.println(board);
	Point[] frontier = getFrontier(offset);
	//System.out.println("In move : offset is "+offset+" piece ("+this.x+
	//		   ","+this.y+")");
	
	for (int i=0; i < frontier.length; i++)
	    {
		//System.out.println("In move : testing ("+(frontier[i].x+x-1)+
		//		   ","+(frontier[i].y+y-1)+")");
		if (!board.isEmpty(board.get(frontier[i].x-1+x, 
					     frontier[i].y-1+y)))
		    {
			//System.out.println("FAILED !");
			return false;
		    }
	    }
	remove(board);		
	tag(board, x+offset.x, y+offset.y);
	//System.out.println("AFTER MOVE");
	//System.out.println(board);
	return true;
    }

    public void showBody()
    {
	System.out.print("| ");
	for( int i=0; i<body.length; i++ )
	    System.out.print("("+body[i].x+","+body[i].y+")");
	System.out.println(" |");
    }

    public boolean get(int x, int y)
    {
	return shape.get(x,y);
    }

    public boolean fitIn(Board board, int xPos, int yPos)
    {
	//System.out.print("Fitting ? ");
	for( int i=0; i<body.length; i++ )
	    {
		int xx = xPos+body[i].x-1, yy = yPos+body[i].y-1;
		//System.out.print("("+xx+","+yy+") ");
		try {
		    if (board.get(xx, yy) != null)
			return false;
		} catch (ArrayIndexOutOfBoundsException e){
		    e.printStackTrace();
		    return false;
		}
	    }
	return true;
    }

    public void tag(Board board, int xPos, int yPos)
    {
	x = xPos; y = yPos;
	for( int i=0; i<body.length; i++ )
	    {
		board.set(xPos+body[i].x-1, yPos+body[i].y-1, this);
	    }
	//System.out.println("end tag -> adding "+shortId());
    }

    public String toString()
    {
	StringBuffer s = new StringBuffer();
	for(int y=0; y< sizeY()+1; y++)
	    {
		for(int x=0; x< sizeX()+1; x++)
		    if (shape.get(x, y))
			s.append("*");
		    else s.append(".");
		s.append(System.getProperty("line.separator"));
	    }
	return ""+s;
    }

    public static void main(String[] args)
    {
	boolean[][] shape = new boolean[][]
	{{false, false, false, false, false},
	 {false,  true,  true, false, false},
	 {false, false,  true,  true, false},
	 {false,  true,  true,  true, false},
	 {false,  true, false,  true, false},
	 {false, false, false, false, false}};

	 Piece piece = new Piece(new Shape(shape), "dragon");

	 System.out.println("Piece is "+piece.getId());
	 System.out.println(piece);

	 Point[] south = piece.getFrontier(new Point(0,-1));
	 for (int i=0; i<south.length; i++)
	     System.out.println(south[i]);

	 Point[] east = piece.getFrontier(new Point(-1,0));
	 for (int i=0; i<east.length; i++)
	     System.out.println(east[i]);


	 piece = new Piece(Shape.makeRectangularShape(2,2), "carre");

	 System.out.println("Piece is "+piece.getId());
	 System.out.println(piece);

    }
   
}
