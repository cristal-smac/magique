/**
 * LearnSkill.java
 * <p>
 * <p>
 * Created: Wed Mar 01 09:27:02 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.system;

import fr.lifl.magique.AtomicAgent;
import fr.lifl.magique.skill.DefaultSkill;

public class ForgetSkill extends DefaultSkill {

    public ForgetSkill(AtomicAgent a) {
        super(a);
    }

    /** forget a skill from its class name
     * @param skillClassName the class name
     */
    public void forgetSkillFromClassName(String skillClassName) {
        removeSkillFromClassName(skillClassName);
    }

    /** forget a skill from one of its methods name
     * @param skillMethodName a method name ie. signature
     */
    public void forgetSkill(String skillMethodName) {
        removeSkill(skillMethodName);
    }

} // ForgetSkill
