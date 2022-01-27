package fr.lifl.magique.gui.file;
import fr.lifl.magique.gui.draw.GraphicAgent;
import java.util.*;
import java.io.*;
public class LaunchAgentException extends IOException {
	private GraphicAgent agent;
	public LaunchAgentException (GraphicAgent agent,String message) {
        super(message);
	this.agent=agent;
      }
	public GraphicAgent getAgent () {
	  return agent;
      }
}
