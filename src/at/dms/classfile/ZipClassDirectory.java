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
import java.io.IOException;
import java.util.zip.ZipFile;
import java.util.zip.ZipException;
import java.util.zip.ZipEntry;

/**
 * An implementation of a conceptual directory which holds Java class files,
 * using a zip or jar file in the file system.
 */
class ZipClassDirectory
    extends ClassDirectory
{
    /**
     * The zip or jar file that is represented by this object.
     */
    private ZipFile zip;

    /**
     * Constructs a class directory implemented by a .zip or .jar file.
     */
    public ZipClassDirectory(File zip)
        throws IOException
    {
        this.zip = new ZipFile(zip);
    }

    /**
     * Constructs a class directory implemented by a .zip or .jar file.
     */
    public ZipClassDirectory(ZipFile zip)
    {
        this.zip = zip;
    }

    /**
     * Returns a ClassDescription object that represents the named class.
     *
     * @param name
     * The name of the class.
     *
     * @return
     * A ClassDescription object that represents the named class, or
     * <code>null</code> if this ClassDirectory does not contain the named
     * class.
     */
    public ClassDescription getClassFile(String name)
    {
        name += ".class";

        final ZipEntry entry = zip.getEntry(name);

        if (entry == null) {
            return null;
        }
        else {
            return new ClassDescription() {
                    public Data getData() {
                        try {
                            return new Data(zip.getInputStream(entry));
                        }
                        catch (IOException e) {
                            // REALLY BAD
                            // (file exist in cp but is not accessible)
                            return null;
                        }
                    }};
        }
    }
}
