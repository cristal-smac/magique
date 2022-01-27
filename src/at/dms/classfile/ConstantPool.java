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
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * The constant pool, in this object are stored all the constant of
 * the classfile.
 */
public strictfp class ConstantPool
    implements Constants
{
    /**
     * Maximum number of entries in constant pool. If more are needed, the
     * class must be refactored into more than one class.
     */
    public static int MAX_ENTRY = 65535;

    /**
     * Whether this is a newly created constant pool (if value is true) or
     * one that was read from a class file.
     */
    private boolean newOne;

    /**
     * Start at 1, since first element already used (VM 1.4)
     */
    private int countItems = 1;

    /**
     * ...
     */
    private Map items;

    /**
     * ...
     */
    private PooledConstant[] table = PooledArray.getPooledArray();

    /**
     * Constructs an empty constant pool.
     */
    public ConstantPool()
    {
        items = Maps.getMap();
        countItems = 1;
        newOne = true;
    }

    /**
     * Close this constant pool.
     */
    public void close()
    {
        Maps.release(items);
        PooledArray.release(table);
    }

    /**
     * Constructs a constant pool structure from a class file.
     *
     * @param in
     * The stream to read the class from.
     *
     * @exception IOException
     * An io problem has occured.
     *
     * @exception ClassFileFormatException
     * Attempt to write a bad classfile info.
     */
    public ConstantPool(DataInput in)
        throws IOException, ClassFileFormatException
    {
        int size = in.readUnsignedShort();
        countItems = size;

        int[] pass = new int[size];

        // First pass: read the elements
        for (int i = 1; i < size; i++) {
            byte tag = (byte)in.readUnsignedByte();

            switch (tag) {
            case CST_UTF8:
                table[i] = new AsciiConstant(in.readUTF().intern());
                break;

            case CST_INTEGER:
                table[i] = new IntegerConstant(in.readInt());
                break;

            case CST_FLOAT:
                table[i] = new FloatConstant(in.readFloat());
                break;

            case CST_LONG:
                table[i] = new LongConstant(in.readLong());
                i += 1;     // uses 2 slots
                break;

            case CST_DOUBLE:
                table[i] = new DoubleConstant(in.readDouble());
                i += 1;     // uses 2 slots
                break;

            case CST_CLASS:
                table[i] = new UnresolvedConstant(CST_CLASS,
                                                  in.readUnsignedShort(), 0);
                pass[i] = 1;
                break;

            case CST_STRING:
                table[i] = new UnresolvedConstant(CST_STRING,
                                                  in.readUnsignedShort(), 0);
                pass[i] = 1;
                break;

            case CST_FIELD:
            case CST_METHOD:
            case CST_INTERFACEMETHOD:
                table[i] = new UnresolvedConstant(tag, in.readUnsignedShort(),
                                                  in.readUnsignedShort());
                pass[i] = 2;
                break;

            case CST_NAMEANDTYPE:
                table[i] = new UnresolvedConstant(tag, in.readUnsignedShort(),
                                                  in.readUnsignedShort());
                pass[i] = 1;
                break;

            default:
                throw new ClassFileFormatException("Bad constant tag: " + tag);
            }
        }

        // Second pass: resolve the elements referencing constants resolved
        // at the first pass.
        for (int i = 1; i < size; i++) {
            if (pass[i] == 1) {
                table[i] = ((UnresolvedConstant)table[i])
                    .resolveConstant(table);
            }
        }

        // Third pass: resolve the elements referencing constants resolved at
        // the second pass.
        for (int i = 1; i < size; i++) {
            if (pass[i] == 2) {
                table[i] = ((UnresolvedConstant)table[i])
                    .resolveConstant(table);
            }
        }
    }

    /**
     * This is the method to add items to a class. Items for
     * a class are "uniquefied". Ie, if you add an item whose
     * contents already exist in the class, only one entry is finally
     * written out when the class is written.
     *
     * @param item    the item to be added to the constant pool.
     */
    final void addItem(PooledConstant item)
    {
        PooledConstant old;

        if ((old = (PooledConstant)items.put(item, item)) == null) {
            // resolve it so it adds anything which it depends on
            item.resolveConstants(this);

            if (countItems == table.length) {
                throw new RuntimeException("CLASS CODE EXCEED MAX ALLOWED "
                                           + "SIZE");
            }

            table[countItems] = item;
            item.setIndex((short)countItems);
            countItems += item.getSlotsUsed();
        }
        else {
            // already in, set the references
            item.resolveConstants(old);
        }

        if (item.getIndex() == 0) {
            throw new RuntimeException();
        }
    }

    /**
     * Get the entry at a specified index.
     *
     * @exception ClassFileFormatException
     * Attempt to write a bad classfile info.
     */
    final PooledConstant getEntryAt(int index)
    {
        return table[index];
    }

    /**
     * Write this class into the the file (out) getting data position from
     * the constant pool.
     *
     * @param out     the file where to write this object info.
     *
     * @exception IOException
     * An io problem has occured.
     */
    void write(DataOutput out)
        throws IOException
    {
        // make up indices for entries
        if (!newOne) {
            // REWRITE AN ALREADY READ CLASS || CAN BE OPTIMIZED IF USED $$$
            items = new HashMap(table.length);
            PooledConstant[] table = this.table;
            this.table = new PooledConstant[table.length]; // $$$$$$$
            // Add the constants to the constant pool
            for (int i = 1; i < countItems; i += table[i].getSlotsUsed()) {
                addItem(table[i]);
            }
        }

        out.writeShort((short)countItems);

        // Now write out all the entries
        for (int i = 1; i < countItems; i += table[i].getSlotsUsed()) {
            table[i].write(this, out);
            table[i - 1] = null;
        }
        table[countItems - 1] = null;
    }
}

/**
 * Optimization.
 */
class PooledArray
{
    private static Stack stack = new Stack();

    static PooledConstant[] getPooledArray()
    {
        if (!Constants.ENV_USE_CACHE || stack.empty()) {
            return new PooledConstant[ConstantPool.MAX_ENTRY];
        }
        return (PooledConstant[])stack.pop();
    }

    static void release(PooledConstant[] arr)
    {
        if (arr == null) {
            return;
        }
        if (Constants.ENV_USE_CACHE) {
            stack.push(arr);
        }
    }
}

/**
 * optimization.
 */
class Maps
{
    private static Stack stack = new Stack();

    static Map getMap()
    {
        if (!Constants.ENV_USE_CACHE || stack.empty()) {
            return new HashMap(ConstantPool.MAX_ENTRY / 4);
        }
        Map hash = (Map)stack.pop();
        return hash;
    }

    static void release(Map arr)
    {
        if (arr == null) {
            return;
        }
        if (Constants.ENV_USE_CACHE) {
            arr.clear();
            stack.push(arr);
        }
    }
}
