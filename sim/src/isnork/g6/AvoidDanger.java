package isnork.g6;

import isnork.sim.GameObject.Direction;
import isnork.sim.GameEngine;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class AvoidDanger {

	public static Direction avoidDanger(Point2D myPosition, Set<Observation> whatYouSee, Set<SeaLifePrototype> seaLifePossibilites, Direction wishToGoDirection){
		LinkedList<Direction> possibleDirection = new LinkedList<Direction>();
		possibleDirection.add(Direction.N);
		possibleDirection.add(Direction.NE);
		possibleDirection.add(Direction.E);
		possibleDirection.add(Direction.SE);
		possibleDirection.add(Direction.S);
		possibleDirection.add(Direction.SW);
		possibleDirection.add(Direction.W);
		possibleDirection.add(Direction.NW);
		Iterator<Observation> iterator = whatYouSee.iterator();
		LinkedList<Observation> dangerousCreature = new LinkedList<Observation>();
		while(iterator.hasNext()){
			Observation currentCreature = (Observation)iterator.next();
			GameEngine.println("Creature Moving? : "+currentCreature.getDirection());
			if(currentCreature.isDangerous()){
				possibleDirection = removePossibleDirectionFromDangerousCreature(possibleDirection, currentCreature, myPosition, seaLifePossibilites);
				dangerousCreature.add(currentCreature);
			}
		}
		Direction result = null;
		if(possibleDirection==null){
			//result = findLeastHarmfulDirection(possibleDirection);
			result = wishToGoDirection;
		}else{
			if(possibleDirection.contains(wishToGoDirection)){
				result = wishToGoDirection;
			}else{
				result = wishToGoDirection;
				while(!possibleDirection.contains(wishToGoDirection)){
					result = possibleDirection.removeFirst();
				}
			}
		}
		return result;
	}
	
	private static LinkedList<Direction> removePossibleDirectionFromDangerousCreature(LinkedList<Direction> possibleDirection, Observation currentCreature, Point2D myPosition, Set<SeaLifePrototype> seaLifePossibilites){
		String nameCreature = currentCreature.getName();
		Iterator<SeaLifePrototype> sealifeIterator = seaLifePossibilites.iterator();
		SeaLifePrototype sealife;
		SeaLifePrototype currentSealife = null;
		while(sealifeIterator.hasNext()){
			sealife = sealifeIterator.next();
			if(sealife.getName().equalsIgnoreCase(nameCreature)){
				currentSealife = sealife;
			}
		}
		if(currentSealife!=null){
			if(currentSealife.getSpeed()==0){
				return removeDirectionFromInmobileDangerous(possibleDirection ,myPosition, currentCreature.getLocation());
			}else{
				return removeDirectionFromMobileDangerous(possibleDirection ,myPosition, currentCreature);
			}
		}else{
			return possibleDirection;
		}
	}
	
	private static LinkedList<Direction> removeDirectionFromMobileDangerous(LinkedList<Direction> possibleDirection, Point2D myPosition, Observation creature){
		Point2D creatureLocation = creature.getLocation();
		if((myPosition.getY()-creatureLocation.getY())==3){
			if(Math.abs(myPosition.getX()-creatureLocation.getX())<=1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.NW);
			}else if((myPosition.getX()-creatureLocation.getX())==2){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NW);
			}else if((myPosition.getX()-creatureLocation.getX())==-2){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NE);
			}else if((myPosition.getX()-creatureLocation.getX())==3){
				possibleDirection.remove(Direction.NW);
			}else if((myPosition.getX()-creatureLocation.getX())==-3){
				possibleDirection.remove(Direction.NE);
			}
		}else if((myPosition.getY()-creatureLocation.getY())==2){
			if(Math.abs(myPosition.getX()-creatureLocation.getX())<=1){
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.E);
			}else if((myPosition.getX()-creatureLocation.getX())==2){
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.N);
			}else if((myPosition.getX()-creatureLocation.getX())==-2){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.E);
			}else if((myPosition.getX()-creatureLocation.getX())==3){
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==-3){
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.E);
			}
		}else if((myPosition.getY()-creatureLocation.getY())==1){
			if(myPosition.getX()==creatureLocation.getX()){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==-1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==2){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==-2){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.E);
			}else if((myPosition.getX()-creatureLocation.getX())==3){
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.SW);
			}else if((myPosition.getX()-creatureLocation.getX())==-3){
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.SE);
			}
		}else if(myPosition.getY()==creatureLocation.getY()){
			if((myPosition.getX()-creatureLocation.getX())==1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==-1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.E);
			}else if((myPosition.getX()-creatureLocation.getX())==2){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==-2){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SE);
			}else if((myPosition.getX()-creatureLocation.getX())==3){
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.SW);
			}else if((myPosition.getX()-creatureLocation.getX())==-3){
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.SE);
			}
		}else if((myPosition.getY()-creatureLocation.getY())==-1){
			if(myPosition.getX()==creatureLocation.getX()){
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==-1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==2){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==-2){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.E);
			}else if((myPosition.getX()-creatureLocation.getX())==3){
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.SW);
			}else if((myPosition.getX()-creatureLocation.getX())==-3){
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.SE);
			}
		}else if((myPosition.getY()-creatureLocation.getY())==-2){
			if(Math.abs(myPosition.getX()-creatureLocation.getX())<=1){
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.E);
			}else if((myPosition.getX()-creatureLocation.getX())==2){
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.S);
			}else if((myPosition.getX()-creatureLocation.getX())==-2){
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.E);
			}else if((myPosition.getX()-creatureLocation.getX())==3){
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creatureLocation.getX())==-3){
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.E);
			}
		}else if((myPosition.getY()-creatureLocation.getY())==-3){
			if(Math.abs(myPosition.getX()-creatureLocation.getX())<=1){
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.SW);
			}else if((myPosition.getX()-creatureLocation.getX())==2){
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SW);
			}else if((myPosition.getX()-creatureLocation.getX())==-2){
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SE);
			}else if((myPosition.getX()-creatureLocation.getX())==3){
				possibleDirection.remove(Direction.SW);
			}else if((myPosition.getX()-creatureLocation.getX())==-3){
				possibleDirection.remove(Direction.SE);
			}
		}
		return possibleDirection;
	}
	
	private static LinkedList<Direction> removeDirectionFromInmobileDangerous(LinkedList<Direction> possibleDirection, Point2D myPosition, Point2D creature){
		if(myPosition.getY()-creature.getY()==2){
			if(myPosition.getX()==creature.getX()){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.NW);
			}else if((myPosition.getX()-creature.getX())==1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NW);
			}else if((myPosition.getX()-creature.getX())==-1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NE);
			}else if((myPosition.getX()-creature.getX())==2){
				possibleDirection.remove(Direction.NW);
			}else if((myPosition.getX()-creature.getX())==-2){
				possibleDirection.remove(Direction.NE);
			}
		}else if((myPosition.getY()-creature.getY())==1){
			if((myPosition.getX()-creature.getX())==2){
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creature.getX())==-2){
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.E);
			}else if(myPosition.getX()==creature.getX()){
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creature.getX())==1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.W);
			}else if((myPosition.getX()-creature.getX())==-1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.E);
			}
		}else if(myPosition.getY()==creature.getY()){
			if((myPosition.getX()-creature.getX())==2){
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.SW);
			}else if((myPosition.getX()-creature.getX())==-2){
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.SE);
			}else if((myPosition.getX()-creature.getX())==1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NW);
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.S);
			}else if((myPosition.getX()-creature.getX())==-1){
				possibleDirection.remove(Direction.N);
				possibleDirection.remove(Direction.NE);
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.S);
			}
		}else if((myPosition.getY()-creature.getY())==-1){
			if((myPosition.getX()-creature.getX())==2){
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.SW);
			}else if((myPosition.getX()-creature.getX())==-2){
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.SE);
			}else if(myPosition.getX()==creature.getX()){
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.E);
			}else if((myPosition.getX()-creature.getX())==1){
				possibleDirection.remove(Direction.W);
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.S);
			}else if((myPosition.getX()-creature.getX())==-1){
				possibleDirection.remove(Direction.E);
				possibleDirection.remove(Direction.SE);
				possibleDirection.remove(Direction.S);
			}
		}else if((myPosition.getY()-creature.getY())==-2){
			if((myPosition.getX()-creature.getX())==2){
				possibleDirection.remove(Direction.SW);
			}else if((myPosition.getX()-creature.getX())==1){
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.S);
			}else if(myPosition.getX()==creature.getX()){
				possibleDirection.remove(Direction.SW);
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SE);
			}else if((myPosition.getX()-creature.getX())==-1){
				possibleDirection.remove(Direction.S);
				possibleDirection.remove(Direction.SE);
			}else if((myPosition.getX()-creature.getX())==-2){
				possibleDirection.remove(Direction.SE);
			}
		}
		return possibleDirection;
	}
	
}
