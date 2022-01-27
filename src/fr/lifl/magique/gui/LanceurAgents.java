/*
 * @(#)LanceurAgents.java	1.0 04/05/99
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

/**
 * Interface that	can	distribute a multi-agents	system
 * on	a	network	of computers
 *
 * @version	1.2	25/03/99
 * @author DOGHMANE	Nadir	&	NIQUET Fabien
 */
package fr.lifl.magique.gui;
import fr.lifl.magique.gui.theinterface.*;
import java.io.*;
import java.net.*;
import fr.lifl.magique.gui.draw.JProgressFrame;

import fr.lifl.magique.platform.classloader.BytecodeClassLoader;
import java.lang.reflect.*;

public class LanceurAgents {
    //    static JProgressFrame progress=new JProgressFrame();
    static Object frame;
    public static	void main( String[]	args ) {	 
	
	//	progress.setVisible(true);
	
	//	    if (progress==null) System.out.println("ERREUR");
	try {	
	    BytecodeClassLoader myLoader = new BytecodeClassLoader();
	    Class cl = myLoader.loadClass("fr.lifl.magique.gui.theinterface.MultiAgentsFrame");
	    Constructor co = cl.getConstructor(new Class[]{Class.forName("java.lang.String")}); //,
	    //							   Class.forName("fr.lifl.magique.gui.draw.JProgressFrame")});
	    
	    frame = co.newInstance(new Object[]{"Magique"});//,progress});
	    	    
	    Method m = fr.lifl.magique.util.ClassUtil.getMethod(cl,
								"setVisiBle",
								new String[]{"java.lang.Boolean"} );
	    
	    Boolean visible = Boolean.TRUE;
	    if(args.length!=0) {
		visible = Boolean.FALSE;
	    }
	    
	    m.invoke(frame, new Object[]{visible});
	}
	catch(Exception e) {
	    e.printStackTrace();
	}

	//	progress.setVisible(false);
	//progress.dispose();
    }
}

