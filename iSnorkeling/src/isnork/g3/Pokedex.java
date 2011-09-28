package isnork.g3;

import isnork.sim.SeaLifePrototype;

import java.util.List;
import java.util.SortedMap;

public interface Pokedex {

	/**
	 * Fetches the raw SeaLifePrototype provided at the start of the game 
	 * for the creature with that name.
	 * @param name
	 * @return
	 */
	public SeaLifePrototype get(String name);

	/**
	 * Fetches whether the creature with this name is dangerous
	 * @param name
	 * @return
	 */
	public boolean isDangerous(String name);

	/**
	 * Fetches whether the creature with this name is dangerous
	 * @param name
	 * @return
	 */
	public boolean isMoving(String name);

	/**
	 * Provides the mincount for the creature with this name
	 * @param name
	 * @return
	 */
	public int getMinCount(String name);
	
	/**
	 * Provides the maxcount for the creature with this name
	 * @param name
	 * @return
	 */
	public int getMaxCount(String name);

	/**
	 * Provides the happiness for the given species
	 * @param name
	 * @return
	 */
	public int getHappiness(String name);
	
	/**
	 * Signals the pokedex that a creature of species name has been seen
	 * @param name
	 */
	public void someoneSawCreature(String name);
	
	/**
	 * Signals the pokedex that you saw the creature, should only be called 
	 * if you also receive happiness points for it
	 * @param name
	 */
	public void personallySawCreature(String name, int id);

	/**
	 * Fetches the number of times the given species has been seen
	 * @param name
	 */
	public int getSeenCount(String name);

	/**
	 * Fetches the number of times the diver has personally seen this species
	 * @param name
	 * @return
	 */
	public int getPersonalSeenCount(String name);
	
	/**
	 * Fetches whether the id has been seen before.
	 * @param id of a sealife
	 * @return whether it has personally been seen before
	 */
	public boolean isPersonallySeen(int id);

	public SortedMap<Integer,String> getSpeciesRanking();

    /**
     * Return a list of all species.
     */
    public List<SeaLifePrototype> getAllSpecies();
}
