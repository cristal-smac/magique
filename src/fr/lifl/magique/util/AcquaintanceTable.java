/**
 * AcquaintanceTable.java
 * <p>
 * <p>
 * Created: Mon Oct 08 10:57:41 2001
 *
 * @author <a href="mailto:routier@lifl.fr">Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/** table of who has answered to a given request (referenced by its signature
 */
public class AcquaintanceTable {

    protected Map answerTable;
    protected Map acquaintances;
    protected AnswerLogFactory answerLogFactory;

    public AcquaintanceTable(AnswerLogFactory answerLogFactory) {
        this.answerLogFactory = answerLogFactory;
        answerTable = new HashMap();
        acquaintances = new HashMap();
    }

    /** updates the table when answer has been received. New entry is
     * created if the signature was never encoutered before */
    public void update(String signature, String answerer) {
        if (answerTable.containsKey(signature)) {
            ((AnswerLog) answerTable.get(signature)).update(answerer);
        } else {
            AnswerLog log = createAnswerLog();
            answerTable.put(signature, log);
            log.update(answerer);
        }
    }

    /** returns <i>true</i> iff the criterion for the creation of an
     * acquaintance with <t>answerer</t> for the given signature is
     *   satisfied */
    public boolean isAcquaintanceCreationPossible(String signature, String answerer) {
        if (answerTable.containsKey(signature)) {
            if (hasAcquaintance(signature) && answerer.equals(getAcquaintance(signature))) {
                return false;
            } else {
                return ((AnswerLog) answerTable.get(signature)).isAcquaintanceCreationPossible(answerer);
            }
        } else {
            return false;
        }
    }


    /** returns <i>true</i> iff the criterion for learning from
     *   <t>answerer</t> the given signature skill is satisfied */
    public boolean isLearningAccurate(String signature, String answerer) {
        if (answerTable.containsKey(signature)) {
            if (hasAcquaintance(signature) && answerer.equals(getAcquaintance(signature))) {
                return false;
            } else {
                return ((AnswerLog) answerTable.get(signature)).isLearningAccurate(answerer);
            }
        } else {
            return false;
        }
    }

    /** returns <i>true</i> iff there exists an acquaintance for this request signature*/
    public boolean hasAcquaintance(String signature) {
        return acquaintances.containsKey(signature);
    }

    /** returns the acquaintances for the given Signature
     */
    public String getAcquaintance(String signature) {
        return (String) acquaintances.get(signature);
    }

    /** removes any acquaintance for this siagnture from table
     */
    public void removeAcquaintance(String signature) {
        acquaintances.remove(signature);
    }

    /** creates a new acquaintance for given signature and aent
     */
    public void createAcquaintance(String signature, String agent) {
        acquaintances.put(signature, agent);
    }

    public AnswerLog createAnswerLog() {
        return answerLogFactory.createInstance();
    }

    public void setAnswerLogFactory(AnswerLogFactory answerLogFactory) {
        this.answerLogFactory = answerLogFactory;
    }

    /** returns true iff <em>agent</em> is an acquaintance for at least one requets */
    public boolean isAnAcquaintance(String agent) {
        return acquaintances.containsValue(agent);
    }

    public void removeAgentFromAcquaintances(String agent) {
        Iterator it = acquaintances.keySet().iterator();
        while (it.hasNext()) {
            String signature = (String) it.next();
            if (acquaintances.get(signature).equals(agent)) {
                removeAcquaintance(signature);
            }
        }
        it = answerTable.keySet().iterator();
        while (it.hasNext()) {
            String signature = (String) it.next();
            ((AnswerLog) answerTable.get(signature)).removeAnswerer(agent);
        }
    }
}// AcquaintanceTable
