package fr.lifl.rage.bd;

import java.util.*;

import fr.lifl.rage.*;
import fr.lifl.rage.wrappers.*;

public class DB4RageImpl implements DB4Rage
{

    protected Object  monitor = new Object();
    protected HashMap fieldNamesToResult;

    public DB4RageImpl()
    {
	this.fieldNamesToResult = new HashMap();
    }

    // Warning !!! If two Result objects share the same TaskID (this
    // should not happened!), the first result isn't overwrite.
    public void insert(WrapperResult wrapperResult)
    {
	Result result = wrapperResult.getResult();
	if (!fieldNamesToResult.containsKey(result.getTaskID()))
	    fieldNamesToResult.put(result.getTaskID(), result);
    }

    protected boolean resultMatches(Query query, Result result)
    {
	// fieldClass!fieldName
	String[] keys = result.keys();
	Class  fieldClass;
	String fieldName;
	Object fieldValue;
	int cut;
	for (int i=0; i<keys.length; i++)
	    {
		cut = keys[i].indexOf("!");
		try {
		    fieldClass = Class.forName(keys[i].substring(0, cut));
		} catch (ClassNotFoundException e) { return false; }
		fieldName  = keys[i].substring(cut+1, keys[i].length());
		fieldValue = result.get(fieldClass, fieldName);
		if (((fieldClass == query.fieldClass) || (fieldClass == null)) &&
		    ((fieldName  == query.fieldName)  || (fieldName  == null)) &&
		    ((fieldValue == query.fieldValue) || (fieldValue == null)))
		    return true;
	    }
	return false;
    }

    // Return copies of Result objects matching the query
    public ArrayList select(Query query)
    {
	ArrayList result  = new ArrayList();
	Result    current = null;
	for (Iterator it = fieldNamesToResult.values().iterator(); it.hasNext();)
	    {
		current = (Result) it.next();
		if (resultMatches(query, current))
		    result.add(current.copy());
	    }
	return result;
    }

    public ArrayList selectAndDelete(Query query)
    {
	ArrayList result;
	synchronized(monitor){
	    result = select(query);
	    delete(query);
	}
	return result;
    }

    public void delete(Query query)
    {
    }

    public void cleanUp()
    {
	this.fieldNamesToResult.clear();
    }

}
