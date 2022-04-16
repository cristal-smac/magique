/** This class is used to keep references of all resources available
  *  It keeps a list of URL, and tries to find resources  in the classpath. 
  *
  * PROTOTYPE ONLY, DO NOT USE TO *DEFINE* CLASS
  *
  * @author  Yann SECQ (secq@lifl.fr)
  * @version 0.1b
  *
 */

package fr.lifl.magique.platform.classloader;


import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.NoSuchElementException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;

class ResourceLoader  extends URLClassLoader {

  // Used only to fetch resources not to define new class in the JVM !
  // Abusive use of the look-up mechanism implemented in URLClassLoader ... :-(

  // --------------------------------------------------
  private boolean DEBUG = false;
  
  public void trace(String methodName, String message){
    if (DEBUG) {
      System.out.println(this.getClass()+": ["+methodName+"]"+" -> "+message);
    }
  }
  //---------------------------------------------------

  public ResourceLoader(URL[] urls)  {
    super(urls);
    String path = "";
    String classPath = System.getProperty("java.class.path");
    String separator = System.getProperty("path.separator");
    StringTokenizer strToK = new StringTokenizer(classPath,separator);
    while ( strToK.hasMoreTokens()) {
      try {
	path = "file:" + strToK.nextToken();
	if (path.equals("file:.")) {
	  path = "file:"+System.getProperty("user.dir")+"/";	 
	} else if (path.endsWith(".jar")) {
//	  path = "jar:"+path;
	}
	addURL(path); 
	trace("ResourceLoader","adding "+path+" to ResourceLoader's classpath");
      } catch (NoSuchElementException e) {
	path = "";
      }
    }

//    URL[] urles = getURLs();
//      for (int i=0; i< urles.length; i++){
//  	System.err.println("[URL "+i+"] "+urles[i]);
//      }

  }

  public URL getResourceURL(String resourceName) {
    trace("getResourceURL","Trying to find resource URL "+resourceName+" -> "+
			     findResource(resourceName));
    return findResource(resourceName);
  }

  public InputStream getInputStream(String filename) {
    trace("getInputStream","Trying to find resource "+filename+" from "+getResourceAsStream(filename));
// 	System.out.println(">>>>>>> "+filename);
//     if (filename.startsWith("jar:file")){

// 	JarResources tmp = new JarResources(filename);
// 	return new ByteArrayInputStream(tmp.getResource(filename));
//     }
    return getResourceAsStream(filename);
  }

    public void addURL(URL url) {
	super.addURL(url);
    }

  public void addURL(String newResourceURL) {
    // If it's not a jar or a zip archive, just checks that the filename ends with a'/'
//     if (!newResourceURL.endsWith(".jar") && !newResourceURL.endsWith(".zip" )){
//       if (!newResourceURL.endsWith("/")) {
// 	newResourceURL += "/";
//       }
//     } else if (newResourceURL.endsWith(".jar")){
//       newResourceURL += "!/";
//     }
    try {
      addURL(new URL(newResourceURL));
    } catch (MalformedURLException e){
      e.printStackTrace();
    }
    trace("addURL","Adding new URL : "+newResourceURL);
  }

  public static void main(String args[]) {
    URL urls[] =  {};
    ResourceLoader test = new ResourceLoader(urls);
    if (args.length == 2) {
      try {
	test.addURL(args[0]);
	URL e[] = test.getURLs();
	for (int i=0; i<e.length; i++){
	  System.out.println(i+"> "+e[i]);
	}
	System.out.println("Trying to find ressource "+args[1]+" "+test.findResource(args[1]));
	System.out.println("Loaded from "+args[0]+" -> "+args[1]+" : "+test.getInputStream(args[1]));
      } catch (Exception e) {
	e.printStackTrace();
      }
    } else {
      System.out.println("Usage: java ResourceLoader <http://www.lifl.fr/~secq/tmp.jar> <toto>");
    }
  }

}
