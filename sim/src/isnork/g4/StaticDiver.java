package isnork.g4;

import isnork.sim.Board;
import isnork.sim.GameEngine;
import isnork.sim.Observation;
import isnork.sim.SeaLife;
import isnork.sim.GameObject.Direction;
import isnork.sim.SeaLifePrototype;
import isnork.sim.ui.GUI;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;

public class StaticDiver {
	
	public static final boolean DEBUG_ON = false;
	private static final Point2D BOAT = new Point2D.Double(0.0, 0.0);
	private static final int EDGE_FEAR = 3;
	private static final double DANGER_RADIUS = 3 * Math.sqrt(2);
    private static final Logger log = Logger.getLogger(G4Diver.class);
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
	
	public static Direction locationAfterMoves(Point2D diver,
			Direction dir, int moves) {
		int quadrent = 1;
		if (dir.dy - diver.getY() > 0) {
			quadrent = 0;
		}

		if (dir.dx - diver.getX() > 0) {
			quadrent = quadrent == 0 ? 4 : 1;
		} else {
			quadrent = quadrent == 0 ? 3 : 2;
		}

		double radians = Math.atan(Math.abs(dir.dy - diver.getY())
				/ Math.abs(dir.dx - diver.getX()));

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

	public static Direction[]  getAdjacentDirections(Direction d) {
		for(int i = 0; i < DIRECTIONS.length; i++) {
		    if(DIRECTIONS[(i+1)%DIRECTIONS.length] == d) {
		        return new Direction[]{DIRECTIONS[i],
		                DIRECTIONS[(i+1)%DIRECTIONS.length],
		                DIRECTIONS[(i+2)%DIRECTIONS.length]}; 
		    }
		}
		return new Direction[3];
	}
		
	public static boolean pointIsOnBoard(double x, double y, int boardRadius) {
	    return (Math.abs(x) <= boardRadius && Math.abs(y) <= boardRadius);
	}

    public static boolean checkDangerousDirection(int radius, Point2D ourPoint, 
            Direction ourMove, Point2D itsPoint, Direction itsMove){
        return checkDangerousDirectionFancy(radius, ourPoint, ourMove, itsPoint, itsMove);
    }
	
	public static boolean checkDangerousDirectionSimple(int radius, Point2D ourPoint, 
            Direction ourMove, Point2D itsPoint, Direction itsMove){
	    ArrayList<Direction> badHeadings = new ArrayList<Direction>();
	    Direction[] adj = getAdjacentDirections(directionTowardCreature(ourPoint, itsPoint));
	    for(int i = 0; i < adj.length; i++) {
	        badHeadings.add(adj[i]);
	    }
	    
	    badHeadings.add(itsMove);
	    
	    return badHeadings.contains(ourMove);
	}
	
	public static boolean checkDangerousDirectionFancy(int radius, Point2D ourPoint, 
			Direction ourMove, Point2D itsPoint, Direction itsMove){
	    Point2D ourProjected = new Point2D.Double(ourPoint.getX() + ourMove.getDx(), ourPoint.getY() + ourMove.getDy());
	    Point2D itsProjected = new Point2D.Double(itsPoint.getX() + itsMove.getDx(), itsPoint.getY() + itsMove.getDy());
	    Point2D itsDistantFuture = new Point2D.Double(itsProjected.getX() + itsMove.getDx(), itsProjected.getY() + itsMove.getDy());
	    
	    double preDistance = getDistanceFromCreature(itsPoint, ourPoint);
        double postDistance = getDistanceFromCreature(itsProjected, ourProjected);
        
        return ((postDistance < DANGER_RADIUS && preDistance >= DANGER_RADIUS) ||
                ourMove == directionTowardCreature(ourPoint, itsPoint) ||
                ourMove == directionTowardCreature(ourPoint, itsProjected) ||
                ourMove == directionTowardCreature(ourPoint, itsDistantFuture)); //|| postDistance < preDistance;
	}
	    
	    /*
	    Point2D alteredPoint = new Point2D.Double(ourPoint.getX() + ourMove.dx*radius, 
				ourPoint.getY() + ourMove.dy*radius);
		log.debug("Altered Point:" + alteredPoint.clone() + " direction" + ourMove.toString());
		Line2D.Double originLine = new Line2D.Double(ourPoint, alteredPoint);
		

		Point2D itsAlteredPoint = new Point2D.Double(itsPoint.getX() +itsMove.dx*radius, 
				itsPoint.getY() + itsMove.dy*radius);
		Line2D.Double dangerLine = new Line2D.Double(itsPoint, itsAlteredPoint);
			
        //Checks to see if our move could move next to his move
        for(Direction d : DIRECTIONS){
            Point2D anotherDirection = new Point2D.Double(itsPoint.getX()+d.dx, itsPoint.getY()+d.dy);
            if(wouldMoveNextTo(ourPoint, ourMove, anotherDirection));
                return true;
        }
        
		if (DEBUG_ON) {
			GUI gui = GameEngine.getGui();
			if (originLine.intersectsLine(dangerLine)) {
				log.debug("DANGEROUS collision: " + ourMove);
				if (gui != null) {
					// Debuging in GUI
					Line2D.Double dangerLineBoard = new Line2D.Double(
							Board.toScreenSpace((int) itsPoint.getX()),
							Board.toScreenSpace((int) itsPoint.getY()),
							Board.toScreenSpace((int) itsAlteredPoint.getX()),
							Board.toScreenSpace((int) itsAlteredPoint.getY()));

					Line2D.Double originLineBoard = new Line2D.Double(
							Board.toScreenSpace((int) ourPoint.getX()),
							Board.toScreenSpace((int) ourPoint.getY()),
							Board.toScreenSpace((int) alteredPoint.getX()),
							Board.toScreenSpace((int) alteredPoint.getY()));

					gui.getboardPanel().debugLines.add(dangerLineBoard);
					gui.getboardPanel().debugLines.add(originLineBoard);
				}
			}
		}		
		
		return  originLine.intersectsLine(dangerLine);	
	}*/
	
	public static boolean isValidMove(Point2D origin, Direction move, int boardRadius) {
	    return pointIsOnBoard(origin.getX() + move.getDx(), origin.getY() + move.getDy(), boardRadius);
	}

    public static SeaLife seaLifeFromObservation(Observation o, ArrayList<SeaLifePrototype> lifetypes) {
        for(SeaLifePrototype proto : lifetypes) {
            if(proto.getName().equals(o.getName())) {
                SeaLife creature = new SeaLife(proto);
                creature.setId(o.getId());
                creature.setDirection(o.getDirection());
                creature.setLocation(o.getLocation());
                
                return creature;
            }
        }
        
        return null;
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

	/**
	 * @param whereIAm The Diver's location
	 * @param next The move being tested
	 * @param boardRadius
	 * @param sightRadius
	 * @return whether a move is desirable in that it doesn't move foolishly close to the wall.
	 */
    public static boolean isDesirableMove(Point2D whereIAm, Direction next,
            int boardRadius, int sightRadius) {
        return isValidMove(whereIAm, next, (EDGE_FEAR + boardRadius - sightRadius));
    }
}
