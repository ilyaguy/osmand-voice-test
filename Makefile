JAVA_FILES:=$(wildcard vt/*.java)
JFLAGS = -g -cp "./lib/*"
#
# the rest is independent of the directory
#
JAVA_CLASSES:=$(patsubst %.java,%.class,$(JAVA_FILES))

.PHONY: classes
LIST:=

classes: $(JAVA_CLASSES)
	if [ ! -z "$(LIST)" ] ; then javac $(JFLAGS) $(LIST) ; fi

$(JAVA_CLASSES) : %.class : %.java
	$(eval LIST+=$$<)

jar:	classes
	jar cfm vt.jar manifest.txt vt/*.class

test:	jar
	java -jar vt.jar

clean:
	$(RM) vt/*.class
	$(RM) vt.jar
