package isnork.g6;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;

enum ExplorationStatus { explored, unexplored, interesting, uninteresting };

public class Cell {

	int lastVisitedPlayerId;
	int lastVisitedTime;
	
	Direction currentMovement;
	String message;
	int sender;
	int time;
	int id;
	boolean isMovable; // is the message that was put in here at the given time about a moving thing?
	//(gotta invalidate or not use the date after a while in that case)
	ExplorationStatus explorationStatus;
	
	Cell() {
		lastVisitedPlayerId = -1;
		lastVisitedTime = -1;
		explorationStatus = ExplorationStatus.unexplored; //normally, it is unexplored initially.
	}
	
	Cell(ExplorationStatus a) {
		explorationStatus = a; //in case you want to use this.
	}
}
