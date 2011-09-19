package isnork.g3;

import isnork.sim.SeaLifePrototype;

/**
 * Builder for SeaLifePrototype, to instantiate hit create
 * Default settings are happiness 1, mincount 1, maxcount 1, 
 * dangerous false, stationary.
 * Name field MUST be set.  If you try to create while you have
 * a trivial name, you will get an IllegalStateException in your face
 * @author Moses
 *
 */

public class SeaLifePrototypeBuilder {

	private SeaLifePrototype prototype;
	private String name;
	private int happiness;
	private int minCount;
	private boolean dangerous;
	private int maxCount;
	private boolean moving;

	public SeaLifePrototypeBuilder(){
		happiness = 1;
		minCount = 1;
		maxCount = 1;
		dangerous = false;
		moving = false;
		name = "";
	}
	
	public SeaLifePrototypeBuilder(String name){
		happiness = 1;
		minCount = 1;
		maxCount = 1;
		dangerous = false;
		this.name = name;
	}
	
	public SeaLifePrototypeBuilder dangerous(boolean dangerous){
		this.dangerous = dangerous;
		return this;
	}

	public SeaLifePrototypeBuilder moving(boolean moving){
		this.moving = moving;
		return this;
	}
	
	public SeaLifePrototypeBuilder maxCount(int maxCount){
		this.maxCount = maxCount;
		return this;
	}
	
	public SeaLifePrototypeBuilder minCount(int minCount){
		this.minCount = minCount;
		return this;
	}

	public SeaLifePrototypeBuilder happiness(int happiness){
		this.happiness = happiness;
		return this;
	}

	public SeaLifePrototypeBuilder name(String name){
		this.name = name;
		return this;
	}
	
	public SeaLifePrototype create(){
		if (name == ""){
			throw new IllegalStateException("You must have a non-trivial name.");
		}
		prototype = new SeaLifePrototype();
		prototype.setDangerous(dangerous);
		prototype.setHappiness(happiness);
		prototype.setMaxCount(maxCount);
		prototype.setMinCount(minCount);
		prototype.setName(name);
		if (moving){
			prototype.setSpeed(1);
		}
		else{
			prototype.setSpeed(0);
		}
		return prototype;
	}
}
