package isnork.g5;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;

import isnork.sim.GameConfig;
import isnork.sim.GameEngine;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLife;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;
import isnork.sim.GameObject.Direction;



public class spiralPlayer extends Player {

	private Direction direction;
	private final int timeToStartSpiral = 6;
	private int timeElapsed = 0;
	private Point2D positionOfBoat = new Point2D.Double(0,0);;
	private double distanceFromBoat;
	private int spiralRadius = 8;
	private int changeToSpiralRadius = 5;
	private final int timeToCompleteFirstRevolution = 128; //spiral radius - 8 -> 16*(4 sides)*2 mins 
	private final int timeToCompleteSecondRevolution = 336; //spiral radius - 13 - adding time for first and second revolution
	private int gameBoardRadius;
	private int spiralBoundary;
	private boolean timeToStartDownwardSpiral = false;
	private boolean firstRevolutionCompleted = false;
	private boolean secondRevolutionCompleted = false;
	private double timeToCompleteNthRevolution;
	private boolean nthRevolutionCompleted = false;
	
	
	
	private Direction getNewDirection() {
		int r = random.nextInt(100);
		if(r < 10 || direction == null)
		{
			ArrayList<Direction> directions = Direction.allBut(direction);
			direction = directions.get(random.nextInt(directions.size()));
		}
			return direction;
	}
	@Override
	public String getName() {

		
			
		return "SpiralPlayer";
	}
	Point2D whereIAm = null;
	int n = -1;
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,Set<Observation> playerLocations) {
		timeElapsed++;
		whereIAm = myPosition;
			
		if(n % 10 == 0)
			return "s";
		else
			return null;
	}
	@Override
	public Direction getMove() {
		Direction d = getNewDirection();
		
		
		
			if (firstRevolutionCompleted == false)
				timeToCompleteNthRevolution = timeToCompleteFirstRevolution;
			else {
				
				timeToCompleteNthRevolution = timeToCompleteSecondRevolution;
				
			}
		
		
		distanceFromBoat = whereIAm.distance(positionOfBoat);
		
		if (timeElapsed >= timeToStartSpiral) {
			
			if (timeElapsed > timeToCompleteNthRevolution && whereIAm.getX() < spiralBoundary && whereIAm.getY() < spiralBoundary) {
				if (firstRevolutionCompleted == false) firstRevolutionCompleted = true;
				else  { GameEngine.println("second revolution completed"); secondRevolutionCompleted = true; }
				
				spiralRadius = spiralRadius + changeToSpiralRadius;
				
								
			}
			
			
			
			/* else if (timeElapsed > timeToCompleteNthRevolution && (whereIAm.getX() >= spiralBoundary || whereIAm.getY() >= spiralBoundary)) {
				//GameEngine.println("end of outward spiral");
				timeToStartDownwardSpiral = true;
				
				if (changeToSpiralRadius > 0) { //downward spiral not begun yet
					changeToSpiralRadius = changeToSpiralRadius * -1;
				}
				spiralRadius = spiralRadius + changeToSpiralRadius;
				
			}*/
			
					
			if (secondRevolutionCompleted == false)
				d = getDirectionInSpiralPath();
			else d = null;
			
		}
		
		try {
		Point2D p = new Point2D.Double(whereIAm.getX() + d.dx,
				whereIAm.getY() + d.dy);
		while (Math.abs(p.getX()) > GameConfig.d
				|| Math.abs(p.getY()) > GameConfig.d) {
			d = getNewDirection();
			p = new Point2D.Double(whereIAm.getX() + d.dx,
					whereIAm.getY() + d.dy);
		}
		}
		
		catch (Exception e)
		{
			d = null;
		}
		
		
		return d;
	}
	@Override
	public void newGame(Set<SeaLifePrototype> seaLifePossibilites, int penalty,
			int d, int r, int n) {
		// TODO Auto-generated method stub
		
		gameBoardRadius = r;
		spiralBoundary = GameConfig.d - r;
		
		
	}


	public Direction getDirectionInSpiralPath() {
		
		Direction directionInSpiral = null;
		
		double spiralRadiusLowBound = -1 * spiralRadius;
		double spiralRadiusHighBound = spiralRadius;
		
		

	/*	
	boolean b1 = whereIAm.getX() == spiralRadiusHighBound;
	boolean b2 = whereIAm.getY() < spiralRadiusHighBound;
	boolean b3 = whereIAm.getY() >= spiralRadiusLowBound;	
		
	
	GameEngine.println("snork " + this.getId() + " in position " + whereIAm);
	GameEngine.println("spiralRadiusHighBound " + spiralRadiusHighBound);
	GameEngine.println("whereIAm.getX() == spiralRadiusHighBound " + b1 + " for snork " + this.getId());
	GameEngine.println("whereIAm.getY() < spiralRadiusHighBound;" + b2 + " for snork " + this.getId());
	GameEngine.println("whereIAm.getY() >= spiralRadiusLowBound" + b3 + " for snork " + this.getId());
	*/	
		
	
	//GameEngine.println("spiralRadiusHighBound " + spiralRadiusHighBound + " snork " + this.getId());
		
		if (whereIAm.getX() == spiralRadiusLowBound && whereIAm.getY() <= spiralRadiusHighBound && whereIAm.getY() > spiralRadiusLowBound)
			directionInSpiral = Direction.N;
		else if (whereIAm.getX() == spiralRadiusHighBound && whereIAm.getY() < spiralRadiusHighBound && whereIAm.getY() >= spiralRadiusLowBound)
			directionInSpiral = Direction.S;
		else if (whereIAm.getY() == spiralRadiusLowBound && whereIAm.getX() < spiralRadiusHighBound && whereIAm.getX() >= spiralRadiusLowBound)
			directionInSpiral = Direction.E;
		else if (whereIAm.getY() == spiralRadiusHighBound && whereIAm.getX() <= spiralRadiusHighBound && whereIAm.getX() > spiralRadiusLowBound)
			directionInSpiral = Direction.W;
		else if (whereIAm.getY() == 0 && whereIAm.getX() > spiralRadiusLowBound && whereIAm.getX() < spiralRadiusHighBound) directionInSpiral = Direction.E; 
		else if (whereIAm.getX() == 0 && whereIAm.getY() > spiralRadiusLowBound && whereIAm.getY() < spiralRadiusHighBound) directionInSpiral = Direction.N;
		
		else if (whereIAm.getX() > spiralRadiusHighBound) directionInSpiral = Direction.W;
		else if (whereIAm.getX() < spiralRadiusLowBound) directionInSpiral = Direction.E;
		else if (whereIAm.getY() > spiralRadiusHighBound) directionInSpiral = Direction.N;
		else if (whereIAm.getY() < spiralRadiusLowBound) directionInSpiral = Direction.S;
		
		
		else if (whereIAm.getX() > 0) { GameEngine.println ("snork " + this.getId() + " moving east from " + whereIAm); directionInSpiral = Direction.E; }
		else if (whereIAm.getX() < 0) { GameEngine.println ("snork " + this.getId() + " moving west from " + whereIAm); directionInSpiral =  Direction.W; }
		
		
		
		else {
			directionInSpiral = null; 
			
			//HERE'S WHERE YOU WILL CALL TOWARDSBOAT TO RETURN SNORKS TO BOAT
			//I HAVE SET DIRECTIONINSPIRAL TO NULL FOR NOW
			//GameEngine.println("direction in spiral is null for snork " + this.getId());
		}
		
		
		
		
		
		return directionInSpiral;
		
	}
	
	


}
