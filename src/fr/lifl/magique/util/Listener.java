/**
 * Listener.java
 * <p>
 * <p>
 * Created: Tue Oct 20 13:00:33 1998
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.util;

import fr.lifl.magique.AbstractAgent;
import fr.lifl.magique.Agent;
import fr.lifl.magique.Request;

/** a <em>Listener</em> belongs to an agent(a
 * <em>AbstractAgent</em> object in fact). It checks when
 * requests arrive and execute them. It is a thread
 *
 * @see java.lang.Thread
 * @author JC Routier routier@lifl.fr */
public class Listener extends Thread {

    /** the agent I belongs to */
    private final AbstractAgent agent;

    private boolean stop = false;

    /** @param agent the agent I belongs to*/
    public Listener(AbstractAgent agent) {
        this.agent = agent;
    }

    /** returns the agent I belongs to
     * @return the agent I belongs to */
    public AbstractAgent getAgent() {
        return agent;
    }

    /** returns <em>true</em> if no request are waiting for treatment
     *    @return <em>true</em> if no request are waiting for treatment */
    private boolean toDoIsEmpty() {
        return agent.getToDo().isEmpty();
    }

    /** halts the Listener : no more requests are managed */
    public synchronized void halt() {
        stop = true;
        agent.getToDo().addMessage(null);
    }

    /** executes requests in to do list of my agent when they
     * arrive. each request is processed in a separate thread
     * (synchronization must then be set in methods if required)
     *
     * @return no return value 
     * @see fr.lifl.magique.util.RequestProcessor
     */
    public void run() {
        while (!stop) {
            Request request = (Request) agent.getToDo().firstMessage();
            if (request != null) {
                Thread t = new Thread(new RequestProcessor(agent, request));
                t.start();
            }
        }
        Agent.verbose(3, "listener halted");
    }

} // Listener
