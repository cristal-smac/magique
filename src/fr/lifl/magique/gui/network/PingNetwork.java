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

import fr.lifl.magique.platform.Platform;

import java.util.Hashtable;


public class PingNetwork {
    private final Hashtable Vactif;
    //    private Object platform;
    private final Platform platform;

    public PingNetwork(Platform p) {
        super();
        Vactif = new Hashtable();
        this.platform = p;
    }

    public void testNetwork(Hashtable computers) {
        new NetworkTest(computers, Vactif, platform).start();
    }

    public void reset() {
        synchronized (Vactif) {
            Vactif.clear();
        }
    }

    public int computerState(String S) {
        //	synchronized(Vactif) {
        if (Vactif.containsKey(S)) return 2;
        //	}
	/*	synchronized(Vready) {
		if (Vready.containsKey(S)) return 2;   
		}*/
        return 0;
    }
}
