/*
 * @(#)JVMInputServer.java    1.0 04/05/99
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

package fr.lifl.magique.gui.network;
import fr.lifl.magique.gui.descriptor.*;
import fr.lifl.magique.platform.*;

import java.util.*;
import java.net.*;
import java.io.*;


class NetworkTest extends Thread {
    Hashtable computers;
    Hashtable resultat;
    Platform platform;
    public NetworkTest (Hashtable computers,Hashtable resultat,Platform platform) {
	this.platform = platform;
	this.computers=computers;this.resultat=resultat;
    }
    public void run () {
	for (Enumeration e=computers.elements();e.hasMoreElements();){
	    LinkToDaemon link=(LinkToDaemon) e.nextElement();
	    String key=link.getName();
	    String computer=link.getComputer();
	    	   
//  	    Boolean res = Boolean.FALSE;
//  	    try {
//  		java.lang.reflect.Method m  = fr.lifl.magique.util.ClassUtil.getMethod(platform.getClass(),
//  								     "ping",
//  								     new String[]{"java.lang.String"});
//  		res = (Boolean) m.invoke(platform, new Object[]{computer});
//  	    }
//  	    catch (Exception exc) {
//  		exc.printStackTrace();
//  	    }
	    Boolean res = platform.ping(computer);

	    synchronized(resultat) {
		resultat.remove(key);
		if (res.booleanValue()) 
		    resultat.put(key,computer);
	    }
	}
 
    }
}

		      
