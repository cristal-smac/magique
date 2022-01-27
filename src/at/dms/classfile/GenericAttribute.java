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

import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;

/**
 * This is an opaque attribute that lets you add an uninterpreted
 * stream of bytes into an attribute in a class file. This can be
 * used (for instance) to embed versioning or signatures into the
 * class file or method.
 */
public class GenericAttribute
    extends Attribute
{
    /**
     * Name value.
     */
    private AsciiConstant name;

    /**
     * Data value.
     */
    private byte[] data;

    /**
     * Make up a new attribute.
     *
     * @param name        Name to be associated with the attribute.
     * @param data        stream of bytes to be placed with the attribute.
     */
    public GenericAttribute(String name, byte[] data)
    {
        this.name = new AsciiConstant(name);
        this.data = data;
    }

    /**
     * Make up a new attribute.
     *
     * @param name    the attribute's name.
     * @param in      the stream to read from.
     * @param cp      the constant pool.
     *
     * @exception IOException
     * An io problem has occured.
     */
    public GenericAttribute(AsciiConstant name, DataInput in, ConstantPool cp)
        throws IOException
    {
        this.name = name;

        this.data = new byte[in.readInt()];
        in.readFully(this.data);
    }

    /**
     * Returns the attribute's tag.
     */
    int getTag()
    {
        return Constants.ATT_GENERIC;
    }

    /**
     * Returns the space in bytes used by this attribute in the classfile.
     */
    int getSize()
    {
        return 2 + 4 + data.length;
    }

    /**
     * Returns the attribute's name.
     */
    String getName()
    {
        return name.getValue();
    }

    /**
     * Insert or check location of constant value on constant pool.
     *
     * @param cp      the constant pool for this class.
     */
    void resolveConstants(ConstantPool cp)
    {
        cp.addItem(name);
    }

    /**
     * Write this class into the the file (out) getting data position from
     * the constant pool.
     *
     * @param cp      the constant pool that contain all data
     * @param out     the file where to write this object info
     *
     * @exception IOException
     * An io problem has occured.
     */
    void write(ConstantPool cp, DataOutput out)
        throws IOException
    {
        out.writeShort(name.getIndex());
        out.writeInt(data.length);
        out.write(data);
    }
}


