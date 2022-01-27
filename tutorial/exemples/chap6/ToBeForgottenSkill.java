package chap6;
import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import java.util.*;

public class ToBeForgottenSkill extends MagiqueDefaultSkill{
    public ToBeForgottenSkill (Agent a){
       super(a);
    }

    public void firstMethod(String s) {
       System.out.println("firstMethod "+s);	
    }

    public void secondMethod(String s, ArrayList al) {
       System.out.println("secondMethod "+s+" " + al);	
    }
}// ToBeForgottenSkil
