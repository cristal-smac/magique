/*
 * @(#)MultiAgentsFrame.java 1.0 04/05/99
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
package fr.lifl.magique.gui.theinterface;

import bsh.EvalError;
import fr.lifl.magique.AtomicAgent;
import fr.lifl.magique.gui.descriptor.Argument;
import fr.lifl.magique.gui.descriptor.LinkToDaemon;
import fr.lifl.magique.gui.descriptor.SkillDescriptor;
import fr.lifl.magique.gui.draw.GraphicAgent;
import fr.lifl.magique.gui.draw.GraphicLink;
import fr.lifl.magique.gui.draw.JProgressFrame;
import fr.lifl.magique.gui.execute.Deployement;
import fr.lifl.magique.gui.execute.FluxFrame;
import fr.lifl.magique.gui.execute.Tuple;
import fr.lifl.magique.gui.file.*;
import fr.lifl.magique.gui.network.PingNetwork;
import fr.lifl.magique.gui.tree.ComputerNode;
import fr.lifl.magique.gui.tree.TreeCreator;
import fr.lifl.magique.gui.tree.TreeOrganiser;
import fr.lifl.magique.gui.tree.TreeSaver;
import fr.lifl.magique.gui.util.MenuLanguage;
import fr.lifl.magique.gui.util.UniversalLoader;
import fr.lifl.magique.platform.Platform;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class MultiAgentsFrame extends JFrame {
    static int inc = 3;
    bsh.Interpreter Interprete;
    bsh.util.JConsole beanconsole;
    JProgressFrame progress;
    JDesktopPane desktop = new JDesktopPane();
    MenuLanguage language = new MenuLanguage();
    String[] lang = language.getMenuLanguage();
    String CurrentFile = lang[48], IconDirectory;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    MultiAgentsFrame edition;
    ComputersFrame Computers;
    AgentClassesFrame AgentClasses;
    AgentPropertiesFrame AgentProperties;
    AgentsTreeFrame AgentsTree;
    ExecutorFrame AgentsExecutor;
    FluxFrame IOframe;
    SkillFrame chooseSkill;
    JCheckBoxMenuItem[] cb = new JCheckBoxMenuItem[11];
    JRadioButtonMenuItem[] lf = new JRadioButtonMenuItem[3];
    int CurrentPlaf = 1;
    Vector VExecFrame = new Vector();
    Platform platform;
    // Object platform;
    String propertiespath;
    String filechooserdirectory = ".";

    /**
     * Creates a new MultiAgentsFrame
     *
     * @see javax.swing.JFrame
     */
    public MultiAgentsFrame(String commande) { // ,JProgressFrame progress) {
        super("");
        progress = new JProgressFrame();
        progress.setVisible(true);

        propertiespath = this.getClass().getResource("default.gif").toString();
        int index = propertiespath.indexOf(".jar!");
        if (index == -1) {
            propertiespath = "Properties" + System.getProperty("file.separator");
        } else {
            propertiespath = propertiespath.substring(0, index);
            index = propertiespath.lastIndexOf('/');
            if (index != -1)
                propertiespath = propertiespath.substring(0, index);
            index = propertiespath.lastIndexOf(':');
            propertiespath = propertiespath.substring(index + 1);
            propertiespath.replace('/', File.separatorChar);
            if (!propertiespath.endsWith(System.getProperty("file.separator")))
                propertiespath = propertiespath + System.getProperty("file.separator");
            propertiespath = propertiespath + "Properties" + System.getProperty("file.separator");
        }
        if (!new File(propertiespath).exists())
            new File(propertiespath).mkdir();
        setTitle(lang[44] + " - [" + lang[48] + "]");
        edition = this;
        this.progress = progress;
        progress.setText("Initializing Classes Frame");
        AgentClasses = new AgentClassesFrame();
        progress.addValue(inc);
        progress.setText("Initializing Properties Frame");
        AgentProperties = new AgentPropertiesFrame();
        progress.addValue(inc);
        progress.setText("Initializing Tree Frame");
        AgentsTree = new AgentsTreeFrame();
        progress.addValue(inc);
        progress.setText("Initializing Platform");

        // JC
        // System.out.println("|||||||||||||||||||| "+this.getClass().getClassLoader());
        // Object myPlatform =
        // fr.lifl.magique.Start.go("fr.lifl.magique.gui.Interface.MyPlatform");
        // try {
        // Method m =
        // fr.lifl.magique.util.ClassUtil.getMethod(myPlatform.getClass(),
        // "getPlatform",
        // new String[]{});
        // platform = m.invoke(myPlatform, new Object[]{});
        // }
        // catch (Exception e) {
        // e.printStackTrace();
        // }
        // AtomicAgent agentCreator = new AgentCreator();
        // platform.addAgent(agentCreator);

        platform = new Platform(8888); // 3333
        AtomicAgent agent = platform.createAgent("fr.lifl.magique.AtomicAgent", "AgentCreator");

        agent.addSkill(new fr.lifl.magique.gui.skills.ClassLoaderSkill(agent));
        agent.addSkill(new fr.lifl.magique.gui.skills.AgentCreatorSkill(agent));
        // fin JC

        beanconsole = new bsh.util.JConsole();
        Interprete = new bsh.Interpreter(beanconsole);
        new Thread(Interprete).start();
        // CHANGE 2004 setVariable remplac� par set
        try {
            Interprete.set("platform", platform);
        } catch (EvalError e) {
            e.printStackTrace();
        }
        progress.addValue(inc);
        progress.setText("Initializing Console Frame");
        progress.addValue(inc);
        IOframe = new FluxFrame(beanconsole);
        progress.addValue(inc);
        progress.setText("Initializing Computers Frame");
        progress.addValue(inc);
        Computers = new ComputersFrame();
        progress.setText("Initializing Execution Frame ");
        AgentsExecutor = new ExecutorFrame();
        progress.addValue(inc);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (Computers.hasChanged()) {
                    int choice = JOptionPane.showConfirmDialog(null, "Network has been edited, save?", "Warning",
                            JOptionPane.YES_NO_OPTION);

                    if (choice == JOptionPane.YES_OPTION)
                        Computers.saveNetwork();
                }

                if (AgentsTree.isModified()) {
                    int choice = JOptionPane.showConfirmDialog(null, "Save changes to the configuration ?", "Warning",
                            JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        if (CurrentFile.startsWith("New")) {
                            MagicFilesChooser chooser = new MagicFilesChooser(1, filechooserdirectory);
                            if (chooser.getSelectedFile() != null) {
                                filechooserdirectory = chooser.getCurrentDirectory().toString();
                                if (chooser.getSelectedFile().toString().endsWith(".magic"))
                                    AgentsTree.save(chooser.getSelectedFile().toString());
                                else
                                    AgentsTree.save(chooser.getSelectedFile().toString() + ".magic");
                                if (!AgentsExecutor.isVisible()) {

                                    System.exit(0);
                                }
                                setVisible(false);
                            }
                        } else {
                            if (!CurrentFile.endsWith("*")) {
                                if (new File(CurrentFile + ".bak").exists())
                                    new File(CurrentFile + ".bak").delete();
                                new File(CurrentFile).renameTo(new File(CurrentFile + ".bak"));
                                AgentsTree.save(CurrentFile);
                                if (!AgentsExecutor.isVisible()) {

                                    System.exit(0);
                                }
                                setVisible(false);
                            } else {
                                if (new File(CurrentFile.substring(0, CurrentFile.length() - 1) + ".bak").exists())
                                    new File(CurrentFile.substring(0, CurrentFile.length() - 1) + ".bak").delete();
                                new File(CurrentFile.substring(0, CurrentFile.length() - 1)).renameTo(new File(
                                        CurrentFile.substring(0, CurrentFile.length() - 1) + ".bak"));
                                AgentsTree.save(CurrentFile.substring(0, CurrentFile.length() - 1));
                                System.exit(0);
                            }
                        }
                    } else if (choice == JOptionPane.NO_OPTION) {
                        if (!AgentsExecutor.isVisible()) {

                            System.exit(0);
                        }
                        setVisible(false);

                    }
                } else if (!AgentsExecutor.isVisible()) {

                    System.exit(0);
                }
                setVisible(false);
                AgentsExecutor.setState(Frame.NORMAL);
            }

            public void windowDeiconified(WindowEvent e) {
                repaint();
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                requestFocus();
                setVisible(true);
                e.consume();
            }
        });

        progress.addValue(inc);
        desktop.add(AgentsTree, JLayeredPane.DEFAULT_LAYER);
        desktop.add(Computers, JLayeredPane.MODAL_LAYER);
        desktop.add(AgentClasses, JLayeredPane.MODAL_LAYER);
        desktop.add(AgentProperties, JLayeredPane.MODAL_LAYER);
        getContentPane().add(desktop);
        setJMenuBar(CreateMenuBar());
        chooseSkill = new SkillFrame(edition);
        progress.setText("Loading Options");
        progress.addValue(inc);
        loadOptions();
        progress.setText("Initializing Graphics");
        progress.addValue(inc);

        progress.setVisible(false);

        this.setSize(800, 600);

        if (commande.equals("exec")) {
            AgentsExecutor.setVisible(true);
        }

    }

    /**
     * Changes the title bar of the Interface
     */
    private void changeFrameTitle() {
        setTitle(lang[44] + " - [" + CurrentFile + "]");
    }

    /**
     * Returns the MenuBar of the Interface
     *
     * @return a MenuBar
     * @see javax.swing.JMenuBar
     */
    private JMenuBar CreateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu1 = menuBar.add(new JMenu(lang[0]));
        menu1.setMnemonic(lang[0].charAt(0));
        JMenuItem item1_1 = menu1.add(new JMenuItem(lang[1]));
        item1_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        item1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (AgentsTree.isModified()) {
                    int choice = JOptionPane.showConfirmDialog(null, "Save changes to the configuration ?");
                    if (choice == JOptionPane.YES_OPTION) {
                        MagicFilesChooser chooser = new MagicFilesChooser(1, filechooserdirectory);
                        if (chooser.getSelectedFile() != null) {
                            AgentsTree.save(chooser.getSelectedFile().toString());
                            AgentsTree.reset();
                            filechooserdirectory = chooser.getCurrentDirectory().toString();
                        }
                    } else if (choice == JOptionPane.NO_OPTION)
                        AgentsTree.reset();
                } else
                    AgentsTree.reset();
            }
        });
        JMenuItem item1_2 = menu1.add(new JMenuItem(lang[2]));
        item1_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        item1_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (AgentsTree.isModified()) {
                    int choice = JOptionPane.showConfirmDialog(null, "Save changes to the configuration ?");
                    if (choice == JOptionPane.YES_OPTION) {
                        if (CurrentFile.startsWith("New")) {
                            MagicFilesChooser chooser = new MagicFilesChooser(1, filechooserdirectory);
                            if (chooser.getSelectedFile() != null) {
                                filechooserdirectory = chooser.getCurrentDirectory().toString();
                                AgentsTree.save(chooser.getSelectedFile().toString());
                            }
                        } else {
                            if (!CurrentFile.endsWith("*")) {
                                if (new File(CurrentFile + ".bak").exists())
                                    new File(CurrentFile + ".bak").delete();
                                new File(CurrentFile).renameTo(new File(CurrentFile + ".bak"));
                                AgentsTree.save(CurrentFile);
                            } else {
                                if (new File(CurrentFile.substring(0, CurrentFile.length() - 1) + ".bak").exists())
                                    new File(CurrentFile.substring(0, CurrentFile.length() - 1) + ".bak").delete();
                                new File(CurrentFile.substring(0, CurrentFile.length() - 1)).renameTo(new File(
                                        CurrentFile.substring(0, CurrentFile.length() - 1) + ".bak"));
                                AgentsTree.save(CurrentFile.substring(0, CurrentFile.length() - 1));
                            }
                        }

                        AgentsTree.reset();
                        MagicFilesChooser chooser = new MagicFilesChooser(0, filechooserdirectory);
                        if (chooser.getSelectedFile() != null) {
                            filechooserdirectory = chooser.getCurrentDirectory().toString();
                            AgentsTree.load(chooser.getSelectedFile().toString(), true);

                        }
                    } else if (choice == JOptionPane.NO_OPTION) {
                        AgentsTree.reset();
                        MagicFilesChooser chooser = new MagicFilesChooser(0, filechooserdirectory);
                        if (chooser.getSelectedFile() != null) {
                            AgentsTree.load(chooser.getSelectedFile().toString(), true);
                            filechooserdirectory = chooser.getCurrentDirectory().toString();
                        }
                    }
                } else {
                    AgentsTree.reset();
                    MagicFilesChooser chooser = new MagicFilesChooser(0, filechooserdirectory);
                    if (chooser.getSelectedFile() != null) {
                        filechooserdirectory = chooser.getCurrentDirectory().toString();
                        AgentsTree.load(chooser.getSelectedFile().toString(), true);
                    }
                }
            }
        });
        JMenuItem item1_3 = menu1.add(new JMenuItem(lang[3]));
        item1_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MagicFilesChooser chooser = new MagicFilesChooser(0, filechooserdirectory);
                if (chooser.getSelectedFile() != null) {
                    filechooserdirectory = chooser.getCurrentDirectory().toString();
                    AgentsTree.load(chooser.getSelectedFile().toString(), false);
                }
            }
        });
        JMenuItem item1_4 = menu1.add(new JMenuItem(lang[4]));
        item1_4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        item1_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (AgentsTree.isModified()) {
                    if (CurrentFile.startsWith("New")) {
                        MagicFilesChooser chooser = new MagicFilesChooser(1, filechooserdirectory);
                        if (chooser.getSelectedFile() != null) {
                            filechooserdirectory = chooser.getCurrentDirectory().toString();
                            if (chooser.getSelectedFile().toString().endsWith(".magic"))
                                AgentsTree.save(chooser.getSelectedFile().toString());
                            else
                                AgentsTree.save(chooser.getSelectedFile().toString() + ".magic");
                        }
                    } else {
                        if (!CurrentFile.endsWith("*")) {
                            if (new File(CurrentFile + ".bak").exists())
                                new File(CurrentFile + ".bak").delete();
                            new File(CurrentFile).renameTo(new File(CurrentFile + ".bak"));
                            AgentsTree.save(CurrentFile);
                        } else {
                            if (new File(CurrentFile.substring(0, CurrentFile.length() - 1) + ".bak").exists())
                                new File(CurrentFile.substring(0, CurrentFile.length() - 1) + ".bak").delete();
                            new File(CurrentFile.substring(0, CurrentFile.length() - 1)).renameTo(new File(CurrentFile
                                    .substring(0, CurrentFile.length() - 1) + ".bak"));
                            AgentsTree.save(CurrentFile.substring(0, CurrentFile.length() - 1));
                        }
                    }
                }
            }
        });
        JMenuItem item1_5 = menu1.add(new JMenuItem(lang[5]));
        item1_5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0));
        item1_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MagicFilesChooser chooser = new MagicFilesChooser(1, filechooserdirectory);
                if (chooser.getSelectedFile() != null) {
                    filechooserdirectory = chooser.getCurrentDirectory().toString();
                    if (chooser.getSelectedFile().toString().endsWith(".magic"))
                        AgentsTree.save(chooser.getSelectedFile().toString());
                    else
                        AgentsTree.save(chooser.getSelectedFile().toString() + ".magic");
                }
            }
        });
        JMenuItem item1_6 = menu1.add(new JMenuItem(lang[6]));
        item1_6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        item1_6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JMenu menu2 = menuBar.add(new JMenu(lang[7]));
        menu2.setMnemonic(lang[7].charAt(0));
        menu2.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                cb[10].setState(AgentsExecutor.isVisible());
            }

            public void menuDeselected(MenuEvent e) {
            }

            public void menuCanceled(MenuEvent e) {
            }

        });

        cb[0] = (JCheckBoxMenuItem) menu2.add(new JCheckBoxMenuItem(lang[8]));
        cb[0].setSelected(true);
        cb[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.ALT_MASK));
        cb[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgentsTree.setVisible(cb[0].isSelected());
            }
        });
        cb[1] = (JCheckBoxMenuItem) menu2.add(new JCheckBoxMenuItem(lang[9]));
        cb[1].setSelected(true);
        cb[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        cb[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Computers.setVisible(cb[1].isSelected());
            }
        });
        cb[2] = (JCheckBoxMenuItem) menu2.add(new JCheckBoxMenuItem(lang[10]));
        cb[2].setSelected(true);
        cb[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        cb[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgentClasses.setVisible(cb[2].isSelected());
            }
        });
        cb[3] = (JCheckBoxMenuItem) menu2.add(new JCheckBoxMenuItem(lang[11]));
        cb[3].setSelected(true);
        cb[3].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        cb[3].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgentProperties.setVisible(cb[3].isSelected());
            }
        });

        cb[10] = (JCheckBoxMenuItem) menu2.add(new JCheckBoxMenuItem("Execution Frame"));
        cb[3].setSelected(true);
        cb[10].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cb[10].isSelected()) {
                    AgentsExecutor.setVisible(true);
                    AgentsExecutor.setState(Frame.NORMAL);
                } else
                    AgentsExecutor.setVisible(false);
            }
        });

        cb[9] = new JCheckBoxMenuItem();
        cb[9].setSelected(true);

        JMenu menu3 = menuBar.add(new JMenu(lang[12]));
        menu3.setMnemonic(lang[12].charAt(0));
        JMenu Col = (JMenu) menu3.add(new JMenu(lang[13]));

        JMenuItem item3_1 = Col.add(new JMenuItem(lang[14]));
        item3_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
        item3_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(desktop, lang[14], AgentsTree.Bcolor());
                if (color != null) {
                    AgentsTree.setBColor(color);
                    AgentsTree.repaint();
                }
            }
        });

        JMenuItem item3_2 = Col.add(new JMenuItem(lang[15]));
        item3_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
        item3_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(desktop, lang[15], AgentsTree.Acolor());
                if (color != null) {
                    AgentsTree.setAColor(color);
                    AgentsTree.repaint();
                }
            }
        });

        JMenuItem item3_3 = Col.add(new JMenuItem(lang[16]));
        item3_3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
        item3_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(desktop, lang[16], AgentsTree.Fcolor());
                if (color != null) {
                    AgentsTree.setFColor(color);
                    AgentsTree.repaint();
                }
            }
        });

        JMenuItem item3_4 = Col.add(new JMenuItem(lang[17]));
        item3_4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        item3_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(desktop, lang[17], AgentsTree.Lcolor());
                if (color != null) {
                    AgentsTree.setLColor(color);
                    AgentsTree.repaint();
                }
            }
        });

        JMenuItem item3_5 = Col.add(new JMenuItem(lang[54]));
        item3_5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.ALT_MASK));
        item3_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(desktop, lang[54], AgentsTree.SLcolor());
                if (color != null) {
                    AgentsTree.setSLColor(color);
                    AgentsTree.repaint();
                }
            }
        });
        JMenuItem item3_6 = Col.add(new JMenuItem(lang[18]));
        item3_6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        item3_6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(desktop, lang[18], AgentsTree.Scolor());
                if (color != null) {
                    AgentsTree.setSColor(color);
                    AgentsTree.repaint();
                }
            }
        });

        JMenu LFMenu = (JMenu) menu3.add(new JMenu(lang[19]));
        ButtonGroup group = new ButtonGroup();

        lf[0] = (JRadioButtonMenuItem) LFMenu.add(new JRadioButtonMenuItem(lang[20]));
        lf[0].setSelected(false);
        group.add(lf[0]);
        lf[0].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        lf[0].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                changePlaf(1);
            }
        });

        lf[1] = (JRadioButtonMenuItem) LFMenu.add(new JRadioButtonMenuItem(lang[21]));
        lf[1].setSelected(false);
        group.add(lf[1]);
        lf[1].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        lf[1].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                changePlaf(2);
            }
        });

        lf[2] = (JRadioButtonMenuItem) LFMenu.add(new JRadioButtonMenuItem(lang[22]));
        lf[2].setSelected(false);
        group.add(lf[2]);
        lf[2].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
        lf[2].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                changePlaf(3);
            }
        });

        JMenu icon = (JMenu) menu3.add(new JMenu(lang[23]));
        JMenuItem item4_1 = icon.add(new JMenuItem(lang[24]));
        item4_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("."));
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setDialogTitle("Select A Directory");
                chooser.showOpenDialog(desktop);
                if (chooser.getSelectedFile() != null) {
                    IconDirectory = chooser.getSelectedFile().toString();
                    AgentsTree.setIconDirectory(IconDirectory);
                    AgentsTree.repaintIcons();
                }
            }
        });
        cb[4] = (JCheckBoxMenuItem) icon.add(new JCheckBoxMenuItem(lang[25]));
        cb[4].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK));
        cb[4].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cb[4].isSelected()) {
                    AgentsTree.setClassesIconVisible(true);
                    cb[7].setSelected(true);
                } else {
                    AgentsTree.setClassesIconVisible(false);
                    cb[7].setSelected(false);
                }
            }
        });

        cb[5] = (JCheckBoxMenuItem) icon.add(new JCheckBoxMenuItem(lang[26]));
        cb[5].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK));
        cb[5].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cb[5].isSelected()) {
                    AgentsTree.setDefaultIconVisible(cb[4].isSelected());
                    cb[7].setSelected(cb[4].isSelected());
                } else {
                    AgentsTree.setDefaultIconVisible(false);
                    cb[7].setSelected(false);
                }
            }
        });

        cb[6] = (JCheckBoxMenuItem) icon.add(new JCheckBoxMenuItem(lang[27]));
        cb[6].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgentsTree.setSmallIcons(cb[6].isSelected());
            }
        });

        cb[7] = new JCheckBoxMenuItem();
        cb[7].setSelected(false);
        menu3.addSeparator();
        cb[8] = (JCheckBoxMenuItem) menu3.add(new JCheckBoxMenuItem(lang[31]));
        cb[8].setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        cb[8].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cb[8].isSelected()) {
                    AgentsTree.showAgentName(true);
                    AgentsTree.Organise();
                } else
                    AgentsTree.showAgentName(false);
            }
        });
        menu3.addSeparator();
        JMenuItem item5_1 = menu3.add(new JMenuItem(lang[33]));
        item5_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveOptions();
            }
        });

        return menuBar;
    }

    /**
     * Changes the Look & Feel of the Interface
     *
     * @param an integer
     * @see javax.swing.plaf
     */
    private void changePlaf(int s) {
        try {
            switch (s) {
                case 1:
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    break;
                case 2:
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    break;
                case 3:
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    break;
            }
            SwingUtilities.updateComponentTreeUI(this);
            CurrentPlaf = s;
        } catch (UnsupportedLookAndFeelException e) {
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(null, "Look & Feel not accessible on your system", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Look & Feel not found on your system", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (InstantiationException e) {
            JOptionPane.showMessageDialog(null, "This Look & Feel can't be instanciated", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Execution error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Load the configuration of the application
     */
    private void loadOptions() {
        OptionsFiles options = new OptionsFiles(propertiespath);

        int[] bounds = new int[4];
        bounds = options.getMainBound();
        setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        progress.addValue(inc);
        bounds = options.getComputersBound();
        Computers.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        bounds = options.getClassesBound();
        AgentClasses.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        bounds = options.getPropertiesBound();
        AgentProperties.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        progress.addValue(inc);
        bounds = options.getTreeBound();
        AgentsTree.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        bounds = options.getExecutionBound();
        AgentsExecutor.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        bounds = options.getFluxBound();
        IOframe.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
        progress.addValue(inc);
        String tmp;
        tmp = options.getIconDirectory();
        IconDirectory = tmp;
        AgentsTree.setIconDirectory(IconDirectory);
        tmp = options.getAgentClassesPath();
        AgentClasses.changePath(tmp);
        chooseSkill.changePath(options.getSkillClassesPath());
        filechooserdirectory = options.getFileDirectory();
        progress.addValue(inc);
        Color[] colors = new Color[6];
        colors = options.getColors();
        AgentsTree.setBColor(colors[0]);
        AgentsTree.setAColor(colors[1]);
        AgentsTree.setFColor(colors[2]);
        AgentsTree.setLColor(colors[3]);
        AgentsTree.setSLColor(colors[4]);
        AgentsTree.setSColor(colors[5]);
        AgentsTree.setReverseArrowsMode(options.getArrowMode());
        progress.addValue(inc);

        cb[0].setSelected(options.getShowAgentsTree());
        AgentsTree.setVisible(cb[0].isSelected());
        cb[1].setSelected(options.getShowComputers());
        Computers.setVisible(cb[1].isSelected());
        cb[2].setSelected(options.getShowAgentsClasses());
        AgentClasses.setVisible(cb[2].isSelected());
        progress.addValue(inc);
        cb[3].setSelected(options.getShowAgentsProperties());
        AgentProperties.setVisible(cb[3].isSelected());
        cb[4].setSelected(options.getShowAgentIcons());
        cb[7].setSelected(!cb[4].isSelected());
        cb[5].setSelected(options.getSetDefaultIcons());
        progress.addValue(inc);
        cb[6].setSelected(options.getSetSmallsIcons());
        AgentsTree.setSmallIcons(cb[6].isSelected());
        progress.addValue(inc);
        cb[8].setSelected(options.getShowAgentName());
        AgentsTree.showAgentName(cb[8].isSelected());
        progress.addValue(inc);
        AgentsExecutor.setVisible(options.getShowExecution());
        IOframe.setVisible(options.getShowExecution());
        progress.addValue(inc);
        if (isVisible())
            setVisible(true);
        progress.addValue(inc);
        int LF = options.getLookAndFeel();
        lf[0].setSelected(LF == 1);
        lf[1].setSelected(LF == 2);
        lf[2].setSelected(LF == 3);
        changePlaf(LF);
        progress.addValue(inc);
        SwingUtilities.updateComponentTreeUI(this);
        progress.addValue(inc);
        progress.setText("Initializing Graphics");
    }

    /**
     * Save the application configuration
     */
    private void saveOptions() {
        if (new File(propertiespath + "LanceurAgents.properties").exists())
            new File(propertiespath + "LanceurAgents.properties").renameTo(new File(propertiespath
                    + "LanceurAgents.properties.bak"));

        try {
            BufferedWriter options = new BufferedWriter(new FileWriter(propertiespath + "LanceurAgents.properties"));
            options.write("# @(#)LanceurAgents.properties\n");
            options.write("#\n");
            options.write("# Resource properties for LanceurAgents\n\n\n");

            options.write("# Default Colors\n\n");
            options.write("BackgroundColor=" + AgentsTree.Bcolor().getRed() + " " + AgentsTree.Bcolor().getGreen()
                    + " " + AgentsTree.Bcolor().getBlue() + "\n");
            options.write("AgentColor=" + AgentsTree.Acolor().getRed() + " " + AgentsTree.Acolor().getGreen() + " "
                    + AgentsTree.Acolor().getBlue() + "\n");
            options.write("FontColor=" + AgentsTree.Fcolor().getRed() + " " + AgentsTree.Fcolor().getGreen() + " "
                    + AgentsTree.Fcolor().getBlue() + "\n");
            options.write("LinkColor=" + AgentsTree.Lcolor().getRed() + " " + AgentsTree.Lcolor().getGreen() + " "
                    + AgentsTree.Lcolor().getBlue() + "\n");
            options.write("DirectLink=" + AgentsTree.SLcolor().getRed() + " " + AgentsTree.SLcolor().getGreen() + " "
                    + AgentsTree.SLcolor().getBlue() + "\n");
            options.write("SelectionColor=" + AgentsTree.Scolor().getRed() + " " + AgentsTree.Scolor().getGreen() + " "
                    + AgentsTree.Scolor().getBlue() + "\n\n\n");

            options.write("# Menubar Options\n\n");
            options.write("LookAndFeel=" + CurrentPlaf + "\n\n");
            options.write("ReverseArrow=" + AgentsTree.isReverseArrowsMode() + "\n");
            options.write("ShowAgentsTree=" + cb[0].isSelected() + "\n");
            options.write("ShowComputers=" + cb[1].isSelected() + "\n");
            options.write("ShowAgentsClasses=" + cb[2].isSelected() + "\n");
            options.write("ShowAgentsProperties=" + cb[3].isSelected() + "\n\n");
            options.write("ShowAgentIcons=" + cb[4].isSelected() + "\n");
            options.write("ShowExecution=" + AgentsExecutor.isVisible() + "\n");
            options.write("SetDefaultIcons=" + cb[5].isSelected() + "\n");
            options.write("SetSmallsIcons=" + cb[6].isSelected() + "\n\n");
            options.write("ShowAgentName=" + cb[8].isSelected() + "\n\n\n");

            options.write("# Default Directories\n\n");
            options.write("FileDirectory=" + filechooserdirectory.replace(File.separatorChar, '+') + "\n");
            options.write("IconDirectory=" + IconDirectory.replace(File.separatorChar, '+') + "\n");
            options.write("SkillClassPath=" + chooseSkill.getClassPath().replace(File.separatorChar, '+') + "\n");
            options.write("AgentClassesPath=" + AgentClasses.getClassPath().replace(File.separatorChar, '+') + "\n\n\n");

            options.write("# Frames positions and dimensions\n\n");
            options.write("MainBound=" + getLocation().x + " " + getLocation().y + " " + getSize().width + " "
                    + getSize().height + "\n");
            options.write("ComputersBound=" + Computers.getLocation().x + " " + Computers.getLocation().y + " "
                    + Computers.getSize().width + " " + Computers.getSize().height + "\n");
            options.write("ClassesBound=" + AgentClasses.getLocation().x + " " + AgentClasses.getLocation().y + " "
                    + AgentClasses.getSize().width + " " + AgentClasses.getSize().height + "\n");
            options.write("PropertiesBound=" + AgentProperties.getLocation().x + " " + AgentProperties.getLocation().y
                    + " " + AgentProperties.getSize().width + " " + AgentProperties.getSize().height + "\n");
            options.write("TreeBound=" + AgentsTree.getLocation().x + " " + AgentsTree.getLocation().y + " "
                    + AgentsTree.getSize().width + " " + AgentsTree.getSize().height + "\n");

            options.write("ExecutionBound=" + AgentsExecutor.getLocation().x + " " + AgentsExecutor.getLocation().y
                    + " " + AgentsExecutor.getSize().width + " " + AgentsExecutor.getSize().height + "\n");

            options.write("FluxBound=" + IOframe.getLocation().x + " " + IOframe.getLocation().y + " "
                    + IOframe.getSize().width + " " + IOframe.getSize().height + "\n");
            options.close();
            new File("Properties" + System.getProperty("file.separator") + "LanceurAgents.properties.bak").delete();
            JOptionPane.showMessageDialog(null, "Options have been successfully saved", "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Throwable t) {
            if (new File("Properties" + System.getProperty("file.separator") + "LanceurAgents.properties.bak").exists())
                new File("Properties" + System.getProperty("file.separator") + "LanceurAgents.properties.bak")
                        .renameTo(new File("Properties" + System.getProperty("file.separator")
                                + "LanceurAgents.properties."));
            JOptionPane.showMessageDialog(null, "An error has occured when trying to save options", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setVisiBle(Boolean b) {
        setVisible(b.booleanValue());
    }

    class ExecutorFrame extends JFrame {
        JMenu menu2 = null;
        JMenu menu3 = null;
        JMenu menu4 = null;
        private final Deployement execute;
        private final JMenuBar menuBar = new JMenuBar();
        // private JVMInputServer streamServer;
        private final JDesktopPane desktop2 = new JDesktopPane();

        public ExecutorFrame() {
            super("Execution");
            setJMenuBar(CreateMenuBar());
            progress.addValue(inc);

            execute = new Deployement(menu2, desktop2, platform, IOframe, Interprete);
            try {
                Interprete.set("agent", execute.getMyAgent());
            } catch (EvalError e) {
                e.printStackTrace();
            }
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    if (!edition.isVisible()) {

                        System.exit(0);
                    }
                    setVisible(false);
                }

                public void windowDeiconified(WindowEvent e) {
                    repaint();
                }
            });

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    AgentsExecutor.repaint();
                    e.consume();
                }

                public void mousePressed(MouseEvent e) {
                    requestFocus();

                }
            });
            getContentPane().add(desktop2);
            this.validate();
            // IOframe.setVisible(true);
        }

        private JMenuBar CreateMenuBar() {
            JMenu menu1 = menuBar.add(new JMenu("Config"));
            JMenuItem item1_1 = menu1.add(new JMenuItem("load"));
            item1_1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    MagicFilesChooser chooser = new MagicFilesChooser(0, filechooserdirectory);

                    if (chooser.getSelectedFile() != null)
                        try {
                            filechooserdirectory = chooser.getCurrentDirectory().toString();
                            execute.newConfig(new BufferedReader(new FileReader(chooser.getSelectedFile().toString())),
                                    chooser.getSelectedFile().toString(), Computers.getAllComputers());

                        } catch (Throwable t) {
                        }
                    AgentsExecutor.validate();
                    AgentsExecutor.repaint();

                }
            });
            JMenuItem item1_2 = menu1.add(new JMenuItem("edit"));
            item1_2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    edition.setState(Frame.NORMAL);
                    edition.setVisible(true);
                }
            });

            menu3 = menuBar.add(new JMenu("show"));
            menu3.addMenuListener(new MenuListener() {
                public void menuSelected(MenuEvent e) {
                    AgentsExecutor.repaint();
                    AgentsExecutor.requestFocus();
                    menu3.removeAll();
                    JCheckBoxMenuItem menu3_1 = (JCheckBoxMenuItem) menu3.add(new JCheckBoxMenuItem("Console Tools"));
                    menu3_1.setState(IOframe.isVisible());
                    menu3_1.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            IOframe.setVisible(!IOframe.isVisible());
                        }
                    });
                    for (Enumeration h = execute.getConfigs(); h.hasMoreElements(); ) {
                        String name = (String) h.nextElement();
                        JMenu tmp = (JMenu) menu3.add(new JMenu(name));
                        JMenuItem tmp3 = tmp.add(new JMenuItem("Show Config"));
                        JMenuItem tmp4 = tmp.add(new JMenuItem("Hide Config"));
                        JMenuItem tmp5 = tmp.add(new JMenuItem("Show All Agents"));
                        tmp3.setActionCommand(name);
                        tmp4.setActionCommand(name);
                        tmp5.setActionCommand(name);
                        tmp3.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                execute.setConfigVisible(e.getActionCommand(), true);
                            }
                        });
                        tmp4.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                execute.setConfigVisible(e.getActionCommand(), false);
                            }
                        });
                        tmp5.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                execute.setAllVisible(e.getActionCommand(), true);
                            }
                        });
                        for (Enumeration g = execute.getAgentsIn(name).elements(); g.hasMoreElements(); ) {
                            Tuple tuple = (Tuple) g.nextElement();
                            String name2 = tuple.getName().substring(name.length() + 1);
                            JCheckBoxMenuItem tmp2 = (JCheckBoxMenuItem) tmp.add(new JCheckBoxMenuItem(name2));
                            tmp2.setActionCommand(tuple.getName());
                            tmp2.setState(tuple.isActive());
                            tmp2.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    execute.reverseAgentVisible(e.getActionCommand());
                                }
                            });
                        }
                    }
                    AgentsExecutor.validate();
                    AgentsExecutor.repaint();

                }

                public void menuDeselected(MenuEvent e) {
                }

                public void menuCanceled(MenuEvent e) {
                }

            });

            menu4 = menuBar.add(new JMenu("kill"));
            menu4.addMenuListener(new MenuListener() {
                public void menuSelected(MenuEvent e) {
                    menu4.removeAll();
                    for (Enumeration enu = execute.getConfigs(); enu.hasMoreElements(); ) {
                        String name = (String) enu.nextElement();
                        JMenuItem tmp = menu4.add(new JMenuItem(name));
                        tmp.setActionCommand(name);
                        tmp.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                execute.kill(e.getActionCommand());
                            }
                        });
                    }
                }

                public void menuDeselected(MenuEvent e) {
                }

                public void menuCanceled(MenuEvent e) {
                }

            });

            return menuBar;
        }

        public void newConfig(String transfer) {
            setState(Frame.NORMAL);
            setVisible(true);
            String name = execute.getNewConfName(CurrentFile);
            try {
                execute.newConfig(new BufferedReader(new StringReader(transfer)), name, Computers.getAllComputers());
            } catch (LaunchAgentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                this.showAgentError(e.getAgent());
            } catch (LaunchLinkException el) {
                JOptionPane.showMessageDialog(null, el.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                this.showLinkError(el.getLink());
            } catch (LaunchSpecialLinkException esl) {
                JOptionPane.showMessageDialog(null, esl.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                this.showLinkError(esl.getSpecialLink());
            } catch (IOException error) {
            }
        }

        public void showAgentError(GraphicAgent agent) {
            AgentsTree.selection(agent);
        }

        public void showLinkError(GraphicLink link) {
            AgentsTree.selection(link);
        }

        public void reset() {
            execute.reset();
        }

    }

    class PortFrame extends JDialog {
        JLabel lport;
        JTextField tport;
        ComputerNode node = null;

        public PortFrame(Frame owner) {
            super(owner, true);
            setLocation(150, 150);
            setVisible(false);
            lport = new JLabel("Platform Port");
            tport = new JTextField(5);
            progress.addValue(inc);
            tport.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        node.setPort((new Integer(tport.getText())).intValue());
                    } catch (NumberFormatException n) {
                    }
                    setVisible(false);
                }
            });
            getContentPane().setLayout(new GridLayout(1, 2));
            setSize(200, 60);
            getContentPane().add(lport);
            progress.addValue(inc);
            getContentPane().add(tport);
        }

        public void setNode(ComputerNode node) {
            this.node = node;
            tport.setText((new Integer(node.getPort())).toString());
        }

        public void getFocus() {
            try {
                tport.requestFocus();
            } catch (Throwable t) {
            }
            tport.setEditable(true);
            tport.setEnabled(true);
        }
    }

    /**
     * Shows the computers availables on the network
     *
     * @see javax.swing.JInternalFrame
     */
    class ComputersFrame extends JInternalFrame {

        protected JTree computers;
        protected DefaultTreeModel treeModel;
        PingNetwork available;
        private Vector Vavailable;
        private String CurrentComputer = "";
        private boolean hasChanged = false;
        private final PortFrame portframe = new PortFrame(edition);

        /**
         * Creates a new ComputersFrame
         */
        ComputersFrame() {
            super("", true, false, false, false);
            setTitle(lang[45]);
            available = new PingNetwork(platform);
            StreamTokenizer st;
            try {
                st = new StreamTokenizer(new BufferedReader(
                        new FileReader(propertiespath + "ComputersNames.properties")));
            } catch (FileNotFoundException e) {
                System.out.println("\nCan't find the properties file that contains the computers names.");
                st = null;
            }
            TreeCreator creator = new TreeCreator(st);
            progress.addValue(inc);
            treeModel = creator.createDomainHierarchy();
            computers = new JTree(treeModel);
            computers.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            computers.setEditable(true);
            // Set the properties of the nodes
            computers.setCellRenderer(creator.setRenderer(available));
            progress.addValue(inc);
            // Listen for when the selection changes
            treeModel.addTreeModelListener(new TreeModelListener() {
                public void treeNodesChanged(TreeModelEvent e) {
                    hasChanged = true;
                    available.testNetwork(getAllComputers((DefaultMutableTreeNode) treeModel.getRoot()));
                }

                public void treeNodesInserted(TreeModelEvent e) {
                    hasChanged = true;
                    computers.startEditingAtPath(e.getTreePath().pathByAddingChild(
                            e.getChildren()[0]));
                }

                public void treeNodesRemoved(TreeModelEvent e) {
                    hasChanged = true;
                }

                public void treeStructureChanged(TreeModelEvent e) {

                    hasChanged = true;
                }
            });
            computers.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent kevent) {
                    if (kevent.getKeyCode() == KeyEvent.VK_DELETE)
                        deleteSelectedNode();
                }
            });
            computers.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    computers.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                    int selRow = computers.getRowForLocation(e.getX(), e.getY());
                    if (selRow != -1) {
                        TreePath selPath = computers.getPathForRow(selRow);
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) (selPath.getLastPathComponent());
                        DefaultMutableTreeNode nodetmp = (DefaultMutableTreeNode) node.getParent();
                        CurrentComputer = node.toString();
                        if (nodetmp != null) {
                            while (!nodetmp.toString().toLowerCase().equals(computers.getModel().getRoot().toString())) {
                                CurrentComputer = CurrentComputer + "." + nodetmp;
                                nodetmp = (DefaultMutableTreeNode) nodetmp.getParent();
                            }
                            // CurrentComputer = CurrentComputer + "." +
                            // computers.getModel().getRoot().toString();
                        }
                        if (SwingUtilities.isRightMouseButton(e)) {
                            computers.setSelectionRow(selRow);
                            AgentsTree.savePreviousSelection();
                            AgentsTree.setSelection(CurrentComputer);
                            repaint();
                            JPopupMenu popup0 = new JPopupMenu();
                            if (!node.getAllowsChildren()) {

                                portframe.setNode((ComputerNode) node);
                                JMenuItem jMenu0_1 = popup0.add(new JMenuItem("setPort"));
                                jMenu0_1.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent portevent) {
                                        portframe.setVisible(true);
                                        portframe.getFocus();
                                        // portframe.setSelected(true);
                                        hasChanged = true;
                                    }
                                });
                            }

                            popup0.show(e.getComponent(), e.getX(), e.getY());
                        } else {
                            if (node.getAllowsChildren())
                                CurrentComputer = "";
                        }
                    } else
                        CurrentComputer = "";
                }

                public void mouseReleased(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        AgentsTree.setPreviousSelection();
                        repaint();
                    } else {
                        if (!CurrentComputer.equals("")) {
                            if (AgentProperties.ComputerActive()) {
                                AgentProperties.setComputer(CurrentComputer);
                                AgentsTree.setComputer(CurrentComputer);
                            }
                        }
                    }
                }

                public void mouseEntered(MouseEvent e) {

                    if (!computers.isEditing()) {

                        try {
                            setSelected(true);
                        } catch (Throwable t) {
                        }
                        requestFocus();
                        computers.requestFocus();
                        e.consume();
                    } else
                        computers.startEditingAtPath(computers.getEditingPath());
                }

                public void mouseExited(MouseEvent e) {
                }
            });

            // Create the scroll pane and add the tree to it
            JScrollPane computersView = new JScrollPane(computers);
            getContentPane().add(computersView);
            setJMenuBar(CreateMenuBar());
            available.testNetwork(getAllComputers((DefaultMutableTreeNode) treeModel.getRoot()));
        }

        public Hashtable getAllComputers() {
            return getAllComputers((DefaultMutableTreeNode) treeModel.getRoot());
        }

        public Hashtable getAllComputers(DefaultMutableTreeNode pere) {
            Hashtable Vnom_machines = new Hashtable();
            if (pere != null)
                return getAllComputers(pere, "", Vnom_machines);

            return Vnom_machines;
        }

        public Hashtable getAllComputers(DefaultMutableTreeNode pere, String toplevel) {
            Hashtable Vnom_machines = new Hashtable();
            return getAllComputers(pere, toplevel, Vnom_machines);
        }

        private Hashtable getAllComputers(DefaultMutableTreeNode pere, String current, Hashtable Vnom_machines) {
            String nouveau;
            if (pere.getAllowsChildren()) {
                nouveau = "." + pere + current;
                for (Enumeration e = pere.children(); e.hasMoreElements(); )
                    getAllComputers((DefaultMutableTreeNode) e.nextElement(), nouveau, Vnom_machines);
            } else {
                nouveau = pere + current;
                nouveau = nouveau.substring(0, nouveau.length() - 1);
                Vnom_machines.put(nouveau, new LinkToDaemon(nouveau, ((ComputerNode) pere).getPort()));
            }
            return Vnom_machines;
        }

        protected DefaultMutableTreeNode getSelectedNode() {
            TreePath selPath = computers.getSelectionPath();
            if (selPath != null)
                return (DefaultMutableTreeNode) selPath.getLastPathComponent();
            return null;
        }

        public boolean hasChanged() {
            return hasChanged;
        }

        public void saveNetwork() {
            TreeSaver saver = new TreeSaver();
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter(propertiespath + "ComputersNames.properties"));
                saver.save((DefaultMutableTreeNode) treeModel.getRoot(), out);
                hasChanged = false;
            } catch (Throwable t2) {
                JOptionPane.showMessageDialog(null,
                        "An error has occured when trying to write Properties/ComputersName.properties", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private JMenuBar CreateMenuBar() {
            JMenuBar menuBar = new JMenuBar();
            JMenu menu2 = menuBar.add(new JMenu("File"));
            JMenu menu1 = menuBar.add(new JMenu("edit"));
            JMenu menu3 = menuBar.add(new JMenu("Net"));
            JMenu menu1b = (JMenu) menu1.add(new JMenu("insert"));
            JMenuItem item1_1 = menu1b.add("computer");
            JMenuItem item1_2 = menu1b.add("domain");
            JMenuItem item1_3 = menu1.add("delete");
            JMenuItem item2_1 = menu2.add("reload");
            JMenuItem item2_2 = menu2.add("save");
            JMenuItem item3_1 = menu3.add("reTest");

            item1_1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DefaultMutableTreeNode lastItem = getSelectedNode();
                    DefaultMutableTreeNode parent;
                    int newIndex;
                    /* Determine where to create the new node. */
                    if (lastItem != null) {
                        if (lastItem.getAllowsChildren())
                            parent = lastItem;
                        else
                            parent = (DefaultMutableTreeNode) lastItem.getParent();
                    } else
                        parent = (DefaultMutableTreeNode) treeModel.getRoot();
                    if (lastItem == null || parent == lastItem)
                        newIndex = 0;
                    else
                        newIndex = parent.getIndex(lastItem);

                    /* Let the treemodel know. */
                    treeModel.insertNodeInto(new ComputerNode(""), parent, newIndex);

                }
            }); // End of SampleTree.AddAction

            item1_2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    DefaultMutableTreeNode lastItem = getSelectedNode();
                    DefaultMutableTreeNode parent;
                    int newIndex;
                    /* Determine where to create the new node. */
                    if (lastItem != null) {
                        if (lastItem.getAllowsChildren())
                            parent = lastItem;
                        else
                            parent = (DefaultMutableTreeNode) lastItem.getParent();
                    } else
                        parent = (DefaultMutableTreeNode) treeModel.getRoot();
                    if (lastItem == null || parent == lastItem)
                        newIndex = 0;
                    else
                        newIndex = parent.getIndex(lastItem);
                    /* Let the treemodel know. */
                    treeModel.insertNodeInto(new DefaultMutableTreeNode("", true), parent, newIndex);

                }
            }); // End of SampleTree.AddAction

            item1_3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteSelectedNode();
                }

            });
            item2_1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    StreamTokenizer st = null;
                    try {
                        BufferedWriter out = new BufferedWriter(new FileWriter(propertiespath
                                + "ComputersNames.properties"));
                        st = new StreamTokenizer(new BufferedReader(new FileReader(propertiespath
                                + "ComputersNames.properties")));
                    } catch (Throwable t) {
                        System.out.println("Can't find the properties file that contains the computers names.");
                        System.exit(0);
                    }
                    TreeCreator creator2 = new TreeCreator(st);
                    TreeModel treeModel2 = creator2.createDomainHierarchy();
                    treeModel.setRoot((DefaultMutableTreeNode) treeModel2.getRoot());
                    treeModel.reload();

                    available.testNetwork(getAllComputers((DefaultMutableTreeNode) treeModel.getRoot()));

                }
            });
            item2_2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveNetwork();
                }

            });
            item3_1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        available.reset();
                    } catch (Throwable E) {
                    }
                    computers.repaint();
                    available.testNetwork(getAllComputers((DefaultMutableTreeNode) treeModel.getRoot()));

                }
            });
            return menuBar;
        }

        public void deleteSelectedNode() {
            DefaultMutableTreeNode lastItem = getSelectedNode();
            if (lastItem != null && lastItem != treeModel.getRoot())
                treeModel.removeNodeFromParent(lastItem);
        }

        public void resetNetwork() {
        }
    }

    /**
     * Shows the differents classes of agents
     *
     * @see javax.swing.JInternalFrame
     */

    class AgentClassesFrame extends JInternalFrame {
        private final Container pane = getContentPane();
        private final JTextField racine = new JTextField(20);
        private String pathrac, CurrentClass = "", ClassPath = "", pathbak = ".";
        private final JButton browse = new JButton(lang[49]);
        private final JScrollPane bar = new JScrollPane();
        private final JScrollPane meth = new JScrollPane();
        private final JPanel panel = new JPanel();
        private final JInternalFrame frame = new JInternalFrame("", false, false, false, false);
        private boolean active = true;

        /**
         * Creates a new AgentClassesFrame
         */
        AgentClassesFrame() {
            super("", true, false, false, false);
            setTitle(lang[46]);
            frame.getContentPane().add(meth);
            racine.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changePath(racine.getText());
                }
            });
            progress.addValue(inc);

            browse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File(pathbak));
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setDialogTitle("Select A Directory");
                    chooser.showOpenDialog(desktop);
                    if (chooser.getSelectedFile() != null)
                        changePath(chooser.getSelectedFile().toString());
                }
            });
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    try {
                        setSelected(true); // classes.requestFocus();
                        bar.requestFocus();
                    } catch (Throwable t) {
                    }
                }

                public void mouseExited(MouseEvent e) {
                    AgentsTree.requestFocus();
                }
            });
            frame.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    setVisible(false);
                }
            });
            desktop.add(frame, JLayeredPane.POPUP_LAYER);
            frame.setVisible(false);

            panel.setLayout(new GridLayout(1, 2));
            panel.add(racine);
            panel.add(browse);
            pane.add(panel, BorderLayout.NORTH);
            pane.add(bar, BorderLayout.CENTER);
        }

        /**
         * Sets the path where to find the classes
         *
         * @param path the new path to use as the directory root
         */

        public String getClassPath() {
            return pathrac;
        }

        public void changePath(String path) {
            // Determine the classes directory

            pathrac = (new File(path)).getAbsolutePath();
            if (pathrac.endsWith("."))
                pathrac = pathrac.substring(0, pathrac.length() - 1);
            if (!pathrac.endsWith(System.getProperty("file.separator")))
                pathrac = pathrac + System.getProperty("file.separator");

            racine.setText(pathrac);

            // Create the nodes
            if (new File(pathrac).isDirectory()) {
                pathbak = pathrac;

                TreeCreator creator = new TreeCreator(pathrac);
                final JTree classes = creator.createClassesTree();

                // Listen for when the selection changes
                classes.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        active = true;
                        if (!isSelected() || !classes.hasFocus()) {
                            try {
                                setSelected(true);
                                classes.requestFocus();
                            } catch (Throwable t) {
                            }
                        } else {
                            ClassPath = "";
                            int selRow = classes.getRowForLocation(e.getX(), e.getY());
                            if (selRow != -1) {
                                TreePath selPath = classes.getPathForRow(selRow);
                                DefaultMutableTreeNode node = (DefaultMutableTreeNode) (selPath.getLastPathComponent());
                                DefaultMutableTreeNode nodetmp = (DefaultMutableTreeNode) node.getParent();
                                if (node.toString().equals("..")) {
                                    String upperdirectory = pathbak;
                                    int index = upperdirectory.lastIndexOf(File.separatorChar);
                                    if (index != -1)
                                        upperdirectory = upperdirectory.substring(0, index);
                                    index = upperdirectory.lastIndexOf(File.separatorChar);
                                    if (index != -1) {
                                        upperdirectory = upperdirectory.substring(0, index);
                                    }
                                    if (!upperdirectory.endsWith(System.getProperty("file.separator")))
                                        upperdirectory = upperdirectory + System.getProperty("file.separator");

                                    changePath(upperdirectory);
                                    return;
                                }

                                if (nodetmp != null) {
                                    while (!nodetmp.toString().equals(classes.getModel().getRoot().toString())) {
                                        ClassPath = nodetmp + System.getProperty("file.separator")
                                                + ClassPath;
                                        nodetmp = (DefaultMutableTreeNode) nodetmp.getParent();
                                    }
                                }
                                if (pathrac.endsWith(System.getProperty("file.separator")))
                                    ClassPath = pathrac + ClassPath;
                                else
                                    ClassPath = pathrac + System.getProperty("file.separator") + ClassPath;
                                if (ClassPath.endsWith(".jar" + System.getProperty("file.separator")))
                                    ClassPath = ClassPath.substring(0, ClassPath.length() - 1);

                                if (SwingUtilities.isRightMouseButton(e)) {
                                    classes.setSelectionRow(selRow);
                                    if (!node.getAllowsChildren()) {

                                        UniversalLoader load = new UniversalLoader();
                                        try {
                                            Class c = load.loadClass(
                                                    node.toString().substring(0, node.toString().length() - 6),
                                                    ClassPath);
                                            Method[] m = c.getDeclaredMethods();
                                            if (m.length != 0) {
                                                JList l = new JList(m);
                                                meth.getViewport().setView(l);
                                                int xlist = 50;
                                                if (xlist < 0)
                                                    xlist = 0;
                                                frame.setBounds(xlist, (screen.height - (m.length * 20 + 50)) / 2, 500,
                                                        m.length * 20 + 50);
                                                frame.setVisible(true);
                                            } else
                                                JOptionPane.showMessageDialog(null,
                                                        "No declared methods in this class", "Information",
                                                        JOptionPane.INFORMATION_MESSAGE);
                                        }
                                        /*
                                         * catch (ClassNotFoundException r) {
                                         * JOptionPane.showMessageDialog(null,
                                         * "An error has occured : the class has not been found!"
                                         * , "Error",
                                         * JOptionPane.ERROR_MESSAGE); }
                                         */ catch (Throwable t) {
                                            JOptionPane.showMessageDialog(null,
                                                    "An error has occured when trying to read the class methods",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                } else {
                                    if (!node.getAllowsChildren()) {
                                        CurrentClass = node.toString();
                                        int pt = CurrentClass.lastIndexOf(46);
                                        CurrentClass = CurrentClass.substring(0, pt);
                                    } else
                                        CurrentClass = "";
                                }
                            } else
                                CurrentClass = "";
                        }
                        try {
                            setSelected(true);
                            classes.requestFocus();
                        } catch (Throwable T) {
                        }
                    }

                    public void mouseReleased(MouseEvent e) {
                        if (frame.isVisible())
                            frame.setVisible(false);

                        if (active) {
                            if (frame.isVisible())
                                frame.setVisible(false);
                            else {
                                if (!CurrentClass.equals("")) {
                                    if (AgentProperties.ClassActive()) {
                                        // Vector V =
                                        // AgentsTree.getClassInitialisers(ClassPath,CurrentClass);
                                        // AgentProperties.addInit(V);
                                        // //AgentProperties.setClass(CurrentClass);
                                        AgentsTree.setClass(ClassPath, CurrentClass);
                                    }
                                }
                            }
                        }
                    }

                    public void mouseEntered(MouseEvent e) {
                        try {
                            setSelected(true);
                            classes.requestFocus();// bar.requestFocus();
                            // active = false;
                        } catch (Throwable t) {
                        }
                    }

                    public void mouseExited(MouseEvent e) {
                        try {
                            setSelected(true);
                            bar.requestFocus();
                        } catch (Throwable t) {
                        }
                    }
                });

                // Add the tree to the viewport of the JScrollPane
                invalidate();
                bar.getViewport().setView(classes);
                validate();
            }
        }

    }

    class SkillFrame extends JDialog {
        private final JTextField racine = new JTextField(20);
        private String pathrac, CurrentClass = "", ClassPath = "";
        private String constructor = "";
        private final JButton Ok = new JButton("Add");
        private final JButton Cancel = new JButton("Cancel");
        private final boolean isValid = false;
        private final JButton browse = new JButton("browse");
        private final JScrollPane bar = new JScrollPane();
        private final JScrollPane bar2 = new JScrollPane();
        private final JPanel panel0 = new JPanel();
        private final JPanel panel1 = new JPanel();
        private final JPanel panel2 = new JPanel();
        private final JPanel panel3 = new JPanel();
        private final JPanel panel4 = new JPanel();
        private final JPanel panel5 = new JPanel();
        private final JList liste = new JList();
        private String pathbak = ".";

        public SkillFrame(JFrame owner) {
            super(owner, "Add a skill", true);
            // setLocation(150,150);
            setBounds(100, 100, 400, 300);
            setVisible(false);
            racine.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    changePath(racine.getText());
                }
            });
            Ok.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (liste.getSelectedValue() != null)
                        constructor = liste.getSelectedValue().toString();
                    reportSkill();
                    setVisible(false);
                }
            });
            progress.addValue(inc);

            Cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    CurrentClass = "";
                    setVisible(false);
                }
            });
            browse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    JFileChooser chooser = new JFileChooser();
                    chooser.setCurrentDirectory(new File("."));
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setDialogTitle("Select A Directory");
                    chooser.showOpenDialog(null);
                    if (chooser.getSelectedFile() != null)
                        changePath(chooser.getSelectedFile().toString());
                }
            });
            bar2.getViewport().setView(liste);
            panel0.add(Ok);
            panel0.add(Cancel);
            panel1.setLayout(new GridLayout(1, 2));
            panel1.add(racine);
            panel1.add(browse);
            panel2.setLayout(new BorderLayout());
            panel2.add(panel1, BorderLayout.NORTH);
            panel2.add(bar, BorderLayout.CENTER);
            panel3.setLayout(new BorderLayout());
            panel3.add(new JLabel("      select a constructor"), BorderLayout.NORTH);
            panel3.add(bar2, BorderLayout.CENTER);
            panel4.setLayout(new GridLayout(1, 2));
            panel4.add(panel2);// panel4.add(new
            // JSeparator(SwingConstants.VERTICAL));
            panel4.add(panel3);
            panel5.setLayout(new BorderLayout());
            panel5.add(panel4, BorderLayout.CENTER);
            panel5.add(panel0, BorderLayout.SOUTH);
            Ok.setEnabled(false);
            getContentPane().add(panel5);
        }

        /**
         * Sets the path where to find the classes
         *
         * @param path the new path to use as the directory root
         */

        public SkillDescriptor getSkill() {
            return new SkillDescriptor(CurrentClass, ClassPath, (String) liste.getSelectedValue());
        }

        public void reportSkill() {
            if (isValid())
                AgentsTree.addSkill(new SkillDescriptor(CurrentClass, ClassPath, (liste
                        .getSelectedValue().toString())));
            AgentProperties.showSkills();

        }

        public boolean isValid() {
            if (CurrentClass == null || CurrentClass.equals(""))
                return false;
            return !constructor.equals("");
        }

        public String getClassPath() {
            return pathrac;
        }

        public void changePath(String path) {
            Ok.setEnabled(false);
            liste.setListData(new Vector());
            // Determine the classes directory
            pathrac = (new File(path)).getAbsolutePath();
            if (pathrac.endsWith("."))
                pathrac = pathrac.substring(0, pathrac.length() - 1);
            racine.setText(pathrac);

            // Create the nodes
            if (new File(pathrac).isDirectory()) {
                pathbak = pathrac;
                TreeCreator creator = new TreeCreator(pathrac);
                final JTree classes = creator.createClassesTree();

                // Listen for when the selection changes
                classes.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        ClassPath = "";
                        int selRow = classes.getRowForLocation(e.getX(), e.getY());
                        if (selRow != -1) {
                            TreePath selPath = classes.getPathForRow(selRow);
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) (selPath.getLastPathComponent());
                            DefaultMutableTreeNode nodetmp = (DefaultMutableTreeNode) node.getParent();
                            if (node.toString().equals("..")) {
                                String upperdirectory = pathbak;
                                int index = upperdirectory.lastIndexOf(File.separatorChar);
                                if (index != -1)
                                    upperdirectory = upperdirectory.substring(0, index);
                                index = upperdirectory.lastIndexOf(File.separatorChar);
                                if (index != -1) {
                                    upperdirectory = upperdirectory.substring(0, index);
                                }
                                if (!upperdirectory.endsWith(System.getProperty("file.separator")))
                                    upperdirectory = upperdirectory + System.getProperty("file.separator");

                                changePath(upperdirectory);
                                return;
                            }
                            if (nodetmp != null) {
                                while (!nodetmp.toString().equals(classes.getModel().getRoot().toString())) {
                                    ClassPath = nodetmp + System.getProperty("file.separator") + ClassPath;
                                    nodetmp = (DefaultMutableTreeNode) nodetmp.getParent();
                                }
                            }
                            if (pathrac.endsWith(System.getProperty("file.separator")))
                                ClassPath = pathrac + ClassPath;
                            else
                                ClassPath = pathrac + System.getProperty("file.separator") + ClassPath;
                            if (ClassPath.endsWith(".jar" + System.getProperty("file.separator")))
                                ClassPath = ClassPath.substring(0, ClassPath.length() - 1);
                            if (!node.getAllowsChildren()) {
                                CurrentClass = node.toString();
                                int pt = CurrentClass.lastIndexOf(46);
                                CurrentClass = CurrentClass.substring(0, pt);
                            } else
                                CurrentClass = "";
                        } else
                            CurrentClass = "";
                    }

                    public void mouseReleased(MouseEvent e) {
                        if (!CurrentClass.equals("")) {
                            try {
                                UniversalLoader load = new UniversalLoader();
                                Class c = load.loadClass(CurrentClass, ClassPath);
                                CurrentClass = c.getName();
                                Constructor[] m = c.getConstructors();
                                liste.setListData(m);
                                if (m.length != 0) {
                                    liste.setSelectedIndex(0);
                                    Ok.setEnabled(true);
                                }
                                /*
                                 * }catch (ClassNotFoundException r) {
                                 * JOptionPane.showMessageDialog(null,
                                 * "An error has occured : the class has not been found!"
                                 * , "Error", JOptionPane.ERROR_MESSAGE);
                                 */
                            } catch (Throwable t) {
                                // System.out.println("Error"+t.getMessage());
                                // t.printStackTrace();
                                JOptionPane.showMessageDialog(null,
                                        "An error has occured when trying to read the class methods", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            Ok.setEnabled(false);
                            liste.setListData(new Vector());
                        }

                    }
                });

                // Add the tree to the viewport of the JScrollPane
                invalidate();
                bar.getViewport().setView(classes);
                validate();
            }
        }
    }

    /**
     * Shows the differents properties of the agents
     *
     * @see javax.swing.JInternalFrame
     */

    class AgentPropertiesFrame extends JInternalFrame {
        private final JList liste = new JList();
        private final JScrollPane bar = new JScrollPane();
        private final JScrollPane bar2 = new JScrollPane();
        private final JPanel panel = new JPanel();
        private final JPanel skillPanel = new JPanel();
        private final JPanel initPanel = new JPanel();
        private final JPanel skillGeneralPanel = new JPanel();
        private final JButton bname;
        private final JButton bclass;
        private final JButton bcomputer;
        private final JButton sadd;
        private final JButton sremove;
        private final JTextField tname;
        private final JTextField tclass;
        private final JTextField tcomputer;
        private final Vector VBinit = new Vector();
        private final Vector VTinit = new Vector();
        private final Vector VBSinit = new Vector();
        private final Vector VTSinit = new Vector();
        private int nrow = 3;
        private final JTabbedPane onglets = new JTabbedPane();

        /**
         * Constructs a new AgentPropertiesFrame
         */
        AgentPropertiesFrame() {
            super("", true, false, false, false);
            setTitle(lang[47]);

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    try {
                        setSelected(true);
                        AgentProperties.requestFocus();
                    } catch (Throwable t) {
                    }
                }

                public void mouseExited(MouseEvent e) {
                    AgentsTree.requestFocus();
                }
            });
            onglets.addTab("Properties", bar);
            onglets.addTab("Skills", bar2);

            getContentPane().add(onglets);
            sadd = new JButton("add");
            sremove = new JButton("remove");
            sadd.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    chooseSkill.setVisible(true);
                }
            });
            progress.addValue(inc);

            sremove.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (liste.getSelectedValue() != null) {
                        AgentsTree.removeSkill((SkillDescriptor) liste.getSelectedValue());
                        showSkills();
                    }
                }
            });
            liste.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // String selectedskill=(String)liste.getSelectedValue();
                    showSkillInit((SkillDescriptor) liste.getSelectedValue());
                }
            });
            liste.setPreferredSize(new Dimension(200, 100));
            skillPanel.setLayout(new BorderLayout());
            skillPanel.add(liste, BorderLayout.CENTER);
            JPanel buttons = new JPanel();
            buttons.setLayout(new GridLayout(2, 1));
            buttons.add(sadd);
            buttons.add(sremove);
            skillPanel.add(buttons, BorderLayout.EAST);
            skillGeneralPanel.setLayout(new BorderLayout());
            skillGeneralPanel.add(skillPanel, BorderLayout.NORTH);
            skillGeneralPanel.add(initPanel, BorderLayout.SOUTH);
            bname = new JButton(lang[50]);
            bname.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AgentsTree.requestFocus();
                }
            });
            bclass = new JButton(lang[51]);
            bclass.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AgentClasses.setVisible(!AgentClasses.isVisible());
                    cb[2].setSelected(!cb[2].isSelected());
                    AgentsTree.setRequestFocusEnabled(true);
                    AgentsTree.requestFocus();
                }
            });
            bcomputer = new JButton(lang[52]);
            bcomputer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Computers.setVisible(!Computers.isVisible());
                    cb[1].setSelected(!cb[1].isSelected());
                    AgentsTree.setRequestFocusEnabled(true);
                    AgentsTree.requestFocus();
                }
            });

            tname = new JTextField(50);
            tname.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tname.setText(tname.getText().replace('"', ' '));
                    AgentsTree.setName(tname.getText().replace('"', ' '));
                    AgentsTree.setRequestFocusEnabled(true);
                    AgentsTree.requestFocus();
                }
            });

            tclass = new JTextField(50);
            tclass.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String classe = tclass.getText().replace('"', ' '), classPath = "";
                    if (classe.endsWith(".class"))
                        classe = classe.substring(0, classe.length() - 6);
                    classe = classe.replace('.', File.separatorChar);
                    int pt = classe.lastIndexOf(File.separatorChar);
                    if (pt != -1) {
                        classPath = classe.substring(0, pt + 1);
                        classe = classe.substring(pt + 1);
                    }
                    tclass.setText(classe);
                    AgentsTree.setClass(classPath, classe);
                    Vector V = AgentsTree.getClassInitialisers();
                    addInit(V);
                    AgentsTree.setRequestFocusEnabled(true);
                    AgentsTree.requestFocus();
                }
            });
            tcomputer = new JTextField(50);
            tcomputer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tcomputer.setText(tcomputer.getText().replace('"', ' '));
                    AgentsTree.setComputer(tcomputer.getText().replace('"', ' '));
                    AgentsTree.setRequestFocusEnabled(true);
                    AgentsTree.requestFocus();
                }
            });

            panel.setLayout(new GridLayout(nrow, 2));
            panel.setPreferredSize(new Dimension(280, nrow * 30));
            panel.add(bname);
            panel.add(tname);
            panel.add(bclass);
            panel.add(tclass);
            panel.add(bcomputer);
            panel.add(tcomputer);
            panel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    try {
                        setSelected(true); // classes.requestFocus();
                    } catch (Throwable t) {
                    }
                }
            });
            skillGeneralPanel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    try {
                        setSelected(true); // classes.requestFocus();
                    } catch (Throwable t) {
                    }
                }
            });
            invalidate();
            bar.getViewport().setView(panel);
            bar2.getViewport().setView(skillGeneralPanel);
            validate();
            activateName(false);
            activateClass(false);
            activateComputer(false);
        }

        /**
         * Determines if the agent name is editable or not
         *
         * @param a boolean to indicate if active or not
         * @see java.awt.TextComponentField#setEditable
         */
        public void activateName(boolean b) {
            tname.setEditable(b);
        }

        public boolean NameActive() {
            return tname.isEditable();
        }

        /**
         * Determines if the agent class is editable or not
         *
         * @param a boolean to indicate if active or not
         * @see java.awt.TextComponentField#setEditable
         */
        public void activateClass(boolean b) {
            tclass.setEditable(b);
        }

        public boolean ClassActive() {
            return tclass.isEditable();
        }

        /**
         * Determines if the agent computer is editable or not
         *
         * @param a boolean to indicate if active or not
         * @see java.awt.TextComponentField#setEditable
         */
        public void activateComputer(boolean b) {
            tcomputer.setEditable(b);
        }

        public boolean ComputerActive() {
            return tcomputer.isEditable();
        }

        /**
         * Sets the name of the selected agent
         *
         * @param name a string that indicate the name to set
         * @see java.awt.TextField#setText
         */
        public void setName(String name) {
            tname.setText(name);
        }

        /**
         * Sets the class of the selected agent
         *
         * @param classe a string that indicate the class to set
         * @see java.awt.TextField#setText
         */
        public void setClass(String classe) {
            int sep = classe.lastIndexOf('.');
            if (sep == -1)
                tclass.setText(classe);
            else
                tclass.setText(classe.substring(sep + 1));
        }

        /**
         * Sets the computer of the selected agent
         *
         * @param computer a string that indicate the computer to set
         * @see java.awt.TextField#setText
         */
        public void setComputer(String computer) {
            tcomputer.setText(computer);
        }

        private void setSkillEnabled(boolean enabled) {
            sadd.setEnabled(enabled);
            sremove.setEnabled(enabled);
            liste.setEnabled(enabled);
        }

        public void showSkills() {
            removeSkillsInit();
            initPanel.setPreferredSize(new Dimension(0, 0));
            Vector skills = AgentsTree.getSkills();
            if (skills == null)
                setSkillEnabled(false);
            else {
                liste.setListData(skills);
                setSkillEnabled(true);
                if (skills.size() > 0)
                    liste.setSelectedIndex(0);
            }
        }

        public void showSkillInit(SkillDescriptor desc) {
            removeSkillsInit();
            initPanel.setPreferredSize(new Dimension(0, 0));
            int compteur = 0;
            int nrows = 1;
            if (desc != null) {
                Vector param = desc.getArgs();
                for (Enumeration e = param.elements(); e.hasMoreElements(); ) {
                    final Argument arg = (Argument) e.nextElement();
                    final JButton bsinit = new JButton(arg.getType());
                    VBSinit.add(bsinit);
                    final JTextField tsinit = new JTextField(arg.getValue());
                    tsinit.setActionCommand(new Integer(compteur).toString());
                    tsinit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            ((SkillDescriptor) liste.getSelectedValue()).setValueAt(tsinit.getText(),
                                    new Integer(e.getActionCommand()).intValue());
                            AgentsTree.setRequestFocusEnabled(true);
                            AgentsTree.requestFocus();
                        }
                    });
                    VTSinit.add(tsinit);
                    initPanel.setLayout(new GridLayout(nrows, 2));
                    initPanel.setPreferredSize(new Dimension(280, nrows * 30));
                    nrows++;
                    compteur++;
                    initPanel.add(bsinit);
                    initPanel.add(tsinit);
                }
                invalidate();
                bar2.getViewport().setView(skillGeneralPanel);
                validate();
            }
        }

        public void removeSkillsInit() {
            for (int i = 0; i < VBSinit.size(); i++)
                initPanel.remove((Component) VBSinit.elementAt(i));
            for (int i = 0; i < VTSinit.size(); i++)
                initPanel.remove((Component) VTSinit.elementAt(i));
            invalidate();
            bar2.getViewport().setView(skillGeneralPanel);
            validate();
            VBSinit.removeAllElements();
            VTSinit.removeAllElements();
            initPanel.setPreferredSize(new Dimension(0, 0));
        }

        /**
         * Adds the initializers of the selected agent
         *
         * @param Init a vector containing the initialisers functions of the
         *             agent
         */

        public void addInit(Vector Init) {
            removeInit();
            if (Init != null) {
                for (int i = 0; i < Init.size(); i++) {
                    final String initial = (String) Init.elementAt(i);
                    int p = initial.indexOf("init");
                    String init1 = initial.substring(p);
                    int p1 = init1.indexOf("("), p2 = init1.lastIndexOf(".");
                    if (p2 != -1)
                        init1 = init1.substring(0, p1 + 1) + init1.substring(p2 + 1);
                    final String init = init1;
                    final JButton binit = new JButton(init.substring(4));
                    binit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            AgentsTree.setRequestFocusEnabled(true);
                            AgentsTree.requestFocus();
                        }
                    });
                    VBinit.add(binit);
                    final JTextField tinit = new JTextField(AgentsTree.getInitialiserArgument(initial), 50);
                    tinit.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            AgentsTree.addInitialiser(initial, tinit.getText());
                            AgentsTree.setRequestFocusEnabled(true);
                            AgentsTree.requestFocus();
                        }
                    });
                    VTinit.add(tinit);
                    panel.setLayout(new GridLayout(++nrow, 2));
                    panel.setPreferredSize(new Dimension(280, nrow * 30));
                    panel.add(binit);
                    panel.add(tinit);
                }
            }
            invalidate();
            bar.getViewport().setView(panel);
            validate();
        }

        /**
         * Removes the initializers inserted in this frame
         */
        public void removeInit() {
            for (int i = 0; i < VBinit.size(); i++)
                panel.remove((Component) VBinit.elementAt(i));
            for (int i = 0; i < VTinit.size(); i++)
                panel.remove((Component) VTinit.elementAt(i));
            nrow = nrow - VBinit.size();
            panel.setLayout(new GridLayout(nrow, 2));
            panel.setPreferredSize(new Dimension(280, nrow * 30));
            invalidate();
            bar.getViewport().setView(panel);
            validate();
            VBinit.removeAllElements();
            VTinit.removeAllElements();
        }
    }

    /**
     * Shows the representation of the system of agents
     *
     * @see javax.swing.JInternalFrame
     */

    class AgentsTreeFrame extends JInternalFrame {
        TreeDrawing AgentTree;

        Container pane;
        JScrollPane bar;
        JPanel panel;
        Color BColor, AColor, FColor, LColor, SLColor, SColor;
        JButton Create, Del, Org, Exec;

        /**
         * Creates a new AgentsTreeFrame
         */
        public AgentsTreeFrame() {
            super("", true, false, true, false);
            pane = getContentPane();
            AgentTree = new TreeDrawing();
            progress.addValue(inc);
            bar = new JScrollPane(AgentTree, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

            progress.addValue(inc);
            Create = new JButton(lang[40]);
            Create.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AgentProperties.removeInit();
                    AgentTree.addAgent();
                    repaint();
                    AgentTree.setRequestFocusEnabled(true);
                    AgentTree.requestFocus();
                }
            });

            Del = new JButton(lang[41]);
            Del.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AgentProperties.removeInit();
                    AgentTree.delete();
                    repaint();
                    AgentTree.setRequestFocusEnabled(true);
                    AgentTree.requestFocus();
                }
            });

            Org = new JButton(lang[42]);
            Org.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AgentTree.Organise();
                    repaint();
                    AgentTree.setRequestFocusEnabled(true);
                    AgentTree.requestFocus();
                }
            });

            Exec = new JButton(lang[43]);
            Exec.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    AgentTree.Execute();
                    AgentTree.setRequestFocusEnabled(true);
                    AgentTree.requestFocus();
                }
            });
            // Correction------------
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    try {
                        setSelected(true);
                        AgentsTree.requestFocus();
                        // active = false;
                    } catch (Throwable t) {
                    }
                }

                public void mouseExited(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                    requestFocus();
                    try {
                        setSelected(true);
                    } catch (Throwable y) {
                    }
                }

            });

            // CORRECTION---------------

            panel = new JPanel();
            panel.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    try {
                        setSelected(true);
                        AgentsTree.requestFocus();
                        // active = false;
                    } catch (Throwable t) {
                    }
                }
            });

            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
            panel.add(Create);
            panel.add(Del);
            panel.add(Org);
            panel.add(Exec);
            pane.add(panel, BorderLayout.NORTH);
            pane.add(bar, BorderLayout.CENTER);

        }

        /**
         * Returns the background color
         *
         * @return a Color representing the background color
         */
        public Color Bcolor() {
            return BColor;
        }

        /**
         * Returns the agent color
         *
         * @return a Color representing the agent color
         */
        public Color Acolor() {
            return AColor;
        }

        /**
         * Returns the font color
         *
         * @return a Color representing the font color
         */
        public Color Fcolor() {
            return FColor;
        }

        /**
         * Returns the link color
         *
         * @return a Color representing the link color
         */
        public Color Lcolor() {
            return LColor;
        }

        /**
         * Returns the direct link color
         *
         * @return a Color representing the direct link color
         */
        public Color SLcolor() {
            return SLColor;
        }

        /**
         * Returns the selection color
         *
         * @return a Color representing the selection color
         */
        public Color Scolor() {
            return SColor;
        }

        /**
         * Changes the background color
         *
         * @param C the new background color to be set
         */
        public void setBColor(Color C) {
            BColor = C;
            AgentTree.setBColor(C);
            AgentTree.setBackground(C);
        }

        /**
         * Changes the agent color
         *
         * @param C the new agent color to be set
         */
        public void setAColor(Color C) {
            AColor = C;
            AgentTree.setAColor(C);
        }

        /**
         * Changes the font color
         *
         * @param C the new font color to be set
         */
        public void setFColor(Color C) {
            FColor = C;
            AgentTree.setFColor(C);
        }

        /**
         * Changes the link color
         *
         * @param C the new link color to be set
         */
        public void setLColor(Color C) {
            LColor = C;
            AgentTree.setLColor(C);
        }

        /**
         * Changes the direct link color
         *
         * @param C the new direct link color to be set
         */
        public void setSLColor(Color C) {
            SLColor = C;
            AgentTree.setSLColor(C);
        }

        /**
         * Changes the selection color
         *
         * @param C the new selection color to be set
         */
        public void setSColor(Color C) {
            SColor = C;
            AgentTree.setSColor(C);
        }

        /**
         * Returns the name of the selected agent
         *
         * @return a string representing the agent name
         */
        public String getName() {
            return AgentTree.getName();
        }

        /**
         * Sets the selected agent name
         *
         * @param name the new name of the selected agent
         */
        public void setName(String name) {
            AgentTree.setName(name);
        }

        /**
         * Returns theclass of the selected agent(s)
         *
         * @return a string representing the agent(s) class or "" if they have
         * not the same class
         */
        public String getClasse() {
            return AgentTree.getClasse();
        }

        /**
         * Returns the classpath of the selected agent(s)
         *
         * @return a string representing the agent(s) classpath or "" if they
         * have not the same classpath
         */
        public String getClassPath() {
            return AgentTree.getClassPath();
        }

        /**
         * Returns the port of the selected agent(s)
         *
         * @return a string representing the agent(s) port or "" if they have
         *         not the same port
         */

        /**
         * Sets the selected agent(s) classpath
         *
         * @param name the new classpath of the selected agent(s)
         */
        public void setClassPath(String ClassPath) {
            AgentTree.setClassPath(ClassPath);
        }

        /**
         * Returns the computer of the selected agent(s)
         *
         * @return a string representing the agent(s) computer or "" if they
         * have not the same computer
         */
        public String getComputer() {
            return AgentTree.getComputer();
        }

        /**
         * Sets the selected agent(s) computer
         *
         * @param name the new computer of the selected agent(s)
         */
        public void setComputer(String computer) {
            AgentTree.setComputer(computer);
        }

        /**
         * Sets the selected agent(s) class
         *
         * @param name the new class of the selected agent(s)
         */
        public void setClass(String classPath, String classe) {
            AgentTree.setClass(classPath, classe);
        }

        /**
         * Adds new initialiser to the selected agent(s)
         *
         * @param func the function initialiser
         * @param arg  the function initialiser argument
         */
        public void addInitialiser(String func, String arg) {
            AgentTree.addInitialiser(func, arg);
        }

        /**
         * Adds new initialiser to the selected agent(s)
         *
         * @param classpath the class path of the agent's class
         * @param classe    the agent class
         * @return a Vector representing the agent Initialiser
         */
        public Vector getClassInitialisers(String ClassPath, String classe) {
            return AgentTree.getClassInitialisers(ClassPath, classe);
        }

        /**
         * Returns the argument of an initialiser
         *
         * @param func the function whose argument is requested
         * @return a string representing the initialiser argument
         */
        public String getInitialiserArgument(String func) {
            return AgentTree.getInitialiserArgument(func);
        }

        /**
         * Saves the selection of agents
         */
        public void savePreviousSelection() {
            AgentTree.savePreviousSelection();
        }

        /**
         * Sets as selected all the agents on the given computer
         *
         * @param computer the computer where the agents are
         */
        public void setSelection(String computer) {
            AgentTree.setSelection(computer);
        }

        /**
         * Restores the selection of agents
         */
        public void setPreviousSelection() {
            AgentTree.setPreviousSelection();
        }

        /**
         * Sets the specified boolean to indicate whether or not the agents
         * icons are shown
         *
         * @param show the boolean to be set
         */
        public void setClassesIconVisible(boolean show) {
            AgentTree.setClassesIconVisible(show);
        }

        /**
         * Sets the specified boolean to indicate whether or not the default
         * agents icons are shown
         *
         * @param show the boolean to be set
         */
        public void setDefaultIconVisible(boolean show) {
            AgentTree.setDefaultIconVisible(show);
        }

        /**
         * Sets the specified boolean to indicate whether or not this icons are
         * shown in small format
         *
         * @param show the boolean to be set
         */
        public void setSmallIcons(boolean small) {
            AgentTree.setSmallIcons(small);
        }

        /**
         * Reload the classes icons
         */
        public void repaintIcons() {
            AgentTree.repaintIcons();
        }

        /**
         * Sets the specified boolean to indicate whether or not the agents
         * names are shown
         *
         * @param show the boolean to be set
         */
        public void showAgentName(boolean show) {
            AgentTree.showAgentName(show);
        }

        /**
         * Loads a saved configuration
         *
         * @param Filename the name of the file where the configuration is saved
         * @param reset    a boolean to indicate whether or not the current
         *                 configuration is replaced or keeped
         */
        public void load(String Filename, boolean reset) {
            try {
                AgentTree.load(Filename, reset);
                AgentTree.Organise();
            } catch (NotFormatedException format) {
                if (reset)
                    AgentsTree.reset();
                JOptionPane.showMessageDialog(null, format.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Throwable z) {
                if (reset)
                    AgentsTree.reset();
                JOptionPane.showMessageDialog(null, "An error has occured when trying to read " + Filename, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            repaint();
        }

        /**
         * Saves a configuration
         *
         * @param Filename the name of the file where the configuration is going to
         *                 be saved
         */
        public void save(String Filename) {
            try {
                AgentTree.save(Filename);
                CurrentFile = Filename;
                changeFrameTitle();
            } catch (Throwable f) {
                JOptionPane.showMessageDialog(null, "An error has occured when trying to write " + Filename, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            repaint();
        }

        /**
         * Create a new configuration
         */
        public void reset() {
            AgentTree.reset();
            repaint();
        }

        /**
         * Organises the agent trees
         */
        public void Organise() {
            AgentTree.Organise();
        }

        /**
         * Executes the configuration
         */
        public void Execute() {
            AgentTree.Execute();
        }

        public boolean isReverseArrowsMode() {
            return AgentTree.isReverseArrowsMode();
        }

        public void setReverseArrowsMode(boolean value) {
            AgentTree.setReverseArrowsMode(value);
        }

        /**
         * Returns a boolean to indicate whether or not this configuration has
         * been modified
         *
         * @return <code>true</code> if this configuration has been modified
         * <code>false</code> otherwise
         */
        public boolean isModified() {
            return AgentTree.isModified();
        }

        /**
         * Returns a boolean to indicate whether or not there is a
         * multiselection
         *
         * @return <code>true</code> if there is a multiselection
         * <code>false</code> otherwise
         */
        public boolean isMultiSelection() {
            return AgentTree.isMultiSelection();
        }

        public Vector getClassInitialisers() {
            return AgentTree.getClassInitialisers();
        }

        public void selection(GraphicAgent aselected) {
            AgentTree.selection(aselected);
        }

        public void selection(GraphicLink lselected) {
            AgentTree.selection(lselected);
        }

        public void removeSkill(SkillDescriptor desc) {
            AgentTree.removeSkill(desc);
        }

        public void addSkill(SkillDescriptor desc) {
            AgentTree.addSkill(desc);
        }

        public Vector getSkills() {
            return AgentTree.getSkills();
        }

        public void setIconDirectory(String directory) {
            AgentTree.setIconDirectory(directory);
        }
    }

    class TreeDrawing extends JComponent {
        private final Tree A;
        private int mode, X, Y, Xt, Yt;
        private final FontMetrics metrics;
        private Color B, Ag, F, Li, SL, S;
        private boolean isMultiSelection = false;

        private Image offscreen, offscreenback;
        private Dimension offscreensize;
        private Graphics2D offgraphics;

        private final float[] motif = {5.0f};
        private final BasicStroke pointille = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 5.0f,
                motif, 0.0f);

        TreeDrawing() {
            mode = 0;
            setPreferredSize(new Dimension(screen.width, screen.height));
            setBackground(B);
            setAutoscrolls(true);
            Font police = new Font("Times", Font.PLAIN, 12);
            setFont(police);
            metrics = getFontMetrics(police);
            A = new Tree(this);
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        AgentProperties.setVisible(true);
                        cb[3].setSelected(true);
                    }
                }

                public void mouseEntered(MouseEvent e) {
                    try {
                        requestFocus();
                        AgentsTree.setSelected(true);
                        // active = false;
                    } catch (Throwable t) {
                    }
                }

                // ------------------------
                public void mousePressed(MouseEvent e) {
                    setRequestFocusEnabled(true);
                    requestFocus();
                    int recherche = A.selection(e.getX(), e.getY(), SwingUtilities.isRightMouseButton(e));

                    AgentProperties.setName(A.getName());
                    AgentProperties.setClass(A.getClasse());
                    AgentProperties.setComputer(A.getComputer());
                    AgentProperties.showSkills();

                    if (SwingUtilities.isRightMouseButton(e)) {
                        mode = 0;
                        isMultiSelection = false;
                        switch (recherche) {
                            case 0:
                                AgentProperties.removeInit();
                                JPopupMenu popup0 = new JPopupMenu();

                                if (cb[7].isSelected()) {
                                    JMenuItem jMenu0_1 = popup0.add(new JMenuItem(lang[30]));
                                    jMenu0_1.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            cb[4].setSelected(false);
                                            cb[5].setSelected(false);
                                            cb[7].setSelected(false);
                                            A.setClassesIconVisible(false);
                                        }
                                    });
                                } else {
                                    JMenuItem jMenu0_1 = popup0.add(new JMenuItem(lang[29]));
                                    jMenu0_1.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            cb[4].setSelected(true);
                                            cb[5].setSelected(true);
                                            cb[7].setSelected(true);
                                            A.setClassesIconVisible(true);
                                        }
                                    });
                                }

                                if (!cb[6].isSelected()) {
                                    JMenuItem jMenu0_2 = popup0.add(new JMenuItem(lang[27]));
                                    jMenu0_2.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            AgentsTree.setSmallIcons(true);
                                            cb[6].setSelected(true);
                                        }
                                    });
                                } else {
                                    JMenuItem jMenu0_2 = popup0.add(new JMenuItem(lang[28]));
                                    jMenu0_2.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            AgentsTree.setSmallIcons(false);
                                            cb[6].setSelected(false);
                                        }
                                    });
                                }

                                if (cb[8].isSelected()) {
                                    JMenuItem jMenu0_3 = popup0.add(new JMenuItem(lang[32]));
                                    jMenu0_3.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            A.showAgentName(false);
                                            cb[8].setSelected(false);
                                        }
                                    });
                                } else {
                                    JMenuItem jMenu0_3 = popup0.add(new JMenuItem(lang[31]));
                                    jMenu0_3.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            A.showAgentName(true);
                                            cb[8].setSelected(true);
                                            AgentsTree.Organise();
                                        }
                                    });
                                }
                                if (!cb[9].isSelected()) {
                                    JMenuItem jMenu0_4 = popup0.add(new JMenuItem(lang[37]));
                                    jMenu0_4.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            A.setAllAgentsFreezed(false);
                                            cb[9].setSelected(true);
                                        }
                                    });
                                } else {
                                    JMenuItem jMenu0_4 = popup0.add(new JMenuItem(lang[35]));
                                    jMenu0_4.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            A.setAllAgentsFreezed(true);
                                            cb[9].setSelected(false);
                                        }
                                    });
                                }
                                popup0.show(e.getComponent(), e.getX(), e.getY());
                                break;

                            case 1:
                                Vector V1 = A.getClassInitialisers();
                                AgentProperties.addInit(V1);
                                JPopupMenu popup1 = new JPopupMenu();

                                if (A.AgentisFreezed()) {
                                    JMenuItem jMenu1_1 = popup1.add(new JMenuItem(lang[36]));
                                    jMenu1_1.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            A.setAgentFreezed(false);
                                        }
                                    });
                                } else {
                                    JMenuItem jMenu1_1 = popup1.add(new JMenuItem(lang[34]));
                                    jMenu1_1.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            A.setAgentFreezed(true);
                                        }
                                    });
                                }

                                if (!A.getClasse().equals("")) {
                                    JMenuItem jMenu1_2 = popup1.add(new JMenuItem(lang[38]));
                                    jMenu1_2.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            final JInternalFrame temp = new JInternalFrame("", true, true, false, false);
                                            JScrollPane pane;
                                            try {

                                                UniversalLoader load = new UniversalLoader();
                                                Class c = load.loadClass(A.getClasse(), A.getClassPath());
                                                Method[] m = c.getDeclaredMethods();
                                                if (m.length != 0) {
                                                    JList l = new JList(m);
                                                    pane = new JScrollPane(l);
                                                    temp.getContentPane().add(pane);
                                                    temp.setBounds((screen.width - 500) / 2, (screen.height - 100) / 2,
                                                            500, 100);
                                                    temp.setVisible(true);
                                                    desktop.add(temp, JLayeredPane.POPUP_LAYER);
                                                } else
                                                    JOptionPane.showMessageDialog(null,
                                                            "No declared methods in this class", "Information",
                                                            JOptionPane.INFORMATION_MESSAGE);
                                            }
                                            // catch (ClassNotFoundException r) {
                                            // JOptionPane.showMessageDialog(null,
                                            // "An error has occured : the class has not been found!","Error",
                                            // JOptionPane.ERROR_MESSAGE);
                                            // }
                                            catch (Throwable t) {
                                                // JOptionPane.showMessageDialog(null,
                                                // "An error has occured when trying to read the class methods",
                                                // "Error",
                                                // JOptionPane.ERROR_MESSAGE);
                                            }
                                        }
                                    });
                                }

                                JMenu Initializers = new JMenu();
                                try {
                                    Vector Vinit = A.getClassInitialisers();
                                    if (Vinit != null && Vinit.size() != 0) {

                                        Initializers = (JMenu) popup1.add(new JMenu("Set Initializers Parameters"));
                                        for (int i = 0; i < Vinit.size(); i++) {
                                            final String init = (String) Vinit.elementAt(i);
                                            JMenuItem jMenu1_5 = Initializers.add(new JMenuItem(init));
                                            jMenu1_5.addActionListener(new ActionListener() {
                                                public void actionPerformed(ActionEvent e) {
                                                    final JInternalFrame temp = new JInternalFrame("", false, true, false,
                                                            false);
                                                    final JLabel lparam = new JLabel(init);
                                                    final JTextField tparam = new JTextField(AgentsTree
                                                            .getInitialiserArgument(init), 30);
                                                    tparam.addActionListener(new ActionListener() {
                                                        public void actionPerformed(ActionEvent e) {
                                                            A.addInitialiser(init, tparam.getText());
                                                            AgentProperties.addInit(A.getClassInitialisers());
                                                            temp.dispose();
                                                        }
                                                    });
                                                    temp.setTitle(A.getName());
                                                    temp.getContentPane().setLayout(new GridLayout(1, 2));
                                                    temp.getContentPane().add(lparam);
                                                    temp.getContentPane().add(tparam);
                                                    temp.setBounds((screen.width - 300) / 2, (screen.height - 60) / 2, 300,
                                                            60);
                                                    temp.setVisible(true);
                                                    desktop.add(temp, JLayeredPane.POPUP_LAYER);
                                                    tparam.setRequestFocusEnabled(true);
                                                    tparam.requestFocus();
                                                }
                                            });
                                        }
                                    }

                                } catch (Throwable t) {
                                    if (A.getClasse() != "")
                                        JOptionPane.showMessageDialog(null,
                                                "An error has occured when trying to read the class Initializers", "Error",
                                                JOptionPane.ERROR_MESSAGE);
                                }
                                popup1.show(e.getComponent(), e.getX(), e.getY());
                                break;

                            case 2:
                                Vector V2 = A.getClassInitialisers();
                                AgentProperties.addInit(V2);
                                mode = 4;
                                isMultiSelection = false;
                                X = e.getX();
                                Xt = X;
                                Y = e.getY();
                                Yt = Y;
                                break;

                            case 4:
                                AgentProperties.removeInit();
                                JPopupMenu popup4 = new JPopupMenu();
                                JMenuItem jMenu4_1 = popup4.add(new JMenuItem(lang[41]));
                                jMenu4_1.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        A.delete();
                                        repaint();
                                    }
                                });
                                JMenuItem jMenu4_2 = popup4.add("reverse arrows");
                                jMenu4_2.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        A.reverseArrowsMode();
                                    }
                                });

                                popup4.show(e.getComponent(), e.getX(), e.getY());

                            default:
                        }
                    } else {
                        switch (recherche) {
                            case 0:
                                mode = 2;
                                isMultiSelection = true;
                                AgentProperties.removeInit();
                                X = e.getX();
                                Xt = X;
                                Y = e.getY();
                                Yt = Y;
                                break;
                            case 1:
                                mode = 3;
                                isMultiSelection = false;
                                Vector V3 = A.getClassInitialisers();
                                AgentProperties.addInit(V3);
                                break;
                            case 2:
                                mode = 1;
                                isMultiSelection = false;
                                X = e.getX();
                                Xt = X;
                                Y = e.getY();
                                Yt = Y;
                                Vector V4 = A.getClassInitialisers();
                                AgentProperties.addInit(V4);
                                break;
                            case 4:
                                mode = 0;
                                isMultiSelection = false;
                                AgentProperties.removeInit();
                                break;
                            default:
                                mode = 0;
                                isMultiSelection = false;
                        }
                        repaint();
                    }
                }

                public void mouseReleased(MouseEvent e) {
                    if (mode == 1)
                        A.addLink(e.getX(), e.getY(), false);
                    if (mode == 4)
                        A.addSpecialLink(e.getX(), e.getY(), true);
                    if (mode == 2) {
                        A.selection(new Rectangle(Math.min(X, Xt), Math.min(Y, Yt), Math.abs(Xt - X), Math.abs(Yt - Y)));
                        AgentProperties.setName(A.getName());
                        AgentProperties.setClass(A.getClasse());
                        AgentProperties.setComputer(A.getComputer());
                        Vector V = A.getClassInitialisers();
                        AgentProperties.addInit(V);
                        AgentProperties.showSkills();
                    }
                    mode = 0;
                    repaint();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    int x = e.getX(), y = e.getY();
                    if (x < 30)
                        x = A.getAgentDimension().width / 2 + 10;
                    if (x > getSize().width - A.getAgentDimension().width)
                        x = getSize().width - A.getAgentDimension().width - 1;
                    if (y < 10)
                        y = A.getAgentDimension().height / 2 + 15;
                    if (y > getSize().height - A.getAgentDimension().height)
                        y = getSize().height - A.getAgentDimension().height / 2 - 1;
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (mode == 3)
                            A.drag(x, y);
                        else {
                            Xt = x;
                            Yt = y;
                        }
                        scrollRectToVisible(new Rectangle(x, y, A.getAgentDimension().width,
                                A.getAgentDimension().height));
                        repaint();
                    } else if (mode == 4) {
                        Xt = x;
                        Yt = y;
                        scrollRectToVisible(new Rectangle(x, y, A.getAgentDimension().width,
                                A.getAgentDimension().height));
                        repaint();
                    }
                }
            });
        }

        public void setNewSize(Dimension d) {
            Dimension lastDim = getSize();
            setSize(new Dimension(Math.max(lastDim.width, d.width), Math.max(lastDim.height, d.height)));
            repaint();
        }

        public Vector getClassInitialisers() {
            return A.getClassInitialisers();
        }

        public void setBColor(Color C) {
            B = C;
        }

        public void setAColor(Color C) {
            Ag = C;
        }

        public void setFColor(Color C) {
            F = C;
        }

        public void setLColor(Color C) {
            Li = C;
        }

        public void setSLColor(Color C) {
            SL = C;
        }

        public void setSColor(Color C) {
            S = C;
        }

        public boolean isModified() {
            return A.isModified();
        }

        public boolean isMultiSelection() {
            return isMultiSelection;
        }

        public void addAgent() {
            A.addAgent(metrics);
        }

        public void delete() {
            A.delete();
        }

        public void Organise() {
            A.Organise();
        }

        public void Execute() {
            A.Execute();
        }

        public void load(String s, boolean reset) throws Throwable {
            A.load(s, reset, metrics);
        }

        public void save(String s) throws Throwable {
            BufferedWriter out = new BufferedWriter(new FileWriter(s));
            A.save(out);
            A.setModified(false);

        }

        public void reset() {
            A.reset();
            mode = 0;
        }

        public String getName() {
            return A.getName();
        }

        public void setName(String name) {
            A.setName(name);
        }

        public String getClasse() {
            return A.getClasse();
        }

        public String getClassPath() {
            return A.getClassPath();
        }

        public void setClassPath(String ClassPath) {
            A.setClassPath(ClassPath);
        }

        public String getComputer() {
            return A.getComputer();
        }

        public void setComputer(String computer) {
            A.setComputer(computer);
        }

        public void showAgentName(boolean show) {
            A.showAgentName(show);
        }

        public void setClass(String classe, String ClassPath) {
            A.setClass(classe, ClassPath);
        }

        public void addInitialiser(String func, String arg) {
            A.addInitialiser(func, arg);
        }

        public Vector getClassInitialisers(String ClassPath, String classe) {
            return A.getClassInitialisers(ClassPath, classe);
        }

        public String getInitialiserArgument(String func) {
            return A.getInitialiserArgument(func);
        }

        public void savePreviousSelection() {
            A.savePreviousSelection();
        }

        public void setSelection(String computer) {
            A.setSelection(computer);
        }

        public void setPreviousSelection() {
            A.setPreviousSelection();
        }

        public void setClassesIconVisible(boolean show) {
            A.setClassesIconVisible(show);
        }

        public void setDefaultIconVisible(boolean show) {
            A.setDefaultIconVisible(show);
        }

        public void setSmallIcons(boolean small) {
            A.setSmallIcons(small);
        }

        public void repaintIcons() {
            A.repaintIcons();
        }

        public void setIconDirectory(String directory) {
            A.setIconDirectory(directory);
        }

        public void selection(GraphicAgent aselected) {
            A.selection(aselected);
            Point p = A.getPosition();
            if (p != null)
                scrollRectToVisible(new Rectangle(Math.max(0, p.x - 40), Math.max(0, p.y - 30), 80, 60));
            repaint();
        }

        public void selection(GraphicLink lselected) {
            A.selection(lselected);
            Point p = A.getPosition();
            if (p != null)
                scrollRectToVisible(new Rectangle(Math.max(0, p.x - 40), Math.max(0, p.y - 30), 80, 60));
            repaint();
        }

        public void removeSkill(SkillDescriptor desc) {
            A.removeSkill(desc);
        }

        public void addSkill(SkillDescriptor desc) {
            A.addSkill(desc);
        }

        public Vector getSkills() {
            return A.getSkills();
        }

        public boolean isReverseArrowsMode() {
            return A.isReverseArrowsMode();
        }

        public void setReverseArrowsMode(boolean value) {
            A.setReverseArrowsMode(value);
        }

        public void update(Graphics g) {
            Dimension d = getSize();

            if ((offscreen == null) || (d.width != offscreensize.width) || (d.height != offscreensize.height)) {
                offscreen = createImage(d.width, d.height);
                offscreensize = d;
                offgraphics = (Graphics2D) offscreen.getGraphics();
            }

            offgraphics.setColor(B);
            offgraphics.fillRect(0, 0, d.width, d.height);
            offgraphics.setColor(S);

            switch (mode) {
                case 1:
                    offgraphics.drawLine(X, Y, Xt, Yt);
                    break;
                case 2:
                    offgraphics.setStroke(pointille);
                    offgraphics.drawRoundRect(Math.min(X, Xt), Math.min(Y, Yt), Math.abs(Xt - X), Math.abs(Yt - Y), 8, 8);
                    offgraphics.setStroke(new BasicStroke());
                    break;
                case 4:
                    offgraphics.setStroke(pointille);
                    offgraphics.drawLine(X, Y, Xt, Yt);
                    offgraphics.setStroke(new BasicStroke());
                    break;
                default:
            }

            A.draw(offgraphics, pointille, Ag, F, Li, SL, S, B);
            g.drawImage(offscreen, 0, 0, null);
            offscreenback = offscreen;
        }

        public void paint(Graphics g) {
            update(g);
        }
    }

    class Tree {
        private final Vector VAgents;
        private final Vector VLinks;
        private final Vector VSpecialLinks;
        private final Vector VSelected;
        private final Vector preselect;
        private GraphicAgent Parent;
        private GraphicLink lselectionne;
        private int selected;
        private int num;
        private int abs;
        private int ord;
        private int L;
        private int l;
        private final int decalX;
        private final int decalY;
        private boolean isModified = false, SmallIcons = false;
        private final TreeDrawing TD;
        private Image defaultIcon = null;
        private boolean reversearrowsmode = false;
        private Image smalldefaultIcon = null;

        public Tree(TreeDrawing jc) {
            TD = jc;
            num = 0;
            L = 90;
            l = 44;
            abs = L / 2 + 12;
            ord = l / 2 + 12;
            decalX = 8;
            decalY = 18;
            VAgents = new Vector();
            VLinks = new Vector();
            VSpecialLinks = new Vector();
            VSelected = new Vector();
            preselect = new Vector();
            String IconFilename = "default.gif";
            Dimension d1;
            Dimension d2;
            Image dessin;
            d1 = new Dimension(75, 30);
            d2 = new Dimension(32, 32);

            dessin = getToolkit().getImage(this.getClass().getResource(IconFilename));
            defaultIcon = dessin.getScaledInstance(d1.width, d1.height, Image.SCALE_DEFAULT);
            smalldefaultIcon = dessin.getScaledInstance(d2.width, d2.height, Image.SCALE_DEFAULT);
            MediaTracker tracker = new MediaTracker(TD);
            tracker.addImage(defaultIcon, 0, d1.width, d1.height);
            tracker.addImage(smalldefaultIcon, 1, d2.width, d2.height);
            try {
                tracker.waitForAll(500);
            } catch (InterruptedException e) {
            }
        }

        public void setIconDirectory(String directory) {

            if (!IconDirectory.endsWith(System.getProperty("file.separator")))
                IconDirectory = IconDirectory + System.getProperty("file.separator");
            String IconFilename = IconDirectory + "default.gif";
            Dimension d1;
            Dimension d2;
            Image dessin;
            d1 = new Dimension(75, 30);
            d2 = new Dimension(32, 32);
            if (new File(IconFilename).exists()) {
                dessin = getToolkit().getImage(IconFilename);
                defaultIcon = dessin.getScaledInstance(d1.width, d1.height, Image.SCALE_DEFAULT);
                smalldefaultIcon = dessin.getScaledInstance(d2.width, d2.height, Image.SCALE_DEFAULT);
                MediaTracker tracker = new MediaTracker(TD);
                tracker.addImage(defaultIcon, 0, d1.width, d1.height);
                tracker.addImage(smalldefaultIcon, 1, d2.width, d2.height);
                try {
                    tracker.waitForAll(500);
                } catch (InterruptedException e) {
                }
            }
        }

        public void reverseArrowsMode() {
            this.setReverseArrowsMode(!reversearrowsmode);
        }

        public boolean isReverseArrowsMode() {
            return reversearrowsmode;
        }

        public void setReverseArrowsMode(boolean value) {
            reversearrowsmode = value;
            for (Enumeration e = VLinks.elements(); e.hasMoreElements(); ) {
                ((GraphicLink) e.nextElement()).setReverseArrowMode(value);
            }
            for (Enumeration e = VSpecialLinks.elements(); e.hasMoreElements(); ) {
                ((GraphicLink) e.nextElement()).setReverseArrowMode(value);

            }
        }

        public void load(String s, boolean reset, FontMetrics metrics) throws Exception {
            int treeSize, ComputerPort, posX, posY;
            Vector newTree = new Vector(), newLink = new Vector(), SpecialLink = new Vector();
            ConfigReader config = new ConfigReader();
            config.load(new BufferedReader(new FileReader(s)));
            VSelected.removeAllElements();
            newTree = config.getAgents();
            newLink = config.getLinks();
            SpecialLink = config.getSpecialLinks();
            AgentProperties.removeInit();
            for (int i = 0; i < newTree.size(); i++) {
                GraphicAgent tmp = (GraphicAgent) newTree.elementAt(i);
                tmp.setMetrics(metrics);
                tmp.setDimension(new Dimension(L, l));
                setIcon(tmp, tmp.getClasse());
                if (tmp.isToCollapse())
                    tmp.Collapse();
                tmp.showAgentName(cb[8].isSelected());
                tmp.setSmallIcon(cb[6].isSelected());
                VAgents.addElement(tmp);
            }
            for (int i = 0; i < newLink.size(); i++) {
                GraphicLink link = (GraphicLink) newLink.elementAt(i);
                VLinks.addElement(link);
            }
            for (int i = 0; i < SpecialLink.size(); i++) {
                GraphicLink link = (GraphicLink) SpecialLink.elementAt(i);
                VSpecialLinks.addElement(link);
            }
            if (reset) {
                CurrentFile = s;
                changeFrameTitle();
                isModified = false;
            } else {
                if (!CurrentFile.endsWith("*")) {
                    CurrentFile = CurrentFile + "*";
                    changeFrameTitle();
                    isModified = true;
                }
            }
        }

        public void save(BufferedWriter out) throws Throwable {
            GraphicAgent agent;
            GraphicLink link;
            for (int i = 0; i < VAgents.size(); i++) {
                agent = (GraphicAgent) VAgents.elementAt(i);
                out.write("DEFAGENT ");
                out.write("\"" + agent.getName() + "\"\n");
                out.write("  CLASSE ");
                out.write("\"" + agent.getClasse() + "\"\n");
                out.write("  CLASSPATH ");
                out.write("\"" + agent.getClassPath().replace(File.separatorChar, '+') + "\"\n");
                out.write("  ISCOLLAPSED ");
                out.write("\"" + agent.isCollapsed() + "\"\n");
                out.write("  COMPUTER ");
                out.write("\"" + agent.getComputer() + "\"\n");
                out.write("  POSITION ");
                out.write(agent.position().x + " ");
                out.write(agent.position().y + "\n");
                for (Enumeration e = agent.getInitialiserFunctions().elements(); e.hasMoreElements(); ) {
                    String function = (String) e.nextElement();
                    out.write("  INIT \"" + function + "\" \""
                            + agent.getInitialiserArgument(function).replace('"', '^') + "\"\n");
                }
                for (Enumeration e = agent.getSkills().elements(); e.hasMoreElements(); ) {
                    SkillDescriptor skill = (SkillDescriptor) e.nextElement();
                    out.write("  DEFSKILL \"" + skill.getClasse() + "\"");
                    out.write("     CONSTRUCTOR \"" + skill.getConstructor() + "\"\n");
                    out.write("     CLASSPATH \"" + skill.getClassPath() + "\"\n");
                    for (Enumeration a = skill.getArgs().elements(); a.hasMoreElements(); ) {
                        Argument arg = (Argument) a.nextElement();
                        out.write("     ARG \"" + arg.getValue() + "\"\n");
                    }
                    out.write("   ENDDEF\n");

                }
                out.write("ENDDEF\n");
            }
            for (int i = 0; i < VLinks.size(); i++) {
                link = (GraphicLink) VLinks.elementAt(i);
                out.write("DEFLINK\n");
                out.write("  PARENT \"" + link.GetParent().getName() + "\"\n");
                out.write("  CHILD \"" + link.getChild().getName() + "\"\n");
                out.write("ENDDEF\n");
            }
            for (int i = 0; i < VSpecialLinks.size(); i++) {
                link = (GraphicLink) VSpecialLinks.elementAt(i);
                out.write("DEFSPECIALLINK\n");
                out.write("  PARENT \"" + link.GetParent().getName() + "\"\n");
                out.write("  CHILD \"" + link.getChild().getName() + "\"\n");
                out.write("ENDDEF\n");
            }
            out.close();
        }

        public void removeSkill(SkillDescriptor desc) {
            if (VSelected.size() == 1)
                ((GraphicAgent) VSelected.firstElement()).removeSkill(desc);
        }

        public void addSkill(SkillDescriptor desc) {

            if (VSelected.size() != 0)
                ((GraphicAgent) VSelected.firstElement()).addSkill(desc);
        }

        public Vector getSkills() {
            if (VSelected.size() == 1)
                return ((GraphicAgent) VSelected.firstElement()).getSkills();
            return null;
        }

        public void reset() {
            num = 0;
            abs = L / 2 + 12;
            ord = l / 2 + 12;
            VAgents.removeAllElements();
            VSelected.removeAllElements();
            VLinks.removeAllElements();
            VSpecialLinks.removeAllElements();
            AgentProperties.setName("");
            AgentProperties.setClass("");
            AgentProperties.setComputer("");
            AgentProperties.activateName(false);
            AgentProperties.activateClass(false);
            AgentProperties.activateComputer(false);
            AgentProperties.removeInit();
            AgentProperties.showSkills();
            isModified = false;
            CurrentFile = "New configuration";
            changeFrameTitle();
        }

        public void setMetrics(FontMetrics metric) {
            for (int i = 0; i < VAgents.size(); i++)
                ((GraphicAgent) VAgents.elementAt(i)).setMetrics(metric);
        }

        public void setDimension(Dimension dim) {
            L = dim.width;
            l = dim.height;
            for (int i = 0; i < VAgents.size(); i++)
                ((GraphicAgent) VAgents.elementAt(i)).setDimension(dim);
        }

        public void addAgent(FontMetrics metrics) {
            GraphicAgent a = new GraphicAgent("Agent" + num, new Point(abs, ord), new Dimension(L, l), metrics,
                    cb[8].isSelected(), cb[6].isSelected());
            TD.scrollRectToVisible(new Rectangle(abs - (L / 2), ord - (l / 2 + 10), L, l * 3 / 2));
            if (abs < (250 - 2 * L))
                abs += L + decalX;
            else {
                if (ord + 2 * l > TD.getHeight()) {
                    TD.setNewSize(new Dimension(TD.getWidth(), TD.getHeight() + 2 * l));
                    repaint();
                }
                ord += l + decalY;
                abs = L / 2 + 12;
            }
            num++;
            VSelected.removeAllElements();
            VAgents.addElement(a);
            VSelected.addElement(a);
            selected = 1;
            setAgentFreezed(!cb[9].isSelected());
            AgentProperties.setName(a.getName());
            AgentProperties.setClass(a.getClasse());
            AgentProperties.setComputer(a.getComputer());
            AgentProperties.activateName(true);
            AgentProperties.activateClass(true);
            AgentProperties.activateComputer(true);
            AgentProperties.showSkills();
            if (!CurrentFile.endsWith("*")) {
                CurrentFile = CurrentFile + "*";
                changeFrameTitle();
                isModified = true;
            }
        }

        public Dimension getAgentDimension() {
            return new Dimension(L, l);
        }

        public String getName() {
            if (VSelected.size() == 1) {
                AgentProperties.activateName(true);
                return ((GraphicAgent) VSelected.firstElement()).getName();
            } else {
                AgentProperties.activateName(false);
                return "";
            }
        }

        public void setName(String name) {
            ((GraphicAgent) VSelected.firstElement()).setName(name);
            if (!CurrentFile.endsWith("*")) {
                CurrentFile = CurrentFile + "*";
                changeFrameTitle();
                isModified = true;
            }
            repaint();
        }

        public Point getPosition() {
            if (VSelected.size() == 0)
                return null;
            return ((GraphicAgent) VSelected.firstElement()).position();
        }

        public String getClasse() {
            if (VSelected.size() < 1) {
                AgentProperties.activateClass(false);
                return "";
            } else {
                int cpt = 1;
                AgentProperties.activateClass(true);
                String classe = ((GraphicAgent) VSelected.firstElement()).getClassPath()
                        + ((GraphicAgent) VSelected.firstElement()).getClasse();
                for (int i = 1; i < VSelected.size(); i++) {
                    GraphicAgent tmp = (GraphicAgent) VSelected.elementAt(i);
                    if (classe.equals(tmp.getClassPath() + tmp.getClasse()))
                        cpt++;
                }
                if (cpt == VSelected.size())
                    return ((GraphicAgent) VSelected.firstElement()).getClasse();
                else
                    return "";
            }
        }

        public String getClassPath() {
            if (VSelected.size() < 1)
                return "";
            else {
                int cpt = 1;
                String classpath = ((GraphicAgent) VSelected.firstElement()).getClassPath();
                for (int i = 1; i < VSelected.size(); i++)
                    if (classpath.equals(((GraphicAgent) VSelected.elementAt(i)).getClassPath()))
                        cpt++;
                if (cpt == VSelected.size())
                    return classpath;
                else
                    return "";
            }
        }

        public void setClassPath(String ClassPath) {
            for (int i = 0; i < VSelected.size(); i++)
                ((GraphicAgent) VSelected.elementAt(i)).setClassPath(ClassPath);
        }

        public String getComputer() {
            if (VSelected.size() < 1) {
                AgentProperties.activateComputer(false);
                return "";
            } else {
                int cpt = 1;
                AgentProperties.activateComputer(true);
                String computer = ((GraphicAgent) VSelected.firstElement()).getComputer();
                for (int i = 1; i < VSelected.size(); i++)
                    if (computer.equals(((GraphicAgent) VSelected.elementAt(i)).getComputer()))
                        cpt++;
                if (cpt == VSelected.size())
                    return computer;
                else
                    return "";
            }
        }

        public void setComputer(String computer) {
            for (int i = 0; i < VSelected.size(); i++)
                ((GraphicAgent) VSelected.elementAt(i)).setComputer(computer);
            if (!CurrentFile.endsWith("*")) {
                CurrentFile = CurrentFile + "*";
                changeFrameTitle();
                isModified = true;
            }
            repaint();
        }

        public Class getClass(String classPath, String classe) {
            UniversalLoader load = new UniversalLoader();
            Class c = load.loadClass(classe, classPath);
            return c;
        }

        public void setClass(String classPath, String classe) {
            String classename = classe.replace('/', '.');
            classename = classename.replace(System.getProperty("file.separator").charAt(0), '.');
            setClassPath(classPath);
            AgentProperties.setClass(classename);
            for (int i = 0; i < VSelected.size(); i++) {
                GraphicAgent tmp = (GraphicAgent) VSelected.elementAt(i);
                tmp.resetInitialisers();
                Class c = getClass(classPath, classe);
                if (c != null)
                    classename = c.getName();
                tmp.setClass(classename);
                setIcon(tmp, classename);
                Vector V = getClassInitialisers(c);
                AgentProperties.addInit(V);
                for (Enumeration e = V.elements(); e.hasMoreElements(); )
                    tmp.addInitialiser((String) e.nextElement(), "");
            }
            if (!CurrentFile.endsWith("*")) {
                CurrentFile = CurrentFile + "*";
                changeFrameTitle();
                isModified = true;
            }
            repaint();
        }

        public Vector getClassInitialisers(String ClassPath, String classe) {
            Class c = getClass(ClassPath, classe);
            return getClassInitialisers(c);
        }

        public Vector getClassInitialisers(Class c) {
            Vector initial = new Vector();
            try {
                final Method[] clist = c.getMethods();
                for (int i = 0; i < clist.length; i++) {
                    int p = clist[i].toString().indexOf("init");
                    if (p != -1) {

                        initial.add(clist[i].toString());
                    }
                }
            } catch (Throwable T) {
            }
            return initial;
        }

        public Vector getClassInitialisers() {
            if (VSelected.size() == 0)
                return new Vector();
            return ((GraphicAgent) VSelected.firstElement()).getInitialiserFunctions();
        }

        public void addInitialiser(String function, String arg) {
            for (int i = 0; i < VSelected.size(); i++) {
                GraphicAgent tmp = (GraphicAgent) VSelected.elementAt(i);
                tmp.addInitialiser(function, arg);
            }
            if (!CurrentFile.endsWith("*")) {
                CurrentFile = CurrentFile + "*";
                changeFrameTitle();
                isModified = true;
            }
            repaint();
        }

        public String getInitialiserArgument(String func) {
            int cpt = 1;
            String arg = ((GraphicAgent) VSelected.firstElement()).getInitialiserArgument(func);
            for (int i = 1; i < VSelected.size(); i++)
                if (arg.equals(((GraphicAgent) VSelected.elementAt(i)).getInitialiserArgument(func)))
                    cpt++;
            if (cpt == VSelected.size())
                return arg;
            else
                return "";
        }

        public void setIcon(GraphicAgent agent, String classename) {
            if (classename.equals(""))
                return;
            int sep = (classename.lastIndexOf('.'));
            String classe;
            if (sep != -1)
                classe = classename.substring(classename.lastIndexOf('.'));
            else
                classe = classename;
            String IconFilename = IconDirectory + classe + ".gif";
            Image dessin;
            Dimension d;
            if (!SmallIcons)
                d = new Dimension(75, 30);
            else
                d = new Dimension(32, 32);
            if ((new File(IconFilename)).exists()) {
                MediaTracker tracker = new MediaTracker(TD);
                dessin = getToolkit().getImage(IconFilename);
                dessin = dessin.getScaledInstance(d.width, d.height, Image.SCALE_DEFAULT);
                tracker.addImage(dessin, 0, d.width, d.height);
                try {
                    tracker.waitForAll(100);
                } catch (InterruptedException e) {
                }
                agent.setIconVisible(cb[4].isSelected() && dessin != null);
            } else {
                if (SmallIcons)
                    dessin = smalldefaultIcon;
                else
                    dessin = defaultIcon;
                IconFilename = IconDirectory + "default.gif";
                agent.setIconVisible(cb[5].isSelected());
            }
            agent.setIcon(dessin, IconFilename, d);
        }

        public void repaintIcons() {
            for (int i = 0; i < VAgents.size(); i++) {
                GraphicAgent tmp = (GraphicAgent) VAgents.elementAt(i);
                setIcon(tmp, tmp.getClasse());
            }
            repaint();
        }

        public void savePreviousSelection() {
            preselect.removeAllElements();
            for (int i = 0; i < VSelected.size(); i++)
                preselect.add(VSelected.elementAt(i));
        }

        public void setSelection(String computer) {
            VSelected.removeAllElements();
            repaint();
            selected = 0;
            for (int i = 0; i < VAgents.size(); i++) {
                if (((GraphicAgent) VAgents.elementAt(i)).getComputer().toLowerCase().equals(computer)) {
                    VSelected.add(VAgents.elementAt(i));
                    selected = 1;
                }
            }
            AgentProperties.setName(getName());
            AgentProperties.setClass(getClasse());
            AgentProperties.setComputer(getComputer());
        }

        public void setPreviousSelection() {
            VSelected.removeAllElements();
            repaint();
            selected = 0;
            for (int i = 0; i < preselect.size(); i++) {
                VSelected.add(preselect.elementAt(i));
                selected = 1;
            }
            AgentProperties.setName(getName());
            AgentProperties.setClass(getClasse());
            AgentProperties.setComputer(getComputer());
        }

        public boolean isModified() {
            return isModified;
        }

        public void setModified(boolean val) {
            isModified = false;
        }

        public boolean SmallIcons() {
            return SmallIcons;
        }

        public void setClassesIconVisible(boolean show) {
            for (int i = 0; i < VAgents.size(); i++) {
                GraphicAgent tmp = (GraphicAgent) VAgents.elementAt(i);
                if (!tmp.getClasse().equals(""))
                    tmp.setIconVisible(show);
            }
            setDefaultIconVisible(cb[5].isSelected() && cb[4].isSelected());
            repaint();
        }

        public void setDefaultIconVisible(boolean show) {
            String IconFilename = IconDirectory + "default.gif";

            for (int i = 0; i < VAgents.size(); i++) {
                GraphicAgent agent = (GraphicAgent) VAgents.elementAt(i);
                if (agent.getIcon() == smalldefaultIcon || agent.getIcon() == defaultIcon)
                    agent.setIconVisible(show);
                else if (!agent.getClasse().equals("") && agent.getIconDescription().equals("")) {
                    if (SmallIcons)
                        agent.setIcon(smalldefaultIcon, IconFilename, agent.getIconDimension());
                    else
                        agent.setIcon(defaultIcon, IconFilename, agent.getIconDimension());
                    agent.setIconVisible(show);
                }

                repaint();
            }
        }

        public void setSmallIcons(boolean small) {
            SmallIcons = small;
            for (int i = 0; i < VAgents.size(); i++)
                ((GraphicAgent) VAgents.elementAt(i)).setSmallIcon(small);

            if (SmallIcons) {
                L = l = 32;
                setDimension(new Dimension(L, l));
                repaintIcons();
                cb[4].setSelected(true);
                cb[5].setSelected(true);
                cb[7].setSelected(true);
                setClassesIconVisible(true);
            } else {
                if (cb[8].isSelected()) {
                    L = 90;
                    l = 44;
                } else {
                    L = 75;
                    l = 30;
                }
                setDimension(new Dimension(L, l));
                repaintIcons();
            }
            Organise();
        }

        public void showAgentName(boolean show) {
            for (int i = 0; i < VAgents.size(); i++)
                ((GraphicAgent) VAgents.elementAt(i)).showAgentName(show);
            if (show) {
                setSmallIcons(false);
                cb[6].setSelected(false);
                L = 90;
                l = 44;
            } else {
                if (cb[6].isSelected()) {
                    L = l = 32;
                } else {
                    L = 75;
                    l = 30;
                }
            }
            setDimension(new Dimension(L, l));
            repaint();
        }

        public boolean AgentisFreezed() {
            return ((GraphicAgent) VSelected.firstElement()).isFreezed();
        }

        public void setAgentFreezed(boolean freeze) {
            ((GraphicAgent) VSelected.firstElement()).setFreeze(freeze);
        }

        public void setAllAgentsFreezed(boolean freeze) {
            for (int i = 0; i < VAgents.size(); i++)
                ((GraphicAgent) VAgents.elementAt(i)).setFreeze(freeze);
        }

        public void drag(int x, int y) {
            if (VSelected.isEmpty())
                return;

            Point p = new Point(x, y);
            int decalx, decaly;
            GraphicAgent tmp = (GraphicAgent) VSelected.firstElement();

            if (selected == 1) {
                if (!tmp.isFreezed())
                    tmp.changePosition(p);
                else
                    tmp.shiftPosition(p.x - tmp.position().x, p.y - tmp.position().y);
            }
        }

        public void selection(GraphicAgent aselected) {
            VSelected.removeAllElements();
            selected = 0;
            for (Enumeration e = VAgents.elements(); e.hasMoreElements(); ) {
                GraphicAgent tmp = (GraphicAgent) e.nextElement();
                if (tmp.equals(aselected)) {
                    VSelected.addElement(tmp);
                    selected = 1;
                    AgentProperties.setName(tmp.getName());
                    AgentProperties.setClass(tmp.getClasse());
                    AgentProperties.setComputer(tmp.getComputer());
                    AgentProperties.showSkills();
                    return;
                }
            }
        }

        public void selection(GraphicLink lselected) {
            selected = 2;
            for (Enumeration e = VLinks.elements(); e.hasMoreElements(); ) {
                GraphicLink tmp = (GraphicLink) e.nextElement();
                if (tmp.compare(lselected)) {
                    lselectionne = tmp;
                    return;
                }
            }
            for (Enumeration e = VSpecialLinks.elements(); e.hasMoreElements(); ) {
                GraphicLink tmp = (GraphicLink) e.nextElement();
                if (tmp.compare(lselected)) {
                    lselectionne = tmp;
                    return;
                }
            }
        }

        public int selection(int x, int y, boolean all) {
            Point position = new Point(x, y);
            int compteur = VAgents.size() - 1, found = 0;
            boolean linkfound = false;
            GraphicAgent tmp = new GraphicAgent(cb[8].isSelected());
            GraphicLink tmpl;

            VSelected.removeAllElements();
            selected = 0;
            while (compteur >= 0 && found == 0) {
                tmp = (GraphicAgent) VAgents.elementAt(compteur);
                found = tmp.isSelected(position);
                compteur--;
            }
            if (found != 0) {
                if (found == 3) {
                    boolean PrevState = tmp.isCollapsed();
                    if (tmp.isCollapsed()) {
                        if (!all)
                            tmp.Expand();
                        else
                            tmp.ExpandAll();
                    } else {
                        if (!all)
                            tmp.Collapse();
                        else
                            tmp.CollapseAll();
                    }
                    if (PrevState != tmp.isCollapsed()) {
                        tmp.setFreeze(false);
                        Point p = tmp.position();
                        TreeOrganiser Tree = new TreeOrganiser(tmp, 80, 80, 20);
                        Tree.Organise();
                        tmp.setFreeze(true);
                        TD.setNewSize(new Dimension(Tree.widthMax() + p.x - tmp.position().x, Tree.heightMax() + p.y
                                - tmp.position().y));
                        tmp.shiftPosition(p.x - tmp.position().x, p.y - tmp.position().y);
                    }
                    repaint();
                }
                selected = 1;
                VAgents.removeElement(tmp);
                VAgents.addElement(tmp);
                VSelected.addElement(tmp);
                return found;
            }
            compteur = 0;
            while (compteur < VLinks.size() && !linkfound) {
                tmpl = (GraphicLink) VLinks.elementAt(compteur);
                linkfound = tmpl.isSelected(position);
                compteur++;
                if (linkfound) {
                    selected = 2;
                    lselectionne = tmpl;
                    return 4;
                }
            }
            compteur = 0;
            while (compteur < VSpecialLinks.size() && !linkfound) {
                tmpl = (GraphicLink) VSpecialLinks.elementAt(compteur);
                linkfound = tmpl.isSelected(position);
                compteur++;
                if (linkfound) {
                    selected = 2;
                    lselectionne = tmpl;
                    return 4;
                }
            }
            return 0;
        }

        public void selection(Rectangle R) {
            GraphicAgent tmp = new GraphicAgent(cb[8].isSelected());

            selected = 0;
            VSelected.removeAllElements();
            for (int i = 0; i < VAgents.size(); i++) {
                tmp = (GraphicAgent) VAgents.elementAt(i);
                if (tmp.isSelected(R)) {
                    VSelected.addElement(tmp);
                    selected = 1;
                }
            }
        }

        public void draw(Graphics g, BasicStroke p, Color AColor, Color FColor, Color LColor, Color SLColor,
                         Color SColor, Color BColor) {
            Graphics2D graph = (Graphics2D) g;
            GraphicAgent tmp;
            GraphicLink tmpl;

            graph.setStroke(p);
            for (int i = 0; i < VSpecialLinks.size(); i++) {
                tmpl = (GraphicLink) VSpecialLinks.elementAt(i);
                tmpl.draw(g, (tmpl == lselectionne) && (selected == 2), SLColor, SColor);
            }
            graph.setStroke(new BasicStroke());
            for (int i = 0; i < VLinks.size(); i++) {
                tmpl = (GraphicLink) VLinks.elementAt(i);
                tmpl.draw(g, (tmpl == lselectionne) && (selected == 2), LColor, SColor);
            }
            for (int i = 0; i < VAgents.size(); i++) {
                tmp = (GraphicAgent) VAgents.elementAt(i);
                tmp.draw(g, (VSelected.contains(tmp)) && (selected == 1), AColor, FColor, SColor, BColor);
            }
        }

        public void addSpecialLink(GraphicAgent target, GraphicAgent source, boolean special) {
            GraphicLink newlink;
            Vector SpLink, SLink;
            SpLink = target.getSpecialChildren();
            SLink = target.getChildren();

            if (source.isAParent(target))
                return;
            if (!SpLink.contains(source) && !SLink.contains(source)) {
                newlink = new GraphicLink(target, source, special);
                newlink.setReverseArrowMode(reversearrowsmode);
                if (target.addSpecialChild(source)) {
                    source.addSpecialParent(target);

                    VSpecialLinks.addElement(newlink);
                }
            }
            if (!CurrentFile.endsWith("*")) {
                CurrentFile = CurrentFile + "*";
                changeFrameTitle();
                isModified = true;
            }
        }

        public void addSpecialLink(int x, int y, boolean special) {
            if (VSelected.isEmpty())
                return;
            Point position = new Point(x, y);
            GraphicAgent tmp, source, target = new GraphicAgent(cb[8].isSelected());
            boolean found = false;

            source = (GraphicAgent) VSelected.firstElement();
            for (int i = 0; i < VAgents.size(); i++) {
                tmp = (GraphicAgent) VAgents.elementAt(i);
                if (tmp.isSelected(position) > 0 && tmp != source) {
                    found = true;
                    target = tmp;
                }
            }
            if (found)
                addSpecialLink(target, source, special);
        }

        public void addLink(GraphicAgent target, GraphicAgent source, boolean special) {
            GraphicLink newlink, linkpred, detruire = null;
            if (source.isAParent(target))
                return;
            if (source.hasAlreadyParent()) {
                for (int j = 0; j < VLinks.size(); j++) {
                    linkpred = (GraphicLink) VLinks.elementAt(j);
                    if (linkpred.getChild() == source) {
                        linkpred.delete();
                        detruire = linkpred;
                    }
                }
                VLinks.removeElement(detruire);
            }
            if (target != source) {
                newlink = new GraphicLink(target, source, special);
                newlink.setReverseArrowMode(reversearrowsmode);
                if (target.addChild(source)) {
                    Vector SpChild = target.getSpecialChildren();
                    GraphicLink ltmp;

                    if (SpChild.contains(source)) {
                        target.removeSpecialChild(source);
                        source.removeSpecialParent(target);
                        boolean search = true;
                        int compteur = 0;
                        while (search && compteur < VSpecialLinks.size()) {
                            ltmp = (GraphicLink) VSpecialLinks.elementAt(compteur);
                            search = !ltmp.equals(newlink);
                            if (!search)
                                VSpecialLinks.remove(ltmp);
                            else
                                compteur++;
                        }
                    }
                    source.setParent(target);
                    VLinks.addElement(newlink);
                    if (target.isCollapsed()) {
                        target.Expand();
                        Organise();
                    }
                }
                if (!CurrentFile.endsWith("*")) {
                    CurrentFile = CurrentFile + "*";
                    changeFrameTitle();
                    isModified = true;
                }
            }
        }

        public void addLink(int x, int y, boolean special) {
            if (VSelected.isEmpty())
                return;

            Point position = new Point(x, y);
            GraphicAgent tmp, source, target = new GraphicAgent(cb[8].isSelected());
            boolean found = false;

            source = (GraphicAgent) VSelected.firstElement();
            for (int i = 0; i < VAgents.size(); i++) {
                tmp = (GraphicAgent) VAgents.elementAt(i);
                if (tmp.isSelected(position) > 0 && tmp != source) {
                    found = true;
                    target = tmp;
                }
            }
            if (found)
                addLink(target, source, special);
        }

        public void removeElement(GraphicAgent Parent) {
            Vector VChild = Parent.getChildren();
            for (int i = 0; i < VChild.size(); i++)
                removeElement((GraphicAgent) VChild.elementAt(i));
            VAgents.removeElement(Parent);
        }

        public void delete() {
            Vector tmp = (Vector) VLinks.clone(), tmp2 = (Vector) VSpecialLinks.clone();
            GraphicAgent aselectionne;
            GraphicLink test;

            if (selected == 0)
                return;
            else if (selected == 1) {
                for (int j = 0; j < VSelected.size(); j++) {
                    aselectionne = (GraphicAgent) VSelected.elementAt(j);
                    if (!aselectionne.isCollapsed()) {
                        VAgents.removeElement(aselectionne);
                        aselectionne.delete();
                    } else {
                        removeElement(aselectionne);
                        aselectionne.deleteAll();
                    }
                    for (int i = 0; i < tmp.size(); i++) {
                        test = (GraphicLink) tmp.elementAt(i);
                        if (test.contains(aselectionne))
                            VLinks.removeElement(test);
                    }
                    for (int i = 0; i < tmp2.size(); i++) {
                        test = (GraphicLink) tmp2.elementAt(i);
                        if (test.contains(aselectionne))
                            VSpecialLinks.removeElement(test);
                    }
                }
            } else if (selected == 2) {
                if (VLinks.contains(lselectionne)) {
                    VLinks.removeElement(lselectionne);
                    lselectionne.delete();
                } else {
                    VSpecialLinks.removeElement(lselectionne);
                    lselectionne.delete();
                }
            }
            selected = 0;

            AgentProperties.setName("");
            AgentProperties.setClass("");
            AgentProperties.setComputer("");
            AgentProperties.activateName(false);
            AgentProperties.activateClass(false);
            AgentProperties.activateComputer(false);
            if (!CurrentFile.endsWith("*") && VAgents.size() != 0) {
                CurrentFile = CurrentFile + "*";
                changeFrameTitle();
                isModified = true;
            } else if (CurrentFile.endsWith("*") && VAgents.size() == 0) {
                CurrentFile = CurrentFile.substring(0, CurrentFile.length() - 1);
                changeFrameTitle();
                isModified = false;
            }
        }

        public void Organise() {
            GraphicAgent agent;
            int marge = 250, high = 80, ecartement = 20, hauteur = 0;

            for (int i = 0; i < VAgents.size(); i++) {
                agent = (GraphicAgent) VAgents.elementAt(i);
                Point p = agent.position();
                if (!agent.hasAlreadyParent()) {
                    TreeOrganiser Tree = new TreeOrganiser(agent, marge, high, ecartement);
                    Tree.Organise();
                    agent.setFreeze(true);
                    agent.shiftPosition(p.x - agent.position().x, p.y - agent.position().y);
                    marge = Tree.widthMax() + ecartement;
                    hauteur = Math.max(hauteur, Tree.heightMax());
                }
            }
            TD.setNewSize(new Dimension(marge, hauteur));
        }

        public void Execute() {
            StringWriter swrite = new StringWriter();

            BufferedWriter out = new BufferedWriter(swrite);
            try {

                this.save(out);
            } catch (Throwable t) {
                System.out.println("Erreur de transfert");
            }
            String transfer = swrite.toString();
            AgentsExecutor.newConfig(transfer);
            AgentsExecutor.setVisible(true);
        }
    }

    // public void setVisible(boolean b) {
    // super.setVisible(b);
    // }

}
