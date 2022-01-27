/**
 * MessageList.java
 *
 *
 * Created: Wed Jun 16 09:31:12 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.util;

import java.util.*;
import fr.lifl.magique.Message;

/** a vector of messages...
 * @see fr.lifl.magique.Message
 */
public class MessageList extends Vector {
    
    public MessageList() {
    }
    
    /** add the specified message at the end
     * @param msg the message to add
     */
    public synchronized void addMessage(Message msg) {
	addElement(msg);
	//	System.out.println("Notification");
	this.notify();
    }
    /** REMOVE and return first element
     * @return the first request
     */
    public synchronized Message firstMessage () {
	try {
	    //	    System.out.println("On entre dans wait");
	    while (isEmpty()) { //  pour être sûr que le notify est justifie
		this.wait();
	    }
	    //	    System.out.println("On en sort !");
	} catch (InterruptedException e){
	    e.printStackTrace();
	}
	Message firstMessage = (Message) firstElement();
	removeMessage(firstMessage);
	return (firstMessage);
    }
    /** remove specified message
     * @param the message to be removed
     */
    public synchronized void removeMessage(Message msg) {	
	removeElement(msg);
    }    
} // MessageList
