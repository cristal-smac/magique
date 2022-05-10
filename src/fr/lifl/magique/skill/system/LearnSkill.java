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

public class LearnSkill extends DefaultSkill {

    public LearnSkill(AtomicAgent a) {
        super(a);
    }

    public void learnSkill(String skillName, String from) {
        askNow(from, "teachSkill", skillName, getName());
        addSkill(skillName);
    }

    public void learnSkill(String skillName, String from, Boolean myAgent) {
        askNow(from, "teachSkill", skillName, getName());
        addSkill(skillName, myAgent.booleanValue());
    }

    public void learnSkill(String skillName, String from, Boolean myAgent, Object[] args) {
        askNow(from, "teachSkill", skillName, getName());
        addSkill(skillName, myAgent.booleanValue(), args);
    }

    public void teachSkill(String skillName, String to) {
        if (!teachSkillAcknowledgement(skillName, to)) {
            throw new TeachSkillRefusedException(skillName, getName(), to);
        }
        String MPA = fr.lifl.magique.platform.Platform.PLATFORMMAGIQUEAGENTNAME + "@" + fr.lifl.magique.util.Name.noShortName(getName());
        askNow(MPA, "giveClassArchive", skillName, to);
    }

    private boolean teachSkillAcknowledgement(String skillName, String to) {
        return true;
    }

    public void learnSkillFromSignature(String signature, String agent) {
        String serviceName = (String) askNow(agent, "giveSkillClassNameFromSignature", signature);
        learnSkill(serviceName, agent, Boolean.TRUE);
    }

} // LearnSkill
