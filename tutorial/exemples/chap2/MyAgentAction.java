package chap2;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class MyAgentAction extends MagiqueActionSkill {
    
    public MyAgentAction(Agent a) { super(a); }
    
    public void action() { 
        System.out.println("hello world"); 
    }
}// MyAgentAction
