
/**
 * AcquAgent.java
 *
 *
 * Created: Mon May 29 12:57:27 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique;

import fr.lifl.magique.util.*;
import java.util.*;
import java.net.*;

public class AcquAgent extends Agent {

	protected AgentPoolManager poolManager;

	public AcquAgent(String name) {
		super(name);
		poolManager = new AgentPoolManager(this);
	}

	/** defines the skills known by the agent at creation.
	 *
	 * @exception SkillAlreadyAcquiredException if agent tries to
	 * learn an already known skill 
	 */
	protected void initBasicSkills() throws SkillAlreadyAcquiredException {
		super.initBasicSkills();
		this.addSkill(new fr.lifl.magique.skill.magique.AcquaintanceSkill(this));
	}

	/** change hosts for pool
	 */
	public void setPoolHosts(String[] hosts) {
		poolManager.setPoolHosts(hosts);
	}

	/** treat a request : if the agent knows the method, he applies it,
	 * else if someone in his team knows the method, he sends him the
	 * request, else he sends this to its supervisor
	 *
	 * @param request the request to be treated 
	 * @return no return value
	 */
	public void perform(Request request) {
		String method = request.getSignature();
		verbose(4, "perform : " + getName() + ": i do " + request.getName() + ":" + method);

		if (myMethodsNames().contains(method)) {

			if (poolManager != null && poolManager.hasAPool(method)) {
				poolManager.getPool(method).processRequest(request);
			}
			else {
				interprete(request);
				if (request.getIsQuestion())
					send(request.lastSender(), request);

			}
			// si c'est une question a laquelle je viens de répondre, 
			// je retransmets la reponse
		}
		// specific to acquAgent
		else if (((Boolean) askNow("hasAcquaintance", new Object[] { request.getSignature()})).booleanValue()) {
			send((String) askNow("getAcquaintance", new Object[] { request.getSignature()}), request);
		}
		// end specific
		else if (isBoss() && getMyTeam().knownMethod(method)) {
			Vector theAgents = getMyTeam().whoKnowsMethod(method);
			String agent = (String) theAgents.firstElement();
			send(agent, request);
		}
		else {
			if (isBigBoss()) { // nobody can answer this request
				unsentRequests.addMessage((Message) request);
			}
			else { // maybe my Boss knows who can answer this request ?
				sendUp(request);
			}
		}
	}

	public void createNewPool(String skillName, String serviceName) {
		poolManager.createNewPool(skillName, serviceName, 3);
	}

	/** process an answer (to me)
	 * @param request the request (answer is included)
	 */
	protected void processAnswer(Request question) {

		// specific AcquAgent
		String signature = question.getSignature();
		String answerer = question.getAnswerer();
		if (!answerer.equals(getName())) {
			try {
				perform("updateAcquaintance", new Object[] { signature, answerer });
				if (((Boolean) askNow("isAcquaintanceCreationPossible", new Object[] { signature, answerer })).booleanValue()) {
					fr.lifl.magique.Agent.verbose(2, "acquaintance creation for " + signature + " with " + answerer);
					perform("createAcquaintance", new Object[] { signature, answerer });
				}
				if (((Boolean) askNow("isLearningAccurate", new Object[] { signature, answerer })).booleanValue()) {
					fr.lifl.magique.Agent.verbose(2, "learn " + signature + " from " + answerer);
					askNow("learnSkillFromSignature", new Object[] { signature, answerer });
				}
			}
			catch (NullPointerException e) {}
		}
		// end specific
		questionTable.setAnAnswer(question.getName(), question.getAnswer(), question.getAnswerer(),0);
	}

} // AcquAgent
