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
import java.util.Iterator;
import java.util.List;

import at.dms.util.InternalError;
import at.dms.util.Utils;

/**
 * Complex switch instruction. This class decides whether to use the 
 * lookupswitch or tableswitch opcode.
 * <P>
 * Some instructions are perniticky enough that its simpler
 * to write them separately instead of smushing them with
 * all the rest. The Switch Intstruction is one of them.
 */
public class SwitchInstruction
    extends Instruction
    implements AccessorContainer
{
    /**
     * How many empty bytes to pad between the opcode and the table behind it.
     */
    private int pad;

    /**
     * The target for the default label.
     */
    private InstructionAccessor defaultTarget;

    /**
     * The values of the case labels.
     */
    private int[] matches;

    /**
     * The jump targets for each values in the <code>matches</code> array.
     */
    private InstructionAccessor[] targets;

    /**
     * Constructs a switch instruction.
     *
     * @param defaultTarget
     * Default target for switch.
     *
     * @param matches
     * Array of match values to compare the key to.
     *
     * @param targets
     * Array of target instructions for each match value.
     */
    public SwitchInstruction(InstructionAccessor defaultTarget,
                             int[] matches,
                             InstructionAccessor[] targets)
    {
        super(opc_xxxunusedxxx);  // actual instruction not known here

        this.defaultTarget = defaultTarget;
        this.matches = matches;
        this.targets = targets;
    }

    /**
     * Constructs a switch instruction.
     *
     * @param defaultTarget
     * Default target for switch.
     *
     * @param matches
     * List of match values to compare the key to.
     *
     * @param targets
     * List of target instructions for each match value.
     */
    public SwitchInstruction(InstructionAccessor defaultTarget,
                             List matches,
                             List targets)
    {
        this(defaultTarget,
             listToIntArray(matches),
             (InstructionAccessor[])
             targets.toArray(new InstructionAccessor[targets.size()]));
    }

    /**
     * Converts a list of java.lang.Integer objects to an array of primitive
     * ints.
     */
    private static int[] listToIntArray(List list)
    {
        Iterator iterator = list.iterator();
        int[] result = new int[list.size()];
        for (int i = 0; i < result.length && iterator.hasNext(); i++) {
            result[i] = ((Integer)iterator.next()).intValue();
        }
        return result;
    }

    /**
     * Returns true iff control flow can reach the next instruction
     * in textual order.
     */
    public boolean canComplete()
    {
        return false;
    }

    /**
     * Transforms targets. (deferences to actual instructions)
     */
    public void transformAccessors(AccessorTransformer transformer)
        throws BadAccessorException
    {
        this.defaultTarget = this.defaultTarget.transform(transformer, this);
        for (int i = 0; i < targets.length; i++) {
            this.targets[i] = this.targets[i].transform(transformer, this);
        }
    }

    /**
     * Sets the target for this instruction.
     */
    public void setTarget(InstructionAccessor target, int position)
    {
        if (getOpcode() != opc_xxxunusedxxx) {
            throw new InternalError("switch type already determined");
        }

        if (position == -1) {
            this.defaultTarget = target;
        }
        else {
            this.targets[position] = target;
        }
    }

    /**
     * Gets the number of 'case' labels.
     */
    public int getSwitchCount()
    {
        return matches.length;
    }

    /**
     * Returns the case label's value at the specified position
     * in the list of cases.
     */
    public int getMatch(int position)
    {
        return matches[position];
    }

    /**
     * Returns the target of the specified position in the list of cases.
     */
    public InstructionAccessor getTarget(int position)
    {
        return (position == -1) ? defaultTarget : targets[position];
    }

    /**
     * Sets the target of the specified position in the list of cases.
     */
    public void setTarget(int position, InstructionAccessor accessor)
    {
        if (position == -1) {
            defaultTarget = accessor;
        }
        else { 
            targets[position] = accessor;
        }
    }

    /**
     * Select the appropriate switch type. (tableswitch or lookupswitch)
     */
    public void selectSwitchType()
    {
        int[] matches = this.matches;
        InstructionAccessor[] targets = this.targets;
        int count  = 0;
        int minval = 0;
        int maxval = 0;

        for (int i = 0; i < matches.length; i++) {
            // ignore matches with default target
            if (targets[i] == defaultTarget) {
                continue;
            }

            if (count == 0) {
                minval = matches[i];
                maxval = matches[i];
            }
            else {
                if (matches[i] < minval) {
                    minval = matches[i];
                }
                if (matches[i] > maxval) {
                    maxval = matches[i];
                }
            }

            count++;
        }

        if (count == 0) {
            this.matches = new int[0];
            this.targets = new InstructionAccessor[0];

            setOpcode(opc_lookupswitch); // it uses less size
        }
        else {
            // use long operations to avoid overflows
            long distance = (long)maxval - minval + 1;

            // choose the switch type: tableswitch if density >= 0.5
            if (maxval < 0x7ffffff0 && distance <= 2 * count) {
                int length = (int)distance;

                // build arrays and initialise them
                // Note: the matches array is not really needed (only the
                // first value)
                this.matches = new int[length];
                this.targets = new InstructionAccessor[length];

                for (int i = 0; i < this.matches.length; i++) {
                    this.matches[i] = minval + i;
                    this.targets[i] = defaultTarget;
                }

                for (int i = 0; i < matches.length; i++) {
                    // ignore matches with default target
                    if (targets[i] == defaultTarget) {
                        continue;
                    }

                    this.targets[matches[i] - minval] = targets[i];
                }

                setOpcode(opc_tableswitch);
            }
            else {
                // build arrays
                this.matches = new int[count];
                this.targets = new InstructionAccessor[count];

                for (int i = 0, j = 0; i < matches.length; i++) {
                    // ignore matches with default target
                    if (targets[i] == defaultTarget) {
                        continue;
                    }

                    this.matches[j] = matches[i];
                    this.targets[j] = targets[i];
                    j += 1;
                }

                setOpcode(opc_lookupswitch);
            }
        }
    }

    /**
     * Returns the number of bytes used by the the instruction in the code
     * array.
     */
    int getSize()
    {
        return computeSize(getAddress());
    }

    /**
     * Returns the type pushed on the stack.
     */
    public byte getReturnType()
    {
        return TYP_REFERENCE;
    }

    /**
     * Verifies the enclosed instruction and computes the stack height.
     *
     * @param env
     * The check environment.
     *
     * @param curStack
     * The stack height at the end of the execution of the instruction.
     *
     * @return
     * true iff the next instruction in textual order needs to be checked,
     * i.e. this instruction has not been checked before and it can complete
     * normally.
     *
     * @exception ClassFileFormatException
     * A problem was detected.
     */
    void check(CodeEnv env, int curStack)
        throws ClassFileFormatException
    {
        env.checkExecutionPath((InstructionHandle)defaultTarget, curStack);
        for (int i = 0; i < targets.length; i++) {
            env.checkExecutionPath((InstructionHandle)targets[i], curStack);
        }
    }

    /**
     * Computes the address of the end of the instruction.
     *
     * @param position
     * The minimum and maximum address of the begin of this instruction.
     * This parameter is changed to the minimum and maximum address of the
     * end of this instruction.
     */
    void computeEndAddress(CodePosition position)
    {
        int  size = computeSize(position.max);
        position.min += size;
        position.max += size;
    }

    /**
     * Returns the size of data pushed on the stack by this instruction.
     */
    public int getPushedOnStack()
    {
        return 0;
    }

    /**
     * Return the amount of stack (positive or negative) used by this
     * instruction.
     */
    public int getStack()
    {
        return -1;
    }

    /**
     * Insert or check location of constant value on constant pool.
     *
     * @param cp
     * The constant pool for this class.
     */
    void resolveConstants(ConstantPool cp)
    {}

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

        /* Added pre-calculated padding to pad to next 4-byte boundary */
        for (int i = 0; i < pad; i++) {
            out.writeByte(0);
        }

        /* default target */
        out.writeInt(((Instruction)defaultTarget).getAddress() - getAddress());

        if (getOpcode() == opc_tableswitch) {
            out.writeInt(matches[0]);
            out.writeInt(matches[0] + targets.length - 1);

            for (int i = 0; i < targets.length; i++) {
                out.writeInt(((Instruction)targets[i]).getAddress()
                             - getAddress());
            }
        }
        else if (getOpcode() == opc_lookupswitch) {
            out.writeInt(targets.length);
            sort(matches, targets);
            for (int i = 0; i < targets.length; i++) {
                out.writeInt(matches[i]);
                out.writeInt(((Instruction)targets[i]).getAddress()
                             - getAddress());
            }
        }
        else {
            throw new InternalError("switch type not yet determined");
        }
    }

    /**
     * Compute the size of this instruction.
     */
    private int computeSize(int start)
    {
        if (getOpcode() == opc_xxxunusedxxx) {
            selectSwitchType();
        }

        if (getOpcode() == opc_tableswitch) {
            /* Total size = 1 + padding + 4 + 4 + 4 + jumptable */
            int size = 1 + 12;

            /* Calculate padding for table to start at 4-byte boundary */
            if (((start + 1) % 4) == 0) {
                pad = 0;
            }
            else {
                pad = 4 - ((start + 1) % 4);
            }

            size += pad + 4 * targets.length;

            return size;
        }
        else if (getOpcode() == opc_lookupswitch) {
            /* Total size = 1 + padding + 4 + 4 + jumptable */
            int size = 1 + 8;

            /* Calculate padding for table to start at 4-byte boundary */
            if (((start + 1) % 4) == 0) {
                pad = 0;
            }
            else {
                pad = 4 - ((start + 1) % 4);
            }

            size += pad;

            if (targets != null) {
                size += 8 * targets.length;
            }

            return size;
        }
        else {
            throw new InternalError("unexpected opcode: " + getOpcode());
        }
    }

    /**
     * Bubble sorts and array of integers and keeps the values of an
     * array of instructions in-synch with the array of integers.
     */
    private static final void sort(int[] matches, InstructionAccessor[] instrs)
    {
        for (int i = matches.length; --i >= 0;) {
            for (int j = 0; j < i; j++) {
                if (matches[j] > matches[j + 1]) {
                    int   tmpMatch = matches[j];
                    InstructionAccessor tmpInstr = instrs[j];

                    instrs[j] = instrs[j+1];
                    matches[j] = matches[j+1];
                    instrs[j+1] = tmpInstr;
                    matches[j+1] = tmpMatch;
                }
            }
        }
    }
}

