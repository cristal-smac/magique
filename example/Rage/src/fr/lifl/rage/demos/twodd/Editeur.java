package fr.lifl.rage.demos.twodd;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.Point;
import java.awt.event.*;
import java.awt.image.*;

public class Editeur extends Frame 
    implements MouseListener, MouseMotionListener, ActionListener {

    // Interface
    protected int X0, Y0;
    protected Image image;
    protected Graphics big;
    protected EditeurCanvas canvas;
    protected Label mouseInfo;
    protected int width = 640, height = 480;
    protected int mouseX, mouseY;
    protected int realX, realY;
    protected Button clear;
    protected Button save;
    protected Button quit;
    protected Checkbox mLine;
    protected Checkbox mForce;
    protected int lastForceX = -1, lastForceY = -1;
    protected int currentLine = -1;

    // Points
    protected ArrayList vertices;    
    protected HashMap forces;
    protected HashMap segments;

    // Boite de dialogue des forces
    protected Dialog dialog;
    protected TextField dtangente;
    protected TextField dnormale;
    protected TextField dsegments;
    protected Button dok, dcancel;
    protected boolean forceok = false;
    protected int forceT, forceN, forceS;

    protected class EditeurCanvas extends Canvas {
	
	protected Image image;

	public EditeurCanvas(Image image) {
	    super();
	    this.image = image;
	    setSize(new Dimension(image.getWidth(this), 
				  image.getHeight(this)));	    
	}

	public void paint(Graphics g) {
	    update(g);
	}

	public void update(Graphics g) {	   
	    refresh();
	    g.drawImage(image, 0, 0, this);	    
	}
	
	public void refresh() {	    
	    // Effacer
	    big.setColor(Color.black);
	    big.fillRect(0, 0, width, height);

	    // Axes
	    big.setColor(Color.yellow);
	    big.drawLine(X0, 0, X0, height);
	    big.drawLine(0, Y0, width, Y0);

	    // Polygone
	    big.setColor(Color.green);
	    
	    for(int i = 0; i < vertices.size(); i ++) {
		Point p1 = (Point) vertices.get(i);
		Point p2 = (Point) vertices.get((i+1) % vertices.size());
		
		double x1 = p1.getX(), y1 = p1.getY();
		double x2 = p2.getX(), y2 = p2.getY();

		if (i == currentLine) 
		    big.setColor(Color.red);
		big.drawLine(X0+(int)x1, Y0-(int)y1, X0+(int)x2, Y0-(int)y2);
		if (i == currentLine) 
		    big.setColor(Color.green);
		
		big.drawOval((X0+(int)x1)-4, (Y0-(int)y1)-4, 8, 8);

		// Force ?
		if (i == currentLine && forces.containsKey(currentLine+"")) {
		    Force f = (Force) forces.get(""+currentLine);

		    int xo = X0 + ((int)x1+(int)x2)/2, 
			yo = Y0 - ((int)y1+(int)y2)/2;

		    big.setColor(Color.blue);
		    big.drawOval(xo-3, yo-3, 6, 6);
		    
		    big.drawLine(xo, yo, 
				 (int)(xo+f.tangentielle),
				 (int)(yo-f.normale));

		    big.setColor(Color.green);		    
		}
	    }	    

	    big.setColor(Color.white);		    
	    big.drawLine(realX - 5, realY, realX + 5, realY);
	    big.drawLine(realX, realY - 5, realX, realY + 5);

	    this.repaint();
	}
    }

    public Editeur(int width, int height) {
	super("Editeur");
	
	this.width = width;
	this.height = height;

	this.vertices = new ArrayList();
	this.forces = new HashMap();
	this.segments = new HashMap();

	image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	X0 = width / 2; 
	Y0 = height / 2;

	big = image.getGraphics();

	canvas = new EditeurCanvas(image);

	canvas.addMouseListener(this);
	canvas.addMouseMotionListener(this);

	mouseInfo = new Label("Tout va bien");
	mouseInfo.setAlignment(Label.CENTER);

	clear = new Button("Effacer");
	clear.addActionListener(this);

	save = new Button("Sauver");
	save.addActionListener(this);

	quit = new Button("Quitter");
	quit.addActionListener(this);
	
	CheckboxGroup cbg = new CheckboxGroup();
	mLine = new Checkbox("Ligne", cbg, true);
	mForce = new Checkbox("Force", cbg, false);

	Panel q = new Panel();
	q.add(mLine);
	q.add(mForce);
	q.add(clear);
	q.add(save);
	q.add(quit);

	Panel p = new Panel(new BorderLayout());
	
	p.add(mouseInfo, BorderLayout.NORTH);
	p.add(canvas, BorderLayout.CENTER);
	p.add(q, BorderLayout.SOUTH);

	// Dialogue
	dialog = new Dialog(this, "Force", true);	    
	Panel dp = new Panel(new BorderLayout());

	Label tangente = new Label("Tangente : ");
	Label normale = new Label("Normale : ");
	Label segments = new Label("Segments : ");

	dtangente = new TextField(4);
	dnormale = new TextField(4);
	dsegments = new TextField(3);

	dok = new Button("OK");
	dcancel = new Button("Cancel");

	Panel t = new Panel(new FlowLayout());
	t.add(tangente);
	t.add(dtangente);
	t.add(normale);
	t.add(dnormale);

	dp.add(t, BorderLayout.NORTH);
	
	t.add(segments);
	t.add(dsegments);
	dp.add(t, BorderLayout.CENTER);
	
	t.add(dok);
	t.add(dcancel);
	
	dp.add(t, BorderLayout.SOUTH);

	dialog.add(dp);
	dok.addActionListener(this);
	dcancel.addActionListener(this);
	dialog.pack();

	add(p);
	pack();
	show();
    }

    public void setMousePosition(double x, double y) {
	mouseInfo.setText("x = " + x + "; y = " + y);
    }

    public int getPointedLine(int x, int y) {
	Point p1, p2;
	
	if (vertices.size() < 2)
	    return -1;

	int i = 1;
	p1 = (Point) vertices.get(0);

	while(i <= vertices.size()) {
	    if (i == vertices.size()) 
		p2 = (Point) vertices.get(0);
	    else
		p2 = (Point) vertices.get(i);
	    
	    double X1 = p2.getX() - p1.getX();
	    double Y1 = p2.getY() - p1.getY();
	    double X2 = p2.getX() - x;
	    double Y2 = p2.getY() - y;
	    double n1 = Math.sqrt(X1*X1 + Y1*Y1);
	    double n2 = Math.sqrt(X2*X2 + Y2*Y2);

	    double cosalpha = (X1*X2 + Y1*Y2) / (n1*n2);
	    if (cosalpha >= 0.99)
		return i-1;

	    p1 = p2;
	    i ++;
	}

	return -1;
    }

    // Gestion souris    
    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
	if (mLine.getState())
	    vertices.add(new Point(mouseX, mouseY));		    
	
	if (mForce.getState()) {
	    if (currentLine == -1) 
		return;

	    dialog.show();
	    if (forceok) {
		if (forces.containsKey(currentLine+"")) 
		    forces.remove(""+currentLine);
		Force f = new Force(forceT, forceN);
		forces.put(currentLine+"", f);
		segments.put(currentLine+"", new Integer(forceS));
	    }
	}
    }

    public void mouseReleased(MouseEvent e) {
    }
    
    public void mouseMoved(MouseEvent e) {
	realX = e.getX();
	realY = e.getY();
	mouseX = e.getX() - X0;
	mouseY = Y0 - e.getY();
	
	currentLine = getPointedLine(mouseX, mouseY);
	setMousePosition(mouseX, mouseY);
    }
    
    public void mouseDragged(MouseEvent e) {
    }

    // Gestion actions
    public void actionPerformed(ActionEvent e) {
	Object source = e.getSource();

	if (source == clear) {
	    vertices = new ArrayList();
	    forces = new HashMap();
	    canvas.refresh();
	}

	if (source == save) {
	    this.dump();
	}
	
	if (source == quit) {
	    new Test(getPolygone(), getForces(), getSegments());
	    System.exit(0);
	}

	if (source == dok) {
	    forceT = Integer.parseInt(dtangente.getText());
	    forceN = Integer.parseInt(dnormale.getText());
	    forceS = Integer.parseInt(dsegments.getText());
	    forceok = true;
	    dialog.hide();
	}

	if (source == dcancel) {
	    forceok = false;
	    dialog.hide();
	}
	    
    }

    public Polygone getPolygone() {
	java.awt.geom.Point2D.Double[] points = new java.awt.geom.Point2D.Double[vertices.size()];
	
	for(int i = 0; i < vertices.size(); i ++) {
	    Point p = (Point) vertices.get(i);
	    points[i] = new java.awt.geom.Point2D.Double(p.getX(), p.getY());
	}
	
	return new Polygone(points);
    } 

    public Force[] getForces() {
	Force[] result = new Force[vertices.size()];

	for(int i = 0; i < vertices.size(); i ++) {
	    Force f = (Force) forces.get(i+"");
	    if (f == null) 
		f = new Force(0.0, 0.0);

	    result[i] = f;
	}
	
	return result;
    }
    
    public int[] getSegments() {
	int[] result = new int[vertices.size()];

	for(int i = 0; i < vertices.size(); i ++) {
	    Integer s = (Integer) segments.get(i+"");
	    if (s == null) 
		s = new Integer(1);

	    result[i] = s.intValue();
	}
	
	return result;
    }
    
    public void dump() {
	try {
	    FileOutputStream fos = new FileOutputStream("result.dat");
	    ObjectOutputStream oos = new ObjectOutputStream(fos);
	    	 
	    oos.writeObject(getPolygone());
	    oos.writeObject(getForces());
	    oos.writeObject(getSegments());

	    oos.flush();
	    fos.close();	    
	} catch(IOException err) {
	    err.printStackTrace();
	}
    }

    public static void main(String[] args) {
	int w = 640, h = 480;

	if (args.length == 2) {
	    w = Integer.parseInt(args[0]);
	    h = Integer.parseInt(args[1]);
	}

	new Editeur(w ,h);
    }
}
