package chap5;

public class ParitySkill implements fr.lifl.magique.Skill {    
    public Boolean isEven(Integer x) {
       return new Boolean( (x.intValue()%2) == 0 );
    }        
} // ParitySkill
