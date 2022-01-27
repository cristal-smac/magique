package fr.lifl.magique.gui.file;
import fr.lifl.magique.gui.draw.GraphicLink;
import java.util.*;
import java.io.*;
public class LaunchLinkException extends IOException {
	private GraphicLink link;
	public LaunchLinkException (GraphicLink link,String message) {
        super(message);
	this.link=link;
      }
	public GraphicLink getLink () {
	  return link;
      }
}
