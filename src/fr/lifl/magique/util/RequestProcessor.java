/**
 * RequestProcessor.java
 *
 *
 * Created: Mon Jan 18 18:22:39 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.util;
import fr.lifl.magique.*;

/** this is simply use to treat a given request in a thread. Each
 * request is run in a separate thread which is instantiated by siuch
 * an object.
 *
 * @see java.lang.Runnable
 */
public class RequestProcessor implements Runnable {
    
    /* the object that must execute request */
    private AbstractAgent agent;
    /* the request to execute */
    private Request request;

    /**
     * @param agent the object that must execute request
     * @param request the request to execute
     */
    public RequestProcessor(AbstractAgent agent, Request request) {
	this.agent = agent;
	this.request = request;
    }
    
    /** treats the request */
    public void run() {
	//	System.out.println("**********"+agent.getName()+"  "+request);	
	agent.processRequest(request);
    }
    
} // RequestProcessor
