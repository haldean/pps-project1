#!/bin/bash

CLASSPATH=$(ls sim/*.jar | xargs | sed 's/ /:/g'):$(ls iSnorkeling/lib/*.jar | xargs | sed 's/ /:/g')
SOURCEPATH=iSnorkeling/src/:sim/src/
javac -cp $CLASSPATH -sourcepath $SOURCEPATH -Werror -Xlint:all \
  sim/src/isnork/sim/GameEngine.java \
  iSnorkeling/src/isnork/g3/AwwsimPlayer.java \
  -d bin/ && \
  java -Xmx1g -cp $CLASSPATH:bin/ isnork.sim.GameEngine trials 5 20 20 1317021521936 isnork.g3.AwwsimPlayer 1 100.xml output.txt
