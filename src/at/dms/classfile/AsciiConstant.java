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

import java.io.DataOutput;
import java.io.IOException;

/**
 * An UTF8 constant in the constant pool of the class file.
 *
 * The name of this class is historically wrong, since Java supports the
 * Unicode UCS2 character set, not just ASCII.
 */
public class AsciiConstant
    extends PooledConstant
{
    /**
     * The value of the constant.
     */
    private String value;

    /**
     * Constructs a new UTF8 pooled constant.
     *
     * @param value
     * The value of the constant.
     */
    public AsciiConstant(String value)
    {
        this.value = value;
    }

    /**
     * Returns the value of the constant.
     */
    String getValue()
    {
        return value;
    }

    /**
     * Returns the value of the constant.
     */
    void setValue(String value)
    {
        this.value = value;
    }

    /**
     * Returns the value of the constant.
     */
    Object getLiteral()
    {
        return value;
    }

    /**
     * hashCode (a fast comparison)
     * CONVENTION: return XXXXXXXXXXXX << 4 + Y
     * with Y = ident of the type of the pooled constant
     */
    public final int hashCode()
    {
        return (value.hashCode() << 4) + POO_ASCII_CONSTANT;
    }

    /**
     * equals (an exact comparison)
     * ASSERT: this.hashCode == o.hashCode ===> cast
     */
    public final boolean equals(Object o)
    {
        return (o instanceof AsciiConstant) &&
            ((AsciiConstant)o).value.equals(value);
    }

    /**
     * Check location of constant value on constant pool.
     *
     * @param pc
     * The pooled constant.
     * ASSERT pc.getClass() == this.getClass()
     */
    final void resolveConstants(PooledConstant pc)
    {
        setIndex(pc.getIndex());
    }

    /**
     * Insert or check location of constant value on constant pool.
     *
     * @param cp
     * The constant pool for this class.
     */
    void resolveConstants(ConstantPool cp)
    {
        return;
    }

    /**
     * Writes this constant to a class file.
     *
     * @param cp
     * The constant pool that this constant is in.
     * @param out 
     * Where to write this constant.
     *
     * @exception java.io.IOException an io problem has occured
     */
    void write(ConstantPool _cp, DataOutput out)
        throws IOException
    {
        out.writeByte(CST_UTF8);
        out.writeUTF(value);
    }
}
