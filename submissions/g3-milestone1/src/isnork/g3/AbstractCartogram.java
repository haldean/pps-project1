package isnork.g3;
import isnork.sim.Observation;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Set;


public abstract class AbstractCartogram implements Cartogram {

	@Override
	public String update(Point2D myPosition, Set<Observation> whatYouSee,
			Set<Observation> playerLocations,
			Set<iSnorkMessage> incomingMessages) {
		// TODO Auto-generated method stub
		return null;
	}

}
