package fr.lifl.magique.gui.descriptor;

import java.lang.reflect.Constructor;

/**
 * A class that represents the initialiser function of an agent
 *
 * @author Nadir Doghmane
 * @author Fabien Niquet
 * @version 1.0 04/05/99
 */
public class Initialiser {

    /**
     * the initialiser function name
     */
    private final String function;

    /**
     * the initialiser function argument
     */
    private String argument;

    /**
     * Constructs a new Initialiser
     *
     * @param func the initialiser function name
     * @param arg  the initialiser function argument
     */
    public Initialiser(String func, String arg) {
        function = func;
        argument = arg;
    }


    /**
     * Returns this initialiser function name
     */
    public String getFunction() {
        return function;
    }

    /**
     * Returns this initialiser function argument
     */
    public String getArgument() {
        return argument;
    }

    /**
     * Sets the argument of this function
     */
    public void setArgument(String arg) {
        argument = arg;
    }

    /**
     * Returns an instanciation of the argument
     */

    public Object getObjectArgument() {
        try {
            int sep1 = function.indexOf('(');
            int sep2 = function.indexOf(')');
            String type = function.substring(sep1 + 1, sep2);
            Class typearg = Class.forName(type);
            Class[] classString = new Class[1];
            classString[0] = Class.forName("java.lang.String");
            Object[] paramstring = new Object[1];
            paramstring[0] = argument;
            Constructor arg_string = typearg.getConstructor(classString);
            if (arg_string == null) {
                System.out.println("No Constructor matching with (String)");
                return null;
            }
            return arg_string.newInstance(paramstring);

        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Returns this initialiser function name without args
     */
    public String getFunctionName() {
        int sep1 = function.indexOf('(');
        int sep2 = function.indexOf(')');
        String initname = function.substring(0, sep1);
        int sep3 = initname.lastIndexOf('.');
        initname = initname.substring(sep3 + 1);
        return initname;
    }


}
