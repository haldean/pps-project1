package isnork.g3;

import isnork.sim.GameEngine;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Communication {
	private int d, r, n, id;
	PrintMessages myPrintMessages;
	private Creature creaturePursuing = null;
	//private int numCreaturesWithHighValue = 0;
	private Set<Creature> setOfCreatureObjects;
	//private boolean hasDangerousCreaturesThatMove = false;
	
	public Communication(int d, int r, int n, int id)
	{
		this.d = d;
		this.r = r;
		this.n = n;
		this.id = id;
		myPrintMessages = new PrintMessages(false, 100, id);
	}

	public CreatureLocation findDestinationPointBasedOnIncomingMessages(ArrayList<ObservedCreature> myObservedCreatures, Point2D whereIAm)
	{
		double maxValue = 0;
		Creature creatureToGoAfter = null;
		int idOfCreatureToGoAfter = -2000;
		Point2D destinationPoint = new Point2D.Double(-99999, -99999); //The point with -99999 as both its x and y coordinates is a sentinel value that indicates that there is no destination point
		for(int i = 0; i < myObservedCreatures.size(); i++)
		{
			ObservedCreature myObservedCreature = myObservedCreatures.get(i);
			double distance = whereIAm.distance(myObservedCreature.location);
			double score = getMyScoreForCreature(myObservedCreature); 
			double value = Math.pow(score, 1.5)/distance;
			if(value > maxValue && timeToDest(myObservedCreature.location, whereIAm) < 200) //Just picked 50 randomly. Try changing this. 
			{
				maxValue = value;
				creatureToGoAfter = getCreatureFromObservation(myObservedCreature);
				destinationPoint = myObservedCreature.location;
				idOfCreatureToGoAfter = myObservedCreature.id;
			}
		}
		if(destinationPoint.getX() != -99999) // maybe add in  "&& id % 3 != 1" so that one out of every three snorklers will not chase creatures and stay on their phalanx path? 
		{
			creaturePursuing = creatureToGoAfter;
			return new CreatureLocation(creatureToGoAfter.getName(), destinationPoint, idOfCreatureToGoAfter);
		}
		else
		{
			return new CreatureLocation(null, destinationPoint, idOfCreatureToGoAfter);
		}
		//if(id % 3 != 1)
		
		//return new Point2D.Double(-99999, -99999);
	}
	
	public double getMyScoreForCreature(ObservedCreature myObservedCreature)
	{
		Iterator<Creature> iter = setOfCreatureObjects.iterator();
		while(iter.hasNext())
		{
			Creature myCreature= iter.next();
			String name = myCreature.getName();
			if(name.equals(myObservedCreature.slp.getName()))
			{
				double score = myCreature.getScore(myObservedCreature.id, id);
				//GameEngine.println(id + "Score of : " + name + " for me is " + score);
				return score;
			}
		}
		return 0;
	}
	
	public Creature getCreatureFromObservation(ObservedCreature myObservedCreature)
	{
		Iterator<Creature> iter = setOfCreatureObjects.iterator();
		while(iter.hasNext())
		{
			Creature myCreature= iter.next();
			String name = myCreature.getName();
			if(name.equals(myObservedCreature.slp.getName()))
			{
				return myCreature;
			}
		}
		GameEngine.println("ERROR: SHOULD NOT REACH HERE. IN getCreatureFromObservation() in Communication class");
		return null;
	}
	
	public double timeToDest(Point2D dest, Point2D whereIAm)
	{
		// minimal time to destination
		double x1=whereIAm.getX();
		double y1=whereIAm.getY();
		double x2=dest.getX();
		double y2=dest.getY();
		double deltaX=Math.abs(x1-x2);
		double deltaY=Math.abs(y1-y2);
		double diagonal=Math.min(deltaX, deltaY);
		double orthogonal=Math.abs(deltaX-deltaY);
		return diagonal*3+orthogonal*2;
	}
	
	public void setCreaturePursuingToNull()
	{
		creaturePursuing = null;
	}
	
	public Creature getCreaturePursuing()
	{
		return creaturePursuing;
	}
	
	public void createCreatureObjects(Set<SeaLifePrototype> seaLife)
	{
		setOfCreatureObjects = new HashSet<Creature>();
		Iterator<SeaLifePrototype> iter = seaLife.iterator();
		while(iter.hasNext())
		{
			SeaLifePrototype myPrototype = iter.next();
			setOfCreatureObjects.add(new Creature(myPrototype));
			/*if(myPrototype.getHappiness() > 10)
			{
				numCreaturesWithHighValue += (myPrototype.getMaxCount() + myPrototype.getMinCount())/2;
			}*/
			/*if(myPrototype.isDangerous() && myPrototype.getSpeed() == 1)
				hasDangerousCreaturesThatMove = true; */
		}
	}
	
	/*public boolean doesBoardContainDangerousCreaturesThatMove()
	{
		return hasDangerousCreaturesThatMove;
	}*/
	
	/*public int getNumCreaturesWithHighValue()
	{
		return numCreaturesWithHighValue;
	}*/
	
	public void sawCreature(Observation myObs)
	{
		boolean foundCreature = false;
		Iterator<Creature> iter = setOfCreatureObjects.iterator();
		while(iter.hasNext())
		{
			Creature myCreature= iter.next();
			String name = myCreature.getName();
			if(name.equals(myObs.getName()))
			{
				myCreature.sawCreature(myObs.getId());
				//myPrintMessages.print("Saw a " + myObs.getName());
				foundCreature = true;
			}
		}
		if(foundCreature == false)
		{
			myPrintMessages.print("ERROR: SHOULD NOT GET HERE B/C SHOULD HAVE FOUND OBSERVATION");
		}
	}
	
}
