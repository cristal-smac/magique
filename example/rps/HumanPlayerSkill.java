import java.lang.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class HumanPlayerSkill extends DefaultSkill implements PlayerSkill
{
    private Card myPlay;

    public HumanPlayerSkill(Agent ag) { super(ag); }

    public void setRivalsPlay(Character c)
    {
	Card card = Card.fromChar(c);
	System.out.print("Your opponent played : " + card);
	System.out.println("  score: " + myPlay.compareTo(card));
    }

    public Character getMyPlay()
	{
	    System.out.println("What do you want to play now (r,s,p) ? ");
	    char c='r';
	    boolean erreur;
	    do {
		erreur=false;
		try{
		    c = Input.readChar();
		}
	        catch(java.io.IOException e)  {
		    System.out.println(e);
		    erreur=true;
		}
	    } while(erreur);	
	    myPlay = Card.fromChar(new Character(c));
	    System.out.println("je joue "+myPlay);	   
	    return new Character(c);
	}
    public void init(){}   
    public void setResult(Integer result)
	{
	    int i = result.intValue();

	    if (i==0) System.out.println("match nul !");
	    if (i>0) System.out.println("match gagné ! "+i);
	    if (i<0) System.out.println("match perdu !"+i);
	}   

}
