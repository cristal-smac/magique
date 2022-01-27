package chap4;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
public class Compteur implements Skill { // c'est un service
    int cpt = 0;
    public void incr() { cpt++; }  
    // Boolean and not boolean since everything must be object  
    public Boolean arret() {
      if (cpt==10) {
        return Boolean.TRUE; 
      }
      else {
        return Boolean.FALSE;
      }
    }
}
