package isnork.g3;

import java.util.ArrayList;

public class WhatEachPersonHasSeen {
	
	private int snorklerId;
	private ArrayList<Creature> listOfCreatures;
	public WhatEachPersonHasSeen(int snorklerId) //Note that this is always a POSITIVE number.  So this is -1* the real snorkler ID 
	{
		this.snorklerId = snorklerId;
		this.listOfCreatures = new ArrayList<Creature>();
	}
	
	public void addToList(Creature c)
	{
		listOfCreatures.add(c);
	}
	
	public ArrayList<Creature> getCreatureList()
	{
		return listOfCreatures;
	}
	
	public int getSnorkelerId()
	{
		return snorklerId;
	}
	
	//Returns null if the snorkeler has not seen this creature 
	public Creature getCreature(String name)
	{
		for(int i = 0; i < listOfCreatures.size(); i++)
		{
			Creature myCreature = listOfCreatures.get(i);
			if(myCreature.getName().equals(name))
			{
				return myCreature;
			}
		}
		return null;
	}
	
	public double getTotalScoreFromThisDiver()
	{
		double score = 0;
		for(int i = 0; i < listOfCreatures.size(); i++)
		{
			Creature creatureIveSeen = listOfCreatures.get(i);
			score += creatureIveSeen.getTotalScoreFromThisCreature();
		}
		return score;
	}
	
}
