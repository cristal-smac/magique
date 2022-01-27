package fr.lifl.rage.demos.anerouge.graph;

import java.io.*;

public class CompactGraph implements java.io.Serializable {

    protected String name = "No Name ";
    protected CompactState[] allCompactStates;

    public CompactGraph(java.util.HashMap graph)
    {
	State current;
	State[] neigh;
	allCompactStates = new CompactState[graph.size()];
	java.util.Iterator it = graph.values().iterator();
	CompactState currentCS;
	int[] voisins;
	while (it.hasNext())
	    {
		//System.out.print("!");
		current = (State) it.next();
		//System.out.print(current.getId()+":"+current+" -> ");
		neigh = current.getNeighbours();

		//DEBUG
		if (neigh.length == 0) {
		    System.out.println(current+" has no neighbours ("+
				       neigh.length+")");
		}

		voisins = new int[neigh.length];
		for (int i =0; i<voisins.length; i++)
		    {
			voisins[i] = neigh[i].getId();
			//System.out.print(voisins[i]+" ");
		    }
		//System.out.println();
		currentCS = new CompactState(current.getState(), 
					     current.getId(),
					     current.getDepth(),
					     voisins);
		allCompactStates[currentCS.getId()] = currentCS;
	    }
	/*for (int i =0; i<50; i++)
	    System.out.println(i+"="+allCompactStates[i].getId());*/
    }

    public CompactState[] getRawGraph()
    {
	return allCompactStates;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public String getName()
    {
	return this.name;
    }

    public int getSize()
    {
	return allCompactStates.length;
    }

    public CState getCStateAt(int id)
    {
	CompactState current = allCompactStates[id];
	return new CState(current.getState(), current.getId(), 
			  current.getDepth(), current.getNeighboursAsInt());
    }

    public String toString() {
	StringBuffer sb = new StringBuffer();

	sb.append(getSize() + ":");
	for(int i = 0; i < allCompactStates.length; i ++)
	    sb.append(allCompactStates[i]+",");
	
	return sb.toString();
    }

    public void dumpForApplet(String filename, int height, int width,
			      String center_position)
    {
	BufferedWriter os; 
	CompactState[] neighbours;
	String from, couple;

	try {
	    os = new BufferedWriter(new FileWriter(filename));

	    String head = "<html><head><title>"+name+" Graph</title></head><body><h1>"+name+"Graph</h1><hr><applet codebase=\"../classes\" code=\"Graph/Graph.class\" width="+width+" height="+height+">";
	    String edges = "<param name=edges ";
	    String values = "value=\"";
	    /*"joe-food,joe-dog,joe-tea,joe-cat,joe-table,table-plate/50,plate-food/30,food-mouse/100,food-dog/100,mouse-cat/150,table-cup/30,cup-tea/30,dog-cat/80,cup-spoon/50,plate-fork,dog-flea1,dog-flea2,flea1-flea2/20,plate-knife\">"; */

	    String end_edge = "\">";
	    String center = "<param name=center value=\""+center_position+"\">";
	    String end = "alt=\"Your browser understands the &lt;APPLET&gt; tag but isn't running the applet, for some reason.\" Your browser is completely ignoring the &lt;APPLET&gt; tag!</applet><hr></body></html>";
	    
	    os.write(head, 0, head.length());
	    os.write(edges,0, edges.length());
	    os.write(values,0, values.length());
	    //for (int i=0; i< allCompactStates.length; i++)
	    for (int i=0; i< 100; i++)
		{
		    from = ""+allCompactStates[i].getId();
		    neighbours = allCompactStates[i].getNeighbours();
		    /*System.out.println("From "+i+" ("+
				       allCompactStates[i].getDepth()+")");*/
		    for (int j=0; j<neighbours.length; j++)
			{
			    /*System.out.print(neighbours[j].getId()+"("+
					     neighbours[j].getDepth()+") ");*/
			if ((allCompactStates[i].getDepth() < 3) &&
			    (allCompactStates[i].getId() < neighbours[j].getId()))
			    {
				/*if ((i == allCompactStates.length-1) &&
				    (j == neighbours.length-1))
				    {
					couple = ","+from+"-"+
					    neighbours[j].getId();
				    } else {*/
				//if (Math.abs(neighbours[j].getDepth()-
				//     allCompactStates[i].getDepth()) == 1)
				//    {
				couple = from+"-"+neighbours[j].getId()+",";
				os.write(couple, 0, couple.length());
				//    }
			    }
			os.flush();
			}
		    //System.out.println();
		}
	    //os.write("0-0", 0, 3);
	    os.write(end_edge, 0, end_edge.length());
	    os.write(center, 0, center.length());
	    os.write(end, 0, end.length());
	    os.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void writeObject(java.io.ObjectOutputStream os) 
	throws IOException
    {
	CompactState current;
	CompactState[] voisins;
	
	os.writeObject(name);

	os.writeInt(this.allCompactStates.length);
	for (int i=0; i<this.allCompactStates.length; i++)
	    {
		current = this.allCompactStates[i];
		os.writeObject(current.getState());
		// i == id so no need to save it !
		os.writeInt(current.getDepth());
		voisins = current.getNeighbours();
		os.writeInt(voisins.length);
		for (int j=0; j<voisins.length; j++)
		    os.writeInt(voisins[j].getId());
	    }
    }

    private void readObject(java.io.ObjectInputStream is) 
	throws IOException, ClassNotFoundException
    {	
	CompactState current;
	name = (String) is.readObject();
	this.allCompactStates = new CompactState[is.readInt()];
	for (int i=0; i<this.allCompactStates.length; i++)
	    {
		String state = (String) is.readObject();
		int depth = is.readInt();
		int[] voisins = new int[is.readInt()];
		for (int j=0; j<voisins.length; j++)
		    voisins[j] = is.readInt();
		this.allCompactStates[i] = new CompactState(state, i, depth, voisins);
	    }
    }

    public class CompactState implements java.io.Serializable
    {
	// tab[0] contient l'id de l'état
	// tab[1] contient la profondeur
	// tab[2..k] contient les ids des voisins
	protected int[] tab;
	protected String state;

	public CompactState(String state, int id, int depth, int[] neighbours)
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

	public int[] getNeighboursAsInt()
	{
	    int[] neighbourStates = new int[connectivity()];
	    for (int i=0; i<neighbourStates.length; i++)
	        neighbourStates[i] = tab[i+2];
	    return neighbourStates;
	}

	public CompactState[] getNeighbours()
	{
	    CompactState[] neighbourStates = new CompactState[connectivity()];
	    for (int i=0; i<neighbourStates.length; i++)
	        neighbourStates[i] = allCompactStates[tab[i+2]];
	    return neighbourStates;
	}

	public int[] getNodesWithSmallestDepth()
	{
	    if (getDepth() == 0) return new int[]{};

	    int smallestDepth = getDepth()-1;
	    int nodesAtSmallestDepth = 0;
	    for (int i=2; i<tab.length; i++)
	    {
		if (allCompactStates[tab[i]].getDepth() == smallestDepth)
		    nodesAtSmallestDepth++;
	    }
	    int[] neighboursWithSmallestDepth = new int[nodesAtSmallestDepth];
	    int index = 0;
	    for (int i=2; i<tab.length; i++)
	    {
		if (allCompactStates[tab[i]].getDepth() == smallestDepth)
		    neighboursWithSmallestDepth[index++] = tab[i];
	    }
	    return neighboursWithSmallestDepth;
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

	public String toString() {
	    return getState();
	}

    }
    
}
