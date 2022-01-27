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

/**
 * This exception is to report unresolvable instruction accessors.
 */
public class BadAccessorException
    extends Exception
{

    /**
     * Constructs a BadAccessorException object.
     */
    public BadAccessorException()
    {
        super();
    }

    /**
     * Constructs a BadAccessorException object.
     *
     * @param message     the detail message
     */
    public BadAccessorException(String message)
    {
        super(message);
    }
}
