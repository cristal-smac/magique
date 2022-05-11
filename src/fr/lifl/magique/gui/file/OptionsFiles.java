/*
 * @(#)OptionsFiles.java	1.0 04/05/99
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

package fr.lifl.magique.gui.file;

import java.awt.*;
import java.io.File;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

/**
 * A class that sets the options defined by the user in the LanceurAgents.properties file
 *
 * @author Nadir Doghmane
 * @author Fabien Niquet
 * @version 1.0 04/05/99
 */
public class OptionsFiles {

    /**
     * The options bundle
     */
    private static ResourceBundle options;

    /**
     * The dimension of the user screen
     */
    private final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();

    /**
     * A string to get the differents options
     */
    private String option;


    /**
     * Constructs a new OptionsFiles
     */
    public OptionsFiles() {
        try {
            options = ResourceBundle.getBundle("Properties" + System.getProperty("file.separator") + "LanceurAgents");
        } catch (MissingResourceException mre) {
        }
    }

    public OptionsFiles(String path) {
        try {
            options = ResourceBundle.getBundle(path + "LanceurAgents");
        } catch (MissingResourceException mre) {
        }
    }


    /**
     * Returns the icons directory
     */
    public String getIconDirectory() {
        try {
            option = options.getString("IconDirectory");
            return option.replace('+', File.separatorChar);
        } catch (Throwable mre) {
            return "Images";
        }
    }


    public String getFileDirectory() {
        try {
            option = options.getString("FileDirectory");
            return option.replace('+', File.separatorChar);
        } catch (Throwable mre) {
            return ".";
        }
    }


    /**
     * Returns the agents classes path
     */
    public String getAgentClassesPath() {
        try {
            option = options.getString("AgentClassesPath");
            option = option.replace('+', File.separatorChar);
            File verify = new File(option);
            if (!verify.exists()) return ".";
            if (!verify.isDirectory()) return ".";
            return option;
        } catch (Throwable mre) {
            return ".";
        }
    }

    public String getSkillClassesPath() {
        try {
            option = options.getString("SkillClassPath");
            option = option.replace('+', File.separatorChar);
            File verify = new File(option);
            if (!verify.exists()) return ".";
            if (!verify.isDirectory()) return ".";
            return option;
        } catch (Throwable mre) {
            return ".";
        }
    }


    /**
     * Returns the application colors
     */
    public Color[] getColors() {
        Color[] colors = new Color[6];
        try {
            option = options.getString("BackgroundColor");
            colors[0] = parseColorList(option);
            option = options.getString("AgentColor");
            colors[1] = parseColorList(option);
            option = options.getString("FontColor");
            colors[2] = parseColorList(option);
            option = options.getString("LinkColor");
            colors[3] = parseColorList(option);
            option = options.getString("DirectLink");
            colors[4] = parseColorList(option);
            option = options.getString("SelectionColor");
            colors[5] = parseColorList(option);
        } catch (Throwable mre) {
            colors[0] = new Color(80, 170, 170);
            colors[1] = new Color(0, 100, 100);
            colors[2] = Color.white;
            colors[3] = Color.blue;
            colors[4] = Color.red;
            colors[5] = new Color(255, 255, 50);
        }
        return colors;
    }

    /**
     * Returns the application look&feel
     */
    public int getLookAndFeel() {
        try {
            option = options.getString("LookAndFeel");
            return Integer.valueOf(option).intValue();
        } catch (Throwable mre) {
            return 1;
        }
    }

    /**
     * Returns a boolean to know if the AgentsTree frame is shown
     */
    public boolean getShowAgentsTree() {
        try {
            option = options.getString("ShowAgentsTree");
            return Boolean.valueOf(option).booleanValue();
        } catch (Throwable mre) {
            return true;
        }
    }

    public boolean getShowExecution() {
        try {
            option = options.getString("ShowExecution");
            return Boolean.valueOf(option).booleanValue();
        } catch (Throwable mre) {
            return false;
        }
    }


    /**
     * Returns a boolean to know if the Computers frame is shown
     */
    public boolean getShowComputers() {
        try {
            option = options.getString("ShowComputers");
            return Boolean.valueOf(option).booleanValue();
        } catch (Throwable mre) {
            return true;
        }
    }

    public boolean getArrowMode() {
        try {
            option = options.getString("ReverseArrow");
            return Boolean.valueOf(option).booleanValue();
        } catch (Throwable mre) {
            return false;
        }
    }


    /**
     * Returns a boolean to know if the AgentsClasses frame is shown
     */
    public boolean getShowAgentsClasses() {
        try {
            option = options.getString("ShowAgentsClasses");
            return Boolean.valueOf(option).booleanValue();
        } catch (Throwable mre) {
            return true;
        }
    }

    /**
     * Returns a boolean to know if the AgentsProperties frame is shown
     */
    public boolean getShowAgentsProperties() {
        try {
            option = options.getString("ShowAgentsProperties");
            return Boolean.valueOf(option).booleanValue();
        } catch (Throwable mre) {
            return true;
        }
    }

    /**
     * Returns a boolean to know if the agents icons are shown
     */
    public boolean getShowAgentIcons() {
        try {
            option = options.getString("ShowAgentIcons");
            return Boolean.valueOf(option).booleanValue();
        } catch (Throwable mre) {
            return true;
        }
    }

    /**
     * Returns a boolean to know if the agents default icons are activated
     */
    public boolean getSetDefaultIcons() {
        try {
            option = options.getString("SetDefaultIcons");
            return Boolean.valueOf(option).booleanValue();
        } catch (Throwable mre) {
            return false;
        }
    }

    /**
     * Returns a boolean to know if the small icons are activated
     */
    public boolean getSetSmallsIcons() {
        try {
            option = options.getString("SetSmallsIcons");
            return Boolean.valueOf(option).booleanValue();
        } catch (Throwable mre) {
            return false;
        }
    }

    /**
     * Returns a boolean to know if the agents names are shown
     */
    public boolean getShowAgentName() {
        try {
            option = options.getString("ShowAgentName");
            return Boolean.valueOf(option).booleanValue();
        } catch (Throwable mre) {
            return true;
        }
    }

    /**
     * Returns the bounds of the main frame
     */
    public int[] getMainBound() {
        int[] bounds = new int[4];
        try {
            option = options.getString("MainBound");
            bounds = parseIntList(option);
        } catch (Throwable mre) {
            bounds[0] = 0;
            bounds[1] = 0;
            bounds[2] = screen.width;
            bounds[3] = screen.height;
        }
        return bounds;
    }

    /**
     * Returns the bounds of the Computers frame
     */
    public int[] getComputersBound() {
        int[] bounds = new int[4];
        try {
            option = options.getString("ComputersBound");
            bounds = parseIntList(option);
            if (bounds[0] > screen.width - 20) throw new Throwable();
            if (bounds[1] > screen.height - 15) throw new Throwable();
        } catch (Throwable mre) {
            bounds[0] = 3;
            bounds[1] = screen.height / 2;
            bounds[2] = 175;
            bounds[3] = screen.height / 2 - 65;
        }
        return bounds;
    }

    public int[] getExecutionBound() {
        int[] bounds = new int[4];
        try {
            option = options.getString("ExecutionBound");
            bounds = parseIntList(option);
            if (bounds[0] > screen.width - 15) throw new Throwable();
            if (bounds[1] > screen.height - 15) throw new Throwable();
        } catch (Throwable mre) {
            bounds[0] = 0;
            bounds[1] = 0;
            bounds[2] = 400;
            bounds[3] = 400;
        }
        return bounds;
    }

    /**
     * Returns the bounds of the AgentsClasses frame
     */
    public int[] getClassesBound() {
        int[] bounds = new int[4];
        try {
            option = options.getString("ClassesBound");
            bounds = parseIntList(option);
            if (bounds[0] > screen.width - 20) throw new Throwable();
            if (bounds[1] > screen.height - 15) throw new Throwable();
        } catch (Throwable mre) {
            bounds[0] = screen.width - 207;
            bounds[1] = screen.height / 2;
            bounds[2] = 185;
            bounds[3] = screen.height / 2 - 65;
        }
        return bounds;
    }

    /**
     * Returns the bounds of the AgentsProperties frame
     */
    public int[] getPropertiesBound() {
        int[] bounds = new int[4];
        try {
            option = options.getString("PropertiesBound");
            bounds = parseIntList(option);
            if (bounds[0] > screen.width - 20) throw new Throwable();
            if (bounds[1] > screen.height - 15) throw new Throwable();
        } catch (Throwable mre) {
            bounds[0] = (screen.width - 300) / 2;
            bounds[1] = screen.height - 200;
            bounds[2] = 300;
            bounds[3] = 135;
        }
        return bounds;
    }

    /**
     * Returns the bounds of the AgentsTree frame
     */
    public int[] getTreeBound() {
        int[] bounds = new int[4];
        try {
            option = options.getString("TreeBound");
            bounds = parseIntList(option);
            if (bounds[0] > screen.width - 15) throw new Throwable();
            if (bounds[1] > screen.height - 15) throw new Throwable();
        } catch (Throwable mre) {
            bounds[0] = 0;
            bounds[1] = 0;
            bounds[2] = screen.width;
            bounds[3] = screen.height - 45;
        }
        return bounds;
    }

    public int[] getFluxBound() {
        int[] bounds = new int[4];
        try {
            option = options.getString("FluxBound");
            bounds = parseIntList(option);
            if (bounds[0] > screen.width - 20) throw new Throwable();
            if (bounds[1] > screen.height - 20) throw new Throwable();
        } catch (Throwable mre) {
            bounds[0] = 0;
            bounds[1] = 0;
            bounds[2] = 400;
            bounds[3] = 200;
        }
        return bounds;
    }


    /**
     * Returns the color represented by the specified string
     */
    private Color parseColorList(String StringList) {
        int i = 0;
        int[] v = new int[3];
        StringTokenizer tokenizer = new StringTokenizer(StringList, " ");
        while (tokenizer.hasMoreTokens())
            v[i++] = (Integer.valueOf(tokenizer.nextToken())).intValue();
        return new Color(v[0], v[1], v[2]);
    }

    /**
     * Returns an array of bounds represented by the specified string
     */
    private int[] parseIntList(String StringList) {
        int i = 0;
        int[] v = new int[4];
        StringTokenizer tokenizer = new StringTokenizer(StringList, " ");
        while (tokenizer.hasMoreTokens())
            v[i++] = (Integer.valueOf(tokenizer.nextToken())).intValue();
        return v;
    }

}
