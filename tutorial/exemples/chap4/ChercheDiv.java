package chap4;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class ChercheDiv implements Skill {
    // recherche un diviseur de N entre bInf et bSup
    // renvoie le premier diviseur trouvé ou -1 si aucun
    public Integer cherche(Integer bInf, Integer bSup, 
                           Integer n) {
        int bi=bInf.intValue();
        int bs=bSup.intValue();
        int nb=n.intValue();
        System.out.println("cherche un diviseur entre " + 
                           bi + " et " + bs);
        // to be sure bi is odd
        if (bi%2==0) bi++; 
        for (int i=bi; i<=bs; i+=2)
            if (nb % i == 0) {
                System.out.println("diviseur : " + i);
                return new Integer(i);
            }
        return new Integer(-1);
    }
} // ChercheDiv
