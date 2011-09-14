package isnork.g2.utilities;

import isnork.sim.GameObject.Direction;

public class Probability {
	
	public Probability(Direction d, double p){
		dir = d;
		prob = p;
	}
	
	public String toString(){
		return "(" + dir +", " + prob + ")";
	}

	public Direction dir;
	public double prob;
}
