#!/bin/bash

CLASSPATH=$(ls sim/*.jar | xargs | sed 's/ /:/g'):$(ls iSnorkeling/lib/*.jar | xargs | sed 's/ /:/g')
SOURCEPATH=iSnorkeling/src/:sim/src/
javadoc -classpath $CLASSPATH -private -author -sourcepath $SOURCEPATH \
  -exclude '*Test' -subpackages isnork -d doc/ -linksource
