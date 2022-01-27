/*
 * @(#)ExecutionFrame.java	1.0 04/05/99
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

package fr.lifl.magique.gui.execute;
import bsh.*;
import bsh.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.*;
import java.net.*;
import java.io.*;


/**
 * This class is used to show the result of the execution of the configuration.
 * It shows all the output of its JVM
 */
public class FluxFrame extends JFrame {
  private JTextArea  area = new JTextArea();
  private JScrollPane bar;
    //  private  JVMInputServer streamServer;


  public FluxFrame(JConsole beanconsole) {
      super("Console Tools");
      area.setEditable(false);
      bar = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
      area.addMouseListener(new MouseAdapter() {
	      public void mousePressed(MouseEvent e) {         
		  if (SwingUtilities.isRightMouseButton(e)) 
		      area.setText("");
	      }
	  });
      addWindowListener(new WindowAdapter(){
       public void windowClosing(WindowEvent e) {
	   setVisible(false);
       }
	      public void windowDeiconified(WindowEvent e) {repaint();}
	  });   
      
      getContentPane().add(new JSplitPane(JSplitPane.VERTICAL_SPLIT,bar,beanconsole));
  }
    
    /**
     * Appends text on this frame
     * 
     * @param stream the text to be appended
     */
    public void addText(String stream) {
	invalidate();     
	area.append(stream+"\n"); validate();
    }  
    
}
