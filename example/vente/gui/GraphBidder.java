// GraphBidder.java
package vente.gui;

import vente.objects.*;

import java.util.* ;
import javax.swing.* ;
import java.awt.event.* ;
import fr.lifl.magique.* ;
import fr.lifl.magique.skill.* ;
import fr.lifl.magique.platform.* ;

/**
 *
 */
public class GraphBidder extends DefaultSkill
{
	String myAgent ;
	GraphC myGraph = null ;
	String myBoss ;
	
	// constructor
	public GraphBidder (Agent a, String s) { 
		super(a) ;
		myAgent = a.getName() ;
		myGraph = new GraphC(myAgent, this) ;
		myBoss = s ;
	}
	
	
	public void printCatalog(Catalog c)
	{
		String toPrint = c.toString() ;
		myGraph.catalog.setText(toPrint) ;
		addMessage(" Receiving the catalog...") ;
	}
	
	public void connect(int i, String strat, String ip)
	{
		System.out.println("arrivee dans la fonction bidder de GraphBidder");
		System.out.println("appel de la fonction connectToBoss");
		StringTokenizer mbl = new StringTokenizer(myBoss, "@") ;
		String myBossLong = (String)mbl.nextElement() ;
		myBossLong += "@" + ip + ":4444" ;
		System.out.println("Connection to" + myBossLong) ;
		connectTo(myBossLong) ;
		myBoss = myBossLong ;
		perform(myAgent, "setBoss", myBoss) ;
		System.out.println("fin du connectToBoss");
		System.out.println("appel du setCatalog");
		perform(myBoss, "sendCatalog", myAgent) ;
		Integer money = new Integer(i) ;
		System.out.println("appel de la fonction setMoney");
		perform(myAgent, "setMoney", money) ;
		System.out.println("appel de la fonction setStrategy");
		perform(myAgent, "setStrategy", strat ) ;
	}


	public void addMessage(String message)
	{
		synchronized (myGraph.messages)
		{
			myGraph.messages.append(message) ;
			myGraph.messages.append("\n") ;
		} // synchronized
	} // addMessage


	public void articleWon(Article a, String comment, Integer rem)
	{
		myGraph.rNames.append("\n") ;
		myGraph.rPrices.append("\n") ;
		myGraph.rOwners.append("\n") ;
		myGraph.rComment.append("\n") ;
		myGraph.rLeft.append("\n") ;
		myGraph.rNames.append(a.getNom()) ;
		String s = new  String() ;
		s += a.getPrice() + " $" ;
		myGraph.rPrices.append(s) ;
		myGraph.rOwners.append(" Yes") ;
		myGraph.rComment.append(comment) ;
		myGraph.rLeft.append(rem.toString()) ;
	} // articleWon

	public void articleLost(Article a, String comment, Integer rem)
	{
		myGraph.rNames.append("\n") ;
		myGraph.rPrices.append("\n") ;
		myGraph.rOwners.append("\n") ;
		myGraph.rComment.append("\n") ;
		myGraph.rLeft.append("\n") ;
		myGraph.rNames.append(a.getNom()) ;
		String s = new  String() ;
		s += a.getPrice() + " $" ;
		myGraph.rPrices.append(s) ;
		myGraph.rOwners.append(" No") ;
		myGraph.rComment.append(comment) ;
		myGraph.rLeft.append(rem.toString()) ;
	} // articleLost

	
	public void setChoosen(Catalog v)
	{
		myGraph.tChoosen.setText(v.toString()) ;
	}


	public void setRemaining(Integer rem)
	{
		String mon = new String() ;
		mon += rem.toString() ;
		myGraph.uRemValue.setText(mon) ;
	}
	
	public void disconnect()
	{
		perform(myBoss, "disconnectMe", myAgent) ;
		perform(myAgent, "askForDisconnectionFrom", myBoss) ;
	} // disconnect

} // GraphAuctioneer
