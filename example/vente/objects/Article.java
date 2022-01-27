// Article.java
package vente.objects;

public class Article implements java.io.Serializable
{	
	// Variables
    private String nom;		// Le nom de l'article
    private int price;		// Le prix de mise aux encheres
    
    // Constructor
    public Article(String n, int p)
    {	
	nom = n;
	price = p;
    }
    
    // Methods
    public String toString() { return( nom + "[" + price +"$]"); }
    public void setPrice(int i) { if (i>price) price = i ; }
    public int getPrice() { return price; }
    public String getNom() { return nom; }
    public void improvePrice(int i) {price += i ; }
}
