you need java >= jdk 1.* 
you need the magique.jar (http://www.lifl.fr/MAGIQUE)

THE PROBLEM :
************
Explore a labyrinth using a multi-agent system writtent in MAGIQUE



PRESENTATION :
**************

This exercise was given to student of the "DEA de Lille" (one year degree
required to prepare the Ph D).They had no prior knownledge on Magique,
except a quick presentation of the model.


Here are 2 realisations
      * labyrinth1.zip
      * labyrinth2.zip


FOR MORE DETAILS... read the lisezmoi.txt in frecnh (sorry...)


POUR LABYRINTH1
***************

WINDOWS/DOS :
java -cp %CLASSPATH%;labyrinth1.jar AllImp

UNIX/Linux :
java -cp $CLASSPATH:labyrinth2.jar AllImp



POUR LABYRINTH2
***************

WINDOWS/DOS :
java -cp %CLASSPATH%;labyrinth2.jar Exploration.ExplorationDeLabyrinthe [0 -n n]

UNIX/Linux :
java -cp $CLASSPATH:labyrinth2.jar Exploration.ExplorationDeLabyrinthe [0 -n n]

où	n = number of explorers,  = 3 par défaut
