package isnork.g7;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;


public class DangerFinder {
	
	static final double DANGER_MULTIPLIER = 2.0;
	static final double DANGER_MAX_DISTANCE = 6.0;
	static final double WALL_MAX_DISTANCE = 5.0 / Math.sqrt(2.0) - 1.0;  // let you see corner
	static final double STATIONARY_DANGER_DISTANCE = 2;
	
	private HashMap<Direction, Double> directionDanger;
	private Point2D myPosition;
	private Point2D myPreviousPosition;
	private Set<Observation> whatYouSee;
	private OurBoard ourBoard;
	private int d;
	private int r;
	private Set<SeaLifePrototype> seaLifePossibilities;
	private Direction mySafestDirection;
	private Random random;
	
	private ArrayList<Point2D> backtrackLocations;
	
	// TODO change time-to-get-home based on stats (number of dangerous creatures)
	
	Logger logger = Logger.getLogger(DangerFinder.class);
	

	public DangerFinder(OurBoard ourBoard, int d, int r, Set<SeaLifePrototype> seaLifePossibilities, Random random){
		this.ourBoard = ourBoard;
		this.d = d;
		this.r = r;
		this.seaLifePossibilities = seaLifePossibilities;
		this.directionDanger = new HashMap<Direction, Double>();
		this.random = random;
		this.backtrackLocations = new ArrayList<Point2D>();
	}
	
	
	/*Should only be called once*/
	private void findDanger(){
			
		for(Observation o : whatYouSee){
//			logger.trace("What I see: " + o.getName() + "\t\tIs it dangerous? " + o.isDangerous());
			if (!o.isDangerous())
				continue;
			
			Point2D predictedLocation;
			
			int speed = 0;
			int happy = 0;  // can't use o.happiness() because that is 0 when you are on boat!
			for (SeaLifePrototype life : seaLifePossibilities) {
				if (life.getName().equals(o.getName())) {
					speed = life.getSpeed();
					happy = life.getHappiness();
					break;
				}
			}
			
			// Get the location and distance based on predicted location
			// (taking into account whether creature is stationary or moving).
			if (speed == 0 || o.getDirection() == null) {
				predictedLocation = o.getLocation();
			} else {
				// predict where it will be
				Point2D loc = new Point2D.Double(
//						o.getLocation().getX() + (o.getDirection().getDx() * (o.getDirection().isDiag() ? 3 : 2)),
//						o.getLocation().getY() + (o.getDirection().getDy() * (o.getDirection().isDiag() ? 3 : 2))
						o.getLocation().getX() + (o.getDirection().getDx()),
						o.getLocation().getY() + (o.getDirection().getDy())
						);
				if (ourBoard.inBounds((int)loc.getX(), (int)loc.getY())) {
					predictedLocation = loc;
				} else {
					predictedLocation = o.getLocation();
				}
			}
			
			// For each Direction that diver can move, calculate the sum of danger from all creatures affecting it.
			// The danger is scaled based on distance (decreases by a factor of radius).
			for (Direction d : Direction.values()) {
				
				Point2D nextPosition = new Point2D.Double(
						myPosition.getX() + d.getDx(),
						myPosition.getY() + d.getDy());
				
				if (!ourBoard.inBounds((int)nextPosition.getX(), (int)nextPosition.getY()))
					continue;
				
				double distanceToCreature = Math.max(1.0, nextPosition.distance(predictedLocation));
				
//				logger.debug("Direction:"+d+" distanceToCreature = " + distanceToCreature);

				// Only consider dangerous creatures if:
				// 1. They are stationary and affect cells next to the candidate cell, OR
				// 2. They are moving and affect cells within DANGER_MAX_DISTANCE of the candidate cell.
				if (o.isDangerous() && ((speed == 0 && distanceToCreature <= STATIONARY_DANGER_DISTANCE)
						|| (speed > 0 && distanceToCreature <= DANGER_MAX_DISTANCE))) {
					
					double formerDirectionDanger = 0;
					
					if (directionDanger.containsKey(d)) {
						formerDirectionDanger = directionDanger.get(d);
						//logger.debug("formerDirectionDanger = " + formerDirectionDanger);
					}
						
					directionDanger.put(d, new Double(formerDirectionDanger +
							( Math.abs(happy*DANGER_MULTIPLIER) / Math.pow(distanceToCreature, 2) )));
						
				}
				
			}
			
		}
	}
	
	private boolean isMovingTowardsMe(Observation o) {
		Point2D nextPosition = new Point2D.Double(
				o.getLocation().getX() + o.getDirection().getDx(),
				o.getLocation().getY() + o.getDirection().getDy());
		if (ourBoard.inBounds((int)nextPosition.getX(), (int)nextPosition.getY())) {
			return myPosition.distance(nextPosition) < myPosition.distance(o.getLocation());
		}
		// trying to move invalid; so it doesn't move; so not getting closer.
		return false;
	}
	

	
	public void updateCoordinates(Point2D myPosition, Point2D myPreviousPosition, Set<Observation> whatYouSee){
		this.myPosition = myPosition;
		this.myPreviousPosition = myPreviousPosition;
		this.whatYouSee = whatYouSee;
		
		for (Direction d: Direction.values()){
		//	logger.debug("d: " + d);
			directionDanger.clear();
		}
	}
	
	public void printSurroundingDanger(){
		logger.debug("Here's the danger surrounding the diver at " + myPosition + ":");
		
		for (Direction d : directionDanger.keySet()){
			if (d!= null)
				logger.debug(d.toString() + ": " + directionDanger.get(d));
		}
		
		logger.debug("I want to head in direction " + mySafestDirection + ":");

	}
	
	public Direction findSafestDirection(Point2D myPosition, Point2D myPreviousPosition,
			Set<Observation> whatYouSee, Direction preferredDirection, boolean shouldReturnToBoat){
		updateCoordinates(myPosition, myPreviousPosition, whatYouSee);
		findDanger();
		
		// if we are at boat, reset the tracked helper state
		if (myPosition.equals(new Point2D.Double(0,0))) {
			backtrackLocations.clear();
		}
		
		//logger.debug("in find safest direction");
		double minDanger = Integer.MAX_VALUE;
		
		ArrayList<DirectionAndDanger> directionsSafeToDangerous = new ArrayList<DirectionAndDanger>();
		ArrayList<Direction> safestDirections = new ArrayList<Direction>();
		
		// First, find the minimum danger. Create an equivalence class of directions sharing this value.
		for (Direction d : Direction.values()){
			double curDanger;
			if (directionDanger.containsKey(d)) {
				curDanger = Math.abs(directionDanger.get(d));
				//logger.trace("directionDanger.get("+d+") = "+curDanger);
			} else {
				curDanger = 0;
			}
			
			Point2D nextPosition = new Point2D.Double(
					myPosition.getX() + d.getDx(),
					myPosition.getY() + d.getDy());
			// Do not consider directions that take us too close to the walls
			if (ourBoard.isNearBoundary(nextPosition, Math.max(WALL_MAX_DISTANCE, (double)r / Math.sqrt(2.0) - 1.0))) {
				continue;
			}
			
			// Do not consider directions that put us in backtracked locations
			// i.e. we are trying to go around danger, so don't go backwards
			if (backtrackLocations.contains(nextPosition)) {
				continue;
			}
			
//			logger.debug("current danger in direction " + d + ":" + curDanger);
			
			if (curDanger < minDanger) {
				safestDirections.clear();
				safestDirections.add(d);
				//logger.debug("Min Danger so far in Direction: " + d);
				
				minDanger = curDanger;
			}
			else if (curDanger == minDanger) {
				safestDirections.add(d);
			}

			
			directionsSafeToDangerous.add(new DirectionAndDanger(d, curDanger));
		} 
		
		// Sort from least dangerous to most dangerous
		Collections.sort(directionsSafeToDangerous);
		
		mySafestDirection = null;
		if (shouldReturnToBoat) {
			// If returning to boat, always head in preferredDirection if it is among the safest
			if (preferredDirection == null) {
				mySafestDirection = null;
			} else if (safestDirections.contains(preferredDirection)) {
				mySafestDirection = preferredDirection;
			} else {
				// Prioritize the directions closer to the safest
				// And de-prioritize the previous location
				Direction previousDirection = OurBoard.getDirectionTowards(myPosition, myPreviousPosition);
				for (Direction d : DirectionUtil.getClosestDirections(preferredDirection)) {
					if (safestDirections.contains(d) && !d.equals(previousDirection)) {
						mySafestDirection = d;
						break;
					}
				}
				// We excluded previous direction in above loop.
				// If mySafestDirection == null, that means safest is previous direction.
				if (mySafestDirection == null) {
					if (!directionsSafeToDangerous.isEmpty()) {
						mySafestDirection = directionsSafeToDangerous.get(0).getDirection();
					} else {
						// just go back towards boat...
						mySafestDirection = OurBoard.getDirectionTowards(myPosition, new Point2D.Double(0,0));
					}
				}
				
				// we did not pick preferredDirection, so next time make sure we don't come back
				backtrackLocations.add(myPreviousPosition);
			}
		} else {
			// 80% of the time, continue in preferredDirection if it is among the safest

			if (preferredDirection != null && safestDirections.contains(preferredDirection) && random.nextDouble() <= 0.80) {

				mySafestDirection = preferredDirection;
			} else {
				List<Direction> closestDirections = DirectionUtil.getClosestDirections(preferredDirection);
				// Try to pick the safest direction out of [forward-left, forward-right, left, right]
				List<Direction> firstTwo = closestDirections.subList(0, 2);
				List<Direction> nextTwo = closestDirections.subList(2, 4);
				// Look at the top 3 safest directions
				if (directionsSafeToDangerous.size() >= 3) {
					for (DirectionAndDanger dad : directionsSafeToDangerous.subList(0, 3)) {
						// skip the one going backwards
						if (dad.equals(OurBoard.getDirectionTowards(myPosition, myPreviousPosition))) {
							continue;
						}
						if (firstTwo.contains(dad.getDirection())) {
							mySafestDirection = dad.getDirection();
							break;
						}
						else if (nextTwo.contains(dad.getDirection())) {
							mySafestDirection = dad.getDirection();
							break;
						}
					}
				}
				if (mySafestDirection == null) {
					if (!safestDirections.isEmpty()) {
						Collections.shuffle(safestDirections);
						mySafestDirection = safestDirections.get(0);
					} else {
						// no safe directions?? return to boat then...
						mySafestDirection = OurBoard.getDirectionTowards(myPosition, new Point2D.Double(0,0));
					}
				}
				
				// we did not pick preferredDirection, so next time make sure we don't come back
				backtrackLocations.add(myPreviousPosition);
			}
		}
		
		// if we end up going in preferredDirection, no need to avoid backtrack areas anymore
		backtrackLocations.clear();
		
		//logger.debug("Safest direction: " + mySafestDirection + " (from among " + safestDirections.toString() + ")");
		return mySafestDirection;
	}
	
	public Double getDangerInDirection(Direction direction) {
		if (directionDanger.containsKey(direction)){
			return directionDanger.get(direction);
		}
		else
			return 0.0;
	}
}
