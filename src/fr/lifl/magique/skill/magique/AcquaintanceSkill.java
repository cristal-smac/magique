
/**
 * AcquaintanceSkill.java
 *
 *
 * Created: Mon Oct 22 09:00:50 2001
 *
 * @author <a href="mailto:routier@lifl.fr">Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.skill.magique;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.util.*;

import java.util.*;

public class AcquaintanceSkill extends MagiqueDefaultSkill{

    /** the table of acquantainces for specific methods 
     */
    //    protected AcquaintanceTable acquaTable = new AcquaintanceTable(new DefaultAnswerLogFactory());
    protected AcquaintanceTable acquaTable = new AcquaintanceTable(new NoLogAnswerLogFactory());


    public AcquaintanceSkill (Agent myAgent){
	super(myAgent);
    }
    /** need a wrapper to pass the factory */
    public void changeAnswerLogFactory(AnswerLogFactoryWrapper factoryWrapper) {
	acquaTable.setAnswerLogFactory(factoryWrapper.getAnswerLogFactory());
    }

    public Boolean hasAcquaintance(String signature) {
	return new Boolean(acquaTable.hasAcquaintance(signature));
    }
    
    public String getAcquaintance(String signature) {
	return acquaTable.getAcquaintance(signature);
    }
    
    public Boolean isAcquaintanceCreationPossible(String signature, String agent) {
	return new Boolean(acquaTable.isAcquaintanceCreationPossible(signature,agent));
    }

    public Boolean isLearningAccurate(String signature, String agent) {
	return new Boolean(acquaTable.isLearningAccurate(signature,agent));
    }

    public void createAcquaintance(String signature, String agent) {
	acquaTable.createAcquaintance(signature, agent);
	connectTo(agent);
    }

    public void updateAcquaintance(String signature, String agent) {
	acquaTable.update(signature,agent); 	
    }

    public void removeAgentFromAcquaintances(String agent) {
	acquaTable.removeAgentFromAcquaintances(agent);
    }

}// AcquaintanceSkill
