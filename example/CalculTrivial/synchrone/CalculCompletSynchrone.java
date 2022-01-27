package chap4;
import java.util.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class CalculCompletSynchrone 
             extends MagiqueDefaultSkill {
    
    public CalculCompletSynchrone(Agent a) { super(a); }

    public void complet() {
        System.out.println("debut de complet");
        long deb = (new Date()).getTime();
        int cumul = 0;
        cumul+= ((Integer) askNow("f","calcul",
                                  new Integer(15), 
                                  new Integer(15000))
                 ).intValue();
        cumul+= ((Integer) askNow("g","calcul",
                                  new Integer(10), 
                                  new Integer(10000))
                 ).intValue();
        System.out.println("Cumul = " + cumul);
        long fin = (new Date()).getTime();
        System.out.println((fin - deb) + " ms");
    }
} // CalculComplet
