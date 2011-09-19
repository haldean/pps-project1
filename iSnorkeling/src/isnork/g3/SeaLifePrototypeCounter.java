package isnork.g3;

import isnork.sim.SeaLifePrototype;

/**
 * 
 * @author Moses Nakamura
 * Prefer composition to inheritance blah blah blah
 */

public class SeaLifePrototypeCounter {
	private int count;
	private SeaLifePrototype delegate;
	private int captured;
	
	public SeaLifePrototypeCounter(SeaLifePrototype prototype){
		this.delegate = prototype;
		this.count = 0;
		this.captured = 0;
	}
	
	/**
	 * Should be called whenever a creature is seen
	 */
	public void sawCreature(){
		count++;
	}
	
	/**
	 * Fetches the number of times a given species has been seen
	 */
	public int getSeenCount(){
		return count;
	}
	
	/**
	 * Fetches the actual prototype
	 * @return
	 */
	public SeaLifePrototype get(){
		return delegate;
	}

	/**
	 * Whether the species is dangerous
	 * @return
	 */
	public boolean isDangerous() {
		return delegate.isDangerous();
	}

	/**
	 * Mincount on the species
	 * @return
	 */
	public int getMinCount() {
		return delegate.getMinCount();
	}

	/**
	 * Maxcount on the species
	 * @return
	 */
	public int getMaxCount() {
		return delegate.getMaxCount();
	}

	/**
	 * The happiness of the species
	 * @return
	 */
	public int getHappiness() {
		return delegate.getHappiness();
	}

	/**
	 * The number of creatures you have gotten happiness from
	 */
	public void captured() {
		this.captured++;
	}
	
	/**
	 * The number of creatures you have gotten happiness from
	 * @return 
	 */
	public int getCaptured() {
		return captured;
	}

	public boolean isMoving() {
		return delegate.getSpeed() > 0;
	}
}
