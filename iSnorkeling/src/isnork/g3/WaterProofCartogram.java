package isnork.g3;
import isnork.sim.Observation;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Set;

public class WaterProofCartogram extends AbstractCartogram{

//	private final int mapWidth;
//	private final int viewRadius;
//	private final int numDivers;
//	private final WaterProofSquare[][] mapStructure;

	public WaterProofCartogram(int mapWidth, int viewRadius, int numDivers) {
//		TODO add these back in when we need them
//		commented them out because they were triggering warnings on 
//		dead code which made me nervous

//		this.mapWidth = mapWidth;
//		this.viewRadius = viewRadius;
//		this.numDivers = numDivers;
//		this.mapStructure = new WaterProofSquare[mapWidth][mapWidth];
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getNextDirection() {
		// TODO Auto-generated method stub
		
	}

}