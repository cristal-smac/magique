// Graph.java
package vente.gui;

import vente.objects.*;

import java.util.* ;

import java.awt.* ;
import javax.swing.* ;
import java.awt.event.* ;
import fr.lifl.magique.* ;
import fr.lifl.magique.skill.* ;
import fr.lifl.magique.skill.system.* ;
import fr.lifl.magique.platform.* ;

/**
 *
 */
public class Graph extends JFrame // implements java.io.Serializable
{   // int i=0;
    JTextArea catalog, qNames, rNames, rPrices, rOwners, messages, rBidders ;    
    JTextField  qTime, qNumber ;
    GraphAuctioneer myLinkWithMyAgent ;
    String myAgent ;
	 javax.swing.Timer myTimer = null ;
	 JButton qRun, qStep ;
//    JCheckBox mode ;
    
    public Graph(String a, GraphAuctioneer b)
    {
    	myLinkWithMyAgent = b ;
    	myAgent = a ;
	setSize(350,300);
	setTitle(myAgent);


	JTabbedPane j = new JTabbedPane();


	// first pannel, for the catalog
	JPanel p = new JPanel() ;
	p.setLayout(new BorderLayout()) ;
	catalog = new JTextArea() ;
	JScrollPane sp = new JScrollPane(catalog);
	sp.createVerticalScrollBar();
	p.add(sp, BorderLayout.CENTER);
	catalog.setText("  Empty") ;	


	// second panel for the auctions
	JPanel q = new JPanel() ;
	q.setLayout(new GridLayout(4,2)) ;
	


	JLabel qLabel1 = new JLabel(" Time for an auction (ms)") ;
	q.add(qLabel1) ;
	qTime = new JTextField("10000", 7) ;
	q.add(qTime) ;
	
	
	
	
	
	JLabel qLabel2 = new JLabel("") ;
	q.add(qLabel2) ;
	
	JLabel qJ1 = new JLabel(" Sell one article ") ;
//	qJ1.setBounds(150, 0, 200, 50) :
	q.add(qJ1) ;
	
	JLabel qJ2 = new JLabel(" Enter the number of the article to sell ") ;
//	qJ2.setBounds(50, 50, 150, 50) ;
	q.add(qJ2) ;
	
	qNumber = new JTextField(4) ;		
//	qNumber.setBounds(200, 100, 50, 20) ;
	q.add( qNumber) ;
	
	qStep = new JButton("Sell it") ;
	qStep.addActionListener(new BSELL()) ;
//	qStep.setBounds(125, 100, 150, 50) ;
	q.add(qStep) ;
	
	qRun = new JButton("Run") ;
	qRun.addActionListener(new BRUN()) ;
//	qRun.setBounds(100, 175, 150, 50) ;
	q.add(qRun) ;
	



	// third panel for the result
	JPanel r = new JPanel() ;
	r.setLayout(new GridLayout()) ;
	GridBagConstraints cr = new GridBagConstraints() ;
	// for the window appearence	
	cr.weightx = 10 ;
	cr.ipadx = 10 ;
	
	
	
	// the first part: the articles' names
	rNames = new JTextArea() ;
	cr.gridx = 0 ;
	cr.gridy = 0 ;
	cr.gridwidth = 1 ;
	cr.gridheight = 1 ;
	r.add(rNames, cr) ;
	rNames.setText("Articles sold") ;

	// the second part: the articles' prices
	rPrices = new JTextArea() ;
	cr.gridx = 1 ;
	cr.gridy = 0 ;
	cr.gridwidth = 1 ;
	cr.gridheight = 1 ;
	r.add(rPrices, cr) ;
	rPrices.setText("prices") ;

	// the third part: the articles' owners
	rOwners = new JTextArea() ;
	cr.gridx = 2 ;
	cr.gridy = 0 ;
	cr.gridwidth = 1 ;
	cr.gridheight = 1 ;
	r.add(rOwners, cr) ;
	rOwners.setText("articles' owners") ;

	
	
	
	// fourth panel for the messages
	JPanel s = new JPanel() ;
	s.setLayout(new BorderLayout()) ;
	messages = new JTextArea() ;
	JScrollPane ss = new JScrollPane(messages) ;
	ss.createVerticalScrollBar() ;
	s.add(ss, BorderLayout.CENTER) ;	
	messages.setText(" no messages") ;



	// fith panel for the bidders
	JPanel t = new JPanel() ;
	t.setLayout(new BorderLayout()) ;
	rBidders = new JTextArea() ;
	JScrollPane st = new JScrollPane(rBidders) ;
	st.createVerticalScrollBar() ;
	t.add(st, BorderLayout.CENTER) ;
	rBidders.setText(" not given yet") ;
	

	// end of the JTabbedPane definition	
	j.add("Catalog"  , p ) ;
	j.add("Auctions" , q ) ;
	j.add("Result"   , r ) ;
	j.add("Messages" , s ) ;
	j.add("Bidders"  , t ) ;

	getContentPane().add(j);
	setVisible(true) ;
    } // constructor

    // gestion de l'evt : click sur bouton    
    class BSELL implements ActionListener
    {
	public void actionPerformed(ActionEvent e)
	{
		qStep.setEnabled(false) ;
		qRun.setEnabled(false) ;
		Integer time = new Integer(qTime.getText().trim()) ;
		myLinkWithMyAgent.sendTime(time) ;
//		try {Thread.sleep(500) ;}
//		catch (InterruptedException ie) { System.out.println("waiting") ; }
		int i =  Integer.parseInt(qNumber.getText().trim()) ;
		myLinkWithMyAgent.beginSell(i) ;
	}
    }

    // gestion de l'evt : click sur bouton    
    class BRUN implements ActionListener
    {
	public void actionPerformed(ActionEvent e)
	{
		Integer time = new Integer(qTime.getText().trim()) ;
		myLinkWithMyAgent.sendTime(time) ;
		myLinkWithMyAgent.beginGlobalSell() ;
		qStep.setEnabled(false) ;
		qRun.setEnabled(false) ;
	}
    }

/********************************************************************************/


     protected void makebutton(String name,
                               GridBagLayout gridbag,
                               GridBagConstraints c) {
         Button button = new Button(name);
         gridbag.setConstraints(button, c);
         getContentPane().add(button);
     }
     
     protected void makeJTextField(String name,
                                   GridBagLayout gridbag,
                                   GridBagConstraints c) {
         JTextField jtf = new JTextField(name);
         gridbag.setConstraints(jtf, c);
         getContentPane().add(jtf);
     }
     
     protected void makeJLabel(String name,
                               GridBagLayout gridbag,
                               GridBagConstraints c) {
         JLabel jl = new JLabel(name);
         gridbag.setConstraints(jl, c);
         getContentPane().add(jl);
     }
     
     protected void makeJTextArea(String name,
                                   GridBagLayout gridbag,
                                   GridBagConstraints c) {
         JTextArea jta = new JTextArea();
         gridbag.setConstraints(jta, c);
         getContentPane().add(jta);
     }
/********************************************************************************/    

}
