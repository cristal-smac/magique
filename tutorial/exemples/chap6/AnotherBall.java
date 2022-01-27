package chap6;

public class AnotherBall implements java.io.Serializable {
    private String myAnotherBall;
    public AnotherBall (String myAnotherBall){
	this.myAnotherBall = myAnotherBall;
    }
    public String toString() {
	return myAnotherBall;
    }
    
}// AnotherBall
