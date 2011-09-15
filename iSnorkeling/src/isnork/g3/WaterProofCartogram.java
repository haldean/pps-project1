package isnork.g3;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Set;

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
		return randomWalk();
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
