package fr.lifl.magique.gui.util;
import java.net.*;
import java.io.*;
import java.util.*;


public class UniversalLoader {
    
    public Class loadClass (String name,String classpath)  {
	URL[] Path=new URL[2];
	String dirtmp=classpath;
	String classetmp=name;
	//	Path[0]=new URL("file:"+classpath) ;
	try {
	    Path[0]=new URL("file:"+classpath) ;
	Path[1]=new URL("file:"+classpath+File.separatorChar);
	    URLClassLoader load=new URLClassLoader(Path);
	    Class c=load.loadClass(name);
	    if (c!=null)
	    return c;
	} catch (Throwable err) {}
	int num=0;
	while (num!=-1) {
	    dirtmp=dirtmp.substring(0,dirtmp.length()-1);
	    num=dirtmp.lastIndexOf(System.getProperty("file.separator")); 
	     if (num!=-1) {
		 classetmp=dirtmp.substring(num+1)+"."+classetmp;
		 dirtmp = dirtmp.substring(0,num+1);
		 try {
		 Path[0]=new URL("file:"+dirtmp) ;
		 Path[1]=new URL("file:"+dirtmp+File.separatorChar);		 
		 URLClassLoader load=new URLClassLoader(Path);
		 Class c=load.loadClass(classetmp);
		 if (c!=null) return c;
		 } catch (Throwable err) {} 
	     }
	}
        num=0; dirtmp=classpath;
	while (num!=-1) {
	    dirtmp=dirtmp.substring(0,dirtmp.length()-1);
	    num=dirtmp.lastIndexOf(System.getProperty("file.separator")); 
	     if (num!=-1) {
		 dirtmp = dirtmp.substring(0,num+1);
		 try {
		 Path[0]=new URL("file:"+dirtmp) ;
		 Path[1]=new URL("file:"+dirtmp+File.separatorChar);		 
		 URLClassLoader load=new URLClassLoader(Path);
		 Class c=load.loadClass(name);
		 if (c!=null) return c;
		 } catch (Throwable err) {} 
	     }
	}
	int sep=name.lastIndexOf('.');
	if (sep==-1)
	    return this.loadClassFromClassPath(name);
	else return loadClass(name.substring(sep+1),classpath);
    }

    private Class loadClassFromClassPath(String name) {
	Vector Vurls=new Vector();
	URL[] VURLs;  
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
	try {
	    Vurls.addElement(new URL("file:."));
	    Vurls.addElement(new URL("file:"+classpath));
	}catch(Throwable rr) {}
	VURLs=new URL[Vurls.size()];
	for (int i=0;i<Vurls.size();i++)      
	    VURLs[i]=(URL)Vurls.elementAt(i);   
	try {          
            URLClassLoader load=new URLClassLoader(VURLs);
	    Class c=load.loadClass(name);
	    return c;
	} catch (Throwable err) {}
	
	return null;
	
    }
	
	
}






      
