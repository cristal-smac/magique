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

/**
 * A helper class for InnerClassTable.
 *
 * The inner class table contains an array this type. InnerClassInfo
 * contains information about one specific inner class that is used
 * in another class.
 */
public class InnerClassInfo
{
    /**
     * The FQN of the inner class.
     */
    private ClassConstant innerClass;

    /**
     * The FQN of the outer class, if it is a member class.
     * Note from implementor (EJB): the VMSpec says:
     * <P>
     * If C is not a member, the value of the outer_class_info_index item must
     * be zero. Otherwise, the value of the outer_class_info_index item must
     * be a valid index into the constant_pool table, and the entry at that
     * index must be a CONSTANT_Class_info (par.4.4.1) structure representing
     * the class or interface of which C is a member.
     * </P>
     * But in what case would C not be a member class? Anonymous non-member
     * classes?
     */
    private ClassConstant outerClass;

    /**
     * The name that the class had in the source code, or null if it is
     * an anonymous class.
     */
    private AsciiConstant simpleName;

    /**
     * The modifiers that the inner class had in the source code.
     */
    private short modifiers;

    /**
     * Create an entry in the inner class table.
     *
     * @param innerClass
     * The encoded name of the (inner) class.
     * @param outerClass
     * The defining scope of the (inner) class.
     * @param simpleName
     * The simple name of the (inner) class.
     * @param modifiers
     * Access permission to and properties of the class.
     */
    public InnerClassInfo(String innerClass,
                          String outerClass,
                          String simpleName,
                          short modifiers)
    {
        this.innerClass = new ClassConstant(innerClass);
        this.outerClass = ( outerClass == null
                            ? null : new ClassConstant(outerClass) );
        this.simpleName = ( simpleName == null
                            ? null : new AsciiConstant(simpleName) );
        this.modifiers = modifiers;
    }

    /**
     * Returns the modifiers that the inner class had in the source code.
     */
    public int getModifiers()
    {
        return modifiers;
    }

    /**
     * Returns the FQN of the outer class, if it is a member class; else
     * <code>null</code>.
     */
    public ClassConstant getOuterClass()
    {
        return outerClass;
    }

    /**
     * Returns the FQN of the inner class.
     */
    public ClassConstant getInnerClass()
    {
        return innerClass;
    }

    /**
     * Returns the name that the class had in the source code, or null if it
     * is an anonymous class.
     */
    public String getSimpleName()
    {
        if (simpleName == null) {
            return null;
        }
        else {
            return simpleName.getValue();
        }
    }

    /**
     * Create an entry in the inner class table from a class file stream.
     *
     * @param in
     * The stream to read from.
     * @param cp
     * The constant pool.
     * @exception IOException
     * An io problem has occured.
     */
    public InnerClassInfo(DataInput in, ConstantPool cp)
        throws IOException
    {
        this.innerClass = (ClassConstant)cp.getEntryAt(in.readUnsignedShort());

        int outerClass = in.readUnsignedShort();
        this.outerClass = ( outerClass == 0
                            ? null : (ClassConstant)cp.getEntryAt(outerClass));

        int simpleName = in.readUnsignedShort();
        this.simpleName = ( simpleName == 0
                            ? null : (AsciiConstant)cp.getEntryAt(simpleName));

        this.modifiers = (short)in.readUnsignedShort();
    }

    /**
     * Return the qualified name of this class.
     */
    public String getQualifiedName()
    {
        return innerClass.getName();
    }

    /**
     * Compares this object with another object. This will only result in
     * true of the other object is of the same subclass of InnerClassInfo
     * as this one, and all fields are equals.
     */
    public boolean equals(Object o)
    {
        if (o != null && o.getClass() == getClass()) {
            return equalsInnerClassInfo((InnerClassInfo)o);
        }
        else {
            return false;
        }
    }

    /**
     * Compares with another InnerClassInfo object.
     * Will only return true of contents are completely the same.
     */
    public boolean equalsInnerClassInfo(InnerClassInfo other)
    {
        return ( innerClass.equals(other.innerClass) &&
                 outerClass.equals(other.outerClass) &&
                 simpleName.equals(other.simpleName) &&
                 modifiers == other.modifiers );
    }

    /**
     * Returns a hash code for this object.
     */    
    public int hashCode()
    {
        return ( innerClass.hashCode() ^
                 outerClass.hashCode() ^
                 simpleName.hashCode() ^
                 modifiers );
    }

    /**
     * Insert or check location of constant value on constant pool.
     *
     * @param cp
     * The constant pool for this class.
     */
    void resolveConstants(ConstantPool cp)
    {
        cp.addItem(innerClass);
        if (outerClass != null) {
            cp.addItem(outerClass);
        }
        if (simpleName != null) {
            cp.addItem(simpleName);
        }
    }

    /**
     * Write this class into the the file (out) getting data position from
     * the constant pool.
     *
     * @param cp
     * The constant pool that contain all data.
     * @param out
     * The file where to write this object info.
     * @exception IOException
     * An io problem has occured.
     */
    void write(ConstantPool cp, DataOutput out)
        throws IOException
    {
        out.writeShort(innerClass.getIndex());
        out.writeShort(outerClass == null ? (short)0 : outerClass.getIndex());
        out.writeShort(simpleName == null ? (short)0 : simpleName.getIndex());
        out.writeShort(modifiers);
    }
}

