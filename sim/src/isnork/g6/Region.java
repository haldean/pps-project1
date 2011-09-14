package isnork.g6;

import java.awt.geom.Point2D;

import org.apache.log4j.Logger;

import isnork.sim.GameObject.Direction;

/* defines the region that the explorer is going to be inside */
public class Region {
	
	int startx, starty;
	int endx, endy;
	int n, d, r;

	boolean explorer; //we need to further refine this so that only some are explorers.
	int playerId;
	Direction first, second, third, fourth;
	Direction currentDirection, prevDirection;
	int directionIndex = 0;	
	private Logger log = Logger.getLogger(this.getClass());

	private void initialize() {
		int explorers = 0;
		if( n > 10) {
			explorers = 4 * n/10;
		}else {
			explorers = 2;
		}
		if( playerId < explorers / 2) {
			explorer = true;
			startx = -(d) + 2*2*d/explorers * playerId; //have only explorers /2
			endx = -d + 2 * 2 * d / explorers * (playerId + 1);
			starty = 0;
			endy = d;
			first = Direction.N;
			third = Direction.S;
		}else if(playerId < explorers) {
			explorer = true;
			startx = -(d) + 2* 2*d/explorers * (playerId - explorers/2);
			endx =  -(d) + 2* 2*d/explorers * (playerId - explorers/2 + 1);
			starty = 0;
			endy = -d;
			first = Direction.S;
			third = Direction.N;
		}
		if(startx > 0) {
			second = Direction.E;
			fourth = Direction.W;
		} else {
			second = Direction.W;
			fourth = Direction.E;
		}
		prevDirection = null;
		currentDirection = first;
		log.info("playerId = " + playerId + "startx = " + startx + "endx = " + endx + " starty = " + starty + "end y = " + endy);
	
	}
	
	Region(int playerId, int n, int r, int d) {
		this.n = n;
		this.r = r;
		this.d = d;
		this.playerId = playerId;
		initialize();
	}
	
	public boolean yWithin(Point2D whereIAm) {
		int y = (int) whereIAm.getY();
		boolean yWithin = false;
		if( endy == d) {
			if( (starty - r) < y && (endy - r) > y) {
				yWithin = true;
			}
			if( starty < y && endy > y) {
				yWithin = true;
			}
		}
		if( endy == -d) {
			if( (starty - r) > y && (endy - r) < y) {
				yWithin = true;
			}
			if( starty > y && endy < y) {
				yWithin = true;
			}
		}
		return yWithin;
	}
	
	public boolean xWithin(Point2D whereIAm) {
		boolean xWithin = false;
		int x = (int) whereIAm.getX();
		if((startx -r) < x && (endx - r)> x) {
			xWithin = true;
		}
		if(startx  < x && endx > x) {
			xWithin = true;
		}
		return xWithin;
	}
	
	public boolean isWithinRegion(Point2D whereIAm) {
		
		boolean isWithin = false;
		if( xWithin(whereIAm) && yWithin(whereIAm)){
			isWithin = true;
		}
		return isWithin;
	}
	
	//either east or west depending on the position of region wrt to you
	public Direction directionToRegion(int x) {
		Direction d = null;
		if (x < startx ) {
			d = Direction.E;
		} else {
			d = Direction.W;
		}
		prevDirection = currentDirection;
		currentDirection = d;
		return d;
	}

	public Direction directionWithinRegion(Point2D whereIAm) {
		
		Direction directionArray[] = { first, second, third, second, first, second };
		Direction d = null;
		prevDirection = currentDirection;
		if(currentDirection == second) {
			if(whereIAm.getX() % r == 0) {
				if(prevDirection == first) {
					currentDirection = third;
					//log.info(playerId + "setting currentDirection to third");
				} else {
					currentDirection = first;
					//log.info(playerId + "setting currentDirection to third");
				}
			}
		}else if (currentDirection == first || currentDirection == third) {
			if(!yWithin(whereIAm)) {
				//log.info(playerId + " changing to second direction");
				currentDirection = second;
			}
			else if(whereIAm.getX() == endx - r) {
				//log.info(playerId + " changing to fourth direction");
				currentDirection = fourth;
			}
		}else if( currentDirection == fourth) {
			if(whereIAm.getX() == startx + r) {
				currentDirection = first;
				//log.info(playerId + "changing direction from fourth to first");
			}
		}
		return currentDirection;
	}
	
	public Direction getNewDirection(Point2D whereIAm) {
		Direction d = null;
		//first move to the region
		if(!xWithin(whereIAm)) {
			d = directionToRegion((int) whereIAm.getX());
		}else {
			d = directionWithinRegion(whereIAm);
		}
		return d;
	}
}
