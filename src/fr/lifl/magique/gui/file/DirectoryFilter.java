/*
 * @(#)DirectoryFilter.java	1.0 04/05/99
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

package fr.lifl.magique.gui.file;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A class that permits to get only the subdirectories of a directory
 *
 * @author Nadir Doghmane
 * @author Fabien Niquet
 * @version 1.0 04/05/99
 */

public class DirectoryFilter implements FilenameFilter {

    /**
     * Return	true if	the	file's a directory
     * and false if	it isn't.
     *
     * @see    FilenameFilter#accept
     */
    public boolean accept(File dir, String name) {
        File absname;
        if (dir.toString().endsWith(System.getProperty("file.separator")))
            absname = new File(dir + name);
        else
            absname = new File(dir + System.getProperty("file.separator") + name);
        return (absname.isDirectory());
    }

}
