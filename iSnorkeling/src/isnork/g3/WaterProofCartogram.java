package isnork.g3;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Lists;

public class WaterProofCartogram implements Cartogram {
	private final int sideLength;
	private final int viewRadius;
	private final int numDivers;
	private final Square[][] mapStructure;
	
	private Point2D currentLocation;
	private Random random;
	private int ticks;
	private static final int MAX_TICKS_PER_ROUND = 60 * 8;
	
	public WaterProofCartogram(int mapWidth, int viewRadius, int numDivers) {
		this.sideLength = mapWidth;
		this.viewRadius = viewRadius;
		this.numDivers = numDivers;
		this.mapStructure = new WaterProofSquare[sideLength][sideLength];
		this.random = new Random();
		ticks = 0;
	}

	@Override
	public void update(Point2D myPosition, Set<Observation> whatYouSee,
			Set<Observation> playerLocations,
			Set<iSnorkMessage> incomingMessages) {
		ticks++;
		currentLocation = myPosition;
		for (Observation location : playerLocations) {
			location.getLocation();
			location.getId();
			location.getName();
		}

		for (iSnorkMessage message : incomingMessages) {
			message.getLocation();
			message.getMsg();
			message.getSender();
		}

		for (Observation observation : whatYouSee) {
			observation.getDirection();
			observation.getLocation();
			observation.getId();
			observation.happiness();
			observation.isDangerous();
			observation.happinessD();
			observation.getName();
		}
	}

	@Override
	public String getMessage() {
		return "";
	}

	@Override
	public Direction getNextDirection() {
		return greedyHillClimb();
	}

	private Direction greedyHillClimb() {
		/*
		 * Iterate over all possible new squares you can hit next.  
		 * For you to move in a diagonal direction, you need to be 1.5* as good as ortho
		 * To stay in the same square, you only need to be .5 * as good as ortho
		 */
		
		double x = (int) this.currentLocation.getX();
		double y = (int) this.currentLocation.getY();

		List<DirectionValue> lst = Lists.newArrayListWithCapacity(8);

		lst.add(new DirectionValue(Direction.STAYPUT, getExpectedHappinessForCoords(x, y) * 6.0));
		
		lst.add(new DirectionValue(Direction.E, getExpectedHappinessForCoords(x + 1, y) * 3.0));
		lst.add(new DirectionValue(Direction.W, getExpectedHappinessForCoords(x - 1, y) * 3.0));
		
		lst.add(new DirectionValue(Direction.S, getExpectedHappinessForCoords(x, y + 1) * 3.0));
		lst.add(new DirectionValue(Direction.N, getExpectedHappinessForCoords(x, y - 1) * 3.0));
		
		lst.add(new DirectionValue(Direction.SE, getExpectedHappinessForCoords(x + 1, y + 1) * 2.0));
		lst.add(new DirectionValue(Direction.SW, getExpectedHappinessForCoords(x - 1, y + 1) * 2.0));
		
		lst.add(new DirectionValue(Direction.NE, getExpectedHappinessForCoords(x + 1, y - 1) * 2.0));
		lst.add(new DirectionValue(Direction.NW, getExpectedHappinessForCoords(x - 1, y - 1) * 2.0));
		
		DirectionValue max = lst.get(0);
		for (DirectionValue dv : lst) {
			if (dv.getDub() > max.getDub()){
				max = dv;
			}
		}
		
		return max.getDir();
	}

	private double getExpectedHappinessForCoords(double x, double y) {
		if (isInvalidCoords(x, y)){
			return Double.MIN_VALUE;
		}
		
		int minX = (int) x - viewRadius;
		minX = ((minX < -sideLength / 2) ? minX : -sideLength / 2) + sideLength / 2;
		
		int minY = (int) y - viewRadius;
		minY = ((minY < -sideLength / 2) ? minY : -sideLength / 2) + sideLength / 2;

		int maxX = (int) x + viewRadius;
		maxX = ((maxX > sideLength / 2) ? maxX : sideLength / 2) + sideLength / 2;

		int maxY = (int) y + viewRadius;
		maxY = ((maxY > sideLength / 2) ? maxY : sideLength / 2) + sideLength / 2;
		
		double expectedHappiness = 0.0;
		for (int xCoord = minX; xCoord < maxX; xCoord++){
			for (int yCoord = minY; yCoord < maxY; yCoord++){
				if (((x + sideLength / 2) * (x + sideLength / 2) +
						(y + sideLength / 2) * (y + sideLength / 2)) < viewRadius){
					expectedHappiness += mapStructure[xCoord][yCoord].getExpectedHappiness();
				}
			}
		}
		return expectedHappiness;
	}

	private boolean isInvalidCoords(double x, double y) {
		if ( x < -sideLength / 2 ){
			return true;
		}
		else if ( x > sideLength / 2 ){
			return true;			
		}
		else if ( y < -sideLength / 2 ){
			return true;
		}
		else if ( y > sideLength / 2 ){
			return true;
		}
		else{
			return false;
		}
	}

	private Direction randomWalk() {
		if (ticks < MAX_TICKS_PER_ROUND - 3 * sideLength) {
			Direction[] dirs = Direction.values();
			return dirs[random.nextInt(dirs.length)];
		} else {
			return returnBoat();
		}
	}

	private Direction returnBoat() {
		// Move towards boat
		String direc = "";

		if (currentLocation.getY() < 0)
			direc = direc.concat("S");
		else if (currentLocation.getY() > 0)
			direc = direc.concat("N");

		if (currentLocation.getX() < 0)
			direc = direc.concat("E");
		else if (currentLocation.getX() > 0)
			direc = direc.concat("W");

		if (direc.equals("W")) {
			return Direction.W;
		} else if (direc.equals("E")) {
			return Direction.E;
		} else if (direc.equals("N")) {
			return Direction.N;
		} else if (direc.equals("S")) {
			return Direction.S;
		} else if (direc.equals("NE")) {
			return Direction.NE;
		} else if (direc.equals("SE")) {
			return Direction.SE;
		} else if (direc.equals("NW")) {
			return Direction.NW;
		} else if (direc.equals("SW")) {
			return Direction.SW;
		} else {
			return Direction.STAYPUT;
		}
	}

}
