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
 * Representation of a JVM-bytecode Jump instruction.
 */
public class JumpInstruction
    extends Instruction
    implements AccessorContainer
{
    /**
     * Used as an identifier to find the Java syntax tree element that
     * generated a bad jump instruction.
     */
    private String debugIdent;

    /**
     * ...
     */
    private InstructionAccessor target;

    /**
     * Whether the instruction is a wide jump (offset is 16 bits instead
     * of 8)
     */
    private boolean wide;

    /**
     * Constructs a new instruction that takes a label as argument.
     *
     * @param opcode
     * The opcode of the instruction.
     * @param target
     * The referenced instruction.
     */
    public JumpInstruction(int opcode, InstructionAccessor target)
    {
        this(opcode, target, "<no-debug-info>");
    }

    /**
     * Constructs a new instruction that takes a label as argument.
     *
     * @param opcode
     * The opcode of the instruction.
     *
     * @param target
     * The referenced instruction.
     *
     * @param debugIdent
     * Can be used to trace wrong jump instructions back to the generator
     * of that instruction.
     */
    public JumpInstruction(int opcode, InstructionAccessor target,
                           String debugIdent)
    {
        super(opcode);

        this.debugIdent = debugIdent;
        this.target = target;
    }

    /**
     * Returns true iff control flow can reach the next instruction
     * in textual order.
     */
    public boolean canComplete()
    {
        return getOpcode() != opc_goto && getOpcode() != opc_goto_w;
    }

    /**
     * Transforms targets (deferences to actual instructions).
     */
    public void transformAccessors(AccessorTransformer transformer)
        throws BadAccessorException
    {
        this.target = this.target.transform(transformer, this);
    }

    /**
     * Sets the target for this instruction
     */
    public void setTarget(InstructionAccessor target)
    {
        this.target = target;
    }

    /**
     * Return the target of this instruction
     */
    public InstructionAccessor getTarget()
    {
        return target;
    }

    /**
     * Returns the number of bytes used by the the instruction in the code
     * array.
     */
    int getSize()
    {
        return 1 + (wide ? 4 : 2);
    }

    /**
     * Verifies the enclosed instruction and computes the stack height.
     *
     * @param env the check environment
     * @param curStack
     *        the stack height at the end of the execution of the instruction
     * @return true iff the next instruction in textual order needs to be
     *  checked, i.e. this instruction has not been checked before
     *  and it can complete normally
     * @exception ClassFileFormatException a problem was detected
     */
    void check(CodeEnv env, int curStack)
        throws ClassFileFormatException
    {
        try {
            if (getOpcode() == opc_jsr_w || getOpcode() == opc_jsr) {
                // the return address is pushed on the stack, but not
                // counted here because:
                // - it is popped on return before control flows to the
                //   next instruction in textual order
                // - it is counted in the traget
                env.checkExecutionPath((InstructionHandle)target,
                                       curStack + 1);
            }
            else {
                env.checkExecutionPath((InstructionHandle)target, curStack);
            }
        }
        catch (ClassFileFormatException e) {
            System.err.println("JumpInstruction/debugIdent = " + debugIdent);
            throw e;
        }
    }

    /**
     * Computes the address of the end of the instruction.
     *
     * @param position the minimum and maximum address of the
     *    begin of this instruction. This parameter
     *    is changed to the minimum and maximum
     *    address of the end of this instruction.
     */
    void computeEndAddress(CodePosition position)
    {
        CodePosition target = ((InstructionHandle)this.target).getPosition();

        boolean  minWide;
        boolean  maxWide;

        if (target.min == -1) {
            // target not yet known
            minWide = false;
            maxWide = true;
        }
        else if (target.min < position.max) {
            // target before this instruction
            minWide = (target.max - position.min) < Short.MIN_VALUE;
            maxWide = (target.min - position.max) < Short.MIN_VALUE;
        }
        else {
            // target after this instruction
            minWide = (target.min - position.max) > Short.MAX_VALUE;
            maxWide = (target.max - position.min) > Short.MAX_VALUE;
        }

        position.min += 1 + (minWide ? 4 : 2);
        position.max += 1 + (maxWide ? 4 : 2);

        if (minWide == maxWide) {
            wide = minWide;

            switch (getOpcode()) {
            case opc_goto_w:
            case opc_goto:
                setOpcode(wide ? opc_goto_w : opc_goto);
                break;

            case opc_jsr:
            case opc_jsr_w:
                setOpcode(wide ? opc_jsr_w : opc_jsr);
                break;

            default:
                if (wide) {
                    // !!! graf 000206 REPLACE BY USER ERROR
                    throw new InternalError("target to far away");
                }
            }
        }
    }

    /**
     * Returns the size of data pushed on the stack by this instruction
     */
    public int getPushedOnStack()
    {
        if (getOpcode() == opc_jsr_w) {
            return 2;
        }
        if (getOpcode() == opc_jsr) {
            return 1;
        }
        return 0;
    }

    /**
     * Returns the type pushed on the stack
     */
    public byte getReturnType()
    {
        return TYP_VOID;
    }

    /**
     * Return the amount of stack (positive or negative) used by this
     * instruction.
     */
    public int getStack()
    {
        switch (getOpcode()) {
        case opc_ifeq:
        case opc_ifne:
        case opc_iflt:
        case opc_ifge:
        case opc_ifgt:
        case opc_ifle:
        case opc_ifnull:
        case opc_ifnonnull:
            return -1;

        case opc_if_icmpeq:
        case opc_if_icmpne:
        case opc_if_icmplt:
        case opc_if_icmpge:
        case opc_if_icmpgt:
        case opc_if_icmple:
        case opc_if_acmpeq:
        case opc_if_acmpne:
            return -2;

        case opc_goto_w:
        case opc_goto:
        case opc_jsr:
        case opc_jsr_w:
            return 0;

        default:
            throw new InternalError("invalid opcode: " + getOpcode());
        }
    }

    /**
     * Insert or check location of constant value on constant pool
     *
     * @param cp  the constant pool for this class
     */
    void resolveConstants(ConstantPool cp)
    {
    }

    /**
     * Write this instruction into a file
     *
     * @param cp  the constant pool that contain all data
     * @param out  the file where to write this object info
     *
     * @exception java.io.IOException an io problem has occured
     */
    void write(ConstantPool cp, DataOutput out)
        throws IOException
    {
        Instruction  target = (Instruction)this.target;

        out.writeByte((byte)getOpcode());
        if (wide) {
            out.writeInt(target.getAddress() - getAddress());
        }
        else {
            out.writeShort((short)(target.getAddress() - getAddress()));
        }
    }

    /**
     * Debugging method.
     */
    public void dump()
    {
        System.err.println("" + OpcodeNames.getName(getOpcode())
                           + " [" + this + "] ===> " + target);
    }
}

