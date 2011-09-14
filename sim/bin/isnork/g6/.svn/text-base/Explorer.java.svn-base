package isnork.g6;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
import isnork.sim.Observation;
import isnork.sim.GameObject.Direction;

public class Explorer {
	
	Board board;
	Direction newDirection, bestDirection = Direction.E;
	Direction desiredDirection = null;
	
	int d;
	int r;
	private Logger log = Logger.getLogger(this.getClass());
	
	public Direction getNewDirection(Board board, Point2D myPosition, int d, int r) {
		
		this.board = board;
		Direction dir = null;
		this.d = d;
		this.r = r;
		dir = pickBestDirection(myPosition);
		return dir;
	}
	
	public static Direction convertIntToDirection(int dir) {
		Direction d = null;
		
		switch(dir) {
		
		case 0 : d = Direction.E;
					break;
		case 1 : d = Direction.N;
					break;
		case 2:d = Direction.NE;
					break;
		case 3:d = Direction.NW;
					break;
		case 4 :d = Direction.S;
					break;
		case 5:d = Direction.SE;
					break;
		case 6:d = Direction.SW;
					break;
		case 7:d = Direction.W;
					break;
		case 8 : d = null; //convert 8 to null
					break;
		default: d = Direction.N;
						
		}
		return d;
	}
	
	public static int convertDirectionToInt(Direction dir) {
		int d = 0;
		if( dir == null) { //convert null to 8
			d = 8;
		}else {
			switch(dir) {

			case E : d = 0;
			break;
			case N : d = 1;
			break;
			case NE:d = 2;
			break;
			case NW :d = 3;
			break;
			case S :d = 4;
			break;
			case SE:d = 5;
			break;
			case SW:d = 6;
			break;
			case W:d = 7;
			break;
			}
		}
		return d;
	}
	
	/*find all unexplored directions from your current position
	 * There has to be at least 1 since the control wont reach here
	 * otherwise. (There is a check for overExplored somewhere else)
	 * 
	 * Lesson learnt : maximizing the distance will mean they get spread out,
	 * but they will not have incentive to come back together once this happens.
	 * 
	 * This function might still be buggy.
	 */
	Direction pickBestDirection(Point2D currentPosition) {
		Set<Direction> possibleDirections = new HashSet<Direction>();
		Set<Direction> bestDirections = new HashSet<Direction>();
		Set<Observation> playerLocations = board.currentPlayerLocations;
		int x = (int)currentPosition.getX(); 
		int y = (int)currentPosition.getY();
		int distance = 0, maxDistance = 0;
		int flag = 0;
		
		//find unexplored directions.
		//include code here to explore old cells again.
		if(!Utilities.onBoundary(new Point2D.Double(x,y), r, d)) //no need to go beyond this.
		 {
			for(int j = 0; j < 8; j++) {
				newDirection = convertIntToDirection(j);
				int newx = x + newDirection.dx;
				int newy = y + newDirection.dy;
				int boardx = newx + d, boardy = newy + d;
				int age = InitialPlayer.getCurrentTime() - board.matrix[boardx][boardy].lastVisitedTime;
				//log.info("the direction of the cell : " + newDirection + " the age of the cell is " + age);
				if(board.matrix[boardx][boardy].explorationStatus == ExplorationStatus.unexplored || age > d ) { //might have to vary based on experiments
					possibleDirections.add(newDirection);
				}
				//age creates a problem when we are close to the boundary. It will mark all directions as impossible
			}
			
		 } else { //if one utility is on boundary then pick the ones that are on the boundary.
			 for(int j = 0; j < 8; j++) {
					newDirection = convertIntToDirection(j);
					int newx = x + newDirection.dx;
					int newy = y + newDirection.dy;
					if(!Utilities.onBoundary(new Point2D.Double(newx, newy), r, d) ) {
						possibleDirections.add(newDirection);
					}
					//age creates a problem when we are close to the boundary. It will mark all directions as impossible
				} 
		 }
		
		log.info("number of possible directions " + possibleDirections.size());
		//choose the best from these directions. If there is more than 1, pick randomly?
		//best => max distance from everyone else.
		for(Direction dir : possibleDirections) {
			int newx = x + dir.dx;
			int newy = y + dir.dy;
			int boardx = newx, boardy = newy;
			distance = 0;
			Point2D newPosition = new Point2D.Double(boardx, boardy);
			for(Observation o : playerLocations) {
				Point2D playerPosition = o.getLocation();
				distance += (int) Utilities.distance(newPosition, playerPosition); //add up all the distances.
			}
			if(maxDistance < distance) {
				maxDistance = distance;
				log.info("max distance " + maxDistance);
			}
			
			//take care of mindistance = 0 also separately so that there is incentive to not be
			//on same cell.
		}
		
		//choose all with max. 
		for(Direction dir : possibleDirections) {
			int newx = x + dir.dx;
			int newy = y + dir.dy;
			int boardx = newx, boardy = newy;
			int totalDistance = 0;
			Point2D newPosition = new Point2D.Double(boardx, boardy);
			for(Observation o : playerLocations) {
				Point2D playerPosition = o.getLocation();
				distance = (int) Utilities.distance(newPosition, playerPosition); //add up all the distances
				totalDistance += distance;
				if(distance == 0) {
					flag = 1; //if the player is going to move to a place thats occupied by another player
					log.info("setting flag to 1 ");
				}
				distance = (int) Utilities.distance(currentPosition, playerPosition);
				if(distance == 0) {
					flag = 1; // players are already sharing the same space
				}
			}
			if(maxDistance == totalDistance) {
				bestDirections.add(dir);
			}
		}
		int Min = 0, Max = bestDirections.size() - 1;
		int a = Min + (int)(Math.random() * ((Max - Min) + 1));
		int count = 0;
		for(Direction d : bestDirections) {
			if(count == a) {
				bestDirection = d;
			}
			count++;
		}
		if( flag == 1) { //there is at least one other player in the same position as this one.
			if(bestDirections.size() == 1) {
				log.info("best Direction size = " + bestDirections.size() + " picked direction " + a);
				log.info("possible direction size = " + possibleDirections.size());
				Min = 0;
				Max = possibleDirections.size() - 1;
				a = Min + (int)(Math.random() * ((Max - Min) + 1));
				for(Direction d : possibleDirections) {
					if(count == a) {
						bestDirection = d;
					}
					count++;
				}				
			}
		}
		return bestDirection;
	}
}
