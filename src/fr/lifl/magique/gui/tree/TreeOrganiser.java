package fr.lifl.magique.gui.tree;

import fr.lifl.magique.gui.draw.GraphicNode;

import java.awt.*;
import java.util.Vector;

/**
 * A class that organises a tree
 *
 * @author Nadir Doghmane
 * @author Fabien Niquet
 * @version 1.0 04/05/99
 */
public class TreeOrganiser {

    /**
     * The vector representing the tree to organise
     */
    private final Vector Tree;

    /**
     * The GraphicNode representing the root of this tree
     */
    private final GraphicNode Root;

    /**
     * The marge to use on the left side
     */
    private final int marge;

    /**
     * The horizontal shift between two nodes
     */
    private final int ecartementX;

    /**
     * The vertical shift between two nodes
     */
    private final int ecartementY;

    private int compteur;

    private int decalage;

    /**
     * Constructs a new TreeOrganiser with the specified root , marge , vertical and horizontal shifts
     *
     * @param root   the root of the tree
     * @param marge  the left side marge
     * @param ecartY the vertical shift
     * @param ecartX the horizontal shift
     */
    public TreeOrganiser(GraphicNode root, int Marge, int ecartY, int ecartX) {
        Tree = new Vector();
        Root = root;
        marge = Marge;
        decalage = marge;
        ecartementX = ecartX;
        ecartementY = ecartY;
    }

    /**
     * Returns the max width of this tree
     */
    public int widthMax() {
        return decalage;
    }

    /**
     * Returns the max height of this tree
     */
    public int heightMax() {
        return (Tree.size() + 2) * ecartementY;
    }

    /**
     * Adds a node to this tree
     */
    private void add(GraphicNode Node, int high) {
        Vector stage = new Vector();
        int positionX, positionY = 0;

        if (high == 0 || high >= Tree.size()) {
            Tree.addElement(stage);
            positionX = marge;
        } else {
            GraphicNode dernier;
            stage = (Vector) Tree.elementAt(high);
            dernier = (GraphicNode) stage.lastElement();
            positionX = (dernier.position()).x + dernier.width() / 2 + ecartementX;
        }
        positionY = 30 + high * ecartementY;
        Node.changePositionDeb(new Point(positionX, positionY));
        decalage = Math.max(decalage, Node.position().x + Node.width() / 2);
        stage.addElement(Node);
    }

    /**
     * Moves a stage of this tree
     */
    private void moveStage(GraphicNode Parent, int h, int offset) {
        if (Parent.isVisible()) {
            GraphicNode brother = Parent;
            Vector stage = (Vector) Tree.elementAt(h);

            for (int i = stage.indexOf(Parent); i < stage.size(); i++) {
                brother = (GraphicNode) stage.elementAt(i);
                if (brother.isVisible()) {
                    brother.changePosition(new Point(brother.position().x + offset, brother.position().y));
                    decalage = Math.max(decalage, brother.position().x + brother.width() / 2);
                }
            }
        }
    }

    /**
     * Moves a subtree of this tree of the specified offset
     */
    private void moveSubTree(GraphicNode Parent, int h, int offset) {
        if (Parent.isVisible()) {
            Vector VChild = Parent.getChildren();
            GraphicNode Child = Parent;

            if (h > compteur) {
                moveStage(Parent, h, offset);
                compteur = h;
            }
            if (!VChild.isEmpty()) {
                for (int i = 0; i < VChild.size(); i++) {
                    Child = (GraphicNode) VChild.elementAt(i);
                    if (Child.isVisible()) moveSubTree(Child, h + 1, offset);
                }
            }
        }
    }

    /**
     * Moves this tree of the specified offset
     */
    private void moveTree(GraphicNode Parent, int h, int offset) {
        moveSubTree(Parent, h, offset);
        compteur = h;
    }

    private void Organise1(GraphicNode Parent, int h) {
        if (Parent.isVisible()) {
            Vector VChild = Parent.getChildren();
            add(Parent, h);
            for (int i = 0; i < VChild.size(); i++)
                Organise1((GraphicNode) VChild.elementAt(i), h + 1);
        }
    }

    private void Organise2(GraphicNode Parent, int h) {
        if (Parent.isVisible()) {
            Vector VChild = Parent.getChildren();
            GraphicNode Child;
            int CenterChild = 0, debut = 0, fin = 0;
            int positionX = (Parent.position()).x;

            if (!VChild.isEmpty()) {
                for (int i = 0; i < VChild.size(); i++) {
                    Child = (GraphicNode) VChild.elementAt(i);
                    if (Child.isVisible()) {
                        Organise2(Child, h + 1);
                        if (i == 0)
                            debut = Child.position().x;
                        if (i == VChild.size() - 1)
                            fin = Child.position().x;
                    }
                }
                compteur = h;
                CenterChild = (debut + fin) / 2;
                if (positionX < CenterChild)
                    moveStage(Parent, h, CenterChild - positionX);
                else
                    moveTree(Parent, h, positionX - CenterChild);
            }
        }
    }

    /**
     * Organises this tree
     */
    public void Organise() {
        Organise1(Root, 0);
        Organise2(Root, 0);
    }

}
