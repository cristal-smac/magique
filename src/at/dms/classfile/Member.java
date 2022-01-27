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
 * Members: VMS 4.5.
 *
 * Root class for class members (fields, methods, inner classes and
 * interfaces).
 *
 */
public abstract class Member
    implements Constants
{
    /**
     * The modifiers (access and other) of this member.
     */
    private short modifiers;

    /**
     * Constructs a member object.
     */
    Member()
    {
        modifiers = 0;
    }

    /**
     * Returns the modifiers of this member.
     */
    public short getModifiers()
    {
        return modifiers;
    }

    /**
     * Returns the modifiers of this member.
     */
    public void setModifiers(short modifiers)
    {
        this.modifiers = modifiers;
    }

    /**
     * Returns the name of the this member.
     */
    public abstract String getName();

    /**
     * Returns the type of the this member.
     */
    public abstract String getSignature();
}
