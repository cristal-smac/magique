package fr.lifl.rage.demos.anerouge.graph;

import java.io.*;
import java.util.*;

public class State implements Serializable
{
    protected final int id;
    protected short depth = Short.MAX_VALUE;

    protected String state;
    protected State[] neighbours = new State[10];
    protected short index = 0;

    public State(String state, int counter)
    {
	this.state = state;
	this.id = counter;
    }

    public short getDepth()
    {
	return depth;
    }

    public void setDepth(short depth)
    {
	if (depth < this.depth)
	    this.depth = depth;
    }

    public int getId()
    {
	return id;
    }

    public int connectivity()
    {
	for (int i=0; i<neighbours.length; i++)
	    if (neighbours[i] == null)
		return i;
	return 0;
    }

    public String getState()
    {
	return this.state;
    }

    public String toString()
    {
	return getState();
    }

    public State[] getNeighbours()
    {
	State[] result = new State[connectivity()];
	for (int i=0; i<result.length; i++)
	{
	    result[i] = neighbours[i];
	}
	return result;
    }
    
    public boolean isNeighbour(State neighbour)
    {
	for (int i=0; i<index; i++)
	    if (neighbours[i] == neighbour)
		return true;
	return false;
    }

    public void addNeighbour(State neighbour)
    {
	if (!isNeighbour(neighbour))
	    neighbours[index++] = neighbour;
    }

    private void writeObject(java.io.ObjectOutputStream os) throws IOException
    {
//	System.out.println("In writeObject !");
	os.writeInt(this.id);
	os.writeObject(this.state);
	os.writeInt(connectivity());
	for (int i=0; i<connectivity(); i++)
	    os.writeInt(neighbours[i].getId());
    }

    private void readObject(java.io.ObjectInputStream is) throws IOException, ClassNotFoundException
    {
/*	this.state = is.readString();
	int idx = is.readInt();
	String[] neighb = new String[idx];
	for (int i=0; i<idx; idx++)
	    neighb[i] = (State) is.readObject();*/
    }

    // Introducing StringBuffer to save memory

/*    protected final int ID = 0;
    protected final int WORD_SIZE = 10;
    protected final int EDGE_CONNECTIVITY = 10;

    protected StringBuffer memory = new StringBuffer(1+WORD_SIZE+EDGE_CONNECTIVITY);*/

    

}
