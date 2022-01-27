/**
 * ClassUtil.java
 *
 *
 * Created: Mon Feb 19 12:58:29 2001
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.util;

import java.lang.reflect.*;

public class ClassUtil  {
    

    public static final Method getMethod(Class theClass, String methodName, String[] argsClassName) 
	throws NoSuchMethodException, SecurityException {
	
	Method[] tabOfMethods = theClass.getMethods();

	Method aMethod = null;
	boolean found = false;
	int i = 0;
	while (i < tabOfMethods.length && !found) {
	    aMethod = tabOfMethods[i];	    
	    if (methodName.equals(aMethod.getName())) {
		//		System.out.println("aMethod name : "+aMethod.getName()+" "+methodName); 
		Class[] paramTypes = aMethod.getParameterTypes();
		if (argsClassName.length == paramTypes.length) {
		    boolean stop = false;
		    for (int j = 0; j < argsClassName.length && !stop; j++) {
			//System.out.println("argsClass "+j+" : "+argsClassName[j]+" and "+paramTypes[j].getName()); 
			stop = !(argsClassName[j].equals(paramTypes[j].getName()));
		    }
		    found = !stop;
		}
	    }
	    i++;
	}
	if (found) {
	    return aMethod;
	}
	else {
	    throw new java.lang.NoSuchMethodException("\nMAGIQUE : "+methodName+" not found\n");
	}	
    }
    
} // ClassUtil
