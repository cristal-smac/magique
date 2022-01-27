// AuctioneerImp.java
package vente;

import fr.lifl.magique.* ;

public class AuctioneerImp extends AbstractMagiqueMain
{

    public void theRealMain(String[] args) {

	if (args.length>=3)
	    Agent.setVerboseLevel(new Integer(args[2]).intValue());		

	Agent auctioneer = createAgent("auctioneer") ;

//  	auctioneer.addSkill("vente.gui.GraphAuctioneer",
//  			    new Object[]{auctioneer});
	// au choix
	auctioneer.addSkill(new vente.gui.GraphAuctioneer(auctioneer));
	auctioneer.addSkill("vente.AuctioneerSkill",
			    new Object[]{auctioneer});
	auctioneer.perform("initCatalog");

	auctioneer.start() ;
    }

}

