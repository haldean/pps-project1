package isnork.g7;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;
import isnork.sim.GameObject.Direction;

public class Tracker {
	private static SeaLifePrototype seaLifePrototype; 
	private static final Logger logger = Logger.getLogger(Tracker.class);
	private int d;
	private int r;
	private HashMap<Point2D,Integer> points;
	
	public Tracker(int _d, int _r)
	{ 
		points = new HashMap<Point2D,Integer>();
		d = _d;
		r = _r;
		initializePoints();
	}
	
	private void initializePoints()
	{
		for(int i = -d; i<=d; i++)
		{
			for(int j = -d;j<=d; j++)
			{
				points.put(new Point2D.Double(i,j), 0);
			}
		}
	}
	
	public void updatePoints(Set<Observation> locations, Point2D myPosition)
	{
		Iterator<Point2D> i = points.keySet().iterator();
		while(i.hasNext())
		{
			Point2D p = i.next();
			if(p.distance(myPosition)<=r)
			{
				i.remove();
			}
		}
		for(Observation o: locations)
		{
			Iterator<Point2D> it = points.keySet().iterator();
			while(it.hasNext())
			{
				Point2D p = it.next();
				if(p.distance(o.getLocation())<=r)
				{
					it.remove();
				}
			}
		}
		if(points.isEmpty())
			initializePoints();
	}
	
	public Direction getClosestUnexplored(Point2D myPosition)
	{
		Point2D best = null;
		for(Point2D p: points.keySet())
		{
			if(best== null)
			{
				best = p;
			}
			else
			{
				if(p.distance(myPosition) < best.distance(myPosition))
					best = p;
			}
		}
		if(best == null)
			return null;
		else
		{
			Direction d =  OurBoard.getDirectionTowards(myPosition, best);
			//logger.debug(d);
			return d;
		}
	}
	
	public static Direction track(Point2D me, Point2D beast)
	{
		if(beast == null)
			return null;
		return OurBoard.getDirectionTowards(me, beast);
	}

	
	public static Direction getRandomDirection()
	{
		int i = (int)(Math.random()*8);
		return Direction.allBut(null).get(i);
	}
	
	public SeaLifePrototype getSeaLifePrototype(){
		return seaLifePrototype;
	}
	
}
