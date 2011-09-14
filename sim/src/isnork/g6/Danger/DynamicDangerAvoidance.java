package isnork.g6.Danger;

import isnork.g6.Explorer;
import isnork.g6.Utilities;
import isnork.sim.Observation;
import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class DynamicDangerAvoidance {

	private Logger log = Logger.getLogger(this.getClass());
	
	Direction  avoidImmediateDanger(Point2D whereIAm, Set<Observation> whatISee, Direction dir, int dimension) {
		Direction direc = dir;
		int currentx = (int) whereIAm.getX();
		int currenty = (int) whereIAm.getY();
		//find point to which you are going to move.
		if(dir == null) 
			return Direction.E;
		
		int newx = currentx + dir.dx;
		int newy = currenty + dir.dy;
		
		int observedx, observedy;
		boolean directions[] = new boolean[8]; //unsafe direction
		
		int dangerdistance = 0, maxDangerDistance = 0;
		//if present move causes danger, try all directions, until a safe one is found.

		for(int i = 0; i < 8; i++) {
			Direction trialDirection = Explorer.convertIntToDirection(i);
			newx = currentx + trialDirection.dx;
			newy = currenty + trialDirection.dy;
			for(Observation o : whatISee) {
				if(!o.isDangerous()) 
					continue;
				observedx = (int) o.getLocation().getX();
				observedy = (int) o.getLocation().getY();
				if(o.getDirection() != null) {
					observedx = observedx + o.getDirection().dx; //find this thing's would be location
					observedy = observedy + o.getDirection().dy;
				}
				double d = Utilities.distance(new Point2D.Double(observedx,observedy),new Point2D.Double(newx,newy));
				if(d - Math.floor(d) > 0.5) {
					dangerdistance = (int) Math.ceil(d);
				}else {
					dangerdistance = (int) Math.floor(d);
				}
				if(dangerdistance < 5) {
					log.info(i + "direction is marked as unsafe");
					directions[i] = true;
				}
				//out of the board is unsafe. Not a perfect fix, though. 
				if(observedx + dimension > 2*dimension || observedy + dimension > 2*dimension || observedx + dimension < 0 || observedy + dimension < 0) {
					directions[i] = true;
				}
			}
		}

		Set<Direction> safeDirections = new HashSet<Direction>();
		for(int i = 0; i < 8; i++) {
			if(directions[i] == false) {
				direc = Explorer.convertIntToDirection(i);
				safeDirections.add(direc);
				log.info(" a safe direction " + i);
			}
		}
		
		if(safeDirections.size() == 0) {
			log.info("no safe directions");
			log.info("number of safe directions" + safeDirections.size());
			//go through all directions again, choose one that maximizes min distance to a dangerous creature.
			direc = bestUnsafeDirection(whereIAm, whatISee, dir, dimension);
			
		} else {
			log.info("number of safe directions" + safeDirections.size());
			int Min = 0, Max = safeDirections.size() - 1;
			int a = Min + (int)(Math.random() * ((Max - Min) + 1));
			int count = 0;
			for(Direction d : safeDirections) {
				if(count == a) {
					direc = d;
				}
				count++;
			}
		}
		return direc;
	}
	
	/* dont have anything safe according to calculations, so you gotta choose something that 
	 * you think will work best. This means choosing max distance in most cases. However, if you choose
	 * a direction thats exactly same as the direction of movement of danger, and distance between the two
	 * of you is 1, you are in trouble. So in this particular case, choose perpendicular, even though 
	 * that looks a little suboptimal. */
	
	public Direction bestUnsafeDirection(Point2D whereIAm, Set<Observation> whatISee, Direction dir, int dimension) {
		int observedx, observedy;
		int newx, newy;
		int currentx = (int) whereIAm.getX();
		int currenty = (int) whereIAm.getY();
		int dangerdistance = 0, maxDangerDistance = 0;
		Direction bestpossibleDirection = Direction.E;
		int minDangerDistance = 9999;
		
		//if nothing is safe, and you are on the boat, stay put. 
		if(whereIAm.getX() == 0 && whereIAm.getY() == 0) {
			log.info(" Trying to return null for direction....... ");
			return null;
		}
		
		//try all directions.
		for(int i = 0; i < 8; i++) {
			Direction trialDirection = Explorer.convertIntToDirection(i);
			newx = currentx + trialDirection.dx;
			newy = currenty + trialDirection.dy;
			
			//if any one of them is home, go there. 
			if(newx == 0 && newy == 0) {
				bestpossibleDirection = trialDirection;
				break;
			}
			
			minDangerDistance = 9999;
			if(newx + dimension > 2*dimension || newy + dimension > 2*dimension || newx + dimension < 0 || newy + dimension < 0) {
				continue;
			}
			for(Observation o : whatISee) {
				if(!o.isDangerous()) 
					continue;
				observedx = (int) o.getLocation().getX();
				observedy = (int) o.getLocation().getY();
				if(o.getDirection() != null) {
					observedx = observedx + o.getDirection().dx; //find this thing's would be location
					observedy = observedy + o.getDirection().dy;
				}
				
				double d = Utilities.distance(new Point2D.Double(observedx,observedy),new Point2D.Double(newx,newy));
				if(d - Math.floor(d) > 0.5) {
					dangerdistance = (int) Math.ceil(d);
				}else {
					dangerdistance = (int) Math.floor(d);
				}
				log.info("danger distance" + dangerdistance);
				if(minDangerDistance > dangerdistance) {
					minDangerDistance = dangerdistance;
				}
			}
			log.info("direction " + i + "min danger distance" + minDangerDistance);
			//min danger distance has the min distance to danger for that direction. 
			//find the direction that maximizes this.
			if(maxDangerDistance < minDangerDistance && minDangerDistance < 99) {
				maxDangerDistance = minDangerDistance;
				bestpossibleDirection = trialDirection;
			}
		}
		log.info("max danger distance" + maxDangerDistance + "direction chosen " + Explorer.convertDirectionToInt(bestpossibleDirection));
		return bestpossibleDirection;
	}
}
