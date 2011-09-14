package isnork.g2.utilities;

import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;

public class DirectionToSort implements Comparable{

	public Direction dir;
	public Point2D goal;
	public Point2D whereIAm;
	
	public DirectionToSort(Direction d, Point2D g, Point2D w) {
		dir = d;
		goal = g;
		whereIAm = w;
	}

	public int compareTo(Object o) {
		
		
		if(goingTo().distance(goal) < ((DirectionToSort) o).goingTo().distance(goal))
			return -1;
		
		if(goingTo().distance(goal) > ((DirectionToSort) o).goingTo().distance(goal))
			return 1;
		
		return 0;
	}
	
	public Point2D goingTo(){
		return new Point2D.Double(whereIAm.getX() + dir.dx, whereIAm.getY() + dir.dy);
	}
	
	public String toString(){
		return dir + " " + goingTo().distance(goal);
	}

}
