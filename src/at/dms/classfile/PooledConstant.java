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
 * this is an abstraction to contain all the constant items
 * that can be created.
 */
public abstract class PooledConstant
    implements Constants
{
    /**
     * Index in constant pool.
     */
    private short index;

    /**
     * Constructs a new pooled constant.
     *
     * @param uniq
     * A string that identifies this constant.
     */
    public PooledConstant()
    {
    }

    /**
     * Returns the number of slots in the constant pool used by this entry.
     * This number is normally 1. Constants which use more than one slot should
     * override this method.
     */
    int getSlotsUsed()
    {
        return 1;
    }

    /**
     * Returns the associated literal
     */
    abstract Object getLiteral();

    /**
     * hashCode (a fast comparison)
     * CONVENTION: return XXXXXXXXXXXX << 4 + Y
     * with Y = ident of the type of the pooled constant
     */
    public abstract int hashCode();

    /**
     * equals (an exact comparison)
     * ASSERT: this.hashCode == o.hashCode ===> cast
     */
    public abstract boolean equals(Object o);

    public final short getIndex()
    {
        return index;
    }

    public final void setIndex(short index)
    {
        this.index = index;
    }

    /**
     * Insert or check location of constant value on constant pool.
     *
     * @param cp
     * The constant pool for this class.
     */
    abstract void resolveConstants(ConstantPool cp);

    /**
     * Check location of constant value on constant pool.
     *
     * @param pc
     * The already in pooled constant.
     */
    abstract void resolveConstants(PooledConstant pc);

    /**
     * Write this class into the the file (out) getting data position from
     * the constant pool.
     *
     * @param cp
     * The constant pool that contain all data.
     *
     * @param out
     * The file where to write this object info
     *
     * @exception IOException
     * An io problem has occured.
     */
    abstract void write(ConstantPool cp, DataOutput out) throws IOException;
}