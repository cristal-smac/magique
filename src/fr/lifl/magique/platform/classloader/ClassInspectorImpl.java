
package fr.lifl.magique.platform.classloader;

import java.io.*;
import java.util.*;

import at.dms.classfile.*;

/** This class is the default implementation of the ClassInspector interface.
 * It relies on the API for bytecode manipulation of KOPI (kjc)
 * 
 * @author Yann Secq (secq@lifl.fr)
 * @version 0.1
 */
public class ClassInspectorImpl implements ClassInspector
{

    /**      */
    protected ClassInfo classInfo;

    /**      */
    public ClassInspectorImpl(String filename) 
	throws FileNotFoundException, ClassFileFormatException, IOException
    {
//  	System.err.println("XXXXXXXXXXXXX "+filename+" XXXXXXXXXXXXX");
//  	System.err.flush();
	FileInputStream s = new FileInputStream(filename);
	int size = s.available();
	byte[] data = new byte[100000];
	if (size > data.length) {
	    data = new byte[size];
	} else if (size == 0) {
	    s.close();
	    return;
	}
	s.read(data);
	s.close();
	InputStream is = new ByteArrayInputStream(data, 0, size);
	classInfo = new ClassInfo(new DataInputStream(is), false);
	is.close();
    }

    /**      */
    public ClassInspectorImpl(DataInput input) 
	throws ClassFileFormatException, IOException
    {
	classInfo = new ClassInfo(input, false);
    }

    /**      */
    public String getClassName()
    {
	return replaceSlash(classInfo.getName());
    }

    /**      */
    public boolean hasASuperClassOtherThanObject()
    {
	return (!Object.class.getName().equals(getSuperClassName()));
    }

    /**      */
    public String getSuperClassName()
    {
	return replaceSlash(classInfo.getSuperClass());
    }

    /**      */
    public boolean hasInterfaces()
    {
	if (classInfo.getInterfaces() == null) return false;
	return (classInfo.getInterfaces().length > 0);
    }

    /**      * 
     */
    public String[] getInterfacesNames()
    {
	String[] interfaces = classInfo.getInterfaces();
	if (interfaces != null)
	{
	    String[] replaced = new String[interfaces.length];
	    for (int i=0; i < replaced.length; i++)
		{
		    replaced[i] = replaceSlash(interfaces[i]);
		}
	    return replaced;
	}
	return new String[]{};
    }

    /**      */
    public boolean hasInnerClasses()
    {
	if (classInfo.getInnerClasses() == null) return false;
	return (classInfo.getInnerClasses().length > 0);
    }

    /**      */
    public String[] getInnerClassesNames()
    {	
	InnerClassInfo[] inners = classInfo.getInnerClasses();
	if (inners != null)
	{
	    HashMap innerClasses = new HashMap();
	    for (int i=0; i<inners.length; i++)
		{
		    innerClasses.put(replaceSlash(inners[i].getQualifiedName()), Boolean.TRUE);
		}
	    return toStringArray(innerClasses);
	}
	return new String[]{};
    }

    /**      */
    public boolean hasFields()
    {
	return (classInfo.getFields().length > 0);
    }

    /**      * 
     */
    public String[] getFieldsClassesNames()
    {	
	FieldInfo[] fields = classInfo.getFields();
	if (fields != null)
	{
	    String fieldClass;
	    HashMap fieldsClasses = new HashMap();
	    for (int i=0; i<fields.length; i++)
		{
		    fieldClass = fields[i].getSignature();
		    int idx = fieldClass.lastIndexOf('[') ;
		    if (idx != -1) fieldClass = fieldClass.substring(idx+1);
		    String primitiveTypeConstants = "JISFDCBZ";		
		    if (primitiveTypeConstants.indexOf(fieldClass) != -1) 
			continue;
		    fieldClass = cleanFieldSignature(fieldClass);
		    if (!fieldsClasses.containsKey(fieldClass)) {
			fieldsClasses.put(replaceSlash(fieldClass),
					  Boolean.TRUE);
		    }
		}
	    return toStringArray(fieldsClasses);
	}
	return new String[]{};
    }

    /** Return true if thos class declares at least one method.
     * 
     * @return true if this class declares at least one method
     */
    public boolean hasMethods()
    {
	// Because of the default constructor !
	return (classInfo.getMethods().length > 1);
    }

    /** 
     * This methods parses String which have this template :
     * (Lclassname1;Lclassname2)Lreturntype;
     * 
     * @param signature 
     */
    protected ArrayList parseMethodSignature(String signature)
    {
	ArrayList signatureClasses = new ArrayList();
	int idx = signature.indexOf(')');
	String parameters = signature.substring(1, idx);
	//System.out.println("Parameters : "+parameters);
	StringTokenizer tokenizer = new StringTokenizer(parameters,";");
	while (tokenizer.hasMoreTokens())
	{
	    String currentClass = tokenizer.nextToken();
	    int idxL = currentClass.indexOf('L');
	    if (idxL != -1) {
		//		System.out.println("XXXXXXXXXX    "+ currentClass.substring(idxL+1));	    
		signatureClasses.add(replaceSlash(currentClass.substring(idxL+1))); //7/6/2001 .substring(1, currentClass.length())));
	    }
	}
	String returnType = signature.substring(idx+1, signature.length());
	//System.out.println("Return type: "+returnType);
	int idxL = returnType.indexOf('L');
	if (idxL != -1) {
	    //		System.out.println("   return  "+ replaceSlash(returnType.substring(idxL, returnType.length()-1)));		
		signatureClasses.add(replaceSlash(returnType.substring(idxL+1, returnType.length()-1)));
	}      
	
	return signatureClasses;
    }

    /**      * 
     */
    public String[] getClassesNamesInsideMethods()
    {
	MethodInfo[] methods = classInfo.getMethods();
	HashMap methodsClasses = new HashMap();
	//System.out.println("I've "+methods.length+" methds");
	for (int i=0; i<methods.length; i++)
	{
	    //	    System.out.println("Method "+i+" : "+methods[i].getName());
	    /*if (methods[i].getName().equals("<init>") ||
		methods[i].getName().equals("class$")) continue;*/
	    // Parsing the signature (return type and parameters)
	    //	    System.out.println("Signature  : "+methods[i].getSignature());
	    ArrayList signatureClasses = parseMethodSignature(methods[i].getSignature());
	    Iterator iterator = signatureClasses.iterator();
	    String signature;
	    while (iterator.hasNext())
		{
		    signature = (String) iterator.next();
		    if (!signature.equals("")) {
			//				System.out.println(" [signature]   "+replaceSlash(signature));
			methodsClasses.put(replaceSlash(signature), Boolean.TRUE);

		    }
		}
	    // Parsing the exceptions thrown by the method 
	    //System.out.print("Exceptions : ");
	    String[] exceptions = methods[i].getExceptions();
	    if (exceptions != null)
		{
		    for (int j=0; j<exceptions.length; j++){
			if (exceptions[j] != null) {
			    //			    System.out.print(" EXCEPTION : "+exceptions[j]+" ");
			    
			    methodsClasses.put(replaceSlash(exceptions[j]), Boolean.TRUE);
			}
		    }
		   // System.out.println();
		}
	    CodeInfo codeInfo = methods[i].getCodeInfo();
	    if (codeInfo != null){
		/*System.out.println("Code length : "+codeInfo.getCodeLength()+
				   " | Max locals  "+codeInfo.getMaxLocals()+
				   " | Max stack : "+codeInfo.getMaxStack());*/

		// Parsing the code of the method : any try/catch block ?
		HandlerInfo[] hi = codeInfo.getHandlers();
		if (hi != null)
		    for (int j=0; j<hi.length; j++)
			{
			    if (hi[j].getThrown() != null) {
				//				System.err.println("Catched exceptions :"+hi[j].getThrown());
				
				methodsClasses.put(replaceSlash(hi[j].getThrown()), Boolean.TRUE);
			    }
			}
		//else System.out.println("No try/catch in this method !");
		//System.out.println();
		// Parsing the code of the method : any object creation ?
		Instruction[] inst = codeInfo.getInstructions();
		if (inst != null)
		    for (int j=0; j<inst.length; j++)
			{
			    //inst[j].dump();
			    String instrName = OpcodeNames.getName(inst[j].getOpcode());
			    if (instrName.equals("new")) {
				ClassRefInstruction cref = (ClassRefInstruction) inst[j];
				ClassConstant ccons = cref.getClassConstant();

				//						System.out.println(" [new]  "+replaceSlash(ccons.getName()));
				methodsClasses.put(replaceSlash(ccons.getName()), Boolean.TRUE);
			    } else if (instrName.equals("invokestatic") ||
				       instrName.equals("invokevirtual")
				       ) {
				MethodRefInstruction mref = (MethodRefInstruction) inst[j];
				MethodRefConstant mrc = (MethodRefConstant) mref.getMethodRefConstant();
				//				System.out.println("[invokestatic] "+mrc.getClassName());
				methodsClasses.put(replaceSlash(mrc.getClassName()), Boolean.TRUE);
			    } else if (instrName.equals("getstatic")) {
				FieldRefInstruction fref = (FieldRefInstruction) inst[j];
				FieldRefConstant frc = (FieldRefConstant) fref.getFieldRefConstant();
				//								System.out.println("[getstatic] "+frc.getClassName());
				methodsClasses.put(replaceSlash(frc.getClassName()), Boolean.TRUE);
			    }
			}
	    }
	    //System.out.println();	    
	}	

	return toStringArray(methodsClasses);
	// We should have all that we wanted to know (at least :) !
    }

    /**      */
    protected String[] toStringArray(HashMap listOfStrings)
    {
	String[] result = new String[listOfStrings.size()];
	String current; int i=0;
	for (Iterator it = listOfStrings.keySet().iterator(); it.hasNext();)
	{
	    current = (String) it.next();
	    result[i++] = current;
	}
	return result;
    }

    /**      */
    public String[] getAllReferencedClasses()
    {
	HashMap classNames = new HashMap();
	// Does this class has a superclass ?
	if (hasASuperClassOtherThanObject())
	{
	    //	    	    System.out.println("   xxxxxxxxxxxx      superC "+getSuperClassName());
	    classNames.put(getSuperClassName(), Boolean.TRUE);
	}
	// Does this class implements some interfaces ?
	if (hasInterfaces())
	{
	    String[] interfaces = getInterfacesNames();
	    for (int i=0; i<interfaces.length; i++)
	    {
		//				System.out.println("   xxxxxxxxxxxx      interfaces "+interfaces[i]);
		classNames.put(interfaces[i], Boolean.TRUE);
	    }
	}
	// Does this class declares any inner classes ?
	if (hasInnerClasses())
	{
	    String[] innerClasses = getInnerClassesNames();
	    for (int i=0; i<innerClasses.length; i++)
	    {
	     //  System.out.println("   xxxxxxxxxxxx      inner "+innerClasses[i]);
		classNames.put(innerClasses[i], Boolean.TRUE);
	    }
	}
	// Does this class have any fields ?
	if (hasFields())
	{
	    String[] fields = getFieldsClassesNames();
	    for (int i=0; i<fields.length; i++)
	    {
		//		System.out.println("   xxxxxxxxxxxx      field "+fields[i]);
		classNames.put(fields[i], Boolean.TRUE);
	    }
	}
	// Now let's have a look inside each method !!!
	if (hasMethods())
	{
	    String[] methods = getClassesNamesInsideMethods();
	    for (int i=0; i<methods.length; i++)
	    {
		//				System.out.println("   xxxxxxxxxxxx      method "+methods[i]);
		classNames.put(methods[i], Boolean.TRUE);
	    }
	}
	String[] allClassesNames = new String[classNames.size()];
	Iterator iterator = classNames.keySet().iterator();
	int i=0;
	while (iterator.hasNext())
	{
	    String s = (String) iterator.next();
	    //	    System.out.println("   xxxxxxxxxxxx      "+s);	    
	    allClassesNames[i] = s; // 8/6/2001 cleanFieldSignature(s);
	    //System.err.print("!"+allClassesNames[i]+"!");
	    i++;
	}

//  	System.out.println("\n **********");
//  	for(int z = 0; z < allClassesNames.length; z++) {
//  	    System.out.println(allClassesNames[z]);	    
//  	}
//  	System.out.println("********** \n");	

	return allClassesNames;
    }

    /**      */
    protected String replaceSlash(String s)
    {
	return s.replace('/','.');
    }

    /**      */
    protected String cleanFieldSignature(String fieldSignature)
    {
	// Removes the first and last characters (i.e. "L" ... ";")
	//    	System.err.println(">>>>"+fieldSignature+"<<<<<");
//    	System.err.println("****"+getClassName()+"****");

	int startIndex = fieldSignature.lastIndexOf('[') == -1 ? 0 : fieldSignature.lastIndexOf('[')+1 ;

  	fieldSignature = fieldSignature.substring(startIndex,fieldSignature.length());

	if (fieldSignature.startsWith("L")) {
	    startIndex = 1;
	}
	else {
	    startIndex = 0;
	}
	
	int stopIndex = fieldSignature.lastIndexOf(';') == -1  ? 0 : 1;
	return fieldSignature.substring(startIndex,fieldSignature.length()-stopIndex);
    }

    /**      */
    public void dump(ClassInfo ci)
    {
	System.out.println("Class         : "+ci.getName());
	System.out.println("Superclass    : "+ci.getSuperClass());
	System.out.println("Major version : "+ci.getMajorVersion());
	System.out.println("Minor version : "+ci.getMinorVersion());
	System.out.println("-----------------------------------------------------------");
	InnerClassInfo[] inners = ci.getInnerClasses();
	if (inners != null)
	    for (int i=0; i<inners.length; i++)
		System.out.println("Inner class   : "+inners[i].getQualifiedName());
	else System.out.println("No Inner classes declared !");
	System.out.println("-----------------------------------------------------------");
	String[] interfaces = ci.getInterfaces();
	if (interfaces != null)
	    for (int i=0; i<interfaces.length; i++)
		System.out.println("Interface     : "+interfaces[i]);
	else System.out.println("No interfaces implemented !");
	System.out.println("-----------------------------------------------------------");
	FieldInfo[] fields = ci.getFields();
	if (fields != null)
	    for (int i=0; i<fields.length; i++)
		{
		    System.out.println("Field          : "+fields[i].getName());
		    System.out.println("Signature      : "+fields[i].getSignature());
		    System.out.println("Constant value : "+fields[i].getConstantValue());
		    System.out.println("Is synthetic ? : "+fields[i].isSynthetic());
		}
	else System.out.println("No fields declared !");
	System.out.println("-----------------------------------------------------------");
	MethodInfo[] methods = ci.getMethods();
	if (methods != null)
	    for (int i=0; i<methods.length; i++)
		{
		    System.out.println("Method     : "+methods[i].getName());
		    System.out.println("Signature  : "+methods[i].getSignature());
		    System.out.print("Exceptions : ");
		    String[] exceptions = methods[i].getExceptions();
		    if (exceptions != null)
			{
			    for (int j=0; j<exceptions.length; i++)
				System.out.print(exceptions[j]+" ");
			    System.out.println();
			}
		    CodeInfo codeInfo = methods[i].getCodeInfo();
		    System.out.println("Code length : "+codeInfo.getCodeLength()+
				       " | Max locals  "+codeInfo.getMaxLocals()+
				       " | Max stack : "+codeInfo.getMaxStack());
		    HandlerInfo[] hi = codeInfo.getHandlers();
		    if (hi != null)
			for (int j=0; j<hi.length; j++)
			    System.out.println("Catched exceptions :"+hi[j].getThrown());
		    else System.out.println("No try/catch in this method !");
		    
		    Instruction[] inst = codeInfo.getInstructions();
		    if (inst != null)
			for (int j=0; j<inst.length; j++)
			    {
				System.out.println("Instruction :"+inst[j].getOpcode());
				/*inst[j].dump();
				if (inst[j] instanceof MethodRefInstruction)
				{
				MethodRefInstruction mref = (MethodRefInstruction) inst[j];
				MethodRefConstant mcons = mref.getMethodRefConstant();
				System.out.println(">> Ref "+mcons.getName()+" | Type "+mcons.getType());
				}*/
				if (inst[j] instanceof ClassRefInstruction)
				    {
					ClassRefInstruction cref = (ClassRefInstruction) inst[j];
					ClassConstant ccons = cref.getClassConstant();
					System.out.println(">> Name "+ccons.getName());
				    }
				/*if (inst[j].getOpcode() == 187)
				  System.out.println("Found a new !");
				if (inst[j].getOpcode() == 183)
				{
				System.out.println("Found an invokeSpecial !");
				try {
				System.out.println("Return type "+inst[j].getReturnType());
				} catch (at.dms.util.InternalError e){
				e.printStackTrace();
				}
				}*/
			    }
		    System.out.println("-----------------------------------------------------------");
		}
	else System.out.println("No methods declared !");
    }
   

}