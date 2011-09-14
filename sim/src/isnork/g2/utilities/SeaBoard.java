package isnork.g2.utilities;

import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;

import isnork.sim.GameObject.Direction;

/** Represents board */
public class SeaBoard {

	private static final double DANGER_RADIUS = 5;
	private ArrayList<EachSeaCreature> creatures;
	private Set<SeaLifePrototype> prototypes;
	public SeaSpace[][] board;
	private int radius, distance;
	private Point2D boat;
	private int myId;

	public SeaBoard(int x, int y, int r, Set<SeaLifePrototype> p, int d,
			Point2D b, int id) {
		creatures = new ArrayList<EachSeaCreature>();
		prototypes = p;
		board = new SeaSpace[x + 1][y + 1];
		for (int i = 0; i < x + 1; i++) {
			for (int j = 0; j < y + 1; j++) {
				board[i][j] = new SeaSpace(new Point2D.Double(i, j));
			}
		}

		radius = r;
		distance = d;
		boat = b;
		myId = id;
	}

	public void remove(int id) {

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j].isoccupideby(id)) {
					board[i][j].remove(id);
				}
			}
		}
	}

	public void add(Observation o, int r, boolean b) {

		Boolean found = false;
		for (EachSeaCreature c : creatures) {
			if (c.getId() == o.getId()) {
				board[(int) o.getLocation().getX() + distance][(int) o
						.getLocation().getY()
						+ distance].addCreature(c, r, new Point2D.Double(
						(int) o.getLocation().getX(), (int) o.getLocation()
								.getY()), b);
				found = true;
			}
		}

		if (!found) {
			for (SeaLifePrototype p : prototypes) {
				if (p.getName() == o.getName()) {
					creatures.add(
							new EachSeaCreature(
									p, o.getId(), r, b, o.getLocation().getX(), o.getLocation().getY()));
				}
			}
		}
	}

	/*
	 * Method which checks if there are dangerous creatures around, and adds
	 * their locations to a list
	 */
	public boolean areThereDangerousCreatures(Set<Observation> whatISee) {
		boolean isThereDanger = false;
		for (Observation creature : whatISee) {
			if (creature.isDangerous()) {
				return true;
			}
		}
		return false;
	}

	public boolean areThereDangerousCreaturesInRadius(
			Set<Observation> whatISee, Point2D whereIAm, int r) {

		for (Observation creature : whatISee) {

			Point2D creatureLocation = new Point2D.Double(creature
					.getLocation().getX()
					+ distance, creature.getLocation().getY() + distance);
			if (creature.isDangerous()
					&& whereIAm.distance(creatureLocation) < r) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Observation> getDangerousCreaturesInRadius(Point2D whereIam,Set<Observation> whatISee) {
		ArrayList<Observation> dangerousCreatures = new ArrayList<Observation>();
		for (Observation creature : whatISee) {
			if (creature.isDangerous()) {
				Point2D p = new Point2D.Double(creature.getLocation().getX()
						+ boat.getX(), creature.getLocation().getY()
						+ boat.getY());
				if(p.distance(whereIam)<=DANGER_RADIUS){
					dangerousCreatures.add(creature);
				}
			}
		}
		return dangerousCreatures;
	}

	public ArrayList<Point2D> getPositionOfDangerousCreatures(
			Set<Observation> whatISee) {

		ArrayList<Point2D> dangerousCreatures = new ArrayList<Point2D>();
		for (Observation creature : whatISee) {
			if (creature.isDangerous()) {
				Point2D p = new Point2D.Double(creature.getLocation().getX()
						+ boat.getX(), creature.getLocation().getY()
						+ boat.getY());
				dangerousCreatures.add(p);
			}
		}
		return dangerousCreatures;
	}

	public ArrayList<Point2D> getPositionOfDangerousCreaturesInRadius(
			Set<Observation> whatISee, Point2D whereIAm, int smallradius) {
		ArrayList<Point2D> dangerousCreatures = new ArrayList<Point2D>();
		for (Observation creature : whatISee) {
			if (creature.isDangerous()
					&& creature.getLocation().distance(whereIAm) < smallradius) {
				dangerousCreatures.add(creature.getLocation());
			}
		}
		return dangerousCreatures;
	}

	public ArrayList<Direction> getHarmfulDirectionsInRadius(Point2D whereIAm,
			Set<Observation> whatISee, int smallradius) {
		ArrayList<Direction> harmfulDirections = new ArrayList<Direction>();
		return harmfulDirections;
	}
	
	
	public ArrayList<Direction> getHarmfulDirections(Point2D myLocation,
			Set<Observation> whatISee) {
		ArrayList<Direction> harmfulDirections = new ArrayList<Direction>();

		for (Point2D p : getPositionOfDangerousCreatures(whatISee)) {

			harmfulDirections.addAll(getDirections(myLocation, p));
		}
		return harmfulDirections;
	}

	public ArrayList<Direction> getDirections(Point2D myLocation, Point2D p) {

		ArrayList<Direction> harmfulDirections = new ArrayList<Direction>();

		double myX = myLocation.getX();
		double myY = myLocation.getY();
		double dangerX = p.getX() + boat.getX();
		double dangerY = p.getY() + boat.getY();
		if (myX == dangerX && myY > dangerY) {
			harmfulDirections.add(Direction.N);
			harmfulDirections.add(Direction.NE);
			harmfulDirections.add(Direction.NW);
		}

		if (myX == dangerX && myY < dangerY) {
			harmfulDirections.add(Direction.S);
			harmfulDirections.add(Direction.SE);
			harmfulDirections.add(Direction.SW);
		}

		if (myX > dangerX && myY == dangerY) {
			harmfulDirections.add(Direction.W);
			harmfulDirections.add(Direction.NW);
			harmfulDirections.add(Direction.SW);
		}
		if (myX < dangerX && myY == dangerY) {
			harmfulDirections.add(Direction.E);
			harmfulDirections.add(Direction.NE);
			harmfulDirections.add(Direction.SE);
		}
		if (myX < dangerX && myY > dangerY) {
			harmfulDirections.add(Direction.NE);
			harmfulDirections.add(Direction.N);
			harmfulDirections.add(Direction.E);
		}
		if (myX < dangerX && myY < dangerY) {
			harmfulDirections.add(Direction.SE);
			harmfulDirections.add(Direction.S);
			harmfulDirections.add(Direction.E);
		}
		if (myX > dangerX && myY > dangerY) {
			harmfulDirections.add(Direction.NW);
			harmfulDirections.add(Direction.N);
			harmfulDirections.add(Direction.W);
		}
		if (myX > dangerX && myY < dangerY) {
			harmfulDirections.add(Direction.SW);
			harmfulDirections.add(Direction.S);
			harmfulDirections.add(Direction.W);
		}

		return harmfulDirections;
	}

	public boolean isValidMove(int x, int y, Direction d) {
		Point2D p = new Point2D.Double(x + d.dx, y + d.dy);

		// check if the point is out of bounds
		if (p.getX() < 0 || p.getX() > distance * 2 - 1 || p.getY() < 0
				|| p.getY() > distance * 2 - 1)
			return false;

		return true;
	}

	public EachSeaCreature getHighScoringCreatureInRadius() {
		EachSeaCreature high = null;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				for (EachSeaCreature o : board[i][j].getOccupiedby()) {

					if (high == null) // first creature
						high = o;

					if (o.returnCreature().getHappiness() > high
							.returnCreature().getHappiness())
						high = o;
				}
			}
		}

		return high;
	}

	public int getMaxScore() {

		int maxscore = 0;

		for (SeaLifePrototype c : prototypes) {

			if (c.getMaxCount() >= 3)
				maxscore += 1.75 * c.getHappiness();

			if (c.getMaxCount() == 2)
				maxscore += 1.5 * c.getHappiness();

			if (c.getMaxCount() == 1)
				maxscore += c.getHappiness();
		}

		return maxscore;
	}

	public SeaSpace getSeaSpace(Point2D p) {

		return board[(int) p.getX()][(int) p.getY()];
	}

	public Boolean toGoal(Point2D whereIAm, Direction d, Point2D goal) {
		// in a quadrant
		if (whereIAm.getX() > goal.getX() && whereIAm.getY() > goal.getY()) {
			if (d == Direction.NW)
				return true;
		}

		if (whereIAm.getX() < goal.getX() && whereIAm.getY() < goal.getY()) {
			if (d == Direction.SE)
				return true;
		}

		if (whereIAm.getX() < goal.getX() && whereIAm.getY() > goal.getY()) {
			if (d == Direction.NE)
				return true;
		}

		if (whereIAm.getX() > goal.getX() && whereIAm.getY() < goal.getY()) {
			if (d == Direction.SW)
				return true;
		}

		// on a line
		if (whereIAm.getX() < goal.getX() && whereIAm.getY() == goal.getY()) {
			if (d == Direction.E)
				return true;
		}
		if (whereIAm.getX() == goal.getX() && whereIAm.getY() < goal.getY()) {
			if (d == Direction.S)
				return true;
		}

		if (whereIAm.getX() == goal.getX() && whereIAm.getY() > goal.getY()) {
			if (d == Direction.N)
				return true;
		}
		if (whereIAm.getX() > goal.getX() && whereIAm.getY() == goal.getY()) {
			if (d == Direction.W)
				return true;
		}

		return false;
	}

	public EachSeaCreature getCreature(int id) {

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {

				if (board[i][j].isoccupideby(id))
					return board[i][j].getCreature(id);
			}
		}
		return null;
	}

	/** Get the probability that a creature with an ID is on a particular space */
	public double getProbOnSpace(int id, int x, int y, int currRound) {

		EachSeaCreature ourCreature = getCreature(id);

		// on space and static creature
		if (board[x][y].isoccupideby(id)
				&& board[x][y].getCreature(id).returnCreature().getSpeed() == 0)
			return 1;

		// not on space and static creature
		if (!board[x][y].isoccupideby(id)
				&& board[x][y].getCreature(id).returnCreature().getSpeed() == 0)
			return 0;

		// too far away to possibly be there
		Point2D goingTo = new Point2D.Double(x, y);
		if (goingTo.distance(ourCreature.location) > (currRound - ourCreature
				.getLastseen()) / 2)
			return 0;

		// Otherwise return a probability
		return getProbOnSpace(x, y, currRound, ourCreature);
	}

	/** Get the probability that a creature with an ID is on a particular space */
	public double getProbOnSpace(EachSeaCreature ourCreature, int x, int y,
			int currRound) {

		// on space and static creature
		/*
		 * if (board[x][y].isoccupideby(id) &&
		 * board[x][y].getCreature(id).returnCreature().getSpeed() == 0) return
		 * 1;
		 * 
		 * // not on space and static creature if (!board[x][y].isoccupideby(id)
		 * && board[x][y].getCreature(id).returnCreature().getSpeed() == 0)
		 * return 0;
		 */

		// too far away to possibly be there
		Point2D goingTo = new Point2D.Double(x, y);
		if (goingTo.distance(ourCreature.location) > (currRound - ourCreature
				.getLastseen()) / 2)
			return 0;

		// Otherwise return a probability
		return getProbOnSpace(x, y, currRound, ourCreature);
	}

	/** Internal method to calculate prob when it is not 1 or 0 */
	private double getProbOnSpace(int x, int y, int currRound,
			EachSeaCreature ourCreature) {

		ArrayList<Direction> allDirections = Direction.allBut(null);
		ProbabilityCell[][] cells = new ProbabilityCell[2 * distance][2 * distance];

		// initialize board
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				cells[i][j] = new ProbabilityCell(new Point2D.Double(i, j), 0,
						null, 2 * distance);
			}
		}
		cells[distance][distance] = new ProbabilityCell(new Point2D.Double(
				distance, distance), 1, Direction.N, 2 * distance);

		for (int k = ourCreature.getLastseen(); k < currRound; k++) {
			// Calculate new probabilities
			for (int i = 0; i < cells.length; i++) {
				for (int j = 0; j < cells[0].length; j++) {

					for (Direction d : allDirections) {

						if (!(i + d.dx < 0 || i + d.dx > cells.length - 1
								|| j + d.dy < 0 || j + d.dy > cells.length - 1)) {

							for (Probability p : cells[i][j].probs) {

								if (p.dir.equals(d)) {

									cells[(i + d.dx)][(j + d.dy)]
											.ammendsameprob(cells[i][j]
													.getProb(p.dir), d,
													cells[i][j].same);
								} else

									cells[(i + d.dx)][(j + d.dy)].ammenddif(
											cells[i][j].getProb(p.dir), d,
											cells[i][j].diff);
							}
						}
					}
				}
			}

			// Update
			for (int i = 0; i < cells.length; i++) {
				for (int j = 0; j < cells[0].length; j++) {
					cells[i][j].update();
				}
			}
		}

		return cells[x][y].probability;
	}

	/** Determines whether or not we have seen an animal of a particular id */
	public boolean alreadySeen(int id) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j].isoccupideby(id))
					return true;
			}
		}

		return false;
	}

	public ArrayList<Direction> getLastDirectionOfHarmfulCreatures(
			Set<Observation> whatISee) {

		ArrayList<Direction> lastKnownDirectionOfDangerousCreatures = new ArrayList<Direction>();

		for (Observation creature : whatISee) {
			if (creature.isDangerous()) {
				if (creature.getDirection() != null) {
					lastKnownDirectionOfDangerousCreatures.add(creature
							.getDirection());
				}
			}
		}
		return lastKnownDirectionOfDangerousCreatures;
	}

	public ArrayList<Direction> getLastDirectionOfHarmfulCreaturesInRadius(
			Set<Observation> whatISee, Point2D whereIAm, int smallradius) {

		ArrayList<Direction> lastKnownDirectionOfDangerousCreatures = new ArrayList<Direction>();

		for (Observation creature : whatISee) {
			if (creature.isDangerous()
					&& creature.getLocation().distance(whereIAm) < smallradius) {
				if (creature.getDirection() != null) {
					lastKnownDirectionOfDangerousCreatures.add(creature
							.getDirection());
				}
			}
		}
		return lastKnownDirectionOfDangerousCreatures;
	}

	public boolean isDangerMobile(Set<Observation> danger){
		for(Observation creature : danger){
			if(creature.getDirection()!=null){
				return true;
			}
		}
		return false;
	}
	/**
	 * Determines whether or not we have seen the creature of type on that space
	 * using a probability model
	 */
	public boolean seenCreatureOnSpace(SeaCreatureType c, Point2D space,
			int currRound) {

		double totalprob = 0;
		for (Integer id : c.seen) {
			totalprob += getProbOnSpace(id, (int) space.getX(), (int) space
					.getY(), currRound);
		}

		if (totalprob > .1) // this is a constant that we can change in testing
			return true;

		return false;
	}

	/**
	 * Determines if this is a very dangerous board. We say it is dangerous if
	 * 100% of the creatures are dangerous
	 */
	public boolean dangerdanger() {
		int numdangerous = 0;
		double numOfDangerousStuff=0,sumOfUnhappiness=0,numOfStatic=0,totalCreatures=prototypes.size();
		for (SeaLifePrototype s : prototypes) {
			if(s.getSpeed()==0){
				numOfStatic++;
			}
			if (s.isDangerous()){
				numdangerous++;
				numOfDangerousStuff+=(s.getMaxCount()+s.getMinCount())/2.0;
				sumOfUnhappiness=((s.getMaxCount()+s.getMinCount())/2.0)*(2*s.getHappiness());
			}
		}
		if(numOfDangerousStuff/distance>1.0 && numOfStatic/totalCreatures<.5){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean seenall() {

		for (SeaLifePrototype s : prototypes) {

			int count = 0;

			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if (board[i][j].isoccupideby(s))
						count++;
				}
			}
			
			if((count < 3 && s.getMaxCount() > 3)){
				//Systemout.println(myId + " needs to see: " + s.getName() + " and have only seen: " + count);
				return false;
			}
			
			if((s.getMaxCount() < 3 && count < s.getMaxCount())){
				//Systemout.println(myId + " needs to see: " + s.getName() + " and have only seen: " + count);
				return false;
			}
		
			//Systemout.println("for " + s.getName() + " we have seen: " + count + " and max is: " + s.getMaxCount());
		}

		return true;
	}

	public boolean areThereDangerousCreaturesInRadiusNew(Set<Observation> whatISee, Point2D whereIAm) {
		for (Observation creature : whatISee) {
			if(creature.isDangerous()){
				/*dangerous creature in our radius*/
				Point2D p = new Point2D.Double(creature.getLocation().getX()+boat.getX(),
						creature.getLocation().getY()+boat.getY());
				if(whereIAm.distance(p) <= DANGER_RADIUS){
					return true;
				}
				/*there is a dangerous creature that we see but it is not too close to us*/
			}
		}
		return false;
	}

	public Direction getRelativeDirection(Point2D whereIAm, Point2D location) {
		Point2D creatureLoc =new Point2D.Double(location.getX()+boat.getX(),location.getY()+boat.getY());
		ArrayList<Direction> harmfulDirections = new ArrayList<Direction>();

		double myX = whereIAm.getX();
		double myY = whereIAm.getY();
		double dangerX = creatureLoc.getX();
		double dangerY = creatureLoc.getY();
		if (myX == dangerX && myY > dangerY) {
			return Direction.N;
		}

		if (myX == dangerX && myY < dangerY) {
			return (Direction.S);
		}

		if (myX > dangerX && myY == dangerY) {
			return (Direction.W);
		}
		if (myX < dangerX && myY == dangerY) {
			return (Direction.E);
		}
		if (myX < dangerX && myY > dangerY) {
			return (Direction.NE);
			
		}
		if (myX < dangerX && myY < dangerY) {
			return (Direction.SE);
		}
		if (myX > dangerX && myY > dangerY) {
			return (Direction.NW);
		}
		if (myX > dangerX && myY < dangerY) {
			return (Direction.SW);	
		}
		return null;
	}

}
