package isnork.g2.utilities;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import isnork.sim.SeaLife;
import isnork.sim.SeaLifePrototype;
import isnork.sim.GameObject.Direction;

/**Represents a sea creature!*/
public class EachSeaCreature{
	
	private int numTimesSeen= 0;
	private SeaLifePrototype seaCreature;
	private int id = 1000;
	public int lastseen = 0;
	public Point2D location;
	public Direction direction;
	public boolean seen = false;
	
	public EachSeaCreature()
	{
		
	}
	
	public EachSeaCreature(SeaLifePrototype s, double x, double y){
		seaCreature = s;
		location = new Point2D.Double(x, y);
	} 
	
	public EachSeaCreature(SeaLifePrototype p, int id2, double x, double y) {
		seaCreature = p;
		this.id = id2;
		location = new Point2D.Double(x, y);

	}

	public EachSeaCreature(SeaLifePrototype p, int id2, int r, boolean b, double x, double y) {
		seaCreature = p;
		this.id = id2;
		lastseen = r;
		seen = b;
		location = new Point2D.Double(x, y);

	}

	public EachSeaCreature(SeaCreatureType seaCreatureType, double x, double y) {
		seaCreature = seaCreatureType.returnCreature();
		location = new Point2D.Double(x, y);
	}

	public int getNumTimesSeen() {
		return numTimesSeen;
	}

	public void setNumTimesSeen(int numTimesSeen) {
		this.numTimesSeen = numTimesSeen;
	}
	
	public SeaLifePrototype returnCreature(){
		return seaCreature;
	}
	
	public void setId(int i){
		id = i;
	}
	
	public int getId(){
		return id;
	}
	
	public int getLastseen(){
		return lastseen;
	}
	
	public void setLastSeen(int r, Point2D l) {
		lastseen = r;
		location = l;
	} 
	
	/**
	 * Compares SeaCreatures by id, that's the only way to determine that they are the same creature
	 */
	public boolean equals(Object o)
	{
		if(o.getClass() != this.getClass())
			return false;
		
		if(this.id == ((EachSeaCreature)o).id)
			return true;
		
		return false;
	}
}
