
/**
 * BossTeamSkill.java
 *
 *
 * Created: Wed Apr 12 21:22:22 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.magique;

import fr.lifl.magique.*;
import fr.lifl.magique.util.*;
import fr.lifl.magique.skill.*;
import java.util.*;


/** the main fonctionnality for being a Magique agent : all what is required to manage team and boss */
public class BossTeamSkill extends MagiqueDefaultSkill {
    

    /** my supervisor name */
    private String myBoss = "";
    /** my team 
     * @see fr.lifl.magic.util.Team
     */
    private Team myTeam = null;    

    private Object monitor = new Object();


    public BossTeamSkill(Agent agent) {
	super(agent);
    }    


    /** @return my boss name */
    public String getMyBoss() { 
	return(myBoss); 
    }

    /** sets the name of my boss
     * @param myBoss the name of my boss
     */
    public void setMyBoss(String myBoss) { 
	this.myBoss = myBoss; 
	Agent.verbose(3,getName()+" * set my boss * "+myBoss); 
	if (!isBigBoss()) {
	    updateTeam();
	}
    }    

    /** @return my team */
    public Team getMyTeam() { return(myTeam); }


    /** returns all the names known in my team 
     * @return  all the names known in my team 
     */
    public Vector knownNames() {
	synchronized (monitor) {
	    if (myTeam != null) {
		return getMyTeam().getNames();
	    }
	    else {
		return new Vector();
	    }
	}
    }
    /** returns all the methods known in my team
     * @return all the methods known in my team
     */
    public Vector knownMethods() {
	synchronized (monitor) {
	    Vector allMethods = getMyAgent().myMethodsNames();
	    if (myTeam != null) {
		for(Enumeration enu=getMyTeam().getMethods().elements();enu.hasMoreElements();) {
		    String method = (String) enu.nextElement();
		    if (!allMethods.contains(method))
			allMethods.addElement(method);
		}
	    }
	    return allMethods;
	}
    }    



    //
    //  boss and team
    //
    /** no boss ? i am the big boss
     * @return <em>true</em> if no supervisor
     */
    public boolean isBigBoss () { 
	return(myBoss.equals("")); 
    }
    /** i have a team ? then i am a boss
     * @return <em>true</em> if i am a boss
     */
    public boolean isBoss () { 
	return ((myTeam != null) && (myTeam.getTeamSize() != 0));
    }
    
    /** if i have no team i am a basic agent
     * @return <em>true</em> if no team
     */
    public boolean isBasis () { return(myTeam == null); }
    /** returns size of my team
     * 
     *@return size of my team */
    private int teamSize () { return(myTeam.size()); }
    /** add an agent to <em>myTeam</em>. If I am big Boss, unsent requests
     * are considered, maybe the new fellow can handle some of them.
     * @param agent the full name of the added agent
     * @param info info about the added agent : who he knows and what he can do
     * @return no return value
     */
    public void addToMyTeam(String agent,TeamInfo info) {
	Agent.verbose(3,getName()+" addToMyTeam "+agent);
	synchronized (monitor) {
	    if (myTeam==null) myTeam = new Team();
	    myTeam.newMember(agent,info);
	    //	    changeTeam(agent,info);	
	    if (!isBigBoss()) {
		perform(myBoss,"newMember",getName(), agent,info);	    
	    }
	    else { // I am big boss, what about unsent requests ?
		getMyAgent().treatUnsentRequests(agent,info.getMethods());
	    }
	}
    }

    /** remove <em>agentName</em> from my team. He MUST be in my team
     * : no test
     *
     * @param agentName : the name of the agent to remove
     */
    public  void removeFromMyTeam(String agentName) {
	synchronized (monitor) {
	    Agent.verbose(3,getName()+" remove "+ agentName +" from my team");	    
	    myTeam.remove(agentName);
	    if (!isBigBoss()) {
		updateTeam();
	    }
	}
    }

    /** my team has changed, i must advise my boss, it is assumed I am not Big boss
     */
    public void updateTeam() {
	synchronized (monitor) {
	    TeamInfo teamInfo = new TeamInfo(knownNames(),
					     knownMethods());
	    perform(myBoss,"changeTeam",getName(),teamInfo);		
	}
    }

    /** tells me that someone in my team has a new member in its team...
     * informations must be updated in the team
     *
     * @param agent the agent of my team
     * @param member the new member of the team of <em>agent</em>
     * @param memberInfo the info about this member : who he know and 
     *         what he can do
     * @return no return value
     */
    public void newMember(String agent, String member, TeamInfo memberInfo) {
	synchronized (monitor) {
	    myTeam.addInfo(agent,member,memberInfo);	
	    if (!isBigBoss()) {
		//	    updateTeam();
		perform(myBoss,"newMember",getName(), agent,memberInfo);	     
	    }
	    else {
		getMyAgent().treatUnsentRequests(agent, memberInfo.getMethods());
	    }
	}    
    }

    /** tells me that someone in my team has new methods (or new skill)
     * informations must be updated in the team
     *
     * @param agent the agent of my team who has new skills
     * @param subAgent the agent in the team of <em>agent</em> who has new skills
     * @param member the new member of the team of <em>agent</em>
     * @param memberInfo the info about this member : who he know and 
     *         what he can do
     * @return no return value
     */
    public void newMethods(String agent, String subAgent, Vector newMethods) {
	//	System.out.println("newMethods "+ agent +" #################");	
	synchronized (monitor) {
	    myTeam.addMethods(agent,newMethods);
	    getMyAgent().treatUnsentRequests(subAgent,newMethods);
	    if (!isBigBoss()) {
		perform(myBoss,"newMethods",getName(),subAgent,newMethods);
	    }
	}    
    }

    /** modifies the value of the teaminfo associated to
     * <em>agent</em> (an agent of my team), it becomes <em>info</em>
     * If agent was not previously known, it is added.
     *
     * @param agent the agent of my team 
     * @param info the new team info of this agent
     * */
    public void changeTeam(String agent, TeamInfo info) {
	synchronized (monitor) {
	    Agent.verbose(3,getName()+" changeTeam : "+agent);
	    myTeam.newMember(agent,info);
	    if (!isBigBoss()) {
		updateTeam();
	    }
	}
    }

} // BossTeamSkill
