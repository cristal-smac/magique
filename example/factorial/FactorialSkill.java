/**
 * FactorialSkill.java
 *
 *
 * Created: Fri Oct 08 16:38:36 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import java.util.*;
import java.math.BigInteger;
 
public class FactorialSkill extends MagiqueDefaultSkill {    
    
    
    private static BigInteger DEUX = new BigInteger("2");

    private Vector theQuestions = new Vector();

    //constructeurs
    public FactorialSkill(Agent ag) {
	super(ag);
    }   


    // dès qu'une réponse (résultat) à une des questions
    // (multiplications) posées a été reçue, retourne la question
    // répondue
    private Request firstAnsweredQuestion() {
	boolean found = false;
	Request question = new Request();
	while (!found) {
	    for (Enumeration quest = theQuestions.elements(); 
		 quest.hasMoreElements() && !found;) {
		question = (Request) quest.nextElement();
		found = isAnswerReceived(question);
	    }
	}
	theQuestions.removeElement(question);
	return question;
    }

    // le calcul de n!
    public BigInteger factorielle(BigInteger n) {	
	Enumeration team;

	BigInteger i;
	if (n.mod(DEUX).intValue()==0) i = BigInteger.ONE; else i = DEUX;
	team = getMyTeam().getMembers(); 
 
	// distribution aux agents multiplieur de mon équipe des
	// multiplications de base
	for(BigInteger j=i;j.compareTo(n)==-1;j=j.add(DEUX)) {
	    if (!team.hasMoreElements()) {
		team = getMyTeam().getMembers(); 
	    }
	    theQuestions.addElement(ask((String) team.nextElement(),"mult",j, j.add(BigInteger.ONE)));
	}
	// distribution des multiplications des résultats intermédiaires reçus
	while (theQuestions.size()>1) {
	    if (!team.hasMoreElements()) {
		team = getMyTeam().getMembers(); 
	    }
	    theQuestions.addElement(ask((String) team.nextElement(),"mult",
					(BigInteger) returnValue(firstAnsweredQuestion()), 
					(BigInteger) returnValue(firstAnsweredQuestion())));
	}
	
	Request quest = firstAnsweredQuestion();
	return (BigInteger) returnValue(quest);
    }

    public BigInteger factorielle(Integer i) {
	return factorielle(new BigInteger(i.toString()));
    }
} // FactorialSkill
 
