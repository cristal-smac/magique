package chap5;
import fr.lifl.magique.*;

public class ParityImp extends AbstractMagiqueMain{
    // args[0] = super host name 
    public void theRealMain(String[] args) {
    if (args.length == 2) {
       Agent.setVerboseLevel(Integer.parseInt(args[1]));
    }  
              // création du superviseur "mathématique"
    Agent supermath = createAgent("supermath");  
              // connexion à son superviseur
    supermath.connectToBoss("super@"+args[0]);
              // création de l'agent pour la parité
    Agent parity = createAgent("parity");
             // enseignement de la compétence de parité
    parity.addSkill(new chap5.ParitySkill());
             // connexion à son superviseur
    parity.connectToBoss("supermath");
  }
}
