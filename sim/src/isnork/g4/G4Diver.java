package isnork.g4;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.log4j.Logger;

import isnork.sim.BoardPanel;
import isnork.sim.GameEngine;
import isnork.sim.GameController;
import isnork.sim.GameObject;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLife;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;
import isnork.sim.GameObject.Direction;
import isnork.sim.ui.GUI;

public class G4Diver extends Player {
    private static final Logger log = Logger.getLogger(G4Diver.class);
    
    private static final int TOTAL_ROUNDS = 480;
    private static final double DELAY_FACTOR = 18;
    private static final double LONGEST_MOVE = 3.5;
    private static final int FEAR_RADIUS = 6;
    private static final Point2D BOAT = new Point2D.Double(0.0, 0.0);
    
    private CreatureEncoding language;
    private int currentRound;
    private int numberOfDivers;
    private int boardRadius;
    private int sightRadius;
    
    private Itinerary myPlan;
    
    private Point2D whereIAm = BOAT;
    private PriorityQueue<Observation> dangerQueue;
    private PriorityQueue<SeaLifePrototype> priorityAnimals;
    private SpeciesChecklist seenAnimals;
    private String messageInProgress = null;
    private iSnorkTransmission[] messagesCache;
	
	@Override
	public String getName() {
		return "G4 in Costa Rica";
	}
	  
    public Point2D getLocation() {
        return whereIAm;
    }
	
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages, Set<Observation> playerLocations) {
	    currentRound++;
	    myPlan.tick(whereIAm);
        whereIAm = myPosition;

        dangerQueue.clear();
        
		boolean sendMessage = false;
		for (Observation o : whatYouSee){
			if (o.isDangerous())
				dangerQueue.add(o);
			if(priorityAnimals.peek().getName().equals(o.getName()))
				sendMessage = true;
		}
		
		// TODO: Actually receive messages properly
		messagesCache = new iSnorkTransmission[numberOfDivers];
		for(iSnorkMessage msg : incomingMessages) {
		    messagesCache[-msg.getSender()] = new iSnorkTransmission(msg);
		}
		updateKnowledge(myPosition, whatYouSee, true);

		// TODO: Actually send useful messages
		if(sendMessage){
			//log.debug("Found creature! Sending message.");
			return "a";
		}
		return null;
	}

	@SuppressWarnings("unused")
    private String bufferMessage(Point2D myPosition,
			Set<Observation> whatYouSee, Set<iSnorkMessage> incomingMessages,
			Set<Observation> playerLocations) {
		

		Point2D[] idToLocation = new Point2D[playerLocations.size()];
		for(Observation p : playerLocations)
			idToLocation[-p.getId()] = p.getLocation();
		
        for(Observation o: whatYouSee)
            if(o.isDangerous())
                dangerQueue.add(o);
		
		int index = currentRound % language.getCodingLength();
		log.debug(currentRound);
		int targetLength = 0;
		if(currentRound > 0) {
		    for(iSnorkMessage sms : incomingMessages) {
		        if(sms.getSender() != this.getId()) {
		            if(messagesCache[sms.getSender()] == null) {
		                messagesCache[sms.getSender()] = new iSnorkTransmission(sms);
		            } else {
		                messagesCache[sms.getSender()].appendMessage(sms);
		                int tmpTarget = messagesCache[sms.getSender()].getMsg().length();
		                targetLength = tmpTarget > targetLength ? tmpTarget : targetLength;
		            }
		        }
		    }
		    
		    
		    for(int j = 0; j < messagesCache.length; j++) {
		    	if(messagesCache[j] == null) {
	                messagesCache[j] = new iSnorkTransmission(j, idToLocation[j]);
	            }else if(messagesCache[j].getMsg().length() < targetLength) {
		            messagesCache[j].appendMessage(null);
		        }
		    }
		}
		
		updateKnowledge(myPosition, whatYouSee, (currentRound > 0 && index == 0));
		
		if(index == 0) {
		    messageInProgress = language.getEncodingForObservations(whatYouSee);
		    messagesCache = new iSnorkTransmission[numberOfDivers-1];
		}
		
		return messageInProgress.substring(index, index + 1).equals(" ") ?
		        null : messageInProgress.substring(index, index + 1);
	}

	@Override
	public Direction getMove() {
		Direction nextDir = null;
		int timeLeft = TOTAL_ROUNDS - currentRound;

		// Return home programming
		if (timeLeft < (Math.max(LONGEST_MOVE, StaticDiver.getDistanceFromCreature(BOAT, whereIAm)) * DELAY_FACTOR)) {
			if (whereIAm.equals(BOAT)) {
				nextDir = null;
				//log.debug("I'm On a boat, diver: " + getId() + "round: "+ currentRound);
			} else {
				nextDir = StaticDiver.directionTowardCreature(whereIAm, BOAT);
				
				// If there is time left, correct for danger in going back to the boat
				if (timeLeft > Math.ceil(StaticDiver.getDistanceFromCreature(BOAT, whereIAm)) * LONGEST_MOVE * 2) {
					nextDir = correctForDanger(nextDir);
				}
			}
		} else {
		    // Set heading towards the plan's next goal and correct for danger
		    if(myPlan.hasNext()) {
		        nextDir = StaticDiver.directionTowardCreature(whereIAm, myPlan.nextGoal(whereIAm).getLocation());
		    } else {
		        nextDir = Direction.allBut(null).get(random.nextInt(Direction.allBut(null).size()));
		    }
			nextDir = correctForDanger(nextDir);
	    }
	    
		// TODO: Logging of a bug
        if (nextDir != null && !StaticDiver.isValidMove(whereIAm, nextDir, boardRadius)) {
            log.error("Sending invalid move! FIXME!");
            nextDir = null;
        }
        
	    return nextDir;
	}


/**
 * @param dir The direction to correct
 * @return The closest direction to dir that is valid and avoids dangerous creatures
 */
	public Direction correctForDanger(Direction dir){
		log.debug("Direction to correct: " + dir);

		if(dir == null)
			return null;
		
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
		    if(StaticDiver.getDistanceFromCreature(o.getLocation(), whereIAm) < FEAR_RADIUS) {
		        for(Iterator<Direction> i = possibilities.iterator(); i.hasNext() && (whereIAm.equals(BOAT) || possibilities.size() > 1); ) {		        
		            Direction d = i.next();
		            if (StaticDiver.wouldMoveNextTo(this.whereIAm, d, o.getLocation())) {
		                i.remove();
		            } else if (o.getDirection() != null && StaticDiver.checkDangerousDirection(sightRadius,
		                    this.whereIAm, d, o.getLocation(), o.getDirection())) {
		                i.remove();
		            }
		        }
		    }
		}
		
		// Eliminate useless moves (that just reduce the amount we can see)
		for(Iterator<Direction> i = possibilities.iterator(); i.hasNext() && possibilities.size() > 1;){                
            Direction d = i.next();
            if(!StaticDiver.isDesirableMove(whereIAm, d, boardRadius, sightRadius)) {
                i.remove();
                log.debug(d + " " +  getId() + "undesirable direciton");
            }
        }
		
		// If the direction itself still is possible, return it. Otherwise, find the closest one.
		if(possibilities.contains(dir)) {
			return dir;
		} else if (possibilities.isEmpty()) {
		    return null; 
		} else {
		    // Logging
			log.debug(dir + " is dangerous or undesirable!");
	        log.debug("d: " + getId() + " round: " + currentRound + " destination before : " + (dir == null ? "" : dir.dx +" , " + dir.dy) );
	        
	        
	        // Find the closest uneliminated direction
	        int dirIndex = 0;
	        for(int i = 0; i < StaticDiver.DIRECTIONS.length; i++) {
	            if(StaticDiver.DIRECTIONS[i] == dir) {
	                dirIndex = i;
	                break;
	            }
	        }
	        for(int i = 1; i < StaticDiver.DIRECTIONS.length; i++) {
	            int poss1 = (dirIndex + i) % StaticDiver.DIRECTIONS.length;
	            int poss2 = ((dirIndex - i) + (i > dirIndex ? StaticDiver.DIRECTIONS.length : 0)) % StaticDiver.DIRECTIONS.length;
	            
	            if(possibilities.contains(StaticDiver.DIRECTIONS[poss1])) {
	                dir = StaticDiver.DIRECTIONS[poss1];
	                break;
	            }
	            
	            if(possibilities.contains(StaticDiver.DIRECTIONS[poss2])) {
                    dir = StaticDiver.DIRECTIONS[poss2];
                    break;
                }
	        }
	        
	        // Logging the results
	        log.debug(dir + " is the closest safe option!");
            log.debug("d: " + getId() + " round: " + currentRound + " destination before : " + (dir == null ? "" : dir.dx +" , " + dir.dy) );
	        
	        return dir;
		}
	}
	
	@Override
	/**
	 * For most intents, the constructor for the diver. Sets everything up for the new game.
	 */
	public void newGame(Set<SeaLifePrototype> seaLifePossibilites, int penalty,
			int boardDimension, int sightDimension, int diverCount) {
		messagesCache = new iSnorkTransmission[numberOfDivers];
	    sightRadius = sightDimension;
        boardRadius = boardDimension;
        numberOfDivers = diverCount;
        currentRound = -1;
	    
	    int dangerCount = 0;
	    int seaLifeCount = 0;

	    for(SeaLifePrototype species : seaLifePossibilites) {
	        seaLifeCount++;
	        if(species.isDangerous()) {
	            dangerCount += species.getMaxCount();
	        }
	    }

	    dangerQueue = new PriorityQueue<Observation>(dangerCount, new ObservComparator(this));
		priorityAnimals = new PriorityQueue<SeaLifePrototype>(seaLifeCount, new SeaLifePrototypeComparator(boardDimension));
		priorityAnimals.addAll(seaLifePossibilites);
		seenAnimals = new SpeciesChecklist(priorityAnimals);
		myPlan = new Itinerary(boardRadius);
		
		/* I don't think we have to negate the ID value anymore, do we? I mean, it makes
		 * no difference, but just a thing to keep a note of. 
		 */
        double myRadians = Math.toRadians((360.0/numberOfDivers) * -this.getId());
        myPlan.addGoal(new PointOfInterest(
                new Point2D.Double((int)(Math.cos(myRadians) * (1 + boardRadius - sightRadius)), (int)(Math.sin(myRadians) * (1 + boardRadius - sightRadius)))));
		
		language = new CreatureEncoding(seaLifePossibilites, boardRadius, sightRadius);
		messageInProgress = language.getEncodingForObservations(null);
	}
	
	/**
	 * Updated our itinerary based on what has happened this tick.
	 * @param myPosition Where I am.
	 * @param whatYouSee What creatures I observe.
	 * @param messagesDone Whether messages finished accumulating this round.
	 */
	protected void updateKnowledge(Point2D myPosition, Set<Observation> whatYouSee, boolean messagesDone) {
	    if(messagesDone) {
	        for(iSnorkTransmission sms : messagesCache) {
	            /**
	            SeaLife creature = language.getEncodedCreature(sms);
	            if(creature != null && seenAnimals.countRemaining(creature) > 0) {
	                myPlan.addGoal(new PointOfInterest(creature.getLocation(), creature, creature.getDirection() == null));
	            }
	            */
	            if(sms != null && sms.getMsg().equals("a")) {
	                SeaLife creature = new SeaLife(priorityAnimals.peek());
	                creature.setLocation(sms.getLocation());
	                creature.setDirection(Direction.N);
	                if(creature != null && seenAnimals.countRemaining(creature) > 0) {
	                    myPlan.addGoal(new PointOfInterest(creature.getLocation(), creature, creature.getDirection() == null));
	                }
	            }
	        }
	    }

	    if(whatYouSee != null) {
	        for(Observation o : whatYouSee) {
	            SeaLife creature = StaticDiver.seaLifeFromObservation(o, new ArrayList<SeaLifePrototype>(priorityAnimals));
	            if(creature != null && !seenAnimals.hasSeenIndividual(creature)) {
	                seenAnimals.checkOff(creature);
	                if(seenAnimals.countRemaining(creature) <= 0) {
	                    myPlan.creatureFulfilled(creature);
	                }
	            }
	        }
	    }
	}
}
