#!/bin/bash

CLASSPATH=$(ls sim/*.jar | xargs | sed 's/ /:/g'):$(ls iSnorkeling/lib/*.jar | xargs | sed 's/ /:/g')
SOURCEPATH=iSnorkeling/src/:sim/src/
javac -cp $CLASSPATH -sourcepath $SOURCEPATH sim/src/isnork/sim/GameEngine.java iSnorkeling/src/isnork/g3/AwsmPlayer.java -d bin/
java -cp $CLASSPATH:bin/ isnork.sim.GameEngine gui
