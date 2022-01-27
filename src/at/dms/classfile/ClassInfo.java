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

import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import at.dms.util.Utils;

/**
 * Class file representation: VMS 4.1 Class File.
 *
 * This is the place where all information about the class defined
 * by this class file resides.
 */
public class ClassInfo
    extends Member
{
    /**
     * Major version of class file format.
     */
    private int majorVersion;

    /**
     * Minor version of class file format.
     */
    private int minorVersion;

    /**
     * Name of the class in the class file.
     */
    private ClassConstant thisClass;

    /**
     * Name of the superclass of the class in the class file.
     */
    private ClassConstant superClass;

    /**
     * Names of the interfaces that are implemented by the class in the class
     * file.
     */
    private ClassConstant[] interfaces;

    /**
     * The fields in the classfile.
     */
    private FieldInfo[] fields;

    /**
     * The methods in the classfile.
     */
    private MethodInfo[] methods;

    /**
     * Other attributes in the classfile.
     */
    private AttributeList attributes;

    /**
     * Constructs a new class info structure.
     *
     * @param modifiers
     * Access permission to and properties of this class.
     * @param thisClass
     * The class or interface defined by this class file.
     * @param superClass
     * The superclass of this class.
     * @param interfaces
     * The interfaces implemented by this class.
     * @param fields
     * The fields which are members of this class.
     * @param methods
     * The methods which are members of this class.
     * @param innerClasses
     * The inner classes which are members of this class.
     * @param sourceFile
     * The name of the source file.
     * @param deprecated
     * Is this class deprecated?
     */
    public ClassInfo(short modifiers,
                     String thisClass, String superClass,
                     List interfaces, List fields, List methods,
                     InnerClassInfo[] innerClasses,
                     String sourceFile,
                     boolean deprecated)
    {
        this(modifiers,
             thisClass,
             superClass,
             makeInterfacesArray(interfaces),
             makeFieldInfoArray(fields),
             makeMethodInfoArray(methods),
             innerClasses,
             sourceFile,
             deprecated);
    }

    /**
     * Constructs a new class info structure.
     *
     * @param modifiers
     * Access permissions to and properties of this class.
     * @param thisClass
     * The class or interface defined by this class file.
     * @param superClass
     * The superclass of this class.
     * @param interfaces
     * The interfaces implemented by this class.
     * @param fields
     * Tthe fields which are members of this class.
     * @param methods
     * The methods which are members of this class.
     * @param innerClasses
     * The inner classes which are members of this class.
     * @param sourceFile
     * The name of the source file.
     * @param deprecated
     * Is this class deprecated?
     */
    public ClassInfo(short modifiers,
                     String thisClass,
                     String superClass,
                     ClassConstant[] interfaces,
                     FieldInfo[] fields,
                     MethodInfo[] methods,
                     InnerClassInfo[] innerClasses,
                     String sourceFile,
                     boolean deprecated)
    {
        minorVersion = JAVA_MINOR;
        majorVersion = JAVA_MAJOR;

        setModifiers((short)(modifiers | ACC_SUPER));
        this.thisClass = new ClassConstant(thisClass);
        this.superClass = superClass != null ?
            new ClassConstant(superClass) :
            null;
        this.fields = fields;
        this.methods = methods;
        this.interfaces = interfaces;

        this.attributes =
            new AttributeList(innerClasses != null
                              ? new InnerClassTable(innerClasses) : null,
                              sourceFile != null
                              ? new SourceFileAttribute(sourceFile) : null,
                              deprecated ? new DeprecatedAttribute() : null);
    }

    /**
     * Constructs a class info structure from a class file.
     *
     * @param in
     * The stream to read the class from.
     * @param interfaceOnly
     * Load only the interface, not the source code.
     *
     * @exception IOException
     * An io problem has occured.
     * @exception ClassFileFormatException
     * Attempt to read from a bad classfile.
     */
    public ClassInfo(DataInput in, boolean interfaceOnly)
        throws IOException, ClassFileFormatException
    {
        int  magic = in.readInt();
        if (magic != JAVA_MAGIC) {
            throw new ClassFileFormatException("Bad magic number: " + magic);
        }

        minorVersion = in.readUnsignedShort();
        majorVersion = in.readUnsignedShort();

        ConstantPool constantPool = new ConstantPool(in);

        setModifiers((short)in.readUnsignedShort());
        thisClass =
            (ClassConstant)constantPool.getEntryAt(in.readUnsignedShort());
        superClass =
            (ClassConstant)constantPool.getEntryAt(in.readUnsignedShort());

        interfaces = new ClassConstant[in.readUnsignedShort()];
        for (int i = 0; i < interfaces.length; i += 1) {
            interfaces[i] =
                (ClassConstant)constantPool.getEntryAt(in.readUnsignedShort());
        }

        fields = new FieldInfo[in.readUnsignedShort()];
        for (int i = 0; i < fields.length; i += 1) {
            fields[i] = new FieldInfo(in, constantPool);
        }

        methods = new MethodInfo[in.readUnsignedShort()];
        for (int i = 0; i < methods.length; i += 1) {
            methods[i] = new MethodInfo(in, constantPool, interfaceOnly);
        }

        this.attributes = new AttributeList(in, constantPool, false);

        constantPool.close();
        constantPool = null; // save memory
    }

    /**
     * Returns the name of the this class (fully qualified).
     */
    public String getName()
    {
        return thisClass.getName();
    }

    /**
     * Sets the name of the this field (fully qualified).
     */
    public void setName(String name)
    {
        thisClass = new ClassConstant(name);
    }

    /**
     * Returns the type of this class.
     */
    public String getSignature()
    {
        return thisClass.getName();
    }

    /**
     * Returns the super class of the class in the file.
     */
    public String getSuperClass()
    {
        return superClass == null ? null : superClass.getName();
    }

    /**
     * Sets the super class of the class in the file.
     */
    public void setSuperClass(String superClass)
    {
        this.superClass = (superClass == null
                           ? null : new ClassConstant(superClass));
    }

    /**
     * Returns the version of the class in the file.
     */
    public int getMajorVersion()
    {
        return majorVersion;
    }

    /**
     * Sets the version of the class in the file.
     */
    public void setMajorVersion(int majorVersion)
    {
        this.majorVersion = majorVersion;
    }

    /**
     * Returns the version of the class in the file.
     */
    public int getMinorVersion()
    {
        return minorVersion;
    }

    /**
     * Sets the version of the class in the file.
     */
    public void setMinorVersion(int minorVersion)
    {
        this.minorVersion = minorVersion;
    }

    /**
     * Returns the inner classes table of the class in the file.
     */
    public InnerClassInfo[] getInnerClasses()
    {
        Attribute  attr = attributes.get(Constants.ATT_INNERCLASSES);

        return attr == null ? null : ((InnerClassTable)attr).getEntries();
    }

    /**
     * Sets the inner classes table of the class in the file.
     */
    public void setInnerClasses(InnerClassInfo[] inners)
    {
        if (inners != null) {
            attributes.add(new InnerClassTable(inners));
        }
        else {
            attributes.remove(Constants.ATT_INNERCLASSES);
        }
    }

    /**
     * Returns the source file of the class in the file.
     */
    public String getSourceFile()
    {
        Attribute  attr = attributes.get(Constants.ATT_SOURCEFILE);

        return attr == null ? null : ((SourceFileAttribute)attr).getValue();
    }

    /**
     * Returns the source file of the class in the file.
     */
    public void setSourceFile(String name)
    {
        if (name != null) {
            attributes.add(new SourceFileAttribute(name));
        }
        else {
            attributes.remove(Constants.ATT_SOURCEFILE);
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
     * Returns the interfaces of the class in the file.
     */
    public String[] getInterfaces()
    {
        String[] names = new String[interfaces.length];

        for (int i = 0; i < interfaces.length; i++) {
            names[i] = interfaces[i].getName();
        }

        return names;
    }

    /**
     * Sets the interfaces of the class in the file.
     */
    public void setInterfaces(String[] interfaces)
    {
        this.interfaces = new ClassConstant[interfaces.length];
        for (int i = 0; i < this.interfaces.length; i++) {
            this.interfaces[i] = new ClassConstant(interfaces[i]);
        }
    }

    /**
     * Returns the fields info of the class in the file.
     */
    public FieldInfo[] getFields()
    {
        return fields;
    }

    /**
     * Sets the fields info of the class in the file.
     */
    public void setFields(FieldInfo[] fields)
    {
        this.fields = fields;
    }

    /**
     * Returns the methods info of the class in the file.
     */
    public MethodInfo[] getMethods()
    {
        return methods;
    }

    /**
     * Sets the methods info of the class in the file.
     */
    public void setMethods(MethodInfo[] methods)
    {
        this.methods = methods;
    }

    /**
     * Writes the content of the class to the specified output stream.
     *
     * @param out
     * The stream to write to.
     *
     * @exception IOException
     * An io problem has occured.
     * @exception ClassFileFormatException
     * Attempt to write a bad classfile info.
     */
    public void write(DataOutput out)
        throws IOException, ClassFileFormatException
    {
        ConstantPool constantPool = resolveConstants();

        // Headers
        out.writeInt(JAVA_MAGIC);
        out.writeShort(minorVersion);
        out.writeShort(majorVersion);

        constantPool.write(out);

        // Class hierarchy/access
        out.writeShort(getModifiers());
        out.writeShort(thisClass.getIndex());
        out.writeShort(superClass == null ? 0 : superClass.getIndex());

        // interfaces
        out.writeShort(interfaces.length);
        for (int i = 0; i < interfaces.length; i++) {
            out.writeShort(interfaces[i].getIndex());
        }

        // fields
        out.writeShort(fields.length);
        for (int i = 0; i < fields.length; i++) {
            fields[i].write(constantPool, out);
        }

        // methods
        out.writeShort(methods.length);
        for (int i = 0; i < methods.length; i++) {
            methods[i].write(constantPool, out);
        }

        // attributes
        attributes.write(constantPool, out);

        constantPool.close();
    }

    /**
     * Writes the contents of the class to a file.
     *
     * @param destination
     * The root directory of the class hierarchy.
     *
     * @exception IOException
     * An io problem occured.
     *
     * @exception ClassFileFormatException
     * Attempt to write a bad classfile info.
     */
    public void write(String destination)
        throws IOException, ClassFileFormatException
    {
        String[] nameSplit = Utils.splitQualifiedName(getName(), '/');

        File writeDir = new File(destination, nameSplit[0]);
        String writeFile = nameSplit[1] + ".class";

        write(writeDir, writeFile);
    }

    /**
     * Writes the contents of the class to a file.
     *
     * @param destination
     * The root directory of the class hierarchy.
     *
     * @exception IOException
     * An io problem occured.
     *
     * @exception ClassFileFormatException
     * Attempt to write a bad classfile info.
     */
    public void write(File writeDir, String writeFile)
        throws IOException, ClassFileFormatException
    {
        if (writeDir == null) {
            writeDir = new File(".");
        }

        if ( !writeDir.exists() ) {
            writeDir.mkdirs();
        }
        
        if ( !writeDir.isDirectory() ) {
            //!!! FIXME graf 000319: USE A CHECKED EXCEPTION
            throw new RuntimeException("File " + writeDir
                                       + " is not a directory.");
        }

        File outputFile = new File(writeDir, writeFile);
        // System.err.println("Destination: " + writeDir + " , " + writeFile);
        // System.err.println("Output file: " + writeFile);

        DataOutputStream outputStream =
            new DataOutputStream(new BufferedOutputStream
                (new FileOutputStream(outputFile)));

        write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * Create a constant pool and insert in it all constants from from
     * all the element of the class (fields, method, attributes, ...)
     */
    private ConstantPool resolveConstants()
        throws ClassFileFormatException
    {
        try {
            ConstantPool constantPool = new ConstantPool();
            
            constantPool.addItem(thisClass);
            if (superClass != null) {
                constantPool.addItem(superClass);
            }
            
            for (int i = 0; i < interfaces.length; i++) {
                constantPool.addItem(interfaces[i]);
            }
            
            for (int i = 0; i < fields.length; i++) {
                fields[i].resolveConstants(constantPool);
            }
            
            // methods (generate code, resolve constants, and pregen)
            for (int i = 0; i < methods.length; i++) {
                methods[i].resolveConstants(constantPool);
            }
            
            attributes.resolveConstants(constantPool);
            
            return constantPool;
        }
        catch (ClassFileFormatException e) {
            System.err.println("Class: " + thisClass.getName());
            throw e;
        }
    }

    /**
     * Converts list of FieldInfo objects to an array of them.
     */
    private static FieldInfo[] makeFieldInfoArray(List list)
    {
        return (FieldInfo[])list.toArray(new FieldInfo[list.size()]);
    }

    /**
     * Converts list of MethodInfo objects to an array of them.
     */
    private static MethodInfo[] makeMethodInfoArray(List list)
    {
        return (MethodInfo[])list.toArray(new MethodInfo[list.size()]);
    }

    /**
     * Converts list of Strings to an array of ClassConstant objects that
     * contain each string as a class or interface name.
     */
    private static ClassConstant[] makeInterfacesArray(List list)
    {
        ClassConstant[] array;

        array = new ClassConstant[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = new ClassConstant((String)list.get(i));
        }
        return array;
    }
}
