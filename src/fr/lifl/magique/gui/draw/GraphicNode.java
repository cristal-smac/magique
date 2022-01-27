/*
 * @(#)GraphicNode.java	1.0 04/05/99
 *
 * Copyright 1999 by Nadir Doghmane & Niquet Fabien
 */

package fr.lifl.magique.gui.draw;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;



/**
 * A class that is the graphic representation of a node
 *
 * @version 1.0 04/05/99
 * @author Nadir Doghmane
 * @author Fabien Niquet
 */

public class GraphicNode extends Node {
 	
 	/**
   * The rectangle which is the graphic representation of the node
   */
  protected Rectangle rect;
  
  /**
   * The center of the rectangle
   */
  protected Point Center;
  
  /**
   * The width of the node name
   */
  protected int Namewidth;
  
  /**
   * The boolean to know whether or not this node is visible on the screen
   */
  protected boolean isVisible;

  /**
   * The boolean to know whether or not this node is collapsed
   */
  protected boolean isCollapsed;

  /**
   * The boolean to know whether or not this node is to collapse (used only when load)
   */
  protected boolean toCollapse;


 	/**
 	 * Creates a GraphicNode
 	 */
 	public GraphicNode() {
  	rect = new Rectangle(new Point(0,0),new Dimension(75,44));
   	isVisible = true; isCollapsed = false;
 	}	
 	
 	/**
 	 * Returns the center of this node
 	 *
 	 * @return a point representing this node's center
 	 */
 	public Point position() {return Center;}
  
  /**
 	 * Changes the center of this node
 	 *
 	 * @param P  the new location of this node's center
 	 */
 	public void changePosition(Point P) {
   	Point pm = new Point (P.x-rect.width/2, P.y-rect.height/2);
   	if (isVisible) {Center = P; rect.setLocation(pm);}
  }
	
	/**
 	 * Changes the location of this node
 	 *
 	 * @param P  the new location of this node
 	 */
  public void changePositionDeb(Point P) {
   	Center = new Point (P.x+rect.width/2, P.y+rect.height/2);
   	rect.setLocation(P);
  }
  
  /**
 	 * Shifts this node
 	 *
 	 * @param decalx  the horizontal shifting
 	 * @param decaly  the vertical shifting
 	 */
  public void shiftPosition(int decalx, int decaly) {
   	if (isVisible) {
   		Center = new Point(Center.x+decalx, Center.y+decaly);
   		Point pm = new Point (Center.x-rect.width/2, Center.y-rect.height/2);
   		rect.setLocation(pm);
   		for (int i=0; i < VChild.size(); i++)
   			((GraphicNode) VChild.elementAt(i)).shiftPosition(decalx,decaly);
   	}
  }
    
  /**
 	 * Sets the specified boolean to indicate whether or not this node is visible
 	 *
 	 * @param visible the boolean to be set
 	 */
 	public void setVisible(boolean visible) {
 		isVisible = visible;
 		if ( !isCollapsed && isVisible) {
 			for (int i=0; i < VChild.size(); i++)
        ((GraphicNode) VChild.elementAt(i)).setVisible(true);
    }
    else if ( !isVisible ) {
    	for (int i=0; i < VChild.size(); i++)
        ((GraphicNode) VChild.elementAt(i)).setVisible(false);
    }
 	}
	
	/**
 	 * Expand this node
 	 */
 	public void Expand() {
 		if (VChild.isEmpty()) ;
 		else {
 			isCollapsed = false;
 			for (int i=0; i < VChild.size(); i++)
      	((GraphicNode) VChild.elementAt(i)).setVisible(true);
    }
 	}
	
	/**
 	 * Expand this node and all its children
 	 */
 	public void ExpandAll() {
		Expand();
		for (int i=0; i < VChild.size(); i++)
     	((GraphicNode) VChild.elementAt(i)).ExpandAll();
 	}
	
	/**
 	 * Collapse this node
 	 */
 	public void Collapse() {
 		if (VChild.isEmpty()) ;
 		else {
 			isCollapsed = true;
 			for (int i=0; i < VChild.size(); i++)
      	((GraphicNode) VChild.elementAt(i)).setVisible(false);
    }
 	}
	
	/**
 	 * Collapse this node all its children
 	 */
 	public void CollapseAll() {
		Collapse();
		for (int i=0; i < VChild.size(); i++)
      	((GraphicNode) VChild.elementAt(i)).CollapseAll();
 	}
 		
	/**
 	 * Sets that this node is to collapse (used only with load)
 	 */
 	public void setToCollapse() {toCollapse = true;}
	
	/**
 	 * Tests if this node is selected
 	 *
 	 * @param P the position to test if it's included in the node
 	 * @return 1 if this node is selected
 	 *         2 if a link is selected
 	 *         0 otherwise
 	 */
  public int isSelected(Point P) {
  	if (isVisible) {
    	if(new Rectangle(new Point(Center.x-4,Center.y+rect.height/2-4),new Dimension(8,8)).contains(P))
      	return 3;      
      if (rect.contains(P)) 
      	return 1;
    	Arc2D arc = new Arc2D.Double(Center.x-12,rect.y-12,24,24,0,360,Arc2D.PIE);
    	if (arc.contains(P))
    		return 2;
  	  return 0;
  	}
  	else return 0;
  }
  
  /**
 	 * Tests if this node is selected
 	 *
 	 * @param R the rectangle to test if it intersects the node
 	 * @return  <code>true</code> if this node is selected
   *          <code>false</code> otherwise
 	 */
 	public boolean isSelected(Rectangle R) {
    if (isVisible) return R.intersects(rect);
    else return false;
  }    
  
	/**
 	 * Returns a boolean to indicate whether or not this node is visible
 	 *
 	 * @return <code>true</code> if this node is visible
 	 *         <code>false</code> otherwise
 	 */
 	public boolean isVisible() {return isVisible;}
  
  /**
 	 * Returns a boolean to indicate whether or not this node is collapsed
 	 *
 	 * @return <code>true</code> if this node is collapsed
 	 *         <code>false</code> otherwise
 	 */
 	public boolean isCollapsed() {return isCollapsed;}

  /**
 	 * Returns a boolean to indicate whether or not this node is to collapse
 	 *
 	 * @return <code>true</code> if this node is to collapse
 	 *         <code>false</code> otherwise
 	 */
 	public boolean isToCollapse() {return toCollapse;}
  
	/**
 	 * Returns the width of this node
 	 *
 	 * @return the width dimension
 	 */
 	public int width() {return rect.width;}

	/**
 	 * Returns the height of this node
 	 *
 	 * @param the height dimension
 	 */
 	public int height() {return rect.height;}
	
}
