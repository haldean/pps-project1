package isnork.g5;

import java.awt.geom.Point2D;
import java.util.Set;

import isnork.sim.GameObject.Direction;
import isnork.sim.SeaLifePrototype;

public class CreatureMove {
	private int id;
	private double distanceToMe;
	private Direction PossibleDirection = null;
	private int speed = 0;// 0 is static, else is moving
	private Point2D position;
	private Point2D formerPosition;
	private Direction DirectionToMe;

	
	public CreatureMove(int creatureId, String creatureName, Set<SeaLifePrototype> seaLifePossibilites, Point2D creature, Point2D Player)
	{
		this. id = creatureId;
		this. position = creature;
		this. distanceToMe = position.distance(Player);
		this.DirectionToMe = countDirection(this.position, Player);

		for (SeaLifePrototype a :seaLifePossibilites)
		{
			if (a.getName().equals(creatureName))
				{
					this.speed  = a.getSpeed();
					if(this.speed ==0) this.PossibleDirection= null;
					break;
				}
		}
	}
	
	public void update(Point2D currentPosition, Point2D Player)
	{
		this.formerPosition = this.position;
		this.position = currentPosition;
		this.distanceToMe= position.distance(Player);
		this.DirectionToMe = countDirection(this.position, Player);
		if (this.speed ==0) return;
		else if(currentPosition != this.position)
		{
			formerPosition = position;
			this.position = currentPosition;
			//set direction
			int x = (int)(position.getX()-formerPosition.getX());
			int y = (int)(position.getY()-formerPosition.getY());
			for (Direction i: Direction.values())
			{
				if (i.dx == x && i.dy ==y)
					{
						PossibleDirection =i;
						break;
					}
			}
		}
	}
	
	private Direction countDirection(Point2D creature, Point2D player)
	{
		//-22.5~22.5 belongs to E, 22.5~67.5 belongs to NE, 67.5~112.5 belongs to N, 112.5~157.5 belongs to NW, 
		//157.5~-157.5 belongs to W, -157.5~-112.5 belongs to SW, -112.5~-67.5 belongs to S, -67.5~-22.5 belongs to SE
		double dx = creature.getX()-player.getX();
		double dy = creature.getY()-player.getY();
		
		Direction d = null;
		if(dx ==0 && dy>0) return Direction.S;
		else if (dx ==0 && dy <= 0) return Direction.N;
		else
		{
			if (dy/dx>=-0.4142 && dy/dx <=0.4142)
			{
				if (dx>= 0) return Direction.E;
				else if (dx <= 0) return Direction.W;
			}
			else if(dy/dx >=0.4142 && dy/dx<= 2.4142)
			{
				if (dx>= 0) return Direction.SE;
				else if (dx <= 0) return Direction.NW;
			}
			else if(dy/dx >=2.4142 || dy/dx<= -2.4142)
			{
				if (dy>=0) return Direction.S;
				else if (dy <= 0) return Direction.N;
			}
			else if(dy/dx >=-2.4142 && dy/dx<= -0.4142)
			{
				if (dx>= 0) return Direction.NE;
				else if (dx <= 0) return Direction.SW;
			}
		}
		
		return d;
	}

	public int getID()
	{
		return this.id;
	}
	
	public double getDistanceToMe()
	{
		return this.distanceToMe;
	}
	
	public Direction getPossibleDirection()
	{
		return this.PossibleDirection;
	}
	
	public int getSpeed()
	{
		return this.speed;
	}
	
	public Point2D getPosition()
	{
		return this.position;
	}
	
	public Direction getDirectionToMe()
	{
		return this.DirectionToMe;
	}
}
