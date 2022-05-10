/**
 * Start.java
 * <p>
 * <p>
 * Created: Wed Feb 16 09:48:54 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */

package fr.lifl.magique;

import fr.lifl.magique.platform.Platform;
import fr.lifl.magique.platform.classloader.BytecodeClassLoader;
import fr.lifl.magique.platform.classloader.StartPlatform;

import java.lang.reflect.Method;
import java.util.StringTokenizer;

public class Start {

    private static final String EXCLUDE_PROPERTY = "excludePrefix";

    public static final Object start(int port, String platformClassName) {
        return new StartPlatform(port).start(platformClassName);
    }

    public static final Object start(String platformClassName) {
        return new StartPlatform(Platform.DEFAULT_PORT).start(platformClassName);
    }

    public static final Object start(int port) {
        return new StartPlatform(port).start();
    }

    public static final Object start() {
        return new StartPlatform(Platform.DEFAULT_PORT).start();
    }

    /**
     * invokes go(realMainClassName,Platform.DEFAULT_PORT,args);
     */
    public static Object go(String realMainClassName, String[] args) {
        return go(realMainClassName, Platform.DEFAULT_PORT, args);
    }

    /**
     * invokes go(realMainClassName,Platform.DEFAULT_PORT,new String[]{});
     */
    public static Object go(String realMainClassName) {
        return go(realMainClassName, Platform.DEFAULT_PORT, new String[]{});
    }

    /**
     * create a platform and launch a realMain file
     *
     * @param realMainClassName the name of theRealMain class
     * @param port              port of the platform
     * @param args              the args for theRealMain
     * @return the object representing the instance of AbstractMagiqueMain that has
     * been created
     * @see fr.lifl.magique.AbstractMagiqueMain
     */
    public static Object go(String realMainClassName, int port, String[] args) {
        Object theRealMainInstance = new Object();

        try {
            Object platform;
            platform = Start.start(new Integer(port).intValue());

            BytecodeClassLoader myLoader = (BytecodeClassLoader) platform.getClass().getClassLoader();
            addPackageToExclude(myLoader);

            Class c = myLoader.loadClass(realMainClassName);

            Class superClass = c.getSuperclass();
            boolean extendOk = false;
            while (superClass != null && !extendOk) {
                extendOk = (superClass.getName()).equals("fr.lifl.magique.AbstractMagiqueMain");
                superClass = superClass.getSuperclass();
            }
            if (!extendOk) {
                throw new ClassNotFoundException(
                        "\nMAGIQUE : " + realMainClassName + " does not extend fr.lifl.magique.AbstractMagiqueMain\n");
            }

            theRealMainInstance = c.getConstructor().newInstance();

            Method method = fr.lifl.magique.util.ClassUtil.getMethod(c, "setPlatform",
                    new String[]{"fr.lifl.magique.platform.Platform"});
            method.invoke(theRealMainInstance, platform);

            method = fr.lifl.magique.util.ClassUtil.getMethod(c, "theRealMain", new String[]{"[Ljava.lang.String;"});

            method.invoke(theRealMainInstance, new Object[]{args});

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
        return theRealMainInstance;
    }

    private static void addPackageToExclude(BytecodeClassLoader myLoader) {
        String packages = System.getProperty(EXCLUDE_PROPERTY);
        System.err.println(packages);

        if (packages != null) {
            StringTokenizer st = new StringTokenizer(packages, ",");
            while (st.hasMoreTokens()) {
                myLoader.addPathToExclude(st.nextToken());
            }
        }
    }

    // **********

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("usage : \n" + "     java fr.lifl.magique.Start theRealMainClass [port] [args*] \n"
                    + " theRealMainClass : class that inherits AbstractMagiqueMain\n"
                    + "                    the 'theRealMain' method describes what the program  does\n"
                    + " port : the port of the rmi server of the platform ("
                    + fr.lifl.magique.platform.Platform.PLATFORMMAGIQUEAGENTNAME + " by default\n"
                    + " args* : the args (as Strings) for the 'theRealMain' \n"
                    + "  NOTE : confusion may occur with port if first 'args' is an integer, in this case, port MUST be declared even if it is 4444  \n\n"
                    + " creates the platform at port 'port' and invokes 'theRealMain(args)'");
            System.exit(0);
        }

        if (args.length == 1) {
            fr.lifl.magique.Start.go(args[0]);
        } else {
            int port;
            int delta = 2;
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                port = -1; // args[1] is not a number and then not a port number
                delta = 1;
            }
            String[] theTrueArgs = new String[args.length - delta];
            for (int i = 0; i < args.length - delta; i++) {
                theTrueArgs[i] = args[i + delta];
            }
            if (port == -1) {
                fr.lifl.magique.Start.go(args[0], theTrueArgs);
            } else {
                fr.lifl.magique.Start.go(args[0], port, theTrueArgs);
            }
        }

    }

} // Start
