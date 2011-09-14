package isnork.g4;

import java.util.Comparator;
import isnork.sim.Observation;

public class ObservComparator implements Comparator<Observation>{
    private G4Diver diver;
    
	public ObservComparator(G4Diver player) {
	    diver = player;
	}
	
	public int compare(Observation o1, Observation o2 )
	{
		double result = StaticDiver.getDistanceFromCreature(o2.getLocation(), diver.getLocation()) -
		    StaticDiver.getDistanceFromCreature(o1.getLocation(), diver.getLocation());
		
		if(result < 0) return -1;
		if(result > 0) return 1;
		return 0;
	}
}
