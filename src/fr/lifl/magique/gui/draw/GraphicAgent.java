/*
 * @(#)GraphicAgent.java	1.0 04/05/99
 *
 * Copyright 1999 by Nadir Doghmane & Niquet Fabien

 */

package fr.lifl.magique.gui.draw;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import fr.lifl.magique.gui.descriptor.*;

/**
 * A class that is the graphic representation of an agent
 *
 * @version 1.0 04/05/99
 * @author Nadir Doghmane
 * @author Fabien Niquet
 */

public class GraphicAgent extends GraphicNode {

  /**
   * The string representing the name of this agent
   */
  private String name;
  private String exname;

  /**
   * The string representing the class of this agent
   */
  private String classe;

  /**
   * The string representing the class path of this agent
   */
  private String classpath;

  /**
   * The string representing the computer of this agent
   */
  private String computer;

  /**
   * A string representing the name which is shown in this agent graphic representation
   */
  private String showname = "";

  /**
   * A string representing the class which is shown in this agent graphic representation
   */
  private String shownclass = "";

    private String executionName="";

  /**
   * A string representing the computer which is shown in this agent graphic representation
   */
  private String showncomputer = "";

  /**
   * The integer representing the port of this agent
   */
  
  /**
   * An integer representing the shift of this agent name in the graphic representation
   */
  private int NameMoved;

  /**
   * An integer representing the shift of this agent class in the graphic representation
   */
  private int ClassMoved;

  /**
   * An integer representing the shift of this agent computer in the graphic representation
   */
  private int ComputerMoved;

  /**
   * The boolean to know whether or not this agent name is visible
   */
  private boolean NameVisible;

  /**
   * The boolean to know whether or not this agent uses a small icon
   */
  private boolean SmallIcon;

  /**
   * The boolean to know whether or not this agent name is freezed
   */
  private boolean isFreezed = false;

  /**
   * The boolean to know whether or not this agent icon is visible
   */
  private boolean isIconVisible;

  /**
   * The FontMetrics used in the application
   */
  private FontMetrics police; 

  /**
   * The dimension of this agent
   */
  private Dimension IconDimension = new Dimension(75,30);

  /**
   * A string to represent this agent icon (its filename or "")
   */
  private String IconDescription = "";  

  /**
   * The image representing this agent icon
   */
  private Image Icon; 

  /**
   * The vector containing this agent initialisers functions and its parameters
   */
  private Vector Initialisers = new Vector();
  private boolean isGraphic=true;
    private Vector skills=new Vector();
  
  
  /**
 	 * Creates a GraphicAgent
 	 *
 	 * @param showName a boolean to set if the name is shown or not
 	 */
 	public GraphicAgent(boolean showName) {
  	super(); NameVisible = showName; name = ""; classe = "fr.lifl.magique.Agent"; classpath = ""; computer = "";
        Icon = null; isIconVisible = false; SmallIcon = false;police=null;
  }
        public GraphicAgent(boolean showName,boolean isgraphic) {
  	super(); NameVisible = showName; name = ""; classe = "fr.lifl.magique.Agent"; classpath = ""; computer = "";
        Icon = null; isIconVisible = false; SmallIcon = false;police=null;
        isGraphic=isgraphic;
  }

  
  /**
 	 * Creates a GraphicAgent with the specified name and police
 	 *
 	 * @param name the name of this agent
 	 * @param police the font to use with this agent
 	 * @param showName a boolean to set if the name is shown or not
 	 */
 	public GraphicAgent(String name, FontMetrics Police, boolean showName) {
    super(); police = Police; NameVisible = showName;
    setName(name); classe = "fr.lifl.magique.Agent"; classpath = ""; computer = "";
  	Icon = null; isIconVisible = false; SmallIcon = false;
  }
  
  /**
 	 * Creates a GraphicAgent with the specified name ,police and dimension at the location P
 	 *
 	 * @param name the name of this agent
 	 * @param P this agent's center
 	 * @param d this agent's dimension
 	 * @param police the font to use with this agent
 	 * @param showName a boolean to set if the name is shown or not
 	 */
 	public GraphicAgent(String name, Point P, Dimension d, FontMetrics Police, boolean showName, boolean small) {
   	super(); changePosition(P); police = Police; NameVisible = showName;
   	setName(name); classe = "fr.lifl.magique.Agent"; classpath = ""; computer = "";
  	Icon = null; isIconVisible = false; SmallIcon = small; setDimension(d); 
  }
  /**
   * Returns the name of this agent
   *
   * @return this agent's name
   */
  public String getName() {return name;}
  public String getExName() {return exname;}
  
  /**
   * Returns the class of this agent
   *
   * @return this agent's class
   */
  public String getClasse() {return classe;}
  
  /**
   * Returns the class path of this agent
   *
   * @return this agent's class path
   */
  public String getClassPath() {return classpath;}
  

    public void setExecutionName(String name) {
	this.executionName=name;
    }
    public String getExecutionName() {
	return executionName;
    }
 public Image getIcon() {
return Icon; 
}
  /**
   * Returns the computer of this agent
   *
   * @return this agent's computer
   */
  public String getComputer() {return computer;}

  /**
   * Returns the computer port of this agent
   *
   * @return this agent's computer port
   */

  
  /**
 	 * Gets the initialiser function at the place i
 	 *
 	 * @param i the place of the initialiser function to get 
 	 * @return the initialiser function
 	 */
 	public String getInitialiserFunction(int i) {return ((Initialiser)Initialisers.elementAt(i)).getFunction();}
  public Vector getInitialiserFunctions () {
    Vector result=new Vector();
    for (Enumeration e=Initialisers.elements();e.hasMoreElements();)
      result.addElement(((Initialiser) e.nextElement()).getFunction());
    return result;
  }
  /**
 	 * Gets the argument of the initialiser function at the place i
 	 *
 	 * @param i the place of the initialiser function's argument to get 
 	 * @return the initialiser function's argument
 	 */
 	public String getInitialiserArgument(int i) {return ((Initialiser)Initialisers.elementAt(i)).getArgument();}
  
  /**
 	 * Gets the argument of the initialiser function equals to func
 	 *
 	 * @param func the initialiser function which argument we want
 	 * @return the initialiser function's argument
 	 */
 	public String getInitialiserArgument(String func) {
 		for (int i=0; i < Initialisers.size(); i++) {
 			if (((Initialiser)Initialisers.elementAt(i)).getFunction().equals(func))
 				return ((Initialiser)Initialisers.elementAt(i)).getArgument();
 		}
 		return "";
  }
    /**
     * Gets an enumeration of the Initialisers of this agent
     * @return an Enumeration of fr.lifl.magique.gui.descriptor.Initialiser.
     */

	public Enumeration getIninialisersObject () {
	  return Initialisers.elements();
     }
    
  /**
 	 * Gets the number of intializers functions
 	 *
 	 * @return the number of intializers functions
 	 */
 	public int getInitialisersNumber() {return Initialisers.size();}
  
   

  /**
 	 * Gets the icon of this node
 	 *
 	 * @return this node's icon
 	 */
 	public String getIconDescription() {return IconDescription;}
  
  /**
 	 * Gets the icon dimension of this node
 	 *
 	 * @return this node's icon dimension
 	 */
 	public Dimension getIconDimension() {return IconDimension;}
  
  /**
   * Sets the name of this agent to the specified one
   *
   * @param Name the new name of this agent
   */
  public void setName(String Name) {
   	name = Name;
        if (isGraphic) {
          showname = new Converter().show(name, police,rect.width-1);
          Namewidth = Math.max(IconDimension.width,police.stringWidth(showname));
          NameMoved = (rect.width-police.stringWidth(showname))/2;
        }
  }
  
  /**
   * Sets the class of this agent to the specified one
   *
   * @param Classe the new class of this agent
   */
  public void setClass(String Classe) {
   	classe = Classe;
	String classetmp=Classe;
	int sep=classe.lastIndexOf('.');
	
	if (sep!=-1)
	    classetmp=classetmp.substring(sep+1);
        if (isGraphic) {
          shownclass = new Converter().show(classetmp, police,rect.width-1);
          ClassMoved = (rect.width-police.stringWidth(shownclass))/2;
        }
//    Initialisers.removeAllElements();
	}
  public void resetInitialisers () {
    Initialisers.removeAllElements();
  }
  /**
   * Sets the class path of this agent to the specified one
   *
   * @param ClassPath the new class path of this agent
   */
  public void setClassPath(String ClassPath) {classpath = ClassPath;}
  
  /**
   * Sets the computer of this agent to the specified one
   *
   * @param Computer the new computer of this agent
   */
  public void setComputer(String Computer) {
	  computer = Computer;
            if (isGraphic) {
  	showncomputer = new Converter().show(computer, police,rect.width-1);
	  ComputerMoved = (rect.width-police.stringWidth(showncomputer))/2;
}
	}
	
	/**
   * Sets a new port for this agent
   *
   * @param p the new computer port of this agent
   */

 	
 	/**
 	 * Add an initializer function to this agent
 	 *
 	 * @param init the initializer function to this agent
 	 */
 	public void addInitialiser(String func, String arg) {
		boolean change = false;
 		for (int i=0; i < Initialisers.size(); i++) {
 			if (((Initialiser)Initialisers.elementAt(i)).getFunction().equals(func)) {
 				((Initialiser)Initialisers.elementAt(i)).setArgument(arg); change = true;
  			}
 		}
 		if (!change) Initialisers.add(new Initialiser(func,arg));
 	}
 		
 	/**
 	 * Sets the icon of this node to icon
 	 *
 	 * @param icon this node's icon
 	 */
 	public void setIcon(Image icon, String Description, Dimension d) {
 		Icon = icon; IconDescription = Description; IconDimension = d;
 	}
  
  /**
 	 * Sets the specified boolean to indicate whether or not this node's icon is shown
 	 *
 	 * @param IconActive the boolean to be set
 	 */
 	public void setIconVisible(boolean IconActive) {isIconVisible = IconActive;}
  
  /**
 	 * Sets the specified boolean to indicate whether or not this node's icon is small
 	 *
 	 * @param small the boolean to be set
 	 */
 	public void setSmallIcon(boolean small) {SmallIcon = small;}
  
  /**
   * Sets the new dimension of this agent
   *
   * @param d the new dimension of this agent
   */
  public void setDimension(Dimension d) {
        isGraphic=true;
   	rect = new Rectangle (new Point(Center.x-d.width/2, Center.y-d.height/2),d);
   	setName(name); setClass(classe); setComputer(computer);
  }
  
  /**
   * Sets the new font of this agent
   *
   * @param metric the new font of this agent
   */
  public void setMetrics (FontMetrics metric) {           
  	police = metric; setDimension(new Dimension(rect.width,rect.height));
	}

  /**
 	 * Sets the specified boolean to indicate whether or not this agent is freezed
 	 *
 	 * @param freeze the boolean to be set
 	 */
 	public void setFreeze(boolean freeze) {isFreezed = freeze;}
  
  /**
 	 * Sets the specified boolean to indicate whether or not this agent's name is shown
 	 *
 	 * @param show the boolean to be set
 	 */
 	public void showAgentName(boolean show) {NameVisible = show;}
	
	/**
 	 * Returns a boolean to indicate whether or not this node's icon is visible
 	 *
 	 * @return <code>true</code> if this agent's icon is visible
 	 *         <code>false</code> otherwise
 	 */
 	public boolean isIconVisible() {return isIconVisible;}
  
  /**
 	 * Returns a boolean to indicate whether or not this agent is freezed
 	 *
 	 * @return <code>true</code> if this agent is freezed
 	 *         <code>false</code> otherwise
 	 */
 	public boolean isFreezed() {return isFreezed;}
  
  /**
 	 * Draws this agent
 	 *
 	 * @param g1 the graphic context to use to paint
	 * @param selected a boolean to indicate whether or not this agent is selected
 	 * @param AColor the agent color
 	 * @param FColor the font color
 	 * @param SColor the selection color
 	 * @param BColor the backgroung color
 	 */
 	public void draw (Graphics g1, boolean selected, Color AColor, Color FColor, Color SColor, Color BColor) {
    if (isVisible) {
    	
    	Graphics2D g = (Graphics2D) g1;
    	Stroke save = g.getStroke();

    	// Line Selector
    	g.setColor(SColor); g.fillOval(Center.x-10,rect.y-10,20,22);
    	g.setColor(BColor); g.fillOval(Center.x-4,rect.y-8,8,7);

    	if (selected) {
	      g.setColor(SColor);
  	    g.fillRoundRect(rect.x-3,rect.y-3,rect.width+6,rect.height+6,6,6);
    	}
    	if (isIconVisible && Icon != null) {
       	g.setColor(SColor); 
       	g.fillRoundRect(rect.x-1,rect.y-1,rect.width+2,rect.height+2,4,4);
       	g.setColor(AColor);
       	g.fillRoundRect(rect.x,rect.y,rect.width,rect.height,4,4);
	      g.drawImage(Icon,rect.x+(rect.width-IconDimension.width)/2,rect.y,null);
  	    g.setColor(FColor);
    	  if (NameVisible && !SmallIcon) g.drawString(showname,rect.x+NameMoved,rect.y+rect.height-5);
    	}
    	else {
	   		g.setColor(SColor);
  	    g.fillRoundRect(rect.x-1,rect.y-1,rect.width+2,rect.height+2,4,4);
				g.setColor(AColor);
   			g.fillRoundRect(rect.x,rect.y,rect.width,rect.height,4,4);
   			g.setColor(FColor);
   			if (NameVisible) {
	   			g.drawString(showname,rect.x+NameMoved,rect.y+police.getMaxAscent()-2);
  	    	if ( 2*police.getMaxAscent()-3 < rect.height && !shownclass.equals("") )
    	    	g.drawString(shownclass, rect.x+ClassMoved,rect.y+police.getMaxAscent()*2-2);
	    	  if ( 3*police.getMaxAscent()-3 < rect.height && !showncomputer.equals("") )
  	    	  g.drawString(showncomputer, rect.x+ComputerMoved,rect.y+police.getMaxAscent()*3-2);
  	  	}	
  	  	else {
	  	  	if (!shownclass.equals(""))
  		  		g.drawString(shownclass, rect.x+ClassMoved,rect.y+police.getMaxAscent()-2);
	  	    if (!showncomputer.equals("") && 2*police.getMaxAscent()-3 < rect.height)
  	  	    g.drawString(showncomputer, rect.x+ComputerMoved,rect.y+police.getMaxAscent()*2-2);
  	  	}	
    	}
  
    	g.setColor(Color.white);
    	g.fillRect(Center.x-4,Center.y+rect.height/2-4,8,8);
    	g.setColor(Color.black);
	    g.drawRect(Center.x-4,Center.y+rect.height/2-4,8,8);
  	  if (isCollapsed) g.drawString("+",Center.x-3,Center.y+rect.height/2+5);
	    else g.drawString("-",Center.x-1,Center.y+rect.height/2+4);
  	}
  }
  
  /**
   * Deletes this GraphicAgent and its children
   */
    public boolean equals(GraphicAgent compare) {
	if(compare.getName().equals(name)&&compare.getComputer().equals(computer)&&compare.getClasse().equals(classe))
   
	    return true;
	return false;
    }
    public void deleteAll() {
  	for (int i=0; i < VChild.size(); i++)
	    ((Node) VChild.elementAt(i)).delete();
    }
	/**
   * Add a Skill to this agent 
   * @param desc the new Skill added to this agent
   */

    public void addSkill (SkillDescriptor desc) {
	skills.addElement(desc);
    }
	/**
   * remove a Skill to this agent 
   * @param desc the Skill to remove 
   */

    public void removeSkill (SkillDescriptor desc) {
	skills.removeElement(desc);
    }
	/**
   * gets all the Skills defined for this agent
   * @return a Vector of SkillDescriptor
   */

    public Vector getSkills() {
	return skills;
    }
  
    





}
