/*
 * Copyright (C) 1990-99 DMS Decision Management Systems Ges.m.b.H.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package at.dms.classfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * An implementation of a conceptual directory which holds Java class files,
 * using a file system directory.
 */
class DirClassDirectory
    extends ClassDirectory
{
    /**
     * Constructs a class directory representing a real directory.
     */
    public DirClassDirectory(File dir)
    {
        this.dir = dir;
    }

    /**
     * Returns a ClassDescription object that can give the data of the
     * classfile for the named class.
     *
     * @param name
     * The name of the class, using '/'-es instead of dots in the package name
     * part, but does NOT have ".class" at the end of the class name.
     *
     * @return
     * Returns a ClassDescription object that can give the data of the
     * classfile for the named class. If the named class cannot be found
     * in this directory, this method returns <code>null</code>.
     */
    public ClassDescription getClassFile(String name)
    {
        /* Replace '/' with OS-dependent directory separator, and append
         * ".class" file extension.
         */
        name = name.replace('/', File.separatorChar) + ".class";

        final File file = new File(dir.getPath(), name);

        if (!file.canRead()) {
            return null;
        }
        else {
            return new ClassDescription() {
                    public Data getData()
                    {
                        try {
                            return new Data(new FileInputStream(file));
                        }
                        catch (FileNotFoundException e) {
                            return null;
                        }
                    }};
        }
    }

    // ----------------------------------------------------------------------
    // PRIVATE DATA MEMBERS
    // ----------------------------------------------------------------------

    private File  dir;  // non null iff is a real directory
}
