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
 * An entry in a LineNumberTable.
 */
public class LineNumberInfo
    implements AccessorContainer
{
    /**
     * The line number.
     */
    private int line;

    /**
     * A pointer to the intruction at that line number.
     */
    private InstructionAccessor instruction;

    /**
     * Create an entry in the line number table.
     *
     * @param line
     * The line number in the source code.
     *
     * @param instruction
     * The instruction where the line begins.
     */
    public LineNumberInfo(short line, InstructionAccessor instruction)
    {
        this.line = line;
        this.instruction = instruction;
    }

    /**
     * Create an entry in the line number table from a class file stream.
     *
     * @param in
     * The stream to read from.
     * @param cp
     * The constant pool.
     * @param instructions
     * (sparse) array of instructions.
     *
     * @exception java.io.IOException
     * An io problem has occured.
     */
    public LineNumberInfo(DataInput in, ConstantPool cp,
                          Instruction[] instructions)
        throws IOException
    {
        int pos = in.readUnsignedShort();
        if (pos < instructions.length) {
            this.instruction = instructions[pos];
        }
        this.line = in.readUnsignedShort();
    }

    /**
     * Transforms targets. (deferences to actual instructions)
     */
    public void transformAccessors(AccessorTransformer transformer)
        throws BadAccessorException
    {
        this.instruction = this.instruction.transform(transformer, this);
    }

    /**
     * Returns the line number in the source code
     */
    public int getLine()
    {
        return line;
    }

    /**
     * Returns the instruction where the line begins
     */
    public InstructionAccessor getInstruction()
    {
        return instruction;
    }

    /**
     * Sets the instruction where the line begins
     */
    public void setInstruction(InstructionAccessor instruction)
    {
        this.instruction = instruction;
    }

    /**
     * Write this class into the the file (out) getting data position from
     * the constant pool.
     *
     * @param cp
     * The constant pool that contain all data.
     * @param out
     * The file where to write this object info.
     *
     * @exception java.io.IOException
     * An io problem has occured.
     */
    /*package*/ void write(DataOutput out)
        throws IOException
    {
        Instruction instruction = (Instruction)this.instruction;

        out.writeShort(instruction.getAddress());
        out.writeShort((short)line);
    }
}

