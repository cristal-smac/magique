// TestAuction.java
package vente;

import java.util.* ;
import fr.lifl.magique.* ;

public class TestAuction extends AbstractMagiqueMain
{
    
    public void theRealMain(String args[])
    {
	if (args.length>=1)
	    Agent.setVerboseLevel(new Integer(args[0]).intValue());		

	Agent auctioneer = createAgent("auctioneer") ;
	Agent bidderOne = createAgent("bidderOne") ;
	Agent bidderTwo = createAgent("bidderTwo") ;
	Agent bidderThree = createAgent("bidderThree") ;
			
	//	String from = Platform.PLATFORMMAGIQUEAGENTNAME+"@"+p.getName();

	auctioneer.addSkill("vente.gui.GraphAuctioneer",
			    new Object[]{auctioneer});
	auctioneer.addSkill("vente.AuctioneerSkill",
			    new Object[]{auctioneer});
	auctioneer.perform("initCatalog");

	bidderOne.addSkill("vente.BidderSkill",
			   new Object[]{bidderOne,auctioneer.getName()});
	bidderOne.addSkill("vente.gui.GraphBidder",
			   new Object[]{bidderOne, auctioneer.getName()});

	bidderTwo.addSkill("vente.BidderSkill",
			   new Object[]{bidderTwo, auctioneer.getName()});
	bidderTwo.addSkill("vente.gui.GraphBidder",
			   new Object[]{bidderTwo, auctioneer.getName()});

	bidderThree.addSkill("vente.BidderSkill",
			   new Object[]{bidderThree,auctioneer.getName()});
	bidderThree.addSkill("vente.gui.GraphBidder",
			   new Object[]{bidderThree,auctioneer.getName()});

	auctioneer.start() ;
	bidderOne.start() ;
	bidderTwo.start() ;
	bidderThree.start() ;
		
	
    } // main
}
