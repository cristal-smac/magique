
package fr.lifl.rage.demos.twodd;


public class Twodd
{
    
    protected static final double PI   = Math.PI;
    // Module d'élasticité (varie entre 20000 et 40000)
    protected static final double E    = 20000;
    // Coefficient de Poisson (varie entre 2.0 et 2.5)
    protected static final double PR   = 2.0;
    // Gestion des symétrie axiales.
    protected static final double XSYM = 1.0;
    protected static final double YSYM = 1.0;
    // Nombre de segments (i.e. de points valués)
    protected int NUMBS;
    // Type de symétrie 1 : aucune, 2 : x=0, 3 : y=0, 4 : les deux
    protected int KSYM = 1;
    // ???? Post-traitements
    protected double NUMOS;
    protected double PXX;
    protected double PYY;
    protected double PXY;
	
    protected static final double PR1  = 1.0 - 2.0 * PR;
    protected static final double PR2  = 2.0 * ( 1.0 - PR);
    protected static final double CON  = 1.0 / (4.0 * PI * (1.0 - PR));
    protected static final double CONS = E / (1.0+ PR);
	
    protected double SXXS =0, SXXN =0, SYYS =0, SYYN =0, SXYS =0, SXYN =0;
    protected double SXXDS=0, SXXDN=0, SYYDS=0, SYYDN=0, SXYDS=0, SXYDN=0;
    protected double UXS  =0, UXN  =0, UYS  =0, UYN  =0;
    protected double UXDS =0, UXDN =0, UYDS =0, UYDN =0;
	
    // La matrice contenant les influences
    public double[][]  C;
    // Vecteur contenant les forces imposées
    public double[]    B;
    // Vecteur de déplacement (inconnue)
    public double[]    D;
	
    // Informations sur les points représentant le milieu
    protected double[]     XM;
    protected double[]     YM;
    protected double[]      A;
    protected double[] COSBET;
    protected double[] SINBET;
    protected int[]       KOD;
	
    public Twodd(PointValue[] points)
    {
	PointValue current;
	int numberOfPoints = points.length;
	XM = new double[numberOfPoints];
	YM = new double[numberOfPoints];
	A = new double[numberOfPoints];
	COSBET = new double[numberOfPoints];
	SINBET = new double[numberOfPoints];
	KOD = new int[numberOfPoints];
	B = new double[2*numberOfPoints];
	D = new double[2*numberOfPoints];
	C = new double[2*numberOfPoints][2*numberOfPoints];
	
	for (int i=0; i<points.length; i++)
	    {
		// Informations géométriques
		current = points[i];
		XM[i] = current.point.x;
		YM[i] = current.point.y;
		A[i] = current.point.length / 2.0;
		COSBET[i] = current.point.nx;
		SINBET[i] = current.point.ny;
		KOD[i] = 1;
		
		// Informations sur les forces imposées
		B[2*i] = current.force.tangentielle;
		B[2*i+1] = current.force.normale;
		
	    }
    }
    
    protected void initl()
    {
	SXXS = 0; SXXN = 0; SYYS = 0; SYYN = 0; SXYS = 0; SXYN = 0;
	UXS  = 0; UXN  = 0; UYS  = 0; UYN  = 0;
    }

    protected void coeff(double X, double Y, double CX, double CY, 
			 double A, double COSB, double SINB, int MSYM)
    {
	double COSB2 = COSB * COSB;
	double SINB2 = SINB * SINB;
	double COS2B = COSB2 - SINB2;
	double SIN2B = 2.0 * SINB * COSB;
	
	double XB =  (X - CX) * COSB + (Y - CY) * SINB;
	double YB = -(X - CX) * SINB + (Y - CY) * COSB;
	
	double R1S = (XB - A) * (XB - A) + YB * YB;
	double R2S = (XB + A) * (XB + A) + YB * YB;
		
       	double FL1 = 0.5 * Math.log(R1S);
	double FL2 = 0.5 * Math.log(R2S);
		
	double FB2 = CON * (FL1 -FL2);
	double FB3;

	if ( YB != 0.0){
	    FB3 = -CON * (Math.atan((XB + A) / YB) - Math.atan((XB - A) / YB));
	} else {
	    FB3 = 0.0;
	    if (Math.abs(XB) < A) {
		FB3 = CON * PI;
	    }
	}
	
	double FB4 = CON * (YB / R1S - YB / R2S);
	double FB5 = CON * ((XB - A) / R1S - (XB + A) / R2S);
	double FB6 = CON * ( ((XB-A)*(XB-A)-YB*YB)/(R1S*R1S) - 
			     ((XB+A)*(XB+A)-YB*YB)/(R2S*R2S) );
	double FB7 = 2.0 * CON * YB * ((XB-A)/(R1S*R1S)-(XB+A)/(R2S*R2S));
		
	UXDS = -PR1 * SINB * FB2 + PR2 * COSB * FB3 + YB * (SINB * FB4 - COSB * FB5);
	UXDN = -PR1 * COSB * FB2 - PR2 * SINB * FB3 - YB * (COSB * FB4 + SINB * FB5);
	UYDS =  PR1 * COSB * FB2 + PR2 * SINB * FB3 - YB * (COSB * FB4 + SINB * FB5);
	UYDS = -PR1 * SINB * FB2 + PR2 * COSB * FB3 - YB * (SINB * FB4 - COSB * FB5);
	
	SXXDS = CONS * (2.0 * COSB2 * FB4 + SIN2B * FB5 + YB * (COS2B * FB6 - SIN2B * FB7));
	SXXDN = CONS * (-FB5 + YB * (SIN2B * FB6 + COS2B * FB7));
	SYYDS = CONS * (2.0 * SINB2 * FB4 - SIN2B * FB5 - YB * (COS2B * FB6 - SIN2B * FB7));
	SYYDN = CONS * (-FB5 - YB * (SIN2B * FB6 + COS2B * FB7));
	SXYDS = CONS * (SIN2B * FB4 - COS2B * FB5 + YB * (SIN2B * FB6 + COS2B * FB7));
	SXYDN = CONS * (-YB * (COS2B * FB6 - SIN2B * FB7));
	
	UXS += MSYM*UXDS;
	UXN += UXDN;
	UYS += MSYM*UYDS;
	UYN += UYDN;
		
	SXXS += MSYM * SXXDS;
	SXXN += SXXDN;
	SYYS += MSYM * SYYDS;
	SYYN += SYYDN;
	SXYS += MSYM * SXYDS;
	SXYN += SXYDN;
	
    }

    public void computeInfluenceCoefficients()
    {
	int IN, IS;
	int JN, JS; 
	double XI, XJ, COSBI, SINBI;
	int KODE;
	double YI, YJ, COSBJ, SINBJ, AJ;
	// La matrice des coefficients d'influence est carrée
	int I, J;
	for (int i=0; i < C.length / 2; i++)
	    {
		I = i+1;
		IN = 2 * I;
		IS = IN - 1;
		XI = XM[i];
		YI = YM[i];
		COSBI = COSBET[i];
		SINBI = SINBET[i];
		KODE = KOD[i];
		for (int j=0; j < C.length / 2; j++)
		    {
			J = j+1;
			JN = 2 * J;
			JS = JN - 1;
			initl();
			XJ = XM[j];
			YJ = YM[j];
			COSBJ = COSBET[j];
			SINBJ = SINBET[j];
			AJ = A[j];
			coeff(XI, YI, XJ, YJ, AJ, COSBJ, SINBJ, +1);
			
			switch (KSYM) {
			case 1 :			   
			    break;
			case 2:
			    YJ = 2.0 * YSYM - YM[j];
			    coeff(XI, YI, XJ, YJ, AJ, COSBJ, -SINBJ, -1);
			    break;
			case 3:
			    XJ = 2.0 * XSYM - XM[j];
			    coeff(XI, YI, XJ, YJ, AJ, COSBJ, SINBJ, -1);
			    break;
			default:
			    XJ = XM[j];
			    YJ = 2.0 * YSYM - YM[j];
			    coeff(XI, YI, XJ, YJ, AJ, -COSBJ, SINBJ, -1);
			    XJ = 2.0 * XSYM - XM[j];
			    coeff(XI, YI, XJ, YJ, AJ, -COSBJ, -SINBJ, +1);
			}
			
			switch (KODE) {
			case 1:
			    C[IS-1][JS-1] = (SYYS-SXXS)*SINBI*COSBI+SXYS*(COSBI*COSBI-SINBI*SINBI);
			    //System.out.print("("+(IS-1)+","+(JS-1)+") = "+C[IS-1][JS-1]+" | ");
			    C[IS-1][JN-1] = (SYYN-SXXN)*SINBI*COSBI+SXYN*(COSBI*COSBI-SINBI*SINBI);
			    //System.out.print("("+(IS-1)+","+(JN-1)+") = "+C[IS-1][JN-1]+" | ");
			    C[IN-1][JS-1] = SXXS*SINBI*SINBI-2.0*SXYS*SINBI*COSBI+SYYS*COSBI*COSBI;
			    //System.out.print("("+(IN-1)+","+(JS-1)+") = "+C[IN-1][JS-1]+" | ");
			    C[IN-1][JN-1] = SXXN*SINBI*SINBI-2.0*SXYN*SINBI*COSBI+SYYN*COSBI*COSBI;
			    //System.out.println("("+(IN-1)+","+(JN-1)+") = "+C[IN-1][JN-1]);
			    break;
			case 2:
			    C[IS-1][JS-1] =  UXS * COSBI + UYS * SINBI;
			    C[IS-1][JN-1] =  UXN * COSBI + UYN * SINBI;
			    C[IN-1][JS-1] = -UXS * SINBI + UYS * COSBI;
			    C[IN-1][JN-1] = -UXN * SINBI + UYN * COSBI;
			    break;
			case 3:
			    C[IS-1][JS-1] =  UXS * COSBI + UYS * SINBI;
			    C[IS-1][JN-1] =  UXN * COSBI + UYN * SINBI;
			    C[IN-1][JS-1] = SXXS*SINBI*SINBI-2.0*SXYS*SINBI*COSBI+SYYS*COSBI*COSBI;
			    C[IN-1][JN-1] = SXXN*SINBI*SINBI-2.0*SXYN*SINBI*COSBI+SYYN*COSBI*COSBI;
			    break;
			default :
			    C[IS-1][JS-1] = (SYYS-SXXS)*SINBI*COSBI+SXYS*(COSBI*COSBI-SINBI*SINBI);
			    C[IS-1][JN-1] = (SYYN-SXXN)*SINBI*COSBI+SXYN*(COSBI*COSBI-SINBI*SINBI);
			    C[IN-1][JS-1] = -UXS * SINBI + UYS * COSBI;
			    C[IN-1][JN-1] = -UXN * SINBI + UYN * COSBI;
			}
			
		    }
	    }
	
    }

    public void dump()
    {
	for (int i=0; i<C.length; i++) {
	    System.out.print("| ");
	    for (int j=0; j<C.length; j++) {
		System.out.print(((int)C[i][j])+"\t");
	    }
	    System.out.print("| | ");
	    System.out.println(((int) D[i])+"\t| = |\t"+((int)B[i])+"\t|");
	}
    }

    protected void computeBoundaryDisplacementsAndStresses() {
	int IN, IS;
	int JN, JS; 
	double XI, XJ, COSBI, SINBI;
	double YI, YJ, COSBJ, SINBJ, AJ;
	double UXNEG, UYNEG, SIGXX, SIGYY, SIGXY, SIGS, SIGN;
	double USPOS, UNPOS, USNEG, UNNEG, UXPOS, UYPOS;
	
	int I, J;
	for (int i=0; i < C.length; i++) {

	    IN = 2 * i;
	    IS = IN - 1;
	    XI = XM[i];
	    YI = YM[i];
	    COSBI = COSBET[i];
	    SINBI = SINBET[i];
	    
	    UXNEG = 0;
	    UYNEG = 0;
	    SIGXX = PXX;
	    SIGYY = PYY;
	    SIGXY = PXY;
	    
	    for (int j=0; j < C.length; j++) {
		JN = 2 * j;
		JS = JN - 1;
		initl();
		XJ = XM[j];
		YJ = YM[j];
		AJ = A[j];
		COSBJ = COSBET[j];
		SINBJ = SINBET[j];
		coeff(XI, YI, XJ, YJ, AJ, COSBJ, SINBJ, 1);
		
		XJ = 2.0 * XSYM - XM[j];
		coeff(XI, YI, XJ, YJ, AJ, COSBJ, -SINBJ, -1);
		
		YJ = 2.0 * YSYM - YM[j];
		coeff(XI, YI, XJ, YJ, AJ, -COSBJ, SINBJ, -1);
		
		XJ = 2.0 * XSYM - XM[j];
		coeff(XI, YI, XJ, YJ, AJ, COSBJ, -SINBJ, -1);
		XJ = XM[j];
		YJ = 2.0 * YSYM - YM[j];
		coeff(XI, YI, XJ, YJ, AJ, -COSBJ, SINBJ, -1);
		XJ = 2.0 * XSYM - XM[j];
		coeff(XI, YI, XJ, YJ, AJ, -COSBJ, -SINBJ, 1);
		
		UXNEG = UXNEG +  UXS * D[JS] +  UXN * D[JN];
		UYNEG = UYNEG +  UYS * D[JS] +  UYN * D[JN];
		SIGXX = SIGXX + SXXS * D[JS] + SXXN * D[JN];
		SIGYY = SIGYY + SYYS * D[JS] + SYYN * D[JN];
		SIGXY = SIGXY + SXYS * D[JS] + SXYN * D[JN];
		
		USNEG =  UXNEG * COSBI + UYNEG * SINBI;
		UNNEG = -UXNEG * SINBI + UYNEG * COSBI;
		USPOS = USNEG - D[IS];
		UNPOS = UNNEG - D[IN];
		UXPOS = USPOS * COSBI - UNPOS * SINBI;
		UYPOS = USPOS * SINBI + UNPOS * COSBI;
		SIGS  = (SIGYY-SIGXX)*SINBI*COSBI + SIGXY*(COSBI*COSBI-SINBI*SINBI);
		SIGN  = SIGXX*SINBI*SINBI-2.0*SIGXY*SINBI*COSBI+SIGYY*COSBI*COSBI;
		
	    }
	}
    }

    protected void computeDisplacementsAndStressLocaly(double XBEG, double YBEG,
						       double XEND, double YEND, double NUMPB)
    {
	double NUMOS = 0, NPOINT, DELX, DELY, NUMP, XP, YP, UX, UY;
	double XJ, YJ, AJ, COSBJ, SINBJ, SIGXX, SIGYY, SIGXY;
	int JN, JS;

	if (NUMOS <= 0) return;
	NPOINT = 0;

	for (int n=1; n < NUMOS; n++) {
	    // READ XBEG, YBEG, XEND, YEND, NUMPB
	    NUMP = NUMPB + 1;
	    DELX = (XEND - XBEG) / NUMP;
	    DELY = (YEND - YBEG) / NUMP;
	    if ( NUMPB > 0)
		NUMP= NUMP + 1;
	    if (DELX*DELX+DELY*DELY == 0)
		NUMP = 1;
	    for (int ni=1; ni < NUMP; ni++) {
		XP = XBEG + (ni-1)*DELX;
		YP = YBEG + (ni-1)*DELY;
		
		UX = 0; UY = 0;
		SIGXX = PXX;
		SIGYY = PYY;
		SIGXY = PXY;
		
		for (int j=1; j<C.length; j++) {
		    JN = 2 * j;
		    JS = JN - 1;
		    initl();
		    XJ = XM[j];
		    YJ = YM[j];
		    AJ = A[j];
		    
		    if (Math.sqrt((XP-XJ)*(XP-XJ)+(YP-YJ)*(YP-YJ)) < 2.0 * AJ)
			return;
		    
		    COSBJ = COSBET[j];
		    SINBJ = SINBET[j];
		    coeff(XP, YP, XJ, YJ, AJ, COSBJ, SINBJ, 1);
		    
		    XJ = 2.0 * XSYM - XM[j];
		    coeff(XP, YP, XJ, YJ, AJ, COSBJ, -SINBJ, -1);
		    
		    YJ = 2.0 * YSYM - YM[j];
		    coeff(XP, YP, XJ, YJ, AJ, -COSBJ, SINBJ, -1);
		    
		    XJ = 2.0 * XSYM - XM[j];
		    coeff(XP, YP, XJ, YJ, AJ, COSBJ, -SINBJ, -1);
		    XJ = XM[j];
		    YJ = 2.0 * YSYM - YM[j];
		    coeff(XP, YP, XJ, YJ, AJ, -COSBJ, SINBJ, -1);
		    XJ = 2.0 * XSYM - XM[j];
		    coeff(XP, YP, XJ, YJ, AJ, -COSBJ, -SINBJ, 1);
		    
		    UX = UX +  UXS * D[JS] +  UXN * D[JN];
		    UY = UY +  UYS * D[JS] +  UYN * D[JN];
		    SIGXX = SIGXX + SXXS * D[JS] + SXXN * D[JN];
		    SIGYY = SIGYY + SYYS * D[JS] + SYYN * D[JN];
		    SIGXY = SIGXY + SXYS * D[JS] + SXYN * D[JN];
		    
		    NPOINT = NPOINT + 1;
		}
	    }
	}
	
    }   
}
