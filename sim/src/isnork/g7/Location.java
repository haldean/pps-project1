package isnork.g7;


import isnork.sim.Observation;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;


public class Location {
	
	private Point2D coordinate;
	private int playerID = Integer.MAX_VALUE;
	private Set<Observation> playerLocations;
	
	private Logger logger = Logger.getLogger(Location.class);
	
	public Location(Point2D coordinate){
		this.coordinate = coordinate;
	}
	
	public Location(int playerID){
		this.playerID = playerID;
	}
	
	public Point2D getLocation(){
		if (playerID != Integer.MAX_VALUE){
			//logger.trace("Getting location from player " + playerID);
			//System.out.println("Getting location from player " + playerID);
			//TRAVERSE CURRENT PLAYER LOCATION LIST AND RETURN THAT LOCATION
			return getPlayerLocation(playerID);
		}
		else{

			//logger.trace("Returning a static coordinate");
			return coordinate;
		}
	}
	
	public void setPlayerLocations(Set<Observation> playerLocations){
		this.playerLocations = playerLocations;
	}
	
	private Point2D getPlayerLocation(int playerID){
		//logger.debug("playerLocations: " + playerLocations);
		
		if(playerLocations!= null){
			Iterator<Observation> playerIterator = playerLocations.iterator();
			
			while(playerIterator.hasNext()){
				Observation nextPlayer = playerIterator.next();
				if(nextPlayer.getId() == playerID  && nextPlayer!=null){
					return nextPlayer.getLocation();
				}
			}
		}
		
		return null;
	}
	
	public int getPlayerID(){
		return playerID;
	}

}
