/**
 * This class is used to keep references of all resources available
 * It keeps a list of URL, and tries to find resources  in the classpath.
 * <p>
 * PROTOTYPE ONLY, DO NOT USE TO *DEFINE* CLASS
 *
 * @author Yann SECQ (secq@lifl.fr)
 * @version 0.1b
 */

package fr.lifl.magique.platform.classloader;


import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

class ResourceLoader extends URLClassLoader {

    // Used only to fetch resources not to define new class in the JVM !
    // Abusive use of the look-up mechanism implemented in URLClassLoader ... :-(

    // --------------------------------------------------
    private final boolean DEBUG = false;

    public ResourceLoader(URL[] urls) {
        super(urls);
        String path = "";
        String classPath = System.getProperty("java.class.path");
        String separator = System.getProperty("path.separator");
        StringTokenizer strToK = new StringTokenizer(classPath, separator);
        while (strToK.hasMoreTokens()) {
            try {
                path = "file:" + strToK.nextToken();
                if (path.equals("file:.")) {
                    path = "file:" + System.getProperty("user.dir") + "/";
                } else if (path.endsWith(".jar")) {
                }
                addURL(path);
                trace("ResourceLoader", "adding " + path + " to ResourceLoader's classpath");
            } catch (NoSuchElementException e) {
                path = "";
            }
        }
    }
    //---------------------------------------------------

    public static void main(String[] args) {
        URL[] urls = {};
        ResourceLoader test = new ResourceLoader(urls);
        if (args.length == 2) {
            try {
                test.addURL(args[0]);
                URL[] e = test.getURLs();
                for (int i = 0; i < e.length; i++) {
                    System.out.println(i + "> " + e[i]);
                }
                System.out.println("Trying to find ressource " + args[1] + " " + test.findResource(args[1]));
                System.out.println("Loaded from " + args[0] + " -> " + args[1] + " : " + test.getInputStream(args[1]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Usage: java ResourceLoader <http://www.lifl.fr/~secq/tmp.jar> <toto>");
        }
    }

    public void trace(String methodName, String message) {
        if (DEBUG) {
            System.out.println(this.getClass() + ": [" + methodName + "]" + " -> " + message);
        }
    }

    public URL getResourceURL(String resourceName) {
        trace("getResourceURL", "Trying to find resource URL " + resourceName + " -> " +
                findResource(resourceName));
        return findResource(resourceName);
    }

    public InputStream getInputStream(String filename) {
        trace("getInputStream", "Trying to find resource " + filename + " from " + getResourceAsStream(filename));
        return getResourceAsStream(filename);
    }

    public void addURL(URL url) {
        super.addURL(url);
    }

    public void addURL(String newResourceURL) {
        // If it's not a jar or a zip archive, just checks that the filename ends with a'/'
        try {
            addURL(new URL(newResourceURL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        trace("addURL", "Adding new URL : " + newResourceURL);
    }

}
