package isnork.g3;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class SpecificCreatureWithUniqueId {

	private int creatureId;
	
	private ArrayList<Integer> mySeeingTimes;
	
	public SpecificCreatureWithUniqueId(int creatureId, int tickNumber)
	{
		this.creatureId = creatureId;
		mySeeingTimes = new ArrayList<Integer>();
		mySeeingTimes.add(tickNumber);
	}
	public int getCreatureID()
	{
		return creatureId;
	}
	
	public void addSeeingTime(int tickNumber)
	{
		if(!mySeeingTimes.contains(new Integer(tickNumber)))
		{
			mySeeingTimes.add(new Integer(tickNumber));
		}
	}
	
	public ArrayList<Integer> getSeeingTimes()
	{
		return mySeeingTimes;
	}
	
	
	
}
