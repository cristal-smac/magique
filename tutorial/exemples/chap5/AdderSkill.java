package chap5;

public class AdderSkill implements fr.lifl.magique.Skill {
    public Integer add(Integer x, Integer y) {
       return new Integer(x.intValue()+y.intValue());
    }    
} // AdderSkill
