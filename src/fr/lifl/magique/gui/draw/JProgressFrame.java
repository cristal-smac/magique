package fr.lifl.magique.gui.draw;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class JProgressFrame extends JFrame{
   Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    ImagePainter logo=new ImagePainter();
    JProgressBar bar;
    JLabel label=new JLabel("");
    JPanel panel_chargement=new JPanel();
    private int x=0;
    public JProgressFrame() {
   
        super("Chargement...");
	
	//Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
       int Jheight=logo.getHeight()+100,Jwidth=logo.getWidth();
	bar=new JProgressBar();        
	setSize(Jwidth,Jheight);
	setLocation(screen.width/2-Jwidth/2,screen.height/2-Jheight/2);
	bar.setVisible(true);
      getContentPane().setLayout(new BorderLayout());
	panel_chargement.setLayout(new BorderLayout());
	panel_chargement.add(label,BorderLayout.NORTH);
	panel_chargement.add(bar,BorderLayout.CENTER);
getContentPane().add(logo,BorderLayout.CENTER);      
  getContentPane().add(panel_chargement,BorderLayout.SOUTH);
	bar.setVisible(true);
	validate();
      }
      public void setValue (int x) {
        this.x=x;
        bar.setValue(x);
      }
      public void addValue (int y) {
        x=x+y;
        if (x<101)
        bar.setValue(x);

      }
      public int getValue () {
        return x;
      }
      public void setText(String text) {
        label.setText(text);
      }
 
}
class ImagePainter extends JComponent {
  Image dessin;
  public ImagePainter () {  
      dessin=getToolkit().getImage(this.getClass().getResource("MAGIQUE.gif"));
      MediaTracker tracker = new MediaTracker(this); 
      tracker.addImage(dessin,0);
      try {tracker.waitForID(0);} catch(InterruptedException e) {}
      setSize(dessin.getWidth(this),dessin.getHeight(this)-65);
     
  }

    public void update(Graphics g) {
	g.drawImage(dessin,0,0,null);
    }
    public void paint(Graphics g) {
	update (g);
    }
}
