package isnork.g5;

import isnork.sim.GameObject;
import isnork.sim.GameObject.Direction;
import isnork.sim.GameEngine;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Move {

	int dangerFlag = 0;
	Point2D myPosition;
	ArrayList <CreatureMove> dangerCreatureMove = new ArrayList<CreatureMove>();
	ArrayList <Direction> directions = new ArrayList<Direction>();
	ArrayList <Observation> whatyousee = new ArrayList<Observation>();

	private void initalDirections()
	{
		Direction[] direction = Direction.values();
		for (int i=0; i< direction.length; i++)
		{
			directions.add(direction[i]);
		}
	}


	public Move(Set<SeaLifePrototype> seaLifePossibilites, ArrayList<Observation> whatyousee, Point2D myPosition)
	{
		this.myPosition= myPosition;
		checkDangerous(seaLifePossibilites, whatyousee, myPosition);
		this.whatyousee = whatyousee;
		initalDirections();

	}

	public Direction randomMove(Random random)
	{
		Direction[] directions = Direction.values();
		Direction direction = directions[random.nextInt(directions.length)];
		return direction;
	}

	public void checkDangerous(Set<SeaLifePrototype> seaLifePossibilites, ArrayList<Observation> whatyousee, Point2D myPosition)
	{//TODO: haven't add the requirement that only one penalty to one kind of creature. record the dangerous creature we already get penalty.
		ArrayList <CreatureMove> temper = new ArrayList<CreatureMove>();
		for (Observation i : whatyousee)
		{

			if (i.isDangerous()&& i.getLocation().distance(myPosition)<=6)
			{
				int UpdateFlag = 0;
				dangerFlag = 1;
				for( CreatureMove a :dangerCreatureMove)
				{
					if(a.getID() == i.getId())
					{
						a.update(i.getLocation(),myPosition);
						UpdateFlag = 1;
						temper.add(a);
						break;
					}
				}
				if (UpdateFlag ==0)
				{	
					CreatureMove creature =  new CreatureMove(i.getId(),i.getName(), seaLifePossibilites, i.getLocation(), myPosition);
					temper.add(creature);	
				}

			}
		}
		dangerCreatureMove = temper;
	}
	public Direction escapeMove(Random random)
	{
		Direction d = null;
		if (dangerCreatureMove.size()!= 0)
		{
			//avoid the 6 directions
			ArrayList<Direction> a = (ArrayList<Direction>) directions.clone();
			Direction z=null;
			for (CreatureMove i : dangerCreatureMove)
			{	

				z = i.getDirectionToMe();
				GameEngine.println("dangerous direction: "+z);
				if(a.contains(z))
				{
					a.remove(i.getDirectionToMe());
					//GameEngine.println("remove dangerouse direction: "+ i.getDirectionToMe());

				}
				if (a.contains(getLeft(z)))
				{
					a.remove(getLeft(z));  //ADDED BY NEHA
					//GameEngine.println("remove left dangerouse direction: "+ getLeft(z));
				}

				if(a.contains(getLeft(getLeft(i.getDirectionToMe()))))
				{
					a.remove(getLeft(getLeft(z)));
					//GameEngine.println("remove left left dangerouse direction: "+ getLeft(getLeft(i.getDirectionToMe())));
				}
				if(a.contains(getRight(i.getDirectionToMe())))
				{
					a.remove(getRight(z)); //ADDED BY NEHA
					//GameEngine.println("remove right dangerouse direction: "+ getRight(i.getDirectionToMe()));
				}

				if (a.contains(getRight(getRight(z))))
				{
					a.remove(getRight(getRight(z)));
					//GameEngine.println("remove right right dangerouse direction: "+ getRight(getRight(z)));
				}
				if (a.contains(getOpposite(i.getDirectionToMe())))
				{
					a.remove(getOpposite(z));
					//GameEngine.println("remove opposite dangerouse direction: "+ getOpposite(i.getDirectionToMe()));
				}

				if (a.contains(i.getPossibleDirection()))
					a.remove(i.getPossibleDirection());
				//GameEngine.println("remove possible direction: "+ i.getPossibleDirection());
			}
			if (a.size() != 0)
			{
				Direction dir = a.get(random.nextInt(a.size()));
				//GameEngine.println("escape move: "+ dir);
				return dir;
			}
			//else GameEngine.println("6 directions remove way: directions size is 0.");
			// avoid five directions
			ArrayList<Direction> b = (ArrayList<Direction>) directions.clone();
			for (CreatureMove i : dangerCreatureMove)
			{	

				//GameEngine.println("come into avoid 5 directions");
				//GameEngine.println("dangerous direction: "+i.getDirectionToMe());
				//GameEngine.println(b);
				z = i.getDirectionToMe();
				if(b.contains(z))
				{
					b.remove(i.getDirectionToMe());
					//GameEngine.println("remove dangerouse direction: "+ (i.getDirectionToMe()));
				}
				if (b.contains(getLeft(z)))
				{
					b.remove(getLeft(z));  //ADDED BY NEHA
					//GameEngine.println("remove left dangerouse direction: "+ getLeft(i.getDirectionToMe()));
				}

				if(b.contains(getLeft(getLeft(i.getDirectionToMe()))))
				{
					b.remove(getLeft(getLeft(z)));
					//GameEngine.println("remove left left dangerouse direction: "+ getLeft(getLeft(i.getDirectionToMe())));

				}
				if(b.contains(getRight(i.getDirectionToMe())))
				{
					b.remove(getRight(z)); //ADDED BY NEHA
					//GameEngine.println("remove right dangerouse direction: "+ getRight(i.getDirectionToMe()));
				}

				if (b.contains(getRight(getRight(z))))
				{
					b.remove(getRight(getRight(z)));
					//GameEngine.println("remove right right dangerouse direction: "+ getRight(getRight(i.getDirectionToMe())));
				}

				if (b.contains(i.getPossibleDirection()))
					b.remove(i.getPossibleDirection());
			}
			if (b.size() != 0)
			{
				Direction dir = b.get(random.nextInt(b.size()));
				//GameEngine.println("escape move: "+ dir);
				return dir;
			}
			//else {GameEngine.println("remove 5 directions way: directions size is 0.");}
			//avoid three directions
			ArrayList<Direction> c = (ArrayList<Direction>) directions.clone();
			for (CreatureMove i : dangerCreatureMove)
			{	
				//GameEngine.println("come into avoid 3 directions");
				z = i.getDirectionToMe();
				if(c.contains(z))
				{
					c.remove(i.getDirectionToMe());
					//GameEngine.println("remove dangerouse direction: "+ i.getDirectionToMe());
				}
				if (c.contains(getLeft(z)))
				{
					c.remove(getLeft(z));  //ADDED BY NEHA
					//GameEngine.println("remove left dangerouse direction: "+ getLeft(i.getDirectionToMe()));
				}


				if(c.contains(getRight(i.getDirectionToMe())))
				{
					c.remove(getRight(z)); //ADDED BY NEHA
					//GameEngine.println("remove right dangerouse direction: "+ getRight(i.getDirectionToMe()));
				}



				if (c.contains(i.getPossibleDirection()))
					c.remove(i.getPossibleDirection());
			}
			if (c.size() != 0)
			{
				Direction dir = c.get(random.nextInt(c.size()));
				//GameEngine.println("escape move: "+ dir);
				return dir;
			}
			else {GameEngine.println("remove 3 directions way: directions size is 0.");}
			//avoid 1 direction
			ArrayList<Direction> e = (ArrayList<Direction>) directions.clone();
			for (CreatureMove i : dangerCreatureMove)
			{	
				//GameEngine.println("come into avoid 1 directions");
				z = i.getDirectionToMe();
				if(e.contains(z))
				{
					e.remove(i.getDirectionToMe());
					//GameEngine.println("remove dangerouse direction: "+ i.getDirectionToMe());
				}

			}
			if (e.size() != 0)
			{
				Direction dir = e.get(random.nextInt(e.size()));
				//GameEngine.println("escape move: "+ dir);
				return dir;
			}
			else {GameEngine.println("remove 1 directions way: directions size is 0.");}
			// go to the direction which penalty is least.
			int penaltyScore = StaticIsnork.maxDangerouse;
			for (CreatureMove i : dangerCreatureMove)
			{
				for (Observation x: this.whatyousee)
				{if (x.getId()== i.getID())
				{
					int penalty = 2 * x.happiness();
					if(penalty < penaltyScore) 
					{
						penaltyScore = penalty;
						d = i.getDirectionToMe();
					}
					break;
				}
				}

			}
			if(d != null)
			{
				return d;
			}
			//else GameEngine.println("to the least dangerous method is useless.");

		}
		else {return randomMove(random);}
		return d;
	}

	public Direction stayMove(Random random,Direction d)
	{
		Direction direction =null;
		if (d != null)
		{
			direction = towardsBoat();
			return direction;
		}	
				
		if(dangerFlag==1) 
			{
				direction= null;
			}
		else 
		{
			Direction[] dir = Direction.values();
			direction= dir[random.nextInt(dir.length)];
		}
		return direction;
	}
	
	public Direction towardsBoat() {

		Direction direction = null;

		if (myPosition.getY() == StaticIsnork.locationOfBoat.getY()) {
			if (myPosition.getX() < StaticIsnork.locationOfBoat.getX())
				direction = Direction.E;
			else if (myPosition.getX() > StaticIsnork.locationOfBoat.getX())
				direction = Direction.W;

		}

		if (myPosition.getX() == StaticIsnork.locationOfBoat.getX()) {
			if (myPosition.getY() < StaticIsnork.locationOfBoat.getY())
				direction = Direction.S;
			else if (myPosition.getY() > StaticIsnork.locationOfBoat.getY())
				direction = Direction.N;
		}

		if (myPosition.getY() < StaticIsnork.locationOfBoat.getY()
				&& myPosition.getX() < StaticIsnork.locationOfBoat.getX())
			direction = Direction.SE;
		else if (myPosition.getY() > StaticIsnork.locationOfBoat.getY()
				&& myPosition.getX() < StaticIsnork.locationOfBoat.getX())
			direction = Direction.NE;
		else if (myPosition.getY() < StaticIsnork.locationOfBoat.getY()
				&& myPosition.getX() > StaticIsnork.locationOfBoat.getX())
			direction = Direction.SW;
		else if (myPosition.getY() > StaticIsnork.locationOfBoat.getY()
				&& myPosition.getX() > StaticIsnork.locationOfBoat.getX())
			direction = Direction.NW;


		return direction;

	}
	public Direction getRight(Direction d)
	{
		Direction right = null;
		switch(d)
		{
		case E:
		{
			right= GameObject.Direction.SE;
			break;
		}


		case SE:
		{
			right= GameObject.Direction.S;
			break;
		}	
		case S:
		{
			right= GameObject.Direction.SW;
			break;
		}
		case SW:
		{
			right= GameObject.Direction.W;
			break;
		}	
		case W:
		{
			right= GameObject.Direction.NW;
			break;
		}	
		case NW:
		{
			right= GameObject.Direction.N;
			break;
		}

		case N:
		{
			right= GameObject.Direction.NE;
			break;
		}	
		case NE:
		{
			right= GameObject.Direction.E;
			break;
		}	
		}
		return right;
	}

	public Direction getLeft(Direction d)
	{
		Direction left = null;
		switch(d)
		{
		case S:
		{
			left= GameObject.Direction.SE;
			break;
		}
		case SE:
		{
			left= GameObject.Direction.E;
			break;
		}	
		case E:
		{
			left= GameObject.Direction.NE;
			break;
		}
		case NE:
		{
			left= GameObject.Direction.N;
			break;
		}

		case N:
		{
			left= GameObject.Direction.NW;
			break;
		}

		case NW:
		{
			left= GameObject.Direction.W;
			break;
		}
		case W:
		{
			left= GameObject.Direction.SW;
			break;
		}
		case SW:
		{
			left= GameObject.Direction.S;
			break;
		}
		}
		return left;
	}

	public Direction getOpposite(Direction d)
	{
		Direction opp= null;
		switch(d)
		{
		case S:
		{
			opp= GameObject.Direction.N;
			break;
		}
		case N:
		{
			opp= GameObject.Direction.S;
			break;
		}

		case E:
		{
			opp= GameObject.Direction.W;
			break;
		}
		case W:
		{
			opp= GameObject.Direction.E;
			break;
		}
		case NE:
		{
			opp= GameObject.Direction.SW;
			break;
		}

		case SW:
		{
			opp= GameObject.Direction.NE;
			break;
		}
		case NW:
		{
			opp= GameObject.Direction.SE;
			break;
		}
		case SE:
		{
			opp= GameObject.Direction.NW;
			break;
		}

		}
		return opp;
	}
}

