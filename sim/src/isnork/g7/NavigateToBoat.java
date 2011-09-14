package isnork.g7;

import isnork.sim.GameObject.Direction;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.Set;

public class NavigateToBoat {
	/**
	 * Get the shortest time back to boat
	 * @param from
	 * @return
	 */
	public static int getTimeToBoat(Point2D from) {
		int minutes = 0;
		Point2D loc = (Point2D) from.clone();
		// 3 minutes to travel diagonals, 2 to travel orthogonally.
		// (Diagonal travel is faster.)
		// So, first travel along diagonals until you are in line
		// vertically or horizontally with (0, 0).
		double absX = Math.abs(loc.getX());
		double absY = Math.abs(loc.getY());
		if (absX < absY) {
			// X will reach 0 first
			minutes += 3 * absX;
			minutes += 2 * (absY - absX);
		} else {
			// Y will reach 0 first
			minutes += 3 * absY;
			minutes += 2 * (absX - absY);
		}
		return minutes;
	}
	
	public static Direction getShortestDirectionToBoat(Point2D from) {
		if (from.getX() > 0 && from.getY() > 0) {
			return Direction.NW;
		} else if (from.getX() > 0 && from.getY() < 0) {
			return Direction.SW;
		} else if (from.getX() < 0 && from.getY() < 0) {
			return Direction.SE;
		} else if (from.getX() < 0 && from.getY() > 0) {
			return Direction.NE;
		} else if (from.getX() > 0) {
			return Direction.W;
		} else if (from.getX() < 0) {
			return Direction.E;
		} else if (from.getY() > 0) {
			return Direction.N;
		} else if (from.getY() < 0) {
			return Direction.S;
		} else {
			return null;
		}
	}
	
	public static int getBoatTimeBufferAdjusted(int baseTime, Set<SeaLifePrototype> seaLifePossibilities, int d) {
		int numDangerousCreatures = 0;
		for (SeaLifePrototype life : seaLifePossibilities) {
			if (life.isDangerous()) {
				numDangerousCreatures += life.getMaxCount();
			}
		}
		// it takes 14 minutes to go around a 3x3 square, versus 8 minutes to go straight through
		return baseTime + (14 * numDangerousCreatures / 4 / (d * d));
	}
}
