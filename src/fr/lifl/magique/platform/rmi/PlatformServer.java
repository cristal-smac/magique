
/**
 * PlatformServer.java
 *
 *
 * Created: Fri Jan 21 09:48:38 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.platform.rmi;

import java.rmi.*;
import java.rmi.server.*;
import fr.lifl.magique.platform.*;
import fr.lifl.magique.Message;

import fr.lifl.magique.util.Name;

import java.io.*;

/** The rmi server for the platform. It accepts 2 remote methods :
 * connection and message "sending" between platforms.
 * */
public class PlatformServer extends UnicastRemoteObject
    implements Communicate, Connect {
    
    private Platform platform;

    /** @param platform the platform i am the server of
     */
    public PlatformServer(Platform platform) throws RemoteException {
	super();
	this.platform = platform;
	
    }
    
    /** method remotely invoked by other platform to send a message to my platform
     *
     * @param msg the sent message     *
     * @return <em>Boolean.TRUE</em> iff alive
     *
     * @see fr.lifl.magique.platform.rmi.Communicate
     */
    //    public void haveAMessage(Message msg) {
    public void haveAMessage(byte[] bytesMsg) throws ClassNotFoundException{
	Message msg = null;
	try {
	    ByteArrayInputStream baiStream = new ByteArrayInputStream(bytesMsg);
	    ObjectInputStream oiStream = new ObjectInputStream(baiStream);
	    msg = (Message) oiStream.readObject();
	    platform.haveAMessage(msg);
	}
        catch(InvalidClassException e) {
	    System.out.println("**********  haveAmessage server ice");
	    //	    e.printStackTrace();
	    System.out.println("ice "+e.getMessage());	    
	}
        catch(StreamCorruptedException e) {
	    System.out.println("**********  haveAmessage server sce");
	    //	    e.printStackTrace();
	    System.out.println("sce "+e.getMessage());	    
	}         
	catch(OptionalDataException e) {
	    System.out.println("**********  haveAmessage server ode");
	    //	    e.printStackTrace();
	    System.out.println("ode "+e.getMessage());	    
	}
	catch(IOException e) {
	    System.out.println("**********  haveAmessage server ioe");
	    //	    e.printStackTrace();
	    System.out.println("ioe "+e.getMessage());	    
	}
//  	catch(Exception e) {
//  	    System.err.println("**********  haveAmessage server ee");
//  	    e.printStackTrace();
//  	    //	    System.err.println("bytesmsg "+bytesMsg+ " "+bytesMsg == null);	    
//  	//      System.err.println("msg "+msg);	    
//  //  	    System.err.println("e "+e.getMessage());	    
//  	}
    }


    /** test if the platform <em>hostname</em> is alive
     *
     * @param platformName the platform to test
     *
     * @see fr.lifl.magique.platform.rmi.Connect
     */
    public Boolean ping(String platformName) {
	try {
	    Connect theOther = (Connect) Naming.lookup("//"+platformName+"/PlatformServer");
	}
	catch(Exception e) {
	    return Boolean.FALSE;
	}
	return Boolean.TRUE;
    }
    
    /** method remotely invoked by other platform to connect to my platform
     *
     * @param platformName the name of the platform who wants to be connected to my platform
     *
     * @see fr.lifl.magique.platform.rmi.Connect
     */
    public void connect(String platformName) {
	if (platform.getPlatformAgenda().containsKey(platformName)) {
	    System.out.println(platformName+ " already known");	   
	}
	else {
	    System.out.println(platformName+ " not yet known");	   
	    boolean connected = false;
	    while (!connected) {
		try {
		    Communicate theOther = (Communicate) Naming.lookup("//"+platformName+"/PlatformServer");
		    platform.getPlatformAgenda().put(platformName,theOther);
		    Connect theOtherConnect = (Connect) Naming.lookup("//"+platformName+"/PlatformServer");
		    theOtherConnect.connect(platform.getName());
		    connected = true;
		    System.out.println("connection with "+platformName+" performed");
		}
		
		catch(Exception e) {
//  		    e.printStackTrace();
//  		    System.out.println("exception");		   
		    connected = false;
		}
	    }
	}	
    }

    /**  performs (rmi) disconnection fom <it>platformName</it>.
     */
    public void disconnect(String platformName) {
	try {
	    System.out.println(platform.getName()+" disconnect from "+platformName); 
	    Naming.unbind("//"+platformName+"/PlatformServer");
	    platform.getPlatformAgenda().remove(platformName);
	}
	catch(RemoteException e) {
	    e.printStackTrace();
	}
	catch(NotBoundException  e) {
	    e.printStackTrace();
	}
	catch(java.net.MalformedURLException e) {
	    e.printStackTrace();
	}
    }
    
} // PlatformServer
