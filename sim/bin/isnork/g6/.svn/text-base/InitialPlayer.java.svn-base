package isnork.g6;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.log4j.Logger;


import isnork.g6.Danger.DangerAvoidance;
import isnork.sim.GameConfig;
import isnork.sim.GameObject;
import isnork.sim.Observation;
import isnork.sim.Player;
import isnork.sim.SeaLife;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;
import isnork.sim.GameObject.Direction;

public class InitialPlayer extends Player {

	private Direction direction;
	private int dimension = 1;
	private int radius = 1;
	private int numberOfDivers = 1;
	private Point2D boatPosition;
	private Set<SeaLifePrototype> seaLifePossibilites;
	
	private int playerId;
	private static int distributeId = 0;
	private static int currentTime = 0;
	private LinkedList<Set<iSnorkMessage>> incomingMessage;
	private LinkedList<Set<Observation>> playerLocation;
	
	Region region;
	Board board;
	Message message = new Message(); 
	
	private Logger log = Logger.getLogger(this.getClass());
	Set<Observation> whatISee;
	DangerAvoidance dangerAvoider = new DangerAvoidance();
	Controller controller = new Controller(dangerAvoider);
	GoBack goBack = new GoBack();
	
	Point2D whereIAm = null;
	int n = -1;
	
	public InitialPlayer() {	
		distributeId++;
		playerId = distributeId; //keep separate id for each player
		whatISee = null;
	}
	
	public static int getCurrentTime() {
		return currentTime;
	}
		
	public boolean tooClose(Point2D a, Point2D b) {
		boolean isTooClose = false;
		if( Utilities.distance(a,b) < 1.5) {
			isTooClose = true;
		}
		return isTooClose;
	}
	
	private Direction getNewRandomDirection() {
		int r = random.nextInt(100);
		if(r < 10 || direction == null)
		{
			ArrayList<Direction> directions = Direction.allBut(direction);
			direction = directions.get(random.nextInt(directions.size()));
		}
		return direction;
	}
	
	@Override
	public String getName() {
		return "g6 ImprovedPlayer";
	}
	
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,Set<Observation> playerLocations) {
		
		whereIAm = myPosition;
		whatISee = whatYouSee;
		//keep message
		incomingMessage.add(incomingMessages);
		playerLocation.add(playerLocations);
		
		board.storeSeaObservations(whatYouSee, playerLocations, currentTime, whereIAm, message);
		board.storeMessages(incomingMessages, playerLocations);
		String m = message.getMessage(this.getHappiness());
		if(m == null) {
			m = "y";
		}
		currentTime++;
		goBack.calculateTimeLeft();
		return m;
	}
	
	@Override
	public Direction getMove() {
		if(!goBack.shouldGoBack(whereIAm, board)){
			
			Direction d = controller.getNewDirection(whereIAm, whatISee, board, dimension, radius);
			if( d != null) {
				Point2D p = new Point2D.Double(whereIAm.getX() + d.dx,
						whereIAm.getY() + d.dy);
				while (Math.abs(p.getX()) > GameConfig.d
						|| Math.abs(p.getY()) > GameConfig.d) {
					d = getNewRandomDirection();
					p = new Point2D.Double(whereIAm.getX() + d.dx,
							whereIAm.getY() + d.dy);
				}
			}
			return d;
		}else{
			log.info(" going back is chosen");
			Direction d = goBack.goBack(whereIAm);
			Direction dangerdir = dangerAvoider.avoidImmediateDanger(whereIAm, whatISee, d, dimension); //very needed
			if(dangerdir != null) {
				d = dangerdir;
			}
			if((whereIAm.getX()==0)&&(whereIAm.getY()==0)){
				d=null;
			}
			return d;
		}
	}
	
	@Override
	public void newGame(Set<SeaLifePrototype> seaLifePossibilites, int penalty,
			int d, int r, int n) {
		// TODO Auto-generated method stub
		dimension = d;
		radius = r;
		numberOfDivers = n;
		incomingMessage = new LinkedList<Set<iSnorkMessage>>();
		playerLocation = new LinkedList<Set<Observation>>();
		region = new Region(playerId % numberOfDivers, n, r, d);
		board = new Board(d,r,n);
		board.setVocabulary(seaLifePossibilites);
		dangerAvoider.setDangerTypes(seaLifePossibilites);
		Utilities.calculateMaximumHappiness(seaLifePossibilites);
		this.seaLifePossibilites = seaLifePossibilites;
		currentTime = 0;
	}
}