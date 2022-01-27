package fr.lifl.rage.demos.anerouge;

import java.math.BigInteger;
 
/**
 * Allow the generation of *big* factorial numbers
 *
 * @version 0.1
 * @author Yann Secq
 */
public final class Factorial implements java.io.Serializable {

    public static final BigInteger of(long n) {
	final BigInteger one = new BigInteger("1");
	BigInteger result  = one;
	BigInteger current = one;

	for (int i=1; i <= n; i++) {
	    result  = result.multiply(current);
	    current = current.add(one);
	}

	return result;
    }

    public static void main(String[] args)
    {
	if (args.length > 0) {
	    int n = (new Integer(args[0])).intValue();
	    
	    System.out.println(n+"! = "+ Factorial.of(n) );
	} else {
	    System.out.println("java Factorial <n>");
	}
    }    
}
