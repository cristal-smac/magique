package fr.lifl.magique.platform.classloader;

import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BytecodeClassLoader extends ClassLoader {

    protected ClassLibrary myLibrary;
    private final ResourceLoader resourceLoader;
    // addition for package exclusion
    private final List packageList = initPackageList();

    public BytecodeClassLoader() {
        super(ClassLoader.getSystemClassLoader());
        resourceLoader = new ResourceLoader(new URL[]{});
        myLibrary = new ClassLibrary(resourceLoader);
    }

    public static void main(String[] arg) {
        BytecodeClassLoader myLoader = new BytecodeClassLoader();
        try {
            System.out.println("Trying to load class " + arg[0]);
            Class c = myLoader.loadClass(arg[0]);
            System.out.println("Class " + arg[0] + " loaded ->" + c);
            System.out.println("Instanciation " + c.newInstance());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            System.out.println("Instantiation has failed -> NoClassDefFoundError.");
        }
    }

    public boolean knownClassArchive(String classname) {
        return myLibrary.knownClassArchive(classname);
    }

    public ClassArchive getClassArchive(String classname) throws ClassArchiveNotFoundException {
        if (myLibrary.knownClassArchive(classname)) {
            return myLibrary.getClassArchive(classname);
        } else {
            throw new ClassArchiveNotFoundException();
        }
    }

    protected boolean classIsSystem(String prefix, String className, String separator) {
        return isExcluded(prefix, className.replace('.', '/'));
    }

    protected boolean classIsMagique(String prefix, String className, String separator) {
        String magiqueName = "fr.lifl.magique";
        return (className.startsWith(prefix + magiqueName.replace('.', separator.charAt(0))));
    }

    public Class loadClass(String className) throws ClassNotFoundException {
        // We need to find a better way to discriminate "core" classes !!!!
        if (classIsSystem("", className, ".")
                || classIsSystem("L", className, ".")
                || classIsSystem("[L", className, ".")
                || classIsSystem("", className, "/")
                || classIsSystem("L", className, "/")
                || classIsSystem("[L", className, "/")) {
            return super.loadClass(className);
        }
        Class c = findLoadedClass(className);
        if (c != null) {
            return c;
        }
        System.err.print(".");
        //	CHANGE 2004
        if (className.indexOf('/') != -1) {
            return findClass(className.replace('/', '.'));
        } else {
            return findClass(className);
        }
    }

    public void addURL(URL url) {
        resourceLoader.addURL(url);
    }

    public void addClassArchive(ClassArchive classArchive) {
        myLibrary.addClassArchive(classArchive);
    }

    // classname represent here the name of the class without the ".class" extension !!!
    protected Class findClass(String classname) throws ClassNotFoundException {
        // Here we get the FileName (and not the classname!)
        // CHANGE 2004
        String className = classname.replace('.', '/') + ".class";
        // fin 2004
        if (className.startsWith("[L")) {
            className = className.substring(2);
        }
        ClassArchive ca;
        if (myLibrary.knownClassArchive(classname.replace('/', '.'))) {
            ca = myLibrary.getClassArchive(classname.replace('/', '.'));
        } else {
            if (classIsMagique("", className, ".") || classIsMagique("", className, "/")) {
                // simplement pour le chargement du bytecode des classes Magique
                ca = new ClassArchive(className, myLibrary);
            } else {
                try {
                    Hashtable h = (new ClassDependancy(myLibrary, packageList)).getDependantClasses(className);
                } catch (NotFoundException | BadBytecode e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ca = myLibrary.getClassArchive(classname.replace('/', '.'));
            }
        }
        Class c = null;
        byte[] bytecode = ca.getBytecode();
        try {
            c = defineClass(classname, bytecode, 0, bytecode.length);
        } catch (ClassFormatError e) {
            e.printStackTrace();
        }
        return c;
    }

    private List initPackageList() {
        List l = new ArrayList();
        l.add("fr/lifl/magique/platform/classloader");
        l.add("bsh");
        l.add("javassist");
        l.add("java");
        l.add("java.base");
        l.add("org/omg");
        l.add("sun");
        l.add("jdk");
        return l;
    }

    public void addPathToExclude(String packagePrefix) {
        packageList.add(packagePrefix.replace('.', '/')); // :-(
    }
    // end addition for package exclusion

    private boolean isExcluded(String prefix, String currentName) {
        boolean excluded = false;
        Iterator it = packageList.iterator();
        while (!excluded && it.hasNext()) {
            String name = (String) it.next();
            excluded = currentName.startsWith(prefix + name);
        }
        return excluded;
    }

    // ----------------------------------------------------------------

    public void dump() {
        Hashtable h = myLibrary.getAll();
        Enumeration e = h.keys();
        System.out.println("*************************");
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            System.out.println("    >>" + key + " value " + h.get(key));
        }
        System.out.println("*************************");
    }
}
