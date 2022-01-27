
package fr.lifl.magique.platform.classloader;

public interface ClassInspector
{

    public String getClassName();
    
    public boolean hasASuperClassOtherThanObject();
    
    public String getSuperClassName();
    
    public boolean hasInterfaces();
    
    public String[] getInterfacesNames();

    public String[] getAllReferencedClasses();

    public boolean hasInnerClasses();

    public String[] getInnerClassesNames();

    public boolean hasFields();

    public String[] getFieldsClassesNames();

    public boolean hasMethods();

    public String[] getClassesNamesInsideMethods();

    // We should had a method like the following to warn the user
    // We just have to look if we inherit from classloader or if
    // we use somewhere the method "Class.loadClass(String)" && "Class.forName(String)" !
    // public boolean usesDynamicClassLoading()

}
