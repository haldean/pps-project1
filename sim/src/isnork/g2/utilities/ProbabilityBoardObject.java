package isnork.g2.utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class ProbabilityBoardObject 
{
	public HashMap<String, SeaCreatureType> creatureMapping;
	ArrayList<SeaCreatureType> seenCreatures;
	int x, y;
	
	public ProbabilityBoardObject(HashMap<String, SeaCreatureType> cm, int myX, int myY)
	{
		seenCreatures = new ArrayList<SeaCreatureType>();
		creatureMapping = cm;
		x = myX;
		y = myY;
	}
	
	public void addSeenCreature(int id, String name, int round)
	{
		boolean added = false;
		
		//if we've already seen a creature of that type, add it to the existing type
		for(SeaCreatureType sct : seenCreatures)
		{
			if(sct.returnCreature().getName().equals(name))
			{
				sct.addSeen(id);
				//sct.addSeenWithRound(id, round, x, y);
				added = true;
				break;
			}
		}
		
		//if we haven't seen a creature of that type, add a new creature
		if(!added)
		{
			SeaCreatureType temp = creatureMapping.get(name);
			temp.addSeen(id);
			//temp.addSeenWithRound(id, round, x, y);
			seenCreatures.add(temp);
		}
	}
}
