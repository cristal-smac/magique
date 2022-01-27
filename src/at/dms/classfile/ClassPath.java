/*
 * Copyright (C) 1990-99 DMS Decision Management Systems Ges.m.b.H.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package at.dms.classfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the conceptual directory structure for .class files.
 */
public class ClassPath
{
    /**
     * Special constant that identifies classes that don't exist, to avoid
     * repeatedly looking on the file system for the same nonexisting class
     * file. Also known as 'negative caching'.
     */
    private final static ClassDescription CLASSFILE_NOT_FOUND =
        new ClassDescription() {
                public Data getData() {
                    throw new RuntimeException("CLASSFILE_NOT_FOUND");
                }
            };

    /**
     * The static system-wide instance of ClassPath.
     */
    private static ClassPath instance = null;

    /**
     * A mapping from name to ClassDescription for all loaded class files,
     * including class files that cannot be found.
     * (They are mapped to CLASSFILE_NOT_FOUND)
     */
    private static Map allLoadedClassFiles = new HashMap();

    /**
     * List of directories in class path.
     */
    private ClassDirectory[] dirs;

    /**
     * Constructs the single, system-wide instance of ClassPath.
     */
    private ClassPath(String classPath, boolean appendSystemPath)
    {
        if (instance != null) {
            throw new IllegalStateException("ClassPath already initialized.");
        }

        if (appendSystemPath) {
            String version = System.getProperty("java.version");
            if (System.getProperty("sun.boot.class.path") != null) {
                classPath += File.pathSeparator
                    + System.getProperty("sun.boot.class.path");
            }
            else if (version.startsWith("1.2") || version.startsWith("1.3")) {
                String home = System.getProperty("java.home");
                classPath += File.pathSeparator + home + File.separatorChar +
                    "lib" + File.separatorChar + "rt.jar";
            }
        }
        dirs = parseClassPath(classPath);
    }

    /**
     *
     */
    public static ClassDirectory[] parseClassPath(String classPath)
    {
        int   start, length;

        List dirs = new ArrayList();
        length = classPath.length();
        start  = 0;
        while (start < length) {
            int end = classPath.indexOf(File.pathSeparatorChar, start);

            if (end == -1) {
                end = length;
            }

            try {
                File file = new File(classPath.substring(start, end));

                if (file.isDirectory()) {
                    dirs.add(new DirClassDirectory(file));
                }
                else if (file.isFile()) {
                    // check if file is zipped (.zip or .jar)
                    if (file.getName().endsWith(".zip")
                        || file.getName().endsWith(".jar")) {

                        try {
                            dirs.add(new ZipClassDirectory(file));
                        }
                        catch (IOException e) {
                            // it was not a zip file, or some other io problem:
                            // ignore the classpath entry.
                        }
                    }
                }
            }
            catch (SecurityException e) {
                // unreadable file, ignore it
            }

            start = end + 1;
        }

        ClassDirectory[] cd = new ClassDirectory[dirs.size()];
        return (ClassDirectory[])dirs.toArray(cd);
    }

    /**
     * Initialization from a string that represents the class path.
     *
     * @param path
     * Whether to append the system path. For normal operation, this is
     * desired, but not when compiling an alternative implementation of the
     * system libraries.
     */
    public static void init(String path, boolean appendSystemPath)
    {
        instance = new ClassPath(path, appendSystemPath);
    }

    /**
     * Returns whether the named class is on this path.
     *
     * @param name
     * The name of the class file.
     *
     * @return
     * Whether the named class is on this path.
     */
    public static boolean hasClassFile(String name)
    {
        Object o = allLoadedClassFiles.get(name);

        if (o != null && o != CLASSFILE_NOT_FOUND) {
            return true;
        }
        else {
            ClassDescription file = instance.getClassFile(name);

            allLoadedClassFiles.put(name,
                                    file != null ? file : CLASSFILE_NOT_FOUND);

            return file != null;
        }
    }

    /**
     * Returns a class-info object that contains the named class.
     *
     * @param name
     * The name of the class file.
     *
     * @return
     * A class-info object that contains the named class.
     */
    public static ClassInfo getClassInfo(String name, boolean interfaceOnly)
    {
        if (!hasClassFile(name)) {
            return null;
        }

        ClassDescription clazz =
            (ClassDescription)allLoadedClassFiles.get(name);

        allLoadedClassFiles.remove(name);

        Data data = clazz.getData();
        try {
            ClassInfo ci = new ClassInfo(data.getDataInput(), interfaceOnly);
            data.release();
            return ci;
        }
        catch (ClassFileFormatException e) {
            data.release();
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            data.release();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a class file but dont read it.
     */
    private ClassDescription getClassFile(String name)
    {
        for (int i = 0; i < dirs.length; i++) {
            ClassDescription file = dirs[i].getClassFile(name);

            if (file != null) {
                return file;
            }
        }

        return null;
    }

}

