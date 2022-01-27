/**
 * AtomicAgent.java
 *
 *
 * Created: Tue Apr 20 15:34:07 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique;

import fr.lifl.magique.util.*;
import fr.lifl.magique.policy.*;
import fr.lifl.magique.skill.*;
import fr.lifl.magique.platform.*;
import fr.lifl.magique.policy.concurrency.*;
import fr.lifl.magique.skill.system.*;
import fr.lifl.magique.platform.classloader.BytecodeClassLoader;

import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.*;

/**
 * This class is dedicated to be extended to create more complex agent. It
 * provides an hollow shell for creating agents.
 * 
 * An agent must own to a <em>platform</em>, then after creation he must be
 * added to the platform that runs on his host (see platform.addPlatform(Agent))
 * 
 * The <name>of a agent is based on a short name given at creation. To this
 * short name, the IP address (or hostname) of the host where the agent runs is
 * added, as well as the port number of the platform. Therefore, name is of the
 * form <tt>shortName@hostname:port</tt> (e.g.
 * anAgent@here.domain.country:4444).
 * 
 * An agent is gifted of <em>skills</em>. Roughly a skill is a component
 * (then an object) the public methods of which are immediately usable buy the
 * agent. An agent can dynamically learn new skills using the <tt>addSkill</tt>
 * method or the <tt>learnSkill</tt> skill. A new skill can be learn while it
 * does not contain a public method with exactly the same signature that another
 * already "known" by the agent. Skills correspond to the <em>reactive</em>
 * part of the agent.
 * 
 * Skills known by agent at creation are defined in the <tt>initBasicSkills</tt>
 * methods.
 * 
 * Here messages are in fact <em>Request</em> objects. The <tt>text</tt>
 * member of a request object is a name of a method (args can be provided).
 * Therefore, an agent can "ask" another agent to achieve a "method" for him.
 * 
 * Some othe properties can be set (e.g. the policy for concurrent request :
 * <tt>setConcurrencyPolicy</tt>
 * 
 * @see fr.lifl.magique.Agent
 * @see fr.lifl.magique.Skill
 * @see fr.lifl.magique.skill.DefaultSkill
 * @see fr.lifl.magique.platform.Platform
 * @author JC Routier routier@lifl.fr
 */

public class AtomicAgent extends AbstractAgent {

   protected MessageList unsentRequests = new MessageList();

   protected Object monitor = new Object();

   /** the policy for concurrency */
   protected ConcurrencyPolicy myConcurrencyPolicy = new DefaultConcurrencyPolicy();

   /** the platform i belong to */
   private Platform platform;

   /**
    * this attributs stores all the "object-skill" the agent own. the mapping is :
    * the key is a method's name, the value is a reference to the associated
    * object.
    */
   protected Hashtable mySkills = null;

   protected List allKnownSkills = null;

   // CONSTRUCTORS
   /** */
   public AtomicAgent() {
      super();
      this.initialize();
   }

   /**
    * @param name =
    *           shortname : identifier for my name (platform hostname and port
    *           number are added) <em>hostname</em> is by default the local
    *           one (not "localhost" if Ip address does exist)
    */
   public AtomicAgent(String name) {
      super(name);
      this.initialize();
   }

   //-------------- Dynamical Skills ----------------

   protected final void initialize() {
      setQuestionTable(new QuestionTable(myConcurrencyPolicy));
      mySkills = new Hashtable();
      this.allKnownSkills = new ArrayList();
      // mise � jour des competences de "Agent"
      Method[] tabOfMethods = this.getClass().getMethods();
      for (int m = 0; m < tabOfMethods.length; m++) {
         if (tabOfMethods[m].getModifiers() == Modifier.PUBLIC) {
            String methodName = (String) tabOfMethods[m].getName() + "(";
            Class[] parameters = tabOfMethods[m].getParameterTypes();
            for (int i = 0; i < parameters.length; i++) {
               methodName += parameters[i].getName();
               if ((i + 1) < parameters.length) {
                  methodName += ", ";
               }
            }
            methodName += ")";
            mySkills.put(methodName, this);
         }
      }
      try {
         initBasicSkills();
      } catch (SkillAlreadyAcquiredException e) {
      }
   }

   /**
    * defines the skills known by the agent at creation.
    * 
    * @exception SkillAlreadyAcquiredException
    *               if agent tries to learn an already known skill
    */
   protected void initBasicSkills() throws SkillAlreadyAcquiredException {
      this.addSkill(new fr.lifl.magique.skill.system.ConnectionSkill(this));
      this.addSkill(new fr.lifl.magique.skill.system.KillSkill(this));
      this.addSkill(new fr.lifl.magique.skill.system.DisplaySkill());
      this.addSkill(new fr.lifl.magique.skill.system.AddSkillSkill(this));
      this.addSkill(new fr.lifl.magique.skill.system.LearnSkill(this));
      this.addSkill(new fr.lifl.magique.skill.system.ForgetSkill(this));
   }

   /** set the concurrency policy */
   public void setConcurrencyPolicy(ConcurrencyPolicy theConcurrencyPolicy) {
      verbose(3, "concurrency policy has changed");
      this.myConcurrencyPolicy = theConcurrencyPolicy;
      getQuestionTable().setConcurrencyPolicy(theConcurrencyPolicy);
   }

   /** set my platform */
   public void setPlatform(Platform platform) {
      this.platform = platform;
      setName(Name.getShortName(getName()) + "@" + platform.getName());
   }

   /**
    * get my platform
    * 
    * @return my platform
    */
   public Platform getPlatform() {
      return platform;
   }

   /** @return my unsent requests list */
   protected MessageList getUnsentRequests() {
      return (unsentRequests);
   }

   //------------ Dynamical Skills -----------------------
   /**
    * Add all the methods of the object newSkill to the agent
    * 
    * 
    * @param newSkill
    *           the new skills to be added. Each method of the object newSkill,
    *           is added to the agent.
    * 
    * @exception SkillAlreadyAcquiredException
    *               if <tt>newSkill</tt> contains a method with the same
    *               signature as another already known
    *  
    */
   public void addSkill(Object newSkill) throws SkillAlreadyAcquiredException {
      synchronized (monitor) {
         verbose(3, getName() + " addSkill " + newSkill.getClass().getName());
         Method[] tabOfMethods = newSkill.getClass().getDeclaredMethods();
         // JC : si d�j� une instance de la m�me classe connue, inutile de continuer
         String newSkillClassName = newSkill.getClass().getName();
         for (Iterator it = this.allKnownSkills.iterator(); it.hasNext();) {
            if (newSkillClassName.equals(it.next().getClass().getName())) {
               verbose(3, getName() + " skill " + newSkill.getClass().getName() + " already knwon");
               throw new SkillAlreadyAcquiredException(newSkillClassName);
            }
         }
         //----
         for (int m = 0; m < tabOfMethods.length; m++) {
            if (tabOfMethods[m].getModifiers() == Modifier.PUBLIC) {
               String methodName = (String) tabOfMethods[m].getName() + "(";
               Class[] parameters = tabOfMethods[m].getParameterTypes();
               for (int i = 0; i < parameters.length; i++) {
                  methodName += parameters[i].getName();
                  if ((i + 1) < parameters.length) {
                     methodName += ", ";
                  }
               }
               methodName += ")";
               if (mySkills.containsKey(methodName)) {
                  verbose(3, getName() + " method " + methodName + " already knwon");
                  throw new SkillAlreadyAcquiredException(methodName);
               }
            }
         }
         //
         this.allKnownSkills.add(newSkill);
         Vector newMethods = new Vector();
         for (int m = 0; m < tabOfMethods.length; m++) {
            if (tabOfMethods[m].getModifiers() == Modifier.PUBLIC) {
               String methodName = (String) tabOfMethods[m].getName() + "(";
               Class[] parameters = tabOfMethods[m].getParameterTypes();
               for (int i = 0; i < parameters.length; i++) {
                  methodName += parameters[i].getName();
                  if ((i + 1) < parameters.length) {
                     methodName += ", ";
                  }
               }
               methodName += ")";
               mySkills.put(methodName, newSkill);
               newMethods.addElement(methodName);
            }
         }
      }
   }

   /**
    * Add all the methods of the class <tt>skillClassName</tt> to the agent.
    * <tt>skillClassName</tt> must be in the classpath.
    * 
    * 
    * @param skillClassName
    *           the class used that contains new skills to be added.
    * 
    * @exception SkillAlreadyAcquiredException
    *               if <tt>newSkill</tt> contains a method with the same
    *               signature as another already known
    */
   public void addSkill(String skillClassName) throws SkillAlreadyAcquiredException {
      try {
         Class skillClass = (Class) askNow(Platform.PLATFORMMAGIQUEAGENTNAME, "getClass", skillClassName);

         Skill skill = null;
         if (MagiqueDefaultSkill.class.isAssignableFrom(skillClass)) {
            Constructor constructor;
            try {
               constructor = skillClass.getConstructor(new Class[]{Agent.class});
               skill = (Skill) constructor.newInstance(new Object[]{this});
            } catch (SecurityException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
            }
            catch (IllegalArgumentException e2) {
               // TODO Auto-generated catch block
               e2.printStackTrace();
            } catch (InvocationTargetException e2) {
               // TODO Auto-generated catch block
               e2.printStackTrace();
            }
         }
         else {
            skill = (Skill) skillClass.newInstance();
         }
         addSkill(skill);
      } catch (InstantiationException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }
   }

   /**
    * Add all the methods of the class <tt>skillClassName</tt> to the agent.
    * <tt>skillClassName</tt> must be in the classpath. <tt>args</tt> are
    * used to create the skill.
    * 
    * 
    * @param skillClassName
    *           the class used that contains new skills to be added.
    * 
    * @param args
    *           the ragument used to create the skill
    * 
    * @exception SkillAlreadyAcquiredException
    *               if <tt>newSkill</tt> contains a method with the same
    *               signature as another already known
    * 
    * @see AtomicAgent#addSkill(Skill)
    */
   public void addSkill(String skillClassName, Object[] args) throws SkillAlreadyAcquiredException {
      try {

         Class skillClass = (Class) askNow(Platform.PLATFORMMAGIQUEAGENTNAME, "getClass", skillClassName);

         // tableau des vecteurs des classes heritees des args
         Vector[] argsClassVector = new Vector[args.length];
         for (int i = 0; i < args.length; i++) {
            Class c = args[i].getClass();
            argsClassVector[i] = new Vector();
            while (c != null) {
               argsClassVector[i].addElement(c);
               c = c.getSuperclass();
            }
         }
         // table des constructeur/tableaux des classes des params du
         // constructeur
         Constructor[] skillConstructors = skillClass.getConstructors();
         Hashtable constructorTable = new Hashtable();
         for (int i = 0; i < skillConstructors.length; i++) {
            Class[] p = skillConstructors[i].getParameterTypes();
            constructorTable.put(skillConstructors[i], p);
         }

         // elimination des constructeurs inapplicables
         Enumeration e = constructorTable.keys();
         while (e.hasMoreElements()) {
            Constructor c = (Constructor) e.nextElement();
            Class[] p = (Class[]) constructorTable.get(c);
            if (p.length != args.length) {
               constructorTable.remove(c);
            } else {
               int i = 0;
               boolean encore = true;
               while (encore && i < p.length) {
                  encore = argsClassVector[i].contains(p[i]);
                  i++;
               }
               if (!encore) {
                  constructorTable.remove(c);
               }
            }
         }
         // A AMELIORER !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         Constructor skillConstructor = (Constructor) constructorTable.keys().nextElement();
         Skill skill = (Skill) skillConstructor.newInstance(args);
         addSkill(skill);
      } catch (InvocationTargetException e) {
         e.printStackTrace();
      } catch (InstantiationException e) {
         e.printStackTrace();
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      }
   }

   public String giveSkillClassNameFromSignature(String signature) {
      return mySkills.get(signature).getClass().getName();
   }

   public Iterator getAllKnownSkills() {
      return this.allKnownSkills.iterator();
   }

   /**
    * Remove the "key" from the hashtable of skills. This method removes all the
    * method learned at the same time than key (that means the "full" skill
    * which contains "key")
    * 
    * @param key
    *           a string representing the method's signature to be removed.
    * 
    * @exception SkillNotKnownException
    *               if <tt>key</tt> is not a knwon method signature
    */
   public void removeSkill(String key) throws SkillNotKnownException {
      verbose(3, getName() + " removeSkill " + key);
      synchronized (monitor) {
         if (mySkills.containsKey(key)) {
            //
            Skill skill = (Skill) mySkills.get(key);
            this.removeSkill(skill);
         } else {
            throw new SkillNotKnownException(key);
         }
      }
   }

   private void removeSkill(Skill skill) {
      verbose(3, getName() + " removeSkill " + skill.getClass().getName());
      for (Enumeration enu = mySkills.keys(); enu.hasMoreElements();) {
         String nextKey = (String) enu.nextElement();
         if (mySkills.get(nextKey).equals(skill))
            mySkills.remove(nextKey);
      }
      this.allKnownSkills.remove(skill);
   }

   /**
    * remove a skill from its class name
    * 
    * @param skillClassName
    *           the class name of the skill to be removed
    * @throws SkillNotKnownException
    *            if skill is not a known skill
    */
   public void removeSkillFromClassName(String skillClassName) throws SkillNotKnownException {
      verbose(3, getName() + " removeSkillFromClassName " + skillClassName);
      synchronized (monitor) {
         boolean trouve = false;
         for (Iterator it = this.allKnownSkills.iterator(); it.hasNext() && !trouve;) {
            Skill skill = (Skill) it.next();
            //System.out.println(getName()+"   "+skill.getClass().getName()+" ** "+skillClassName+"  --> "+skill.getClass().getName().equals(skillClassName));
            if (skill.getClass().getName().equals(skillClassName)) {
               trouve = true;
               this.removeSkill(skill);
            }
         }
         if (!trouve) {
            throw new SkillNotKnownException(skillClassName);
         }
      }
   }

   //------------ Dynamical Skills -----------------------

   /**
    * the vector containing the names (String) of the methods of this object
    * 
    * @return the vector containing the names (String) of the methods of this
    *         object
    */
   public Vector myMethodsNames() {
      Vector myMethods = new Vector();
      for (Enumeration enu = mySkills.keys(); enu.hasMoreElements();) {
         myMethods.addElement(enu.nextElement());
      }
      return (myMethods);
   }

   //
   // connection
   //
   /**
    * connect this agent to another one
    * 
    * @param agentName
    *           the name of the agent i connect to (of the form
    *           "name@hostname:rmiport")
    */
   public void connectTo(String agentName) {
      askNow("connectionTo", new Object[] { agentName });
   }

   //
   // requests : in and out
   //

   public void send(String to, Request request) {
      send(to, (Message) request);
   }

   /**
    * send a request to <em>to</em> through the platform if needed If
    * <em>to</em> is unknown, <em>request</em> is stored
    * 
    * @param to
    *           name of recipient (name can be short name)
    * @param request
    *           request to be sent
    * @see java.io.Serializable
    */
   public void send(String to, Message msg) {
      Request request = (Request) msg;

      // got the "true name"
      String fullName = getAgenda().getFullName(to);

      if (!fullName.equals(Name.getShortName(fullName))) {
         try {
            String agentShortName = Name.getShortName(fullName);
            String agentHost = InetAddress.getByName(Name.getHostName(fullName)).getHostAddress();
            InetAddress address = InetAddress.getByName(agentHost);
            fullName = agentShortName + "@" + agentHost + ":" + Name.getPort(fullName);
         } catch (Exception e) {
            e.printStackTrace();
         }
      } else {
         fullName = fullName + "@" + Name.getHostName(getName()) + ":" + Name.getPort(getName());
      }

      verbose(5, getName() + ": " + "i send " + request.getName() + ":" + request.getText() + " to " + fullName);
      if (fullName.equals(getName())) {
         processRequest(request);
      } else {
         if (!getAgenda().containsKey(fullName)) {// if unknown then
            Agent.verbose(2, getName() + " ***** UNKNOWN REQUEST " + request.getText() + " *****");
            unsentRequests.addMessage((Message) createOrder("send", new Object[] { fullName, request }));
         } else {
            if (!request.isAnAnswer()) {
               request.addToPath(getName());
            }
            sendMessage(fullName, request);
         }
      }
   }

   public void sendMessage(String to, Message msg) {
      platform.haveAMessage(new PlatformMessage(to, msg));
   }

   /**
    * <em>agent</em> is just known at now, maybe he can handle some of the
    * unsent requests ? if so I send them to him
    * 
    * @param agent
    *           the new known agent
    * @param methods
    *           the methods this agent can handle
    */
   public void treatUnsentRequests(String agent, Vector methods) {
      Agent.verbose(3, getName() + " treatUnsentRequests " + agent);
      MessageList tmp = (MessageList) unsentRequests.clone();
      for (Enumeration enu = tmp.elements(); enu.hasMoreElements();) {
         Request request = (Request) enu.nextElement();
         if (request.getText().equals("send")) {
            Request unsent = (Request) request.getParams()[1];
            if (methods.contains(unsent.getSignature())) {
               String name = (String) request.getParams()[0];
               if (agent.equals(name) || (Name.getShortName(name).equals(name) && Name.getShortName(agent).equals(name))) {
                  if (!unsent.isAnAnswer() && !unsent.getSender().equals(getName())) {
                     unsent.addToPath(getName());
                  }
                  send(name, unsent);
                  unsentRequests.removeMessage((Message) request);
               }
            }
         } else if (methods.contains(request.getSignature())) {
            send(agent, request);
            unsentRequests.removeMessage((Message) request);
         }
      }
   }

   /**
    * process a received <em>Request</em> object, it can be
    * <UL>
    * <LI>an answer, to one of my request, back</LI>
    * <LI>an answer to someone else back on the way to its creator</LI>
    * <LI>a request to treat</LI>
    * </UL>
    * 
    * @param request
    *           request to handle
    */
   public void processRequest(Request request) {
      // c'est une reponse ?
      if (request.isAnAnswer()) {
         if (request.isAnswerToMe(getName())) //a une de mes requetes ?
            processAnswer(request);
         else { // reponse en transit (a un autre) : je fais suivre
            request.removeLastFromPath();
            String nextAgent = request.lastSender();
            send(nextAgent, request);
         }
      } else if (request.isConcurrent())
         concurrentPerform(request);
      else
         perform(request);
   }

   /**
    * invoke request : text is a known method name. If is a question, answer is
    * replied.
    * 
    * @param request
    *           the reauest to invoke
    * @return the request with its <em>answer</em> field updated with
    *         invocation result (<em>null</em> if no answer is required)
    */
   protected Request interprete(Request request) {
      Object target = this;
      String methodSignature = request.getText() + "(";
      Object[] params = request.getParams();
      for (int i = 0; i < params.length; i++) {
         methodSignature += params[i].getClass().getName();
         if ((i + 1) < params.length) {
            methodSignature += ", ";
         }
      }
      methodSignature += ")";
      if (mySkills.containsKey(methodSignature)) {
         target = mySkills.get(methodSignature);
      }

      try {
         params = request.getParams();
         Class[] paramClasses = new Class[params.length];
         verbose(5, getName() + ": i interprete " + request.getName() + ":" + request.getText());
         for (int i = 0; i < params.length; i++)
            paramClasses[i] = params[i].getClass();
         // get and create method corresponding to request text
         Method method = (target.getClass()).getMethod(request.getText(), paramClasses);
         // method is invoked and computed value is catched and stored
         // as the answer to the request
         request.setAnswer(method.invoke(target, params), getName());
      } catch (InvocationTargetException e) {
         System.out.println(" 1 " + getName() + " refuses " + request.getName() + " " + request.getText() + " exception levee");
         Object[] parameters = request.getParams();
         for (int i = 0; i < parameters.length; i++) {
            System.out.println("                params " + i + " : " + parameters[i]);
         }
         System.out.println(" target exception*****************************************************************");
         (e.getTargetException()).printStackTrace();
         System.out.println(" fin target exception ************************************************************");
      } catch (IllegalAccessException e) {
         System.out.println(e.getMessage() + " 2 " + getName() + " refuses " + request.getName() + " " + request.getText());
         e.printStackTrace();
      } catch (NoSuchMethodException e) {
         System.out.println("********** debut NoSuchMethod EXCEPTION **********");

         System.out.println(" **********  DEBUT **********");
         for (int i = 0; i < params.length; i++)
            System.out.println(params[i].getClass() + " " + params[i].getClass().getClassLoader());
         System.out.println("***");
         System.out.println("Target : " + target.getClass() + " " + target.getClass().getClassLoader());
         System.out.println(" **********  FIN **********");

         System.out.println(e.getLocalizedMessage());
         System.out.println("target " + target);
         Object[] parameters = request.getParams();
         for (int i = 0; i < parameters.length; i++) {
            System.out.println("                params " + i + " : " + parameters[i]);
         }
         System.out.println("********** fin NoSuchMethod EXCEPTION **********");
         System.out.println(e.getMessage() + " 3 " + getName() + " refuses " + request.getName() + " " + request.getText());
         e.printStackTrace();
      }
      //	verbose(5,getName()+": i've finished to interprete
      // "+request.getName()+":"+request.getText());
      return (request);
   }

   /**
    * starts the agent activity described in <tt>action()</tt> method
    * (something like the <tt>start()</tt> method in thread)
    * 
    * @see fr.lifl.magique.Agent#setAction()
    * @exception NoActionSkillException
    *               if action has not been defined
    */
   public void start() throws NoActionSkillException {
      try {
         perform("action");
      } catch (Exception e) {
         throw new NoActionSkillException(getName());
      }
   }

   /**
    * changes the <tt>action</tt> skill that corresponds to the proactive part
    * of the agent
    * 
    * @param action
    *           the new action skill, it must inherit <tt>ActionSkill</tt>
    * 
    * @see fr.lifl.magique.skill.ActionSkill
    */
   public void setAction(ActionSkill action) {
      try {
         removeSkill("action()");
      } catch (SkillNotKnownException e) {
         e.printStackTrace();
      }
      addSkill(action);
   }

   /**
    * treat a request : if the agent knows the method, he applies it,
    * 
    * @param request
    *           the request to be treated
    * @return no return value
    */
   public void perform(Request request) {
      String method = request.getSignature();
      verbose(4, "perform : " + getName() + ": i do " + request.getName() + ":" + method);

      if (myMethodsNames().contains(method)) {
         interprete(request);
         // si c'est une question a laquelle je viens de repondre,
         // je retransmets la reponse
         if (request.getIsQuestion())
            send(request.lastSender(), request);
      }
   }

   /**
    * treat a concurrent request : if the agent knows the method, he applies it,
    * 
    * @param request
    *           the request to be treated
    * @return no return value
    */
   public void concurrentPerform(Request request) {
      String method = request.getSignature();
      verbose(2, getName() + ": " + "i concurrentPerform " + request.getName() + ":" + request.getText());
      if (myMethodsNames().contains(method)) {
         interprete(request);
         // si c'est une question a laquelle je viens de repondre,
         // je retransmets la reponse
         if (request.getIsQuestion())
            send(request.lastSender(), request);
      }
   }

} // AtomicAgent

