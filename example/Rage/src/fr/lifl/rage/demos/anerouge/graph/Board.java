package fr.lifl.rage.demos.anerouge.graph;

import java.io.*;
import java.awt.*;
import java.util.*;

public class Board implements Serializable
{
    public static final String EMPTY_ID = "0";

    public static final Shape one = Shape.makeRectangularShape(1,1);
    public Piece EMPTY = new Piece(one, EMPTY_ID);
    public static final Piece BORDER = new Piece(one, "O");
    
    //protected ArrayList pieces;
    protected String name;
    protected Piece[][] shape;
    protected int xPos, yPos;

    public Board(Shape shape, String name)
    {	
	this.name = name;
	this.shape = new Piece[shape.sizeY()][shape.sizeX()];
	for (int y=0; y < sizeY(); y++)
	    for (int x=0; x < sizeX(); x++)
		if (shape.get(x,y))
		    this.shape[y][x] = null;
		else this.shape[y][x] = BORDER;
	this.xPos = 0; this.yPos = 0;
	findNextFreeCell();
	//this.pieces = new ArrayList();
    }

    public ArrayList getPieces()
    {
	//return pieces;
	return null;
    }

    public Piece get(int x, int y)
    {
	return shape[y][x];
    }

    public void set(int x, int y, Piece p)
    {
	shape[y][x] = p;
    }

    public void reset()
    {	
	for (int y=0; y < sizeY(); y++)
	    for (int x=0; x < sizeX(); x++)
		if (shape[y][x] != BORDER)
		    shape[y][x] = null;
	xPos = 0; yPos = 0;
	findNextFreeCell();
    }

    protected void findNextFreeCell()
    {
	while ( yPos < sizeY() && xPos < sizeX())
	    {
		if (shape[yPos][xPos] == null) return;
		xPos++;
		if (xPos == sizeX())
		    {
			xPos = 1;
			yPos++;
		    }
	    }
    }

    public boolean addPiece(Piece p)
    {
	//System.out.println("Trying to fit "+p.shortId()+" at ("+xPos+","+yPos+")");
	if(!p.fitIn(this, xPos, yPos))
	    {
		//System.out.println("Piece do not fit !");
		return false;
	    }
	p.tag(this, xPos, yPos);
	p.x = xPos; p.y = yPos;
	//pieces.add(p);
	findNextFreeCell();
	//System.out.println("Piece do fit ! ("+xPos+","+yPos+")");
	return true;
    }

    public boolean validate(String positionAsString, HashMap symbolToPieces)
    {
	reset();
	Piece[] position = new Piece[positionAsString.length()];
	for (int i=0; i< positionAsString.length(); i++)
	    {
		String symbol = positionAsString.substring(i,i+1);
		Piece current = (Piece) symbolToPieces.get(symbol);
		position[i] = (Piece) current.clone(current.x,current.y);
		//System.out.print(symbol+"="+position[i].shortId()+" ");
	    }
	//System.out.println("Position translated ");
	for (int i=0; i < position.length; i++)
	    {
		// If one of the piece can't fit, it's not 
		// a valid position !
		if (!addPiece(position[i])) 
		    return false;
	    }
	// We now have to check that the board is 
	// totally filled with pieces.
//	for (int x=1; x < sizeX(); x++)
//	    for (int y=1; y < sizeY(); y++)
//		if (shape[y][x] == EMPTY) return false;
	return true;
    }

    protected int sizeX()
    {
	return this.shape[0].length;
    }

    protected int sizeY()
    {
	return this.shape.length;
    }

    public String getName()
    {
	return this.name;
    }

    protected ArrayList findEmptyPieces()
    {
	ArrayList empties = new ArrayList();

	for (int iy=1; iy<sizeY(); iy++)
	    for (int ix=1; ix<sizeX(); ix++)
		    if (isEmpty(get(ix, iy)))
			empties.add(get(ix, iy));

	return empties;
    }

    public boolean isEmpty(Piece p)
    {
	return p.shortId().equals(EMPTY_ID);
    }

    protected Point reverse(Point offset)
    {
	return new Point(-1*offset.x, -1*offset.y);
    }

    protected String getState()
    {
	ArrayList piecesRemoved = new ArrayList();
	StringBuffer state = new StringBuffer();
	for (int iy=1; iy < sizeY(); iy++)
	    for (int ix=1; ix < sizeX(); ix++)
		{
		    Piece current = get(ix,iy);
		    if (current != BORDER && current != null)
			{
			    state.append(current.shortId());
			    piecesRemoved.add(current);
			    current.remove(this, null);
			}
		}
	reset();
	for (int i=0; i < piecesRemoved.size(); i++)
	    addPiece((Piece) piecesRemoved.get(i));
	return state.toString();
    }

    public String[] getNeighbours()
    {	
	ArrayList states = new ArrayList();
	final Point[] offset = new Point[]{
	    new Point(0,-1), new Point(0,1),
	    new Point(-1,0), new Point(1,0)};
	final ArrayList emptyPieces = findEmptyPieces();
	//System.out.println("We have "+emptyPieces.size()+" holes");
	Piece empty, current;
	for(int i=0; i < emptyPieces.size(); i++)
	{
	    empty = (Piece) emptyPieces.get(i);
	    //System.out.println("CURRENT Empty ("+empty.x+","+empty.y+")");
	    for (int j=0; j<offset.length; j++)
		{
		    //System.out.println("Should hold ("+
			//	       empty.x+""+offset[j].x+","+
			//	       empty.y+""+offset[j].y+")  "+
			//	       offset[j]);
		    current = get(empty.x+offset[j].x, 
					empty.y+offset[j].y);
		    //System.out.println("Actually holding ("+
			//	       current.x+","+current.y+")");
		    if (current != BORDER && !current.shortId().equals(EMPTY_ID) &&
			current.move(this, reverse(offset[j])))
			{
			    states.add(getState());
			    current.move(this, offset[j]);
			    //System.out.println("MOVE IS VALID");
			} else {
			    //System.out.println("MOVE NOT VALID");
			}
		}
	}
	String[] result = new String[states.size()];
	for(int i=0; i < states.size(); i++)
	{
	    result[i] = (String) states.get(i);
	}
	return result;
    }

    public String toString()
    {
	StringBuffer s = new StringBuffer();
	for(int y=0; y< sizeY(); y++)
	    {
		for(int x=0; x< sizeX(); x++)
		    s.append((shape[y][x] != null ? shape[y][x].shortId():"."));
		s.append(System.getProperty("line.separator"));
	    }
	return ""+s;
    }

    public static void main(String[] args)
    {
	boolean[][] shape = new boolean[][]
	{{false, false, false, false, false},
	 {false,  true,  true, false, false},
	 {false,  true,  true,  true, false},
	 {false,  true,  true,  true, false},
	 {false,  true, false,  true, false},
	 {false, false, false, false, false}};

	 Board board = new Board(new Shape(shape), "napoleon");

	 System.out.println("Board is "+board.getName());
	 System.out.println(board);

	 shape = new boolean[][]
	 {{false, false, false, false, false, false},
	  {false,  true,  true,  true,  true, false},
	  {false,  true,  true,  true,  true, false},
	  {false,  true,  true,  true,  true, false},
	  {false,  true,  true,  true,  true, false},
	  {false,  true,  true,  true,  true, false},
	  {false, false, false, false, false, false}};

	 board = new Board(new Shape(shape), "Ane Rouge");

	 System.out.println("Board is "+board.getName());
	 System.out.println(board);

	 HashMap symbolToPieces = new HashMap();
	 symbolToPieces.put("0", 
			    new Piece(Shape.makeRectangularShape(1,1), "0"));
	 symbolToPieces.put("1", 
			    new Piece(Shape.makeRectangularShape(1,2), "1"));
	 symbolToPieces.put("2", 
			    new Piece(Shape.makeRectangularShape(2,2), "2"));
	 symbolToPieces.put("3", 
			    new Piece(Shape.makeRectangularShape(1,1), "3"));
	 symbolToPieces.put("4", 
			    new Piece(Shape.makeRectangularShape(2,1), "4"));
	 symbolToPieces.put("5", 
			    new Piece(Shape.makeRectangularShape(1,1), "5"));

	 String position = "121040155155";
	 
	 System.out.println(board.validate(position, symbolToPieces));
	 System.out.println(board);
	 System.out.println(board.getState());
	 String[] states = board.getNeighbours();
	 for (int i=0; i<states.length; i++)
	     {
		 System.out.println(states[i]);
		 System.out.println(board.validate(states[i], 
						   symbolToPieces));
		 System.out.println(board);
	     }
    }

}
