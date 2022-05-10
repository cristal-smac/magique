.PHONY: clean, test, docs
VERSION=
SOURCE_JAR = ../magique-source.jar
MAGIQUE_JAR = ../magique$(VERSION).jar
MAGIQUE_MANIFEST = ../magique-manifest.mf
MAGIQUE_GUI_JAR = ../magiqueGUI$(VERSION).jar
MAGIQUE_GUI_MANIFEST = ../magiqueGUI-manifest.mf
CLASSES = ../classes
LIB = ../lib
BSH = bsh-20b1.jar
CLASSPATH = .\;$(CLASSES)\;$(LIB)/$(BSH)
JAR_LIBS = javassist


# 
all: init build jar

init:	clean
	mkdir docs
	mkdir classes
# bsh must be exploded for inclusion into jar
	cd classes; jar xf $(LIB)/$(BSH)

#compile sources
compile: 
	cd src ; javac -classpath $(CLASSPATH) $(SOURCES) -d $(CLASSES)

#création du jar
jar:    init build
	cd src; jar cf $(SOURCE_JAR) fr $(JAR_LIBS)
	cd classes; jar cfm $(MAGIQUE_JAR) $(MAGIQUE_MANIFEST)  fr/lifl/magique/*.class fr/lifl/magique/agent fr/lifl/magique/platform fr/lifl/magique/util fr/lifl/magique/skill fr/lifl/magique/policy fr/lifl/magique/PlatformLauncher.class fr/lifl/magique/gui/skills $(JAR_LIBS)
	cd classes; jar cfm $(MAGIQUE_GUI_JAR) $(MAGIQUE_GUI_MANIFEST) fr/lifl/magique $(JAR_LIBS) bsh


# compilations : target "build" to compile everything
agent:
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/agent/*.java -d $(CLASSES)

platform:
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/platform/rmi/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/platform/classloader/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/platform/*.java -d $(CLASSES)

util:
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/util/*.java -d $(CLASSES)

policy:
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/policy/*.java -d $(CLASSES)

concurrency:
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/policy/concurrency/*.java -d $(CLASSES)

skill:
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/skill/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/skill/system/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/skill/group/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/skill/magique/*.java -d $(CLASSES)

main:
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/*.java -d $(CLASSES)

gnu:
	cd src ; javac -classpath $(CLASSPATH) gnu/bytecode/*.java -d $(CLASSES)

at:
	cd src ; javac -classpath $(CLASSPATH) at/dms/classfile/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) at/dms/util/*.java -d $(CLASSES)


gui:	
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/gui/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/gui/descriptor/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/gui/draw/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/gui/execute/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/gui/file/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/gui/theinterface/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/gui/network/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/gui/skills/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/gui/tree/*.java -d $(CLASSES)
	cd src ; javac -classpath $(CLASSPATH) fr/lifl/magique/gui/util/*.java -d $(CLASSES)

build: agent platform util policy concurrency skill main gui image


image:
	cd src; cp fr/lifl/magique/gui/draw/*.gif -d ../classes/fr/lifl/magique/gui/draw/
	cd src; cp fr/lifl/magique/gui/execute/*.gif -d ../classes/fr/lifl/magique/gui/execute/
	cd src; cp fr/lifl/magique/gui/tree/*.gif -d ../classes/fr/lifl/magique/gui/tree/
	cd src; cp fr/lifl/magique/gui/theinterface/*.gif -d ../classes/fr/lifl/magique/gui/theinterface/
	cd src; cp fr/lifl/magique/gui/file/*.gif -d ../classes/fr/lifl/magique/gui/file/


#generate  documentation
doc:
	javadoc -classpath $(CLASSPATH) -d docs -sourcepath src -subpackages fr.lifl.magique

clean:
	rm -f -r classes
	rm -f -r docs
