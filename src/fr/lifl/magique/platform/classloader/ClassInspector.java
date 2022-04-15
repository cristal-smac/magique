
package fr.lifl.magique.platform.classloader;

import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;

public interface ClassInspector
{

    public String getClassName();
    
    public boolean hasASuperClassOtherThanObject() throws NotFoundException;
    
    public String getSuperClassName() throws NotFoundException;
    
    public boolean hasInterfaces() throws NotFoundException;
    
    public String[] getInterfacesNames() throws NotFoundException;

    public String[] getAllReferencedClasses() throws NotFoundException, BadBytecode;

    public boolean hasInnerClasses() throws NotFoundException;

    public String[] getInnerClassesNames() throws NotFoundException;

    public boolean hasFields();

    public String[] getFieldsClassesNames();

    public boolean hasMethods();

    public String[] getClassesNamesInsideMethods() throws NotFoundException, BadBytecode;

    // We should had a method like the following to warn the user
    // We just have to look if we inherit from classloader or if
    // we use somewhere the method "Class.loadClass(String)" && "Class.forName(String)" !
    // public boolean usesDynamicClassLoading()

}
