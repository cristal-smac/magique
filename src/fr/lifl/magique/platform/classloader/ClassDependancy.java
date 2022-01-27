/**
 * This class is used to determine the graph of dependancy of a bytecode
 * ".class" It takes an URL, parses the bytecode of the class and delivers
 * informations about class that are used in the current bytecode.
 * 
 * @author Yann SECQ (secq@lifl.fr)
 * @version 0.1b
 *  
 */

package fr.lifl.magique.platform.classloader;

import java.io.*;
import java.net.*;
import java.util.*;

import at.dms.classfile.*;

public class ClassDependancy {

   private ClassLibrary classLibrary;
   private Hashtable bytecodeParsed;
   private Vector bytecodeToBeParsed;

   // --------------------------------------------------
   private boolean DEBUG = false;

   public void trace(String methodName, String message) {
      if (DEBUG) {
         System.out.println(this.getClass() + ": [" + methodName + "]" + " -> " + message);
      }
   }

   //---------------------------------------------------

   public ClassDependancy(ClassLibrary classLibrary) {
      this.classLibrary = classLibrary;
      bytecodeParsed = new Hashtable();
      bytecodeToBeParsed = new Vector();
   }

   private List packageList = new ArrayList();

   public ClassDependancy(ClassLibrary classLibrary, List packList) {
      this(classLibrary);
      Iterator it = packList.iterator();
      while (it.hasNext()) {
         this.packageList.add(it.next());
      }
      packageList.add("fr/lifl/magique");
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

   public Hashtable getDependantClasses(String rootClass) throws ClassNotFoundException {
      Enumeration newClasses;
      String currentClass;
      String otherClass;
      bytecodeToBeParsed.addElement(rootClass);
      while (bytecodeToBeParsed.size() > 0) {
         currentClass = getNextClass();
         newClasses = getFirstLevelDependancy(currentClass).elements();
         while (newClasses.hasMoreElements()) {
            otherClass = (String) newClasses.nextElement();
            if (!bytecodeParsed.containsKey((Object) otherClass.replace('/', '.'))
                  && !bytecodeToBeParsed.contains((Object) otherClass.replace('/', '.'))) {
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
         //	    if (excluded) System.out.println("*** >>> excluded "+currentName);
      }
      return excluded;
   }

   // end addition 29/02/2002

   public Vector getFirstLevelDependancy(String className) throws ClassNotFoundException {
      Vector allClasses = new Vector();
      ClassArchive classArchive;

      classArchive = classLibrary.newClassArchive(className.substring(0, className.lastIndexOf('.')));

      //    classLibrary.addClassArchive(classArchive);
      String currentName = null;
      ClassInspector cl = null;
      try {
         cl = new ClassInspectorImpl(new DataInputStream(new ByteArrayInputStream(classArchive.getBytecode())));
      } catch (ClassFileFormatException e) {
         e.printStackTrace();
      } catch (IOException ee) {
         ee.printStackTrace();
      }
      String[] allClassesRef = cl.getAllReferencedClasses();
      for (int i = 0; i < allClassesRef.length; i++) {
         currentName = allClassesRef[i].replace('.', '/');
         //		System.err.println("("+i+","+allClassesRef[i]+") Watching
         // "+currentName);
         if (!isExcluded(currentName) &&
         //  	    !currentName.startsWith("java") &&
               //  // !currentName.startsWith("Ljava") &&
               //  // !currentName.startsWith("[Ljava") &&
               //  	    !currentName.startsWith("org/omg") &&

               //  	    !currentName.startsWith("org/w3c") &&
               //  	    !currentName.startsWith("org/apache") &&

               //  	    !currentName.startsWith("fr/lifl/magique") &&
               !currentName.equals(cl.getClassName().replace('.', '/'))) {
            //		System.out.println("*** "+currentName);
            allClasses.addElement(currentName);
            		//System.err.println("                                       adding class : "+currentName);
            ClassArchive dependancy = classLibrary.newClassArchive(currentName);
            classArchive.addDependency(dependancy);
            //		classLibrary.addClassArchive(dependancy);
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
         //       System.err.println("Adding "+currentName+" to known classes");
      }
      return allClasses;
   }

   //    public static void main(String args[]) throws ClassNotFoundException {
   //      if (args.length < 2) {
   //        System.out.println("This class parse a bytecode file and produce a
   // dependancy graph");
   //        System.out.println("Usage: java ClassDependancy <file:/home/toto/>
   // <hello.class>");
   //        System.out.println("if you want a class located in a jar : gnu.test.toto
   // -> type gnu/test/toto.class");
   //      } else {
   //        URL urls[] = {};
   //        ResourceLoader resourceLoader = new ResourceLoader(urls);
   //        resourceLoader.addURL(args[0]);
   //        ClassLibrary classLibrary = new ClassLibrary(resourceLoader);
   //        ClassDependancy test = new ClassDependancy(classLibrary);
   //        System.out.println("The class "+args[1]+" needs the following classes :");
   //        System.out.println("Direct Dependencies :
   // "+test.getFirstLevelDependancy(args[1]));
   //        System.out.println();
   //        System.out.println("The complete class needed are :");
   //        test.getDependantClasses(args[1]);
   //        Enumeration e = classLibrary.getAll().elements();
   //        while (e.hasMoreElements()) {
   //  	((ClassArchive) e.nextElement()).flush();
   //        }
   //        System.out.println();
   //        System.out.println("Serialization of "+args[1]);
   //        try {
   //  	ObjectOutputStream out = new ObjectOutputStream(new
   // FileOutputStream(args[1]+".ser"));
   //  	System.out.println("URL : "+resourceLoader.getResourceURL(args[1]));
   //  	System.out.println("Object:
   // "+classLibrary.getClassArchive(args[1].substring(0,args[1].length()-6)));
   //  	out.writeObject(classLibrary.getClassArchive(args[1].substring(0,args[1].length()-6)));
   //  	out.flush();
   //  	out.reset();
   //  	out.close();
   //  	System.out.println(" done !");
   //  	classLibrary.clearAll();
   //  	System.out.print("Trying to load it back");
   //  	ObjectInputStream in = new ObjectInputStream(new
   // FileInputStream(args[1]+".ser"));
   //  	ClassArchive toot = (ClassArchive) in.readObject();
   //  	in.close();
   //  	toot.flush();
   //  	ClassLibrary otherClassLibrary = new ClassLibrary(resourceLoader);
   //  	otherClassLibrary.addClassArchive(toot);
   //  	System.out.println(" done !");
   //  	e = otherClassLibrary.getAll().elements();
   //  	while (e.hasMoreElements()) {
   //  	  ((ClassArchive) e.nextElement()).flush();
   //  	}
   //  	System.out.println();
   //        } catch (Exception ex){
   //  	ex.printStackTrace();
   //        }
   //      }
   //    }

}
