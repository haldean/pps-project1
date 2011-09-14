package isnork.g6;


import isnork.sim.GameEngine;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;
import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class Board {

	int n, d, r;
	
	Cell matrix[][];
	Set<Observation> currentPlayerLocations;
	Set<SeaLifePrototype> possibileCreatures;
	
	/*to be decided after experimenting / other parameters */ 
	private static final int HAPPY_THRESHOLD = 0;
	
	/*id and number of times seen */
	HashMap<Integer, Integer> listOfExplored = new HashMap<Integer,Integer>();
	private Logger log = Logger.getLogger(this.getClass());
	private int tick = -1;
	
	//list of players who have seen max happiness. One this is = n, choose go back. 
	HashSet<Integer> seenMaxHappiness = new HashSet<Integer>();
 	
	Board(int d, int r, int n) {
		this.n = n;
		this.d = d;
		this.r = r;
		/*initialize the stuff*/
		matrix = new Cell[2*d + 1][2*d + 1];
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = new Cell(ExplorationStatus.unexplored);
			}
		}
		currentPlayerLocations = new HashSet<Observation>();
	}
	
	/*have not seen this id before */
	public boolean InListOfExplored(Observation o) {
		boolean isInListOfExplored = false;
		if(listOfExplored.containsKey(o.getId()) && (listOfExplored.get(o.getId()) > 3)) {
			isInListOfExplored = true;
		}
		return isInListOfExplored;
	}
	
	public void storeSeaObservations(Set<Observation> observations, Set<Observation> playerLocations, int currentTime, Point2D whereIAm, Message message) {
		int value;
		
		int newx = (int)whereIAm.getX() + d;
		int newy = (int)whereIAm.getY() + d ;
		matrix[newx][newy].explorationStatus = ExplorationStatus.explored;
		
		//everything thats not in the direction of movement is also explored, to speed things up.
		
		for(Observation o : observations) {
			value = 1;
			Point2D point = o.getLocation();
			int x = (int) point.getX() + d;
			int y = (int) point.getY() + d;
			
			matrix[x][y].message = Message.getLetter(o.getName());
			matrix[x][y].time = currentTime;
			matrix[x][y].sender = -1; //means its me for now.
			matrix[x][y].isMovable = false; //to be done
			matrix[x][y].currentMovement = o.getDirection();
			
			//log.info(" currentMovement for crature = " + o.getDirection() + " name = " + o.getName());
			if(listOfExplored.containsKey(o.getId())) {
				 value = listOfExplored.get(o.getId());
				 value++;
			}
			listOfExplored.put(o.getId(), value);
			//log.info(" setting " + o.getId() + " at location " + (int) point.getX() + " " + (int) point.getY() + " to be " + value);
			if(o.happiness() > HAPPY_THRESHOLD && !InListOfExplored(o) ) {
				matrix[x][y].explorationStatus = ExplorationStatus.interesting;
			}else {
				matrix[x][y].explorationStatus = ExplorationStatus.uninteresting; //should I actively mark something as uninteresting?
			}
			//add this observation to message queue.
			message.addMessageToQueue(o.getName(), o.happiness(), o.getDirection());
		}
		
		for(Observation p : playerLocations) {
			Point2D point = p.getLocation(	);
			int x = (int) point.getX() + d;
			int y = (int) point.getY() + d;
			matrix[x][y].lastVisitedTime = currentTime;
			matrix[x][y].lastVisitedPlayerId = p.getId();
			matrix[x][y].explorationStatus = ExplorationStatus.explored;
			//might want to update this to reflect that all of cells in r radius are explored. But thats for later.
		}
		currentPlayerLocations = playerLocations;
	}
	
	/*take the incoming messages and store it on the board. for all even time ticks creature name is sent
	 * for odd ones direction is sent. */
	public void storeMessages(Set<iSnorkMessage> incomingMessages,Set<Observation> playerLocations) {
		tick++;
		for(iSnorkMessage m : incomingMessages) {
			//player has seen maxHappiness?
			if(m.getMsg().equals("z")) {
				seenMaxHappiness.add(m.getSender());
				log.info(" store that someone has seen max possible happiness ");
				continue; //no need to store it in the board and confuse ppl.
			}
			
			int x = (int)m.getLocation().getX() + d;
			int y = (int)m.getLocation().getY() + d;
			if(tick % 2 == 0) {
				matrix[x][y].message = m.getMsg();
				//log.info("time = " + tick + " message = " + m.getMsg() + " is the creature name");
			}else {
				matrix[x][y].currentMovement = Explorer.convertIntToDirection(Utilities.mapato1(m.getMsg()));
				//log.info("time = " + tick + " message = " + m.getMsg() + " is the direction it is moving");
			}
			matrix[x][y].time = InitialPlayer.getCurrentTime();
			matrix[x][y].sender = m.getSender();
		}
	}
	
	/*Tested so far : gives uninteresting on start of game */
	/*count that number of cells on the board that have something. If below 80 (1/5th of the board,
	 * board is too sparse. 
	 */
	public boolean isTooUninteresting() {
		boolean boardSparse = true;
		int count = 0;
		int happiness = 0;
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0 ; j < matrix[0].length; j++) {
				if(matrix[i][j].explorationStatus == ExplorationStatus.interesting) {
					count++;
				}
				happiness += Message.getHappiness(matrix[i][j].message);
			}
		}
		if( count >= 10 || happiness > 300) { //change this based on experiments.
			boardSparse = false; //lots of happy creatures seen / one good happy creature seen etc, got to follower mode.
		}
		return boardSparse;
	}
	
	/*Players sometimes get stuck in an over explored area. Check to see if area is over explored 
	 * and put the player in follower mode if area is overexplored
	 * 
	 *  if we have "explored in all directions, it is overexplored.
	 */
	public boolean isOverExplored(Point2D whereIAm) {
		boolean neighborhoodExplored = false;
		int count = 0;
		Direction newDirection;
		for(int j = 0; j < 8; j++) {
			newDirection = Explorer.convertIntToDirection(j);
			int newx = (int) whereIAm.getX();
			int newy = (int) whereIAm.getY();
			newx = newx + newDirection.dx + d;
			newy = newy + newDirection.dy + d ;
			if( newx < 2 * d && newy < 2* d && newx > 0 && newy > 0) {
				if ( matrix[newx ][newy].explorationStatus == ExplorationStatus.unexplored) {
					count ++;
				}
			}
		}
		if( count <= 1) { //change this based on experiments.
			neighborhoodExplored = true;
		}
		return neighborhoodExplored;
	}
	
	
	
	/*print board to reflect the status of each cell */
	public void printBoard() {
	}
	
	/*create a letter for each distinct creature for now */
	public void setVocabulary(Set<SeaLifePrototype> seaLifePossibilities) {
		this.possibileCreatures = seaLifePossibilities;
		Message.setVocabulary(seaLifePossibilities);
	}
	
	/* check to see if everyoen has reached their max happiness score */
	public boolean isEveryoneHappy() {
		boolean isHappy = false;
		if(seenMaxHappiness.size() == n) {
			log.info(" EVERYONE IS HAPPY !!! YIPPPEEEE " );
			isHappy = true;
		}
		return isHappy;
	}
}
