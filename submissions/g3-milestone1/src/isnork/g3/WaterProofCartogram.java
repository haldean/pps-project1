package isnork.g3;
import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Set;

import com.google.common.collect.Lists;

public class WaterProofCartogram extends AbstractCartogram{

	private final int mapWidth;
	private final int viewRadius;
	private final int numDivers;
	private final WaterProofSquare[][] mapStructure;

	public WaterProofCartogram(int mapWidth, int viewRadius, int numDivers) {
		this.mapWidth = mapWidth;
		this.viewRadius = viewRadius;
		this.numDivers = numDivers;
		this.mapStructure = new WaterProofSquare[mapWidth][mapWidth];
	}

	@Override
	public String update(Point2D myPosition, Set<Observation> whatYouSee,
			Set<Observation> playerLocations,
			Set<iSnorkMessage> incomingMessages) {
		for (Observation location : playerLocations) {
			Point2D loc = location.getLocation();
			int id = location.getId();
			String name = location.getName();
		}
		
		for (iSnorkMessage message : incomingMessages) {
			Point2D loc = message.getLocation();
			String msg = message.getMsg();
			int sender = message.getSender();
		}
		
		for (Observation observation : whatYouSee) {
			Direction dir = observation.getDirection();
			Point2D loc = observation.getLocation();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getNextDirection() {
		// TODO Auto-generated method stub
		
	}

}