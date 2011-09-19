package isnork.g3;

import isnork.sim.GameObject.Direction;

public class DirectionValue{
	public DirectionValue(Direction dir, double dub) {
		super();
		this.dir = dir;
		this.dub = dub;
	}
	
	public Direction getDir() {
		return dir;
	}
	public double getDub() {
		return dub;
	}

	Direction dir;
	double dub;
}
