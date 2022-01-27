// Auction.java
package vente.objects;

public class Auction implements java.io.Serializable
{	
    // Variables
    private Article article ;
    private String bidder ;
    
    // Constructor
    public Auction(String n, Article a)
    {	
	article = a ;
	bidder = n ;
    }
    
    // Methods
    public String toString() { return( article.getNom() + 
				       "[" + article.getPrice() +"$]" +"{" + bidder + "}"); }
    public void setBidder(String b) { bidder = b ; }
    public Article getArticle() { return article ; }
    public String getBidder() { return bidder ; }
}
