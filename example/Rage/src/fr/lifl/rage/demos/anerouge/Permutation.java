package fr.lifl.rage.demos.anerouge;

import java.util.*;
import java.math.BigInteger;

/**
 * Allow the generation of the permutations of an array of numbers
 *
 * @version 0.2
 * @author Yann Secq
 */
public final class Permutation implements java.io.Serializable {

    private static int MAX_NUMBER_OF_DIFFERENT_OBJECT_TYPE = 100;
    private ArrayList array;
    private BigInteger numberOfPermutationsWithDoubloons;
    private BigInteger numberOfPermutationsWithoutDoubloons;

    /** This array should be sorted from the smaller to the greater
     */
    public Permutation(ArrayList array)
    {
	this.array = array;
	int[] numberOfOccurences = 
	    new int[MAX_NUMBER_OF_DIFFERENT_OBJECT_TYPE];

	// Initialisation du tableau gérant les occurences
	for (int i=0; i<numberOfOccurences.length; i++)
	    numberOfOccurences[i] = 0;
	
	// Calcul du nombre d'occurences
	for (int i=0; i < array.size(); i++)
	    numberOfOccurences[((Integer)array.get(i)).intValue()]++;

	// Nombre total de permutations (avec doublons)
	numberOfPermutationsWithDoubloons = Factorial.of(array.size());

	numberOfPermutationsWithoutDoubloons = 
	    numberOfPermutationsWithDoubloons;

	// Nombre total de permutations sans doublons
	for (int i=0; i < array.size(); i++)
	    numberOfPermutationsWithoutDoubloons = 
		numberOfPermutationsWithoutDoubloons.divide(Factorial.of(numberOfOccurences[i]));
    }
    
    public BigInteger getNumberOfPermutationsWithoutDoubloons() {
	return this.numberOfPermutationsWithoutDoubloons;
    }

    public BigInteger getNumberOfPermutationsWithDoubloons() {
	return this.numberOfPermutationsWithDoubloons;
    }

    public ArrayList getCurrent() {
	return array;
    }

    public ArrayList nieme(long nieme) {
	ArrayList tmp = new ArrayList();
	ArrayList result = new ArrayList();
	BigInteger fact;
	
	for(int i = 0; i < array.size(); i++) 
	    tmp.add(array.get(i));

	long N = tmp.size(), Nieme = nieme, index;

	while (tmp.size() > 0) {
	    fact   = Factorial.of(N-1);
	    index  = (new BigInteger(""+Nieme)).divide(fact).longValue();
	    Nieme -= fact.multiply(new BigInteger(""+index)).longValue();
	    N--;
	    result.add(tmp.remove((int) index));
	}
	
	return result;
    }

    public static void main(String[] args) {
	ArrayList array = new ArrayList();

	for(int i = 0; i < 5; i ++)
	    array.add(new Integer(i));

	Permutation perm = new Permutation(array);
	
	for(int i = 0; i < 10; i ++) 
	    System.out.println(perm.nieme((long) i));
	    	
    }    
}

