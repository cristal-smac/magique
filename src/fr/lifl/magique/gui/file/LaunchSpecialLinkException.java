
package fr.lifl.magique.gui.file;
import fr.lifl.magique.gui.draw.GraphicLink;
import java.util.*;
import java.io.*;
public class LaunchSpecialLinkException extends IOException {
	private GraphicLink link;
	public LaunchSpecialLinkException (GraphicLink link,String message) {
        super(message);
	this.link=link;
      }
	public GraphicLink getSpecialLink () {
	  return link;
      }
}
