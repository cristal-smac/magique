package chap4;
import java.util.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class CalculCompletAsynchrone 
             extends MagiqueDefaultSkill {

    public CalculCompletAsynchrone(Agent a) { super(a); }

    public void complet() {
        System.out.println("debut de complet");
        long deb = (new Date()).getTime();
        int cumul = 0;
        Request rf=createQuestion("calcul",
                                  new Integer(10), 
                                  new Integer(10000));
        Request rg=createQuestion("calcul",
                                  new Integer(15),
                                  new Integer(15000));
        ask("f",rf);
        ask("g",rg);

        cumul += ((Integer) returnValue(rf)).intValue();
        cumul += ((Integer) returnValue(rg)).intValue();
        
        System.out.println("Cumul = " + cumul);
        long fin = (new Date()).getTime();
        System.out.println((fin - deb) + " ms");
    }
} // CalculCompletAsynchrone
