package fr.lifl.rage.demos.anerouge;

import java.util.*;
import fr.lifl.rage.*;
import fr.lifl.magique.*;
import fr.lifl.rage.wrappers.*;
import fr.lifl.magique.skill.*;
import fr.lifl.rage.demos.anerouge.graph.*;

public class AneRougeSkill extends MagiqueDefaultSkill {

    protected Shape shape;
    protected HashMap symbolToPieces;
    protected TaskFactory factory;

    public AneRougeSkill(Agent a) {
	super(a);
	
	boolean[][] shapeTab = new boolean[][] {
	    {false, false, false, false, false},
	    {false, true,  true,  true,  false},
	    {false, true,  true,  true,  false},
	    {false, true,  true,  true,  false},
	    {false, true,  true,  true,  false},
	    {false, false, false, false, false}
	};

	shape = new Shape(shapeTab);
	symbolToPieces = new HashMap();
	
	//  ----
	// |    |
	// |    | (empty)
	//  ----
	symbolToPieces.put("0", 
			   new Piece(Shape.makeRectangularShape(1,1), "0"));

	//  ----
	// |    |
	// |    |
	// |    |
	// |    |
	//  ----	
	symbolToPieces.put("1", 
			   new Piece(Shape.makeRectangularShape(1,2), "1"));

	//  --------
	// |        |
	// |        |
	// |        |
	// |        |
	//  --------
	symbolToPieces.put("2", 
			   new Piece(Shape.makeRectangularShape(2,2), "2"));

	//  ----
	// |    |
	// |    |
	//  ----
	symbolToPieces.put("3", 
			   new Piece(Shape.makeRectangularShape(1,1), "3"));

	//  --------
	// |        |
	// |        |
	//  --------
	symbolToPieces.put("4", 
			   new Piece(Shape.makeRectangularShape(2,1), "4"));
    }
    
    public void startSkill(String pieces) {

	/*************************************************************
	 *
	 * Phase 1: parcourir l'ensemble des états et éliminer:
	 *            - les doublons
	 *            - les états invalides (i.e. ceux qui ne 
	 *              tiennent pas dans le board)
	 *
	 *************************************************************/
	System.out.println("--------------------------------");
	System.out.println("         Phase 1 started");
	System.out.println("--------------------------------\n");

	factory = new Phase1Factory(shape, pieces, 
				    symbolToPieces); 

	int numOfTasks = 0;
	while(factory.hasNext()) {
	    Task task = factory.next();

	    perform("addNewTask", new Object[] { new WrapperTask(task) });
	    numOfTasks ++;
	}

	System.out.println(numOfTasks + " tasks launched !");

	/*
	 * Attendre que toutes les taches aient termine
	 */
	Integer nelements;
	do {
	    nelements = (Integer) askNow("numberOfElements");
	} while(nelements.intValue() != numOfTasks);

	/*
	 * Recuperation des resultats. Les états invalides ont été
	 * éliminé mais il reste des doublons. Ils sont éliminés
	 * automatiquement par le HashSet.
	 */
	Set allResults = (Set) askNow("getAll");
	Iterator it = allResults.iterator();	
	HashSet allStates = new HashSet();

	while(it.hasNext()) {
	    Result r = (Result) it.next();
	    HashSet s = (HashSet) r.get(HashSet.class, "states");

	    for(Iterator it2 = s.iterator(); it2.hasNext(); ) 
		allStates.add(it2.next());
	}
	
	System.out.println("We have found " + 
			   allStates.size() + 
			   " valid states");	

	/*
	 * Vider le ResultRepository
	 */
	System.out.println("--------------------------------");
	System.out.println("   Cleaning the repository ...");
	System.out.println("--------------------------------\n");
	askNow("cleanUp");

	/*************************************************************
	 *
	 * Phase 2: calcul des graphes. Lorsqu'un graphe a été 
	 * calculé, récupéré tous ses états et arrêter les tâches 
	 * dont le graphe l'un de ces états.
	 *************************************************************/
	System.out.println("--------------------------------");
	System.out.println("         Phase 2 started");
	System.out.println("--------------------------------");

	factory = new Phase2Factory(shape, allStates, symbolToPieces);
	while(factory.hasNext()) {
	    Task task = factory.next();

	    perform("addNewTask", new Object[] { new WrapperTask(task) });
	}

	int lastCount = 0; // Conserve le nombre de Result dans le Repository
	while(true) {

	    Thread.yield();

	    allResults = (Set) askNow("getAll");
	    
	    // Si aucun nouveau resultat, continuer
	    if (allResults.size() == lastCount)
		continue;

	    lastCount = allResults.size();

	    // Si toutes les tâches sont finies, arrêter.
	    if (allResults.size() == allStates.size())
		break;
	    
	    // Supprimer les tâches dont le graphe contient
	    // un état calculé.
	    it = allResults.iterator();
	    while(it.hasNext()) {
		Result r = (Result) it.next();
		CompactGraph cg = (CompactGraph)
		    r.get(CompactGraph.class,"compactGraph");
		
		if (cg != null) {
		    CompactGraph.CompactState[] cs = cg.getRawGraph();

		    for(int i = 0; i < cs.length; i ++) 
			concurrentAsk("killTask", 
				      new Object[] {cs[i].getState()});
		}
	    }
	}
	
	/*
	 * Terminer. Une eventuelle phase 3 viendrait ici.
	 */
	System.out.println("All graphs computed !");
	System.out.println("Number of compact graphs = " + 
			   allResults.size());	
    }
}
