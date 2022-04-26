package fr.lifl.magique.gui.execute;

import fr.lifl.magique.gui.draw.GraphicAgent;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;


public class Configuration {
    private final String Name;
    private final Hashtable HWaitForAgents = new Hashtable();
    private final Vector VRoot = new Vector();
    private final Hashtable HexecFrame = new Hashtable();
    private final JMenu bouton;
    private boolean isdeployed = false;

    public Configuration(String Name, Vector VAgents, JMenu bouton, JComponent desktop) {
        this.Name = Name;
        this.bouton = bouton;
        int margeMax = desktop.getWidth() - 70;
        int hauteurMax = desktop.getHeight() - 70;
        Random generator = new Random();
        int x = 20;
        int y = 15;
        try {
            x = generator.nextInt(margeMax / 3);
            y = generator.nextInt(hauteurMax / 3);
        } catch (Throwable t) {
            x = 20;
            y = 20;
        }

        for (int i = 0; i < VAgents.size(); i++) {
            GraphicAgent agent = (GraphicAgent) VAgents.elementAt(i);
            agent.setName(Name + "-" + agent.getName());
            while (HexecFrame.containsKey(agent.getName()))
                agent.setName(agent.getName() + "~");
            HWaitForAgents.put(agent.getName(), agent.getName());

            //System.out.println(agent.getName()+" "+agent.GetParent());

            if (agent.GetParent() == null && agent.getSpecialParent().size() == 0)
                VRoot.add(agent);

            ExecutionFrame frame = new ExecutionFrame(agent, desktop);
            HexecFrame.put(agent.getName(), frame);
            frame.setLocation(x, y);
            x = x + 30;
            y = y + 30;
            if (x > margeMax || y > hauteurMax) {
                try {
                    x = generator.nextInt(margeMax);
                    y = generator.nextInt(hauteurMax);
                } catch (Throwable t) {
                    x = 20;
                    y = 20;
                }
            }
            frame.toFront();
        }
    }

    public String getName() {
        return Name;
    }

    public void traiteMessage(String name, String text) {
        ExecutionFrame frame = (ExecutionFrame) HexecFrame.get(name);
        if (frame != null)
            frame.addText(text);
    }

    public void traiteMessage(String name, Image dessin) {
        ExecutionFrame frame = (ExecutionFrame) HexecFrame.get(name);
        if (frame != null)
            frame.addImage(dessin);
    }

    public Enumeration getRoots() {
        return VRoot.elements();
    }

    public void setDeployed() {
        for (Enumeration e = HexecFrame.elements(); e.hasMoreElements(); ) {
            ExecutionFrame frame = (ExecutionFrame) e.nextElement();
            frame.setDeployed();
        }
        isdeployed = true;
    }

    public boolean isDeployed() {
        return isdeployed;
    }

    public void launch() {
        for (Enumeration e = HexecFrame.elements(); e.hasMoreElements(); ) {
            ExecutionFrame frame = (ExecutionFrame) e.nextElement();
            frame.setLaunch();
        }
    }

    public void kill() {
        synchronized (HexecFrame) {
            for (Enumeration e = HexecFrame.elements(); e.hasMoreElements(); )
                ((ExecutionFrame) e.nextElement()).kill();
            HexecFrame.clear();
        }
    }

    public Vector getStatusFrames() {
        Vector res = new Vector();
        synchronized (HexecFrame) {
            for (Enumeration e = HexecFrame.elements(); e.hasMoreElements(); ) {
                ExecutionFrame frame = (ExecutionFrame) e.nextElement();
                res.add(new Tuple(frame.getTitle(), frame.isVisible()));
            }
            return res;
        }
    }

    public void reverseAgentVisible(String name) {
        System.out.println("one est dans reverse");
        synchronized (HexecFrame) {
            ExecutionFrame frame = (ExecutionFrame) HexecFrame.get(name);
            if (frame != null) {
                System.out.println("on a trouve la fenetre!!!!");
                frame.setHidden(frame.isVisible());
            }
        }
    }

    public void setVisible(boolean show) {
        synchronized (HexecFrame) {
            for (Enumeration e = HexecFrame.elements(); e.hasMoreElements(); ) {
                ExecutionFrame frame = (ExecutionFrame) e.nextElement();
                frame.setVisible(show);
            }
        }
    }

    public void setHidden(boolean show) {
        synchronized (HexecFrame) {
            for (Enumeration e = HexecFrame.elements(); e.hasMoreElements(); ) {
                ExecutionFrame frame = (ExecutionFrame) e.nextElement();
                frame.setHidden(show);
            }
        }
    }

}


