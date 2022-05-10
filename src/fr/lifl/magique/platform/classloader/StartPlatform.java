/**
 * StartPlatform.java
 * <p>
 * <p>
 * Created: Wed Feb 16 09:50:12 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */

package fr.lifl.magique.platform.classloader;

public class StartPlatform {

    private final int port;

    public StartPlatform(int port) {
        this.port = port;
    }

    public final Object start() {
        BytecodeClassLoader myLoader = new BytecodeClassLoader();
        Object p = null;
        try {

            Class c = myLoader.loadClass("fr.lifl.magique.platform.Platform");

            Class[] argsClass = new Class[]{Integer.class};
            p = c.getConstructor(argsClass).newInstance(Integer.valueOf(port));

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            System.out.println("Instantiation has failed -> NoClassDefFoundError.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    public final Object start(String platformClassName) {
        BytecodeClassLoader myLoader = new BytecodeClassLoader();
        Object p = null;
        try {

            Class c = myLoader.loadClass(platformClassName);

            Class[] argsClass = new Class[]{Integer.class};
            p = c.getConstructor(argsClass).newInstance(new Integer(port));

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            System.out.println("Instantiation has failed -> NoClassDefFoundError.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

} // StartPlatform
