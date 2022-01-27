/*
 * Copyright (C) 1990-99 DMS Decision Management Systems Ges.m.b.H.
 * Copyright (C) 2001    Erwin Bolwidt, Haarlem, The Netherlands.
 *
 * The original author of this file is DMS; refactorings, bug fixes and
 * extensions were done by Erwin Bolwidt.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package at.dms.util;

/**
 * An array of 2-bit values.
 */
public class ArrayOf2BitValues
{
    /**
     * An array of bit masks to highlight just the 2 bits at each of 16
     * logical integer indexes.
     */
    private static final int[] MASKS = {
        3 << (2 * 0),
        3 << (2 * 1),
        3 << (2 * 2),
        3 << (2 * 3),
        3 << (2 * 4),
        3 << (2 * 5),
        3 << (2 * 6),
        3 << (2 * 7),
        3 << (2 * 8),
        3 << (2 * 9),
        3 << (2 * 10),
        3 << (2 * 11),
        3 << (2 * 12),
        3 << (2 * 13),
        3 << (2 * 14),
        3 << (2 * 15)
    };
        
    /**
     * The inverse bitmasks of the <code>MASKS</code> array, used to clear
     * out 2 specified bits in an integer before using the binary-or
     * operation to assign a new value to them.
     */
    private static final int[] REVMASKS = {
        ~(3 << (2 * 0)),
        ~(3 << (2 * 1)),
        ~(3 << (2 * 2)),
        ~(3 << (2 * 3)),
        ~(3 << (2 * 4)),
        ~(3 << (2 * 5)),
        ~(3 << (2 * 6)),
        ~(3 << (2 * 7)),
        ~(3 << (2 * 8)),
        ~(3 << (2 * 9)),
        ~(3 << (2 * 10)),
        ~(3 << (2 * 11)),
        ~(3 << (2 * 12)),
        ~(3 << (2 * 13)),
        ~(3 << (2 * 14)),
        ~(3 << (2 * 15))
    };
        
        
    /**
     * The array of integers that stores the 2-bit values, 16 of them
     * in each integer in this array.
     */
    private int[] bitArray;

    /**
     * Constructs instance from an array of integers, where each integer
     * in that array contains 16 2-bit values, the logically first value
     * in the least significant bits of each integer, and the logically last
     * 2-bit value in that integer in the most significant bits.
     */
    public ArrayOf2BitValues(int[] array)
    {
        this.bitArray = array;
    }

    /**
     * Constructs instance with default size. The size automatically increases
     * as bigger indices are used.
     */
    public ArrayOf2BitValues()
    {
        this.bitArray = new int[2];
    }

 
    /**
     * Overwrites the 2-bit value at the specified logical index in the
     * array.
     *
     * @param index
     * Is a logical index in the array. If the index is greater or equals
     * to the current size of the array, the array has its size increased.
     *
     * @param value
     * A 2-bit value.
     */
    public final void set(int index, int value)
    {
        /* There are 16 2-bit fields in one integer, so to get the array
         * index (array consists of whole integers), divide by 16 in the
         * quickest way possible: bit shift right by 4 bits.
         */
        int arrayIndex = index >> 4;
            
        /* There are 16 2-bit fields in one integer, so to get index within
         * an integer, take the index module 16 in the quickest way
         * possible: mask value with 15.
         */
        int intIndex = index & 0xf;
            
        if (arrayIndex >= bitArray.length) {
            /* Calculate new size heuristically. */
            int newSize =
                Math.max(2 + arrayIndex, Math.round(bitArray.length * 1.5f));

            int[] temp = new int[newSize];
                
            System.arraycopy(bitArray, 0, temp, 0, bitArray.length);
            bitArray = temp;
        }
            
        /* Calculated the value of the integer in the bitarray with the
         * 2 bits that are going to be set, cleared to zero, so a binary
         * or is possible in the line after this one.
         */
        int clearedInt = bitArray[arrayIndex] & REVMASKS[intIndex];
        
        /* Take the cleared integer, binary-or with the new value shifted
         * to the right place in the bit-integer.
         */
        bitArray[arrayIndex] = clearedInt | ( (value & 3) << (intIndex << 1) );
    }
    
    /**
     * Returns the 2-bit value at the specified logical index in the array.
     *
     * @param index
     * Is a logical index in the array. If the index is greater or equal
     * to the current size of the array, the return value is
     * <code>0</code>.
     *
     * @return
     * The 2-bit value at logical index <code>index</code>, or
     * <code>0</code> if <code>index</code> is too large.
     */
    public final int get(int index)
    {
        /* There are 16 2-bit fields in one integer, so to get the array
         * index (array consists of whole integers), divide by 16 in the
         * quickest way possible: bit shift right by 4 bits.
         */
        int arrayIndex = index >> 4;
        
        /* There are 16 2-bit fields in one integer, so to get index within
         * an integer, take the index module 16 in the quickest way
         * possible: mask value with 15.
         */
        int intIndex = index & 0xf;
        
        if (arrayIndex < bitArray.length) {
            /* Clear everything but the bits that are going to be
             * returned.
             */
            int highlightedInt = bitArray[arrayIndex] & MASKS[intIndex];
            
            /* Shift bits of interest into the right position. */
            return highlightedInt >>> (intIndex << 1);
        }
        else {
            return 0;
        }
    }

    /**
     * Returns the internal bit array with a layout that is described in
     * the constructor that takes an int[] as its single argument.
     *
     * @see #ArrayOf2BitValues(int[])
     */
    public final int[] getInternalArray()
    {
        return bitArray;
    }
}
