package chap5;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

public class CollatzSkill extends MagiqueDefaultSkill {
   private Integer x;
   public CollatzSkill(Agent myAgent) {
     super(myAgent);
   }
   private Boolean testParity(Integer x) {
     return (Boolean) askNow("isEven",x);
   }
   private Integer xByTwo(Integer x) {
     return (Integer) askNow("quotient",x,new Integer(2));
   }
   private Integer threeTimesPlus1(Integer x) {
     Integer y = (Integer) askNow("mult",x,new Integer(3));
     return (Integer) askNow("add",y,new Integer(1));
   }
   public void conjecture(Integer x) {
     this.x = x;
     conjecture();
   }
   private void conjecture() {
     while (x.intValue() != 1) {
       perform("display",new Object[]{"x  = "+x.intValue()});
       if (testParity(x).booleanValue()) {
           x = xByTwo(x);
       }
       else {
           x = threeTimesPlus1(x);
       }
     }     
     perform("display",new Object[]{"x = 1 ** finished"});
   }
} // CollatzSkill
