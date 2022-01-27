
/**
 * CalculComplet.java
 *
 *
 * Created: Fri Feb 04 21:40:03 2000
 *
 * @author Philippe MATHIEU
 * @version
 */

import java.util.*;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class CalculComplet extends DefaultSkill {
    Agent myagent;
    public CalculComplet(Agent a) { super(a); myagent=a;}

    public void complet() {
	System.out.println("debut de complet");
	long deb = (new Date()).getTime();
	int cumul = 0;
	Request rf=createQuestion("calcul",new Integer(10), new Integer(1000));
	Request rg=createQuestion("calcul",new Integer(15), new Integer(1500));

	ask("f",rf);
	ask("g",rg);

	cumul += ((Integer) myagent.returnValue(rf)).intValue();
	cumul += ((Integer) myagent.returnValue(rg)).intValue();
	
	System.out.println("Cumul = " + cumul);
	long fin = (new Date()).getTime();
	System.out.println((fin - deb) + " ms");
    }
} // CalculComplet
    
