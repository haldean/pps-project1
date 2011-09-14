package isnork.g3;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import isnork.sim.GameEngine;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

public class Creature {
	private SeaLifePrototype seaLifePrototype;
	private int numTimesSeen;
	private ArrayList<SpecificCreatureWithUniqueId> creaturesSeen;
	PrintMessages myPrintMessages = new PrintMessages(false, 100, 100);
	public Creature(SeaLifePrototype seaLifePrototype)
	{
		this.seaLifePrototype = seaLifePrototype;
		this.numTimesSeen = 0;
		this.creaturesSeen = new ArrayList<SpecificCreatureWithUniqueId>();
	}
	
	public ArrayList<SpecificCreatureWithUniqueId> getCreaturesSeen()
	{
		return creaturesSeen;
	}
	
	public void sawCreature(int creatureId)
	{
		sawCreature(creatureId, 0);
	}
	
	public void sawCreature(int creatureId, int tickNumber)
	{
		if(contains(creatureId) == false)
		{
			numTimesSeen++;
			creaturesSeen.add(new SpecificCreatureWithUniqueId(creatureId, tickNumber));
		}
		else
		{
			if(getSeeingTimes(creatureId) != null)
			{
				SpecificCreatureWithUniqueId mySpecificCreature = getSeeingTimes(creatureId);
				mySpecificCreature.addSeeingTime(tickNumber);
			}
			else
			{
				myPrintMessages.print("Error: SeeingCreatureTime was null");
			}
		}
	}
	
	public boolean contains(int creatureId)
	{
		for(int i = 0; i < creaturesSeen.size(); i++)
		{
			if(compareIds(creaturesSeen.get(i).getCreatureID(), creatureId, seaLifePrototype)) // use Eva's function 
			{
				return true;
			}
		}
		return false;
	}
	
	
	boolean compareIds(int id1, int id2, SeaLifePrototype slp) {
		if ((id1 % slp.getMaxCount()) == (id2 % slp.getMaxCount()))
			return true;
		return false;
	}
	
	//returns a null object if haven't seen this particular creature 
	public SpecificCreatureWithUniqueId getSeeingTimes(int creatureId)
	{
		for(int i = 0; i < creaturesSeen.size(); i++)
		{
			if(compareIds(creaturesSeen.get(i).getCreatureID(), creatureId, seaLifePrototype))
			{
				return creaturesSeen.get(i);
			}
		}
		return null;
	}
		
	public String getName()
	{
		return seaLifePrototype.getName();
	}
	
	//Change this to take into account IDs 
	public double getScore(int creatureId, int snorklerId)
	{
		//if(snorklerId == -3)
			//GameEngine.println(snorklerId + " the creatureId is: " + creatureId);
		if(numTimesSeen == 0)
		{
			return seaLifePrototype.getHappiness();
		}
		
		if(this.contains(creatureId))
		{
			//Already seen this creature, don't go for it. 
			//GameEngine.println("   Already seen this creature, don't go for it");
			return 0.0;
		}
		else
		{
			if(numTimesSeen == 1)
			{
				//GameEngine.println("   Seen this creature (" + seaLifePrototype.getName() + " once, now worth half");
				return .5*seaLifePrototype.getHappiness();
			}
			else if(numTimesSeen == 2)
			{
				//GameEngine.println("   Seen this creature (" + seaLifePrototype.getName() + " twice, now worth one fourth");
				return .25*seaLifePrototype.getHappiness();
			}
			else //if numTimesSeen >2
			{
				return 0.0;
			}
		}
	}
	
	public double getTotalScoreFromThisCreature()
	{
		if(numTimesSeen == 0)
			return 0;
		else if(numTimesSeen == 1)
			return seaLifePrototype.getHappinessD();
		else if(numTimesSeen == 2)
			return 1.5*seaLifePrototype.getHappinessD();
		else if(numTimesSeen >= 3)
			return 1.75*seaLifePrototype.getHappinessD();
		GameEngine.println("ERROR: SHOULD NOT REACH HERE. This is in the getTotalScoreFromThisCreature() fcn in Creature.java");
		return 0; // shouldn't reach here
	}
	
	public int getNumTimesSeen()
	{
		return numTimesSeen;
	}
}
