/*
 * @(#)JarFilesFilter.java	1.0 04/05/99
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
 * A class that permits to get only the jar files of a directory
 *
 * @author Nadir Doghmane
 * @author Fabien Niquet
 * @version 1.0 04/05/99
 */

public class JarFilesFilter implements FilenameFilter {
    /**
     * return	true if	the	file's extension is	"jar"
     * and false if	it isn't.
     *
     * @see    FilenameFilter#accept
     */
    public boolean accept(File dir, String name) {
        return (name.toLowerCase().endsWith(".jar"));
    }

}
