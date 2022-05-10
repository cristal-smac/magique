# Magique

Vous trouverez une [présentation détaillée à cet endroit](https://www.cristal.univ-lille.fr/SMAC/projects/magique/presentation/presentationContent.html).


Vous pouvez découvrir le fonctionnement de Magique dans [le tutoriel](./tutorial/tutorial.pdf).


Ce tutoriel s'accompagne d'exemples. D'autres exemples se trouvent dans le dossier `example` (les liens vers `www.lifl.fr/magique` sont cassés et les examples `Rage` et `vente` sont difficilement exploitables en l'état).

La présentation ci-dessous donne des éléments pour permettre l'exécution d'un premier exemple jouet.

## Exécution de Magique

Les fichiers `magique.jar` et `magiqueGui.jar` fournis ont été générés à partir de la version `18.0.1.1`du compilateur java d'Oracle.

Ils peuvent être reconstruits (éventuellement pour une version
différente de java) à l'aide du fichier `Makefile` fourni à l'aide de
la commande

```bash
  make all
```

Dans la suite de ce document, on suppose que les fichiers `magique.jar` et `magiqueGui.jar` se trouvent dans le dossier `.../magique/`.

## Exemple simple : Pong

Nous allons aborder l'exemple  **Ping-Pong**.
Les codes se trouvent dans le dossier `.../magique/example/pingpong` (et aussi dans le dossier `.../magique/tutorial/exemples/chap2`).

L'exécution des autres exemples reprend les mêmes principes.

Par la suite on suppose que les commandes décrites sont exécutées depuis le dossier `.../magique/example/pingpong/`. 

### Exécution non distribuée dans un seul terminal

Pour vérifier que tout fonctionne correctement, on va exécuter le SMA en plaçant les trois agents au sein de la même plateforme (donc la même JVM), en centralisé.

Il peut être nécessaire de commencer par compiler les codes fournis

```bash
 javac -classpath .:../../magique.jar *.java
```

(NB : sous windows, il faudra replacer le ":" dans l'option classpath par ";", il est aussi possible qu'il faille mettre des guillemets, ce qui donnerait :
```windows
 javac -classpath ".;../../magique.jar" *.java
```

Dans un premier terminal, il faut démarrer le SMA et ses trois agents

```bash
 java -classpath .:../../magique.jar fr.lifl.magique.Start TestImp
```
On obtient la trace :
```bash
....................................
 Magique : PlatformAgent created
..null
.................>>> bossName : super@134.206.12.150:4444
>>> bossName : super@134.206.12.150:4444
ping 0
pong 1
ping 2
pong 3
ping 4
pong 5
ping 6
pong 7
(...)
```

Les agents se répondent par `ping` ou `pong` en alternance et infiniment. Il faut interrompre cela par un superbe `Ctrl+c`.

### Exécution distribuée dans des terminaux

Voici comment exécuter ce programme en mode distribué, en exécutant les agents impliqués dans trois terminaux différents du même ordinateur.

Si cela n'a pas été fait à l'étape précédente, il peut être nécessaire de commencer par compiler les codes fournis

```bash
 javac -classpath .:../../magique.jar *.java
```

Dans un premier terminal, il faut démarrer l'agent **Superviseur**

```bash
 java -classpath .:../../magique.jar fr.lifl.magique.Start SuperImp
```
On obtient la trace :
```bash
....................................
 Magique : PlatformAgent created
..null
...............
```

Chacun des agents *Ping* et *Pong* doit être démarré dans son propre terminal. Il faut leur indiquer l'adresse du superviseur. Pour cela il faut préciser l'adresse IP et le port d'écoute du superviseur. 

L'adresse IP de l'ordinateur (et donc du superviseurs) peut être obtenue à l'aide de la commande `ifconfig` sous linux, ou `ipconfig` sous windows.

Dans la suite nous supposerons que cette adresse est `192.168.100.001`. Précisons qu'il n'est pas possible d'utiliser l'adresse `127.0.0.1` ni `localhost`.

Le port aura la valeur par défaut 4444 puisque nous n'avons rien précisé lors du démarrage de ce superviseur.

Les agents *Ping* et *Pong* étant exécutés sur le même ordinateur, il va falloir préciser un port d'exécution différent pour chacun d'entre eux. Nous prendrons, arbitrairement, 5555 et 6666.


Ce qui donne pour chacun *Ping* :
```bash
 java -classpath .:../../magique.jar fr.lifl.magique.Start PingImp  5555 192.168.100.001:4444
```
et la trace où apparaît les informations de connexion au superviseur
```bash
....................................
 Magique : PlatformAgent created
..null
................Platform : connect to 192.168.100.001:4444
192.168.100.001:4444 not yet known
192.168.100.001:4444 already known
connection with 192.168.100.001:4444 performed
ping 0
```
A la fin de cet affichage on voit apparaitre la trace de l'envoi du premier message `ping` en attente de réponse.

Du côté du *superviseur* on peut constater la trace de connexion de l'agent *Ping* avec l'apparition des messages
```bash
...............192.168.100.001:5555 not yet known
connection with 192.168.100.001:5555 performed
```

Il nous reste à démarrer *Pong* de manière similaire :

```bash
 java -classpath .:../../magique.jar fr.lifl.magique.Start PongImp 6666 192.168.100.001:4444
```

On constate alors dans le terminal du superviseur la connexion de l'agent *Pong*, ainsi que dans les terminaux des agents *Ping* et *Ping* l'envoi infini des messages `ping`  et `pong` entre les deux agents.

Il faut mettre fin à leur partie de ping-pong infinie à nouveauà l'aide de `Ctrl+c` dans l'un des terminaux.

On peut augmenter le niveau de trace pour voir les messages échangés en ajoutant un paramètre à l'exécution. Le niveau de trace va de 0 à 5 :
```bash
java -classpath .:../../magique.jar fr.lifl.magique.Start PingImp  5555 192.168.100.001:4444 5
```

(pour le superviseur il devient nécessaire de préciser la valeur du port 4444 avant le niveau de trace).

### Exécution avec l'interface graphique

Les commandes suivantes sont exécutées depuis le dossier `.../magique/`;

Il faut démarrrer un agent `PlatformLauncher` :
```bash
 java -jar magique.jar
```

Puis l'interface graphique (qu'il convient de passer en plein écran).

```bash
 java -cp magiqueGUI.jar fr.lifl.magique.gui.LanceurAgents
```

Charger la configuration : menu *File* puis *load* : `pingpong.magic`.

Editer l'adresse IP de l'ordinateur, puis sélectionner chaque agent puis cliquer sur l'hôte pour mettre à jour l'adresse de l'agent.

![ajuster les adresses IP](./images/gui-computerIP.png)

Cliquer ensuite sur *Execute*, la fenêtre d'exécution s'ouvre et vous pouvez réorganiser les "sous"-fenêtre par agent.

Sélectionnez *Show* puis "Console Tools". Dans la fenêtre qui s'ouvre saisissez :

`agent.perform(Ping,"ping",1)`

![exécution](./images/gui-execution.png)

et validez pour observer l'exécution. Il est possible d'interrompre l'exécution à l'aide du menu `kill` de la fenêtre `Exécution`.


## Bilbiographie
 * Philippe Mathieu, Jean-Christophe Routier, Yann Secq. [RIO: Roles, Interactions and Organizations](./publis/ceemas03-final.pdf). Proceedings of the 3rd International/Central and Eastern European Conference on Multi-Agent Systems (CEEMAS 2003), 2003, undef, France. pp.147--157. [hal-00731975]
 * Philippe Mathieu, Jean-Christophe Routier, Yann Secq. [Using agents to build a distributed calculus framework](./publis/aisbj2002_rage.pdf). The Interdisciplinary Journal of Artificial Intelligence and the Simulation of Behaviour, 2002, 1 (2), pp.197--208. [hal-00731915]
 * Philippe Mathieu, Jean-Christophe Routier, Yann Secq. [Principles for dynamic multi-agent organizations](./publis/prima02.pdf). Proceedings of 5th Pacific Rim International Workshop on Multi-Agents (PRICAI2002/PRIMA2002), 2002,  France. pp.109--122. [hal-00732049]
 * Philippe Mathieu, Jean-Christophe Routier, Yann Secq. RAGE : [An agent framework for easy distributed computing](./publis/rage-aisb-2002.pdf). Proceedings of the Artificial Intelligence and the Simulation of Behaviour symposium on Artificial Intelligence and Grid Computing (AISB'2002), 2002,  France. pp.20-24. [hal-00731967]
 * Philippe Mathieu, Jean-Christophe Routier. [A Multi-Agent Approach to Co-operative Work](./publis/cadui2002.pdf). Proceedings of 4th International Conference on Computer-Aided Design of User Interfaces (CADUI'02), 2002, undef, France. pp.367--380. [hal-00731973]
 * Philippe Mathieu, Jean-Christophe Routier, Yann Secq. Ubiquitous Computing : vanishing the notion of application. Proceedings of the first International Joint Conference on Autonomous Agents and Multi Agent Systems Workshop on Ubiquitous Agents on embedded, wearable, and mobile devices (UAEWMD@AAMAS2002), 2002, undef, France. [hal-00731956]
 * Philippe Mathieu, Jean-Christophe Routier, Yann Secq. Dynamic organization of multi-agent systems. Proceedings of the first International Joint Conference on Autonomous Agents and Multi-Agent Systems (AAMAS'2002), 2002, undef, France. pp.451--452. [hal-00731955]
 * Philippe Mathieu, Jean-Christophe Routier. [Une contribution du multi-agent aux applications de travail coopératif](./publis/diapo-dans-tsi-2001.pdf). Réseaux et systèmes répartis, calculateurs Parallèles, 2001, 13 (2-3), pp.207--226. [hal-00731926]
 * Jean-Christophe Routier, Philippe Mathieu, Yann Secq. [Dynamic Skill Learning: A Support to Agent Evolution](./publis/aisb01-dynamic-skill-learni.pdf). Proceedings of the Artificial Intelligence and the Simulation of Behaviour symposium on Adaptive Agents and Multi-agent systems (AISB'01), 2001, undef, France. pp.25--32. [hal-00731965]


