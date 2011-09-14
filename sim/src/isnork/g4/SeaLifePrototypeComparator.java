package isnork.g4;

import isnork.sim.SeaLifePrototype;
import java.util.Comparator;


public class SeaLifePrototypeComparator implements Comparator<SeaLifePrototype>
{
    double CONST = 0.5;
    int boardDimension = 0;
	
	public SeaLifePrototypeComparator(int dimension)
	{
		boardDimension = dimension;
	}
	
	public double valueWeight(SeaLifePrototype life)
	{
		int average = (life.getMaxCount() + life.getMinCount()) / 2;
		int happiness = life.getHappiness();
		
		return ((happiness * CONST) + (average * (1 + CONST)));
	}
	
	public int compare(SeaLifePrototype o1, SeaLifePrototype o2 )
	{
		double result = valueWeight(o1) - valueWeight(o2);
	
	if(result < 0) return -1;
	if(result > 0) return 1;
	return 0;
				
	}
}