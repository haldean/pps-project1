package isnork.g3;
import java.awt.geom.Point2D;
import java.util.Set;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;


public class AwwsimPlayer extends Player{
  private static final int MAX_TICKS_PER_ROUND = 60 * 8;
	
	String name;
	private Direction nextMove;
	private int numDivers;
	private int viewRadius;
	private Set<SeaLifePrototype> species;
	private int penalty;
	private Cartogram carto;
  private int ticks = 0;
  private Point2D currentLocation;

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
    ticks++;
    currentLocation = myPosition;
		carto.update(myPosition,whatYouSee,playerLocations,incomingMessages); 
		carto.getNextDirection();
		return carto.getMessage();
	}

	/**
	 * Move should be stored based on information in tick, then return it
	 */
	@Override
	public Direction getMove() {
    if (ticks < MAX_TICKS_PER_ROUND - 100) {
      Direction[] dirs = Direction.values();
      return dirs[random.nextInt(dirs.length)];
    } else {
      // Move towards boat
      int deltaX = 0;
      int deltaY = 0;
      if (currentLocation.getX() < 0)
        deltaX = 1;
      else if (currentLocation.getX() > 0)
        deltaX = -1;

      if (currentLocation.getY() < 0)
        deltaY = 1;
      else if (currentLocation.getY() > 0)
        deltaY = -1;

      if (deltaX > 0 && deltaY == 0) {
        return Direction.E;
      } else if (deltaX > 0 && deltaY > 0) {
        return Direction.NE;
      } else if (deltaX > 0 && deltaY < 0) {
        return Direction.SE;
      } else if (deltaX == 0 && deltaY == 0) {
        return Direction.STAYPUT;
      } else if (deltaX == 0 && deltaY < 0) {
        return Direction.N;
      } else if (deltaX == 0 && deltaY > 0) {
        return Direction.S;
      } else if (deltaX < 0 && deltaY == 0) {
        return Direction.W;
      } else if (deltaX < 0 && deltaY > 0) {
        return Direction.NW;
      } else if (deltaX < 0 && deltaY < 0) {
        return Direction.NE;
      } else {
        throw new RuntimeException("I HAVE NO IDEA");
      }
    }
	}
}
