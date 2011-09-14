package isnork.g7;

import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;

import org.apache.log4j.Logger;

public class OurBoard {
	
	int d;
	
	private Logger logger = Logger.getLogger(OurBoard.class);
	
	public OurBoard(int d) {
		this.d = d;
	}
	
	/*Returns the direction between to points*/
	public static Direction getDirectionTowards(Point2D from, Point2D to){
		
		if (from == null || to == null)
			return null;
		
		if (from.getX() < to.getX() && from.getY() > to.getY()){
			return Direction.NE;
		}
		else if(from.getX() < to.getX() && from.getY() == to.getY()){
			return Direction.E;
		}
		else if(from.getX() < to.getX() && from.getY() < to.getY()){
			return Direction.SE;
		}
		else if(from.getX() == to.getX() && from.getY() < to.getY()){
			return Direction.S;
		}
		else if(from.getX() > to.getX() && from.getY() < to.getY()){
			return Direction.SW;
		}
		else if(from.getX() > to.getX() && from.getY() == to.getY()){
			return Direction.W;
		}
		else if(from.getX() > to.getX() && from.getY() > to.getY()){
			return Direction.NW;
		}
		else if(from.getX() == to.getX() && from.getY() > to.getY()){
			return Direction.N;
		}
		
		return null;
	}
	
	public Direction getOppositeDirection(Direction d){
		if (d == Direction.NE)
			return Direction.SW;
		else if (d == Direction.E)
			return Direction.W;
		else if (d == Direction.SE)
			return Direction.NW;
		else if (d == Direction.S)
			return Direction.N;
		else if (d == Direction.SW)
			return Direction.NE;
		else if (d == Direction.W)
			return Direction.E;
		else if (d == Direction.NW)
			return Direction.SE;
		else if (d == Direction.N)
			return Direction.S;
		else 
			return null;
	}
	
	public double findDistanceToObservation(OurObservation observation, Point2D myCurrentLocation){
		
		Point2D obsLocation = observation.getTheLocation().getLocation();
		
		//logger.debug("observation:" + observation);
		//logger.debug("observation.getTheLocation():" + observation.getTheLocation());
		//logger.debug("observation.getTheLocation().getLocation():" + observation.getTheLocation().getLocation());
		
		if(obsLocation!=null){

		double x = obsLocation.getX();
		double y = obsLocation.getY();
		
		double dx = Math.abs(x - myCurrentLocation.getX());
		double dy = Math.abs(obsLocation.getY() - myCurrentLocation.getY());

		double dz = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		
		return dz;
		}
		else 
			return 1000;
	}
	
	public boolean inBounds(int x, int y) {
		return Math.abs(x) <= d && Math.abs(y) <= d;
	}
	
	public boolean isNearBoundary(Point2D location, double nearDistance) {
		return location.distance(location.getX(), d) < nearDistance
			|| location.distance(location.getX(), -d) < nearDistance
			|| location.distance(d, location.getY()) < nearDistance
			|| location.distance(-d, location.getY()) < nearDistance;
		
	}
}
