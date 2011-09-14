package isnork.g7;

import isnork.sim.SeaLifePrototype;

import java.util.Comparator;

public class SeaLifeComparator implements Comparator<SeaLifePrototype> {


	@Override
	public int compare(SeaLifePrototype o1, SeaLifePrototype o2) {
		if(o1.getHappiness()>o2.getHappiness())
			return -1;
		if(o1.getHappiness()>o2.getHappiness())
			return 1;
		return 0;
	}

}
