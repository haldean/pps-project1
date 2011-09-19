package isnork.g3;

import isnork.sim.SeaLifePrototype;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;
import com.google.common.collect.ImmutableSortedSet;

public class WaterProofPokedex extends AbstractPokedex{
	Map<Integer,String> speciesRanking;

	public WaterProofPokedex(Set<SeaLifePrototype> species) {
		super(species);

		speciesRanking = Maps.newHashMap();
		Ordering<SeaLifePrototype> happiness = new Ordering<SeaLifePrototype>() {
			public int compare(SeaLifePrototype left, SeaLifePrototype right) {
				return Doubles.compare(left.getHappinessD(), right.getHappinessD());
			}
		};
		ImmutableSortedSet<SeaLifePrototype> sortedSpecies = ImmutableSortedSet.orderedBy(happiness).build();
		
		int i = 0;
		for(SeaLifePrototype aSpecies : sortedSpecies) {
			speciesRanking.put(i++, aSpecies.getName());
		}
	}

	public Map<Integer,String> getSpeciesRanking() {
		return speciesRanking;
	}

}
