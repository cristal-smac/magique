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
 * Source File Attribute: VMS 4.7.2.
 *
 * This attribute represents the name of the source file from which
 * this class file was compiled.
 */
public class SourceFileAttribute
    extends Attribute
{
    /**
     * Name of attribute type.
     */
    private final static AsciiConstant attr = new AsciiConstant("SourceFile");

    /**
     * Name of source file.
     */
    private AsciiConstant name;

    /**
     * Create a source file attribute.
     *
     * @param name
     * The name of the source file.
     */
    public SourceFileAttribute(String name)
    {
        this.name = new AsciiConstant(name);
    }

    /**
     * Constructs a source file attribute from a class file stream.
     *
     * @param in
     * The stream to read from.
     * @param cp
     * The constant pool.
     *
     * @exception IOException
     * An io problem has occured.
     *
     * @exception ClassFileFormatException
     * Attempt to write a bad classfile info.
     */
    public SourceFileAttribute(DataInput in, ConstantPool cp)
        throws IOException, ClassFileFormatException
    {
        if (in.readInt() != 2) {
            throw new ClassFileFormatException("bad attribute length");
        }
        this.name = (AsciiConstant)cp.getEntryAt(in.readUnsignedShort());
    }

    /**
     * Returns the attribute's tag
     */
    int getTag()
    {
        return Constants.ATT_SOURCEFILE;
    }

    /**
     * Returns the space in bytes used by this attribute in the classfile
     */
    int getSize()
    {
        return 2 + 4 + 2;
    }

    /**
     * Return the name of the source file
     */
    String getValue()
    {
        return name.getValue();
    }

    /**
     * Insert or check location of constant value on constant pool.
     *
     * @param cp
     * The constant pool for this class.
     */
    void resolveConstants(ConstantPool cp)
    {
        cp.addItem(attr);
        cp.addItem(name);
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
     */
    void write(ConstantPool cp, DataOutput out)
        throws IOException
    {
        out.writeShort(attr.getIndex());
        out.writeInt(2);
        out.writeShort(name.getIndex());
    }
}

