/**
 * Connect.java
 * <p>
 * <p>
 * Created: Fri Jan 21 09:54:36 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */

package fr.lifl.magique.platform.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/** rmi remote interface for connection between platform */
public interface Connect extends Remote {

    void connect(String hostName) throws RemoteException;

    void disconnect(String hostName) throws RemoteException;

    Boolean ping(String hostName) throws RemoteException;

} // Connect
