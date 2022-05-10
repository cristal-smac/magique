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

import java.io.DataOutput;
import java.io.IOException;

import at.dms.util.InternalError;

/**
 * Instructions that refers to class:
 * opc_anewarray, opc_checkcast, opc_instanceof, opc_new
 */
public class ClassRefInstruction
    extends Instruction
{
    /**
     * The class that this instruction refers to.
     */
    private ClassConstant cst;

    /**
     * Constructs a new ldc instruction.
     *
     * @param opcode      the opcode of the instruction.
     * @param name        the qualified name of the referenced object.
     */
    public ClassRefInstruction(int opcode, String name)
    {
        super(opcode);

        this.cst = new ClassConstant(name);
    }

    /**
     * Constructs a new class reference instruction from a class file.
     *
     * @param opcode
     * The opcode of the instruction.
     * @param cst
     * The class reference (as pooled constant).
     */
    public ClassRefInstruction(int opcode, ClassConstant cst)
    {
        super(opcode);

        this.cst = cst;
    }

    /**
     * Returns true iff control flow can reach the next instruction
     * in textual order.
     */
    public boolean canComplete()
    {
        return true;
    }

    /**
     * Insert or check location of constant value on constant pool
     *
     * @param cp
     * The constant pool for this class.
     */
    void resolveConstants(ConstantPool cp)
    {
        cp.addItem(cst);
    }

    /**
     * Returns the number of bytes used by the the instruction in the code
     * array.
     */
    int getSize()
    {
        return 1 + 2;
    }

    /**
     * Return the class constant on which this object refer.
     */
    public ClassConstant getClassConstant()
    {
        return cst;
    }

    /**
     * Returns the type pushed on the stack.
     */
    public byte getReturnType()
    {
        switch (getOpcode()) {
        case opc_checkcast:
            return TYP_VOID;

        case opc_anewarray:
        case opc_instanceof:
        case opc_new:
            return TYP_REFERENCE;

        default:
            throw new InternalError("invalid opcode " + getOpcode());
        }    
    }

    /**
     * Returns the size of data pushed on the stack by this instruction.
     */
    public int getPushedOnStack()
    {
        switch (getOpcode()) {
        case opc_checkcast:
            return 0;

        case opc_anewarray:
        case opc_instanceof:
        case opc_new:
            return 1;

        default:
            throw new InternalError("invalid opcode " + getOpcode());
        }    
    }

    /**
     * Return the amount of stack (positive or negative) used by this
     * instruction.
     */
    public int getStack()
    {
        switch (getOpcode()) {
        case opc_anewarray:
        case opc_checkcast:
        case opc_instanceof:
            return 0;

        case opc_new:
            return 1;

        default:
            throw new InternalError("invalid opcode " + getOpcode());
        }
    }

    /**
     * Write this instruction into a file.
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
        out.writeByte((byte)getOpcode());
        out.writeShort(cst.getIndex());
    }
}
