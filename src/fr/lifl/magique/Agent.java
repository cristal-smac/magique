/**
 * Agent.java
 * <p>
 * <p>
 * Created: Tue Apr 20 15:34:07 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique;

import fr.lifl.magique.skill.MagiqueActionSkill;
import fr.lifl.magique.util.MessageList;
import fr.lifl.magique.util.Name;
import fr.lifl.magique.util.Team;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class defines particular agent : the <b>Magique </b> agent. They are
 * defined through a refinement of the <tt>AtomicAgent</tt> simply by changing
 * the skills known at creation.
 *
 * This class allows to create basic agent (specialist) and supervisor (leader
 * of group). Agents are organized via a hierarchic structure: a supervisor
 * manages some other agents (basic or other supervisors - with own team) called
 * its <em>team</em>. A team contains inofrmation about its memeber
 * abilities.
 *
 * Task achievement can require ability/skills that agent does not have.
 * Therefore the agent must ask someone else to achieve it. Then to achieve a
 * task following process is applied :
 * <ul>
 *
 * <li>agent has the ability, it uses it to achieve task</li>
 * <li>else, it is a supervisor and someone in its team has the ability to do
 * it, it forwards task to this one</li>
 * <li>else, agent forward task achievement to its supervisor if it is at the
 * top of hierarchy, task achievement must wait that new agent join the system
 * </li>
 * </ul>
 *
 * Here messages are in fact <em>Request</em> objects. The <tt>text</tt>
 * member of a request object is a name of a method (args can be provided).
 * Therefore, an agent can "ask" another agent to achieve a "method" for him.
 *
 * <b>Agent creation </b>: Creating an agent looks like scripting. This is due
 * to dynamic skill learning. Here is to what agent creation looks like:
 *
 * <br>
 * <tt>
 * import fr.lifl.magique.*;<br>
 * ...<br>
 *   Agent myAgent = new Agent("myName");     // agent creation <br>
 *   platform.addAgent(myAgent);              // agent subscribes to a defined platform<br>
 *<br>
 *   myAgent.addSkill(new SkillOne());        //<br>
 *   myAgent.addSkill(new SkillTwo());        // skills learning<br>
 *   myAgent.addSkill(new SkillThree());      //<br>
 *   myAgent.setAction(new MyAgentAction());  // proactive part setting<br>
 * <br>
 *    myAgent.connectToBoss("bossName@host.domain.country:4444");  // join a hierarchy (MAS)<br>
 *   myAgent.start();                         // proactive behaviour is launched<br>
 * ...<br>
 * </tt>
 *
 * Some othe properties can be set (e.g. the policy for concurrent request :
 * <tt>setConcurrencyPolicy</tt>
 *
 * @see fr.lifl.magique.Agent
 * @see fr.lifl.magique.Skill
 * @see fr.lifl.magique.skill.MagiqueDefaultSkill
 * @see fr.lifl.magique.skill.magique.BossTeamSkill
 * @see fr.lifl.magique.platform.Platform
 * @see fr.lifl.magique.Request
 * @author JC Routier routier@lifl.fr
 */

public class Agent extends AtomicAgent {

    //    protected Object monitor = new Object();

    //      /** the policy for concurrency */
    //      private ConcurrencyPolicy myConcurrencyPolicy = new
    // DefaultConcurrencyPolicy();

    // CONSTRUCTORS

    /** */
    public Agent() {
        super();
    }

    /**
     * @param name =
     *           shortname : identifier for my name (platform hostname and port
     *           number are added)
     *
     */
    public Agent(String name) {
        super(name);
    }

    /** @return my unsent requests list */
    protected MessageList getUnsentRequests() {
        return (unsentRequests);
    }

    /**
     * defines the skills known by the agent at creation.
     *
     * @exception SkillAlreadyAcquiredException
     *               if agent tries to learn an already known skill
     */
    protected void initBasicSkills() throws SkillAlreadyAcquiredException {
        this.addSkill(new fr.lifl.magique.skill.magique.BossTeamSkill(this));
        this.addSkill(new fr.lifl.magique.skill.system.ConnectionSkill(this));
        this.addSkill(new fr.lifl.magique.skill.magique.ConnectionToBossSkill(this));
        this.addSkill(new fr.lifl.magique.skill.magique.KillSkill(this));
        this.addSkill(new fr.lifl.magique.skill.system.DisplaySkill());
        this.addSkill(new fr.lifl.magique.skill.system.AddSkillSkill(this));
        this.addSkill(new fr.lifl.magique.skill.system.LearnSkill(this));
        this.addSkill(new fr.lifl.magique.skill.system.ForgetSkill(this));
    }

    // transition avec la BossTeamSkill
    //    protected boolean truc = true;
    protected boolean isBigBoss() {
        //  	if (truc) {
        //  	    truc = false;
        //  	    return (true);
        //  	}
        //  	else {
        return (((Boolean) askNow("isBigBoss")).booleanValue());
        //	}
    }

    protected boolean isBoss() {
        return (((Boolean) askNow("isBoss")).booleanValue());
    }

    protected boolean isBasis() {
        return (((Boolean) askNow("isBasis")).booleanValue());
    }

    protected Team getMyTeam() {
        return ((Team) askNow("getMyTeam"));
    }

    protected String getMyBoss() {
        return ((String) askNow("getMyBoss"));
    }

    //

    //       * @deprecated use addSkill(java.lang.String) or addSkill(java.lang.String,
    // Object[]) now
    //       *
    //       * @see #addSkill(jgava.lang.String)
    //       * @see #addSkill(java.lang.String, Object[])

    /**
     * Add all the methods of the object newSkill to the agent
     *
     *
     * @param newSkill
     *           the new skills to be added. Each method of the object newSkill,
     *           is added to the agent.
     *
     * @exception SkillAlreadyAcquiredException
     *               if <tt>newSkill</tt> contains a method with the same
     *               signature as another already known
     *
     */
    public void addSkill(String n) throws SkillAlreadyAcquiredException {
        super.addSkill(n);
    }

    public void addSkill(Object newSkill) throws SkillAlreadyAcquiredException {
        synchronized (monitor) {
            verbose(3, getName() + " addSkill " + newSkill.getClass().getName());
            Method[] tabOfMethods = newSkill.getClass().getDeclaredMethods();
            // JC : si d�j� une instance de la m�me classe connue, inutile de continuer
            String newSkillClassName = newSkill.getClass().getName();
            for (Iterator it = this.allKnownSkills.iterator(); it.hasNext(); ) {
                if (newSkillClassName.equals(it.next().getClass().getName())) {
                    verbose(3, getName() + " skill " + newSkill.getClass().getName() + " already knwon");
                    throw new SkillAlreadyAcquiredException(newSkillClassName);
                }
            }
            //----
            for (int m = 0; m < tabOfMethods.length; m++) {
                if (tabOfMethods[m].getModifiers() == Modifier.PUBLIC) {
                    String methodName = tabOfMethods[m].getName() + "(";
                    Class[] parameters = tabOfMethods[m].getParameterTypes();
                    for (int i = 0; i < parameters.length; i++) {
                        methodName += parameters[i].getName();
                        if ((i + 1) < parameters.length) {
                            methodName += ", ";
                        }
                    }
                    methodName += ")";
                    if (mySkills.containsKey(methodName)) {
                        throw new SkillAlreadyAcquiredException(methodName);
                    }
                }
            }
            this.allKnownSkills.add(newSkill);
            Vector newMethods = new Vector();
            for (int m = 0; m < tabOfMethods.length; m++) {
                if (tabOfMethods[m].getModifiers() == Modifier.PUBLIC) {
                    String methodName = tabOfMethods[m].getName() + "(";
                    Class[] parameters = tabOfMethods[m].getParameterTypes();
                    for (int i = 0; i < parameters.length; i++) {
                        methodName += parameters[i].getName();
                        if ((i + 1) < parameters.length) {
                            methodName += ", ";
                        }
                    }
                    methodName += ")";
                    mySkills.put(methodName, newSkill);
                    newMethods.addElement(methodName);
                }
            }
            if (!isBigBoss()) {
                perform(getMyBoss(), "newMethods", getName(), getName(), newMethods);
            } else {
            }
        }
    }

    /**
     * Remove the "key" from the hashtable of skills. This method removes all the
     * method learned at the same time than key (that means the "full" skill
     * which contains "key")
     *
     * @param key
     *           a string representing the method's signature to be removed.
     *
     * @exception SkillNotKnownException
     *               if <tt>key</tt> is not a knwon method signature
     */
    public void removeSkill(String key) throws SkillNotKnownException {
        verbose(3, getName() + " removeSkill " + key);
        synchronized (monitor) {
            super.removeSkill(key);
            if (!isBigBoss()) {
                askNow("updateTeam");
            }
        }
    }

    /**
     * connect this agent to another one as his boss
     *
     * @param bossName
     *           the name of my boss (of the form "name@hostname:rmiport")
     */
    public void connectToBoss(String bossName) {
        askNow("connectionToBoss", new Object[]{bossName});
    }

    //
    // requests : in and out
    //
    public void send(String to, Request request) {
        send(to, (Message) request);
    }

    /**
     * send a request to <em>to</em> through the platform if needed If
     * <em>to</em> is unknown, boss is asked to forward <em>request</em>.
     *
     * @param to
     *           name of recipient (name can be short name)
     * @param request
     *           request to be sent
     * @see java.io.Serializable
     */
    public void send(String to, Message msg) {
        Request request = (Request) msg;

        if (to.equals(getName()) || to.equals(Name.getShortName(getName()))) {
            processRequest(request);
        } else {
            // got the "true name"
            String fullName = getAgenda().getFullName(to);

            if (!fullName.equals(Name.getShortName(fullName))) {
                try {
                    String agentShortName = Name.getShortName(fullName);
                    String agentHost = InetAddress.getByName(Name.getHostName(fullName)).getHostAddress();
                    InetAddress address = InetAddress.getByName(agentHost);
                    fullName = agentShortName + "@" + agentHost + ":" + Name.getPort(fullName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                fullName = fullName + "@" + Name.getHostName(getName()) + ":" + Name.getPort(getName());
            }

            verbose(5, getName() + ": " + "i send " + request.getName() + ":" + request.getText() + " to " + fullName);
            if (fullName.equals(getName())) {
                processRequest(request);
            } else {
                if (!getAgenda().containsKey(fullName)) {// if unknown then
                    if (isBoss() && getMyTeam().knownName(fullName)) {
                        String theAgent = getMyTeam().whoKnowsName(fullName);
                        if (!request.isAnAnswer() && !request.getSender().equals(getName())) {
                            request.addToPath(getName());
                        }
                        send(theAgent, createOrder("send", new Object[]{fullName, request}));
                    } else if (!isBigBoss()) { // ask to my boss if I am not big boss
                        if (!request.isAnAnswer() && !request.getSender().equals(getName())) {
                            request.addToPath(getName());
                        }
                        Object[] params = {fullName, request};
                        sendUp(createOrder("send", params));
                    } else { // else waits for "to" to become known
                        Agent.verbose(2, getName() + " ***** UNKNOWN REQUEST " + request.getText() + " - " + request.getSender() + " *****");
                        Object[] parameters = request.getParams();
                        for (int i = 0; i < parameters.length; i++) {
                            Agent.verbose(2, "                      params " + i + " : " + parameters[i]);
                        }
                        Agent.verbose(2, " *****     **********     ***********       *****");
                        unsentRequests.addMessage(createOrder("send", new Object[]{fullName, request}));
                    }
                } else {
                    if (!request.isAnAnswer() && !request.getSender().equals(getName())) {
                        request.addToPath(getName());
                    }
                    sendMessage(fullName, request);
                }
            }
        }
    }

    /**
     * sends a request to my boss (supervisor)
     *
     * @param request
     *           the request to be sent
     */
    protected void sendUp(Request request) {
        verbose(5, getName() + ": sendUp " + getMyBoss() + " " + request.getText());
        send(getMyBoss(), request);
    }

    /**
     * sends unsent requests to my boss, it is used just after connection to boss
     * has been performed
     */
    public void sendUnsentRequestToBoss() {
        MessageList tmp = (MessageList) unsentRequests.clone();
        for (Enumeration enu = tmp.elements(); enu.hasMoreElements(); ) {
            Request request = (Request) enu.nextElement();
            sendUp(request);
            unsentRequests.removeMessage(request);
        }
    }

    /**
     * broadcasts an order (request without answer) to a basic agent of my
     * hierarchy. Broadcasting question (request with answer) leads to
     * unpredictable behaviour.
     *
     * @param request
     *           the sent request
     */
    public void broadcastToBasis(Request request) {
        /**
         * si je suis de la base : j'execute la requete, sinon je la transmets a
         * mon equipe
         */
        verbose(3, getName() + ": " + "**** i broadcastToBasis " + request.getText() + " ");
        if (isBasis()) {
            processRequest(request);
        } else {
            for (Enumeration team = getMyTeam().getMembers(); team.hasMoreElements(); ) {
                String agent = (String) team.nextElement();
                // si je ne suis pas l'initiateur de request je m'ajoute au path
                if (!request.getSender().equals(getName()))
                    request.addToPath(getName());
                Object[] params = {request.clone()};
                send(agent, createOrder("broadcastToBasis", params));
            }
        }
    }

    /**
     * broadcasts an order (request without answer) to everybody under me.
     * Broadcasting question (request with answer) leads to unpredictable
     * behaviour.
     *
     * @param request
     *           the sent request
     */
    public void broadcastToAll(Request request) {
        /**
         * si je suis chef : je transmets la requete a mon equipe. La requete est
         * executee par tous, sf par l'initiateur
         */
        verbose(3, getName() + ": i broadcastToAll " + request.getName() + ":" + request.getText());
        if (isBoss())
            for (Enumeration team = getMyTeam().getMembers(); team.hasMoreElements(); ) {
                String agent = (String) team.nextElement();
                // si je ne suis pas l'initiateur de request je m'ajoute au path
                if (!request.getSender().equals(getName()))
                    request.addToPath(getName());
                Object[] params = {request.clone()};
                send(agent, createOrder("broadcastToAll", params));
            }
        if (!request.getSender().equals(getName())) { // je ne suis pas
            // l'initiateur ?
            if (isBoss()) {
                request.removeLastFromPath(); // parce que modifie ci-dessus
            }
            processRequest(request);
        }
    }

    /**
     * treat a request : if the agent knows the method, he applies it, else if
     * someone in his team knows the method, he sends him the request, else he
     * sends this to its supervisor
     *
     * @param request
     *           the request to be treated
     * @return no return value
     */
    public void perform(Request request) {
        String method = request.getSignature();
        verbose(4, "perform : " + getName() + ": i do " + request.getName() + ":" + method);

        if (myMethodsNames().contains(method)) {
            interprete(request);
            // si c'est une question a laquelle je viens de repondre,
            // je retransmets la reponse
            if (request.getIsQuestion())
                send(request.lastSender(), request);
        } else if (isBoss() && getMyTeam().knownMethod(method)) {
            Vector theAgents = getMyTeam().whoKnowsMethod(method);
            String agent = (String) theAgents.firstElement();
            send(agent, request);
        } else {
            if (isBigBoss()) { // nobody can answer this request
                Agent.verbose(2, request.getSender() + " ***** UNKNOWN REQUEST " + request.getText() + " *****");
                Object[] parameters = request.getParams();
                if (parameters.length == 0) Agent.verbose(2, "  ***** no params");
                for (int i = 0; i < parameters.length; i++) {
                    Agent.verbose(2, "                      params " + i + " : " + parameters[i]);
                }
                Agent.verbose(2, " *****     **********     ***********       *****");
                unsentRequests.addMessage(request);
            } else { // maybe my Boss knows who can answer this request ?
                sendUp(request);
            }
        }
    }

    /**
     * treat a concurrent request : if the agent knows the method, he applies it,
     * else he sends the request to all those in his team who knows the method,
     * else he sends it to its supervisor
     *
     * @param request
     *           the request to be treated
     * @return no return value
     */
    public void concurrentPerform(Request request) {
        String method = request.getSignature();
        verbose(2, getName() + ": " + "i concurrentPerform " + request.getName() + ":" + request.getText());
        if (myMethodsNames().contains(method)) {
            interprete(request);
            // si c'est une question a laquelle je viens de repondre,
            // je retransmets la reponse
            if (request.getIsQuestion())
                send(request.lastSender(), request);
        } else if (isBoss() && getMyTeam().knownMethod(method)) {
            Vector theAgents = getMyTeam().whoKnowsMethod(method);
            for (Enumeration agent = theAgents.elements(); agent.hasMoreElements(); ) {
                send((String) agent.nextElement(), (Request) request.clone());
            }
        } else {
            if (isBigBoss()) { // nobody can answer this request
                getUnsentRequests().addMessage(request);
            } else { // maybe my Boss knows who can answer this request ?
                sendUp(request);
            }
        }
    }

    /**
     * changes the <tt>action</tt> skill that corresponds to the proactive part
     * of the agent
     *
     * @param action
     *           the new action skill, it must inherit <tt>ActionSkill</tt>
     *
     * @see fr.lifl.magique.skill.ActionSkill
     */
    public void setAction(MagiqueActionSkill action) {
        try {
            removeSkill("action()");
        } catch (SkillNotKnownException e) {
            //	    e.printStackTrace();
        }
        addSkill(action);
    }

} // Agent

