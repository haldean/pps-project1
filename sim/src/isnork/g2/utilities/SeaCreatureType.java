package isnork.g2.utilities;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import isnork.sim.SeaLife;
import isnork.sim.SeaLifePrototype;
import isnork.sim.GameObject.Direction;

/**Represents a sea creature!*/
public class SeaCreatureType implements Comparable<SeaCreatureType> {
	
	private int numTimesSeen= 0;
	private SeaLifePrototype seaCreature;
	private int id = 1000;
	private int lastseen = 0;
	
	public Set<Integer> seen = new HashSet<Integer>();
	public Set<Integer> probableSeen = new HashSet<Integer>();
	public ArrayList<IdRoundTracker> seenWithRounds = new ArrayList<IdRoundTracker>(); 
	public int nextHappiness = 0;
	public boolean hasValue = true; 
	public double ranking = 0;
	public int minPossibleHappiness = 0;
	public int maxPossibleHappiness = 0;
	public int avgPossibleHappiness = 0;
	public String isnorkMessage = null;
	public boolean seenOnce = false;
	public int numSeen = 0;
	
	public SeaCreatureType(SeaLifePrototype s){
		seaCreature = s;
		nextHappiness = s.getHappiness();
		seen = new HashSet<Integer>();
	} 
	
	public SeaCreatureType(SeaLifePrototype p, int id2) {
		seaCreature = p;
		this.id = id2;
	}

	public SeaCreatureType(SeaLifePrototype p, int id2, int r) {
		seaCreature = p;
		this.id = id2;
		lastseen = r;
	}
	
	public void addSeenWithRound(int id, int round, int x, int y)
	{
		seenWithRounds.add(new IdRoundTracker(id, round, new Point2D.Double(x, y)));
	}
	
	public void addSeenWithRound(int id, int round, int x, int y, Direction d)
	{
		seenWithRounds.add(new IdRoundTracker(id, round, new Point2D.Double(x, y), d));
	}
	
	public class IdRoundTracker
	{
		Point2D loc;
		int myId;
		int myRound;
		Direction myDir;
		double probNotMe = 0;
		
		public IdRoundTracker(int id, int round, Point2D l, Direction d)
		{
			myId = id;
			myRound = round;
			loc = l;
			myDir = d;
		}
		
		public IdRoundTracker(int id, int round, Point2D l)
		{
			myId = id;
			myRound = round;
			loc = l;
		}
	}
	
	/**
	 * Adds the creature to the list of seen creatures and returns
	 * the amount of happiness gained by the creature.
	 * @param newId
	 * @return
	 */
	public int addSeen(int newId)
	{
		seenOnce = true;
		
		//check that you haven't seen the exact same creature
		if(!this.seen.contains(newId))
		{
			seen.add(newId);
			
			if(hasValue)
			{
				numSeen++;
				
				//if the creature can award points, do so!
				if(seen.size() <= 2)
				{
					int rcvd = nextHappiness;
					nextHappiness = nextHappiness / 2;
					return rcvd;
				}
				else if(seen.size() >= 3)
				{
					int rcvd = nextHappiness;
					nextHappiness = 0;
					hasValue = false;
					return rcvd;
				}
					
			}
		}
		
		return 0;
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
	
	public void setLastSeen(int r) {
		lastseen = r;
	}
	
	/**
	 * Compares SeaCreatures by id, that's the only way to determine that they are the same creature
	 */
	public boolean equals(Object o)
	{
		if(o.getClass() != this.getClass())
			return false;
		
		if(this.id == ((SeaCreatureType)o).id)
			return true;
		
		return false;
	}

	public int compareTo(SeaCreatureType arg0) {
		if(this.ranking > arg0.ranking)
			return 1;
		
		if(this.ranking < arg0.ranking)
			return -1;
		
		return 0;
	}
}
