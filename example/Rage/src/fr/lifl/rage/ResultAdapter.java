package fr.lifl.rage;

import java.util.*;

/**
 *  An adapter for the <code>Result</code> interface.
 *
 *  @author Boulanger Patrice
 *  @see Result 
 *  @see Task#getResult()
 **/
public class ResultAdapter implements Result {

    protected HashMap hashMap;
    protected String taskId;

    /**
     *  Create a new ResultAdapter
     **/
    public ResultAdapter() {
        hashMap = new HashMap();
    }

    protected ResultAdapter(HashMap hashMap, String taskId)
    {
	this.hashMap = hashMap;
	this.taskId  = taskId;
    }

    public String getTaskID() {
        return taskId;
    }

    public void setTaskID(String taskId) {
        this.taskId = taskId;
    }

    /**
     *  Save the value of the field <code>fieldName</code> whose type is 
     *  <code>fieldClass</code>. 
     *  
     *  @param fieldClass the type of the field
     *  @param fieldName the name of the field
     *  @param fieldValue the value of the field 
     **/ 
    public void put(Class fieldClass, String fieldName, Object fieldValue) {
        hashMap.put(fieldClass.getName() + "!" + fieldName, fieldValue);
    }

    /**
     *  Get the value for a field.
     *
     *  @param fieldClass the type of the field.
     *  @param fieldName the name of the field.
     *  @return the value of the field.
     **/
    public Object get(Class fieldClass, String fieldName) {
        return hashMap.get(fieldClass.getName() + "!" + fieldName);
    }

    /**
     *  Returns an array containing the name of all the fields in this result.
     *  Format is <I>className!fieldName</I>.
     */
    public String[] keys() {
        Object[] allKeys = hashMap.keySet().toArray(); 
        String[] result = new String[allKeys.length];

        for (int i = 0; i < result.length; i ++) {
            result[i] = (String) allKeys[i];
        }

        return result;
    }

    /**
     *  Clone this result object .
     */
    public Result copy()
    {
	return new ResultAdapter(this.hashMap, this.taskId);
    }

}

