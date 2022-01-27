package fr.lifl.magique.platform.classloader;

import java.io.*;
import java.net.*;
import java.util.*;

public class BytecodeClassLoader extends ClassLoader {

	protected ClassLibrary myLibrary;
	private ResourceLoader resourceLoader;

	public BytecodeClassLoader() {
		super(ClassLoader.getSystemClassLoader());
		resourceLoader = new ResourceLoader(new URL[] {});
		myLibrary = new ClassLibrary(resourceLoader);
	}

	public boolean knownClassArchive(String classname) {
		//System.out.println(">>>>>>>>>"+classname+ " "+myLibrary.knownClassArchive(classname)); 

		return myLibrary.knownClassArchive(classname);
	}

	public ClassArchive getClassArchive(String classname) throws ClassArchiveNotFoundException {
		if (myLibrary.knownClassArchive(classname)) {
			return myLibrary.getClassArchive(classname);
		}
		else {
			throw new ClassArchiveNotFoundException();
		}
	}

	protected boolean classIsSystem(String prefix, String className, String separator) {
		return isExcluded(prefix, className.replace('.', '/'));
		//  	boolean result =  
		//  //    		! (className.equals("fr.lifl.magique.platform.Platform") ||
		//  //    		    className.equals("fr.lifl.magique.agent.PlatformAgent"))
		//  //    		&&
		//  		(className.startsWith(prefix+"java"+separator)   || 
		//  		className.startsWith(prefix+"javax"+separator) ||
		//  		className.startsWith(prefix+"org"+separator+"omg") || 
		//  		 //		 		 className.startsWith(prefix+"fr.lifl.magique"+separator) ||
		//  		 className.startsWith(prefix+"fr.lifl.magique.platform.classloader"+separator) ||
		//  		 className.startsWith(prefix+"fr/lifl/magique/platform/classloader"+separator) ||
		//  		 //  		 className.startsWith(prefix+"fr.lifl.magique.platform.rmi"+separator) ||
		//  		 className.startsWith(prefix+"fr.lifl.magique.gui.descriptor"+separator) ||
		//  		 className.startsWith(prefix+"fr/lifl/magique/gui/descriptor"+separator) ||
		//  		 className.startsWith(prefix+"fr.lifl.magique.gui.draw"+separator) ||
		//  		 className.startsWith(prefix+"fr/lifl/magique/gui/draw"+separator) ||
		//  		 className.startsWith(prefix+"fr.lifl.magique.gui.file"+separator) ||
		//  		 className.startsWith(prefix+"fr/lifl/magique/gui/file"+separator) ||
		//  		 className.startsWith(prefix+"fr.lifl.magique.gui.tree"+separator) ||
		//  		 className.startsWith(prefix+"fr/lifl/magique/gui/tree"+separator) ||
		//  		 className.startsWith(prefix+"fr.lifl.magique.gui.util"+separator) ||
		//  		 className.startsWith(prefix+"fr/lifl/magique/gui/util"+separator) ||
		//  		 className.startsWith(prefix+"bsh"+separator) ||

		//  		 className.startsWith(prefix+"org"+separator) ||  

		//  		 className.startsWith(prefix+"org"+separator+"w3c") ||  
		//  		 className.startsWith(prefix+"org"+separator+"apache") ||  

		//  //  		className.startsWith(prefix+"fr.lifl.magique.gui.Interface.MultiAgentsFrame"+separator) ||
		//  //  		className.startsWith(prefix+"fr/lifl/magique/gui/Interface/MultiAgentsFrame"+separator) ||
		//  		 className.startsWith(prefix+"sun"+separator));
		//  	if (result) System.out.println(")> excluded "+className);	
		//  	    return(result);

	}

	protected boolean classIsMagique(String prefix, String className, String separator) {
		//	System.out.println("xxxxxxxxxx |"+className+"|");
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
		//	System.err.println("******** >>>>> "+className);	
		System.err.print(".");
//	CHANGE 2004
//		if (className.indexOf('.') != -1) {
//			return findClass(className.replace('.', '/'));
			if (className.indexOf('/') != -1) {
				return findClass(className.replace('/', '.'));
		}
		else {
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
//		String className = classname + ".class";
		String className = classname.replace('.','/') + ".class";
		// fin 2004
		if (className.startsWith("[L")) {
			className = className.substring(2, className.length());
			//				System.err.println("findClass >> "+className);
		}
		//		System.out.println("Asked to find class :"+className);
		//	dump();
		ClassArchive ca = null;
		//		System.out.println("Loading "+classname.replace('/','.')+" from ClassArchiveLibrary");
		if (myLibrary.knownClassArchive(classname.replace('/', '.'))) {
			//	    	    	    System.out.println("Loading "+classname.replace('/','.')+" from ClassArchiveLibrary");
			ca = myLibrary.getClassArchive(classname.replace('/', '.'));
			//	    System.out.println("####"+ca);
		}
		else {
			if (classIsMagique("", className, ".") || classIsMagique("", className, "/")) {
				// System.out.println("magique :" +className);
				// simplement pour le chargement du bytecode des classe Magique
				ca = new ClassArchive(className, myLibrary);
				//		    ca = myLibrary.newClassArchive(className);		    
			}
			else {
				//    		System.out.println("We got ClassArchive 1 : "+ca);
				Hashtable h = (new ClassDependancy(myLibrary, packageList)).getDependantClasses(className);
				//				System.out.println("We got ClassArchive 2 : "+ca);

				//		    System.out.println("***              "+classname);
				ca = myLibrary.getClassArchive(classname.replace('/', '.'));
			}
		}
		//		System.out.println("We got ClassArchive : "+ca);
		Class c = null;
		byte[] bytecode = ca.getBytecode();
		try {							  
			c = defineClass(classname, bytecode, 0, bytecode.length);
		}
		catch (ClassFormatError e) {
			e.printStackTrace();
		}
		//	ca.flush();
		return c;
	}

	// addition for package exclusion
	private List packageList = initPackageList();
	private List initPackageList() {
		List l = new ArrayList();
		l.add("fr/lifl/magique/platform/classloader");
		l.add("bsh");
		l.add("at/dms");
		l.add("gnu/bytecode");
		l.add("java");
		l.add("org/omg");
		l.add("sun");
		return l;
	}
	public void addPathToExclude(String packagePrefix) {
		packageList.add(packagePrefix.replace('.', '/')); // :-(
	}

	private boolean isExcluded(String prefix, String currentName) {
		boolean excluded = false;
		Iterator it = packageList.iterator();
		while (!excluded && it.hasNext()) {
			String name = (String) it.next();
			excluded = currentName.startsWith(prefix + name);
			//	    if (excluded) System.out.println(">> excluded "+currentName);	    
		}
		return excluded;
	}
	// end addition for package exclusion

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

	// ----------------------------------------------------------------

	public static void main(String arg[]) {
		BytecodeClassLoader myLoader = new BytecodeClassLoader();
		try {
			System.out.println("Trying to load class " + arg[0]);
			Class c = myLoader.loadClass(arg[0]);
			System.out.println("Class " + arg[0] + " loaded ->" + c);
			System.out.println("Instanciation " + c.newInstance());
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (NoClassDefFoundError e) {
			e.printStackTrace();
			System.out.println("Instantiation has failed -> NoClassDefFoundError.");
		}
	}
}
