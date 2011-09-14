package isnork.g7;

import isnork.sim.GameObject.Direction;

public class DirectionAndDanger implements Comparable<DirectionAndDanger> {
	private Direction direction;
	private double danger;  // equals happiness * dangerMultiplier
	
	public DirectionAndDanger(Direction direction, double danger) {
		this.direction = direction;
		this.danger = danger;
	}
	
	public int compareTo(DirectionAndDanger other) {
		if (this.danger < other.danger)
			return -1;
		else if (this.danger > other.danger)
			return 1;
		else
			return 0;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public double getDanger() {
		return danger;
	}

	public void setDanger(double danger) {
		this.danger = danger;
	}
	
	
}
