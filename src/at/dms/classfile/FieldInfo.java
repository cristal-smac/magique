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
 * VMS 4.5: Fields
 *
 * Each field is described by this structure.
 *
 * Used to make up new field entries. Fields for a class can have
 * an additional "ConstantValue" attribute associated them,
 * which the java compiler uses to represent things like
 * static final int blah = foo;
 */
public class FieldInfo
    extends Member
{
    /**
     * ...
     */
    private AsciiConstant name;

    /**
     * ...
     */
    private AsciiConstant type;

    /**
     * ...
     */
    private AttributeList attributes;

    /**
     * Constructs a field entry.
     *
     * @param modifiers
     * Access permission to and properties of the field.
     *
     * @param name
     * The name of the field.
     *
     * @param type
     * The type signature.
     *
     * @param value
     * The value of a constant field that must be (explicitly or implicitly)
     * static.
     *
     * @param deprecated
     * Is this field deprecated?
     *
     * @param synthetic
     * Is this field synthesized by the compiler?
     */
    public FieldInfo(short modifiers,
                     String name, String type, Object value,
                     boolean deprecated,
                     boolean synthetic)
    {
        setModifiers(modifiers);
        this.name = new AsciiConstant(name);
        this.type = new AsciiConstant(type);

        this.attributes =
            new AttributeList(( value != null
                                ? new ConstantValueAttribute(value) : null ),
                              ( deprecated
                                ? new DeprecatedAttribute() : null ),
                              ( synthetic ? new SyntheticAttribute() : null) );
    }

    /**
     * Constructs a field entry from a class file stream.
     *
     * @param in  the stream to read from
     * @param cp  the constant pool
     *
     * @exception java.io.IOException an io problem has occured
     * @exception ClassFileFormatException attempt to
     *     write a bad classfile info
     */
    public FieldInfo(DataInput in, ConstantPool cp)
        throws IOException, ClassFileFormatException
    {
        setModifiers((short)in.readUnsignedShort());
        this.name = (AsciiConstant)cp.getEntryAt(in.readUnsignedShort());
        this.type = (AsciiConstant)cp.getEntryAt(in.readUnsignedShort());
        this.attributes = new AttributeList(in, cp, false);
    }

    /**
     * Returns the name of the this field.
     */
    public String getName()
    {
        return name.getValue();
    }

    /**
     * Sets the name of the this field.
     */
    public void setName(String name)
    {
        this.name = new AsciiConstant(name);
    }

    /**
     * Returns the type of the this field.
     */
    public String getSignature()
    {
        return type.getValue();
    }

    /**
     * Returns the type of the this field.
     */
    public void getSignature(String type)
    {
        this.type = new AsciiConstant(type);
    }

    /**
     * Returns the value of the this field.
     */
    public Object getConstantValue()
    {
        Attribute  attr = attributes.get(Constants.ATT_CONSTANTVALUE);

        if (attr != null) {
            return ((ConstantValueAttribute)attr).getLiteral();
        }
        else {
            return null;
        }
    }

    /**
     * Sets the value of the this field.
     */
    public void setConstantValue(Object value)
    {
        if (value != null) {
            attributes.add(new ConstantValueAttribute(value));
        }
        else {
            attributes.remove(Constants.ATT_CONSTANTVALUE);
        }
    }

    /**
     * Returns true if the field is deprecated.
     */
    public boolean isDeprecated()
    {
        return attributes.get(Constants.ATT_DEPRECATED) != null;
    }

    /**
     * Sets the deprecated attribute of this field.
     */
    public void setDeprecated(boolean deprecated)
    {
        if (deprecated) {
            attributes.add(new DeprecatedAttribute());
        }
        else {
            attributes.remove(Constants.ATT_DEPRECATED);
        }
    }

    /**
     * Returns true if the field is synthetic.
     */
    public boolean isSynthetic()
    {
        return attributes.get(Constants.ATT_SYNTHETIC) != null;
    }

    /**
     * Returns true if the field is synthetic.
     */
    public void setSynthetic(boolean synthetic)
    {
        if (synthetic) {
            attributes.add(new SyntheticAttribute());
        }
        else {
            attributes.remove(Constants.ATT_SYNTHETIC);
        }
    }

    /**
     * Insert or check location of constant value on constant pool.
     *
     * @param cp  the constant pool for this class
     */
    void resolveConstants(ConstantPool cp)
        throws ClassFileFormatException
    {
        cp.addItem(name);
        cp.addItem(type);
        attributes.resolveConstants(cp);
    }

    /**
     * Write this class into the the file (out) getting data position from
     * the constant pool.
     *
     * @param cp
     * The constant pool that contain all data.
     *
     * @param out
     * The file where to write this object info.
     *
     * @exception IOException
     * An io problem has occured.
     *
     * @exception ClassFileFormatException
     * Attempt to write a bad classfile info.
     */
    void write(ConstantPool cp, DataOutput out)
        throws IOException, ClassFileFormatException
    {
        out.writeShort(getModifiers());
        out.writeShort(name.getIndex());
        out.writeShort(type.getIndex());
        attributes.write(cp, out);
    }
}
