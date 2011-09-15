package isnork.g3;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Set;

/**
 * Snapshot of old wpCartogram in case we ever want to reuse the old code
 * 
 * @author Moses
 * 
 */

public class RandomCartogram implements Cartogram {
	private Point2D currentLocation;
	private Random random;
	private int ticks;
	private static final int MAX_TICKS_PER_ROUND = 60 * 8;
	private final int mapWidth;

	public RandomCartogram(int mapWidth, int viewRadius, int numDivers) {
		this.random = new Random();
		ticks = 0;

		this.mapWidth = mapWidth;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Direction getNextDirection() {
		return randomWalk();
	}

	private Direction randomWalk() {
		if (ticks < MAX_TICKS_PER_ROUND - 3 * mapWidth) {
			Direction[] dirs = Direction.values();
			return dirs[random.nextInt(dirs.length)];
		} else {
			return returnBoat();
		}
	}

	private Direction returnBoat() {
		// Move towards boat
		String direc = getDirection();
		return parseDirection(direc);
	}

	private Direction parseDirection(String direc) {
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

	private String getDirection() {
		String direc = "";
		if (currentLocation.getY() < 0)
			direc = direc.concat("S");
		else if (currentLocation.getY() > 0)
			direc = direc.concat("N");

		if (currentLocation.getX() < 0)
			direc = direc.concat("E");
		else if (currentLocation.getX() > 0)
			direc = direc.concat("W");
		return direc;
	}

}
