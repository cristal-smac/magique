// Agressive.java
package vente.strategies;

import vente.objects.*;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;

/**
 *	The agresive strategy consists into bidding only when you are
 *	interested by an article.  * But when you are interested, you
 *	bid as much as you can.
 * */
public class Agressive extends MagiqueDefaultSkill
{

    public Agressive(Agent myAgent)
    {
	super(myAgent);
    }
	
    public int bid(Auction lastAuction, Integer currentMoney)
    {
	int money = currentMoney.intValue();	
	Catalog wanted = (Catalog)askNow("getWanted");

	int r = 0 ;
	
	if ( wanted.contains(((Article)lastAuction.getArticle())) )
	    {
		int a = money - 10 ;
		int b = lastAuction.getArticle().getPrice() ;
		
		if ( a >= b )
		    {
			System.out.println("  I bid for 10 $!") ;
			r = 10 ;
		    }
	    }
	return r ;
    } // bid
    


} // Agressive
