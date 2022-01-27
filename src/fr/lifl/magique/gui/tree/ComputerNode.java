package fr.lifl.magique.gui.tree;
import javax.swing.tree.*;
import java.io.*;

public class ComputerNode extends DefaultMutableTreeNode {
     private int port=4444;
     public ComputerNode (Object value) {
        super(value,false);
     }
     public ComputerNode (Object value,int port) {
        super(value,false);
        this.port=port;
     }
     public int getPort() {
        return port;
     }
     public void setPort(int port) {
        this.port=port;
     }
}
