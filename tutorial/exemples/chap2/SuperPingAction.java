package chap2;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class SuperPingAction extends MagiqueActionSkill {
    public SuperPingAction(Agent a) { super(a); }

    public void action() { perform("pong"); }
}
