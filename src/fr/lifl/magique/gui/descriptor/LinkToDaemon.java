package fr.lifl.magique.gui.descriptor;

/**
 * A class that represents a link to a platform
 *
 * @version 1.0 04/05/99
 * @author Nadir Doghmane
 * @author Fabien Niquet
 */
public class LinkToDaemon {
     /** 
   * the computer name
   */
  String name;
    /**
   * the computer port
   */
  int port;
 /**
      * Constructs a new Link
      * @param name the name of computer 
     * @param port  the port of the computer 
      */

  public LinkToDaemon (String name,int port) {
        this.name=name;
        this.port=port;
  }

 /**
     * Returns the name of this computer
     */
  public String getName () {
    return name;
  }

/**
     * Returns the port of this computer
     */
  public int getPort () {
    return port;
  }
/**
     * Returns the name of the computer as computername:port
     */
    public String getComputer () {
	return new String(name+":"+port);
    }
}
