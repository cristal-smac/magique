PACKAGE dynamic.pool


Experiment 3 of dynamic organization of multi-agent system with Magique.

This package contains the source code for the second experiment
described in the paper that can be found at
http://www.lifl.fr/MAGIQUE/dynamicity/paper.html

"Create a full of skilled apprentices to avoid of being overwhelmed by requests to a given skill"


You need magique.jar archive to run this example
magique.jar can be downloaded at 

	    http://www.lifl.fr/MAGIQUE topic download


COMPILE :
=========


* go in the root directory of dynamic/pool


* in the ServiceProviderImpl.java file, edit the line
  "a.setPoolHosts(....)" and puts in the String array the IP address
  of hosts you can will use later to create the pool of
  agents. Comment this line if you do not have other available host.

  In the following we will refer as platformIPHost to these hosts.


* compile by (assuming magique.jar is in this diretory) :
	  java -cp .:magique.jar dynamic/pool/*.java

EXECUTE :
=========

We assume you have 2 hosts to execute the different agents. If it is
not the case, launch 2 JVM on the same hosts... BUT IN THIS CASE, YOU
WILL PROBABLY NOTICE NO IMPROVEMENT WHEN THE POOL OF AGENTS ARE
CREATED SINCE THE SAME CPU WILL DO THE JOB.

We will name : host1 host2 and host3, the IP address of this
host (in the number or symbolic format - you, of course, need to
have access to a DNS if you use symbolic name)

for each host, go in the root directory of dynamic/pool

on host1 execute :
 java -cp .:magique.jar fr.lifl.magique.Start dynamic.pool.SuperImp 4444 [n [v]]


this starts the SuperImp magique program, with an agent platform on
port 4444, you can chose another port if you want (but then make the
appropriate change in the following)

=> the agents of the hierarchy S, S1 and S2 (see figure in the paper) are created (and
hierarchically connected)
Noisy agents who make requests to service PI are created too.
Service user agent (who makes requests to service SIGMA) is created.


=> option n (default =1) : to create n agents that sent messages for service
PI are created to simulate other agents in the MAS, these agents
produce noise and load in the MAS. Giving a value <> 0 to n gives a
more "realistic" interpretation to the experiment and the benefit of
the creation of the pool is greater and more easily perceived.

Experiment in the paper were done with n=8

=> option v : verbose, messages exchanged and action in magique are
detailed, 0(mute)<= v <=5(very wordy!)
a verbose of 2 should give enough information to undestand fully what happens


on host2 execute :
 java -cp .:magique.jar fr.lifl.magique.Start dynamic.pool.ServiceProviderImpl 4444 host1:4444 [v]

(if host1=host2 then the port 4444 for ServiceProviderImpl must be
changed to any other value... it will be the same for other hosts)
=> option v : verbose (see host1)

this starts the first service provider agent


on each platformIPHost execute :

   java -cp magique.jar fr.lifl.magique.Start fr.lifl.magique.PlatformLauncher [portHost]

this will launch some (empty) platform that will accept the created apprentice 

RUN
===

at the beginning, service user send requests for service SIGMA and the
noisy agents sent request to some service PI. There is a great number
of requests to PI and this disturb the service provider who can not
answer quickly to service SIGMA requests.

After some times a pool of agents is created to help the service
proveider to satisfy requests to SIGMA : he creates the "apprentice"
agents and teaches them the PI skill, then he distributes the
request to PI to these agents. Thus the service provider has more
times to answer to requests to SIGMA.

The same number of requests to SIGMA and PI are sent in the two steps
and the 



NOTE 
====

You could hae a problem of native threads creation if you chose a too
big value for option n... this is an OS problem, it can be
parametrized, but if you do not wait too much before running the
agents on the four hosts, it should be sufficient to run experiment.
