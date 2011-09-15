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
	public String update(Point2D myPosition, Set<Observation> whatYouSee,
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

		return null;
	}

	@Override
	public String getMessage() {
    return "";
	}

	@Override
	public Direction getNextDirection() {
		if (ticks < MAX_TICKS_PER_ROUND - 100) {
			Direction[] dirs = Direction.values();
			return dirs[random.nextInt(dirs.length)];
		} else {
			// Move towards boat
			int deltaX = 0;
			int deltaY = 0;
			if (currentLocation.getX() < 0)
				deltaX = 1;
			else if (currentLocation.getX() > 0)
				deltaX = -1;

			if (currentLocation.getY() < 0)
				deltaY = 1;
			else if (currentLocation.getY() > 0)
				deltaY = -1;

			if (deltaX > 0 && deltaY == 0) {
				return Direction.E;
			} else if (deltaX > 0 && deltaY > 0) {
				return Direction.NE;
			} else if (deltaX > 0 && deltaY < 0) {
				return Direction.SE;
			} else if (deltaX == 0 && deltaY == 0) {
				return Direction.STAYPUT;
			} else if (deltaX == 0 && deltaY < 0) {
				return Direction.N;
			} else if (deltaX == 0 && deltaY > 0) {
				return Direction.S;
			} else if (deltaX < 0 && deltaY == 0) {
				return Direction.W;
			} else if (deltaX < 0 && deltaY > 0) {
				return Direction.NW;
			} else if (deltaX < 0 && deltaY < 0) {
				return Direction.NE;
			} else {
				throw new RuntimeException("I HAVE NO IDEA");
			}
		}
	}
}
