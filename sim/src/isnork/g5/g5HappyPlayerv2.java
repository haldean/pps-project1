package isnork.g5;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import isnork.sim.GameObject.Direction;
import isnork.sim.GameEngine;
import isnork.sim.GameObject;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;

public class g5HappyPlayerv2 extends Player {

	private Object myCopyOfSnorkelerLocations;
	private Point2D whereIAm;
	ArrayList <Observation> whatYousee = new ArrayList<Observation>();
	int elapsedTImeInMinutes = 0;
	private int id;
	int n = -1;
	Set<SeaLifePrototype> seaLifePossibilites;
	ArrayList <Observation> whatYouSee = new ArrayList<Observation>();
	ArrayList<Direction> dangerDirections= new ArrayList<Direction>();
	ArrayList<Direction> myDirections=new ArrayList<Direction>();
	
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "G5 Happy Player 2";
	}

	@Override
	public void newGame(Set<SeaLifePrototype> seaLifePossibilites, int penalty,
			int d, int r, int n) {
		// TODO Auto-generated method stub
		this.seaLifePossibilites = seaLifePossibilites;
	}

	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,
			Set<Observation> playerLocations) {
		// TODO Auto-generated method stub
		myCopyOfSnorkelerLocations = playerLocations;
		Direction temp=null;
		whereIAm = myPosition;
		this.whatYousee.clear();
		this.dangerDirections.clear();
		for (Observation i: whatYouSee)
			{
				GameEngine.println(i.getName()+"::::::::"+i.getId());
				this.whatYousee.add(i);
			}
		for(Observation i:	whatYousee)
		{
			temp=checkForDangerousCreatures(whereIAm, i);			//Returns the direction of the dangerous creature, null if the creature is not dangerous
			if(temp!=null)
				{
				Direction left=this.getLeft(temp);
				Direction right=this.getRight(temp);
				Direction opp=this.getOpposite(temp);
				dangerDirections.add(temp);
				if(left!=null)
					{
					dangerDirections.add(left);
					dangerDirections.add(this.getLeft(left));
					}
				if(right!=null)
					{
					dangerDirections.add(right);
					dangerDirections.add(this.getRight(right));
					}
				if(opp!=null)
					{
					dangerDirections.add(opp);
					
					}
				}
		}
		//get player id at the first round.
		if(elapsedTImeInMinutes ==0)
		{
		for(Observation i: playerLocations)
		{
			if (i.getLocation() == whereIAm)
			{
				id=i.getId();
				break;
			}
		}
		}
		elapsedTImeInMinutes ++;
		
		n++;
		return "s";
	}

	@Override
	public Direction getMove() {
		
		Random r=new Random();
		for(Direction d:	Direction.values())
			{
			if(!myDirections.contains(d))	myDirections.add(d);
			}
		if(!dangerDirections.isEmpty()){
		for(Direction t:	dangerDirections)
			{if(myDirections.contains(t))	myDirections.remove(t);}
		}
		//for(Direction d:	myDirections)
			//GameEngine.println("Possible direction"+d);
		
		return randomMove(r);
	}
	public Direction checkForDangerousCreatures(Point2D whereIAm, Observation creature) {
		
		Direction d=null;
		if(creature.isDangerous())
		{
			d= this.countDirection(whereIAm, creature.getLocation());
		}
		return d;
	}
	private Direction countDirection(Point2D creature, Point2D player)
	{
		//-22.5~22.5 belongs to E, 22.5~67.5 belongs to NE, 67.5~112.5 belongs to N, 112.5~157.5 belongs to NW, 157.5~-157.5 belongs to W
		double dx = creature.getX()-player.getX();
		double dy = creature.getY()-player.getY();
		Direction d = null;
		if(dx ==0 && dy>0) return Direction.N;
		else if (dx ==0 && dy < 0) return Direction.S;
		else if (dy == 0 && dx > 0) return Direction.E;
		else if (dy ==0 && dx < 0) return Direction.W;
		else
		{
			if (dy/dx>=-0.4142 && dy/dx <=0.4142)
			{
				if (dx>0) return Direction.E;
				else if (dx < 0) return Direction.W;
			}
			else if(dy/dx >=0.4142 && dy/dx<= 2.4142)
			{
				if (dx>0) return Direction.NE;
				else if (dx < 0) return Direction.SW;
			}
			else if(dy/dx >=2.4142 || dy/dx<= -2.4142)
			{
				if (dy>0) return Direction.N;
				else if (dy < 0) return Direction.S;
			}
			else if(dy/dx >=-2.4142 && dy/dx<= -0.4142)
			{
				if (dx>0) return Direction.SE;
				else if (dx < 0) return Direction.NW;
			}
		}
		return d;
	}
	
	public Direction getLeft(Direction d)
	{
		Direction left = null;
		switch(d)
		{
		case E:
			left= GameObject.Direction.SE;
		
		case SE:
			left= GameObject.Direction.S;
			
		case S:
			left= GameObject.Direction.SW;
		
		case SW:
			left= GameObject.Direction.W;
			
		case W:
			left= GameObject.Direction.NW;
			
		case NW:
			left= GameObject.Direction.N;
		
		case N:
			left= GameObject.Direction.NE;
			
		case NE:
			left= GameObject.Direction.E;
			
		}
		return left;
	}
	
	public Direction getRight(Direction d)
	{
		Direction right = null;
		switch(d)
		{
		case S:
			right= GameObject.Direction.SE;
		
		case SE:
			right= GameObject.Direction.E;
			
		case E:
			right= GameObject.Direction.NE;
		
		case NE:
			right= GameObject.Direction.N;
			
		case N:
			right= GameObject.Direction.NW;
			
		case NW:
			right= GameObject.Direction.W;
		
		case W:
			right= GameObject.Direction.SW;
			
		case SW:
			right= GameObject.Direction.S;
			
		}
		return right;
	}
	public Direction getOpposite(Direction d)
	{
		Direction right = null;
		switch(d)
		{
		case S:
			right= GameObject.Direction.N;
		
		case N:
			right= GameObject.Direction.S;
			
		case E:
			right= GameObject.Direction.W;
		
		case W:
			right= GameObject.Direction.E;
			
		case NE:
			right= GameObject.Direction.SW;
			
		case SW:
			right= GameObject.Direction.NE;
		
		case NW:
			right= GameObject.Direction.SE;
			
		case SE:
			right= GameObject.Direction.NW;
			
		}
		return right;
	}
	public Direction randomMove(Random random)
	{
		Direction directions[]=new Direction[8];
		int count=0;
		for(Direction d:	this.myDirections)
		{
			//GameEngine.println("Possible Directions_2"+d);
			directions[count]=d;
			count++;
		}
		
		/*for(Direction temp:		directions)
				GameEngine.println("directions as passed::::::"+temp);*/
		Direction direction = directions[random.nextInt(directions.length)];
		//GameEngine.println("directions returned::::::"+direction);
		return direction;
	}

}
