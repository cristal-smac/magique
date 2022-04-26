/**
 * DefaultLearningLogFactory.java
 * <p>
 * <p>
 * Created: Mon Oct 29 15:10:01 2001
 *
 * @author <a href="mailto: "Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;

public class DefaultLearningLogFactory implements AnswerLogFactory {
    public AnswerLog createInstance() {
        return new DefaultLearningLog();
    }
}// DefaultLearningLogFactory
