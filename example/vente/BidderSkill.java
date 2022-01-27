// BidderSkill.java
package vente;

import vente.gui.*;
import vente.objects.*;

import java.util.* ;
import fr.lifl.magique.* ;
import fr.lifl.magique.platform.* ;
import fr.lifl.magique.skill.* ;


/**
 *
 *   BidderSkill is a skill written in order to enable an agent to buy
 *   articles during the * auctions.
 *
 * */
public class BidderSkill extends DefaultSkill  // This is a service
{
	private String myAgent ;
	private String myBoss  ;
	private Catalog catalog = null ; 
	private Auction lastAuction = null ;
	private Vector lost   = new Vector() ;
	private Vector won    = new Vector() ;
	private int money = 0 ;
	private int currentMoney = 0 ;
    //	private Strategy myStrategy = null ;
	private Catalog wanted = new Catalog() ;


    /**
     * The constructor is very light. It just initiates the names of :
     * <UL>
     *    <LI> The agent
     *    <LI> The auctioneer
     * </UL>
     */
    // Constructor
    public BidderSkill(Agent s, String b) { 
	super( s ) ;
	myAgent = s.getName() ;
	myBoss = b ;
    } // #1
	

    public Catalog getWanted(){return wanted;}


	
	/**
	 *  This function iniates the catalog when you connect the agent.
	 */
	public void setCatalog(Catalog c)
	{
		catalog = c ;
		perform(myAgent, "printCatalog", catalog) ;
		wanted = chooseArticles(c) ;
	} //setCatalog
	
	
	/**
	 * This function initates the amount of money the agent has for bidding.
	 * <BR>
	 * Unlike the "setCatalog", or "setStrategy" functions, even if you disconect
	 * the agent, this function is done only done time in the life of the agent.
	 */
	 public void setMoney( Integer i )
	{
		money = i.intValue() ;
		String mo = new String("I have ") ;
		mo += money + " $" ;
		perform(myAgent, "addMessage", mo) ;
		currentMoney = money ;
	} // setMoney
	

	/**
	 * This function sets the strategy your agent will use. You
	 * can disconnect your agent in order to change its strategy.  */
	public void setStrategy( String strat )
	{
	    addSkill("vente.strategies."+strat,true);

	    perform(myAgent, "addMessage", "strategy "+strat+" acquired");
	    System.out.println("strategy "+strat+" acquired"); 
	    
	    Integer cm = new Integer(currentMoney) ;
	} // setStrategy
    
    /**
     *  This function randomly chooses some articles in the catalog
     *  sent by the * auctioneer when the agent connected to him. If
     *  you disconnect the agent, this * list is lost. If you connect
     *  later, this list is a new time randomly chosen.  */
    public Catalog chooseArticles(Catalog c)
    {
	Catalog chosen = new Catalog() ;
	int max = c.size() ;
	int numberWanted = 5 ;
	if (max < numberWanted)
	    numberWanted = max ;
	for (int i=0 ; i<numberWanted ; i++)
	    {
		int choice = (int)(Math.random() * (double)c.size());
		if (chosen.contains(c.elementAt(choice)))
				// L'article a deja ete choisi
		    i-- ;
		else
		    chosen.addElement(c.elementAt(choice));
	    }
	perform(myAgent, "setChoosen", chosen) ;
	return chosen ;
    } // chooseArticle
    
    
    /**
     * Initiates the name of the auctioneer.
     */
    public void setBoss(String myBossLong)
    {
	this.myBoss = myBossLong ;
    } // setBoss
    
    
    
    
	/**
	 *  This function ends the auctions for an article. It receives as lone parameter
	 * the last auction made for the article. The agent now looks at this auction and
	 * so determines if he won the article or not.
	 */
	public void endArticle(Auction a)
	{
		String comment = new String() ;
		if (wanted.contains((Article)a.getArticle()) )
		{
			comment += "Yes" ;
		} else
		{
			comment +="No" ;
		} // comment has been set
		if ( ((String)a.getBidder()).compareTo(myAgent) == 0 )
		{
			won.addElement(a.getArticle()) ;
			currentMoney = currentMoney - a.getArticle().getPrice() ;

			Integer rem = new Integer(currentMoney) ;
			perform(myAgent, "articleWon", a.getArticle(), comment, rem) ;
		} else
		{
			lost.addElement(a.getArticle()) ;
			Integer rem = new Integer(currentMoney) ;
			perform(myAgent, "articleLost", a.getArticle(), comment, rem) ;
		} // if - else
		String mes = new String("I have now : ") ;
		mes += currentMoney ;
		perform(myAgent, "addMessage", mes) ;
	} // endArticle
	

	/**
	 * This function is called in order to call the auctioneer that we bid
	 *
	 */
	public void iWantToBid(Auction a)
	{
		perform(myBoss, "setLastAuction", a) ;
	}
		

	/**
	 *  This function is called by the auctioneer. It makes the bidder
	 * react to the last auction sent by the auctioneer. This auction is
	 * the lone parameter.
	 * <BR>
	 *  The decision to bid or not is taken by the Strategy object. So the
	 * strategy is called each time an auction is received by the bidder.
	 */
    public void buyArticle(Auction a)
    {
	Auction currentArticle = new Auction(a.getBidder(), a.getArticle()) ;
	//		System.out.println(currentArticle.toString()) ;
	lastAuction = currentArticle ;

	perform(myAgent, "addMessage", "The current auction is now :") ;
	perform(myAgent, "addMessage", a.toString()) ;
	// System.out.println(currentArticle.toString()) ;
	if (a.getBidder().compareTo(myAgent) !=0 )
	    {
		// perform(myAgent, "addMessage", "I am thinking to what i do...") ;
		
		int enchere  = ((Integer)askNow("bid",currentArticle,new Integer(currentMoney))).intValue() ;
		String text = new String("I bid for ") ;
		text += enchere + " $" ;
		perform(myAgent, "addMessage", text) ;
		if (enchere>0)
		    {
			currentArticle.getArticle().improvePrice(enchere) ;
			currentArticle.setBidder(myAgent) ;
			// perform(myBoss, "setLastAuction", currentArticle) ;
			iWantToBid(currentArticle) ;
		    }
	    }
    } // buyArticle
    
} // BidderSkill
