/**
 * Name.java
 *
 *
 * Created: Wed Apr 21 10:39:04 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */

package fr.lifl.magique.util;
import java.util.*;
import java.net.*;

/** class with static methods used to manage name of the form
 * shortName@hostName:port 
 *
 * if no server socket is launch for an agent : port is replaced by a
 * negative numer. The unicity of name is not guaranteed if several
 * agent without serversocket are used in different JVM on the same
 * host */
public class Name {
    
    private static int cpt = -1;
    
    public Name() {	
    }

    /** extract the short name from name, name must be of the form
     *       shortname@hostName:PORT
     *
     * @param name the name to be parsed
     * @return the short name
     */
    public static String getShortName(String name) {
	StringTokenizer st = new StringTokenizer(name,"@");
	return(st.nextToken());
    } 
    /** extract the name of host machine, name must be of the form
     *       shortname@hostName:PORT
     *
     * @param name the name to be parsed
     * @return the host name
     */
    public static String getHostName(String name) {
	StringTokenizer st = new StringTokenizer(name,"@:");
	// to skip the short name
	st.nextToken();
	return(st.nextToken());
    }     
    /** extract the name of host machine, name must be of the form
     *       shortname@hostName:PORT
     *
     * @param name the name to be parsed
     * @return the port number
     */
    public static String getPort(String name) {
	StringTokenizer st = new StringTokenizer(name,"@:");
	// to skip the short name and the host name
	st.nextToken();
	st.nextToken();
	return(st.nextToken());
    }     
    /** extract the name and port of host machine (platform), name must be of the form
     *       shortname@hostName:PORT
     *
     * @param name the name to be parsed
     * @return the host name
     */
    public static String noShortName(String name) {
	StringTokenizer st = new StringTokenizer(name,"@");
	// to skip the short name
	st.nextToken();
	return(st.nextToken());
    }     

    
} // Name
