package isnork.g3;

import isnork.sim.SeaLifePrototype;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Maps;
/**
 * This is lightly documented, but the interface pokedex has all of the 
 * details.
 * @author Moses
 *
 */

public abstract class AbstractPokedex implements Pokedex {

	private final HashMap<String, SeaLifePrototypeCounter> map;

	public AbstractPokedex(Set<SeaLifePrototype> species) {
		map = Maps.newHashMap();
		for (SeaLifePrototype specie : species) {
			map.put(specie.getName(), new SeaLifePrototypeCounter(specie));
		}
	}
	
	@Override
	public SeaLifePrototype get(String name){
    if (! map.containsKey(name)) return null;
		return map.get(name).get();
	}
	
	@Override
	public boolean isDangerous(String name){
		return 	map.get(name).isDangerous();
	}
	
	@Override
	public int getMinCount(String name){
		return map.get(name).getMinCount();
	}
	
	@Override
	public int getMaxCount(String name){
		return map.get(name).getMaxCount();
	}

	@Override
	public int getHappiness(String name){
		return map.get(name).getHappiness();
	}
	
	@Override
	public int getSeenCount(String name){
		return map.get(name).getSeenCount();
	}
	
	@Override
	public void someoneSawCreature(String name){
		map.get(name).sawCreature();
	}
	
	@Override
	public void personallySawCreature(String name){
		map.get(name).sawCreature();
		map.get(name).captured();
	}

	@Override
	public int getPersonalSeenCount(String name){
		return map.get(name).getCaptured();
	}
	
	@Override
	public boolean isMoving(String name){
		return map.get(name).isMoving();
	}

  @Override
  public List<SeaLifePrototype> getAllSpecies() {
    List<SeaLifePrototype> allSpecies = Lists.newArrayList();
    for (SeaLifePrototypeCounter count : map.values()) {
      allSpecies.add(count.get());
    }
    return allSpecies;
  }

}
