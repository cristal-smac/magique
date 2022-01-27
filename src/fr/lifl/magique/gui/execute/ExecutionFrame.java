/*
 * @(#)ExecutionFrame.java	1.0 04/05/99
 *
 * Copyright 1999 by Nadir Doghmane & Niquet Fabien
 */


package fr.lifl.magique.gui.execute;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.lang.Math;
import java.util.*;
import java.net.*;
import java.io.*;
import fr.lifl.magique.gui.draw.GraphicAgent;

/**
 * This class is used to show the result of the execution of the configuration.
 * It shows all the output of its JVM
 */
public class ExecutionFrame extends JInternalFrame {
  private JTextArea area = new JTextArea();
  private JScrollPane bar1;
  private JScrollPane bar2;
  private JDisplay canevas=new JDisplay ();
  private JTabbedPane onglet=new JTabbedPane();
  private boolean ishidden=false;
  private GraphicAgent root;
  private String computer;
  private boolean isActive = true;
  private boolean isFollowing = true;


  static ImageIcon vert=null;

  static ImageIcon rouge=null;

   /**
   * Constructs a new ExecutionFrame
   * 
   * @param Root the root of the tree executed in the JVM
   * @param desktop the JLayeredPane wher this frame is shown
   */
  public ExecutionFrame(GraphicAgent root, Container desktop) {
    super(root.getName(),true,false, false, false);
    if (vert==null)
	vert=new ImageIcon(this.getClass().getResource("Vert.gif"));
    if (rouge==null)
	rouge=new ImageIcon(this.getClass().getResource("Rouge.gif"));
    JMenuBar menuBar = new JMenuBar();
    JMenu menu1 =  (JMenu) menuBar.add(new JMenu("View")); 
    JMenuItem item1_1 =(JMenuItem)menu1.add(new JMenuItem("reset"));
    item1_1.addActionListener(new ActionListener() {  
   		public void actionPerformed(ActionEvent e) {
               area.setText("");
            }
    }); 
    JCheckBoxMenuItem follow = (JCheckBoxMenuItem) menu1.add(new JCheckBoxMenuItem("following")); 
    follow.setSelected(true);
    follow.addItemListener(new ItemListener() {
   	public void itemStateChanged(ItemEvent e) {
	  if (e.getStateChange()==ItemEvent.SELECTED)
           isFollowing=true;
         else
           isFollowing=false;
       }
     });
    setFrameIcon(rouge);
    this.root=root;
    bar1 = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
    area.setEditable(false);
    area.addMouseListener(new MouseAdapter() {
       public void mousePressed(MouseEvent e) {         
         if (SwingUtilities.isRightMouseButton(e)) 
           setHidden(true);
        }
    });
    canevas.addMouseListener(new MouseAdapter() {
       public void mousePressed(MouseEvent e) {         
         if (SwingUtilities.isRightMouseButton(e)) 
           setHidden(true);
        }
    });
   bar2 = new JScrollPane(canevas,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
    onglet.addTab("Text",bar1);
    onglet.addTab("Graphic",bar2);	
    setJMenuBar(menuBar);
    getContentPane().add(onglet,BorderLayout.CENTER);
    setBounds(0,0,200,200); setVisible(true);
    desktop.add(this,JLayeredPane.POPUP_LAYER);
  }
  
  /**
   * Appends text on this frame
   * 
   * @param stream the text to be appended
   */
  public void addText(String stream) {
    invalidate(); area.append(stream+"\n"); validate();
    if (isFollowing) {
       int height=(int)area.getPreferredSize().getHeight();
      // int height=(int)area.getPreferredScrollableViewportSize().getHeight();
       area.scrollRectToVisible(new Rectangle(0,height-5,5,1));
    }
  }
  

  public void addImage (Image image) {
    canevas.setImage(image);
  }  
  
  /**
   * Returns a boolean to indicate whether or not this frame is still active
   * 
   * @return <code>true</code> if the execution still continues
 	 *         <code>false</code> otherwise
 	 */
  public boolean isActive() {return isActive;}
  
  /**
   * Returns the computer where its JVM runs
   * 
   * @return a string representing the computer of its JVM
 	 */
  public String getComputer() {return root.getComputer();}

  public void kill() {
        isActive = false; setVisible(false);
	 dispose();
  }
  public void setDeployed () {
    setFrameIcon(vert);
    repaint();
  }
  public void setLaunch() {
    setFrameIcon(vert);
    repaint();
  }
  public boolean isHidden() {
    return ishidden;
  }
  public void setHidden (boolean show) {
    ishidden=show;
    setVisible(!show);
  }
  public void setVisible(boolean show) {
    if (!ishidden||show==false)
      super.setVisible(show);
}
}
class JDisplay extends JComponent {
  Image image=null;
  public JDisplay () {
      setAutoscrolls(true);
  } 
  public void paint(Graphics g) {update(g);}
  public void update(Graphics g) {
	if (image!=null) {
	  Dimension d=getSize();
	  if (image.getWidth(null)>d.width||image.getHeight(null)>d.height)
           setSize(Math.max(d.width,image.getWidth(null)),Math.max(d.height,image.getHeight(null)));
	  g.drawImage(image,0,0,null);
      }
  }
  public void setImage(Image image) {
    setSize(10,10);
	this.image=image;
	repaint();
  }
}
