package isnork.g6.Danger;

import isnork.g6.Explorer;
import isnork.g6.Utilities;
import isnork.sim.Observation;
import isnork.sim.GameObject.Direction;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

enum Danger {NoDanger, StaticDanger, DynamicDanger};

public class DangerAvoidance {

	private Logger log = Logger.getLogger(this.getClass());

	DynamicDangerAvoidance dynamicAvoider = new DynamicDangerAvoidance();
	StaticDangerAvoidance staticAvoider = new StaticDangerAvoidance();
	
	HashMap<String, Danger> mobilityMap = new HashMap<String, Danger>();

	Set<String> dynamicDangers = new HashSet<String>();
	public Direction avoidImmediateDanger(Point2D whereIAm, Set<Observation> whatISee, Direction dir, int dimension) {
		
		if(dir == null) {
			return null;
		}
		Direction finalDirection = dir;
		Danger type = findDangerType(whereIAm, whatISee, dir);
		if( type == Danger.DynamicDanger) {
			finalDirection = dynamicAvoider.avoidImmediateDanger(whereIAm, whatISee, dir, dimension);
		}else if (type == Danger.StaticDanger) {
			finalDirection = staticAvoider.avoidImmediateDanger(whereIAm, whatISee, dir, dimension);
		}
		return finalDirection;
	}
	
	public Danger findDangerType(Point2D whereIAm, Set<Observation> whatISee, Direction dir) {
		
		Danger type = Danger.NoDanger;
		int currentx = (int) whereIAm.getX();
		int currenty = (int) whereIAm.getY();
		//find point to which you are going to move.
		
		int newx = currentx + dir.dx;
		int newy = currenty + dir.dy;
		
		int observedx, observedy;
		int dirindex = 0;
		boolean directions[] = new boolean[8]; //unsafe direction
		boolean danger = false;
		
		//check if present move causes danger.
		for(Observation o : whatISee) {
			if(!o.isDangerous()) {
				continue;
			}
			observedx = (int) o.getLocation().getX();
			observedy = (int) o.getLocation().getY();
			if(o.getDirection() != null) {
				observedx = observedx + o.getDirection().dx; //find this thing's would be location
				observedy = observedy + o.getDirection().dy;
			}
			if(Utilities.distance(new Point2D.Double(observedx,observedy),new Point2D.Double(newx,newy)) < 5) {
				danger = true;
				log.info("DANGER!!! player location " + newx + ", " + newy + o.getName() + observedx + ", " + observedy);
			}
			String name = o.getName().trim();
			if(dynamicDangers.contains(name)) {
				type = Danger.DynamicDanger;
			}
		}
		log.info(" danger = " + danger + " type = " + type);
		if(danger == true && type == Danger.NoDanger) { //no dynamic danger.
			for(Observation o : whatISee) {
				observedx = (int) o.getLocation().getX();
				observedy = (int) o.getLocation().getY();
				if(o.getDirection() != null) {
					observedx = observedx + o.getDirection().dx; //find this thing's would be location
					observedy = observedy + o.getDirection().dy;
				}
				if(Utilities.distance(new Point2D.Double(observedx,observedy),new Point2D.Double(newx,newy)) < 2) {
					type = Danger.StaticDanger;
					log.info("DANGER!!! player location " + newx + ", " + newy + o.getName() + observedx + ", " + observedy);
				}
			}
		} //if danger = false, then its no danger. Also if above condition doesnt work, its again no danger.
		
		log.info("mode chosen is " + type);
		return type;
	}
	
	public void setDangerTypes(Set<SeaLifePrototype> seaLife) {
		for(SeaLifePrototype s : seaLife) {
			int speed = s.getSpeed();
			if(speed > 0) {
				dynamicDangers.add(s.getName().trim());
			}
			log.info(" the name is " + s.getName() + " and spped is " + s.getSpeed());
		}
		for(String s : dynamicDangers) {
			log.info("added strings are" + "\"" + s +"\"" );
		}
	}
}
