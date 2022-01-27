
/**
 * PlatformMessage.java
 *
 *
 * Created: Tue Jan 25 10:44:42 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.platform;

import fr.lifl.magique.Message;
import java.io.*;

/** Specific messages between platforms : a name and a magique message
 *
 * @see fr.lifl.magique.Message
 */

public class PlatformMessage implements Message {

    private String recipient;
    private Message content;

    public PlatformMessage(String recipient, Message content) {
	this.recipient = recipient;
	this.content = content;
    }

    public String toString() {
	return "to "+recipient+" content "+content;
    }

    public String getRecipient() { return recipient; }
    
    public Message getContent() { return content; }

//      private void writeObject(ObjectOutputStream output) throws IOException {
//  	System.out.println("write "+this);	    
//  	output.defaultWriteObject();
//      }

//      private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
//  	try {
//  	    System.out.println("read "+this);	    
//  	    input.defaultReadObject();
//  	}
//  	catch (IOException e) {
//  	    e.printStackTrace();
//  	}
//  	catch (ClassNotFoundException e) {
//  	    e.printStackTrace();
//  	}
//      }

} // PlatformMessage
