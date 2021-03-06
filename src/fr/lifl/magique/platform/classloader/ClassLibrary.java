/**
 * This class is used to keep references on all available bytecode archive
 * It is not a class loader !
 *
 * @author Yann SECQ (secq@lifl.fr)
 * @version 0.1b
 */

package fr.lifl.magique.platform.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;

class ClassLibrary {

    // Used to store all ClassArchive that we own, the key used is the name of the class (sans ".class")
    private Hashtable allClassArchives;
    private ResourceLoader resourceLoader;

    public ClassLibrary(ResourceLoader resourceLoader) {
        this.allClassArchives = new Hashtable();
        this.resourceLoader = resourceLoader;
    }

    // FOR DEBUG ONLY !!!!!
    public Hashtable getAll() {
        return allClassArchives;
    }

    public void clearAll() {
        allClassArchives = new Hashtable();
    }

    // END OF DEBUGGING AREA

    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public URL getResourceURL(String fileName) {
        return this.resourceLoader.getResourceURL(fileName);
    }

    public InputStream getInputStream(String fileName) throws IOException {
        return resourceLoader.getInputStream(fileName);
    }

    public void addClassArchive(ClassArchive classArchive) {
        if (!knownClassArchive(classArchive)) {
            try {
                allClassArchives.put(classArchive.getClassName().intern(), classArchive);
                classArchive.setClassLibrary(this);
            } catch (IOException e) {
            }
        }
    }

    public ClassArchive newClassArchive(String className) throws ClassNotFoundException {
        ClassArchive classArchive = getClassArchive(className);
        if (classArchive == null) {
            classArchive = new ClassArchive(className + ".class", this);
            addClassArchive(classArchive);
        }
        return classArchive;
    }

    public boolean knownClassArchive(String classArchive) {
        return allClassArchives.containsKey(classArchive.intern());
    }

    public boolean knownClassArchive(ClassArchive classArchive) {
        try {
            return allClassArchives.containsKey(classArchive.getClassName().intern());
        } catch (IOException e) {
            return false;
        }
    }

    public ClassArchive getClassArchive(String className) {
        return (ClassArchive) allClassArchives.get(className);
    }

}
