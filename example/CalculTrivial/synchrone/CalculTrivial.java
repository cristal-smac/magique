package chap4;
import fr.lifl.magique.*;

public class CalculTrivial implements Skill {

    // renvoie n après p millisec
    public Integer calcul(Integer n, Integer p) {
        System.out.println("debut de calcul");
        try { Thread.sleep(p.intValue()); } 
          catch (Exception e){}
        System.out.println("fin de calcul");
        return n;
    }    
} // CalculTrivial
