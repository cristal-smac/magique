/**
 * PlatformMessageProcessor.java
 *
 *
 * Created: Mon Jan 18 18:22:39 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.platform;


/** this is simply use to treat a given PlatformMessage in a thread. Each
 * PlatformMessage is run in a separate thread which is instantiated by siuch
 * an object.
 *
 * @see java.lang.Runnable
 */
public class PlatformMessageProcessor implements Runnable {
    
    /* the object that must execute PlatformMessage */
    private Platform platform;
    /* the PlatformMessage to execute */
    private PlatformMessage platformMessage;

    /**
     * @param agent the object that must execute platformMessage
     * @param platformMessage the platformMessage to execute
     */
    public PlatformMessageProcessor(Platform platform, PlatformMessage platformMessage) {
	this.platform = platform;
	this.platformMessage = platformMessage;
    }
    
    /** treats the PlatformMessage */
    public void run() {
	platform.treatMessage(platformMessage);
    }
    
} // RequestProcessor
