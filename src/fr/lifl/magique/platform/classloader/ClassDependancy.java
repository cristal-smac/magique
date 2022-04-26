/**
 * This class is used to determine the graph of dependancy of a bytecode
 * ".class" It takes an URL, parses the bytecode of the class and delivers
 * informations about class that are used in the current bytecode.
 *
 * @author Yann SECQ (secq@lifl.fr)
 * @version 0.1b
 */

package fr.lifl.magique.platform.classloader;

import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

public class ClassDependancy {

    private final ClassLibrary classLibrary;
    private final Hashtable bytecodeParsed;
    private final Vector bytecodeToBeParsed;

    // --------------------------------------------------
    private final boolean DEBUG = false;
    private final List packageList = new ArrayList();

    //---------------------------------------------------

    public ClassDependancy(ClassLibrary classLibrary) {
        this.classLibrary = classLibrary;
        bytecodeParsed = new Hashtable();
        bytecodeToBeParsed = new Vector();
    }

    public ClassDependancy(ClassLibrary classLibrary, List packList) {
        this(classLibrary);
        Iterator it = packList.iterator();
        while (it.hasNext()) {
            this.packageList.add(it.next());
        }
        packageList.add("fr/lifl/magique");
    }

    public void trace(String methodName, String message) {
        if (DEBUG) {
            System.out.println(this.getClass() + ": [" + methodName + "]" + " -> " + message);
        }
    }

    private String getNextClass() {
        String nextClass = null;
        if (bytecodeToBeParsed.size() > 0) {
            nextClass = (String) bytecodeToBeParsed.elementAt(0);
            bytecodeToBeParsed.removeElementAt(0);
        }
        trace("getNextClass", " -> " + nextClass);
        return nextClass;
    }

    public Hashtable getDependantClasses(String rootClass) throws ClassNotFoundException, NotFoundException, BadBytecode, IOException {
        Enumeration newClasses;
        String currentClass;
        String otherClass;
        bytecodeToBeParsed.addElement(rootClass);
        while (bytecodeToBeParsed.size() > 0) {
            currentClass = getNextClass();
            newClasses = getFirstLevelDependancy(currentClass).elements();
            while (newClasses.hasMoreElements()) {
                otherClass = (String) newClasses.nextElement();
                if (!bytecodeParsed.containsKey(otherClass.replace('/', '.'))
                        && !bytecodeToBeParsed.contains(otherClass.replace('/', '.'))) {
                    trace("getDependantClass", "-> Adding " + otherClass + " to unknown classes");
                    bytecodeToBeParsed.addElement(otherClass + ".class");
                }
            }
        }
        return (Hashtable) bytecodeParsed.clone();
    }

    // addition 29/02/2002
    private boolean isExcluded(String currentName) {
        boolean excluded = false;
        Iterator it = packageList.iterator();
        while (!excluded && it.hasNext()) {
            String name = (String) it.next();
            excluded = currentName.startsWith(name);
        }
        return excluded;
    }

    // end addition 29/02/2002

    public Vector getFirstLevelDependancy(String className) throws ClassNotFoundException, NotFoundException, BadBytecode, IOException {
        Vector allClasses = new Vector();
        ClassArchive classArchive;

        classArchive = classLibrary.newClassArchive(className.substring(0, className.lastIndexOf('.')));

        String currentName = null;
        ClassInspector cl = new ClassInspectorImpl(new DataInputStream(new ByteArrayInputStream(classArchive.getBytecode())));

        String[] allClassesRef = cl.getAllReferencedClasses();
        for (int i = 0; i < allClassesRef.length; i++) {
            currentName = allClassesRef[i].replace('.', '/');
            if (!isExcluded(currentName) &&
                    !currentName.equals(cl.getClassName().replace('.', '/'))) {
                allClasses.addElement(currentName);
                ClassArchive dependancy = classLibrary.newClassArchive(currentName);
                classArchive.addDependency(dependancy);
            }
        }

        try {
            currentName = classArchive.getClassName();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!bytecodeParsed.containsKey(currentName)) {
            bytecodeParsed.put(currentName, classArchive);
            trace("getFirstLevelDependancy", "Adding " + currentName + " to known classes");
        }
        return allClasses;
    }
}
