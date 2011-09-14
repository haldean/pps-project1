package isnork.gdumb;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;
import isnork.sim.GameObject.Direction;

public class FearfulDumbPlayer extends Player {
    private static final Logger log = Logger.getLogger(FearfulDumbPlayer.class);
    
    private static final int TOTAL_ROUNDS = 480;
    private static final double LONGEST_MOVE = 6.8;
    private static final Point2D BOAT = new Point2D.Double(0.0, 0.0);
    
    private int currentRound;
    private int boardRadius;
    
    private Point2D whereIAm = BOAT;
    private ArrayList<Observation> dangerQueue;
    
	@Override
	public String getName() {
		return "Fearful Dumb Player";
	}
	  
    public Point2D getLocation() {
        return whereIAm;
    }
	
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages, Set<Observation> playerLocations) {
	    try{
	        currentRound++;
	        whereIAm = myPosition;

	        dangerQueue = new ArrayList<Observation>();

	        for (Observation o : whatYouSee){
	            if (o.isDangerous())
	                dangerQueue.add(o);
	        }

	        return null;
	    } catch(RuntimeException e){
	        log.error("An exception occurred while calculating tick :(", e);
			whereIAm = myPosition;
			return null;
		}
	}

	@Override
	public Direction getMove() {
	    try{
	        Direction nextDir = null;
	        Direction directionToBoat = StaticDiver.directionTowardCreature(whereIAm, BOAT);
	        int timeLeft = TOTAL_ROUNDS - currentRound;

	        // Return home programming
	        if (timeLeft < ((3 * LONGEST_MOVE) +
	                (StaticDiver.getDistanceFromCreature(BOAT, whereIAm) * LONGEST_MOVE))) {
	            nextDir = directionToBoat;
	        } else {
	            nextDir = getSafeRandomDirection();
	        }

	        return nextDir;
	    } catch(RuntimeException e){
	        log.error("We threw an exception...=(", e);
	        return StaticDiver.directionTowardCreature(whereIAm, BOAT);
	    }
	}


/**
 * @param dir The direction to correct
 * @return The closest direction to dir that is valid and avoids dangerous creatures
 */
	public Direction getSafeRandomDirection(){
		ArrayList<Direction> possibilities = Direction.allBut(null);

		// Eliminate invalid moves
		for(Iterator<Direction> i = possibilities.iterator(); i.hasNext();){
		    Direction d = i.next();
			if(!StaticDiver.isValidMove(whereIAm, d, boardRadius)){
				i.remove();
			}
		}
		
		// Avoids dangerous creatures
		for (Observation o : dangerQueue) {
		    for(Iterator<Direction> i = possibilities.iterator(); i.hasNext() && (whereIAm.equals(BOAT) || possibilities.size() > 1); ) {		        
		        Direction d = i.next();
		        Direction toCreature = StaticDiver.directionTowardCreature(whereIAm, o.getLocation());

		        if (StaticDiver.wouldMoveNextTo(this.whereIAm, d, o.getLocation())) {
		            i.remove();
		        } else if (o.getDirection() != null && d == toCreature) {
		            i.remove();
		        }
		    }
		}
		
		// If the direction itself still is possible, return it. Otherwise, find the closest one.
		if (possibilities.isEmpty()) {
		    return null; 
		} else {
		    return possibilities.get(random.nextInt(possibilities.size()));
		}
	}
	
	@Override
	/**
	 * For most intents, the constructor for the diver. Sets everything up for the new game.
	 */
	public void newGame(Set<SeaLifePrototype> seaLifePossibilites, int penalty,
			int boardDimension, int sightDimension, int diverCount) {
        boardRadius = boardDimension;
        currentRound = -1;
	}
}
