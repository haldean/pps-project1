package isnork.g2.utilities;

import isnork.g2.utilities.SeaCreatureType.IdRoundTracker;
import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ProbabilityBoard {

	//ProbabilityBoardObject[][] creatures;
//	public ArrayList<SeaCreatureType> ratedCreatures = new ArrayList<SeaCreatureType>();
	int radius = 0;
	Point2D boat;
	
	public ProbabilityBoard(int r, Point2D b)
	{
		//creatures = new ProbabilityBoardObject[distance*2][distance*2];
//		ratedCreatures = ratedC;
		radius = r;
		boat = b;
	}
	
	public void addSeenCreature(int x, int y, int id, SeaCreatureType sct, int round, Direction d)
	{
		//ProbabilityBoardObject pbo = creatures[x][y];
		//pbo.addSeenCreature(id, name, round);
		
		sct.addSeenWithRound(id, round, x, y, d);
	}
	
	public void addSeenCreature(int x, int y, int id, SeaCreatureType sct, int round)
	{
		sct.addSeenWithRound(id, round, x, y);
	}
	
	public double getProbabilityOfNewId(Point2D whereIAm, int x, int y, SeaCreatureType sct, int curRound)
	{
		//if you haven't seen any already, it has to be a new sighting
		if(sct.seenWithRounds.size() == 0)
			return 1;
		
		//if you have seen at least 3 of that type or all possible ones
		if(sct.seen.size() >= 3 || sct.seen.size() >= sct.returnCreature().getMaxCount())
			return 0;
		
		//TOUGH CASE - if you've seen 1 or 2 already:
		double probIsNew = 0;
		ArrayList<IdRoundTracker> recentSightings = recentSightings(sct);
		for(IdRoundTracker irt : recentSightings)
		{
			int roundsToGoal = getRoundsDistance(x, y, (int)irt.loc.getX(), (int)irt.loc.getY());
			int roundsPassed = curRound - irt.myRound;
			
			//if distance to point is greater than *rounds* to get there, it must be new
			if(roundsPassed + radius*2 < roundsToGoal) //definitely a new id
			{
				irt.probNotMe = 1;
			}
			else if(whereIAm.distance(boat) <= radius)
			{
				irt.probNotMe = .03;
			}
			else if(roundsPassed + radius < roundsToGoal) //likely a new id
			{
				irt.probNotMe = (double)(roundsPassed+radius)/(double)roundsToGoal;
			}
			else if(roundsPassed < roundsToGoal) //radii makes it hard to tell if new id
			{
				irt.probNotMe = (double)roundsPassed/(double)roundsToGoal;
			}
			else //it's reachable by the distance + radius, so now you guess...
			{
				irt.probNotMe = (double)roundsToGoal/(double)roundsPassed;
				if(irt.probNotMe > .5)
					irt.probNotMe = .5;
			}
			
			probIsNew += irt.probNotMe;
		}
		
//		if(recentSightings.size() > 0)
//		{
//			probIsNew = probIsNew / (double)recentSightings.size();
//			return probIsNew;
//		}
		
		return 1.0;
	}
	
	private ArrayList<IdRoundTracker> recentSightings(SeaCreatureType sct)
	{
		Set<Integer> idSightings = new HashSet<Integer>();
		ArrayList<IdRoundTracker> sightings = new ArrayList<IdRoundTracker>();
		
		for(int x=sct.seenWithRounds.size()-1; x>0; x--)
		{
			IdRoundTracker irt = sct.seenWithRounds.get(x);
			if(!idSightings.contains(irt.myId))
			{
				idSightings.add(irt.myId);
				sightings.add(irt);
			}
		}
		
		return sightings;
	}
	
	/**
	 * Gets the number of rounds needed to travel from start to goal.
	 */
	private int getRoundsDistance(int x1, int y1, int x2, int y2)
	{
		int numRounds = 0;
		Point2D start = new Point2D.Double(x1, y1);
		Point2D goal = new Point2D.Double(x2, y2);
		
		while(!start.equals(goal))
		{
			Direction move = toGoal(start, goal);
			if(move == Direction.NW || move == Direction.SE || move == Direction.NE || move == Direction.SW)
				numRounds += 3;
			else
				numRounds += 2;
			
			start = new Point2D.Double(start.getX()+move.dx, start.getY()+move.dy);
		}
		
		return numRounds;
	}
	
	/**
	 * Determines which direction to go to get to the goal.
	 */
	private Direction toGoal(Point2D whereIAm, Point2D goal) {
		// in a quadrant
		if (whereIAm.getX() > goal.getX() && whereIAm.getY() > goal.getY()) {
			return Direction.NW;
		}

		if (whereIAm.getX() < goal.getX() && whereIAm.getY() < goal.getY()) {
			return Direction.SE;
		}

		if (whereIAm.getX() < goal.getX() && whereIAm.getY() > goal.getY()) {
			return Direction.NE;
		}

		if (whereIAm.getX() > goal.getX() && whereIAm.getY() < goal.getY()) {
			return Direction.SW;
		}

		// on a line
		if (whereIAm.getX() < goal.getX() && whereIAm.getY() == goal.getY()) {
			return Direction.E; 
		}
		if (whereIAm.getX() == goal.getX() && whereIAm.getY() < goal.getY()) {
			return Direction.S;
		}

		if (whereIAm.getX() == goal.getX() && whereIAm.getY() > goal.getY()) {
			return Direction.N;
		}
		if (whereIAm.getX() > goal.getX() && whereIAm.getY() == goal.getY()) {
			return Direction.W;
		}

		return null;
	}
	
	private int getSecondSighting(ArrayList<IdRoundTracker> recent, int id)
	{
		int firstRecent = -1;
		int secondRecent = -1;
		
		for(int x=recent.size()-1; x>0; x--)
		{
			IdRoundTracker irt = recent.get(x);
			if(irt.myId == id && firstRecent < 0)
			{
				firstRecent = x;
			}
			else if(irt.myId == id && secondRecent < 0)
			{
				secondRecent = x;
			}
		}
		
		return -1;
	}
}








