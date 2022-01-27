package chap6;
import fr.lifl.magique.*;

public class IForgetImp extends AbstractMagiqueMain {

   public void theRealMain(String[] args) {
     if (args.length == 1) 
         Agent.setVerboseLevel(Integer.parseInt(args[0]));
     
     Agent a = createAgent("forgotter");
     a.addSkill(new ToBeForgottenSkill(a));
     
     a.perform("firstMethod", new Object[]{"hello world"});
     
     a.removeSkill("firstMethod(java.lang.String)");
     //a.removeSkill("secondMethod(java.lang.String, 
     //                            java.util.ArrayList)");
     
     a.perform("firstMethod", 
               new Object[]{"encore hello world"});
     a.perform("secondMethod", 
               new Object[]{"hello world",  
                            new java.util.ArrayList()});
   }
}// IForgetImp
