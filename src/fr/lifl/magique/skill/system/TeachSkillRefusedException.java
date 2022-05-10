/**
 * TeachSkillRefusedException.java
 * <p>
 * <p>
 * Created: Wed Mar 01 09:35:06 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.system;

public class TeachSkillRefusedException extends RuntimeException {

    public TeachSkillRefusedException(String skillName, String agent, String to) {
        super();
        System.err.println(agent + " refuses to teach " + skillName + " to " + to);
    }

} // TeachSkillRefusedException
