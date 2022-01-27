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
 * Member Attribute Deprecated: ICS.
 *
 * This attribute marks deprecated classes, fields and methods.
 */
public class DeprecatedAttribute
    extends Attribute
{
    /**
     * Name of attribute type: "Deprecated"
     */
    private final static AsciiConstant attr = new AsciiConstant("Deprecated");

    /**
     * The value of this deprecated attribute.
     */
    private byte[] data;

    /**
     * Create a deprecated attribute.
     */
    public DeprecatedAttribute()
    {
        this.data = null;
    }

    /**
     * Constructs a deprecated attribute from a class file stream.
     *
     * @param in
     * The stream to read from.
     *
     * @param cp
     * The constant pool.
     *
     * @exception IOException
     * An io problem has occured.
     */
    public DeprecatedAttribute(DataInput in, ConstantPool cp)
        throws IOException
    {
        this.data = new byte[in.readInt()];
        in.readFully(this.data);
    }

    /**
     * Returns the attribute's tag
     */
    int getTag()
    {
        return Constants.ATT_DEPRECATED;
    }

    /**
     * Returns the space in bytes used by this attribute in the classfile
     */
    int getSize()
    {
        return 2 + 4 + (data == null ? 0 : data.length);
    }

    /**
     * Insert or check location of constant value on constant pool
     *
     * @param cp      the constant pool for this class
     */
    void resolveConstants(ConstantPool cp)
    {
        cp.addItem(attr);
    }

    /**
     * Write this class into the the file (out) getting data position from
     * the constant pool
     *
     * @param cp      the constant pool that contain all data
     * @param out     the file where to write this object info
     *
     * @exception java.io.IOException an io problem has occured
     */
    void write(ConstantPool cp, DataOutput out)
        throws IOException
    {
        out.writeShort(attr.getIndex());
        if (data != null) {
            out.writeInt(data.length);
            out.write(data);
        }
        else {
            out.writeInt(0);
        }
    }
}
