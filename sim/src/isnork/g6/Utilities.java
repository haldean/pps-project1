package isnork.g6;

import isnork.sim.GameEngine;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;
import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.Iterator;
import java.util.Set;

public class Utilities {
	
	
	private static int maxPossibleHappiness; 
	
	public static void calculateMaximumHappiness(Set<SeaLifePrototype> seaLifePossibilites){
		
		Iterator<SeaLifePrototype> iteratorSeaLife = seaLifePossibilites.iterator();
		int happyPoint = 0;
		int sealifeHappyPoint = 0;
		SeaLifePrototype sealife;
		while(iteratorSeaLife.hasNext()){
			sealife = iteratorSeaLife.next();
			sealifeHappyPoint = sealife.getHappiness();
			if((sealife.getMinCount()==0)&&(sealife.getMaxCount()==1)){
				happyPoint += sealifeHappyPoint;
			}
			if(sealife.getMinCount()>0){
				happyPoint += sealifeHappyPoint;
			}
			if(sealife.getMinCount()>1){
				happyPoint += sealifeHappyPoint/2;
			}
			if(sealife.getMinCount()>2){
				happyPoint += sealifeHappyPoint/4;
			}
		}
		maxPossibleHappiness =  happyPoint;
		GameEngine.println(" maximum possible happiness : " + maxPossibleHappiness);
	}
	
	public static int getMaxHappiness() {
		return maxPossibleHappiness;
	}
	
	public static double distance(Point2D a, Point2D b) {
		double distance = 0;
		if( a != null && b != null) { 
			double xsquared = Math.pow(b.getX() - a.getX(),2);
			double ysquared = Math.pow(b.getY() - a.getY(),2);
			distance = Math.pow(xsquared + ysquared, 0.5);
		}
		return distance;	
	}
	
	public static Point2D getConvertedToBoard(Point2D p, int d) {
		int newx = (int) p.getX();
		int newy = (int) p.getY();
		Point2D relocatedp = new Point2D.Double(newx + d, newy + d);
		return relocatedp;
	}
	
	public static Point2D getConvertedToGame(Point2D p, int d) {
		int newx = (int) p.getX();
		int newy = (int) p.getY();
		Point2D relocatedp = new Point2D.Double(newx - d, newy - d);
		return relocatedp;
	}
	
	//function checking if player has reached end of the ocean or not
	public static boolean onBoundary(Point2D currentlocation, int r, int d)
	{
		Point2D center=new Point2D.Double(0.0, 0.0);
		int distance = (int) Utilities.distance(center, currentlocation); 
		GameEngine.println("distance = " + distance);
		if(distance >=(d-r+1)) //+1 for corner coverage
		{
			GameEngine.println(" we are on the BOUNDARY!!!!!!!!");
			return true;
		}
		return false; 
	}

//both the message mapping functions--->
	
	//give it a num equal to the number of alphabet you want returned, eg: a=1 not 0 as in the case of arrays
	//Why is this not a hashmap??!! The semantics is so much simpler.
	public static String map1toa(int num)
	{
		String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
		return alphabet[num];	
	}
	
	public static int mapato1(String alpha)
	{
		String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
		for(int num=0; num<alphabet.length; num++)
		{
			if(alphabet[num]== alpha)
			{
				return (num);
			}
		}
		return -1;   //case of no match found
	}
 }
