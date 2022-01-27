/*
 * @(#)GraphicLink.java	1.0 04/05/99
 *
 * Copyright 1999 by Nadir Doghmane & Niquet Fabien
 */


package fr.lifl.magique.gui.draw;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;


public class GraphicLink {
 	
 	/**
   * The GraphicAgent representing the parent of this link
   */
  private GraphicAgent Parent;
 	
 	/**
   * The GraphicAgent representing the child of this link
   */
 	private GraphicAgent Child;
 	
 	/**
   * An integer representing the number of this link parent(used when load)
   */
 	private int NParent;
 	
 	/**
   * An integer representing the number of this link child(used when load)
   */
 	private int NChild;

 	/**
   * The boolean to know whether or not this link is direct
   */
  private boolean isDirect;

private boolean  reversearrowsmode=false;
  
 	/**
   * Constructs a new GraphicLink with the specified parent, child and type
   *
   * @param parent this link parent
   * @param child this link child
   * @param type this link type
   * @param direct a boolean to set if this link is direct
   */
 	public GraphicLink(GraphicAgent parent, GraphicAgent child,  boolean direct) {
 		Parent = parent; Child = child;  isDirect = direct;
 	}
 	
 	/**
   * Constructs a new GraphicLink with the specified parent, child and type
   *
   * @param parent the number of this link parent
   * @param child the number of this link child
   * @param type this link type
   * @param direct a boolean to set if this link is direct
   */
 	public GraphicLink(int Nparent, int Nchild, boolean isDirect) {
 		NParent = Nparent; NChild = Nchild;	 isDirect = isDirect;
 	}
 
public void  setReverseArrowMode(boolean value) {
	reversearrowsmode=value;
 }	
	  
 	/**
   * Tests if this link is selected
   *
   * @param P the point to tests if it's included in the link
   * @return a boolean to know whether or not this link is selected
   */
 	public boolean isSelected (Point P) {
 		if (!(Parent.isVisible && Child.isVisible)) return false;
 		
 		int y1,y2;
   	y1 = Parent.position().y + Parent.height() / 2;
    y2 = Child.position().y - Child.height() / 2 - 6;
 	  
 	  if ( P.y < Math.min(y1,y2) || P.y > Math.max(y1,y2) ) return false;
   	Line2D ligne = new Line2D.Double(new Point(Parent.position().x,y1),new Point(Child.position().x,y2));
   	return (ligne.ptLineDist(P) < 3);
 	}
  	
 	/**
   * Returns this link parent
   *
   * @return a GraphicAgent representing this link parent
   */
 	public GraphicAgent GetParent() {return Parent;}
  
 	/**
   * Returns this link child
   *
   * @return a GraphicAgent representing this link child
   */
  public GraphicAgent getChild() {return Child;}
 	
 	/**
   * Returns the number of this link parent
   *
   * @return an integer representing the number of this link parent
   */
 	public int getNParent() {return NParent;}
 	
 	/**
   * Returns the number of this link child
   *
   * @return an integer representing the number of this link child
   */
 	public int getNChild() {return NChild;}
  
 
  
	/**
 	 * Returns a boolean to indicate whether or not this link is direct
 	 *
 	 * @return <code>true</code> if this link is direct
 	 *         <code>false</code> otherwise
 	 */
  public boolean isDirect() {return isDirect;}
    
  /**
 	 * Draws this link
 	 *
 	 * @param g1 the graphic context to use to paint
	 * @param selected a boolean to indicate whether or not this link is selected
 	 * @param LColor the link color
 	 * @param SColor the selection color
 	 */
 	public void draw (Graphics g1, boolean selected, Color LColor, Color SColor) {
 		if ( Parent.isVisible() && Child.isVisible() ) {
   		Graphics2D g = (Graphics2D)g1;
   		int x1,y1,x2,y2,xm,ym;
   	
			g.setColor(LColor);
   		if (selected) g.setColor(SColor);
   		x1 = Parent.position().x ; x2 = Child.position().x ;
   		y1 = Parent.position().y + Parent.height() / 2;
   		y2 = Child.position().y - Child.height() / 2 - 6;
   		xm = (x2 + x1) / 2; ym = (y2 + y1) / 2;
   		Arc2D fleche = new Arc2D.Double (new Rectangle (xm-10,ym-10,20,20),0,1,Arc2D.PIE);
	if (reversearrowsmode)
		fleche.setAngleStart(new Point2D.Double (x2,y2));
else
   		fleche.setAngleStart(new Point2D.Double (x2,y1));
   		fleche.setAngleExtent(30); g.fill(fleche);
   		fleche.setAngleExtent(-30); g.fill (fleche);
   		g.drawLine(x1,y1,x2,y2);
  	}
 	}

 	/**
   * Tests if this link contains the specified GraphicAgent
   *
   * @param target the agent to test
   * @return a boolean to know whether or not this link contains the specified agent
   */
 	public boolean contains(GraphicAgent target) {return (target == Parent || target == Child);}

 	/**
   * Deletes this link
   */
  public void delete() {
  	if (!isDirect) {
    	Parent.removeChild (Child); Child.removeParent();
    }
    else {
      Child.removeSpecialParent(Parent); Parent.removeSpecialChild(Child);
    }
  }
  
 	/**
   * Tests if the current link and the specified one are the same
   *
   * @param l a GraphicLink
   * @return a boolean to know whether or not these links are equals
   */
  public boolean equals(GraphicLink l) {
		if (l.GetParent() == Parent && l.getChild() == Child) return true;
    return false;
  }
  public boolean compare(GraphicLink l) {
	GraphicAgent childtmp=l.getChild();
	GraphicAgent parenttmp=l.GetParent();
	if (childtmp.getName().equals(Child.getName())&&parenttmp.getName().equals(Parent.getName())
          &&childtmp.getComputer().equals(Child.getComputer())&&parenttmp.getComputer().equals(Parent.getComputer()))
		return true;
return false;
}
}
