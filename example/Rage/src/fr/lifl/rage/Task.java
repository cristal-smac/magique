package fr.lifl.rage;

import java.io.*;
import fr.lifl.magique.*;

/**
 * This interface represents the basic entity computable by the subsystem.
 *
 * This entity must be a finite computation (i.e. should not contains an
 * infinite loop).
 *
 * Rather than implementing this interface, applications should extend 
 * the <code>AbstractTask</code> class.
 * The <code>AbstractTask</code> provides a model of computation where a
 * the user has to implement only two methods : compute() and finished().
 * While the finished() method returns "true" the compute() method is 
 * called.
 * Moreover, the <code>AbstractTask</code> provides an implementation for
 * the method <code>getResult</code>, which returns a dictionnary of the 
 * fields of the subclass which inherits from the 
 * <code>AbstractTask<code> class.
 *
 * If the model of computation is not suitable for the user's application,
 * this interface should be implemented by following those requirements :
 * <ul>
 *   <li>A call to  the <code>suspend()</code> method must suspend the 
 *       task until the next call to <code>launch()</code>.</li>
 *   <li>The method <code>getResult</code> must return <i>null</i> if
 *       the task isn't finished (i.e. if the <code>finished()</code> 
 *       method returns <i>false</i>).</li>
 * </ul>
 * 
 * @author Boulanger Patrice
 * @see AbstractTask
 **/
public interface Task extends Serializable {
    
    /**
     * Call once and only once before the first execution.
     *
     * Note that this method is called once the task is on the platform. 
     * In consequence, important initializations should be made here to 
     * reduce the load on the client and to reduce the sizeof the tasks 
     * to dispatch.
     *
     * @return true if no error, false otherwise.
     **/
    public boolean init();

    /**
     * Inform the task that it should start its computation.
     * 
     * This method must be synchrone (i.e. blocking).
     *
     * @see #suspend()
     **/
    public void launch();

    public void compute();

    /**
     * Inform the task that it should stop its computation.
     *
     * @return true if no error, false otherwise.
     * @see #launch()
     **/
    public boolean suspend();

    /**
     * Ask to the task if it must die, according to the value of the 
     * object <code>cond</code>.
     *
     * @param cond an object used by the task to decide if it must die.
     */
    public boolean mustDie(Object cond);

    /**
     * Kill the task.
     */
    public void die();
   
    /**
     * Return the result of the task.
     *
     * @return the result of the task.
     * @see Result 
     **/
    public Result getResult();

    /**
     * Determines if this task is finished.
     *
     * @return true if the task is finished.
     * @see #launch() 
     * @see #suspend()
     **/
    public boolean finished();

    /**
     * Returns the furtherance factor of this task.
     *
     * @return the furtherance factor.
     **/
    public double percent();

    /**
     * Return the factory identificator to which this task belong to.
     *
     * @return the factory identificator of this task.
     * @see #setFactoryID(String)
     **/
    public String getFactoryID();

    /**
     * Set the factory identificator of this task.
     *
     * @param id the factory identificator of this task.
     * @see #getFactoryID
     **/
    public void setFactoryID(String id);

    /**
     * Return the task identificator of this task.
     *
     * @return the task identificator of this task.
     **/
    public String getTaskID();

    /**
     * Set the agent holding this task.
     */
    public void setAgent(Agent agent);

    /**
     * Get timeout in seconds.
     */
    public int getTimeout();   
}
