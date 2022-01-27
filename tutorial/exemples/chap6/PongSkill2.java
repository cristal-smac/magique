package chap6;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class PongSkill2 extends MagiqueDefaultSkill {
    public PongSkill2(Agent a){super(a);}
    
    public void pong(UnObject ball, UnObject autreBall)  {
	perform("display", new Object[]{"pong : "+ ball.toString()+
					" "+autreBall.toString()});
	perform("ping",ball,autreBall);
    }
} // PongSkill2
