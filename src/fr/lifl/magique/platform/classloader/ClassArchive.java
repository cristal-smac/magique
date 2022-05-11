/**
 * This class is used to store all available information about a class. It just
 * need the name of a class ( without the ".class") and it tries to find the
 * class in the classpath and load the bytecode but DOESN'T define the the class
 * in the JVM !!!
 *
 * @author Yann SECQ (secq@lifl.fr)
 * @version 0.1b
 */

package fr.lifl.magique.platform.classloader;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.ClassFile;

import java.io.*;
import java.net.URL;
import java.util.Vector;

public class ClassArchive implements Serializable {

    // Assume that we have a package easy containing a class hello :
    // /home/toto/java/easy/hello.class

    // The complete path and filename (as an URL) to reach the bytecode :
    // jar:http://www.home.com/toto/java/test.jar!/hello.class
    protected URL urlName;
    // The file name only
    protected String fileName;
    //---------------------------------------------------
    // The login name of the user who load the bytecode the first time in the
    // system
    protected String login;
    // The byte representing the bytecode : well well [.... bytecode of the file
    // hello.class ....]
    protected Byte[] bytecode;
    // The ClassType of the current bytecode : an intermediate format which
    // encapsulate an array of bytes in an object
    transient protected CtClass ctClass;
    // The name of java class : hello
    protected String className;
    // This Vector is used to keep references on dependant classes. Thus during
    // the serialization
    // process, the graph of dependent classes will be serialized naturally.
    protected Vector dependencies;
    // transient as we don't want to serialize all the bytecode contained in the
    // classLibrary ...
    transient protected ClassLibrary classLibrary = null;
    // --------------------------------------------------
    private final boolean DEBUG = false;

    /**
     * @param fileName :
     *           avec le .class
     */
    public ClassArchive(String fileName, ClassLibrary classLibrary) throws ClassNotFoundException {
        bytecode = null;
        ctClass = null;
        className = null;
        login = System.getProperty("user.name");
        dependencies = new Vector();
        this.classLibrary = classLibrary;
        this.fileName = fileName;
        this.urlName = classLibrary.getResourceURL(this.fileName);
        try {
            loadBytecode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void trace(String methodName, String message) {
        if (DEBUG) {
            System.out.println(this.getClass() + ": [" + methodName + "]" + " -> " + message);
        }
    }

    public ClassLibrary getClassLibrary() {
        return this.classLibrary;
    }

    public void setClassLibrary(ClassLibrary classLibrary) {
        ClassArchive dependantClassArchive;
        this.classLibrary = classLibrary;
        for (int i = 0; i < dependencies.size(); i++) {
            classLibrary.addClassArchive((ClassArchive) dependencies.elementAt(i));
        }
    }

    public URL getURLName() {
        return this.urlName;
    }

    public String getFileName() {
        return this.fileName;
    }

    // Return an exception if we can't find the file : pathToBytecode+fileName
    private void loadBytecode() throws ClassNotFoundException, IOException {
        if (bytecode == null) {
            if (fileName.startsWith("[L")) {
                int indexOfPoint = fileName.indexOf(";");
                fileName = fileName.substring(2, indexOfPoint) + ".class";
            }
            InputStream in = classLibrary.getInputStream(fileName);
            if (in == null) {
                throw new ClassNotFoundException(fileName);
            }
            int length = in.available();
            bytecode = new Byte[length];
            for (int i = 0; i < length; i++) {
                bytecode[i] = Byte.valueOf((byte) in.read());
            }
            in.close();
            loadClassType();
        }
    }

    public byte[] getBytecode() {
        byte[] bytes = new byte[bytecode.length];
        for (int i = 0; i < bytecode.length; i++) {
            bytes[i] = bytecode[i].byteValue();
        }
        return bytes;
    }

    // Return an exception if the bytecode has not yet benn loaded
    private void loadClassType() throws IOException {
        try {
            InputStream in = new ByteArrayInputStream(getBytecode());
            ctClass = ClassPool.getDefault().makeClass(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClassFile getClassFile() throws IOException {
        return ctClass.getClassFile();
    }

    public String getClassName() throws IOException {
        if (className == null) {
            this.className = ctClass.getName();
        }
        return className;
    }

    public void addDependency(ClassArchive classArchive) {
        if (!dependencies.contains(classArchive)) {
            dependencies.addElement(classArchive);
        }
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        trace("writeObject", "Calling my writeObject " + className);
    }

    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        trace("readObject", "Calling my readObject " + className);
        loadClassType();
        trace("readObject", "Reconstructiong ClassType : " + getClassFile());
    }

    public void flush() {
        System.out.println("----- ClassArchive Information ----- " + this);
        System.out.println("URL          : " + urlName);
        System.out.println("FileName     : " + fileName);
        System.out.println("User login   : " + login);
        System.out.println("Bytecode[]   : " + bytecode);
        System.out.println("ClassType    : " + ctClass);
        System.out.println("Class name   : " + className);
        System.out.println("ClassLibrary : " + classLibrary);
        System.out.print("Dependencies [" + dependencies.size() + "]: ");
        for (int i = 0; i < dependencies.size(); i++) {
            try {
                System.out.print(((ClassArchive) dependencies.elementAt(i)).getClassName() + ", ");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
        System.out.println();
    }

}
