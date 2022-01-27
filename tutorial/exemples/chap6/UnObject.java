package chap6;

public class UnObject implements java.io.Serializable {
    private Object ball;
    public UnObject (Object ball){
       this.ball = ball;
    }
    public String toString() { return ball.toString(); }
}// UnObject
