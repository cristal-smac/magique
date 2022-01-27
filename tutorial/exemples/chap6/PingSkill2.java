package chap6;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class PingSkill2 extends MagiqueDefaultSkill {
    public PingSkill2(Agent a){super(a);}

    public void ping(UnObject ball, UnObject autreBall) {
       perform("display",new Object[]{"ping : "+ball.toString()+
                                      " "+autreBall.toString()});
       perform("pong",ball,autreBall);
    }
} // PingSkill2
