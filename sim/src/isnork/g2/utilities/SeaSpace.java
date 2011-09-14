package isnork.g2.utilities;

import isnork.sim.SeaLifePrototype;
import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;

/** Class to represent a space on the board! */
public class SeaSpace {

	public SeaSpace(Point2D p) {
		location = p;
		center = new Point2D.Double(p.getX() + .5, p.getY() + .5);
		occupiedby = new ArrayList<EachSeaCreature>();
		log = Logger.getLogger(this.getClass());
	}

	public Point2D getPoint() {
		return location;
	}

	public void set(ArrayList<EachSeaCreature> occupiedby, int r) {
		roundset = r;
		this.occupiedby = occupiedby;

	}

	public ArrayList<EachSeaCreature> getOccupiedby() {
		return occupiedby;
	}

	public boolean isoccupideby(int id) {

		for (EachSeaCreature s : occupiedby) {
			if (s.getId() == id)
				return true;
		}

		return false;
	}

	public void remove(int id) {

		EachSeaCreature temp = null;
		for (EachSeaCreature s : occupiedby) {
			if (s.getId() == id)
				temp = s;
		}

		occupiedby.remove(temp);
	}

	public void addCreature(EachSeaCreature c, int r, Point2D l, boolean b) {
		c.setLastSeen(r, l);
		c.seen = true;
		occupiedby.add(c);
	}

	/** Tells us if there is a dangerous creature on this space */
	public Boolean hasDanger() {
		for (EachSeaCreature o : occupiedby) {
			// if(o.returnCreture().isDangerous() && o.getLastseen() == r){
			if (o.returnCreature().isDangerous()) {
				log.trace("Danger from: " + o.getId() + " on space: "
						+ this.location);
				return true;
			}
		}
		return false;
	}

	/** Returns the direction from the diver to the creature */
	public ArrayList<Direction> getDirection(Point2D me) {

		log.trace("i am on: " + me);
		ArrayList<Direction> temp = new ArrayList<Direction>();

		if (me.getX() == location.getX() && me.getY() > location.getY()) {
			temp.add(Direction.N);
			temp.add(Direction.NE);
			temp.add(Direction.NW);
		}

		if (me.getX() == location.getX() && me.getY() < location.getY()) {
			temp.add(Direction.S);
			temp.add(Direction.SE);
			temp.add(Direction.SW);
		}

		if (me.getX() > location.getX() && me.getY() == location.getY()) {
			temp.add(Direction.W);
			temp.add(Direction.NW);
			temp.add(Direction.SW);
		}
		if (me.getX() < location.getX() && me.getY() == location.getY()) {
			temp.add(Direction.E);
			temp.add(Direction.NE);
			temp.add(Direction.SE);
		}
		if (me.getX() < location.getX() && me.getY() > location.getY()) {
			temp.add(Direction.NE);
			temp.add(Direction.N);
			temp.add(Direction.E);
		}
		if (me.getX() < location.getX() && me.getY() < location.getY()) {
			temp.add(Direction.SE);
			temp.add(Direction.S);
			temp.add(Direction.E);
		}
		if (me.getX() > location.getX() && me.getY() > location.getY()) {
			temp.add(Direction.NW);
			temp.add(Direction.N);
			temp.add(Direction.W);
		}
		if (me.getX() > location.getX() && me.getY() < location.getY()) {
			temp.add(Direction.SW);
			temp.add(Direction.S);
			temp.add(Direction.W);
		}
		return temp;
	}

	public Point2D getCenter() {
		return center;
	}

	public EachSeaCreature getCreature(int id) {
		for (int i = 0; i < occupiedby.size(); i++) {
			if (occupiedby.get(i).getId() == id)
				return occupiedby.get(i);
		}
		return null;
	}

	public boolean isoccupideby(SeaLifePrototype s) {

		for (EachSeaCreature e : occupiedby) {
			if (e.returnCreature() == s)
				return true;
		}

		return false;
	}

	public boolean isoccupideby(SeaCreatureType seaCreatureType) {

		for (EachSeaCreature e : occupiedby) {
			if (seaCreatureType.returnCreature() == e.returnCreature())
				return true;
		}

		return false;
	}

	public void addCreature(SeaCreatureType seaCreatureType) {
		occupiedby.add(new EachSeaCreature(seaCreatureType, this.location
				.getX(), this.location.getY()));
	}

	public ArrayList<EachSeaCreature> getUnseenCreatures() {

		ArrayList<EachSeaCreature> unseen = new ArrayList<EachSeaCreature>();

		for (EachSeaCreature e : occupiedby) {
			if (!e.seen)
				unseen.add(e);
		}

		return unseen;
	}

	private ArrayList<EachSeaCreature> occupiedby;
	private int roundset;
	private Point2D location, center;
	private Logger log;

}
