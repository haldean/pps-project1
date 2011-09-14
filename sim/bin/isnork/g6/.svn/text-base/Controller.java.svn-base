package isnork.g6;

import isnork.g6.Danger.DangerAvoidance;
import isnork.sim.Observation;
import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

enum PlayerMode{follower, explorer};

public class Controller {

	Explorer explorer = new Explorer();
	Follower follower = new Follower();
	PlayerMode currentMode;
	private Logger log = Logger.getLogger(this.getClass());
	DangerAvoidance dangerAvoider;
	
	
	public Controller(DangerAvoidance dangerAvoider) {
		this.dangerAvoider = dangerAvoider; 
	}
	
	public Direction getNewDirection(Point2D whereIAm, Set<Observation> whatISee, Board board, int d, int r) {
		Direction dir = null;
		
		//TODO : if players have obtained max possible happiness, go back.
		
		while (dir == null) {
			if(board.isOverExplored(whereIAm)) {
				log.info("board is overexplored");
				dir = follower.getNewDirection(board, whereIAm, whatISee, d, r);
				if(dir == null) {
					dir = explorer.getNewDirection(board, whereIAm, d, r );
				}
			}
			if(board.isTooUninteresting()) {
				log.info(" board is uninteresting");
				dir = explorer.getNewDirection(board, whereIAm, d, r);
			}else {
				dir = follower.getNewDirection(board, whereIAm, whatISee, d, r);
				if(dir == null) {
					dir = explorer.getNewDirection(board, whereIAm, d, r);
				}
			}
		}
		//if danger is approaching right now, or if anticipated because of this move, get away.
		dir = dangerAvoider.avoidImmediateDanger(whereIAm, whatISee, dir, d);
		log.info("Direction chosen : " + dir);
		/*if(dangerdir != null) { 
			dir = dangerdir;
		}*/
		return dir;
	}
	
	
}
