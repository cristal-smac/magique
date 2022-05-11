package fr.lifl.magique.gui.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.BufferedWriter;
import java.util.Enumeration;

public class TreeSaver {
    private void saveTree(DefaultMutableTreeNode root, BufferedWriter out, boolean noROOT) throws Throwable {
        if (root.getAllowsChildren()) {
            if (noROOT) {
                out.write("DEBDOMAIN" + "\n");
                out.write("\"" + root + "\"" + "\n");
            }
            for (Enumeration e = root.children(); e.hasMoreElements(); )
                saveTree((DefaultMutableTreeNode) e.nextElement(), out, true);
            if (noROOT)
                out.write("\n" + "FINDOMAIN" + "\n");
        } else if (noROOT) {
            out.write("\"" + root + "\" ");
            out.write(Integer.valueOf(((ComputerNode) root).getPort()) + " ");
        }
    }

    public void save(DefaultMutableTreeNode root, BufferedWriter out) throws Throwable {
        //    out.write(root.toString()+"\n");
        saveTree(root, out, false);
        out.close();
    }
}

