// GraphC.java
package vente.gui;

import vente.objects.*;

import java.util.* ;
import java.awt.* ;
import javax.swing.* ;
import java.awt.event.* ;
import fr.lifl.magique.* ;
import fr.lifl.magique.skill.* ;
import fr.lifl.magique.platform.* ;

/**
 *
 */
public class GraphC extends JFrame // implements java.io.Serializable
{   // int i=0;
    JTextArea catalog, qNames, rNames, rPrices, rOwners, rComment, rLeft, messages, rBidders, tChoosen, uRemTxt, uRemValue, uMonTxt, uMon ;    
    JTextField qNumber, qAmount ;
    GraphBidder myLinkWithMyAgent ;
    JComboBox qStrat = null ;
	 JButton bConnect, bLeave ;
    String myAgent ;
//    JCheckBox mode ;
    
    public GraphC(String a, GraphBidder b)
    {
    	myLinkWithMyAgent = b ;
    	myAgent = a ;
	setSize(400,250);
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


	// second panel for the init
	JPanel q = new JPanel() ;
	q.setLayout(new GridLayout(5,2)) ;

	JLabel qPort = new JLabel("IP of the server") ;
	try {
	    String ipAddress = (java.net.InetAddress.getLocalHost()).getHostAddress();
	    qNumber = new JTextField(ipAddress,15);	
	    //"134.206.100.102", 15) ; 
	}
	catch( Exception e) { e.printStackTrace();}
	q.add(qPort) ;
	q.add(qNumber) ;
	
	// the second block : for the amount of money
	JLabel qJ1 = new JLabel(" Amount of money ") ;
	qAmount = new JTextField(" 450 ", 10) ; 
	q.add(qJ1) ;
	q.add(qAmount) ;
	
	//the third block : to choose a strategy
	JLabel qS1 = new JLabel(" Strategy") ;
	q.add(qS1) ;
	qStrat = new JComboBox() ;
	qStrat.addItem("Agressive") ;
	qStrat.addItem("Penible") ;
	qStrat.addItem("Careful") ;
	qStrat.setEditable(true) ;
	qStrat.setEnabled(true) ;
	qStrat.setSelectedIndex(1) ;
	q.add(qStrat) ;
	
	bConnect = new JButton("connect") ;
	bConnect.addActionListener(new BCONNECT()) ;
	q.add(bConnect) ;
	
	JLabel qLabelVoid = new JLabel("") ;
	q.add(qLabelVoid) ;
	
	bLeave = new JButton("Leave") ;
	bLeave.addActionListener(new BDISCONNECT()) ;
	bLeave.setEnabled(false) ;
	q.add(bLeave) ;
	

	// third panel for the result
	JPanel r = new JPanel() ;
	GridLayout gridBagR = new GridLayout() ;
	r.setLayout(gridBagR) ;

	// the first part: the articles' names
	rNames = new JTextArea() ;
	r.add(rNames) ;
	rNames.setText(" Articles") ;

	// the second part: the articles' prices
	rPrices = new JTextArea() ;
	r.add(rPrices) ;
	rPrices.setText(" Prices") ;

	// the third part: the articles' owners
	rOwners = new JTextArea() ;
	r.add(rOwners) ;
	rOwners.setText(" Won?") ;

	// the fourth part: did I want this article?
	rComment = new JTextArea() ;
	r.add(rComment) ;
	rComment.setText(" Wanted?") ;

	// the fith part: how much money do I have?
	rLeft = new JTextArea() ;
	r.add(rLeft) ;
	rLeft.setText("Saved") ;
	
	
	
	// fourth panel for the messages
	JPanel s = new JPanel() ;
	s.setLayout(new BorderLayout()) ;
	messages = new JTextArea() ;
	JScrollPane ss = new JScrollPane(messages) ;
	ss.createVerticalScrollBar() ;
	s.add(ss, BorderLayout.CENTER) ;	
	messages.setText(" no messages") ;



	// fith panel for the choosen articles
	JPanel t = new JPanel() ;
	t.setLayout(new BorderLayout()) ;
	tChoosen = new JTextArea() ;
	JScrollPane st = new JScrollPane(tChoosen) ;
	st.createVerticalScrollBar() ;
	t.add(st, BorderLayout.CENTER) ;
	



	// end of the JTabbedPane definition	
	j.add("Catalog"  , p ) ;
	j.add("Init"     , q ) ;
	j.add("Result"   , r ) ;
	j.add("Messages" , s ) ;
	j.add("chosen"   , t ) ;
//	j.add("debug"    , u ) ;

	getContentPane().add(j);
	setVisible(true) ;
    } // constructor


    class BCONNECT implements ActionListener
    {
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("appui sur le bouton connect");
		bConnect.setEnabled(false) ;
		qStrat.setEnabled(false) ;
		qNumber.setEnabled(false) ;
		qAmount.setEnabled(false) ;
		bLeave.setEnabled(true) ;
		int i = Integer.parseInt(qAmount.getText().trim()) ;
		String strat = qStrat.getSelectedItem().toString() ;
		String ip = qNumber.getText().trim() ;
		System.out.println("appel de la fonction connect");
		myLinkWithMyAgent.connect(i, strat, ip) ;
	}
    }
    
	class BDISCONNECT implements ActionListener
	{
    		public void actionPerformed(ActionEvent e)
    		{
	    		bConnect.setEnabled(true) ;
    			qStrat.setEnabled(true) ;
    			qNumber.setEnabled(true) ;
    			bLeave.setEnabled(false) ;
    			myLinkWithMyAgent.disconnect() ;
    		}
	} // BDISCONNECT

/********************************************************************************/

     protected void makebutton(String name,
                               GridBagLayout gridbag,
                               GridBagConstraints c) {
         Button button = new Button(name);
         gridbag.setConstraints(button, c);
         add(button);
     }

/********************************************************************************/    

}
