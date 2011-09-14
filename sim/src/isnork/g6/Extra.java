package isnork.g6;

import isnork.sim.Observation;
import isnork.sim.iSnorkMessage;
import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Set;

public class Extra {
	
	/*
	private Point2D getBestRankFromMessage(){
		double best = -1.0;
		double msg;
		int senderID;
		Set<Observation> m;
		Point2D location;
		double rank;
		Point2D bestLocation = null;
		Iterator<Set<Observation>> playerLocationIterator = playerLocation.iterator();
		for(Set<iSnorkMessage> i: incomingMessage){
			m = playerLocationIterator.next();
			for(iSnorkMessage j: i){
				msg = 'a'-j.getMsg().charAt(0);
				senderID = j.getSender();
				location = null;
				for(Observation n: m){
					if(n.getId()==senderID){
						location = n.getLocation();
					}
				}
				if(location!=null){
					rank = msg/(whereIAm.distance(location));
					if(rank>best){
						best = rank;
						bestLocation = location;
					}
				}
			}
		}
		if(best>0){
			return bestLocation;
		}else return null;
	}

	private Direction getDirectionFromLocation(Point2D location){
		if(location.getX()==whereIAm.getX()){
			if(location.getY()==whereIAm.getY()){
				return null;
			}else if(location.getY()<whereIAm.getY()){
				return Direction.N;
			}else{
				return Direction.S;
			}
		}else if(location.getX()<whereIAm.getX()){
			if(location.getY()==whereIAm.getY()){
				return Direction.E;
			}else if(location.getY()<whereIAm.getY()){
				return Direction.NE;
			}else{
				return Direction.SE;
			}
		}else{
			if(location.getY()==whereIAm.getY()){
				return Direction.W;
			}else if(location.getY()<whereIAm.getY()){
				return Direction.NW;
			}else{
				return Direction.SW;
			}
		}
	}

	  //use this one for follower
    private Direction getMoveForFollower(){
            
            Point2D location = getBestRankFromMessage();
            if(location==null){
                    return null;
            }
            return getDirectionFromLocation(location);
    }*/
}
