
/**
 * Communicate.java
 *
 *
 * Created: Fri Jan 21 09:52:57 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.platform.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import fr.lifl.magique.Message;

/** rmi remote interface for "message sending" between platform
 */

public interface Communicate extends Remote {

    //    public void haveAMessage(Message msg) throws RemoteException;
    public void haveAMessage(byte[] msg) throws RemoteException, ClassNotFoundException;
    
} // Communicate
