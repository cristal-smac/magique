// Penible.java
package vente.strategies;

import vente.objects.*;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;
import java.util.*;


/*
 *	This strategy is quite the same as the agressive one : if it
 *	wants an article, it will pay * as much as it can in order to
 *	have it.  * <BR> * But, it can also bid for an article it
 *	doesn't want, just to have the others bidders pay a * lot for
 *	the articles they want.
 * */
public class Penible extends MagiqueDefaultSkill
{

	public Penible(Agent myAgent)
	{
	    super(myAgent);
	}

    public int bid(Auction lastAuction, Integer currentMoney)
    {
	int money = currentMoney.intValue();	
	Catalog wanted = (Catalog)askNow("getWanted");

	int b = 0;
	int rem = money - 10 ;
	boolean found = false ;
	if ( wanted.contains(((Article)lastAuction.getArticle())) )
	    {
				// I am interested, if I can, I bid.
		if ( lastAuction.getArticle().getPrice() <= rem)
		    {
			// I can bid
			b = 10 ;
		    } else if (lastAuction.getBidder().compareTo("none") != 0 && lastAuction.getArticle().getPrice() <= rem)
			{
			    // Someone wants this article I do not want
			    // I decide if I increase the price
			    Random rand = new Random() ;
			    int iBid = rand.nextInt(2) ;
			    if (iBid >= 1)
				{
				    // I decided to increase the price
				    System.out.println(" I want the others to pay a max!") ;
				    b = 10 ;
				}
			}
	    } // if - else
	return b ;
    } // bid
    
    
    

} // Penible
