package isnork.g6;

import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.apache.log4j.Logger;

public class GoBack {

	private int timeLeft = 480;
	private Logger log = Logger.getLogger(this.getClass());
	
	private boolean goBack = true;
	
	public GoBack() { 
		goBack = false;
	}
	
	public boolean shouldGoBack(Point2D whereIAm, Board board){
		
		if(goBack == true) {
			return true;
		} else if(board.isEveryoneHappy()) {
			goBack = true;
			log.info(" go back chosen since everyone is happy");
		} else {
			double distanceHome = Utilities.distance(whereIAm, new Point2D.Double(0,0));
			double timeHome = Math.max ( distanceHome * 3, whereIAm.getX() *2 + whereIAm.getY() * 2 );
			double bufferTime = 30; //to be revised? It should be more for dangerous things.
			log.info("go back timeLeeft value : " + timeLeft);
			if( timeLeft < timeHome + bufferTime) {
				goBack = true;
			}
		}
		return goBack;
	}
	
	public Direction goBack(Point2D whereIAm){
		if(whereIAm.getX()<0.0){
			if(whereIAm.getY()<0.0){
				return Direction.SE;
			}else if(whereIAm.getY()==0.0){
				return Direction.E;
			}else{
				return Direction.NE;
			}
		}else if(whereIAm.getX()==0.0){
			if(whereIAm.getY()<0.0){
				return Direction.S;
			}else if(whereIAm.getY()==0.0){
				if(timeLeft>5){
					double randomDirection = Math.random()*4;
					if(randomDirection<1){
						return Direction.N;
					}else if(randomDirection<2){
						return Direction.E;
					}else if(randomDirection<3){
						return Direction.S;
					}else{
						return Direction.W;
					}
				}else{
					return null;
				}
			}else{
				return Direction.N;
			}
		}else{
			if(whereIAm.getY()<0.0){
				return Direction.SW;
			}else if(whereIAm.getY()==0.0){
				return Direction.W;
			}else{
				return Direction.NW;
			}
		}
	}

	public void calculateTimeLeft(){
		log.info(timeLeft);
		timeLeft --;
	}
	
}
