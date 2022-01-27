package chap5;
import fr.lifl.magique.*;
// args[0] = super host name
public class CollatzImp extends AbstractMagiqueMain {
  // args[0] = super host name | args[1] = conjecture strating value
  // args[2] = verbose level
  public void theRealMain(String[] args) {
    if (args.length == 3) {
       Agent.setVerboseLevel(Integer.parseInt(args[1]));
    }  
    Agent colAgent = createAgent("collatz");  
    colAgent.connectToBoss("super@"+args[0]);
    colAgent.addSkill(new chap5.CollatzSkill(colAgent));
             // calcul de la conjecture avec x=17
    Integer x;
    if (args.length < 2) {
	x = new Integer(17); //default
    }
    else {
	x = new Integer(args[1]);
    }
    colAgent.perform("conjecture",x);
  }
}
