package fr.lifl.rage.demos.twodd;

public class Force implements java.io.Serializable {

    protected double tangentielle;
    protected double normale;

    public Force() {
	this(0.0, 0.0);
    }

    public Force(double tangentielle, double normale) {
	this.tangentielle = tangentielle;
	this.normale = normale;
    }    
}
