package isnork.g3;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Set;


public interface Cartogram {
  /**
   * Update the map with the provided metrics for this tick.
   * @return Unknown.
   */
	String update(Point2D myPosition, Set<Observation> whatYouSee,
			Set<Observation> playerLocations, Set<iSnorkMessage> incomingMessages);

  /**
   * Unknown.
   */
	String getMessage();

  /**
   * Get the next move that the diver should make.
   */
	Direction getNextDirection();

}
