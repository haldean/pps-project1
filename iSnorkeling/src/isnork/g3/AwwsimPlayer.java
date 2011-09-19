package isnork.g3;

import java.awt.geom.Point2D;
import java.util.Set;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;

public class AwwsimPlayer extends Player {
	String name;
	private Direction nextMove;
	private int numDivers;
	private int viewRadius;
	// private int penalty;
	private Cartogram carto;

	private AbstractPokedex dex;

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Reset defaults, set up strategy
	 */
	@Override
	public void newGame(Set<SeaLifePrototype> seaLifePossibilities,
			int penalty, int d, int r, int n) {
		int mapWidth = d * 2 + 1;
		dex = new WaterProofPokedex(seaLifePossibilities);
		carto = new WaterProofCartogram(mapWidth, viewRadius, numDivers, dex);
		viewRadius = r;
		numDivers = n;

		// TODO add these back in when we need them
		// commented them out because they were triggering warnings on
		// dead code which made me nervous

		// this.penalty = penalty;

		// TODO analysis on species precomputation
		// TODO swimming strategy precomputation
	}

	/**
	 * Process input, determine message to be sent
	 */
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,
			Set<Observation> playerLocations) {
    System.out.println("carto update is go");
		carto.update(myPosition, whatYouSee, playerLocations, incomingMessages);
    System.out.println("carto update is done");
		nextMove = carto.getNextDirection();
		return carto.getMessage();
	}

	/**
	 * Move should be stored based on information in tick, then return it
	 */
	@Override
	public Direction getMove() {
		return nextMove;
	}
}
