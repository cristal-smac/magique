PACKAGE dynamic.learn


Experiment 2 of dynamic organization of multi-agent system with Magique.

This package contains the source code for the second experiment
described in the paper that can be found at
http://www.lifl.fr/MAGIQUE/dynamicity/paper.html

"adapt the distribution of the skills by exchanging skills between agents"


You need magique.jar archive to run this example
magique.jar can be downloaded at 

	    http://www.lifl.fr/MAGIQUE topic download


COMPILE :
=========


* go in the root directory of dynamic/learn
* compile by (assuming magique.jar is in this diretory) :
	  java -cp .:magique.jar dynamic/learn/*.java




EXECUTE :
=========

We assume you have 2 hosts to execute the different agents. If it is
not the case, launch 2 JVM on the same hosts... 

We will name : host1 host2 and host3, the IP address of this
host (in the number or symbolic format - you, of course, need to
have access to a DNS if you use symbolic name)

for each host, go in the root directory of dynamic/learn

on host1 execute :
 java -cp .:magique.jar fr.lifl.magique.Start dynamic.learn.SuperImp 4444 [n [v]]


this starts the SuperImp magique program, with an agent platform on
port 4444, you can chose another port if you want (but then make the
appropriate change in the following)
=> the agents of the hierarchy S, S1 and S2 (see figure in the paper) are created (and
hierarchically connected)

=> option n : n pairs of agents that exchanged messages are created to
simulate other agents in the MAS, these agents produce noise and load
in the MAS. Giving a value <> 0 to n gives a more "realistic"
interpretation to the experiment and the benefit of the learning is
greater and more easily perceived.

=> option v : verbose, messages exchanged and action in magique are
detailed, 0(mute)<= v <=5(very wordy!)
a verbose of 2 should give enough information to undestand fully what happens

on host2 execute :
 java -cp .:magique.jar fr.lifl.magique.Start dynamic.learn.ServiceProviderImpl1 4444 host1:4444 [v]

(if host1=host2 then the port 4444 for ServiceProviderImpl1 must be
changed to any other value... it will be the same for other hosts)
=> option v : verbose (see host1)

this starts the first service provider agent


on host3 execute :
 java -cp .:magique.jar fr.lifl.magique.Start dynamic.learn.ServiceUserImpl 4444 host1:4444 [v]

this starts the service user agent

=> option v : verbose (see host1)


RUN
===

at the beginning, service user send requests that reach, using the
hierarchy, the service provider.  After some times, serive user
autonomously decides (see the "*AnswerLog*" message) to learn the
ServiceSkill that is effectively exchanged between the two agents and
thus service user agent can "execute" it himself...


change experiment:
=======
To make the experiment more convincing, copy the file for the service
user in a different directory : create a dynamic/learn directory with
only the ServiceUser*.* files (the important point is that
"ServiceSkill" file will not be there, this file must NOT BE in the
CLASSPATH for the service user in host3).
You run the same experiment, and it works as well : this shows that
the bytecode of "ServiceSkill" is effectively "pysically" exchanged
between the platform of the agents.



NOTE 
====

You could hae a problem of native threads creation if you chose a too
big value for option n... this is an OS problem, it can be
parametrized, but if you do not wait too much before running the
agents on the four hosts, it should be sufficient to run experiment.
