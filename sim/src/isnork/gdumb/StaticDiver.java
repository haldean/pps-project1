package isnork.gdumb;

import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;

public class StaticDiver {
	private static final Point2D BOAT = new Point2D.Double(0.0, 0.0);
    public static final Direction[] DIRECTIONS = {Direction.E, Direction.NE,
        Direction.N, Direction.NW, Direction.W, Direction.SW, Direction.S, Direction.SE};
    
	public static double getDistanceFromCreature(Point2D creature, Point2D diver) {
		return creature.distance(diver);
	}

	public static Direction directionTowardCreature(Point2D diver,
			Point2D creature) {
		int quadrent = 1;
		if (creature.getY() - diver.getY() > 0) {
			quadrent = 0;
		}

		if (creature.getX() - diver.getX() > 0) {
			quadrent = quadrent == 0 ? 4 : 1;
		} else {
			quadrent = quadrent == 0 ? 3 : 2;
		}

		double radians = Math.atan(Math.abs(creature.getY() - diver.getY())
				/ Math.abs(creature.getX() - diver.getX()));

		double degrees = Math.toDegrees(radians);
		
		if(quadrent == 2 || quadrent == 4) {
			degrees = 90 - degrees;
		}
		
		int quad = 2 * (quadrent - 1);
		
		if (degrees > 22.5)
			quad++;
		if (degrees > 45 + 22.5)
			quad++;
		
		quad = quad % 8;
		
		return quad < 0 ? null : DIRECTIONS[quad];
	}
		
	public static boolean pointIsOnBoard(double x, double y, int boardRadius) {
	    return (Math.abs(x) <= boardRadius && Math.abs(y) <= boardRadius);
	}
	
	public static boolean isValidMove(Point2D origin, Direction move, int boardRadius) {
	    if(move == null) {
	        return true;
	    }
	    return pointIsOnBoard(origin.getX() + move.getDx(), origin.getY() + move.getDy(), boardRadius);
	}
	
    /**
     * @param whereIAm
     * @param move The direction being tested for moving
     * @param location The location being tested against
     * @return Whether move would put you adjacent to location.
     */
	public static boolean wouldMoveNextTo(Point2D whereIAm, Direction move, Point2D location) {
		Point2D nextLoc = new Point2D.Double(whereIAm.getX() + move.dx, whereIAm.getY() + move.dy);
		if(nextLoc.equals(BOAT))
			return false;
		return nextLoc.distance(location) < 2;
	}
}
