/**
 * DefaultSkill.java
 *
 *
 * Created: Mon Nov 22 14:44:26 1999
 *
 * @author Jean-Christophe Routier
 * @version
 */

package fr.lifl.magique.skill;

import fr.lifl.magique.*;
import fr.lifl.magique.platform.*;
import fr.lifl.magique.util.*;

/**
 * This class wraps the main methods of a magique agent, then you can use in the
 * skill all these methods without having to add the <tt>getMyAgent()</tt>
 * before (eg. use perform(..) instead of (getMyAgent.perform())) This allows to
 * lighten the code.
 */
public class MagiqueDefaultSkill implements Skill {

   /**
    * This is to keep a reference on the agent this service is belonging to
    */
   protected final Agent myAgent;

   /**
    * @param myAgent
    *           is to keep a reference on the agent this service is belonging to
    */
   public MagiqueDefaultSkill(Agent myAgent) {
      this.myAgent = myAgent;
   }

   /**
    * returns the reference on the agent this service is belonging to
    * 
    * @return the reference on the agent this service is belonging to
    */
   protected Agent getMyAgent() {
      return (Agent) myAgent;
   }

   /**
    * get my agent platform
    * 
    * @return my platform
    */
   protected Platform getPlatform() {
      return (Platform) askNow("getPlatform");
   }

   protected Team getMyTeam() {
      return (Team) askNow("getMyTeam");
   }

   protected String getMyBoss() {
      return (String) askNow("getMyBoss");
   }

   protected boolean isBoss() {
      return ((Boolean) askNow("isBoss")).booleanValue();
   }

   protected boolean isBigBoss() {
      return ((Boolean) askNow("isBigBoss")).booleanValue();
   }

   protected void removeFromMyTeam(String agentName) {
      askNow("removeFromMyTeam", new Object[] { agentName });
   }

   protected void addToMyTeam(String agent, TeamInfo info) {
      askNow("addToMyTeam", new Object[] { agent, info });
   }

   protected void connectToBoss(String bossName) {
      myAgent.connectToBoss(bossName);
   }

   // broadcast
   public void broadcastToBasis(Request request) {
      myAgent.broadcastToBasis(request);
   }

   public void broadcastToAll(Request request) {
      myAgent.broadcastToAll(request);
   }

   //atomic agent part

   protected String getName() {
      return myAgent.getName();
   }

   protected Agenda getAgenda() {
      return myAgent.getAgenda();
   }

   protected void connectTo(String agentName) {
      myAgent.connectTo(agentName);
   }

   public String addAgenda(String theOther) {
      return myAgent.addAgenda(theOther);
   }

   // skills
   public void addSkill(String skillClassName) throws SkillAlreadyAcquiredException {
      ((AtomicAgent) myAgent).addSkill(skillClassName);
   }

   public void addSkill(String skillClassName, Object[] args) throws SkillAlreadyAcquiredException {
      myAgent.addSkill(skillClassName, args);
   }

   public void addSkill(String skillClassName, boolean flag) throws SkillAlreadyAcquiredException {
      if (flag) {
         myAgent.addSkill(skillClassName, new Object[] { myAgent });
      } else {
         ((AtomicAgent) myAgent).addSkill(skillClassName);
      }
   }

   public void addSkill(String skillClassName, boolean flag, Object[] args) throws SkillAlreadyAcquiredException {
      if (flag) {
         Object[] nArgs = new Object[args.length + 1];
         nArgs[0] = myAgent;
         for (int i = 0; i < args.length; i++) {
            nArgs[i + 1] = args[i];
         }
         myAgent.addSkill(skillClassName, nArgs);
      } else {
         myAgent.addSkill(skillClassName, args);
      }
   }

   public void removeSkill(String skillName) throws SkillNotKnownException {
      myAgent.removeSkill(skillName);
   }

   public void removeSkillFromClassName(String skillClassName) throws SkillNotKnownException {
      myAgent.removeSkillFromClassName(skillClassName);
   }

   //
   // perform, ask, askNow and concurrentAsk wrapper
   //

   //
   // perform
   //
   protected void perform(Request question) {
      myAgent.perform(question);
   }

   protected void perform(String text) {
      myAgent.perform(text);
   }

   protected void perform(String text, Object[] params) {
      myAgent.perform(text, params);
   }

   protected void perform(String text, Object param1) {
      myAgent.perform(text, param1);
   }

   protected void perform(String text, Object param1, Object param2) {
      myAgent.perform(text, param1, param2);
   }

   protected void perform(String text, Object param1, Object param2, Object param3) {
      myAgent.perform(text, param1, param2, param3);
   }

   protected void perform(String text, Object param1, Object param2, Object param3, Object param4) {
      myAgent.perform(text, param1, param2, param3, param4);
   }

   protected void perform(String to, Request question) {
      myAgent.perform(to, question);
   }

   protected void perform(String to, String text) {
      myAgent.perform(to, text);
   }

   protected void perform(String to, String text, Object[] params) {
      myAgent.perform(to, text, params);
   }

   protected void perform(String to, String text, Object param1) {
      myAgent.perform(to, text, param1);
   }

   protected void perform(String to, String text, Object param1, Object param2) {
      myAgent.perform(to, text, param1, param2);
   }

   protected void perform(String to, String text, Object param1, Object param2, Object param3) {
      myAgent.perform(to, text, param1, param2, param3);
   }

   protected void perform(String to, String text, Object param1, Object param2, Object param3, Object param4) {
      myAgent.perform(to, text, param1, param2, param3, param4);
   }

   //
   // ask
   //
   protected Request ask(Request question) {
      return myAgent.ask(question);
   }

   protected Request ask(String text) {
      return myAgent.ask(text);
   }

   protected Request ask(String text, Object[] params) {
      return myAgent.ask(text, params);
   }

   protected Request ask(String text, Object param1) {
      return myAgent.ask(text, param1);
   }

   protected Request ask(String text, Object param1, Object param2) {
      return myAgent.ask(text, param1, param2);
   }

   protected Request ask(String text, Object param1, Object param2, Object param3) {
      return myAgent.ask(text, param1, param2, param3);
   }

   protected Request ask(String text, Object param1, Object param2, Object param3, Object param4) {
      return myAgent.ask(text, param1, param2, param3, param4);
   }

   protected Request ask(String to, Request question) {
      return myAgent.ask(to, question);
   }

   protected Request ask(String to, String text) {
      return myAgent.ask(to, text);
   }

   protected Request ask(String to, String text, Object[] params) {
      return myAgent.ask(to, text, params);
   }

   protected Request ask(String to, String text, Object param1) {
      return myAgent.ask(to, text, param1);
   }

   protected Request ask(String to, String text, Object param1, Object param2) {
      return myAgent.ask(to, text, param1, param2);
   }

   protected Request ask(String to, String text, Object param1, Object param2, Object param3) {
      return myAgent.ask(to, text, param1, param2, param3);
   }

   protected Request ask(String to, String text, Object param1, Object param2, Object param3, Object param4) {
      return myAgent.ask(to, text, param1, param2, param3, param4);
   }

   //
   // askNow
   //
   protected Object askNow(Request question) {
      return myAgent.askNow(question);
   }

   protected Object askNow(String text) {
      return myAgent.askNow(text);
   }

   protected Object askNow(String text, Object[] params) {
      return myAgent.askNow(text, params);
   }

   protected Object askNow(String text, Object param1) {
      return myAgent.askNow(text, param1);
   }

   protected Object askNow(String text, Object param1, Object param2) {
      return myAgent.askNow(text, param1, param2);
   }

   protected Object askNow(String text, Object param1, Object param2, Object param3) {
      return myAgent.askNow(text, param1, param2, param3);
   }

   protected Object askNow(String text, Object param1, Object param2, Object param3, Object param4) {
      return myAgent.askNow(text, param1, param2, param3, param4);
   }

   protected Object askNow(String to, Request question) {
      return myAgent.askNow(to, question);
   }

   protected Object askNow(String to, String text) {
      return myAgent.askNow(to, text);
   }

   protected Object askNow(String to, String text, Object[] params) {
      return myAgent.askNow(to, text, params);
   }

   protected Object askNow(String to, String text, Object param1) {
      return myAgent.askNow(to, text, param1);
   }

   protected Object askNow(String to, String text, Object param1, Object param2) {
      return myAgent.askNow(to, text, param1, param2);
   }

   protected Object askNow(String to, String text, Object param1, Object param2, Object param3) {
      return myAgent.askNow(to, text, param1, param2, param3);
   }

   protected Object askNow(String to, String text, Object param1, Object param2, Object param3, Object param4) {
      return myAgent.askNow(to, text, param1, param2, param3, param4);
   }

   //
   // concurrentAsk
   //
   protected Request concurrentAsk(Request question) {
      return myAgent.concurrentAsk(question);
   }

   protected Request concurrentAsk(String text) {
      return myAgent.concurrentAsk(text);
   }

   protected Request concurrentAsk(String text, Object[] params) {
      return myAgent.concurrentAsk(text, params);
   }

   protected Request concurrentAsk(String text, Object param1) {
      return myAgent.concurrentAsk(text, param1);
   }

   protected Request concurrentAsk(String text, Object param1, Object param2) {
      return myAgent.concurrentAsk(text, param1, param2);
   }

   protected Request concurrentAsk(String text, Object param1, Object param2, Object param3) {
      return myAgent.concurrentAsk(text, param1, param2, param3);
   }

   protected Request concurrentAsk(String text, Object param1, Object param2, Object param3, Object param4) {
      return myAgent.concurrentAsk(text, param1, param2, param3, param4);
   }

   //
   // requests = order and question
   //
   protected Request createOrder(String text) {
      return myAgent.createOrder(text);
   }

   protected Request createOrder(String text, Object[] params) {
      return myAgent.createOrder(text, params);
   }

   protected Request createOrder(String text, Object param1) {
      return myAgent.createOrder(text, param1);
   }

   protected Request createOrder(String text, Object param1, Object param2) {
      return myAgent.createOrder(text, param1, param2);
   }

   protected Request createOrder(String text, Object param1, Object param2, Object param3) {
      return myAgent.createOrder(text, param1, param2, param3);
   }

   protected Request createOrder(String text, Object param1, Object param2, Object param3, Object param4) {
      return myAgent.createOrder(text, param1, param2, param3, param4);
   }

   protected Request createQuestion(String text) {
      return myAgent.createQuestion(text);
   }

   protected Request createQuestion(String text, Object[] params) {
      return myAgent.createQuestion(text, params);
   }

   protected Request createQuestion(String text, Object param1) {
      return myAgent.createQuestion(text, param1);
   }

   protected Request createQuestion(String text, Object param1, Object param2) {
      return myAgent.createQuestion(text, param1, param2);
   }

   protected Request createQuestion(String text, Object param1, Object param2, Object param3) {
      return myAgent.createQuestion(text, param1, param2, param3);
   }

   protected Request createQuestion(String text, Object param1, Object param2, Object param3, Object param4) {
      return myAgent.createQuestion(text, param1, param2, param3, param4);
   }

   // gestion des reponses
   public boolean isAnswerReceived(Request question) {
      return myAgent.isAnswerReceived(question);
   }

   public Object returnValue(Request question) {
      return myAgent.returnValue(question);
   }

   public Answer returnAnswer(Request question) {
      return myAgent.returnAnswer(question);
   }

}
