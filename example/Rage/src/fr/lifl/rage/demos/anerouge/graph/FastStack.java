package fr.lifl.rage.demos.anerouge.graph;

import java.util.*;

public class FastStack implements java.io.Serializable {
    
    protected HashMap index;
    protected ArrayList queue;

    public FastStack(int size)
    {
	index = new HashMap(size);
	queue = new ArrayList(size);
    }

    public boolean known(Object key)
    {
	return index.containsKey(key);
    }

    private final Boolean BOOLEAN_CONSTANT = new Boolean(false);
    
    public void add(Object s)
    {
	if (!index.containsKey(s))
	    {
		index.put(s, BOOLEAN_CONSTANT);
		queue.add(s);
	    }
/*	else 
	    {
		System.out.println("Already known "+s+" size of queue "+
				   queue.size()+" size of index "+index.size());
	    }*/
    }
    
    public int size()
    {
	return queue.size();
    }

    public Object get(Object hashcode)
    {
	return index.get(hashcode);
    }

    public Object remove()
    {
	Object result = queue.remove(0);
	index.remove(result);
	return result;
    }
    
}
