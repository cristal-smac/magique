/**
 * Platform.java
 *
 *
 * Created: Fri Jan 21 10:00:15 2000
 *
 * @author Jean-Christophe Routier
 * @version
 */
package fr.lifl.magique.platform;

import fr.lifl.magique.*;
import fr.lifl.magique.agent.PlatformAgent;
import fr.lifl.magique.util.*;
import fr.lifl.magique.platform.rmi.*;
import fr.lifl.magique.platform.classloader.*;

import java.rmi.*;
import java.rmi.registry.*;
import java.util.*;

import java.io.*;

/**
 * A platform is used to gather and manage all the agents on a given host.
 * Communications between remote agents is achieved using a communication layer
 * between platform through RMI.
 * 
 * When 2 remote agents try to communicate, the "connection" between their
 * platform is automatically performed.
 * 
 * Registry is automatically launched and port 4444 is the default registry
 * port.
 * 
 * Agent must be added to the platform using <tt>addAgent</tt>
 */

public class Platform {

   public final static String PLATFORMMAGIQUEAGENTNAME = "MagiquePlatformAgent";
   public final static int DEFAULT_PORT = 4444;

   private int myPort;
   private String name;

   private PlatformServer server;
   private Hashtable platformAgenda = new Hashtable();
   private Hashtable myAgents = new Hashtable();

   private MessageList messages = new MessageList();
   private Listener listener;

   private PlatformAgent platformAgent = new PlatformAgent(this);

   // consctuctor
   /**
    * default port (4444) is used
    */
   public Platform() {
      this(DEFAULT_PORT);
   }

   /**
    * @param port
    *           the port for the server associated with this platform
    */
   public Platform(int port) {
      myPort = port;
      listener = new Listener(this);
      listener.start();
      initServer();
      createPlatformAgent();
   }

   /**
    * @param port
    *           the port for the server associated with this platform
    */
   public Platform(Integer port) {
      this(port.intValue());
   }

   // access to private member
   /**
    * returns the agenda of the platform, agenda associates a platform name name
    * with the corresponding Communicate rmi interface implementation
    * 
    * @return the agenda of the platform,
    * 
    * @see fr.lifl.magique.platform.rmi.Communicate
    */
   public Hashtable getPlatformAgenda() {
      return platformAgenda;
   }

   /**
    * returns the name of the platform (of the form hostIpAddredd:port)
    * 
    * @return the name of the platform
    */
   public String getName() {
      return name;
   }

   /**
    * returns the port of the server of the platform
    * 
    * @return the port of the server of the platform
    */
   public int getPort() {
      return myPort;
   }

   /**
    * returns the RMI server asociated with this platform
    * 
    * @return the RMI server asociated with this platform
    */
   public PlatformServer getServer() {
      return server;
   }

   /**
    * returns the hashtable of the agents active in the platform, it associates
    * an agent name with the corresponding Agent object
    * 
    * @return the hashtable of the agents active in the platform
    * 
    * @see fr.lifl.magique.Agent
    */
   public Hashtable getMyAgents() {
      return myAgents;
   }

   /**
    * returns my message list
    * 
    * @return my message list
    */
   public MessageList getMessages() {
      return messages;
   }

   /**
    * initializes the rmi server for this platform. rmi registry is created at
    * given port
    *  
    */
   private void initServer() {
      try {
         String hostName = java.net.InetAddress.getLocalHost().getHostAddress();
         name = hostName + ":" + myPort;

         LocateRegistry.createRegistry(myPort);

         String serverName = "//" + name + "/PlatformServer";
         server = new PlatformServer(this);
         Naming.rebind(serverName, server);
         Agent.verbose(1, "platform " + name + " launched");
      } catch (Exception e) {
         System.out.println("initServer pb : " + e.getMessage());
         e.printStackTrace();
      }
   }

   /**
    * creates and initializes the plaform agent
    *  
    */
   private PlatformAgent createPlatformAgent() {
      platformAgent = new PlatformAgent(this);

      String trueName = PLATFORMMAGIQUEAGENTNAME + "@" + name;

      platformAgent.setName(trueName);
      platformAgent.setPlatform(this);

      Agent.verbose(0, "\n Magique : PlatformAgent created");
      platformAgent.addSkill(new fr.lifl.magique.skill.system.MPLSkill(platformAgent, myPort + 1));
      Agent.verbose(5, "start MPL");
      platformAgent.perform("startMPLSkill");
      Agent.verbose(5, "MPL started");

      myAgents.put(trueName, platformAgent);

      return platformAgent;
   }

   /**
    * the given agent is added to the platform
    * 
    * @param agent
    *           the agent to be added
    */
   public void addAgent(AtomicAgent agent) throws AlreadyExistingAgentException {
      String trueName = fr.lifl.magique.util.Name.getShortName(agent.getName()) + "@" + name;

      if (myAgents.contains(trueName)) {
         throw new AlreadyExistingAgentException(trueName);
      }

      agent.setName(trueName);
      agent.setPlatform(this);
      myAgents.put(trueName, agent);

      agent.connectTo(PLATFORMMAGIQUEAGENTNAME);

      Agent.verbose(2, agent.getName() + " has joined the platform");
   }

   /**
    * dynamically creates a magique agent (class fr.lifl.magique.Agent). Agent
    * is added to the platform
    * 
    * @param agentName
    *           the name of the created agent
    * 
    * @result the created agent (an <tt>fr.lifl.magique.Agent</tt>)
    */
   public Agent createAgent(String agentName) throws AlreadyExistingAgentException {
      return (Agent) createAgent("fr.lifl.magique.Agent", agentName);
   }

   /**
    * dynamically creates an agent from its class an name. Agent is added to the
    * platform
    * 
    * @param agentClassName
    *           the class of the to be created agent
    * @param agentName
    *           the name of the crated agent
    * 
    * @result the created agent (an <tt>AtomicAgent</tt>)
    */
   public AtomicAgent createAgent(String agentClassName, String agentName) throws AlreadyExistingAgentException {

      AtomicAgent agent = null;

      String trueName = fr.lifl.magique.util.Name.getShortName(agentName) + "@" + name;
      if (myAgents.contains(trueName)) {
         throw new AlreadyExistingAgentException(trueName);
      }
      try {
         Class agentClass = (Class) ((PlatformAgent) myAgents.get(PLATFORMMAGIQUEAGENTNAME + "@" + name)).getClass(agentClassName);

         agent = (AtomicAgent) agentClass.getConstructor(new Class[] { Class.forName("java.lang.String") }).newInstance(
               new Object[] { trueName });
      } catch (Exception e) {
         System.out.println("agent construction problem");
         e.printStackTrace();
      }
      agent.setPlatform(this);
      myAgents.put(trueName, agent);
      agent.connectTo("MagiquePlatformAgent");
      Agent.verbose(2, agent.getName() + " has been created");

      return (agent);
   }

   /**
    * dynamically creates an agent from its class an name. Agent is added to the
    * platform
    * 
    * @param agentClassName
    *           the class of the to be created agent
    * @param agentName
    *           the name of the crated agent
    * @param args
    *           the arguments for the constructor of the agent
    * 
    * @result the created agent (an <tt>AtomicAgent</tt>)
    */
   public AtomicAgent createAgent(String agentClassName, String agentName, Object[] args) throws AlreadyExistingAgentException {

      AtomicAgent agent = null;

      String trueName = fr.lifl.magique.util.Name.getShortName(agentName) + "@" + name;
      if (myAgents.contains(trueName)) {
         throw new AlreadyExistingAgentException(trueName);
      }
      try {
         Class agentClass = (Class) ((PlatformAgent) myAgents.get(PLATFORMMAGIQUEAGENTNAME + "@" + name)).getClass(agentClassName);

         /**
          * get classes of args of constructor and build args array needed for
          * constructor
          */
         Class[] argsClass = new Class[args.length + 1];
         Object[] constructorArgs = new Object[args.length + 1];
         argsClass[0] = Class.forName("java.lang.String");
         constructorArgs[0] = trueName;
         for (int i = 0; i < args.length; i++) {
            argsClass[i + 1] = args[i].getClass();
            constructorArgs[i + 1] = args[i];
         }

         /** construct the agent */
         java.lang.reflect.Constructor c = agentClass.getConstructor(argsClass);

         agent = (AtomicAgent) agentClass.getConstructor(argsClass).newInstance(constructorArgs);
      } catch (Exception e) {
         System.out.println("agent construction problem");
         e.printStackTrace();
      }
      agent.setPlatform(this);
      myAgents.put(trueName, agent);
      agent.connectTo("MagiquePlatformAgent");
      Agent.verbose(2, agent.getName() + " has been created");

      return (agent);
   }

   /**
    * given agent is removed from platform
    * 
    * @param agentName
    *           the name of the agent to be removed
    */
   public void removeAgent(String agentName) {
      Agent.verbose(2, agentName + " has leaved the platform");
      myAgents.remove(agentName);
   }

   /**
    * given message is treated : send to the recipient agent if he belongs to
    * this platform, or send (after connection if required) to the platform who
    * contains the recipient
    * 
    * @param msg
    *           the msg to be treated
    */
   public void treatMessage(PlatformMessage msg) {
      String recipient = msg.getRecipient();
      Message content = msg.getContent();
      if (myAgents.containsKey(recipient)) {
         ((AtomicAgent) myAgents.get(recipient)).getToDo().addMessage(content);
      } else { // send platform to other platform and "connect" to it not
               // already done
         connect(Name.noShortName(recipient)); // does nothing if already done
         sendMessage(Name.noShortName(recipient), msg);
      }
   }

   /**
    * used when a message is sent by anther platform
    * 
    * @param msg
    *           the sent message
    */
   public void haveAMessage(Message msg) {
      messages.addMessage(msg);
   }

   /**
    * send a message to the specified platform (via rmi)
    * 
    * @param to
    *           the recipient platform
    * @param msg
    *           the message
    */
   private boolean alreadyTried = false;

   public void sendMessage(String to, Message msg) {
      try {
         ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
         ObjectOutputStream ooStream = new ObjectOutputStream(baoStream);
         ooStream.writeObject(msg);
         byte[] bytesMsg = baoStream.toByteArray();
         ((Communicate) platformAgenda.get(to)).haveAMessage(bytesMsg);
      } catch (InvalidClassException e) {
         System.out.println("PLATFORM sendMessage ********  invalid");
         e.printStackTrace();
      } catch (NotSerializableException e) {
         System.out.println("PLATFORM sendMessage ********  notserializable");
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
         //	    System.out.println("PLATFORM sendMessage ******** classNotFound");
         if (!alreadyTried) {
            alreadyTried = true;
            String exceptionMessage = e.getMessage();
            //		System.out.println(" message = *"+exceptionMessage+"*");
            String className = exceptionMessage.substring(0, exceptionMessage.lastIndexOf('.'));
            //		System.out.println(" className = *"+className+"*");
            Agent.verbose(2, "**Platform sendMessage : need to give class byte code of " + className + " to " + to);
            platformAgent.giveClassArchive(className, "a@" + to);
            //		System.out.println("après give");
            sendMessage(to, msg);
         }
      } catch (RemoteException e) {

         System.out.println("*\n*\n");
         System.out.println("PLATFORM sendMessage ********  remoteException" + "\n++++++\n" + msg + "\n++++++\n");
         e.printStackTrace();
         System.out.println("*\n*\n");

         System.out.println(" disconnect from " + to);
         platformAgenda.remove(to);
         Enumeration agents = myAgents.keys();
         while (agents.hasMoreElements()) {
            String agentName = (String) agents.nextElement();
            if (!Name.getShortName(agentName).equals(PLATFORMMAGIQUEAGENTNAME)) {
               ((AtomicAgent) myAgents.get(agentName)).askNow("disconnectFromPlatform", new Object[] { to });
            }
         }

         //	    disconnectFrom(to);
         System.out.println(" reconnect from " + to);
         connect(to);
         sendMessage(to, msg);
         //	    e.printStackTrace();
      } catch (IOException e) {
         System.out.println("PLATFORM sendMessage ********  ioex");
         e.printStackTrace();
      } catch (Exception e) {
         System.out.println("************************************************************");
         e.printStackTrace();
         //  	    System.out.println("************************************************************");
         //  	    System.out.println(" to : "+ to+" - msg : "+msg);
         //  	    System.out.println(e.getMessage()+" local
         // :"+e.getLocalizedMessage());
         //  	    if (!alreadyTried) {
         //  		alreadyTried = true;
         //  		Object[] params = ((Request) ((PlatformMessage)
         // msg).getContent()).getParams();
         //  		for (int i = 0; i< params.length; i++) {
         //  		    System.out.println("nullExcep : give
         // "+params[i].getClass().getName());
         //  		    platformAgent.giveClassArchive(params[i].getClass().getName(),"a@"+to);
         //  		}
         //  				sendMessage(to,msg);
         //  	    }
         System.out.println("************************************************************");
         //	    e.printStackTrace();
      } finally {
         alreadyTried = false;
      }
   }

   /**
    * perform a connection (via rmi) to the given platform
    * 
    * @param platformName
    *           the platform to connect to
    */
   public void connect(String platformName) {
      if (platformName != name) {
         while (!platformAgenda.containsKey(platformName)) {
            System.out.println("Platform : connect to " + platformName);
            server.connect(platformName);
         }
      }
   }

   /**
    * disconnect from all otherplatfroms this platform is connected to
    *  
    */
   public void disconnectFromAll() {
      Enumeration p = platformAgenda.keys();
      while (p.hasMoreElements()) {
         disconnectFrom((String) p.nextElement());
      }
   }

   /**
    * disconnects from a given plaform
    * 
    * @param platformName
    *           the platform to disconnect from
    */
   public void disconnectFrom(String platformName) {
      try {
         ((Connect) platformAgenda.get(platformName)).disconnect(name);
         System.out.println(name + " disconnected from platform");
         Enumeration agents = myAgents.keys();
         while (agents.hasMoreElements()) {
            String agentName = (String) agents.nextElement();
            if (!Name.getShortName(agentName).equals(PLATFORMMAGIQUEAGENTNAME)) {
               ((AtomicAgent) myAgents.get(agentName)).perform("disconnectFromPlatform", new Object[] { platformName });
            }
         }
         //	    platformAgenda.remove(platformName);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * stops the platform by killing all agents located on it and stopping it.
    * Note : the rmiregistry is killed to.
    */
   public void stop() {
      Enumeration agents = myAgents.keys();
      while (agents.hasMoreElements()) {
         String agentName = (String) agents.nextElement();
         if (!Name.getShortName(agentName).equals(PLATFORMMAGIQUEAGENTNAME)) {
            System.out.println("      " + agentName + " going to be killed by platform");
            ((AtomicAgent) myAgents.get(agentName)).perform("disconnectAndDie");
            try {
               Thread.sleep(2000);
            } catch (Exception e) {
               e.printStackTrace();
            }
            System.out.println("      " + agentName + " killed by platform");
         }
      }
      ((AtomicAgent) myAgents.get((String) myAgents.keys().nextElement())).perform("die");
      disconnectFromAll();
      listener.halt();
      try {
         Thread.sleep(50);
      } catch (Exception e) {
         e.printStackTrace();
      }
      System.exit(1);
   }

   /**
    * test if the platform <em>hostname</em> is alive
    * 
    * @param platformName
    *           the platform to test
    * 
    * @return <em>Boolean.TRUE</em> iff alive
    */
   public Boolean ping(String platformName) {
      return server.ping(platformName);
   }

} // Platform

