package isnork.g3;

import isnork.sim.SeaLifePrototype;

import java.util.Comparator;

public class SLPComparator implements Comparator<SeaLifePrototype> {

    public int compare(SeaLifePrototype slp1, SeaLifePrototype slp2) {

        if (((SeaLifePrototype)slp1).getHappiness() > ((SeaLifePrototype)slp2).getHappiness())
            return -1;
        else if (((SeaLifePrototype)slp1).getHappiness() < ((SeaLifePrototype)slp2).getHappiness())
            return 1;
        else
            return 0;
    }

}
