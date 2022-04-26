/**
 * Communicate.java
 * <p>
 * <p>
 * Created: Fri Jan 21 09:52:57 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.platform.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** rmi remote interface for "message sending" between platform
 */

public interface Communicate extends Remote {

    void haveAMessage(byte[] msg) throws RemoteException, ClassNotFoundException;

} // Communicate
