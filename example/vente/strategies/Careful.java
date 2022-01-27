// Careful.java
package vente.strategies;

import vente.objects.*;

import fr.lifl.magique.*;
import fr.lifl.magique.skill.*;


/**
 *	The agresive strategy consists into bidding only when you are
 *	interested by an article.  * But when you are interested, you
 *	bid only a third of your starting money.
 * */
public class Careful extends MagiqueDefaultSkill
{
	
    public Careful(Agent myAgent)
    {
	super(myAgent);
    }

    public Integer bid(Auction lastAuction, Integer currentMoney)
    {	    
	int money = currentMoney.intValue();
	Catalog wanted = (Catalog)askNow("getWanted");
	int r = 0 ;
	System.out.println(wanted.toString()) ;
	if ( wanted.contains(((Article)lastAuction.getArticle())) )
	    {
		float a = (money + 10)/wanted.size() ;
		int b = lastAuction.getArticle().getPrice();
		
		if ( a > b )
		    {
			System.out.println("  I bid for 10 $!") ;
			r = 10 ;
		    }
	    }
	return new Integer(r) ;
    } // bid
	
} // Careful
