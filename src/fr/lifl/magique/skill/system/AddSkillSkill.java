/**
 * AddSkillSkill.java
 * <p>
 * <p>
 * Created: Wed Mar 22 11:55:21 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.skill.system;

import fr.lifl.magique.AtomicAgent;
import fr.lifl.magique.SkillAlreadyAcquiredException;
import fr.lifl.magique.skill.DefaultSkill;

public class AddSkillSkill extends DefaultSkill {

    public AddSkillSkill(AtomicAgent a) {
        super(a);
    }

    // skills

    public void addASkill(String skillClassName) throws SkillAlreadyAcquiredException {
        addSkill(skillClassName);
    }

    public void addASkill(String skillClassName, Object[] args) throws SkillAlreadyAcquiredException {
        addSkill(skillClassName, args);
    }

    public void addASkill(String skillClassName, Boolean flag) {
        addSkill(skillClassName, flag.booleanValue());
    }

    public void addASkill(String skillClassName, Boolean flag, Object[] args) throws SkillAlreadyAcquiredException {
        addSkill(skillClassName, flag.booleanValue(), args);
    }

} // AddSkillSkill
