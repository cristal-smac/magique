/**
 * Listener.java
 * <p>
 * <p>
 * Created: Tue Jan 25 10:39:22 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.platform;

/** Listen to the messages received by the platform.
 *
 */

public class Listener extends Thread {

    private final Platform platform;
    private boolean stop = false;

    /** @param platform the platform i am the Listener of */
    public Listener(Platform platform) {
        this.platform = platform;
    }

    /** halts the Listener : no more messages are treated */
    public synchronized void halt() {
        stop = true;
        platform.getMessages().addMessage(null);
    }

    /** spies if messages are to be treated
     */
    public void run() {
        while (!stop) {
            PlatformMessage msg = (PlatformMessage) platform.getMessages().firstMessage();
            if (msg != null) {
                Thread t = new Thread(new PlatformMessageProcessor(platform, msg));
                t.start();
            }
        }
        fr.lifl.magique.AbstractAgent.verbose(2, "platform listener stopped");
    }


} // Listener
