/*
 * Copyright (C) 1990-99 DMS Decision Management Systems Ges.m.b.H.
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
 * This class allows to find the position of an object in an array.
 *
 * @deprecated
 * Use the Collections API, or keeps the array sorted and use
 * <code>java.util.Arrays</code>.
 *
 * @see java.util.Arrays
 * @see java.util.Arrays#binarySearch
 * @see java.util.List
 */
public class ArrayLocator
{
    /**
     * The array to locate int.
     */
    private Object[]      array;

    /**
     * Constructs a new ArrayLocator object.
     */
    public ArrayLocator(Object[] array)
    {
        this.array = array;
    }

    /**
     * Returns the index of the specified object in the array, or -1
     * if the object cannot be found.
     */
    public int getIndex(Object object)
    {
        for (int i = 0; i < array.length; i++) {
            if (object == array[i]) {
                return i;
            }
        }
        return -1;
    }
}
