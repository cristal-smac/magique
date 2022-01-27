
/**
 * ServiceUserSkill.java
 *
 *
 *
 * @author JC Routier
 * @version
 */
package dynamic.pool;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

import java.util.*;

public class ServiceUserSkill extends MagiqueDefaultSkill {
    public ServiceUserSkill(Agent a){super(a);}
    
    public void payService(Date sendDate) 
    {
	Date payDate = new Date();
	perform("display",new Object[]{"service paid, delay = "+(payDate.getTime()-sendDate.getTime())+" ms"});
    }
} // ServiceUserSkill
