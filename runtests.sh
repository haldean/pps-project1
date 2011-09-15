#!/bin/bash

CLASSPATH=$(ls sim/*.jar | xargs | sed 's/ /:/g'):$(ls iSnorkeling/lib/*.jar | xargs | sed 's/ /:/g')
SOURCEPATH=iSnorkeling/src/:sim/src/
javac -cp $CLASSPATH -sourcepath $SOURCEPATH -Werror -Xlint:all \
  iSnorkeling/src/isnork/g3/AllTests.java -d bin/ && \
  java -cp $CLASSPATH:bin/ org.junit.runner.JUnitCore isnork.g3.AllTests
