package chap6;

public class Ball implements java.io.Serializable {
    private String myBall;
    public Ball (String myBall){ this.myBall = myBall; }
    public String toString() { return myBall; }    
}// Ball
