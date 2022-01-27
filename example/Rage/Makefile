##
## Makefile for the Rage package
##

include Make.rules

HOME=$(RAGE_HOME)
SRC_DIR=$(HOME)/src
CLASSES_DIR=$(HOME)/classes

all:
	$(JAVAC) -d $(CLASSES_DIR) `find $(SRC_DIR) -name '*.java'`
	(cd $(CLASSES_DIR) ; jar cvf $(RAGE_LIB_DIR)/rage.jar .)

javadoc:
	$(JAVADOC) -d $(RAGE_JAVADOC) -windowtitle 'Rage documentation' `find $(SRC_DIR) -name '*.java'`

clean:
	find $(CLASSES_DIR) -name *.class -exec rm {} \;
	find $(SRC_DIR) -name '*~' -exec rm {} \;
	-rm -f $(RAGE_LIB_DIR)/rage.jar rage.container
	-rm -rf $(RAGE_JAVADOC)/* 
