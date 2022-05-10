package fr.lifl.magique.gui.descriptor;

import java.util.Enumeration;
import java.util.Vector;

/**
 * A class that represents a skill added to an agent
 *
 * @author Fabien Niquet
 * @version 1.0 1/4/00
 */
public class SkillDescriptor {
    private final String classe;
    private final String shortclasse;
    private final String constructeur;
    private String classpath;
    private final Vector arguments = new Vector();

    /**
     * Constructs a new SkillDescriptor
     *
     * @param classe      the class of this Skill
     * @param classpath   the classpath for this Skill
     * @param constructor the constructor for this Skill
     */
    public SkillDescriptor(String classe, String classpath, String constructor) {
        this.classe = classe;
        this.shortclasse = classe;

        this.constructeur = constructor;
        int coupure = constructeur.lastIndexOf('(');
        String parametres = constructeur.substring(coupure + 1, constructeur.length() - 1);
        if (classpath != null) {
            this.classpath = classpath.replace(System.getProperty("file.separator").charAt(0), '.');
            this.classpath = (this.classpath).replace('/', '.');
        }

        if (!parametres.equals("")) {
            int next = parametres.indexOf(',');
            while (next != -1) {
                arguments.addElement(new Argument(parametres.substring(0, next)));
                parametres = parametres.substring(next + 1);
                next = parametres.indexOf(',');
            }
            arguments.addElement(new Argument(parametres));
        }
    }

    /**
     * Returns the string value representing the class of this Skill
     */
    public String getClasse() {
        return classe;
    }

    /**
     * Returns the classpath of this Skill
     */
    public String getClassPath() {
        return classpath;
    }

    /**
     * Returns the string value of the constructor
     */
    public String getConstructor() {
        return constructeur;
    }

    public String getShortClasse() {
        return shortclasse;
    }

    /**
     * Returns a Vector of Argument
     */
    public Vector getArgs() {
        return arguments;
    }

    /**
     * Returns a Vector with the value of arguments as a string.
     */

    public Vector getArgValues() {
        Vector res = new Vector();
        for (Enumeration e = arguments.elements(); e.hasMoreElements(); )
            res.addElement(((Argument) e.nextElement()).getValue());
        return res;
    }

    /**
     * Returns a Vector with an instanciation of arguments values.
     */
    public Vector getArguments() {
        Vector res = new Vector();
        for (Enumeration e = arguments.elements(); e.hasMoreElements(); ) {
            Object arg = ((Argument) e.nextElement()).getObjectValue();
            if (arg == null) return null;
            res.addElement(arg);
        }
        return res;
    }

    /**
     * Returns true if this skill is a DefaultSkill. ie: The first argument value is "this"
     */

    public boolean isDefaultSkill() {
        for (Enumeration e = arguments.elements(); e.hasMoreElements(); ) {
            if (((Argument) e.nextElement()).getValue().equals("this"))
                return true;
        }
        return false;
    }

    /**
     * Returns a representation of this Skill as a string
     */

    public String toString() {
        return shortclasse;
    }

    /**
     * Sets the value for the argument at index
     */
    public void setValueAt(String value, int index) {
        ((Argument) arguments.elementAt(index)).setValue(value);
    }

    /**
     * Returns true if the description of this Skill is valide
     */
    public boolean isValide() {
        for (Enumeration e = arguments.elements(); e.hasMoreElements(); ) {
            String value = ((Argument) e.nextElement()).getValue();
            if (value == null || value.equals("")) return false;
        }
        return true;
    }


}

