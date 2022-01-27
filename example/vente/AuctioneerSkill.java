// AuctioneerSkill.java
package vente;

import vente.gui.*;
import vente.objects.*;

import java.util.* ;
import fr.lifl.magique.* ;
import fr.lifl.magique.skill.* ;
import java.lang.Thread ;
import javax.swing.Timer ;
import java.awt.event.* ;


/**
 *   AuctioneerSkill is a skill that enables an agent to manage an auction.
 */
public class AuctioneerSkill extends DefaultSkill 
{
    private String myAgent ;
    private Vector myBidders = new Vector() ;
    private Catalog catalog = new Catalog() ; 
    private boolean interrupt = false ;
    private Article currentArticle = null ;
    private Auction lastAuction = null ;
    private Vector sold = new Vector() ;
    private boolean flag = true ;
    private boolean flagWait = false ;
    private boolean flagAuctionFinished = false ;
    private Timer myTimer = null ;
    private boolean flagTimer = true ;
    private Catalog remaining = null ;
    private Timer ti = null ;
    private int timeAllowed = 10000 ;


    
    /**
     * This is the constructor of the skill.
     * <BR>
     * When you create a skill, you need to give two things :
     * <UL>
     *    <LI>the reference of the agent it belongs to,
     *    <LI>the Catalog it has to sell.
     * </UL>
     * <BR>
     * The reference enables us to catch the name of the agent. This name will be given
     *  as the first argument of all the "perform" we will want our agent to do.
     * <BR>
     * The catalog will be set once and for all. It will be cloned in order to create
     *  a copy we will update during the auctions. This will prevent us from selling twice
     *  the same article.
     * <BR>
     * The last thing we do is creating a Timer.
     */
    // Constructor
    public AuctioneerSkill(Agent s)
    { 
	super( s ) ;
	myAgent = s.getName() ;
	myTimer = new Timer(timeAllowed, new TIMEOUT()) ;
	myTimer.setRepeats(false) ;
    } // #1
    

    public void initCatalog() {
	catalog.add(new Article("pen", 10) ) ;
	catalog.add(new Article("suit", 100)) ;
	catalog.add(new Article("toy car", 50)) ;
	catalog.add(new Article("old computer", 150)) ;
	catalog.add(new Article("flowers", 15)) ;
	catalog.add(new Article("book", 30)) ;
	catalog.add(new Article("video", 20)) ;
	catalog.add(new Article("phone", 80)) ;
	catalog.add(new Article("microP", 50)) ;

	remaining = (Catalog) catalog.clone() ;
    }
    
    /**
     *
     *
     */
    public Integer setTime(Integer delay)
    {
	timeAllowed = delay.intValue() ;
	myTimer.setDelay(timeAllowed) ;
	System.out.println("delay changed to " + delay.toString()) ;
	return delay ;
    } // setTime()
    
    
    /**
     * This function sends the Catalog of the remainings objects to the agent whose
     *  name is given as parameter "who".
     * <BR>
     * If the auctions have begun, the agent is said what's the last auction made.
     *  So it can immediatly enter the auction for the current article. Of course, nothing
     *  is done if the auctions haven't begun.
     */
    public void sendCatalog(String who)
    {
	perform(who, "setCatalog", remaining) ;
	String mes = new String() ;
	mes = mes + " Sending catalog to " + who ;
	perform(myAgent, "addMessage", mes) ;
	if ( lastAuction != null)
	    {
		perform(who, "buyArticle", lastAuction) ;
	    }
	myBidders.add(who) ;
	perform(myAgent, "printMyBidders", myBidders) ;
    } //sendCatalog
    
    
    /**
     *  Received by the auctioneer, and called by a bidder, this function
     * disconnects the bidder whose name is given as argumment.
     * <BR>
     *  Of course, this function is called by a bidder. The bidder is immediatly
     * removed from the list of the bidders.
     */
    public void disconnectMe(String who)
    {
	myBidders.removeElement((String)who) ;
	perform(myAgent, "printMyBidders", myBidders) ;
    } // disconnectMe
    
    
    /**
     * This function returns an Article randomly chosen in the Catalog given as
     *  the lone parameter.
     */
    public Article chooseArticle(Catalog c)
    {
	Article chosen = null ;
	int i = (int)(Math.random() * (double)c.size());
	chosen = ((Article)c.elementAt(i)) ;
	c.remove(((Article)c.elementAt(i))) ;		
	return chosen ;
    } // chooseArticle
    
    
    /**
     *  This function calls the "buyArticle" functions for the bidders who are
     * still in the list of the available bidders.
     *
     */
    public /*synchronized*/ void sendArticleDescription(Auction a)
    {
	for (int i=0 ; i<myBidders.size() ; i++)
	    {
		perform( ((String)myBidders.elementAt(i)), "addMessage", a.toString()) ;
		perform( ((String)myBidders.elementAt(i)), "buyArticle", a) ;
	    }
	flag = true ;
    } // sendArticleDescription
    
    
    /**
     * This function is called by the graphic interface manager : "GraphAuctioneer".
     * <BR>
     * It has the agent selling the object designated by its index.
     * <BR>
     *  This method is called by the interface manager in order to sell only one article.
     *
     */
    public void sellArticle(Integer i)
    {
	Article toSell = null ;
	if (i.intValue()<catalog.size() )
	    toSell = ((Article)catalog.elementAt(i.intValue())) ;
	if (remaining.contains((Article)catalog.elementAt(i.intValue())) )
	    {
		lastAuction = new Auction("none", toSell) ;
		perform(myAgent, "addMessage", "I am selling the following article :") ;
		perform(myAgent, "addMessage", toSell.toString() ) ;
		sendArticleDescription(lastAuction) ;
		
		flagTimer = false ;
		myTimer.setDelay(timeAllowed) ;
		myTimer.start() ;
	    } else
		{	System.out.println("Article sold yet") ;
		try { Thread.sleep(1000) ; }
		catch (InterruptedException e) { System.out.println("waiting...") ; }
		oneMoreTime() ;
		}
    } // sellArticle() ;
    
    
    
    /**
     * That method is called by this skill. It is called when you want to
     *  sell the remaider of your articles. This means that it is called
     *  by the sellAll method as many times as there are articles in the
     *  "remaining" Catalog.
     */
    public void sellArticle(Article a, Timer t)
    {
	Article toSell = a ;
	lastAuction = new Auction("none", a) ;
	perform(myAgent, "addMessage", "I am selling the following article :") ;
	perform(myAgent, "addMessage", a.toString() ) ;
	sendArticleDescription(lastAuction) ;
	
	flagTimer = false ;
	myTimer = t ;
	myTimer.start() ;
    }
    
    
    /**
     * This method ends the auctions for an article and broadcasts the
     * result to the bidders.
     **/
    private /*synchronized*/ void sendResult()
    {
	remaining.remove(lastAuction.getArticle()) ;
	Auction eoa = lastAuction ;
	perform("addMessage", "the auctions for this article are over!") ;
	perform(myAgent, "addMessage", "the result is :") ;
	perform(myAgent, "addMessage", eoa.toString()) ;
	perform(myAgent, "addMessage", "\n") ;
	for (int i=0 ; i<myBidders.size() ; i++ )
	    {
		perform( ((String)myBidders.elementAt(i)), "endArticle", eoa) ;
	    }
	
	sold.add(eoa) ;
	perform(myAgent, "updateResult", eoa.getArticle()) ;
	perform(myAgent, "updateOwners", eoa.getBidder()) ;
	flagAuctionFinished = false ;
    } // sendResult
    
    
    
    
    
    
    /**
     * This method is called by the interface manager. It sells all
     * the articles contained in the "remaining" Catalog.  */
    public void sellAll()
    {
		flagTimer = true ;
		currentArticle = chooseArticle(remaining) ;
		ti = new Timer(timeAllowed, new TIMEPAST()) ;
		ti.setRepeats(false) ;
		sellArticle(currentArticle, ti) ;		

	} // sellAll


	
	
	
	
	/**
	 *  This method is called by the bidders. It enables a bidder, and only one,
	 * to update the last auction. The auction is checked before beeing accepted.
	 * This prevents errors such as broadcasting an auction for an article that 
	 * has already been sold, or accepting a price inferior to its present value.
	 *
	 */
	public /*synchronized*/ void setLastAuction(Auction a)
	{
//		System.out.println(" We have been called in SetLastAuction") ;
		System.out.println("Receiving auction" + a.toString()) ;
		synchronized (lastAuction) {

			flag = false ;
			if ( ((String)(a.getArticle().getNom())).compareTo( ((String)lastAuction.getArticle().getNom()) )==0 )
			{
//				System.out.println("Good names") ;
				if (a.getArticle().getPrice() >= lastAuction.getArticle().getPrice())
				{
//					System.out.println("Good price!") ;
					lastAuction = a ;
					System.out.println(lastAuction.toString()) ;
					try {Thread.sleep(200) ;}
					catch (InterruptedException e) { System.out.println("waiting") ; }
					perform(myAgent, "addMessage", a.toString()) ;
					sendArticleDescription(lastAuction) ;
				}
			}
			myTimer.restart() ;

		} // synchronized
	} // setLastAuction
			

	/**
	 *  The action method of this agent.
	 * <BR>
	 *  It is quite empty. In fact, it has no use but having the interface
	 * showing the catalog. 
	 */
	public void action()
	{
		perform(myAgent, "printCatalog", catalog) ;
	}


	/**
	 *  This function sets the buttons from the "Auctions" panel to the
	 * enabled position.
	 *
	 */
	public void oneMoreTime()
	{ perform(myAgent, "enableAuctions") ; }
 

	/**
	 *  This ActionListener is the one for a timer that enables to sell
	 * only one article. When the timer ends, this ActionListener is called
	 * and calls the "sendResult" function. So the article is sold.
	 *
	 *
	 */
	class TIMEOUT implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			System.out.println(" Arret des encheres") ;
			sendResult() ;
			oneMoreTime() ;
			flagTimer = true ;
	 	}
	}


	/**
	 *  This ActionListener is the one for a timer that enables to sell
	 * all the remaining articles. When the timer ends, this ActionListener is called
	 * and calls the "sendResult" function. So the article is sold. The auctioneer
	 * then restarts it for another article.
	 *
	 */
	class TIMEPAST implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("Arret de l'enchere!") ;
			sendResult() ;
			if (remaining.size() > 0)
			{
				currentArticle = chooseArticle(remaining) ;				
				sellArticle(currentArticle, ti) ;
			} else
			{ System.out.println("fin") ; }
		}
	} // TIMEPAST
} // AuctioneerSkill





