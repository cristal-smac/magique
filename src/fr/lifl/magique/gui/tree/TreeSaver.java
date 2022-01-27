
package fr.lifl.magique.gui.tree;

import java.awt.Color;
import javax.swing.*;
import javax.swing.JTree;
import javax.swing.tree.*;

import java.util.*;
import java.util.jar.JarFile;
import java.io.*;

import fr.lifl.magique.gui.file.*;

public class TreeSaver {
  private void saveTree  ( DefaultMutableTreeNode root, BufferedWriter out, boolean noROOT) throws Throwable{
    if (root.getAllowsChildren()) {
      if (noROOT) {
      out.write("DEBDOMAIN"+"\n");
      out.write("\""+root.toString()+"\""+"\n");
      }
      for (Enumeration e=root.children() ;e.hasMoreElements();)
        saveTree ((DefaultMutableTreeNode) e.nextElement(),out,true);
      if (noROOT)
        out.write("\n"+"FINDOMAIN"+"\n");
    }  
    else if (noROOT) {
      out.write("\""+root.toString()+"\" ");out.write(new Integer(((ComputerNode) root).getPort()).toString()+" ");
   }
  }
  public void save  (DefaultMutableTreeNode root, BufferedWriter out) throws Throwable{
      //    out.write(root.toString()+"\n");
    saveTree(root,out,false);
    out.close();
  }
}

