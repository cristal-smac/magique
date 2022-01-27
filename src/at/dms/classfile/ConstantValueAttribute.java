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

import at.dms.util.InternalError;

/**
 * Constant Value Attribute:. VMS 4.7.3.
 *
 * A ConstantValue attribute represents the value of a constant field that
 * must be (explicitly or implicitly) static.
 */
public strictfp class ConstantValueAttribute
    extends Attribute
{
    /**
     * AsciiConstant with name of attribute: "ConstantValue".
     */
    private final static AsciiConstant attr =
        new AsciiConstant("ConstantValue");

    /**
     * The value of this constant-value attribute.
     */
    private PooledConstant value;

    /**
     * Create a new constant attribute whose constant value
     * is picked up from constant pool with the given entry.
     *
     * @param value
     * The value.
     */
    public ConstantValueAttribute(Object value)
    {
        if (value instanceof Integer) {
            this.value = new IntegerConstant(((Integer)value).intValue());
        }
        else if (value instanceof Float) {
            this.value = new FloatConstant(((Float)value).floatValue());
        }
        else if (value instanceof Double) {
            this.value = new DoubleConstant(((Double)value).doubleValue());
        }
        else if (value instanceof Long) {
            this.value = new LongConstant(((Long)value).longValue());
        }
        else if (value instanceof String) {
            this.value = new StringConstant((String)value);
        }
        else if (value instanceof Boolean) {
            this.value = new IntegerConstant(((Boolean)value).booleanValue()
                                             ? 1 : 0);
        }
        else {
            throw new InternalError("bad object type " + value.getClass());
        }
    }

    /**
     * Constructs a constant value attribute from a class file stream.
     *
     * @param in
     * The stream to read from.
     * @param cp
     * The constant pool.
     *
     * @exception IOException
     * An io problem has occured.
     *
     * @exception ClassFileFormatException
     * Attempt to write a bad classfile info.
     */
    public ConstantValueAttribute(DataInput in, ConstantPool cp)
        throws IOException, ClassFileFormatException
    {
        if (in.readInt() != 2) {
            throw new ClassFileFormatException("bad attribute length");
        }
        this.value = cp.getEntryAt(in.readUnsignedShort());
    }

    /**
     * Returns the attribute's tag
     */
    int getTag()
    {
        return Constants.ATT_CONSTANTVALUE;
    }

    /**
     * Returns the space in bytes used by this attribute in the classfile
     */
    int getSize()
    {
        return 2 + 4 + 2;
    }

    /**
     * Returns the value of the constant value attribute
     */
    Object getLiteral()
    {
        return value.getLiteral();
    }

    /**
     * Insert or check location of constant value on constant pool
     *
     * @param cp      the constant pool for this class
     */
    void resolveConstants(ConstantPool cp)
    {
        cp.addItem(attr);
        cp.addItem(value);
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
        out.writeInt(2);
        out.writeShort(value.getIndex());
    }
}
