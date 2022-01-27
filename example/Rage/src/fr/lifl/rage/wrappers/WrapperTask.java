package fr.lifl.rage.wrappers;

import java.io.*;
import fr.lifl.rage.*;

/**
 * Encapsulates an object implementing the interface <code>Task</code>. 
 *
 * This is required because polymorphism of methods isn't implemented in Magique.
 * However, these objects are only used for internal purposes by Rage and shouldn't 
 * be used by users.
 *
 * @author Patrice Boulanger 
 **/
public class WrapperTask implements Serializable
{

    private fr.lifl.rage.Task task;
    
    public WrapperTask(fr.lifl.rage.Task task)
    {
	this.task = task;
    }

    public Task getTask()
    {
	return this.task;
    }

}
