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

import java.util.Vector;

/**
 * This class defines severals utilities methods used in source code.
 */
public abstract class Utils
{
    /**
     * No debugging.
     */
    public static final int DBG_LEVEL_NO = 0;

    /**
     * Low debug output/overhead.
     */
    public static final int DBG_LEVEL_LOW = 1;

    /**
     * High debug output/overhead.
     */
    public static final int DBG_LEVEL_HIGH = 2;

    /**
     * Whether debugging is enabled.
     */
    private static final boolean DEBUG_ENABLED = true;

    /**
     * The level of debugging, if enabled.
     */
    private static int debugLevel = DBG_LEVEL_HIGH;

    /**
     * Check if an assertion is valid.
     *
     * @exception RuntimeException
     * If assertion fails, this exception with the specified message as its
     * message is thrown.
     */
    public static final void assertion(boolean b, String message)
    {
        if (DEBUG_ENABLED) {
            if (!b) {
                RuntimeException re = new RuntimeException(message);
                re.printStackTrace();
                if (debugLevel > DBG_LEVEL_NO) {
                    throw re;
                }
            }
        }
    }

    /**
     * Throws an error.
     *
     * @exception RuntimeException
     * This exception with the specified message as its message is thrown.
     */
    public static final void fail(String message)
    {
        RuntimeException re = new RuntimeException(message);
        re.printStackTrace();
        if (debugLevel > DBG_LEVEL_NO) {
            throw re;
        }
    }

    /**
     * Check if an assertion is valid.
     *
     * @exception RuntimeException
     * If assertion fails, this exception with a default message is thrown.
     */
    public static final void assertion(boolean b)
    {
        if (DEBUG_ENABLED) {
            if (!b) {
                RuntimeException re = new RuntimeException("assertion failed");
                re.printStackTrace();
                if (debugLevel > DBG_LEVEL_NO) {
                    throw re;
                }
            }
        } 
    }

    /**
     * Throws an error.
     *
     * @exception RuntimeException
     * This exception with a default message is thrown.
     */
    public static final void fail()
    {
        RuntimeException re = new RuntimeException("fail reached");
        re.printStackTrace();
        if (debugLevel > DBG_LEVEL_NO) {
            throw re;
        }
    }

    /**
     * Whether modifiers has more than one of the flags set.
     *
     * @return whether modifiers has more than one of the flags set.
     */
    public static boolean hasMoreThanOneFlag(int modifiers, int flags)
    {
        int masked = modifiers & flags;
        int flagCount = 0;
        for (int i = 0; i < 32; i++) {
            if ((masked & 1) != 0) {
                flagCount++;
            }
            masked >>>= 1;
        }
        return flagCount > 1;
    }

    /**
     * Whether the specified flag is enabled in modifiers.
     *
     * @return the specified flag is enabled in modifiers.
     */
    public static boolean hasFlag(int modifiers, int flag)
    {
        return (modifiers & flag) > 0;
    }

    /**
     * Whether other flags than the specified one are enabled in modifiers.
     *
     * @return other flags than the specified one are enabled in modifiers.
     */
    public static boolean hasOtherFlags(int modifiers, int flags)
    {
        return (modifiers & flags) != modifiers;
    }

    /**
     * Creates a typed array from a vector.
     *
     * @param vect
     * The vector containing the elements.
     * @param type
     * The type of the elements.
     */
    public static Object[] vectorToArray(Vector vect, Class type)
    {
        if (vect != null && vect.size() > 0) {
            Object[] array =
                (Object[])java.lang.reflect.Array.newInstance(type,
                                                              vect.size());
      
            try {
                vect.copyInto(array);
            }
            catch (ArrayStoreException e) {
                System.err.println("Array was:" + vect.elementAt(0));
                System.err.println("New type :" + array.getClass());
                throw e;
            }
            return array;
        }
        else {
            return (Object[])java.lang.reflect.Array.newInstance(type, 0);
        }
    }
  
    /**
     * Creates a int array from a vector.
     *
     * @param vect
     * The vector containing the elements.
     *
     * @param type
     * The type of the elements.
     */
    public static int[] vectorToIntArray(Vector vect)
    {
        if (vect != null && vect.size() > 0) {
            int[] array = new int[vect.size()];
      
            for (int i = array.length - 1; i >= 0; i--) {
                array[i] = ((Integer)vect.elementAt(i)).intValue();
            }

            return array;
        }
        else {
            return new int[0];
        }
    }

    /**
     * Splits a string like:
     *   "java/lang/System/out"
     * into two strings:
     *    "java/lang/System" and "out"
     */
    public static String[] splitQualifiedName(String name, char separator)
    {
        String[] result = new String[2];
        int  pos;

        pos = name.lastIndexOf(separator);

        if (pos == -1) {
            // no '/' in string
            result[0] = "";
            result[1] = name;
        }
        else {
            result[0] = name.substring(0, pos);
            result[1] = name.substring(pos + 1);
        }

        return result;
    }

    /**
     * Splits a string like:
     *   "java/lang/System/out"
     * into two strings:
     *    "java/lang/System" and "out"
     */
    public static String[] splitQualifiedName(String name)
    {
        return splitQualifiedName(name, '/');
    }
}
