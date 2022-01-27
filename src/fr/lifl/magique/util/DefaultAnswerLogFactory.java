
/**
 * AnswerLogFactory.java
 *
 *
 * Created: Mon Oct 22 10:51:18 2001
 *
 * @author <a href="mailto:routier@lifl.fr">Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;

public class DefaultAnswerLogFactory implements AnswerLogFactory {
    public AnswerLog createInstance() {
	return new AnswerLogImpl();
    }
}
