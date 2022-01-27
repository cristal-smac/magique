import java.util.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class BankSkill extends MagiqueDefaultSkill
{   private int nbRounds = 10; 

    //constructeurs
    public BankSkill(Agent ag) {
	super(ag);
    }   

    private boolean begin = false;

    public Integer game(String p1, String p2) {   
	    int p1Points=0; int p2Points=0;
	    perform(p1,"init");
	    perform(p2,"init");
	    for (int i=1; i<=nbRounds;i++) {
		Card c1 = Card.fromChar((Character) askNow(p1,"getMyPlay"));
		Card c2 = Card.fromChar((Character) askNow(p2,"getMyPlay"));
		if (c1.compareTo(c2) > 0) p1Points++;
		if (c1.compareTo(c2) == 0);
		if (c1.compareTo(c2) < 0) p2Points++;		    
		perform(p1,"setRivalsPlay",c2.toCharacter());
		perform(p2,"setRivalsPlay",c1.toCharacter());
	    }
	    System.out.println(p1Points+" "+p2Points);
	    return new Integer(p1Points - p2Points);
    }

    public void action() {
	System.out.println(getName()+" parti");
	Enumeration players = getMyTeam().getMembers();
	String player1 = (String) players.nextElement();
	String player2 = (String) players.nextElement();
	Integer result = game(player1, player2);
	perform(player1,"setResult",result);
	perform(player2,"setResult",new Integer( - result.intValue() ));
    }
}

