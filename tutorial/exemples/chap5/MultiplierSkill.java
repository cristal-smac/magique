package chap5;

public class MultiplierSkill implements fr.lifl.magique.Skill {
    public Integer mult(Integer x, Integer y) {
       return new Integer(x.intValue()*y.intValue());
    }    
} // MultiplierSkill
