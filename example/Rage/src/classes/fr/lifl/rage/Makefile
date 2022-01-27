SUBDIRS=. skills wrappers shells 

class:
	javac -d ../../../../classes *.java

jar:
	@(rm -f rage.jar; jar cf rage.jar -C ../../../ fr/) 

javadoc:
	@javadoc -d ../../../../doc/api *.java skills/*.java wrappers/*.java gui/*.java

clean:
	@for i in $(SUBDIRS); do \
		echo Cleaning $$i/ ...; \
		rm -f $$i/*~ $$i/*.class; \
	done
	@rm -f rage.jar

