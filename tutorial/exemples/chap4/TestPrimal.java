package chap4;
import fr.lifl.magique.*;
import fr.lifl.magique.util.*;
import fr.lifl.magique.skill.*;
import java.util.*;

public class TestPrimal extends MagiqueDefaultSkill {
  public TestPrimal(Agent a){super(a);}

  // renvoie True si N est premier, False sinon, pour cela
  // il divise le calcul entre tous les agents de son équipe
  public Boolean prem(Integer n) {
     int nb = n.intValue();
     // pour mémoriser les requêtes
     Vector questions = new Vector();
     int nbAg = ((Team) askNow("getMyTeam")).size();
     int taille = (int) (Math.sqrt(nb)/nbAg)+1 ;
     // boucle d'envoi  à chaque agent de l'équipe
     int tranche = 3;
     Enumeration e=((Team) askNow("getMyTeam")).getMembers();
     while (e.hasMoreElements()) {
  	    String agent = (String) e.nextElement();
  	    Request r = createQuestion("cherche",
  				       new Integer(tranche),
  				       new Integer(tranche+taille-1),
  				       n);
  	    ask(agent,r);
  	    tranche = tranche + taille;
  	    // stocke les questions pour pouvoir les récupérer
  	    questions.addElement(r);
     }
  	
     // boucle de lecture des réponses qui reviennent
     boolean answered = false;
     // tant que je n'ai pas trouve de diviseur
     while (!answered && questions.size()!=0) {
       // parcourir toutes les questions
       for (int i=0; i<questions.size() && !answered ;i++) {
         Request q = (Request) questions.elementAt(i);
         // si q existe et qu'une rep vient d'arriver
         if (q!=null && isAnswerReceived(q)) {
            Integer answer = ((Integer) returnValue(q));
            answered = !answer.equals(new Integer(-1));
            questions.removeElementAt(i);
         }
       }
     }
     return (new Boolean (!answered));
  }   
} // TestPrimal
