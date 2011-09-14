package isnork.g7;

import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Task implements Comparable<Task> {
	
	private OurObservation observation;
	private double priorityScore = 0; 
	private OurBoard ourBoard;
	private Set<SeaLifePrototype> seaLifePossibilities;
	
	public Task(String creatureName, int playerID, OurBoard ourBoard, Set<SeaLifePrototype> seaLifePossibilities, Set<Observation> playerLocations){
		observation = new OurObservation(creatureName, playerID, seaLifePossibilities, playerLocations);
		this.ourBoard = ourBoard; 
		this.seaLifePossibilities = seaLifePossibilities;
	}
	
	public Task(String creatureName, Point2D coordinate, OurBoard ourBoard, Set<SeaLifePrototype> seaLifePossibilities, Set<Observation> playerLocations){
		observation = new OurObservation(creatureName, coordinate, seaLifePossibilities, playerLocations);
		this.ourBoard = ourBoard; 
		this.seaLifePossibilities = seaLifePossibilities;
	}
	
	public void updatePriorityScore(Point2D myCurrentLocation, HashMap<String, HashSet<Integer>> seenCreatures){
		String creatureName = observation.getCreatureName();
		int timesSeen = seenCreatures.get(creatureName).size();
		SeaLifePrototype life = getCreature(creatureName);
		double happiness = life.getHappinessD();
		boolean isDangerous = life.isDangerous();
		if (timesSeen == 0) {
			priorityScore = happiness / ourBoard.findDistanceToObservation(observation, myCurrentLocation);
		} else if (timesSeen == 1) {
			priorityScore = happiness / 2.0 / ourBoard.findDistanceToObservation(observation, myCurrentLocation);
		} else if (timesSeen == 2) {
			priorityScore = happiness / 4.0 / ourBoard.findDistanceToObservation(observation, myCurrentLocation);
		} else if (isDangerous) {
			priorityScore = -happiness * DangerFinder.DANGER_MULTIPLIER / ourBoard.findDistanceToObservation(observation, myCurrentLocation);
		} else {
			priorityScore = 0;
		}
	}

	public double getPriorityScore(){
		return priorityScore;
	}
	
	public OurObservation getObservation(){
		return observation;
	}
	
	public void setPriorityScore(double newScore){
		priorityScore = newScore;
	}

	@Override
	public int compareTo(Task otherTask) {
		if (this.priorityScore < ((Task) otherTask).getPriorityScore())
			return -1;
		else if (this.priorityScore > ((Task) otherTask).getPriorityScore())
			return 1;
		else
			return 0;
	}
	
	private SeaLifePrototype getCreature(String creatureName){
		for (SeaLifePrototype s : seaLifePossibilities){
			if (creatureName == s.getName()){
				return s;
			}
		}
		return null;
	}
	
	public void updatePlayerLocations(Set<Observation> playerLocations){
		observation.getTheLocation().setPlayerLocations(playerLocations);
	}
	
	public boolean doISeeTheTarget(Set<Observation> seaLifeCreatures){
		String targetName = observation.getCreatureName();
		
		for(Observation o: seaLifeCreatures){
			if (targetName.equals(o.getName())){
				return true;
			}
		}
		
		return false;
	}
	
	public String toString(){
		
		return observation.getCreatureName();
	}
	
}