package isnork.g3;

import java.awt.geom.Point2D;
import java.security.KeyStore.Builder;
import java.util.ArrayList;

import isnork.sim.GameObject.Direction;

public class DangerousCreature {
	private String name;
	private int id;
	private Direction dir;
	private Point2D location;
	private Point2D prevLocation;
	private Point2D locationTwoTurnsAgo;
	private int dangerScore;
	private boolean isFresh;
	private int numTicksOnSameLocation;
	
	public DangerousCreature(String name, int id, Point2D location, Direction dir, int dangerScore)
	{
		this.name = name;
		this.id = id;
		this.location = location;
		this.dir = dir;
		this.dangerScore = dangerScore; //should be negative
		this.isFresh = true;
		numTicksOnSameLocation = 1; //assume it's been 1 to be safe 
		if(this.dangerScore > 0)
		{
			////GameEngine.println("___ERROR: DANGEROUS CREATURE SHOULD HAVE NEGATIVE SCORE");
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setNotFresh()
	{
		this.isFresh = false;
	}
	
	public boolean isFresh()
	{
		return isFresh;
	}
	
	public void updateLocation(Point2D location, Direction dir)
	{
		locationTwoTurnsAgo = prevLocation;
		prevLocation = this.location;
		this.location = location;
		this.dir = dir;
		this.isFresh = true;
		
		if(location.equals(prevLocation))
		{
			if(prevLocation.equals(locationTwoTurnsAgo))
				numTicksOnSameLocation = 2;
			else
				numTicksOnSameLocation = 1;
		}
		else
			numTicksOnSameLocation = 0;
	}
	
	public int getNumTicksOnSameLocation()
	{
		return numTicksOnSameLocation;
	}
	
	public int getId()
	{
		return id;
	}
	
	public double getPenalty(Point2D whereIAm, Direction d, boolean playSafely, int tickCount)
	{
		if(d == null && whereIAm.getX() == 0 && whereIAm.getY() == 0)
		{
			return 0;
		}
		if(d != null && whereIAm.getX() + d.getDx() == 0 && whereIAm.getY() + d.getDy() == 0) //The (0,0) spot is safe b/c the boat is there!
		{
			return 0;
		}
		if(dir == null && tickCount > 3) //creature doesn't move
		{
			Point2D whereIdBe;
			if(d == null)
				whereIdBe = whereIAm;
			else
				whereIdBe = new Point2D.Double(whereIAm.getX() + d.getDx(), whereIAm.getY() + d.getDy());
			////GameEngine.println("___Creature doesn't move");
			double penalty;
			if(whereIdBe.distance(location) <= 1.5)
			{
				//GameEngine.println("the danger score due to " + this.name + " with id " + this.id + " at location " + this.location + " is: " + 2*dangerScore);
				penalty = 2*dangerScore; //*2 b/c u'll be there for two ticks (probably - you could be there for three)
			}
			else
			{
				//GameEngine.println("the danger score due to " + this.name + " with id " + this.id + " at location " + this.location + " is: 0");
				penalty = 0;
			}
			
			if(whereIAm.distance(location) <= 1.5 && !(whereIAm.getX() == 0 && whereIAm.getY() == 0)) //if I am currently right next to a dangerous creature (and not on the center location) 
			{
				if(d != null && !manhattanDirection(d))
				{
					penalty += dangerScore; //If you go diagonally, you'll get the danger score for one more tick. 
				}
			}
			//GameEngine.println("The penalty for the direction " + d + " (creatures don't move) is" + penalty);
			return penalty;
		}
		else //creature moves
		{
			ArrayList<DirectionProbability> whereCreatureMayBeIn2Ticks = whereCreatureMayBeInTwoTicks(location, dir, numTicksOnSameLocation);
			if(d != null && !manhattanDirection(d))
			{
				//Pre Tick
				double totalPenalty = 0.0;
				for(int i = 0; i < whereCreatureMayBeIn2Ticks.size(); i++)
				{
					Point2D whereIdBeInTwoTicks = whereIAm;
					DirectionProbability possibleDirectionDangCreatCanMove = whereCreatureMayBeIn2Ticks.get(i);
					Point2D whereDangCreatWouldBeInTwoTicks = possibleDirectionDangCreatCanMove.getLocation();
					double prob = possibleDirectionDangCreatCanMove.getProb();
					double preTickPenaltyInThisScenario = getIndividualTickPenalty(whereIdBeInTwoTicks, whereDangCreatWouldBeInTwoTicks, prob, playSafely);
					
					
					
					//First tick
					double firstAndSecondTickPenaltyInThisScenario = 0.0;
					ArrayList<DirectionProbability> whereCreatureMayBeIn3Ticks = whereCreatureMayBeOneTickLater(possibleDirectionDangCreatCanMove);
					for(int j = 0; j < whereCreatureMayBeIn3Ticks.size(); j++)
					{
						Point2D whereIdBeInThreeTicks = new Point2D.Double(whereIAm.getX() + d.getDx(), whereIAm.getY() + d.getDy());
						DirectionProbability possibleNextDirectionForDangCreat = whereCreatureMayBeIn3Ticks.get(j);
						Point2D whereDangCreatWouldBeInThreeTicks = possibleNextDirectionForDangCreat.getLocation();
						double nextProb = possibleNextDirectionForDangCreat.getProb();
						firstAndSecondTickPenaltyInThisScenario += prob*getIndividualTickPenalty(whereIdBeInThreeTicks, whereDangCreatWouldBeInThreeTicks, nextProb, playSafely);
						
						//Second tick
						ArrayList<DirectionProbability> whereCreatureMayBeIn4Ticks = whereCreatureMayBeOneTickLater(possibleNextDirectionForDangCreat);
						for(int w = 0; w < whereCreatureMayBeIn4Ticks.size(); w++)
						{
							Point2D whereIdBeInFourTicks = new Point2D.Double(whereIAm.getX() + d.getDx(), whereIAm.getY() + d.getDy());
							DirectionProbability possibleNextNextDirectionForDangCreat = whereCreatureMayBeIn4Ticks.get(w);
							Point2D whereDangCreatWouldBeInFourTicks = possibleNextNextDirectionForDangCreat.getLocation();
							double nextNextProb = possibleNextNextDirectionForDangCreat.getProb();
							firstAndSecondTickPenaltyInThisScenario += prob*nextProb*getIndividualTickPenalty(whereIdBeInFourTicks, whereDangCreatWouldBeInFourTicks, nextNextProb, playSafely);
							
						}
					}
					double totalPenaltyForThisDCDirection = preTickPenaltyInThisScenario + firstAndSecondTickPenaltyInThisScenario; 
					totalPenalty += totalPenaltyForThisDCDirection;
				}
				return totalPenalty;
			}
			else
			{
				////GameEngine.println("___\n\n\nCalculating Penalty due to " + this.name + ", id: " + this.id + " if I go: " + d);
				////GameEngine.println("___The dangerous creature has been in the same location for " + numTicksOnSameLocation + " ticks");
				double totalPenalty = 0.0;
				//First tick
				for(int i = 0; i < whereCreatureMayBeIn2Ticks.size(); i++)
				{
					Point2D whereIdBeInTwoTicks;
					if( d == null)
						whereIdBeInTwoTicks = whereIAm;
					else
						whereIdBeInTwoTicks = new Point2D.Double(whereIAm.getX() + d.getDx(), whereIAm.getY() + d.getDy());
					DirectionProbability possibleDirectionDangCreatCanMove = whereCreatureMayBeIn2Ticks.get(i);
					Point2D whereDangCreatWouldBeInTwoTicks = possibleDirectionDangCreatCanMove.getLocation();
					double prob = possibleDirectionDangCreatCanMove.getProb();
					double firstTickPenaltyInThisScenario = getIndividualTickPenalty(whereIdBeInTwoTicks, whereDangCreatWouldBeInTwoTicks, prob, playSafely);
					
					////GameEngine.println("___1st tick: Dangerous creature might be at " + possibleDirectionDangCreatCanMove.getLocation() + " with prob " + prob + " for dang penalty of: " + firstTickPenaltyInThisScenario);
					
					//Second tick
					double secondTickPenaltyInThisScenario = 0.0;
					ArrayList<DirectionProbability> whereCreatureMayBeIn3Ticks = whereCreatureMayBeOneTickLater(possibleDirectionDangCreatCanMove);
					for(int j = 0; j < whereCreatureMayBeIn3Ticks.size(); j++)
					{
						Point2D whereIdBeInThreeTicks;
						if(d == null)
							whereIdBeInThreeTicks = whereIAm;
						else
							whereIdBeInThreeTicks = new Point2D.Double(whereIAm.getX() + d.getDx(), whereIAm.getY() + d.getDy());
						DirectionProbability possibleNextDirectionForDangCreat = whereCreatureMayBeIn3Ticks.get(j);
						Point2D whereDangCreatWouldBeInThreeTicks = possibleNextDirectionForDangCreat.getLocation();
						double nextProb = possibleNextDirectionForDangCreat.getProb();
						secondTickPenaltyInThisScenario += prob*getIndividualTickPenalty(whereIdBeInThreeTicks, whereDangCreatWouldBeInThreeTicks, nextProb, playSafely);
						
						////GameEngine.println("___   2nd tick: and then at " + possibleNextDirectionForDangCreat.getLocation() + " with nextprob " + nextProb + " ( " + nextProb*prob + " overall) for dang penalty of: " + prob*getIndividualTickPenalty(whereIdBeInThreeTicks, whereDangCreatWouldBeInThreeTicks, nextProb));
					}
					double totalPenaltyForThisDCDirection = firstTickPenaltyInThisScenario + secondTickPenaltyInThisScenario;
					totalPenalty += totalPenaltyForThisDCDirection;
					
					////GameEngine.println("___First Tick Penalty: " + firstTickPenaltyInThisScenario + ". Second Tick Penalty on average: " + secondTickPenaltyInThisScenario);
					////GameEngine.println("___**********");
				}
				////GameEngine.println("___The total penalty from moving " + d + " is" + totalPenalty);
				return totalPenalty;
			}
		}
	}
	
	
	private double getIndividualTickPenalty(Point2D whereIdBe, Point2D whereDangCreatWouldBe, double prob, boolean playSafely)
	{
		if(whereIdBe.getX() == 0 && whereIdBe.getY() == 0)
			return 0;
		double distance = whereIdBe.distance(whereDangCreatWouldBe);
		if(playSafely)
		{
			if(distance <= 5)
				return dangerScore*prob*Math.pow(.01, Math.max(0, distance - 1.5));
			else
				return 0;
		}
		else
		{
			if(distance <= 1.5)
				return dangerScore*prob;
			else
				return 0;
		}
	}
	
	
	private boolean manhattanDirection(Direction d)
	{
		if(d!= null && (d.equals(Direction.N) || d.equals(Direction.S) || d.equals(Direction.W) || d.equals(Direction.E)))
			return true;
		else
			return false;
	}
	
	
	private ArrayList<DirectionProbability> whereCreatureMayBeOneTickLater(DirectionProbability myDP)
	{
		ArrayList<DirectionProbability> d = new ArrayList<DirectionProbability>();
		
		if(myDP.getNumTicksAtThisLocation() == 2)
		{
			if(myDP.didFinishMove() == false)
			{
				Point2D currentLocation = myDP.getLocation();
				Direction currentDirection = myDP.getDirection();
				Point2D newLocation = new Point2D.Double(currentLocation.getX() + currentDirection.getDx(), currentLocation.getY() + currentDirection.getDy());
				d.add(new DirectionProbability(currentDirection, newLocation, 1, 0, true));//null b/c we won't look far enough into the future for the direction to be useful
			}
			else
			{
				////GameEngine.println("___ERROR: FORGOT TO HANDLE THIS CASE!"); 
			}
		}
		else if(myDP.getNumTicksAtThisLocation() == 0)
		{	
			if(myDP.didFinishMove()) //No pending moves 
			{
				Direction prevDir = myDP.getDirection();
				
				if(prevDir.equals(Direction.E))
					d.add(new DirectionProbability(Direction.E, myDP.getLocation(), .79, 1, false));
				else
					d.add(new DirectionProbability(Direction.E, myDP.getLocation(), .03, 1, false));
				
				if(prevDir.equals(Direction.NE))
					d.add(new DirectionProbability(Direction.NE, myDP.getLocation(), .79, 1, false));
				else
					d.add(new DirectionProbability(Direction.NE, myDP.getLocation(), .03, 1, false));
				
				if(prevDir.equals(Direction.N))
					d.add(new DirectionProbability(Direction.N, myDP.getLocation(), .79, 1, false));
				else
					d.add(new DirectionProbability(Direction.N, myDP.getLocation(), .03, 1, false));
				
				if(prevDir.equals(Direction.NW))
					d.add(new DirectionProbability(Direction.NW, myDP.getLocation(), .79, 1, false));
				else
					d.add(new DirectionProbability(Direction.NW, myDP.getLocation(), .03, 1, false));
				
				if(prevDir.equals(Direction.W))
					d.add(new DirectionProbability(Direction.W, myDP.getLocation(), .79, 1, false));
				else
					d.add(new DirectionProbability(Direction.W, myDP.getLocation(), .03, 1, false));
				
				if(prevDir.equals(Direction.SW))
					d.add(new DirectionProbability(Direction.SW, myDP.getLocation(), .79, 1, false));
				else
					d.add(new DirectionProbability(Direction.SW, myDP.getLocation(), .03, 1, false));
				
				if(prevDir.equals(Direction.S))
					d.add(new DirectionProbability(Direction.S, myDP.getLocation(), .79, 1, false));
				else
					d.add(new DirectionProbability(Direction.S, myDP.getLocation(), .03, 1, false));
				
				if(prevDir.equals(Direction.SE))
					d.add(new DirectionProbability(Direction.SE, myDP.getLocation(), .79, 1, false));
				else
					d.add(new DirectionProbability(Direction.SE, myDP.getLocation(), .03, 1, false));
			}
			else
			{
				////GameEngine.println("___ERROR: SHOULD NOT HAVE ANY MOVE PENDING IF 0 TICKS");
			}
		}
		else if(myDP.getNumTicksAtThisLocation() == 1)
		{	
			if(myDP.didFinishMove() == false) //there is still a pending move
			{
				Direction pendingDir = myDP.getDirection();
				Point2D currentPos = myDP.getLocation();
				if(manhattanDirection(pendingDir))
				{
					Point2D newPos = new Point2D.Double(currentPos.getX() + pendingDir.getDx(), currentPos.getY() + pendingDir.getDy());
					d.add(new DirectionProbability(pendingDir, newPos, 1, 0, true));
				}
				else //pendingDir is a diagonal direction
				{
					d.add(new DirectionProbability(pendingDir, currentPos, 1, 2, false));
				}
			}
			
			else //No pending move
			{
				Direction prevDir = myDP.getDirection();
				Point2D currentPos = myDP.getLocation();
				
				Point2D newPositionIfEast = new Point2D.Double(currentPos.getX() + Direction.E.getDx(), currentPos.getY() + Direction.E.getDy());
				if(prevDir.equals(Direction.E))
					d.add(new DirectionProbability(Direction.E, newPositionIfEast, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.E, newPositionIfEast, .03, 0, true));
				
				
				if(prevDir.equals(Direction.NE))
					d.add(new DirectionProbability(Direction.NE, currentPos, .79, 2, false));
				else
					d.add(new DirectionProbability(Direction.NE, currentPos, .03, 2, false));
				
				
				Point2D newPositionIfNorth = new Point2D.Double(currentPos.getX() + Direction.N.getDx(), currentPos.getY() + Direction.N.getDy());
				if(prevDir.equals(Direction.N))
					d.add(new DirectionProbability(Direction.N, newPositionIfNorth, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.N, newPositionIfNorth, .03, 0, true));
				
				
				if(prevDir.equals(Direction.NW))
					d.add(new DirectionProbability(Direction.NW, currentPos, .79, 2, false));
				else
					d.add(new DirectionProbability(Direction.NW, currentPos, .03, 2, false));
				
				
				
				Point2D newPositionIfWest = new Point2D.Double(currentPos.getX() + Direction.W.getDx(), currentPos.getY() + Direction.W.getDy());
				if(prevDir.equals(Direction.W))
					d.add(new DirectionProbability(Direction.W, newPositionIfWest, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.W, newPositionIfWest, .03, 0, true));
				
				
				
				if(prevDir.equals(Direction.SW))
					d.add(new DirectionProbability(Direction.SW, currentPos, .79, 2, false));
				else
					d.add(new DirectionProbability(Direction.SW, currentPos, .03, 2, false));
				
				
				
				Point2D newPositionIfSouth = new Point2D.Double(currentPos.getX() + Direction.S.getDx(), currentPos.getY() + Direction.S.getDy());
				if(prevDir.equals(Direction.S))
					d.add(new DirectionProbability(Direction.S, newPositionIfSouth, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.S, newPositionIfSouth, .03, 0, true));
				
				
				if(prevDir.equals(Direction.SE))
					d.add(new DirectionProbability(Direction.SE, currentPos, .79, 2, false));
				else
					d.add(new DirectionProbability(Direction.SE, currentPos, .03, 2, false));
			}
			
			
		}
		return d;
	}
	
	
	private ArrayList<DirectionProbability> whereCreatureMayBeInTwoTicks(Point2D location, Direction dir, int numTicksOnSameSpot)
	{
		ArrayList<DirectionProbability> d=new ArrayList<DirectionProbability>();
		
		if(dir == null)
		{
			Point2D positionIfEast = new Point2D.Double(location.getX() + Direction.E.getDx(), location.getY() + Direction.E.getDy());
			Point2D positionIfNorth = new Point2D.Double(location.getX() + Direction.N.getDx(), location.getY() + Direction.N.getDy());
			Point2D positionIfWest = new Point2D.Double(location.getX() + Direction.W.getDx(), location.getY() + Direction.W.getDy());
			Point2D positionIfSouth = new Point2D.Double(location.getX() + Direction.S.getDx(), location.getY() + Direction.S.getDy());
			
			d.add(new DirectionProbability(Direction.E, positionIfEast, .125, 0, true));
			d.add(new DirectionProbability(Direction.NE, location, .125, 2, false));
			d.add(new DirectionProbability(Direction.N, positionIfNorth, .125, 0, true));
			d.add(new DirectionProbability(Direction.NW, location, .125, 2, false));
			d.add(new DirectionProbability(Direction.W, positionIfWest, .125, 0, true));
			d.add(new DirectionProbability(Direction.SW, location, .125, 2, false));
			d.add(new DirectionProbability(Direction.S, positionIfSouth, .125, 0, true));
			d.add(new DirectionProbability(Direction.SE, location, .125, 2, false));
		}
		else
		{
			if(numTicksOnSameSpot == 0)
			{
				Point2D positionIfEast = new Point2D.Double(location.getX() + Direction.E.getDx(), location.getY() + Direction.E.getDy());
				if(dir.equals(Direction.E))
					d.add(new DirectionProbability(Direction.E, positionIfEast, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.E, positionIfEast, .03, 0, true));
				
				Point2D positionIfNorthEast = location;
				if(dir.equals(Direction.NE))
					d.add(new DirectionProbability(Direction.NE, positionIfNorthEast, .79, 2, false));
				else
					d.add(new DirectionProbability(Direction.NE, positionIfNorthEast, .03, 2, false));
				
				Point2D positionIfNorth = new Point2D.Double(location.getX() + Direction.N.getDx(), location.getY() + Direction.N.getDy());
				if(dir.equals(Direction.N))
					d.add(new DirectionProbability(Direction.N, positionIfNorth, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.N, positionIfNorth, .03, 0, true));
				
				Point2D positionIfNorthWest = location;
				if(dir.equals(Direction.NW))
					d.add(new DirectionProbability(Direction.NW, positionIfNorthWest, .79, 2, false));
				else
					d.add(new DirectionProbability(Direction.NW, positionIfNorthWest, .03, 2, false));
	
				Point2D positionIfWest = new Point2D.Double(location.getX() + Direction.W.getDx(), location.getY() + Direction.W.getDy());
				if(dir.equals(Direction.W))
					d.add(new DirectionProbability(Direction.W, positionIfWest, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.W, positionIfWest, .03, 0, true));
				
				Point2D positionIfSouthWest = location;
				if(dir.equals(Direction.SW))
					d.add(new DirectionProbability(Direction.SW, positionIfSouthWest, .79, 2, false));
				else
					d.add(new DirectionProbability(Direction.SW, positionIfSouthWest, .03, 2, false));
				
				Point2D positionIfSouth = new Point2D.Double(location.getX() + Direction.S.getDx(), location.getY() + Direction.S.getDy());
				if(dir.equals(Direction.S))
					d.add(new DirectionProbability(Direction.S, positionIfSouth, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.S, positionIfSouth, .03, 0, true));
				
				Point2D positionIfSouthEast = location;
				if(dir.equals(Direction.SE))
					d.add(new DirectionProbability(Direction.SE, positionIfSouthEast, .79, 2, false));
				else
					d.add(new DirectionProbability(Direction.SE, positionIfSouthEast, .03, 2, false));
			}
			
			
			
			else if(numTicksOnSameSpot == 1)
			{
				Point2D positionIfEast = new Point2D.Double(location.getX() + Direction.E.getDx(), location.getY() + Direction.E.getDy());
				if(dir.equals(Direction.E))
					d.add(new DirectionProbability(Direction.E, positionIfEast, .79, 1, true));
				else
					d.add(new DirectionProbability(Direction.E, positionIfEast, .03, 1, true));
				
				Point2D positionIfNorthEast = new Point2D.Double(location.getX() + Direction.NE.getDx(), location.getY() + Direction.NE.getDy());;
				if(dir.equals(Direction.NE))
					d.add(new DirectionProbability(Direction.NE, positionIfNorthEast, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.NE, positionIfNorthEast, .03, 0, true));
				
				
				Point2D positionIfNorth = new Point2D.Double(location.getX() + Direction.N.getDx(), location.getY() + Direction.N.getDy());
				if(dir.equals(Direction.N))
					d.add(new DirectionProbability(Direction.N, positionIfNorth, .79, 1, true));
				else
					d.add(new DirectionProbability(Direction.N, positionIfNorth, .03, 1, true));
				
				
				Point2D positionIfNorthWest = new Point2D.Double(location.getX() + Direction.NW.getDx(), location.getY() + Direction.NW.getDy());
				if(dir.equals(Direction.NW))
					d.add(new DirectionProbability(Direction.NW, positionIfNorthWest, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.NW, positionIfNorthWest, .03, 0, true));
	
				Point2D positionIfWest = new Point2D.Double(location.getX() + Direction.W.getDx(), location.getY() + Direction.W.getDy());
				if(dir.equals(Direction.W))
					d.add(new DirectionProbability(Direction.W, positionIfWest, .79, 1, true));
				else
					d.add(new DirectionProbability(Direction.W, positionIfWest, .03, 1, true));
				
				Point2D positionIfSouthWest = new Point2D.Double(location.getX() + Direction.SW.getDx(), location.getY() + Direction.SW.getDy());
				if(dir.equals(Direction.SW))
					d.add(new DirectionProbability(Direction.SW, positionIfSouthWest, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.SW, positionIfSouthWest, .03, 0, true));
				
				Point2D positionIfSouth = new Point2D.Double(location.getX() + Direction.S.getDx(), location.getY() + Direction.S.getDy());
				if(dir.equals(Direction.S))
					d.add(new DirectionProbability(Direction.S, positionIfSouth, .79, 1, true));
				else
					d.add(new DirectionProbability(Direction.S, positionIfSouth, .03, 1, true));
				
				Point2D positionIfSouthEast = new Point2D.Double(location.getX() + Direction.SE.getDx(), location.getY() + Direction.SE.getDy());
				if(dir.equals(Direction.SE))
					d.add(new DirectionProbability(Direction.SE, positionIfSouthEast, .79, 0, true));
				else
					d.add(new DirectionProbability(Direction.SE, positionIfSouthEast, .03, 0, true));
			}
			else if(numTicksOnSameSpot == 2)
			{
				double probE, probNE, probN, probNW, probW, probSW, probS, probSE;
				
				//take into account walls, do this for prev ones also
				
				probE = 0;
				probN = 0;
				probW = 0;
				probS = 0;
				
				if(manhattanDirection(dir))
				{
					probNE = .25;
					probNW = .25;
					probSW = .25;
					probSE = .25;
				}
				else
				{
					if(dir.equals(Direction.NE))
						probNE = .90;
					else
						probNE = .035;
					
					if(dir.equals(Direction.NW))
						probNW = .90;
					else
						probNW = .035;
					
					if(dir.equals(Direction.SW))
						probSW = .90;
					else
						probSW = .035;
					
					if(dir.equals(Direction.SE))
						probSE = .90;
					else
						probSE = .035;
				}
				
				
				Point2D positionIfNorthEast = new Point2D.Double(location.getX() + Direction.NE.getDx(), location.getY() + Direction.NE.getDy());;
				if(dir.equals(Direction.NE))
					d.add(new DirectionProbability(Direction.NE, positionIfNorthEast, probNE, 1, true));
				else
					d.add(new DirectionProbability(Direction.NE, positionIfNorthEast, probNE, 1, true));
				
				
				
				Point2D positionIfNorthWest = new Point2D.Double(location.getX() + Direction.NW.getDx(), location.getY() + Direction.NW.getDy());
				if(dir.equals(Direction.NW))
					d.add(new DirectionProbability(Direction.NW, positionIfNorthWest, probNW, 1, true));
				else
					d.add(new DirectionProbability(Direction.NW, positionIfNorthWest, probNW, 1, true));
	
				
				Point2D positionIfSouthWest = new Point2D.Double(location.getX() + Direction.SW.getDx(), location.getY() + Direction.SW.getDy());
				if(dir.equals(Direction.SW))
					d.add(new DirectionProbability(Direction.SW, positionIfSouthWest, probSW, 1, true));
				else
					d.add(new DirectionProbability(Direction.SW, positionIfSouthWest, probSW, 1, true));
				
				
				Point2D positionIfSouthEast = new Point2D.Double(location.getX() + Direction.SE.getDx(), location.getY() + Direction.SE.getDy());
				if(dir.equals(Direction.SE))
					d.add(new DirectionProbability(Direction.SE, positionIfSouthEast, probSE, 1, true));
				else
					d.add(new DirectionProbability(Direction.SE, positionIfSouthEast, probSE, 1, true));
			}
		}
		return d;
	}
	
}
