/**
 * Request.java
 * <p>
 * <p>
 * Created: Thu Sep 10 12:58:35 1998
 *
 * @author Jean-Christophe.Routier
 * @version
 */
package fr.lifl.magique;

import java.util.Enumeration;
import java.util.Vector;

/** <em>Requests</em> are the basic message object between
 * agents. A request can be an order or a
 * question if answer is required. It is composed of a text (a string)
 * and possibly a vector of args for the request. Args must be
 * <em>serializable</em>.  Typically text is the name of a method
 * (maybe with args) that will be invoked (or forwarded for invocation
 * by someone else) by the recipient.
 *
 * @author JC Routier. routier@lifl.fr
 * @see java.io.Serializable
 * @see fr.lifl.magique.Agent
 */

public class Request
        implements Cloneable, Message {

    private static int cpt = 0;
    /** <em>true</em> if this request is a question       
     * @serial
     */
    private Boolean isQuestion;
    /** <em>true</em> if this request is a concurrent one. Default is
     * <em>false</em> 
     * @serial */
    private Boolean concurrent = Boolean.FALSE;
    /** request identifier (respect the pattern "<em>sender</em>-<em>number</em>")
     * @serial
     */
    private String name;
    /** request text (typically a method name)       
     * @serial
     */
    private String text;
    /** args of request (typically of the method)       
     * @serial
     */
    private Object[] params = new Object[0];  // les arguments de l'ordre
    /** the answer (if any, null otherwise)       
     * @serial
     */
    private Object answer = null;
    /** name of who has answered the request       
     * @serial
     */
    private String answerer = null;
    /** request are forwarded through the net untill it reaches the
     *  convenient recipient. This stores the name of all agents met
     *  during its course       
     * @serial
     */
    private Vector path = new Vector();

    private int pathLength;

    /** request with just a text : to transmit simple messages 
     * @param text : text of message     
     */
    public Request() {
    }

    /** request with just a text : to transmit simple messages 
     * @param text : text of message     
     */
    public Request(String text) {
        this.text = text;
    }

    /** creates a request without args
     * @param sender the sender of the request
     * @param isQuestion <em>true</em> if it is a question
     *  <em>false</em> otherwise (order)
     * @param text text of request */
    public Request(String sender, boolean isQuestion, String text) {
        this.text = text;
        this.name = sender + "-" + cpt++;
        this.isQuestion = Boolean.valueOf(isQuestion);
        addToPath(sender);
    }

    /** creates a request wit args
     * @param sender the sender of the request
     * @param isQuestion <em>true</em> if it is a question
     *  <em>false</em> otherwise (order)
     * @param text text of request 
     * @param params request args (lust be serializable)
     */
    public Request(String sender, boolean isQuestion,
                   String text, Object[] params) {
        this.text = text;
        this.params = new Object[params.length];
        for (int i = 0; i < params.length; i++)
            this.params[i] = params[i];
        this.name = sender + "-" + cpt++;
        this.isQuestion = Boolean.valueOf(isQuestion);
        addToPath(sender);
    }

    public static int getCpt() {
        return cpt;
    }

    /** returns a lone of this request (with SAME name)
     *
     * @return a clone of this request
     */
    public Object clone() {
        Request requestClone = new Request("");
        try {
            requestClone = (Request) super.clone();
        } catch (CloneNotSupportedException e) {
        }
        requestClone.path = new Vector();
        for (Enumeration enu = path.elements(); enu.hasMoreElements(); )
            requestClone.addToPath((String) enu.nextElement());
        return requestClone;
    }

    /** give a string representation of Request object
     * @return string representation of Request object
     */
    public String toString() {
        System.out.print("name " + name + " " + text + " " + params.length + " ");
        System.out.print("sender " + lastSender());
        if (params.length != 0) System.out.print("params(-1) " + params[(params.length) - 1]);
        System.out.println();

        String s = name + " : " + text + "(";
        for (int i = 0; i < params.length - 1; i++)
            s = s + params[i].toString() + ",";
        if (params.length != 0) {
            s = s + params[(params.length) - 1].toString();
        }
        return s + ") from " + lastSender();
    }

    /**  @return the request name */
    public String getName() {
        return (name);
    }

    /** @return request text (typically a method name)*/
    public String getText() {
        return (text);
    }

    /** @return request args */
    public Object[] getParams() {
        return (params);
    }

    /** @return request sender */
    public String getSender() {
        return (String) path.firstElement();
    }

    /** @return name of the answerer to request (typically method invoker) */
    public String getAnswerer() {
        return answerer;
    }

    /** @return the answer (null if none) */
    public Object getAnswer() {
        return answer;
    }

    /** @return <em>true</em> if it is a question : an answer is required */
    public boolean getIsQuestion() {
        return isQuestion.booleanValue();
    }

    /** @return <em>true</em> if it is a concurrent request */
    public boolean isConcurrent() {
        return concurrent.booleanValue();
    }

    /** @return the path from sender to recipient through the agent
     *  net. This is used to return answer */
    public Vector getPath() {
        return path;
    }

    /** @return the length  of the path used to reach the answerer 
     * If answerer is the "sender", path is 1. */
    public int getPathLength() {
        return pathLength;
    }


    /** returns the string that represents signature of the method
     * defined by text and parameters classes
     *
     * @return the string that represents the signature of the method
     * defined by text and parameters classes */
    public String getSignature() {
        String signature = text + "(";
        for (int i = 0; i < params.length; i++) {
            // System.out.println(">>>"+this.text+" p ("+i+") "+params[i]);
            //System.out.println("... "+params[i].getClass());
            signature = signature + params[i].getClass().getName();
            if ((i + 1) < params.length) {
                signature = signature + ", ";
            }
        }
        signature = signature + ")";
        //System.out.println("SIGNATURE "+signature);

        return signature;
    }

    /** sets  concurrent to <true>, request becomes a concurrent one */
    public void setConcurrent() {
        concurrent = Boolean.TRUE;
    }


    /** sets the answer
     * @param answer answer value
     * @param answerer answerer name
     * @return no return value
     */
    public void setAnswer(Object answer, String answerer) {
        this.answer = answer;
        this.answerer = answerer;
        this.pathLength = path.size();
    }

    /** <em>true</em> iff this request is an answer to a request from <em>name</em>
     * @param name the name to test
     * @return <em>true</em> iff it is an answer to a request from <em>name</em>
     */
    public boolean isAnswerToMe(String name) {
        return (getSender().equals(name));
    }

    /** <em>true</em> iff this request is an answer (request has
     * already been treated and is back to sender)
     * @return <em>true</em> iff it is an answer */
    public boolean isAnAnswer() {
        return answerer != null;
    }


    // gestion de path 

    /** returns the name of last agent who has forwarded the request
     * @return the name of last agent who has forwarded the request*/
    public String lastSender() {
        return (String) path.lastElement();
    }

    /** adds an agent to <em>path</em> (at the end)
     * @param agentName the name of the agent to add to <am>path</em>
     * @return no return value */
    public void addToPath(String agentName) {
        //	System.out.println(name+" -> "+text+" -- "+agentName);
        path.addElement(agentName);
    }

    /** removes last agent from path <em>path</em>
     * @return no return value */
    public void removeLastFromPath() {
        path.removeElementAt(path.size() - 1);
    }

} // Request
