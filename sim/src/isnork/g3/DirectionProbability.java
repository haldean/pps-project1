package isnork.g3;

import java.awt.geom.Point2D;

import isnork.sim.GameObject.Direction;

public class DirectionProbability {
	private Direction direction;
	private double prob;
	private Point2D location;
	private int numTicksAtThisLocation;
	private boolean finishedMove;
	
	public DirectionProbability(Direction direction, Point2D location, double prob, int numTicksAtThisLocation, boolean finishedMove)
	{
		this.direction = direction;
		this.location = location;
		this.prob = prob;
		this.numTicksAtThisLocation = numTicksAtThisLocation;
		this.finishedMove = finishedMove;
	}
	
	public Direction getDirection()
	{
		return direction;
	}

	public Point2D getLocation()
	{
		return location;
	}
	
	public double getProb()
	{
		return prob;
	}
	
	public int getNumTicksAtThisLocation()
	{
		return numTicksAtThisLocation;
	}
	
	public boolean didFinishMove()
	{
		return finishedMove;
	}

}
