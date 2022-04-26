package fr.lifl.magique.gui.skills;

import fr.lifl.magique.AtomicAgent;
import fr.lifl.magique.skill.DefaultSkill;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;


public class ClassLoaderSkill extends DefaultSkill {
    private URL[] VURLs;

    public ClassLoaderSkill(AtomicAgent agent) {
        super(agent);
        this.checkClassPath();
    }

    public Class loadClass(String name) {
        return this.loadClass(name, "");
    }

    public Class loadClass(String name, String classpath) {
//name=nom de la classe(sans .class) classpath=repertoire de preference
// separateur='.' => a)"fr.lifl.magique"  b)"directory.subdirectory."
        int num;
        num = name.lastIndexOf('.');
        if (num == -1)
            return findClass(name, classpath);
        Class resultat = findClass(name, classpath);
        if (resultat != null)
            return resultat;
        return findClass(name.substring(num + 1), classpath);
    }

    private void checkClassPath() {
        Vector Vurls = new Vector();
        String classpath = System.getProperty("java.class.path", ".");
        char separator = System.getProperty("path.separator").charAt(0);
        String url = classpath;
        if (classpath.endsWith(separator + ""))
            classpath = classpath.substring(0, classpath.length() - 1);
        int sep = classpath.lastIndexOf(separator);
        while (sep != -1) {
            try {
                Vurls.addElement(new URL("file:" + classpath.substring(sep + 1)));
                Vurls.addElement(new URL("file:" + classpath.substring(sep + 1) + System.getProperty("file.separator")));

            } catch (Throwable err) {
            }
            classpath = classpath.substring(0, sep);
            sep = classpath.lastIndexOf(separator);
        }
        try {
            Vurls.addElement(new URL("file:."));
            Vurls.addElement(new URL("file:" + classpath));
        } catch (Throwable rr) {
            System.out.println("LoaderSkill: WARNING");
        }
        VURLs = new URL[Vurls.size()];
        for (int i = 0; i < Vurls.size(); i++)
            VURLs[i] = (URL) Vurls.elementAt(i);
    }

    private Class findClass(String name, String classpath) {
        String dirtmp = classpath;
        String classetmp = name;
        try {           //basic
            URLClassLoader load = new URLClassLoader(VURLs);
            Class c = load.loadClass(name);
            return c;
        } catch (Throwable err) {
        }
        int sepa = classetmp.lastIndexOf('.');
        if (sepa != -1) { //pour les jars
            try {
                URLClassLoader load = new URLClassLoader(VURLs);
                Class c = load.loadClass(name.substring(sepa + 1));
                return c;
            } catch (Throwable err) {
            }
        }
        int num = 0;
        while (num != -1) {  //pour les packages
            dirtmp = dirtmp.substring(0, dirtmp.length() - 1);
            num = dirtmp.lastIndexOf('.');
            if (num != -1) {
                classetmp = dirtmp.substring(num + 1) + "." + classetmp;
                //System.out.println("on regarde la classe "+classetmp);
                dirtmp = dirtmp.substring(0, num + 1);
                try {
                    URLClassLoader load = new URLClassLoader(VURLs);
                    Class c = load.loadClass(classetmp);
                    return c;
                } catch (Throwable err) {
                }
            }
        }
        return null;
    }
}


      
