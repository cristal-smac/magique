/**
 * AnswerLogFactory.java
 * <p>
 * <p>
 * Created: Mon Oct 22 10:51:18 2001
 *
 * @author <a href="mailto:routier@lifl.fr">Jean-Christophe Routier</a>
 * @version
 */
package fr.lifl.magique.util;

/** answer lof factory are serializable */
public interface AnswerLogFactory extends java.io.Serializable {
    AnswerLog createInstance();
}
