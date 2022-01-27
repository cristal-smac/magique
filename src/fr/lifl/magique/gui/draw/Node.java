/*
 * @(#)Node.java	1.0 04/05/99
 *
 */

package fr.lifl.magique.gui.draw;
import javax.swing.*;
import java.util.*;

/**
 * A class that represents a node
 *
 * @version 1.0 12/05/99
 * @author Nadir Doghmane
 * @author Fabien Niquet
 */

public class Node extends JComponent{

 	/**
   * The node representing the parent of this node
   */
  protected Node parent;
 	
 	/**
   * The vector representing the children of this node
   */
 	protected Vector VChild = new Vector();
	
 	/**
   * The vector representing the special parents (the parents with a direct connection) of this node
   */
	protected Vector VSpecialParent = new Vector();

 	/**
   * The vector representing the special children (the children with a direct connection) of this node
   */	
	protected Vector VSpecialChild = new Vector();
    

 	/**
   * Sets the parent of this node
   * @param parent the Node to be set as the parent of this node
   */
  public void setParent(Node parent) {this.parent = parent;}
 	/**
   * Appends Parent to the end of this node's special parents
   *
   * @param Parent the Node to be added
   * @return  <code>true</code> if and only if the addition succeeded;
   *          <code>false</code> otherwise
   */
  public boolean addSpecialParent (Node Parent) {
   	 if (VSpecialParent.contains(Parent) || this == Parent) return false;
     VSpecialParent.addElement (Parent);return true;
 	 }
 	/**
   * Appends Child to the end of this node's children
   *
   * @param Child the Node to be added
   * @return  <code>true</code> if and only if the addition succeeded;
   *          <code>false</code> otherwise
   */
 	public boolean addChild (Node Child) {
    if (VChild.contains(Child) || this == Child) return false;
	  VChild.addElement(Child); return true;
	}
 	/**
   * Appends Child to the end of this node's special children
   *
   * @param Child the Node to be added
   * @return  <code>true</code> if and only if the addition succeeded;
   *          <code>false</code> otherwise
   */
  public boolean addSpecialChild (Node Child) {
  	if (VSpecialChild.contains(Child) || this == Child) return false;
    VSpecialChild.addElement (Child); return true;
  }

  /**
   * Returns this node's parent or null if this node has no parent
   *
   * @return this node's parent
   */
  public Node GetParent() {return parent;}
	/**
   * Returns the children of this node
   *
   * @return a vector representing the children of of this node
   */
  public Vector getChildren() {return VChild;}	
  /**
   * Returns this node's special parents
   *
   * @return  a vector representing this node's special parents
   */
  public Vector getSpecialParent() {return VSpecialParent;}
  /**
   * Returns this node's special children
   *
   * @return  a vector representing this node's special children
   */
  public Vector getSpecialChildren() {return VSpecialChild;}

  /**
   * Removes the parent of this node
   */
  public void removeParent() {parent=null;}
	/**
   * Removes the node parent from this node's special parents
   *
   * @param parent the Node to be removed
   */
  public void removeSpecialParent(Node parent) {VSpecialParent.remove(parent);}
	/**
   * Removes the node child from this node
   * @param child the Node to be removed
   */
  public void removeChild(Node child) {VChild.remove(child);}
	/**
   * Removes the node child from this node's special children
   *
   * @param child the Node to be removed
   */
  public void removeSpecialChild(Node child) {VSpecialChild.remove(child);}

  /**
   * Tests if the specified node is a Child of this node
   * @param tmp the Node to test to know if it's a Child of the node
   * @return  <code>true</code> if tmp is a Child of this node
   *          <code>false</code> otherwise
   */
  public boolean isAParent(Node tmp) {
    if (VChild.contains(tmp)) return true;
    for (int i=0; i < VChild.size(); i++)
      if (((Node) VChild.elementAt(i)).isAParent(tmp)) return true;
    return false;
  }
  
  /**
   * Deletes this node
   */
 	public void delete() {
	 	if (parent != null)	
	 		parent.removeChild(this);
  	for (int i=0; i < VChild.size(); i++)
     	((Node) VChild.elementAt(i)).removeParent();
  	for (int i=0; i < VSpecialChild.size(); i++)
     	((Node) VSpecialChild.elementAt(i)).removeSpecialParent(this);     	
  }

  /**
   * Tests if the current node has already a parent
   * @return  <code>true</code> if the node has already a parent
   *          <code>false</code> otherwise
   */
  public boolean hasAlreadyParent() {return (parent != null);}

}
