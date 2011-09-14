package isnork.g6.Danger;

import isnork.g6.Explorer;
import isnork.g6.Utilities;
import isnork.sim.Observation;
import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class StaticDangerAvoidance {
	
	private Logger log = Logger.getLogger(this.getClass());

	Direction  avoidImmediateDanger(Point2D whereIAm, Set<Observation> whatISee, Direction dir, int dimension) {
		Direction direc = null;
		int currentx = (int) whereIAm.getX();
		int currenty = (int) whereIAm.getY();
		//find point to which you are going to move.
		if(dir == null) 
			return Direction.E;
		int newx = currentx + dir.dx;
		int newy = currenty + dir.dy;
		
		int observedx, observedy;
		boolean directions[] = new boolean[8]; //unsafe direction
		boolean danger = false;
		
		int dangerdistance = 0, maxDangerDistance = 0;
		Direction bestpossibleDirection = Direction.E;
		int minDangerDistance = 9999;
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
				double distance = Utilities.distance(new Point2D.Double(observedx,observedy),new Point2D.Double(newx,newy));
				if((distance - (int) distance) > 0.5) {
					dangerdistance = (int) Math.ceil(distance);
				} else {
					dangerdistance = (int) Math.floor(distance);
				}
				if(dangerdistance < 2) {
					log.info(i + "direction is marked as unsafe");
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
		/* dont have anything safe according to calculations 
		 * We may not need this for static danger. If there is no safe direction, we cant avoid danger. */
		if(safeDirections.size() == 0) {
			log.info("no safe directions");
			log.info("number of safe directions" + safeDirections.size());
			//go through all directions again, choose one that maximizes min distance to a dangerous creature.
			for(int i = 0; i < 8; i++) {
				Direction trialDirection = Explorer.convertIntToDirection(i);
				newx = currentx + trialDirection.dx;
				newy = currenty + trialDirection.dy;
				minDangerDistance = 9999;
				for(Observation o : whatISee) {
					if(!o.isDangerous()) 
						continue;
					observedx = (int) o.getLocation().getX();
					observedy = (int) o.getLocation().getY();
					if(o.getDirection() != null) {
						observedx = observedx + o.getDirection().dx; //find this thing's would be location
						observedy = observedy + o.getDirection().dy;
					}
					dangerdistance = (int) Utilities.distance(new Point2D.Double(observedx,observedy),new Point2D.Double(newx,newy));
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
			direc = bestpossibleDirection;
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
}
