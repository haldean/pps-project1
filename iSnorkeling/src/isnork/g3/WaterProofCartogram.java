package isnork.g3;
import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Set;

public class WaterProofCartogram implements Cartogram {
  private final int sideLength;
	private final int viewRadius;
	private final int numDivers;
	private final Square[][] mapStructure;

	public WaterProofCartogram(int mapWidth, int viewRadius, int numDivers) {
		this.sideLength = mapWidth;
		this.viewRadius = viewRadius;
		this.numDivers = numDivers;
		this.mapStructure = new WaterProofSquare[sideLength][sideLength];
	}

	@Override
	public String update(Point2D myPosition, Set<Observation> whatYouSee,
			Set<Observation> playerLocations,
			Set<iSnorkMessage> incomingMessages) {
		for (Observation location : playerLocations) {
			location.getLocation();
			location.getId();
			location.getName();
		}
		
		for (iSnorkMessage message : incomingMessages) {
			message.getLocation();
			message.getMsg();
			message.getSender();
		}
		
		for (Observation observation : whatYouSee) {
			observation.getDirection();
			observation.getLocation();
			observation.getId();
			observation.happiness();
			observation.isDangerous();
			observation.happinessD();
			observation.getName();
		}

		return null;
	}

	@Override
	public String getMessage() {
    return "";
	}

	@Override
	public Direction getNextDirection() {
    return null;
	}
}
