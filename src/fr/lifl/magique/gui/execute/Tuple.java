package fr.lifl.magique.gui.execute; 
public class Tuple {
  private String name;
  private boolean active;
  public Tuple (String name,boolean active) {
    this.name=name;
    this.active=active;
  }
  public String getName () {
    return name;
  }
  public boolean isActive () {
    return active;
  }
}
