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
 * Wrap an Double constant reference with this CPE.
 */
public strictfp class DoubleConstant
    extends PooledConstant
{
    /**
     * Value of this double constant.
     */
    private double value;

    /**
     * Constructs double constant.
     *
     * @param value
     * Value for Double constant.
     */
    public DoubleConstant(double value)
    {
        this.value = value;
    }

    /**
     * Returns the associated literal.
     */
    Object getLiteral()
    {
        return new Double(value);
    }

    /**
     * Returns the number of slots in the constant pool used by this entry.
     * A double constant uses 2 slots.
     */
    int getSlotsUsed()
    {
        return 2;
    }

    /**
     * hashCode (a fast comparison)
     * CONVENTION: return XXXXXXXXXXXX << 4 + Y
     * with Y = ident of the type of the pooled constant
     */
    public final int hashCode()
    {
        return ((int)value << 4) + POO_DOUBLE_CONSTANT;
    }

    /**
     * equals (an exact comparison)
     * ASSERT: this.hashCode == o.hashCode ===> cast
     */
    public final boolean equals(Object o)
    {
        return (o instanceof DoubleConstant) &&
            ((DoubleConstant)o).value == value;
    }

    /**
     * Check location of constant value on constant pool
     *
     * @param pc      the already in pooled constant
     * ASSERT pc.getClass() == this.getClass()
     */
    final void resolveConstants(PooledConstant pc)
    {
        setIndex(pc.getIndex());
    }

    /**
     * Insert or check location of constant value on constant pool
     *
     * @param cp      the constant pool for this class
     */
    void resolveConstants(ConstantPool cp)
    {
        return;
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
        out.writeByte(CST_DOUBLE);
        out.writeDouble(value);
    }
}