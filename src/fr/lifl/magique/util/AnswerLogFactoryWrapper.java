
/**
 * AnswerLogFactoryWrapper.java
 *
 *
 * Created: Mon Oct 29 10:26:57 2001
 *
 * @author <a href="mailto: "Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;

public class AnswerLogFactoryWrapper {
    AnswerLogFactory alf;
    public AnswerLogFactoryWrapper (AnswerLogFactory alf){
	this.alf = alf;
    }
    public AnswerLogFactory getAnswerLogFactory() { return alf; }

}// AnswerLogFactoryWrapper
