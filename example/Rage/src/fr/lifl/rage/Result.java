package fr.lifl.rage;

import java.io.*;

/**
 *  This interface describes the methods which have to be supported by objects 
 *  which can be used to saved the state of a <code>Task</code> implementator.
 *
 *  @author Boulanger Patrice
 *  @see ResultAdapter
 **/
public interface Result extends Serializable
{
    /**
     *  Save the value of the field <code>fieldName</code> whose class is 
     *  <code>fieldClass</code>. 
     *  
     *  @param fieldClass the class of the field
     *  @param fieldName the name of the field
     *  @param fieldValue the value of the field 
     **/     
    public void put(Class fieldClass, String fieldName, Object fieldValue);    

    /**
     *  Get the value for a field.
     *
     *  @param fieldClass the class of the field.
     *  @param fieldName the name of the field.
     *  @return the value of the field.
     **/
    public Object get(Class fieldClass, String fieldName);
    
    /**
     *  Returns an array containing all the fields names knew by this Result.
     */
    public String[] keys();

    /**
     *  Returns the task identificator of the task of this result.
     */
    public String getTaskID();

    /**
     *  Set the task identificator.
     */
    public void setTaskID(String id);

    /**
     *  Clone this result object .
     */
    public Result copy();

}

