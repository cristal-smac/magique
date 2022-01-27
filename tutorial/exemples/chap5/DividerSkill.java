package chap5;

public class DividerSkill implements fr.lifl.magique.Skill {
    public Integer quotient(Integer x, Integer y) {
       return new Integer(x.intValue()/y.intValue());
    }        
} // DividerSkill
