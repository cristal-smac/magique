/*
 * @(#)TreeCreator.java    1.0 04/05/99
 *
 * Copyright 1999 by Nadir Doghmane & Niquet Fabien
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Nadir Doghmane & Niquet Fabien("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Nadir Doghmane & Niquet Fabien.
 */

package fr.lifl.magique.gui.tree;

import java.awt.Color;
import java.awt.*;
import javax.swing.*;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;
import java.util.jar.JarFile;
import java.io.*;
import fr.lifl.magique.gui.network.PingNetwork;

import fr.lifl.magique.gui.file.*;

public class TreeCreator {
  StreamTokenizer comp;
  String pathrac;
		DefaultTreeModel treeModel;
  	DirectoryFilter filtre1 = new DirectoryFilter();
  	JarFilesFilter filtre2 = new JarFilesFilter();
  	ClassFilesFilter filtre3 = new ClassFilesFilter();

  
  /**
   * Creates a new TreeCreator to create a computers network
   *
   * @param st the StreamTokenizer where to read the network description
   */
  public TreeCreator(StreamTokenizer st) {comp = st;}
  /**
   * Creates a new TreeCreator to create a class browser
   *
   * @param path the path to browse to get classes
   */
  public TreeCreator(String path) {pathrac = path;}  
  
  /**
   * Creates a the computers network
   *
   * @return a JTree representing the network
   */
  public DefaultTreeModel createDomainHierarchy() {
  	DefaultTreeModel treeModel = new DefaultTreeModel(createNetwork(),true);
    treeModel.setAsksAllowsChildren(true);
    
		 //Create a tree that allows one selection at a time
    return treeModel;
  }
  
  private DefaultMutableTreeNode createNetwork() {
    Vector domains = new Vector();
  	DefaultMutableTreeNode root = null, tmp = null;

        boolean already_read=false;
	root=new DefaultMutableTreeNode("",true);
	  try {
   	        domains.add(root); comp.nextToken();
   		while ( comp.ttype != StreamTokenizer.TT_EOF ) {
                        if ( comp.sval.toLowerCase().equals("debdomain") ) {
                          comp.nextToken();
     			tmp = new DefaultMutableTreeNode(comp.sval,true);
     			((DefaultMutableTreeNode)domains.elementAt(domains.size()-1)).add(tmp);
     			domains.add(tmp); 
   			}
   			else if ( comp.sval.toLowerCase().equals("findomain") )
           domains.remove(domains.size()-1);
   			else {
                        tmp = new ComputerNode(comp.sval);
                        comp.nextToken();
                        if (comp.ttype!=StreamTokenizer.TT_NUMBER)
                          already_read=true;
                        else 
                          ((ComputerNode) tmp).setPort((int)comp.nval);
     			((DefaultMutableTreeNode)domains.elementAt(domains.size()-1)).add(tmp);
   			}
                        if (!already_read)
                          comp.nextToken();
                        already_read=false;
   		}
  	}
  	catch (Throwable t) {
                System.out.println("Error while reading Computers Properties");
		root.add(new ComputerNode("127.0.0.1"));
	}
  	return root;  	    	  
  }
  	
  /**
   * Creates a the class browser
   *
   * @return a JTree representing the classes hierarchy
   */
  public JTree createClassesTree() {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(pathrac);
   	DefaultMutableTreeNode dir = new DefaultMutableTreeNode("..",true); root.add(dir);
   	createClassesHierarchy(root,pathrac);
      treeModel = new DefaultTreeModel(root,true);
   	treeModel.setAsksAllowsChildren(true);
    
   	//Create a tree that allows one selection at a time
   	JTree classes = new JTree(treeModel);
   	classes.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
 	classes.addTreeWillExpandListener(new TreeWillExpandListener(){
		public void treeWillCollapse(TreeExpansionEvent e) {}
		public void treeWillExpand(TreeExpansionEvent e) {
		  
		  createClassesHierarchy(e.getPath(),(JTree)e.getSource());
	}
	});
	  //Set the properties of the nodes
    classes.setCellRenderer(setRenderer());
    return classes;
  }    
 public void createClassesHierarchy(TreePath treepath,JTree t) {

	DefaultMutableTreeNode rac=(DefaultMutableTreeNode) treepath.getLastPathComponent();
	DefaultMutableTreeNode dir = rac, file = null;
	DefaultTreeModel model=(DefaultTreeModel)t.getModel();
	//DefaultTreeModel model=treeModel;
	String name="";
	if (rac.getChildCount()!=0) return;
	  while (! dir.toString().equals(pathrac)) {
    		name = dir.toString() + System.getProperty("file.separator") + name;				
    		dir = (DefaultMutableTreeNode)dir.getParent();
		if (dir==null) return;
    	  }
		if (pathrac.endsWith(System.getProperty("file.separator")))
    		name = pathrac+name;
	else 
    		name = pathrac+System.getProperty("file.separator")+name;
	String path=name;	
	rac=(DefaultMutableTreeNode) treepath.getLastPathComponent();
	if (!new File(path).isDirectory()||path.indexOf("..")!=-1) return;
  	File Fich = new File(path);
	String Res1[] = Fich.list(filtre1), Res2[] = Fich.list(filtre2), Res3[] = Fich.list(filtre3), tmp;
      Arrays.sort(Res1); Arrays.sort(Res2); Arrays.sort(Res3); 		
	  for (int i=0; i < Res1.length; i++) {
   		if ( path.endsWith(System.getProperty("file.separator")) ) tmp = path+Res1[i];
   		else tmp = path+System.getProperty("file.separator")+Res1[i];
   		dir = new DefaultMutableTreeNode(Res1[i],true);
 
		 model.insertNodeInto(dir,rac,rac.getChildCount()); 
  	 }
   
      for (int i=0; i < Res2.length; i++) {
    	  dir = new DefaultMutableTreeNode(Res2[i],true); 
	  model.insertNodeInto(dir,rac,rac.getChildCount()); 
    	  name = dir.toString();
    	  DefaultMutableTreeNode node = (DefaultMutableTreeNode)dir.getParent();
    	  while (! node.toString().equals(pathrac)) {
    		name = node.toString() + System.getProperty("file.separator") + name;
    		node = (DefaultMutableTreeNode)node.getParent();
    	}
    	if (pathrac.endsWith(System.getProperty("file.separator")))
    		name = pathrac+name;    
	else 
    		name = pathrac+System.getProperty("file.separator")+name;
   		try {
  			JarFile f = new JarFile(name);
   			for (Enumeration e =f.entries();e.hasMoreElements();) {
     			String s = e.nextElement().toString();
     			if (s.endsWith(".class")) {
     				file = new DefaultMutableTreeNode(s,false);
//     				file = new DefaultMutableTreeNode(s,false);//.substring(pt2+1),false);
		 model.insertNodeInto(file,dir,dir.getChildCount());      		
     			}
   			}
   		}	
   		catch (IOException e) {
   			JOptionPane.showMessageDialog(null, "An error has occured when trying to read "+name,
         	"Error", JOptionPane.ERROR_MESSAGE);
      }
    }
   
		for (int i=0; i < Res3.length; i++) {
    	file = new DefaultMutableTreeNode(Res3[i],false); 
		 model.insertNodeInto(file,rac,rac.getChildCount()); 

   	}
  }





  private void createClassesHierarchy(DefaultMutableTreeNode rac, String path) {
  	DefaultMutableTreeNode dir = null, file = null;
     	File Fich = new File(path);
  	String Res1[] = Fich.list(filtre1), Res2[] = Fich.list(filtre2), Res3[] = Fich.list(filtre3), tmp;
	  Arrays.sort(Res1); Arrays.sort(Res2); Arrays.sort(Res3); 
	  for (int i=0; i < Res1.length; i++) {
   		if ( path.endsWith(System.getProperty("file.separator")) ) tmp = path+Res1[i];
   		else tmp = path+System.getProperty("file.separator")+Res1[i];
   		dir = new DefaultMutableTreeNode(Res1[i],true); 

		  rac.add(dir);
   		//createClassesHierarchy(dir,tmp);
  	}
   
    for (int i=0; i < Res2.length; i++) {
    	dir = new DefaultMutableTreeNode(Res2[i],true); rac.add(dir);
    	String name = dir.toString();
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)dir.getParent();
    	while (! node.toString().equals(pathrac)) {
    		name = node.toString() + System.getProperty("file.separator") + name;
    		node = (DefaultMutableTreeNode)node.getParent();
    	}
    	if (pathrac.endsWith(System.getProperty("file.separator")))
    		name = pathrac+name;
	else 
    		name = pathrac+System.getProperty("file.separator")+name;
    
   		try {
  			JarFile f = new JarFile(name);
   			for (Enumeration e =f.entries();e.hasMoreElements();) {
     			String s = e.nextElement().toString();
     			if (s.endsWith(".class")) {
     				file = new DefaultMutableTreeNode(s,false);
     				file = new DefaultMutableTreeNode(s,false);//.substring(pt2+1),false);
     				dir.add(file);
     			}
   			}
   		}	
   		catch (IOException e) {
   			JOptionPane.showMessageDialog(null, "An error has occured when trying to read "+name,
         	"Error", JOptionPane.ERROR_MESSAGE);
      }
    }
   
		for (int i=0; i < Res3.length; i++) {
    	file = new DefaultMutableTreeNode(Res3[i],false); rac.add(file);
   	}
  }

  /**
   * Sets the renderer of a JTree
   *
   * @param image the image to used to represent the leaves
   */
  public DefaultTreeCellRenderer setRenderer(PingNetwork available) {
    MyRenderer renderer = new MyRenderer(available);
    /*   if (new File("Images"+System.getProperty("file.separator")+image).exists())
	 renderer.setLeafIcon(new ImageIcon("Images"+System.getProperty("file.separator")+image));*/
    renderer.setBackgroundSelectionColor(Color.blue);
    renderer.setTextSelectionColor(Color.white);
    return renderer;
  }
  public DefaultTreeCellRenderer setRenderer() {
    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
    ImageIcon iconeclass=new ImageIcon(this.getClass().getResource("classe.gif"));
    if (iconeclass!=null) {
	renderer.setLeafIcon(iconeclass);
    }
    renderer.setBackgroundSelectionColor(Color.blue);
    renderer.setTextSelectionColor(Color.white);
    return renderer;
  }


}
class MyRenderer extends DefaultTreeCellRenderer {
    ImageIcon ready;
    ImageIcon unknown;
    ImageIcon actif;

    PingNetwork available;
    public MyRenderer (PingNetwork available) {
	this.available=available;
        ready=new ImageIcon(this.getClass().getResource("Vert.gif"));
	unknown=new ImageIcon(this.getClass().getResource("Orange.gif"));
	actif=new ImageIcon(this.getClass().getResource("station.gif"));
    }
    public Component getTreeCellRendererComponent (JTree tree,Object value,boolean sel,
						   boolean expanded,boolean leaf,int row,
						   boolean hasFocus) {
	super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);
	if (available!=null) {
          if (leaf) {
            int state=isAvailable(value);
            if (state==0)  
              setIcon(unknown);
            else if(state==1) 
              setIcon(actif);
            else if (state==2) 
              setIcon(ready);        
          } 
	}
	if (row==0) 
	    setIcon(actif);
	return this;
    }
    protected int isAvailable(Object value) {
	String nom;
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
	DefaultMutableTreeNode nodetmp = (DefaultMutableTreeNode)node.getParent();	
	String  CurrentComputer = node.toString();
	while (nodetmp!=null&&nodetmp.getParent()!=null){
	    CurrentComputer = CurrentComputer +"."+nodetmp.toString();
	    nodetmp = (DefaultMutableTreeNode)nodetmp.getParent();
	}
	try {
          int res=available.computerState(CurrentComputer);
          return res;
        }catch(Throwable t) {return 0;}
    }
}
