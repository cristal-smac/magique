
package fr.lifl.magique.gui.skills;
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.*;

/**
 * A class that is a classLoader for the skill CreateAgentSkill
 *
 * @version 1.0 04/04/00
 * @author Fabien Niquet
 */

public class DaemonClassLoader {
    private URL[] VURLs;
/**
 	 * Creates a DaemonClass Loader
 	 *
 	
 	 */
    public DaemonClassLoader() {
      this.checkURL();
    }

    private Class loadClass2 (String name,String classpath)  {
	String dirtmp=classpath;
	String classetmp=name;
        try {           //basic
            URLClassLoader load=new URLClassLoader(VURLs);
	    Class c=load.loadClass(name);
	    return c;
	} catch (Throwable err) {}
        int sepa=classetmp.lastIndexOf('.');
        if (sepa!=-1) { //pour les jars
          try {
            URLClassLoader load=new URLClassLoader(VURLs);
            Class c=load.loadClass(name.substring(sepa+1));
	    return c;
          } catch (Throwable err) {}
        }
	int num=0;
        while (num!=-1) {  //pour les packages
	    dirtmp=dirtmp.substring(0,dirtmp.length()-1);
            num=dirtmp.lastIndexOf('.'); 
	     if (num!=-1) {
		 classetmp=dirtmp.substring(num+1)+"."+classetmp;
		 
		 dirtmp = dirtmp.substring(0,num+1);
		 try {
                     URLClassLoader load=new URLClassLoader(VURLs);
		     Class c=load.loadClass(classetmp);
		     return c;
		 } catch (Throwable err) {} 
	     }
	}
	Vector Vurls=new Vector();
	try {
	Vurls.addElement(new URL("file:"+classpath.replace('.','/')));
	}catch(Throwable tt) {}
	int sep=classpath.lastIndexOf('.');
	while (sep!=-1)  {
	    try {
		Vurls.addElement(new URL("file:"+classpath.substring(0,sep).replace('.','/')));
		Vurls.addElement(new URL("file:"+classpath.substring(0,sep).replace('.','/')+System.getProperty("file.separator")));
		
	    }catch(Throwable err) {}
	    classpath=classpath.substring(0,sep);
       
	    sep=classpath.lastIndexOf('.');
	}  
	VURLs=new URL[Vurls.size()];
	for (int i=0;i<Vurls.size();i++) {
	    System.out.println("L'ajout des path:"+(URL)Vurls.elementAt(i));
	    VURLs[i]=(URL)Vurls.elementAt(i);  
}   
	try {
	    URLClassLoader load=new URLClassLoader(VURLs);
	    Class c=load.loadClass(name);
	    return c;
	} catch (Throwable err) {} 





  	  return null;
    }




    /*
     * return a Class defined by the name of the class and his classpath
     * @param name the name of the class
     * @param classpath the classpath fot this classe
     * @return the class
     */
    public Class loadClass (String name,String classpath)  {
        int num;
        num=name.lastIndexOf('.');
        if (num==-1)
          return loadClass2(name,classpath);
        Class resultat=loadClass2(name,classpath);
        if (resultat!=null)
          return resultat;
        return loadClass2(name.substring(num+1),classpath);
   }
   private void checkURL () {
     Vector Vurls=new Vector();
     String classpath=System.getProperty("java.class.path",".");
     char separator=System.getProperty("path.separator").charAt(0);
     String url=classpath;
     if (classpath.endsWith(separator+""))
       classpath=classpath.substring(0,classpath.length()-1);
     int sep=classpath.lastIndexOf(separator);
     while (sep!=-1)  {
       try {
          Vurls.addElement(new URL("file:"+classpath.substring(sep+1)));
          Vurls.addElement(new URL("file:"+classpath.substring(sep+1)+System.getProperty("file.separator")));
       }catch(Throwable err) {}
       classpath=classpath.substring(0,sep);
       
       sep=classpath.lastIndexOf(separator);
     }  
      sep=classpath.lastIndexOf('.');
     while (sep!=-1)  {
       try {
          Vurls.addElement(new URL("file:"+classpath.substring(sep+1).replace('.','/')));
          Vurls.addElement(new URL("file:"+classpath.substring(sep+1).replace('.','/')+System.getProperty("file.separator")));
       }catch(Throwable err) {}
       classpath=classpath.substring(0,sep);
       
       sep=classpath.lastIndexOf(separator);
     }  
     try {
	 Vurls.addElement(new URL("file:."));
	 Vurls.addElement(new URL("file:"+classpath));
	 Vurls.addElement(new URL("file:"+"agents"+System.getProperty("file.separator")));
	 Vurls.addElement(new URL("file:"+"daemon"+System.getProperty("file.separator")+"agents"+System.getProperty("file.separator")));	
     }catch(Throwable rr) {System.out.println("WARNING");}
    
     VURLs=new URL[Vurls.size()];
     for (int i=0;i<Vurls.size();i++)      
         VURLs[i]=(URL)Vurls.elementAt(i);     
   }
}


      
