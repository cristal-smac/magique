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

/**
 * A conceptual directory which holds Java class files.
 * Since Java can use archived class files found in a compressed ("zip") file,
 * this entity may or may not correspond to an actual directory on disk.
 */
abstract class ClassDirectory
{
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
    public abstract ClassDescription getClassFile(String name);
}
