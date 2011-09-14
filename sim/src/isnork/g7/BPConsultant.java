package isnork.g7;


import isnork.sim.GameConfig;
import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;
import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

public class BPConsultant extends Player {

	// A buffer to give you some extra time to go back to boat.
	// Why 6: allows you to make 1 useless diagonal move away from boat. 3 minutes each way.
	private static final int BOAT_TIME_BUFFER = 6;
	
	private static final Logger logger = Logger.getLogger(BPConsultant.class);
	
	private Direction direction;
	Point2D whereIAm = null;
	// having this flag prevents him from wavering between going and not going to boat
	private boolean shouldReturnToBoat = false;
	int boatTimeBufferAdjusted;
	
	Set<SeaLifePrototype> seaLifePossibilities;
	int d = -1;  // parameter "d" == half the board length
	int r = -1;  // parameter "r" == number of cells away you can see
	int n = -1;  // parameter "n" == number of snorkelers
	int penalty;
	int round = 0;
	
	private OurBoard ourBoard;
	private DangerFinder dangerFinder;
	private Point2D myPosition = null;
	private Point2D myPreviousPosition = null;
	private Set<Observation> whatYouSee = null;
	private Point2D beast = null;
	private TaskManager taskManager;
	private boolean isTracker = false;
	private boolean isSeeker = false;
	private boolean curTracking = false;
	private SeaLifePrototype toTrack = null;
	private Point2D trackLocal = null;
	private Task task = null;
	private Direction radiateOut = null;
	private Set<Observation> pLocations = null;
	private PlayerMovementTracker pmt= null;
	private SpecialCaseAnalyzer specialCaseAnalyzer = null;  // detect Piranha etc. maps
	private String specialCase = "";
	
	// keep track of maxDistance and timeToMaxDistance to estimate time to return to boat
	private double maxDistance = 0.0;
	private int timeToMaxDistance = 1;
	private Point2D previousMaxDistanceLocation = new Point2D.Double(0,0);
	private double avgSpeed = 0.5;



	private Tracker ourTracker;

	private int myID;
	
	@Override
	public String getName() {
		return "BP Consultant";
	}
	
	@Override
	public String tick(Point2D myPosition, Set<Observation> whatYouSee,
			Set<iSnorkMessage> incomingMessages,Set<Observation> playerLocations) {
		this.myPreviousPosition = this.myPosition;
		this.myPosition = myPosition;
		this.whatYouSee = whatYouSee;
		taskManager.updatePlayerLocations(playerLocations);
		pLocations = playerLocations;
		ourTracker.updatePoints(playerLocations, myPosition);
		
		taskManager.updateSeenCreatures(whatYouSee);
		
		if(pmt!=null)
			pmt.update(myPosition, playerLocations);
		
		if(round == 10)
			logger.debug(myID=getMyID(playerLocations, myPosition));
		
		Observation[] a = new Observation[whatYouSee.size()];
		ArrayList<OurObservation> o = new ArrayList<OurObservation>();
		whatYouSee.toArray(a);
		for(int i=0; i<a.length;i++)
		{
			if(a[i].getId()>=0 && !a[i].getName().equalsIgnoreCase(getName()))
			{
				o.add(new OurObservation(a[i]));
			}
		}
		Collections.sort(o);
		
		String snorkMessage = null;
		whereIAm = myPosition;
		round++;

		// FIXME they are going back too early. avgSpeed calculation is reporting too-low average speed
		if (whereIAm.distance(0, 0) > maxDistance) {
			int roundDelta = round - timeToMaxDistance;
//			double locationDelta = whereIAm.distance(previousMaxDistanceLocation);
			double locationDelta = whereIAm.distance(0,0);
			double newAvgSpeed = locationDelta / (double)roundDelta;
			maxDistance = whereIAm.distance(0, 0);
			timeToMaxDistance = round;
//			previousMaxDistanceLocation = whereIAm;
			
			// keep track of slowest avg speed. conservative measurement to get back on time.
			if (newAvgSpeed < avgSpeed && newAvgSpeed > 0) {
				//logger.debug("newAvgSpeed="+newAvgSpeed + ", 	old avgSpeed="+avgSpeed);
				avgSpeed = newAvgSpeed;
			}
		}

		//if(isTracker)
		//{
			//look at all creatures
			//if tracking, return highest static creature
			/*if(pmt!= null){
				if(!pmt.continueChase())
				{
					logger.error("Stopping chase");
					curTracking = false;
					//return MessageTranslator.getMessage(toTrack.getName());
				}
			}*/
			
			if(task != null){
				if(task.doISeeTheTarget(whatYouSee))
				{
					taskManager.markTaskComplete(task);
					task = null;
				}
			}
			
			//read messages
			iSnorkMessage temp = null;
			String mess = null;
			SeaLifePrototype obs = null;
			Iterator<iSnorkMessage> it = incomingMessages.iterator();
			while(it.hasNext())
			{	
				temp = it.next();
				mess= temp.getMsg();
				obs = MessageTranslator.hm.get(mess);
				if(obs == null)
					continue;
			/*
				if(task != null){
					if(obs.getName().equalsIgnoreCase(task.toString()))
					{
						taskManager.markTaskComplete(task);
						task = null;
					}
				}
				*/
				taskManager.cleanTasks(obs.getName(), temp.getSender());
				
				
				if(obs.getSpeed() == 0)
				{
					taskManager.addTask(obs.getName(), temp.getLocation());
				}
				else
				{
					taskManager.addTask(obs.getName(), temp.getSender());
				}
			}
			
			
			if(curTracking)
			{
				logger.debug("Diver " + " is following " + toTrack.getName());
				logger.debug("");
				
				//update trackLocal
				boolean change = false;
				for(OurObservation ob : o)
				{
					if(ob.o.getName().equals(toTrack.getName()))
					{
						change = true;
						trackLocal = ob.o.getLocation();
					}
				}
				if(!change){
					curTracking=false;
					logger.debug("STOPPPPPPPPPPPPPPPPP");
					return MessageTranslator.getMessage(toTrack.getName());
				}
				
				for(OurObservation ob: o)
				{
					snorkMessage = MessageTranslator.getMessage(ob.o.getName());
					if(MessageTranslator.hm.get(snorkMessage).getSpeed() == 0)
					{
						logger.debug("tracker sees: " +snorkMessage);
						return snorkMessage;
					}
				}
				return null;
			}
			//else
			//if highest scoring is moving, track
			for(OurObservation ob: o)
			{
				snorkMessage = MessageTranslator.getMessage(ob.o.getName());
				if(!ob.o.getName().equalsIgnoreCase("sea weed")){
				//	logger.debug(myID);
				//	logger.debug(ob.o.getName());
				//	logger.debug(MessageTranslator.hm.get(snorkMessage).getSpeed());
				//	logger.debug(taskManager.seenCreatures.get(ob.o.getName()).size());
				//	logger.debug(!taskManager.chasedCreatures.containsKey(ob.o.getName()));
				}
				
				if(MessageTranslator.hm.get(snorkMessage).getSpeed() > 0 
						&& taskManager.seenCreatures.get(ob.o.getName()).size() <= 2 
						&& !taskManager.chasedCreatures.containsKey(ob.o.getName()))
				{
					logger.debug(myID +" tracking: " +snorkMessage);
					curTracking = true;
					pmt = new PlayerMovementTracker(myPosition, playerLocations);
					toTrack = MessageTranslator.hm.get(snorkMessage);
					trackLocal = ob.o.getLocation();
					return snorkMessage;
				}
			}
			//else return highest static creature
		//}
		//else
		//{
			
			//add stuff to queue
	//	}
		
		return snorkMessage;
	}
	
	private int getMyID(Set<Observation> playerPositions, Point2D mypos){
	
		for(Observation o: playerPositions)
		{
			if(o.getLocation().distance(mypos)==0)
				return o.getId();
		}
		
		return 0;
	}
	
//	private void preprocessDanger(){
//		if (mapDanger.equals("Extreme danger")){
//			logger.debug("Detected really dangerous map.");
//			
//			//put code here to keep players really close to the board 
//			//on this dangerous board
//		}
//	}

	
	@Override
	public Direction getMove() {	
		
		logger.debug("Printing task list");
		taskManager.printTaskList();
		
		// handle dangerous maps like Piranha
		if (SpecialCaseAnalyzer.HIGH_DANGER_STRING.equals(specialCase)) {
			return getDangerousMapMove();
		}
		
		
		// Head back to boat if we are running low on time, based on calculated avg speed
		if (shouldReturnToBoat || (getRemainingTime() <= 20 + (whereIAm.distance(0,0) / avgSpeed))) {
			if (!shouldReturnToBoat) {
				shouldReturnToBoat = true;
				//logger.debug("Returning to the boat. round="+round);
			}
			// If not enough time, ignore all dangerous creatures and return to boat.
			if (getRemainingTime() < NavigateToBoat.getTimeToBoat(whereIAm) + 6) {
				direction = NavigateToBoat.getShortestDirectionToBoat(whereIAm);
				//logger.debug("No time to avoid danger");
			}
			// If there are at least 6 spare minutes, that lets you maneuver around dangerous creatures,
			// even going diagonally away from boat once.
			else {
				//logger.debug("Returning to boat but trying to avoid danger still");
				Direction preferredDirectionToBoat = NavigateToBoat.getShortestDirectionToBoat(whereIAm);
				direction = dangerFinder.findSafestDirection(myPosition, myPreviousPosition,
						whatYouSee, preferredDirectionToBoat, true);
			}
			//logger.debug("(boat) remaining: " + getRemainingTime() + " whereIAm:"+whereIAm + " (dir "+direction+")");
			return direction;
		} else {
			//logger.trace("in getMove()");
			
			//if(isTracker)
			//{
				//if tracking track
				if(curTracking)
				{
					logger.debug(myPosition + " " + trackLocal);
					Direction dt =  Tracker.track(myPosition, trackLocal);
					//logger.debug("isTracker and Currently tracking.");
					return dangerFinder.findSafestDirection(myPosition, myPreviousPosition, whatYouSee, dt, false);
				}
				//else rando-walk
				//return Tracker.track(null, myPosition, beast);
			/*	
				if(radiateOut != null)
				{
					if(Math.abs(myPosition.getX())+r >= Math.abs(d/2) || Math.abs(myPosition.getY())+r >= Math.abs(d/2))
						radiateOut = null;
					//logger.debug("is tracker but not tracking, should radiate out.");
					return dangerFinder.findSafestDirection(myPosition, myPreviousPosition, whatYouSee, radiateOut, false);
				}
				else
				{
					Direction dest = ourTracker.getClosestUnexplored(myPosition);
					//logger.debug("non tracking tracker after radiate out.");
					return dangerFinder.findSafestDirection(myPosition, myPreviousPosition, whatYouSee, dest, false);
				}
				*/
		//	}
		//	else
		//	{
				//if on task, go
				if(task == null)
				{
					task = taskManager.getNextTask(myPosition);
				}
				if(task!=null)
				{
					/** Elizabeth, please see this code below */
					Point2D objCoord = task.getObservation().getTheLocation().getLocation();
			
					direction = Tracker.track(myPosition, objCoord);
					//logger.debug("Chasing a task");
					return dangerFinder.findSafestDirection(myPosition, myPreviousPosition, whatYouSee, direction, false);
						
				
				}
				//else rando walk
				if(radiateOut != null)
				{
					logger.debug("Doing rando walk");
					if(Math.abs(myPosition.getX())+r >= Math.abs(d/2) || Math.abs(myPosition.getY())+r >= Math.abs(d/2))
						radiateOut = null;
					//logger.debug("seeker radiating out");
					return dangerFinder.findSafestDirection(myPosition, myPreviousPosition, whatYouSee, radiateOut, false);
				}
				else
				{
					Direction dest = ourTracker.getClosestUnexplored(myPosition);
					//logger.debug("seeker random walk");
					return dangerFinder.findSafestDirection(myPosition, myPreviousPosition, whatYouSee, dest, false);
				}
				
		//	}
			/*
			Direction d = dangerFinder.findSafestDirection(myPosition, myPreviousPosition,
					whatYouSee, direction, false);
			
			if (d == null){
				
				//Why would d ever be null? There should always be a safest direction.
				d = getNewDirection();
			

				Point2D p = new Point2D.Double(whereIAm.getX() + d.dx,
						whereIAm.getY() + d.dy);
				while (Math.abs(p.getX()) > GameConfig.d
						|| Math.abs(p.getY()) > GameConfig.d) {
					d = getNewDirection();
					p = new Point2D.Double(whereIAm.getX() + d.dx,
							whereIAm.getY() + d.dy);
				}
			}
			return d;
			*/
		}
	}
	
	// For very dangerous maps (Piranha) use a single-chain DFA
	private static final int DANGEROUS_MAP_STATE_ONBOAT = 0;
	private static final int DANGEROUS_MAP_STATE_OFFBOAT = 1;
	private static final int DANGEROUS_MAP_STATE_ENDBOAT = 2;
	private int dangerousMapState = DANGEROUS_MAP_STATE_ONBOAT;
		
	private Direction getDangerousMapMove() {
		switch (dangerousMapState) {
		case DANGEROUS_MAP_STATE_ONBOAT:
			if (getRemainingTime() >= 6) {
				// wait for opening to leave boat quickly
				Direction direction = dangerFinder.findSafestDirection(myPosition, myPreviousPosition,
						whatYouSee, getNewDirection(), false);
				if (dangerFinder.getDangerInDirection(direction) == 0.0) {
					dangerousMapState = DANGEROUS_MAP_STATE_OFFBOAT;
					return direction;
				}
			}
			return null;
		case DANGEROUS_MAP_STATE_OFFBOAT:
			// wait until you see something or get scared, then get back on
			if (this.getHappiness() != 0 || getRemainingTime() <= 3) {
				dangerousMapState = DANGEROUS_MAP_STATE_ENDBOAT;
				return NavigateToBoat.getShortestDirectionToBoat(myPosition);
			}
			return null;
		case DANGEROUS_MAP_STATE_ENDBOAT:
			return null;
		default:
			return null;
		}
	}
	
	@Override
	public void newGame(Set<SeaLifePrototype> seaLifePossibilities, int penalty,
			int d, int r, int n) {
		this.seaLifePossibilities = seaLifePossibilities;
		this.penalty = penalty;
		this.d = d;
		this.r = r;
		this.n = n;
		this.round = 0;
		this.boatTimeBufferAdjusted = NavigateToBoat.getBoatTimeBufferAdjusted(BOAT_TIME_BUFFER, seaLifePossibilities, d);
		this.ourBoard = new OurBoard(d);
		this.dangerFinder = new DangerFinder(ourBoard, d, r, seaLifePossibilities, random);
		this.radiateOut = Tracker.getRandomDirection();
		taskManager = new TaskManager(seaLifePossibilities, ourBoard);
		MessageTranslator.initializeMap(seaLifePossibilities);
		this.ourTracker = new Tracker(d,r);
		this.specialCaseAnalyzer = new SpecialCaseAnalyzer();
		
		//mapDanger = specialCaseAnalyzer.detectDangerousMap(seaLifePossibilities);
		//preprocessDanger();
		
		this.specialCaseAnalyzer = new SpecialCaseAnalyzer();
		this.specialCase = specialCaseAnalyzer.detectDangerousMap(seaLifePossibilities);
		logger.debug("special case: " + this.specialCase);
		
		if(Math.random() >= 0.5)
			isTracker = true;
		else
			isSeeker = true;
	}


	private Direction getNewDirection() {
		int r = random.nextInt(100);
		if(r < 10 || direction == null)
		{
			ArrayList<Direction> directions = Direction.allBut(direction);
			direction = directions.get(random.nextInt(directions.size()));
		}
			return direction;
	}
	
	/**
	 * Get the remaining time out of 480 rounds (8 hours).
	 * The final round will return 0.
	 * GameEngine starts with round 0, and Game Over is before beginning of round 481.
	 * @return
	 */
	private int getRemainingTime() {
		return 480 - round;
	}


}
