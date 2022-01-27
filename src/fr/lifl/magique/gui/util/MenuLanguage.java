/*
 * @(#)ManuLanguage.java	1.0 04/05/99
 *
 * Copyright 1999 by Nadir Doghmane & Niquet Fabien
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Nadir Doghmane & Niquet Fabien("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Nadir Doghmane & Niquet Fabien.
 */

package fr.lifl.magique.gui.util;
import java.util.*;

/**
 * A class that formats the menu in a specific language
 *
 * @version 1.0 04/05/99
 * @author Nadir Doghmane
 * @author Fabien Niquet
 */
public class MenuLanguage {
  
  /**
   * A bundle to gets the language , reading the Language.properties file
   */
	private static ResourceBundle Language;

  /**
   * A bundle to gets the Menu format , reading the MenuLanguage.properties file
   */
	private static ResourceBundle MenuLanguage;
	
  /**
   * The lowercase two-letter ISO-639 code
   */
	private String language;
	
  /**
   * The uppercase two-letter ISO-3166 code
   */
	private String country;
	
	private Locale locale;
	
  /**
   * Constructs a new MenuLanguage
   */
  public MenuLanguage() {
		try {
    	Language = ResourceBundle.getBundle("Properties"+System.getProperty("file.separator")+"Language");
    	language = Language.getString("language"); country =  Language.getString("country");
    	locale = new Locale(language,country);
    	MenuLanguage = ResourceBundle.getBundle("Properties"+System.getProperty("file.separator")+"Language"+
    								 System.getProperty("file.separator")+"MenuLanguage",locale);
    }
   	catch (MissingResourceException mre) {}
 	}
	
  /**
   * Returns the menu of this application in the specified language
   */
	public String[] getMenuLanguage() {
		String[] menu = new String[55];
		try {    	
    	menu[0] = MenuLanguage.getString("File");
			menu[1] = MenuLanguage.getString("New");
			menu[2] = MenuLanguage.getString("Load");
			menu[3] = MenuLanguage.getString("Add");
			menu[4] = MenuLanguage.getString("Save");
			menu[5] = MenuLanguage.getString("SaveAs");
			menu[6] = MenuLanguage.getString("Exit");

			menu[7] = MenuLanguage.getString("View");
			menu[8] = MenuLanguage.getString("ShowAgents");
			menu[9] = MenuLanguage.getString("ShowComputers");
			menu[10] = MenuLanguage.getString("ShowClasses");
			menu[11] = MenuLanguage.getString("ShowProperties");

			menu[12] = MenuLanguage.getString("Options");

			menu[13] = MenuLanguage.getString("Colors");
			menu[14] = MenuLanguage.getString("BackgroundColor");
			menu[15] = MenuLanguage.getString("AgentColor");
			menu[16] = MenuLanguage.getString("FontColor");
			menu[17] = MenuLanguage.getString("LinkColor");
			menu[54] = MenuLanguage.getString("DirectLink");			
			menu[18] = MenuLanguage.getString("SelectionColor");

			menu[19] = MenuLanguage.getString("LookAndFeel");
			menu[20] = MenuLanguage.getString("JavaLookAndFeel");
			menu[21] = MenuLanguage.getString("MotifLookAndFeel");
			menu[22] = MenuLanguage.getString("WindowsLookAndFeel");

			menu[23] = MenuLanguage.getString("Icons");
			menu[24] = MenuLanguage.getString("SetIconDirectory");
			menu[25] = MenuLanguage.getString("ShowAgentsIcons");
			menu[26] = MenuLanguage.getString("SetDefaultIcons");
			menu[27] = MenuLanguage.getString("SetSmallIcons");
			menu[28] = MenuLanguage.getString("SetBigIcons");
			menu[29] = MenuLanguage.getString("SetAllIconsAct");
			menu[30] = MenuLanguage.getString("SetAllIconsInact");

			menu[31] = MenuLanguage.getString("ShowAgentName");
			menu[32] = MenuLanguage.getString("HideAgentName");

			menu[33] = MenuLanguage.getString("SaveOptions");			
			
			menu[34] = MenuLanguage.getString("Freeze");
			menu[35] = MenuLanguage.getString("FreezeAll");
			menu[36] = MenuLanguage.getString("Unfreeze");
			menu[37] = MenuLanguage.getString("UnfreezeAll");

			menu[38] = MenuLanguage.getString("ShowClassMethod");
			menu[39] = MenuLanguage.getString("SetAgentPort");
			

			menu[40] = MenuLanguage.getString("Create");
			menu[41] = MenuLanguage.getString("Delete");
			menu[42] = MenuLanguage.getString("Organise");
			menu[43] = MenuLanguage.getString("Execute");
			
			menu[44] = MenuLanguage.getString("Title");
			menu[45] = MenuLanguage.getString("Comp");
			menu[46] = MenuLanguage.getString("Classes");
			menu[47] = MenuLanguage.getString("Properties");
			menu[48] = MenuLanguage.getString("NewConf");
			menu[49] = MenuLanguage.getString("Browse");

			menu[50] = MenuLanguage.getString("Name");
			menu[51] = MenuLanguage.getString("Class");
			menu[52] = MenuLanguage.getString("Computer");
			menu[53] = MenuLanguage.getString("AgentPort");			
		}
		catch (Throwable mre) {
			menu[0] = "File";
			menu[1] = "New";
			menu[2] = "Load";
			menu[3] = "Add";
			menu[4] = "Save";
			menu[5] = "Save As...";
			menu[6] = "Exit";

			menu[7] = "View";
			menu[8] = "Show Agents Tree";
			menu[9] = "Show Computers";
			menu[10] = "Show Agents Classes";
			menu[11] = "Show Agents Properties";

			menu[12] = "Options";

			menu[13] = "Colors";
			menu[14] = "Background Color";
			menu[15] = "Agent Color";
			menu[16] = "Font Color";
			menu[17] = "Link Color";
			menu[54] = "Direct Link Color";			
			menu[18] = "Selection Color";

			menu[19] = "Look And Feel";
			menu[20] = "Java Look And Feel";
			menu[21] = "Motif Look And Feel";
			menu[22] = "Windows Look And Feel";

			menu[23] = "Icons";
			menu[24] = "Set Icon Directory...";
			menu[25] = "Show Agents Icons";
			menu[26] = "Set Default Icons Active";
			menu[27] = "Set Small Icons Active";
			menu[28] = "Set Big Icons Active";
			menu[29] = "Set All Icons Active";
			menu[30] = "Set All Icons Inactive";

			menu[31] = "Show Agents Name";
			menu[32] = "Hide Agents Name";
			
			menu[33] = "Save Options";
			
			menu[34] = "Freeze";
			menu[35] = "Unfreeze";
			menu[36] = "Freeze All";
			menu[37] = "Unfreeze All";

			menu[38] = "Show Class Methods";
			menu[39] = "Set Agent Port";
			
			menu[40] = "Create";
			menu[41] = "Delete";
			menu[42] = "Organise";
			menu[43] = "Execute";
			
			menu[44] = "Multi-Agents System Thrower";
			menu[45] = "Computers";
			menu[46] = "Agents Classes";
			menu[47] = "Agents Properties";
			menu[48] = "New configuration";
			menu[49] = "Browse...";
			
			menu[50] = "Name";
			menu[51] = "Class";
			menu[52] = "Computer";
			menu[53] = "Agent Port";
		}
		return menu;
	}

}
