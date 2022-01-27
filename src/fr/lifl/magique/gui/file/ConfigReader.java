/**
 * A class that read a configuration 
 *
 * @version 1.0 1/4/00
 * @author Fabien Niquet
 */
package fr.lifl.magique.gui.file;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import fr.lifl.magique.gui.draw.*;
import fr.lifl.magique.gui.descriptor.*;
import fr.lifl.magique.gui.file.NotFormatedException;


public class ConfigReader {
    private Vector VAgents=new Vector();    //les fenetres internes
    private Vector VLinks=new Vector();
    private Vector VSpecialLinks=new Vector();
/**
      * load a configuration
      * @param in a BufferedReader on a configuration
      */
        public void load (BufferedReader in) throws Exception{
	  StreamTokenizer st= new StreamTokenizer (in);
        st.ordinaryChar('_');
	st.ordinaryChar('=');
        while (true) {
          st.nextToken();
          if (st.ttype==StreamTokenizer.TT_EOF) {return;}
	  if (st.ttype != StreamTokenizer.TT_WORD) 
	      throw new NotFormatedException(st.lineno(),"General Bad Definition: Attempt DEFXXX");
          if (st.sval.equals("DEFAGENT")) {
            GraphicAgent agent=readAgent(st);
            VAgents.addElement(agent);
          }
          else if (st.sval.equals("DEFLINK")) {
            GraphicLink link=readLink(st,false);
            addLink(link.GetParent(),link.getChild(),false);
          }
          else if (st.sval.equals("DEFSPECIALLINK")) {
            GraphicLink link=readLink(st,true);
            addSpecialLink(link.GetParent(),link.getChild(),true);
          }
       }
      }
      private GraphicAgent readAgent(StreamTokenizer st) throws Exception {
        GraphicAgent newagent=new GraphicAgent(false,false);
        st.nextToken();
        newagent.setName(st.sval);
        while (true) {
          st.nextToken();
	  if (st.ttype != StreamTokenizer.TT_WORD&&st.ttype!='"') 
	      throw new NotFormatedException(st.lineno(),"Bad Definition in agent "+newagent.getName());
          if (st.sval.equals("CLASSE")) {
            st.nextToken();
	    if (st.ttype != StreamTokenizer.TT_WORD&&st.ttype!='"') 
		throw new NotFormatedException(st.lineno(),"Bad Definition in agent "+newagent.getName()+" : CLASSE");
            newagent.setClass(st.sval);
          }
          if (st.sval.equals("CLASSPATH")) {
            st.nextToken();
	    if (st.ttype != StreamTokenizer.TT_WORD&&st.ttype!='"') 
		throw new NotFormatedException(st.lineno(),"Bad Definition in agent "+newagent.getName()+" :CLASSPATH");
            newagent.setClassPath(st.sval.replace('+',File.separatorChar));
          }
          else if (st.sval.equals("ISCOLLAPSED")) {
            st.nextToken();
            if (st.sval.toLowerCase().equals("true"))
              newagent.setToCollapse();
          }
          else if (st.sval.equals("COMPUTER")) {
            st.nextToken();
	    if (st.ttype != StreamTokenizer.TT_WORD&&st.ttype!='"') 
		throw new NotFormatedException(st.lineno(),"Bad Definition in agent "+newagent.getName()+ " :COMPUTER");
            newagent.setComputer(st.sval);
          }
          else if (st.sval.equals("PORT")) {
            st.nextToken();
            if (st.ttype != StreamTokenizer.TT_NUMBER) 
		throw new NotFormatedException(st.lineno(),"Bad Definition in agent "+newagent.getName()+ " :PORT");        
          }
          else if (st.sval.equals("POSITION")) {
            int x,y;
            st.nextToken();
            if (st.ttype != StreamTokenizer.TT_NUMBER) 
		throw new NotFormatedException(st.lineno(),"Bad Definition in agent "+newagent.getName()+ " :POSITION");
            x=(int)st.nval;
            st.nextToken();
	    if (st.ttype != StreamTokenizer.TT_NUMBER) 
		throw new NotFormatedException(st.lineno(),"Bad Definition in agent "+newagent.getName()+ " :POSITION");	    
            y=(int) st.nval;
            newagent.changePosition(new Point(x,y));
          }
          else if(st.sval.equals("DEFSKILL")) {
             String skillname="",constructorname="", classpath="null";
             Vector arg=new Vector();
             st.nextToken();
             skillname=st.sval;
             st.nextToken();
             
             while (!st.sval.equals("ENDDEF")) {
                 if (st.sval.equals("CONSTRUCTOR")) {
                   st.nextToken();
                   constructorname=st.sval;
                   st.nextToken();
                 }
                 else if (st.sval.equals("CLASSPATH")) {
                   st.nextToken();
                   classpath=st.sval;
                   st.nextToken();
                 }
                 else if (st.sval.equals("ARG")) {
                   st.nextToken();
                   arg.addElement(st.sval);
                   st.nextToken();
                 }
                 else
                   throw new NotFormatedException(st.lineno(),"Bad Definition of Skill in agent "+newagent.getName());           
            }
            if (skillname.equals("")||constructorname.equals(""))
              throw new NotFormatedException(st.lineno(),"Bad Definition of Skill in agent "+newagent.getName());           
            SkillDescriptor skill=new SkillDescriptor(skillname,classpath,constructorname);
            for (int i=0;i<arg.size();i++)
              skill.setValueAt((String)arg.elementAt(i),i);
            newagent.addSkill(skill);
          }
                                
          else if(st.sval.equals("INIT")) {
            st.nextToken();
	    if (st.ttype != StreamTokenizer.TT_WORD&&st.ttype!='"') 
		throw new NotFormatedException(st.lineno(),"Bad Definition in agent "+newagent.getName()+" :INIT");
            String function=st.sval;
            st.nextToken();
	    if (st.ttype != StreamTokenizer.TT_WORD&&st.ttype!='"') 
		throw new NotFormatedException(st.lineno(),"Bad Definition in agent "+newagent.getName()+" :INIT(param)");
            String param=st.sval.replace('^','"');
            newagent.addInitialiser(function,param);
          }
          else if (st.sval.equals("ENDDEF")) {
            return newagent;
          }
        }
      }
      private GraphicLink readLink(StreamTokenizer st,boolean direct) throws IOException{
        String newparent=null;
        String newchild=null;

        while (true) {
          st.nextToken();
          if (st.sval.equals("PARENT")) {
            st.nextToken();
            newparent=st.sval;
          }
          else if(st.sval.equals("CHILD")) {
            st.nextToken();
            newchild=st.sval;
          }
          else if(st.sval.equals("TYPE")) {  //compatibilite ancienne version
            st.nextToken();
            
          }
        
          else if(st.sval.equals("ENDDEF")) {
            if(newparent!=null&&newchild!=null) {
              GraphicAgent agentChild=null;
              GraphicAgent agentParent=null;
              for (Enumeration e=VAgents.elements();e.hasMoreElements();) {
                GraphicAgent tmp=(GraphicAgent) e.nextElement();
                if (tmp.getName().equals(newparent))
                  agentParent=tmp;
                if (tmp.getName().equals(newchild))
                  agentChild=tmp;
              }
              if ((agentParent!=null)&&(agentChild!=null)) 
                return new GraphicLink(agentParent,agentChild,direct);
            }          
	    throw new NotFormatedException(st.lineno(),"Bad Definition in Link : Child or Parent not Defined");

  
          }
          else
	      throw new NotFormatedException(st.lineno(),"Bad Definition in Link : Child or Parent not Defined");            
        }              
    }
 /**
   * Returns the agents of a configuration
   *
   * @return a Vector that contains the GraphicAgent of a configuration
   */

  public Vector getAgents() {return VAgents;}
  /**
   * Returns the SpecialLinks of a configuration
   *
   * @return a Vector that contains the specials GraphicLink of a configuration
   */

   public Vector getSpecialLinks () {
     return VSpecialLinks;
   }

 /**
   * Returns the Links of a configuration
   *
   * @return a Vector that contains the GraphicLink of a configuration
   */
   public Vector getLinks () {
     return VLinks;
   }
 /**
   * Test if the configuration is ready to launch 
   *
   * @return true if the configuration is ready to launch.
   */
    public boolean isReadyToLaunch() throws IOException{
 
	for (Enumeration e=VAgents.elements();e.hasMoreElements();) {
	    GraphicAgent agent=(GraphicAgent) e.nextElement();
	   
	    if (agent.getClasse().equals("")) 
		throw new LaunchAgentException(agent,"Classe not defined");
	    if (agent.getComputer().equals(""))
		throw new LaunchAgentException(agent,"Computer not defined");
            Vector V1=agent.getSkills();
	    if (V1!=null&&!V1.isEmpty()) {
		for (Enumeration f=V1.elements();f.hasMoreElements();) {
		    if (!((SkillDescriptor) f.nextElement()).isValide())
			throw new LaunchAgentException(agent,"Skill not Valide"); 
		}
	    }
	}	
	return true;
    }
    
    private void addSpecialLink(GraphicAgent target, GraphicAgent source,boolean special) {
      GraphicLink newlink; Vector SpLink,SLink;
      SpLink=target.getSpecialChildren();
      SLink=target.getChildren();
      if (source.isAParent(target)) 
    	  return;
    	if (!SpLink.contains(source) && !SLink.contains(source)) {
        newlink = new GraphicLink(target, source, special);
        if (target.addSpecialChild(source)) {
          source.addSpecialParent(target);
          VSpecialLinks.addElement(newlink);
        }
      }
    }

    private void addLink(GraphicAgent target,GraphicAgent source,  boolean special) {
      GraphicLink newlink, linkpred, detruire = null;
      if (source.isAParent(target)) 
    	  return;
      if (source.hasAlreadyParent()) {
      	for (int j=0; j < VLinks.size(); j++) {
        	linkpred=(GraphicLink) VLinks.elementAt(j);
          if (linkpred.getChild() == source) {
          	linkpred.delete(); detruire = linkpred;
          }              
        }
        VLinks.removeElement(detruire);
      }
      if (target != source) {          
      	newlink = new GraphicLink(target,source,special);
        if (target.addChild(source)) {
          Vector SpChild = target.getSpecialChildren();
          GraphicLink ltmp;
          if (SpChild.contains(source)) {
          	target.removeSpecialChild(source);
            source.removeSpecialParent(target);
            boolean search=true; int compteur=0;
            while (search && compteur < VSpecialLinks.size()) {
            	ltmp = (GraphicLink) VSpecialLinks.elementAt(compteur);
              search = !ltmp.equals(newlink);
              if (!search) VSpecialLinks.remove(ltmp);
              else compteur++;
            }
          }
         source.setParent(target); VLinks.addElement(newlink);
       }          
     }    
    }

 }

