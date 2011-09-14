package isnork.g3;
import java.awt.geom.Point2D;
import java.util.Set;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;


public class AwsmPlayer extends Player{
	
	String name;
	private Direction nextMove;
	private int numDivers;
	private int viewRadius;
	private Set<SeaLifePrototype> species;
	private int penalty;
	private Cartogram carto;

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Reset defaults, set up strategy
	 */
	@Override
	public void newGame(Set<SeaLifePrototype> seaLifePossibilities, int penalty,
			int d, int r, int n) {
		int mapWidth = d*2 + 1;
		carto = new WaterProofCartogram(mapWidth, viewRadius, numDivers);
		viewRadius = r;
		numDivers = n;
		species = seaLifePossibilities;
		this.penalty = penalty;
		//TODO analysis on species precomputation
		//TODO swimming strategy precomputation
	}

	/**
	 * Process input, determine message to be sent
	 */
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,
			Set<Observation> playerLocations) {
		return carto.update(myPosition,whatYouSee,playerLocations,incomingMessages); 
	}

	/**
	 * Move should be stored based on information in tick, then return it
	 */
	@Override
	public Direction getMove() {
		return nextMove;
	}

}
