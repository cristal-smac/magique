package fr.lifl.magique.gui.util;
import java.io.* ;

public class NullStream extends OutputStream {
  public NullStream () {}
  public void close () {}
  public void flush(){}
  public void write(byte[] b) {}
  public void write(byte[] b,int offset) {}
  public void write(int b) {}
}
