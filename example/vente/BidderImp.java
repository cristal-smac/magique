// BidderImp.java
package vente;

import vente.gui.*;

import fr.lifl.magique.* ;

public class BidderImp extends AbstractMagiqueMain
{
    public void theRealMain(String[] args) {
		
	if (args.length>=5)
	    Agent.setVerboseLevel(new Integer(args[4]).intValue());	

	Agent bidderOne = createAgent(args[2]) ;
	bidderOne.addSkill("vente.BidderSkill",
			   new Object[]{bidderOne, args[3]});
	bidderOne.addSkill("vente.gui.GraphBidder",
			   new Object[]{bidderOne, args[3]});
	bidderOne.start();

    }

}
