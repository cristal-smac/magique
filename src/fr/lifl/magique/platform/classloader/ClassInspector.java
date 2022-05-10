package fr.lifl.magique.platform.classloader;

import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;

public interface ClassInspector {

    String getClassName();

    boolean hasASuperClassOtherThanObject() throws NotFoundException;

    String getSuperClassName() throws NotFoundException;

    boolean hasInterfaces() throws NotFoundException;

    String[] getInterfacesNames() throws NotFoundException;

    String[] getAllReferencedClasses() throws NotFoundException, BadBytecode;

    boolean hasInnerClasses() throws NotFoundException;

    String[] getInnerClassesNames() throws NotFoundException;

    boolean hasFields();

    String[] getFieldsClassesNames();

    boolean hasMethods();

    String[] getClassesNamesInsideMethods() throws NotFoundException, BadBytecode;

    // We should had a method like the following to warn the user
    // We just have to look if we inherit from classloader or if
    // we use somewhere the method "Class.loadClass(String)" && "Class.forName(String)" !
    // public boolean usesDynamicClassLoading()

}
