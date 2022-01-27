/**
 * Team.java
 *
 *
 * Created: Fri Oct 16 16:02:45 1998
 *
 * @author Jean-Christophe Routier
 * @version
 */

package fr.lifl.magique.util;
import fr.lifl.magique.*;
import java.util.*;

/** maps a member of the team (identified by his name) with a TeamInfo
 * object that contains two vectors : the members of the subteams and
 * the vector of what they are able to do. 
 *  @author JC Routier routier@lifl.fr 
 * @see java.util.Hashtable 
 * @see fr.lifl.magique.util.TeamInfo */
public class Team extends Hashtable {

    //    private Object monitor = new Object()
    
    public Team() {	
	super();
    }

    /** returns the <em>TeamInfo</em> associated to an <em>agent</em>
     * @param agent name of the agent
     * @return the associated <em>TeamInfo</em>
     * @exception java.lang.NullPointerException si <em>agent</em> est 
     * inconnu
     * @see fr.lifl.magique.util.TeamInfo
     * @exception NullPointerException if agent is unknown
     */
    public TeamInfo getTeamInfo (String agent) {
	if (containsKey(agent))
	    return ((TeamInfo) get(agent));
	else throw new NullPointerException();
    }   

    /** returns the (first level) members of the team 
     * @return the (first level) members of the team 
     */
    public Enumeration getMembers () { return keys(); }
    /** Returns the size of this tem ie the number of first level members 
     * @return the size of this tem ie the number of first level members 
     */
    public int getTeamSize() { return size(); }

    /** adds an agent and its TeamInfo info to the team
     * @param agent the name of the agent to add
     * @param info the associated TeamInfo object
     */
    public synchronized void newMember(String agent,TeamInfo info) {
	//System.out.println(" ******************** newMember in "); 
	put(agent,info);
	//System.out.println(" ******************** newMember out "); 
    }
    

    /** remove an agent from my team
     *
     * @param agent the agent to remove from my team
     */
    public synchronized void remove(String agent) {
//System.out.println(" ******************** remove in "); 
	super.remove(agent);
//System.out.println(" ******************** remove out "); 
    }

    /** updates info associated to the agent  when a new
     * member is added in his subteam
     *
     * @param agent the name of the agent
     * @param newMember the new member of the subteam
     * @param memberInfo the team info associated to the new member */
    public synchronized void addInfo(String agent,String newMember, TeamInfo memberInfo) {
	//System.out.println(" ******************** addInfo in "); 
	TeamInfo agentInfo = getTeamInfo(agent);
	agentInfo.addName(newMember);	
	agentInfo.addNames(memberInfo.getNames());	
	addMethods(agent, memberInfo.getMethods());
	//System.out.println(" ******************** addInfo out "); 
    }

    /** updates info associated to the agent  when a 
     * member has new methods (skills)
     *
     * @param agent the name of the agent who has lerned
     * @param newMethods vector of the new methods to be added to info on <em>agent</em>
     */
    public synchronized void addMethods(String agent, Vector newMethods) {
	//System.out.println(" ******************** addMethods in "); 
	TeamInfo agentInfo = getTeamInfo(agent);
	agentInfo.addMethods(newMethods);	
	//System.out.println(" ******************** addMethods out "); 
    }

    /** returns the vector of all the names of agent appearing in this
     * team and the relative TeamInfo. Without repetition if any.
     * @return the vector of all the names of agent appearing in this
     * team and the relative TeamInfo 
     */
    public synchronized Vector getNames() {
//System.out.println(" ******************** getNames in"); 
	Vector allNames = new Vector();
	for(Enumeration members = getMembers();members.hasMoreElements();)
	    allNames.addElement(members.nextElement());	    
	for(Enumeration enu = elements();enu.hasMoreElements();) {	    
	    for(Enumeration e = ((TeamInfo) enu.nextElement()).getNames().elements(); 
		e.hasMoreElements();) {
		String name = (String) e.nextElement();
		if (!allNames.contains(name))
		    allNames.addElement(name);
	    }
	}
//System.out.println(" ******************** getNames out"); 
	return allNames;
    }
    /** returns the vector of all the names of methods appearing in this
     * team and the relative TeamInfo. Without repetition if any.
     * @return the vector of all the names of methods appearing in this
     * team and the relative TeamInfo 
     */
    public synchronized Vector getMethods() {
//System.out.println(" ******************** getMethods in"); 
	Vector allMethods = new Vector();
	for(Enumeration enu = elements();enu.hasMoreElements();) {
	    for(Enumeration e = ((TeamInfo) enu.nextElement()).getMethods().elements(); 
		e.hasMoreElements();) {
		String method = (String) e.nextElement();
		if (!allMethods.contains(method))
		    allMethods.addElement(method);
	    }
	}
//System.out.println(" ******************** getMethods out"); 
	return allMethods;
    }
    /** returns <em>true</em> if agent appears somewhere in this team
     * (maybe in a subteam)
     * 
     * @param name the name of the searched agent
     * @return <em>true</em> if agent appears somewhere in this team 
     * (maybe in a subteam) */
    public boolean knownName(String name) {
	// peut etre "accelere" si on utilise pas la fonction getNames
	return(getNames().contains(name));
    }
    /** @param name the name of the searched method
     * @return <em>true</em> if method appears somewhere in this team
     * (maybe in a subteam)
     */
    public boolean knownMethod(String method) {
	return(getMethods().contains(method));
    }
    /** returns the vector of the names of the agents (of the first
     * level of hierarchy below) who are able to perform the given
     * method (or maybe someone below it). It is assumed that at list
     * one agent in the team (or subteams) is candidate.
     *
     * @param method the name of the method to look for
    
     * @return the vector of the names of the agents of the team who
     * are able to perform the given method */
    public Vector whoKnowsMethod(String method) {
	String agent = new String();
	Vector theAgents = new Vector();
	
	for (Enumeration enu = keys();enu.hasMoreElements();) {
	    agent = (String) enu.nextElement();
	    if (getTeamInfo(agent).getMethods().contains(method))
	       theAgents.addElement(agent);		
	}
	return(theAgents);
    }
    /** returns the name of the agent (first level of hierarchy below)
     * who knows the given name (which can be in a "sublevel"). It is
     * assumed that the name is necesseraly known.
     *
     * @param name the name to look for 
     * @return the name of the agent of the team who knows the given
     * name */
    public String whoKnowsName(String name) {
	String agent = new String();
	String theAgent = new String();

	for (Enumeration enu = keys();enu.hasMoreElements();) {
	    agent = (String) enu.nextElement();
	    if (getTeamInfo(agent).getNames().contains(name))
	       theAgent = agent;
	}
	return(theAgent);
    }

} // Team


