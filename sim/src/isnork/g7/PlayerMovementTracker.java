package isnork.g7;

import isnork.sim.Observation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class PlayerMovementTracker {
	
	private Point2D myOriginialPosition;
	private LinkedList<pLocation> locations;
	private int round;
	public PlayerMovementTracker(Point2D myPosition, Set<Observation> pLocations)
	{
		locations = new LinkedList<pLocation>();
		setMyOriginialPosition(myPosition);
		for(Observation o : pLocations)
		{
			locations.add(new pLocation(o.getId(),o.getLocation().distance(myPosition)));
		}
		round = 0;
	}
	
	public void update(Point2D myPosition, Set<Observation> pLocations)
	{
		++round;
		if(round%15!=0)
			return;
		
		Iterator<pLocation> it = locations.iterator();
		while(it.hasNext())
		{
			pLocation pl = it.next();
			for(Observation o: pLocations)
			{
				if(o.getId()!=pl.getId())
					continue;
				if(pl.getDistance() < myPosition.distance(o.getLocation()))
					it.remove();
			}
		}
	}
	
	public boolean continueChase()
	{
		if(locations.size() ==0)
			return false;
		else
			return true;
	}
	
	public void setMyOriginialPosition(Point2D myOriginialPosition) {
		this.myOriginialPosition = myOriginialPosition;
	}

	public Point2D getMyOriginialPosition() {
		return myOriginialPosition;
	}

	private class pLocation
	{
		private int id;
		private double distance;
		public pLocation(int i, double d)
		{
			setId(i);
			setDistance(d);
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getId() {
			return id;
		}
		public void setDistance(double distance) {
			this.distance = distance;
		}
		public double getDistance() {
			return distance;
		}
	}
}
