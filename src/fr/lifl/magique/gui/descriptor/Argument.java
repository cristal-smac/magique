package fr.lifl.magique.gui.descriptor;

import java.lang.reflect.Constructor;

/**
 * A class that represents an argument of an initialiser function
 *
 * @author Fabien Niquet
 * @version 1.0 1/4/00
 */
public class Argument {
    /**
     * the type of argument
     */
    private final String type;
    /**
     * the String value of argument
     */

    private String value;

    /**
     * Constructs a new Argument
     *
     * @param type the class of the argument
     */
    public Argument(String type) {
        this.type = type;
        this.value = "";
    }

    /**
     * Returns the class of this argument
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the string value of this argument
     */

    public String getValue() {
        return value;
    }

    /**
     * Sets the value for this argument
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the instanciate value for this argument
     */
    public Object getObjectValue() {
        try {
            Class typearg = Class.forName(type);
            if (value.equals("this"))
                return value;
            else {

                Class[] string = new Class[1];
                string[0] = Class.forName("java.lang.String");
                Object[] paramstring = new Object[1];
                paramstring[0] = value;
                Constructor arg_string = typearg.getConstructor(string);
                if (arg_string == null) {
                    System.out.println("No Constructor matching with (String)");
                    return null;
                }
                return arg_string.newInstance(paramstring);
            }
        } catch (Throwable t) {
            return null;
        }
    }


}
