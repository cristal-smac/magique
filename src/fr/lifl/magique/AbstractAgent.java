
/**
 * AbstractAgent.java
 *
 *
 * Created: Fri Jan 28 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */

package fr.lifl.magique;

import java.util.*;
import java.io.*;
import java.lang.reflect.*;

import fr.lifl.magique.util.*;

/** Abstract base for Agent class.
 *
 * The <name> of a agent is based on a short name given at
 * creation. To this short name, the IP address (or hostname) of the
 * host where the agent runs is added, as well as the port number of
 * the platform. Therefore, name is of the form
 * <tt>shortName@hostname:port</tt> (e.g. anAgent@here.domain.country:4444).
 *
 * An agent contains  <ul>
 * <li> an <em>agenda</em> to list its acquaintances.</li>
 * <li> a message list <em>toDo</em> which contains the received messages </li>
 * <li> a <em>question table</em> to manage the asynchronous question </li>
 * <li> a <em>listener</em> who listen to incoming message sand store them in <em>toDo</em> list
 * </ul>
 *
 * This class contain some basic methods for sending messages, it allows to lighten Agent class.
 *
 * @see fr.lifl.magique.Agent
 * @see fr.lifl.magique.Message */


public abstract class AbstractAgent { // implements Runnable {
    
    /** give correspondances names of known agents, informations about
        these agents can be store in that agenda */
    private Agenda agenda = new Agenda();
    /** my name...  */
    private String name ;
    /** the FIFO list of messages to be treated */
    protected MessageList toDo = new MessageList();
    /** my <em>listener</em> : manage requests sent to me */
    private Listener listener;
    /** in order to manage the answer to a question I have
        asked, when it is known (it arrives from other comObject) */
    protected QuestionTable questionTable;


    public AbstractAgent() {
	listener = new Listener(this);
	this.questionTable = new QuestionTable();
	listener.start();
    }

    public AbstractAgent(String name) {
	setName(name);
	listener = new Listener(this);
	this.questionTable = new QuestionTable();
	listener.start();
    }

    // access to the private members
    /** returns my name 
     * @return my name  */
    public String getName() { return(name); }
    /** returns the list of messages I still have to execute
     * @return the list of messages I still have to execute */
    public MessageList getToDo() { return(toDo); }
    /** returns my agenda
     * @return my agenda */
    public Agenda getAgenda() { return(agenda); }
    /**  returns my QuestionTable
     * @return my QuestionTable */
    public QuestionTable getQuestionTable() { return questionTable; }

    /**  returns my listener
     * @return my listener */
    public Listener getListener() { return listener; }

    /**  sets my QuestionTable
     */
    public void setQuestionTable(QuestionTable questionTable) { 
	this.questionTable = questionTable; }



    /** sets the  name
     * @param name my name
     */
    public void setName(String name) { this.name = name; }

    /** add a new ref to my agenda 
     *
     * @param theOther name of other side comObject
     *
     * @return the name of the agent i connect to*/
    public String addAgenda (String theOther) {
	getAgenda().put(theOther,"");    
	verbose(2,getName()+": "+theOther+" known ");	
	return theOther;
    }

    /** send a message to <em>to</em> through the
     * appropriate <em>canalComm</em>.
     *
     * @param to name of recipient
     * @param msg message to be sent
     * @see java.io.Serializable 
     */
    public abstract void send(String to, Message msg);

//      /** read information coming from <em>from</em> 
//       *
//       * @return a message
//       */
//      public Message get() {
//  	return getToDo().firstMessage();
//      }  

    /** is <em>name</em> known ? (from my agenda)
     * @param name the name to look for
     * @return <em>true</em> if <em>name</em>is in my agenda
     */
    private boolean knows(String name) {
	return(agenda.containsKey(name));
    }

    /** it is used to see trace messages. level 0 and 1 are kept for
        the user */
    protected static int verboseLevel = 0;
    /** gets the verboseLevel 
     * @result the current verbose level 
     */
    public static int getVerboseLevel() { return verboseLevel; }
    /** sets the verbose level
     * @param level the value to set
     */
    public static void setVerboseLevel(int level) { verboseLevel = level; }
    /** display a message (standard out) if current verboseLevel is at
     * least <em>level</em>
     *
     * @param level the verbose level  from which the message will be displayed
     * @param msg the message to display */
    public static void verbose(int level,String msg) {
	if (level <= verboseLevel)
	    System.out.println(msg);
    }



    //
    // requests = order and question 
    //
    /** create a request with no args and with no required answer
     * @param text the text of request
     * @return the created request
     */
    public Request createOrder(String text) {
	return(new Request(getName(),false,text));
    }
    /** create a request with args but no answer
     * @param text request text
     * @param params parameters array
     * @return created request
     */
    public Request createOrder(String text, Object[] params) {
	return(new Request(getName(),false,text,params));
    }

    /** create a request with 1 arg but no answer
     * @param text request text
     * @param param1 parameter 
     * @return created request
     */
    public Request createOrder(String text, Object param1) {
	Object[] params = {param1};
	return(new Request(getName(),false,text,params));
    }
    /** create a request with 2 args but no answer
     * @param text request text
     * @param param1 parameter 1
     * @param param2 parameter 2
     * @return created request
     */
    public Request createOrder(String text, Object param1, Object param2) {
	Object[] params = {param1, param2};
	return(new Request(getName(),false,text,params));
    }
    /** create a request with 3 args but no answer
     * @param text request text
     * @param param1 parameter 1
     * @param param2 parameter 2
     * @param param3 parameter 3
     * @return created request
     */
    public Request createOrder(String text, Object param1, Object param2,
			       Object param3) {
	Object[] params = {param1, param2, param3};
	return(new Request(getName(),false,text,params));
    }
    /** create a request with 4 args but no answer
     * @param text request text
     * @param param1 parameter 1
     * @param param2 parameter 2
     * @param param3 parameter 3
     * @param param4 parameter 4
     * @return created request
     */
    public Request createOrder(String text, Object param1, Object param2, 
			       Object param3, Object param4) {
	Object[] params = {param1, param2, param3, param4};
	return(new Request(getName(),false,text,params));
    }


    /** create a request with no arg but answer is required
     * @param text text of request
     * @return created request
     */
    public Request createQuestion(String text) {
	Request question = new Request(getName(),true,text);
	questionTable.newQuestion(question);
	return(question);    
    }

    /** create a request with args and required answer
     * @param text text of request
     * @param params array of args
     * @return created request
     */
    public Request createQuestion(String text, Object[] params) {
	Request question = new Request(getName(),true,text,params);
	questionTable.newQuestion(question);
	return(question);    
    }

    /** create a request with 1 arg and required answer
     * @param text text of request
     * @param param1 arg 1
     * @return created request
     */
    public Request createQuestion(String text, Object param1) {
	Object[] params = { param1 } ;
	Request question = new Request(getName(),true,text,params);
	questionTable.newQuestion(question);
	return(question);    
    }
    /** create a request with 2 args and required answer
     * @param text text of request
     * @param param1 arg 1
     * @param param2 arg 2
     * @return created request
     */
    public Request createQuestion(String text, Object param1, Object param2) {
	Object[] params = { param1 , param2 } ;
	Request question = new Request(getName(),true,text,params);
	questionTable.newQuestion(question);
	return(question);    
    }
    /** create a request with 3 args and required answer
     * @param text text of request
     * @param param1 arg 1
     * @param param2 arg 2
     * @param param3 arg 3
     * @return created request
     */
    public Request createQuestion(String text, Object param1, Object param2,
				  Object param3) {
	Object[] params = { param1 , param2 , param3} ;
	Request question = new Request(getName(),true,text,params);
	questionTable.newQuestion(question);
	return(question);    
    }
   /** create a request with 4 args and required answer
     * @param text text of request
     * @param param1 arg 1
     * @param param2 arg 2
     * @param param3 arg 3
     * @param param4 arg 4
     * @return created request
     */
    public Request createQuestion(String text, Object param1, Object param2,
				  Object param3, Object param4) {
	Object[] params = { param1 , param2 , param3 , param4 };
	Request question = new Request(getName(),true,text,params);
	questionTable.newQuestion(question);
	return(question);    
    }


    /** create a request with no arg but answer is required
     * @param text text of request
     * @param isConcurrent if <em>true</em> then question is concurrent...
     * @return created request
     */
    public Request createQuestion(String text, boolean isConcurrent) {
	Request question = new Request(getName(),true,text);
	if (isConcurrent) question.setConcurrent();
	getQuestionTable().newQuestion(question);
	return(question);    
    }
    /** create a request with args and required answer
     * @param text text of request
     * @param params args (vector)
     * @param isConcurrent if <em>true</em> then question is concurrent...
     * @return created request
     */
    public Request createQuestion(String text, Object[] params, 
				  boolean isConcurrent) {
	Request question = new Request(getName(),true,text,params);
	if (isConcurrent) question.setConcurrent();
	getQuestionTable().newQuestion(question);
	return(question);    
    }


      
    //
    // requests process
    //
    /** invoke request : text is a known method name. If it is a question,
     * answer is replied.
     * @param request the request to interprete
     * @return the request with its <em>answer</em> field updated
     * with invocation result  (<em>null</em> if no answer is required)
     */
    protected Request interprete(Request request) {	
	try {
	    Object[] params = request.getParams();
	    Class[] paramClasses = new Class[params.length]; 
	    verbose(5,getName()+": "+"i interprete "+request.getName()+":"+request.getText());	    
	    for(int i = 0;i<params.length;i++)
		paramClasses[i] = params[i].getClass();
	    // get and create method corresponding to request text
	    Method method = (this.getClass()).getMethod(request.getText(),paramClasses);
	    // method is invoked and computed value is catched and stored
	    // as the answer to the request
	    Object answer = method.invoke(this,params);
	    request.setAnswer(answer,getName());
	}
	catch (InvocationTargetException e) { 
	    System.out.println(" 1 "+getName()+" refuses "+ 
			       request.getName() + " "+ request.getText()+" exception levee"); 
	    Object[] parameters = request.getParams();
	    for (int i = 0; i< parameters.length; i++) {
		System.out.println("                params "+i+" : "+parameters[i]);
	    }
	    System.out.println("***************************************************************************");
	    (e.getTargetException()).printStackTrace();
	    System.out.println("***************************************************************************");
	}
	catch (IllegalAccessException e) {
	    System.out.println(e.getMessage()+" 2 "+getName()+" refuses "+
			       request.getName() + " "+request.getText()); 
	    e.printStackTrace();
	}
	catch (NoSuchMethodException e) {
	    System.out.println(e.getMessage()+" 3 "+getName()+" refuses "+
			       request.getName() + " "+request.getText()); 
	    e.printStackTrace();
	}	
	return(request);
    }    

    /** process a received <em>Request</em> object, it can be 
     * <UL> 
     * <LI>an answer to one of my request, back</LI>
     * <LI>an answer to someone else back on the way to its creator</LI>
     * <LI>a request to treat</LI>
     * </UL>
     * @param request request to handle
     */
    public void processRequest(Request request) {      
	// c'est une reponse ?
	if (request.isAnAnswer()) {
	    if (request.isAnswerToMe(getName())) //a une de mes requetes ?
		processAnswer(request);        
	    else { // reponse en transit (a un autre) : je fais suivre
		request.removeLastFromPath();
		String nextAgent = request.lastSender();
		send(nextAgent,request);
	    }
	}
	else
	    perform(request);
    }

    /** process an answer (to me)
     * @param request the request (answer is included)
     */
    protected void processAnswer(Request question) {
	questionTable.setAnAnswer(question.getName(),
				  question.getAnswer(),
				  question.getAnswerer(),
				  question.getPathLength());
    }    

    /** treat a request
     *
     * @param request the request to be treated 
     * @return no return value
     */    
    public abstract void perform(Request request);

    /** treat a concurrent request
     *
     * @param request the request to be treated 
     * @return no return value */
    public abstract void concurrentPerform(Request request);


    /** delegate <em>to</em> to an order to be treated
     * 
     * @param to recipient of order
     * @param request the order to be treated */
    public void perform(String to, Request order) {	
	send(to,order);
    }
    /** same as <em>perform(Request)</em> but Request is created from text 
     *
     * @param text text of the request
     */
    public void perform(String text) {
	perform(createOrder(text));
    }
   /** same as <em>perform(Request)</em> but Request is created from text and params
     *
     * @param text text of the request
     * @param pamams parameters of request
     */
    public void perform(String text, Object params[]) {
	perform(createOrder(text,params));
    }
    /** same as <em>perform(to,Request)</em> but Request is created from text 
     *
     * @param to recipient of request
     * @param text text of the request
     */
    public void perform(String to,String text) {
	perform(to,createOrder(text));
    }
   /** same as <em>perform(to, Request)</em> but Request is created from
     *text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param pamams parameters of request */
    public void perform(String to, String text, Object params[]) {
	perform(to,createOrder(text,params));
    }
    /** Returns <em>true</em> iff answer to <em>question</em> has been received
     * @param question the question to test
     * @return <em>true</em> iff answer to <em>question</em> has been received
     */
    public boolean isAnswerReceived(Request question) {
	return getQuestionTable().isAnswerReceived(question.getName());
    }
    /** Removes <em>question</em> from <em>questionTable</em>
     * @param question the question to remove
     */
    public void forgetQuestion(Request question) {
	getQuestionTable().remove(question.getName());
    }
    
    /** gets the value of the answer to a question : must wait until
     * this answer comes back, so it is blocking. You can get the
     * value only once.
     *    
     * @param question question to get the answer to 
     * @return the answer (an <em>Object</em>) */
    public Object returnValue(Request question) {
	return returnAnswer(question).getValue();
    }

    /** gets the answer to a question : must wait until
     * this answer comes back, so it is blocking. You can get the
     * answer only once.
     *    
     * @param question question to get the answer to 
     * @return the answer (an <em>Object</em>) */
    public Answer returnAnswer(Request question) {
	Answer answer = getQuestionTable().selectTheAnswer(question.getName());
	forgetQuestion(question);
	return answer;
    }

    /** gets the answerer to a question
     * @param question question to get the answer to
     * @return the answer (an <em>Object</em>)
     */
    public String getAnswerer(Request question) {
	return getQuestionTable().getTheAnswer(question.getName()).getAnswerer();
    }

    /** ask  to treat <em>question</em> and does not wait for
     * the result
     *
     * @param question the question
     * @return  the asked question
     */
    public Request ask(Request question) {	
	perform(question);
	return question;
    }
    /** ask to <em>to</em> to treats <em>question</em> and does not wait for
     * the result
     *
     * @param to recipient of question
     * @param question the question
     * @return the asked question
     */
    public Request ask(String to, Request question) {	
	perform(to,question);
	return question;
    }
    /** same as <em>ask(Request)</em> but Request is created from text 
     *
     * @param text text of the request
     * @return the asked question
     * @see #ask(Request)
     */
    public Request ask(String text) {
       return ask(createQuestion(text));
    }
   /** same as <em>ask(Request)</em> but Request is created from text and params
     *
     * @param text text of the request
     * @param params parameters of request
     * @return the asked question
     * @see #ask(Request)
     */
    public Request ask(String text, Object params[]) {
        return ask(createQuestion(text,params));
    }
    /** same as <em>ask(to,Request)</em> but Request is created from text 
     *
     * @param to recipient of request
     * @param text text of the request
     * @return the asked question
     * @see #ask(String,Request)
     */
    public Request ask(String to,String text) {
	return ask(to,createQuestion(text));
    }
   /** same as <em>ask(to, Request)</em> but Request is created from
     * text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param pamams parameters of request 
     * @return the asked question
     * @see #ask(String,Request)
     */
    public Request ask(String to, String text, Object params[]) {
	return ask(to,createQuestion(text,params));
    }
    /** treats a request and waits for the result and returns it
     * @param question the question
     * @return the answer (an <em>Object</em>)
     */
    public Object askNow(Request question) {	
	return returnValue(ask(question));
    }
    /** sends a request to <em>to</em> and waits for the result and returns it
     *
     * @param to recipient of order
     * @param question the question
     * @return the answer (an <em>Object</em>)
     */
    public Object askNow(String to,Request question) {	
	return returnValue(ask(to,question));
    }
    /** same as <em>askNow(Request)</em> but Request is created from text 
     *
     * @param text text of the request
     * @return the answer (an <em>Object</em>)
     * @see #askNow(Request)
     */
    public Object askNow(String text) {
       return askNow(createQuestion(text));
    }
   /** same as <em>askNow(Request)</em> but Request is created from
     *text and params
     *
     * @param text text of the request
     * @param pamams parameters of request 
     * @return the answer (an <em>Object</em>)
     * @see #askNow(Request)
     */
    public Object askNow(String text, Object params[]) {
        return askNow(createQuestion(text,params));
    }
    /** same as <em>askNow(to,Request)</em> but Request is created from text 
     *
     * @param to recipient of request
     * @param text text of the request
     * @return the answer (an <em>Object</em>)
     * @see #askNow(String,Request)
     */
    public Object askNow(String to,String text) {
	return askNow(to,createQuestion(text));
    }
   /** same as <em>askNow(to, Request)</em> but Request is created from
     * text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param pamams parameters of request 
     * @return the answer (an <em>Object</em>)
     * @see #askNow(String,Request)
     */
    public Object askNow(String to, String text, Object params[]) {
	return askNow(to,createQuestion(text,params));
    }

    /** stops the listener : no more requests are treated 
     * @see fr.lifl.magique.util.Listener */
    public void stopListener() {
	listener.halt();
    }



    //----------------------------------------------------------------------
    // a lot of perform/ask/askNow for syntax convenience
   /** same as <em>perform(Request)</em> but Request is created from
     * text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param text text of the request
     * @param param1 the first (and unique) parameter of request
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     */
    public void perform(String text, Object param1) {
	Object[] params = { param1 };
	perform(createOrder(text,params));
    }
   /** same as <em>perform(to, Request)</em> but Request is created from
     *text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first (and unique) parameter of request*/
    public void perform(String to, String text, Object param1) {
	Object[] params = { param1 };
	perform(to,createOrder(text,params));
    }

   /** same as <em>perform(Request)</em> but Request is created from text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param text text of the request
     * @param param1 the first parameter of request 
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     * @param param2 the second parameter of request
     */
    public void perform(String text, Object param1, Object param2) {
	Object[] params = { param1, param2 };
	perform(createOrder(text,params));
    }
   /** same as <em>perform(to, Request)</em> but Request is created from
     *text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     */
    public void perform(String to, String text, Object param1, Object param2) {
	Object[] params = { param1, param2 };
	perform(to,createOrder(text,params));
    }
    
   /** same as <em>perform(Request)</em> but Request is created from text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param text text of the request
     * @param param1 the first parameter of request
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     */
    public void perform(String text, Object param1, Object param2, Object param3) {
	Object[] params = { param1, param2, param3 };
	perform(createOrder(text,params));
    }
   /** same as <em>perform(to, Request)</em> but Request is created from
     *text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     */
    public void perform(String to, String text, Object param1, Object param2, Object param3) {
	Object[] params = { param1, param2 , param3};
	perform(to,createOrder(text,params));
    }
    
   /** same as <em>perform(Request)</em> but Request is created from text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param text text of the request
     * @param param1 the first parameter of request
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @param param4 the fourth parameter of request
     */
    public void perform(String text, Object param1, Object param2, Object param3, Object param4) {
	Object[] params = { param1, param2, param3 , param4};
	perform(createOrder(text,params));
    }
   /** same as <em>perform(to, Request)</em> but Request is created from
     *text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @param param4 the third parameter of request
     */
    public void perform(String to, String text, Object param1, Object param2, 
			Object param3, Object param4) {
	Object[] params = { param1, param2 , param3, param4};
	perform(to,createOrder(text,params));
    }
    

    //
    // ask
    //

    /** same as <em>ask(Request)</em> but Request is created from text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param text text of the request
     * @param param1 the first (and unique) parameter of request
     * @return the asked question
     */
    public Request ask(String text, Object param1) {
	Object[] params = { param1 };
	return ask(createQuestion(text,params));
    }
   /** same as <em>ask(to, Request)</em> but Request is created from
     *text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first (and unique) parameter of request
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     * @return the asked question
     */
    public Request ask(String to, String text, Object param1) {
	Object[] params = { param1 };
	return ask(to,createQuestion(text,params));
    }

   /** same as <em>ask(Request)</em> but Request is created from text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param text text of the request
     * @param param1 the first parameter of request
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     * @param param2 the second parameter of request
     * @return the asked question
     */
    public Request ask(String text, Object param1, Object param2) {
	Object[] params = { param1, param2 };
	return ask(createQuestion(text,params));
    }
   /** same as <em>ask(to, Request)</em> but Request is created from
     *text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * @return the asked question
     */
    public Request ask(String to, String text, Object param1, Object param2) {
	Object[] params = { param1, param2 };
	return ask(to,createQuestion(text,params));
    }
    
   /** same as <em>ask(Request)</em> but Request is created from text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param text text of the request
     * @param param1 the first parameter of request
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @return the asked question
     */
    public Request ask(String text, Object param1, Object param2, Object param3) {
	Object[] params = { param1, param2, param3 };
	return ask(createQuestion(text,params));
    }
   /** same as <em>ask(to, Request)</em> but Request is created from
     *text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @return the asked question
     */
    public Request ask(String to, String text, Object param1, Object param2, Object param3) {
	Object[] params = { param1, param2 , param3};
	return ask(to,createQuestion(text,params));
    }
    
   /** same as <em>ask(Request)</em> but Request is created from text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param text text of the request
     * @param param1 the first parameter of request
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @param param4 the fourth parameter of request
     * @return the asked question
     */
    public Request ask(String text, Object param1, Object param2, Object param3, Object param4) {
	Object[] params = { param1, param2, param3 , param4};
	return ask(createQuestion(text,params));
    }
   /** same as <em>ask(to, Request)</em> but Request is created from
     *text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @param param4 the third parameter of request
     * @return the asked question
     */
    public Request ask(String to, String text, Object param1, Object param2, 
			Object param3, Object param4) {
	Object[] params = { param1, param2 , param3, param4};
	return ask(to,createQuestion(text,params));
    }



    /** same as <em>askNow(Request)</em> but Request is created from text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param text text of the request
     * @param param1 the first (and unique) parameter of request
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     * @return the answer (an <em>Object</em>)
     */
    public Object askNow(String text, Object param1) {
	Object[] params = { param1 };
	return askNow(createQuestion(text,params));
    }
   /** same as <em>askNow(to, Request)</em> but Request is created from
     *text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first (and unique) parameter of request
     * @return the answer (an <em>Object</em>)
     */
    public Object askNow(String to, String text, Object param1) {
	Object[] params = { param1 };
	return askNow(to,createQuestion(text,params));
    }

   /** same as <em>askNow(Request)</em> but Request is created from text and params
     *
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     * @return the answer (an <em>Object</em>)
     */
    public Object askNow(String text, Object param1, Object param2) {
	Object[] params = { param1, param2 };
	return askNow(createQuestion(text,params));
    }
   /** same as <em>askNow(to, Request)</em> but Request is created from
     *text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * @return the answer (an <em>Object</em>)
     */
    public Object askNow(String to, String text, Object param1, Object param2) {
	Object[] params = { param1, param2 };
	return askNow(to,createQuestion(text,params));
    }
    
   /** same as <em>askNow(Request)</em> but Request is created from text and params
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param text text of the request
     * @param param1 the first parameter of request
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @return the answer (an <em>Object</em>)
     */
    public Object askNow(String text, Object param1, Object param2, Object param3) {
	Object[] params = { param1, param2, param3 };
	return askNow(createQuestion(text,params));
    }
   /** same as <em>askNow(to, Request)</em> but Request is created from
     *text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @return the answer (an <em>Object</em>)
     */
    public Object askNow(String to, String text, Object param1, Object param2, Object param3) {
	Object[] params = { param1, param2 , param3};
	return askNow(to,createQuestion(text,params));
    }
    
   /** same as <em>askNow(Request)</em> but Request is created from
     * text and params. 
     *
     * <b>Warning</b> must not be used if param1 is a String
     *
     * @param text text of the request
     * @param param1 the first parameter of request
     * (Beware : since, the name of an agent is not cleanly typed by a
     * particular class, you MUST NOT use this syntax if <em>param1</em> is a
     * java.lang.String instance)
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @param param4 the fourth parameter of request
     * @return the answer (an <em>Object</em>) */
    public Object askNow(String text, Object param1, Object param2, Object param3, Object param4) {
	Object[] params = { param1, param2, param3 , param4};
	return askNow(createQuestion(text,params));
    }
   /** same as <em>askNow(to, Request)</em> but Request is created from
     * text and params
     *
     * @param to recipient of request
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @param param4 the third parameter of request
     * @return the answer (an <em>Object</em>)
     */
    public Object askNow(String to, String text, Object param1, Object param2, 
			 Object param3, Object param4) {
	Object[] params = { param1, param2 , param3, param4};
	return askNow(to,createQuestion(text,params));
    }



    /** ask  to treat a concurrent <em>question</em> and does not wait for
     * the result
     *
     * @param question the question
     * @return  the asked question
     */
    public Request concurrentAsk(Request question) {	
	concurrentPerform(question);
	return question;
    }
    /** same as <em>concurrentAsk(Request)</em> but Request is created from text 
     *
     * @param text text of the request
     * @return the asked question
     * @see #concurrentAsk(Request)
     */
    public Request concurrentAsk(String text) {
	Request question = createQuestion(text,true);
	return concurrentAsk(question);
    }

   /** same as <em>concurrentAsk(Request)</em> but Request is created from text and params
     *
     * @param text text of the request
     * @param params parameters of request
     * @return the asked question
     * @see #concurrentAsk(Request)
     */
    public Request concurrentAsk(String text, Object params[]) {
	Request question = createQuestion(text,params,true);
	return concurrentAsk(question);
    }


    /** same as <em>concurrentAsk(Request)</em> but Request is created from text and params
     *
     * @param text text of the request
     * @param param1 the first (and unique) parameter of request
     * @return the asked question
     */
    public Request concurrentAsk(String text, Object param1) {
	Object[] params = { param1 };
	return concurrentAsk(createOrder(text,params));
    }

   /** same as <em>concurrentAsk(Request)</em> but Request is created from text and params
     *
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * @return the asked question
     */
    public Request concurrentAsk(String text, Object param1, Object param2) {
	Object[] params = { param1, param2 };
	return concurrentAsk(createOrder(text,params));
    }
    
   /** same as <em>concurrentAsk(Request)</em> but Request is created from text and params
     *
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @return the asked question
     */
    public Request concurrentAsk(String text, Object param1, Object param2, Object param3) {
	Object[] params = { param1, param2, param3 };
	return concurrentAsk(createOrder(text,params));
    }
    
   /** same as <em>concurrentAsk(Request)</em> but Request is created from text and params
     *
     * @param text text of the request
     * @param param1 the first parameter of request
     * @param param2 the second parameter of request
     * @param param3 the third parameter of request
     * @param param4 the fourth parameter of request
     * @return the asked question
     */
    public Request concurrentAsk(String text, Object param1, Object param2, Object param3, Object param4) {
	Object[] params = { param1, param2, param3 , param4};
	return concurrentAsk(createOrder(text,params));
    }

    
    
} // AbstractAgent
