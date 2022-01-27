import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import java.lang.*;

public class RandomPlayerSkill extends DefaultSkill implements PlayerSkill
{
    public RandomPlayerSkill(Agent ag) { super(ag); }

    public void setRivalsPlay(Character c){}
    public Character getMyPlay() {     
	int n = (int)(Math.random()*1024)%3;
        switch (n) 
	    {
	    case 0: return new Character('r');
	    case 1: return new Character('s');       
	    case 2: return new Character('p'); 
	    default : return null; // ne doit jamais se produire !!
	    }
    }
    public void init(){}   
    public void setResult(Integer result){}   
    public void action() {}
}
