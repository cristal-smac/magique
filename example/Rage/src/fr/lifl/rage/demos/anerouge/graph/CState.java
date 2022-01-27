package fr.lifl.rage.demos.anerouge.graph;

public class CState implements java.io.Serializable
{
    // tab[0] contient l'id de l'état
    // tab[1] contient la profondeur
    // tab[2..k] contient les ids des voisins
    protected int[] tab;
    protected String state;
    
    public CState(String state, int id, int depth, int[] neighbours)
    {
	this.state = state;
	this.tab = new int[neighbours.length+2];
	this.tab[0] = id;
	this.tab[1] = depth;
	for (int i=2; i<this.tab.length; i++)
	    this.tab[i] = neighbours[i-2];
    }
    
    public int connectivity()
    {
	return tab.length-2;
    }
    
    public int[] getNeighbours()
    {
	int[] neighbourStates = new int[connectivity()];
	for (int i=0; i<neighbourStates.length; i++)
	neighbourStates[i] = tab[i+2];
	return neighbourStates;
    }
    
    public String getState()
    {
	return this.state;
    }
    
    public int getId()
    {
	return tab[0];
    }
    
    public int getDepth()
    {
	return tab[1];
    }
    
}
