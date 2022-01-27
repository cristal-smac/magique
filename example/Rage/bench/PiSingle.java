public class PiSingle {
    
    public static void main(String[] args) throws Exception {
	
	if (args.length < 2) {
	    System.err.println("usage: PiSingle number_of_task number_of_iteration");
	    System.exit(0);
	}
       
	int ntasks = Integer.parseInt(args[0]);
	int niter = Integer.parseInt(args[1]);
	int inner = 0, total = 0;
	double x, y, tmp, pi = 0;

	for(int i = 0; i < ntasks; i++) {
	    for(int j = 0; j < niter; j ++) {
		x = Math.random();
		y = Math.random();
		
		if (java.lang.Math.sqrt(x*x + y*y) <= 1.0)
		    inner ++;
		
		total ++;
	    }
	    
	    tmp = ((double)inner / (double)total) * 4;
	    pi = (pi + tmp) / 2;
	}

	//System.out.println("PI = " + pi);
    }
}
