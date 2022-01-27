Ce package a pour objectif d'illustrer l'utilisation
de méthodes synchrones et asynchrones avec Magique

Le problème est trivial :
on a deux agents nommés f et g qui renvoient un entier
au bout d'un certain temps. Le superviseur doit récupérer
les deux entiers renvoyés et en faire la somme .

En synchrone ce temps mis pour ce calcul est la somme du temps
de f + le temps de g tandis qu'en asynchone ce temps est
le max(f,g).

Cette application a été réglée avec des temps de 10s pour f 
et 15s pour g.
