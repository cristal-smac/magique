/**
 * Agenda.java
 *
 *
 * Created: Mon Sep  7 11:33:24 1998
 *
 * @author Jean-Christophe.Routier
 * @version
 */
package fr.lifl.magique.util;

import java.util.*;

/** stores the agents i am connected to. The value in the Hashtabe
 *could be used to store informations about each agent.
 *
 * @author JC Routier routier@lifl.fr
 * @see java.util.Hashtable
 */
public class Agenda extends Hashtable {
  
    /** */
    public Agenda() {
	super();
    }
    /** gives the <em>value</em> associates to  <em>theOther</em>, this could be some informations about him
     * 
     * @param theOther name of an agent
     * @return the  <em>vallue</em> associates with  <em>theOther</em>
     * 
     */
    public Object getInfo (String theOther) {
	return  get(theOther);
    }   
    /** returns 
     * <UL> 
     * <LI> <em>name</em> if it is already a full name or not 
     * a known short name</LI> 
     * <LI> the (full name of the) key whose short name is <em>name</em> </LI>
     * </UL>
     *
     * @param name the (maybe short) name to search for
     * @return the full name relative to <em>name</em> as a key
     */
    public String getFullName(String name) {
	if (!containsKey(name)) {  // otherwise it is a full name
	    for(Enumeration fullNames = keys();fullNames.hasMoreElements();) {
		String aFullName = (String) fullNames.nextElement();
		if (fr.lifl.magique.util.Name.getShortName(aFullName).equals(name)) {
		    return(aFullName);
		}
	    }
	}
	return name;  // either name was already a full name or it is unknown
	              // as full or short name
    }

    /** supprime l'agent de l'agenda
     *
     * @param agent l'agent � supprimer
     */
    public void removeAgent(String agent) {
	remove(getFullName(agent));
    }
        
    /** give the nth key
     * @param int index of the key we are looking for
     * @return the nth key
     */  
    public String nTh (int index) {
	Enumeration enu = keys();
	for(int i=0;i<index;i++)
	    enu.nextElement();
	return (String) enu.nextElement();
    }
} // Agenda
