PACKAGE dynamic.acqua


Experiment 1 of dynamic organization of multi-agent system with Magique.

This package contains the source code for the first experiment
described in the paper that can be found at
http://www.lifl.fr/MAGIQUE/dynamicity/paper.html

"Dynamic creation of acquaintances in the MAS to match to the natural relationship"


You need magique.jar archive to run this example
magique.jar can be downloaded at 

	    http://www.lifl.fr/MAGIQUE topic download


COMPILE :
=========


* go in the root directory of dynamic/acqua
* compile by (assuming magique.jar is in this diretory) :
	  java -cp .:magique.jar dynamic/acqua/*.java

EXECUTE :
=========

We assume you have 4 hosts to execute the different agents. If it is
not the case, launch 4 JVM on the same hosts...

We will name : host1 host2 host3 and host4, the IP address of this
host (in the number or symbolic format - you, of course, need to
have access to a DNS if you use symbolic name)

for each host, go in the root directory of dynamic/acqua

on host1 execute :
 java -cp .:magique.jar fr.lifl.magique.Start dynamic.acqua.SuperImp 4444 [n [v]]


this starts the SuperImp magique program, with an agent platform on
port 4444, you can chose another port if you want (but then make the
appropriate change in the following)
=> the agents of the hierarchy S, S1 and S2 (see figure in the paper) are created (and
hierarchically connected)

=> option n : n pairs of agents that exchanged messages are created to
simulate other agents in the MAS, these agents produce noise and load
in the MAS. Giving a value <> 0 to n gives a more "realistic"
interpretation to the experiment and the benefit of the creation of
acquaintance is greater and more easily perceived.

=> option v : verbose, messages exchanged and action in magique are
detailed, 0(mute)<= v <=5(very wordy!)
a verbose of 2 should give enough information to understand fully what happens

on host2 execute :
 java -cp .:magique.jar fr.lifl.magique.Start dynamic.acqua.ServiceProviderImpl1 4444 host1:4444 [v]

(if host1=host2 then the port 4444 for ServiceProviderImpl1 must be
changed to any other value... it will be the same for other hosts)
=> option v : verbose (see host1)

this starts the first service provider agent


on host3 execute :
 java -cp .:magique.jar fr.lifl.magique.Start dynamic.acqua.ServiceProviderImpl2 4444 host1:4444 [v]

this starts the first second provider agent

=> option v : verbose (see host1)


on host4 execute :
 java -cp .:magique.jar fr.lifl.magique.Start dynamic.acqua.ServiceUserImpl 4444 host1:4444 [v]

this starts the service user agent

=> option v : verbose (see host1)


RUN
===

at the beginning one of the 2 service provider is chosen to provide
service used by service user, communications use the hierarchical
structure, times between request is sent and payment is made are
given.

After some time, direct acquaintance between service user and this
service provider is created acoording to a autonomous decision (see the
"*AnswerLog*" message) of the service user. Displaid times should be
smaller (the greater option n had been set, the greated the benefit
should be)...

Then this service provider is killed.

Then the other requests are sent to the other service provider using
the hierarchy first, then, again, direct acquaintance relations is created.



NOTE 
====

You could hae a problem of native threads creation if you chose a too
big value for option n... this is an OS problem, it can be
parametrized, but if you do not wait too much before running the
agents on the four hosts, it should be sufficient to run experiment.
