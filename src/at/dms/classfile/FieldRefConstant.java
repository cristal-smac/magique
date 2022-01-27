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
 * FieldRefConstant's are used to refer to a field in a particular
 * class.
 */
public class FieldRefConstant
    extends ReferenceConstant
{
    /**
     * Constructs a field reference constant.
     *
     * @param name        the qualified name of the referenced object
     * @param type        the signature of the referenced object
     */
    public FieldRefConstant(String name, String type)
    {
        super(CST_FIELD, name, type);
    }

    /**
     * Constructs a field reference constant.
     *
     * @param name        the qualified name of the referenced object
     * @param type        the signature of the referenced object
     */
    public FieldRefConstant(String owner, String name, String type)
    {
        super(CST_FIELD, owner, name, type);
    }

    /**
     * Constructs a field reference constant.
     *
     * @param clazz    the class that defines the referenced object
     * @param nametype the simple name and signature of the referenced object
     */
    public FieldRefConstant(ClassConstant clazz, NameAndTypeConstant nametype)
    {
        super(CST_FIELD, clazz, nametype);
    }
}
