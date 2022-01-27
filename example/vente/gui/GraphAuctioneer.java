// GraphAuctioneer.java
package vente.gui;

import vente.objects.*;

import java.util.* ;
import java.lang.* ;
import javax.swing.* ;
import java.awt.event.* ;
import fr.lifl.magique.* ;
import fr.lifl.magique.skill.* ;
import fr.lifl.magique.skill.system.* ;
import fr.lifl.magique.platform.* ;

/**
 *
 */
public class GraphAuctioneer extends DefaultSkill /*implements ActionListener*/
{
	String myAgent ;
	Graph myGraph = null ;
	
	// constructor
	public GraphAuctioneer (Agent a) { 
		super(a) ;
		myAgent = a.getName() ;
		myGraph = new Graph(myAgent, this) ;
	}
	
	
	public void printCatalog(Catalog c)
	{
		String toPrint = c.toString() ;
		myGraph.catalog.setText(toPrint) ;	
	} // printCatalog
	
	public void beginSell(int i)
	{
		Integer index = new Integer(i) ;
//		Article toSell = (Article)askNow(myAgent, "getArticle", index) ;
//		perform(myAgent, "sellArticle", toSell) ;
		perform(myAgent, "sellArticle", index) ;
	}
	
	
	public void beginGlobalSell()
	{
		perform(myAgent, "sellAll") ;
	} // beginGlobalSell
	
	
	public void addMessage(String message)
	{
		synchronized (myGraph.messages)
		{
			myGraph.messages.append(message) ;
			myGraph.messages.append("\n") ;
		}
	} // addMessage

	public void updateResult(Article a)
	{
		myGraph.rNames.append("\n") ;
		myGraph.rNames.append(a.getNom()) ;
		String price = new String() ;
		price += a.getPrice() ;
		myGraph.rPrices.append("\n") ;
		myGraph.rPrices.append(price) ;

	} // updateResult
	
	public void updateOwners(String who)
	{
		myGraph.rOwners.append("\n") ;
		myGraph.rOwners.append(who) ;
	} // addMessage	

	public void printMyBidders(Vector v)
	{
		if (v == null || v.size() == 0)
		{
			myGraph.rBidders.setText(" no Bidders connected") ;
		}
		else
		{
			myGraph.rBidders.setText("") ;
			for (int i=0 ; i<v.size() ; i++)
			{
				myGraph.rBidders.append("\n") ;
				myGraph.rBidders.append(v.elementAt(i).toString()) ;
			} // for
		} // if - else
	} // printMyBidders
	
	public void sendTime(Integer time)
	{
		Integer timeSet = (Integer)askNow(myAgent, "setTime", time) ;
//		try {Thread.sleep(2000) ;}
//		catch (InterruptedException e) { System.out.println("waiting") ; }
	} // sendTime


	public void enableAuctions()
	{
		myGraph.qRun.setEnabled(true) ;
		myGraph.qStep.setEnabled(true) ;
	} // enableAuctions

	

} // GraphAuctioneer
