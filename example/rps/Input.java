
/**
 * Une classe utilitaire pour la saisie de chaînes ou d'entiers sur l'entrée standard.
 *
 *
 * Created: Mon Oct 07 15:53:21 2002
 *
 * @author <a href="mailto:routier@lifl.fr">Jean-Christophe Routier</a>
 * @version
 */
public class Input {

    /** permet la saisie d'une chaîne sur l'entrée standard
     * @return la chaîne saisie
     */
    public static String readString() throws java.io.IOException {	
	return new java.io.BufferedReader(new java.io.InputStreamReader(System.in)).readLine();
    }

    /** permet la saisie d'un entier sur l'entrée standard
     * @return l'entier saisi
     */
    public static int readInt() throws java.io.IOException {
	int n;
	try {
	    n=Integer.parseInt(readString());
	} catch(NumberFormatException e) {
	    throw new java.io.IOException();
	}
	return n;
    }
    /** permet la saisie d'un char sur l'entrée standard
     * @return le char saisi (les uatres caractères sont ignorés et "perdus")
     */
    public static char readChar() throws java.io.IOException {
	return (char) new java.io.BufferedReader(new java.io.InputStreamReader(System.in)).read();
    }

}// Input
