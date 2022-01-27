/**
 * MultiplierSkill.java
 *
 *
 * Created: Mon Oct 26 14:12:17 1998
 *
 * @author Jean-Christophe Routier
 * @version
 */
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import java.math.BigInteger;

public class MultiplierSkill extends DefaultSkill {
    //constructeurs
    public MultiplierSkill(Agent ag) {
	super(ag);
    }
  
    public BigInteger mult(BigInteger x, BigInteger y) {
	perform("display",new Object[]{getMyAgent().getName()+" multiplie "+ x+" par "+y});
	return x.multiply(y);
    }    
    
} // MultiplierSkill
