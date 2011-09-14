package isnork.g5;

	import java.awt.geom.Point2D;
	import java.util.ArrayList;
	import java.util.Set;

	import isnork.sim.GameConfig;
import isnork.sim.GameEngine;
	import isnork.sim.Observation;
	import isnork.sim.Player;
	import isnork.sim.SeaLife;
	import isnork.sim.SeaLifePrototype;
	import isnork.sim.iSnorkMessage;
import isnork.sim.GameObject.Direction;



	public class G5HappyPlayerv3 extends Player {

		private Direction direction;
		private int id;
		Point2D whereIAm = null;
		int n = -1;
		int elapsedTImeInMinutes = 0;
		final int smallBoardDimension = 20;
		final double totalTimeAvailable = 480;
		double minimumTimeToReturnToBoatForSmallBoard = 80; //not considering dangerous creatures, (20squares*2mins) * 2 
		final double bufferforDangerousCreaturesForSmallBoard = 50; 
		final double timeToStartReturnRouteForSmallBoard = totalTimeAvailable - (minimumTimeToReturnToBoatForSmallBoard + bufferforDangerousCreaturesForSmallBoard);
		double timeToStartReturnRoute = timeToStartReturnRouteForSmallBoard;
		final int maxDistanceSnorkelerTravelsBasedOnMessage = 10; //for which he spends 28 minutes for best route
		final Point2D locationOfBoat = new Point2D.Double(0,0); 
		ArrayList <Observation> whatYousee = new ArrayList<Observation>();
		Set<iSnorkMessage> messagesReceived;
		Set<Observation> myCopyOfSnorkelerLocations;
		Set<SeaLifePrototype> seaLifePossibilites;
		ArrayList <Observation> whatYouSee = new ArrayList<Observation>();
		ArrayList<Direction> unsafeDirections = new ArrayList<Direction>();
		//Direction unsafeDirection;
		

		
		@Override
		public String getName() {
			double multiplicationFactor = GameConfig.d/smallBoardDimension;
			if (GameConfig.d > smallBoardDimension)
				timeToStartReturnRoute = totalTimeAvailable - (multiplicationFactor * minimumTimeToReturnToBoatForSmallBoard) +  ((multiplicationFactor/2 * 1.5) * bufferforDangerousCreaturesForSmallBoard);
			
			
			double buffer = ((multiplicationFactor/2 * 1.5) * bufferforDangerousCreaturesForSmallBoard);
			
			/* if (Math.abs(this.getId()) == 1) {
				GameEngine.println("timeToStartReturnRoute " + timeToStartReturnRoute);
				GameEngine.println("buffer " + buffer);
				GameEngine.println("GameConfig.d " + GameConfig.d);
				GameEngine.println("smallBoardDimension " + smallBoardDimension);
			}*/
			
			return "G5HappyPlayerv3";
		}
		
		@Override
		public String tick(Point2D myPosition, Set<Observation> whatYouSee, Set<iSnorkMessage> incomingMessages,Set<Observation> playerLocations) {
			
			messagesReceived = incomingMessages;
			myCopyOfSnorkelerLocations = playerLocations;
			whereIAm = myPosition;
			this.whatYousee.clear();
			//unsafeDirection = null;
			unsafeDirections.clear();
			
			for (Observation i: whatYouSee)
				{
				if (i.isDangerous() == true) {
					GameEngine.println("dangerous creature " + i.getName() + " in vicinity of snorkeler " + this.getId());
					computeUnsafeDirection(i.getLocation());
				}
				this.whatYousee.add(i);
					
					
				}
			//get player id at the first round.
			if(elapsedTImeInMinutes ==0)
			{
			for(Observation i: playerLocations)
			{
				if (i.getLocation() == whereIAm)
				{
					id=i.getId();
					break;
				}
			}
			}
			elapsedTImeInMinutes ++;
			
			n++;
			
			
			
			//if(n % 10 == 0) {
				//if (elapsedTImeInMinutes < timeToStartReturnRoute)
					//GameEngine.println("Snorkeler " + this.getId() + " returned a message ");
				return "s";
			//}
			//else
			//return null;
		}
		
		private void computeUnsafeDirection(Point2D locationOfDangerousCreature) {
			// TODO Auto-generated method stub
			final int unsafeDistanceFromDangerousCreature = 5;
			//unsafeDirections.clear();
			//unsafeDirection = null;
			
			if (whereIAm.distance(locationOfDangerousCreature) <= unsafeDistanceFromDangerousCreature) {
				
				if (locationOfDangerousCreature.getX() == whereIAm.getX()) {
					if (locationOfDangerousCreature.getY() > whereIAm.getY()) unsafeDirections.add(Direction.S);
					else if (locationOfDangerousCreature.getY() < whereIAm.getY()) unsafeDirections.add(Direction.N);
				}
				else if (locationOfDangerousCreature.getY() == whereIAm.getY()) {
					if (locationOfDangerousCreature.getX() > whereIAm.getX()) unsafeDirections.add(Direction.E);
					else if (locationOfDangerousCreature.getX() < whereIAm.getX()) unsafeDirections.add(Direction.W);
				}
				else {
					
					if (locationOfDangerousCreature.getX() > whereIAm.getX() && locationOfDangerousCreature.getY() < whereIAm.getY()) {
						//unsafeDirection = Direction.NE;
						unsafeDirections.add(Direction.NE);
					}
					else if (locationOfDangerousCreature.getX() > whereIAm.getX() && locationOfDangerousCreature.getY() > whereIAm.getY()) {
						unsafeDirections.add(Direction.SE);
					}
					else if (locationOfDangerousCreature.getX() < whereIAm.getX() && locationOfDangerousCreature.getY() < whereIAm.getY()) {
						unsafeDirections.add(Direction.NW);
					}
					else if (locationOfDangerousCreature.getX() < whereIAm.getX() && locationOfDangerousCreature.getY() > whereIAm.getY()) {
						unsafeDirections.add(Direction.SW);
					}
					
					
				}
				
				GameEngine.println("unsafe direction for snorkeler " + this.getId() + " are the following ");
				
				for (Direction unsafeDirection : unsafeDirections)
					GameEngine.println(unsafeDirection);
				
			}
			
			
						
		}

		@Override
		public void newGame(Set<SeaLifePrototype> seaLifePossibilites, int penalty,
				int d, int r, int n) {
			// TODO Auto-generated method stub
			
			this.seaLifePossibilites = seaLifePossibilites;
			
		}
		@Override
		public Direction getMove() {
			Direction d = null;
			int timeWhenTeamIsSpreadOut = 80;
			
			//TODO: Fix the following - 
			//Only if snorkelers are nicely spread out, we respond to messages.
			//We are therefore, wasting the first many messages
			
			
			
			
			if (!messagesReceived.isEmpty() && elapsedTImeInMinutes >= timeWhenTeamIsSpreadOut && elapsedTImeInMinutes < timeToStartReturnRoute)
				d = actBasedOnIncomingMessages(messagesReceived);
			else
				d = getNewDirection();
			
			
						
						
			if (elapsedTImeInMinutes >= timeToStartReturnRoute) {
				//GameEngine.println("Elapsed Time :" + elapsedTImeInMinutes + " Returning to boat ");
				
				d = towardsBoat();
				
			}
			
			if (!unsafeDirections.isEmpty()) {
				GameEngine.println("calling getSafeDirection for snork " + this.getId() );
				d = getSafeDirection();
			}
			
			
			try {
				Point2D p = new Point2D.Double(whereIAm.getX() + d.dx,
						whereIAm.getY() + d.dy);
				
				while (Math.abs(p.getX()) > GameConfig.d
						|| Math.abs(p.getY()) > GameConfig.d) {
					d = getNewDirection();
					p = new Point2D.Double(whereIAm.getX() + d.dx,
							whereIAm.getY() + d.dy);
				}
			}
			
			catch (Exception e) { 
				GameEngine.println("direction is null for snorkeler " + this.getId());
				d = null;
			}
			return d;
		}
		
		private Direction getSafeDirection() {
			// TODO Auto-generated method stub
			
			ArrayList<Direction> safeDirections = new ArrayList<Direction>();
			
			for (Direction direction: Direction.values())
				safeDirections.add(direction);
			
		
		for (Direction unsafeDirection : unsafeDirections){	
			
		if (unsafeDirection == Direction.N) {
			safeDirections.remove(Direction.N);
			safeDirections.remove(Direction.NE);
			safeDirections.remove(Direction.NW);
			safeDirections.remove(Direction.E);
			safeDirections.remove(Direction.W);
			
			
			/*
			safeDirections.add(Direction.S);
			safeDirections.add(Direction.SE);
			safeDirections.add(Direction.SW);
			*/
		}
		else if (unsafeDirection == Direction.S) {
			
			safeDirections.remove(Direction.S);
			safeDirections.remove(Direction.SE);
			safeDirections.remove(Direction.SW);
			safeDirections.remove(Direction.E);
			safeDirections.remove(Direction.W);
			
			/*
			safeDirections.add(Direction.N);
			safeDirections.add(Direction.NE);
			safeDirections.add(Direction.NW);
			*/
		}
		else if (unsafeDirection == Direction.W) {
			
			safeDirections.remove(Direction.N);
			safeDirections.remove(Direction.S);
			safeDirections.remove(Direction.NW);
			safeDirections.remove(Direction.SW);
			safeDirections.remove(Direction.W);
			
			/*
			safeDirections.add(Direction.E);
			safeDirections.add(Direction.NE);
			safeDirections.add(Direction.SE);
			*/
		}
		else if (unsafeDirection == Direction.E) {
			
			safeDirections.remove(Direction.N);
			safeDirections.remove(Direction.NE);
			safeDirections.remove(Direction.SE);
			safeDirections.remove(Direction.S);
			safeDirections.remove(Direction.E);
			
			
/*			safeDirections.add(Direction.W);
			safeDirections.add(Direction.NW);
			safeDirections.add(Direction.SW);*/
		}
		else if (unsafeDirection == Direction.NW) {
			
			safeDirections.remove(Direction.N);
			safeDirections.remove(Direction.NE);
			safeDirections.remove(Direction.NW);
			safeDirections.remove(Direction.SW);
			safeDirections.remove(Direction.W);
			
			
			/*
			safeDirections.add(Direction.S);
			safeDirections.add(Direction.SE);
			safeDirections.add(Direction.E);
			*/
		}
		else if (unsafeDirection == Direction.NE) {
		
			safeDirections.remove(Direction.N);
			safeDirections.remove(Direction.NE);
			safeDirections.remove(Direction.NW);
			safeDirections.remove(Direction.SE);
			safeDirections.remove(Direction.E);
			
			
			
			/*	safeDirections.add(Direction.S);
			safeDirections.add(Direction.SW);
			safeDirections.add(Direction.W); */
		}
		else if (unsafeDirection == Direction.SW) {
			
			safeDirections.remove(Direction.NW);
			safeDirections.remove(Direction.S);
			safeDirections.remove(Direction.SE);
			safeDirections.remove(Direction.SW);
			safeDirections.remove(Direction.W);
			
			
			
			
			/*safeDirections.add(Direction.N);
			safeDirections.add(Direction.NE);
			safeDirections.add(Direction.E);*/
		}
		else if (unsafeDirection == Direction.SE) {
			
			safeDirections.remove(Direction.NE);
			safeDirections.remove(Direction.S);
			safeDirections.remove(Direction.SE);
			safeDirections.remove(Direction.SW);
			safeDirections.remove(Direction.E);
			
			/*
			safeDirections.add(Direction.N);
			safeDirections.add(Direction.NW);
			safeDirections.add(Direction.W); */
		}
		}
		
		
		GameEngine.println("list of safe directions ");
		for (Direction safeDirection: safeDirections)
			GameEngine.println(safeDirection);
		
		
		if (safeDirections.isEmpty()) GameEngine.println("SAFE DIRECTIONS NULL ");
				
		Direction safeDirection = null;
		
		try {
		safeDirection = safeDirections.get(random.nextInt(safeDirections.size()));
		}
		
		catch (Exception e) {
			safeDirection = null;
		}
		
		if (safeDirection == null) GameEngine.println("random safe direction null for snorkeler " + this.getId());
			
			return safeDirection;
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

		
		
		public Direction towardsBoat() {
			
			Direction direction = null;
			
			//GameEngine.println("towardsBoat " + locationOfBoat.getX() + " " + locationOfBoat.getY());
					
			if (whereIAm.getY() == locationOfBoat.getY()) {
				if (whereIAm.getX() < locationOfBoat.getX()) direction = Direction.E; 
				else if (whereIAm.getX() > locationOfBoat.getX()) direction = Direction.W;
				
			}
				
			if (whereIAm.getX() == locationOfBoat.getX()) {
				if (whereIAm.getY() < locationOfBoat.getY()) direction = Direction.S;
				else if  (whereIAm.getY() > locationOfBoat.getY())direction = Direction.N;
			}
			
			//Direction = null when whereIAm.getX() = 0 and whereIAm.getY() = 0
			
			
			if (whereIAm.getY() < locationOfBoat.getY() && whereIAm.getX() != locationOfBoat.getX()) direction = Direction.S;
			else if (whereIAm.getY() > locationOfBoat.getY() && whereIAm.getX() != locationOfBoat.getX()) direction = Direction.N;
			
			
			direction = checkForDangerousCreatures(whereIAm, direction);
			
			/* if (whereIAm.getX() != 0 && whereIAm.getY() != 0)
				GameEngine.println("direction in towardsBoat fn" + direction); */
			
			return direction;
			
		}
		
		
		public Direction checkForDangerousCreatures(Point2D whereIAm, Direction direction) {
		
			//check whereIAm
			//if dangerous creature is in sight, avoid
			//else
			
			
			Direction d = direction;
			
			/*
			Move m = new Move(seaLifePossibilites, whatYouSee, whereIAm);
			if (m.dangerFlag ==0) d = direction;
			else d = m.escapeMove(random);
			*/
			
			
			
			return d;
		
		}
		
		
		public Direction actBasedOnIncomingMessages(Set<iSnorkMessage> incomingMessages) {
			
			Direction direction = null;
			
			int tooSmallDistance = 4; //TODO: Should this number be 'r'?
			double minimumDistanceToTravel = maxDistanceSnorkelerTravelsBasedOnMessage; //initialize to max distance he can travel
			double squareRootOfTwo = 1.414;
			//initialize Point to a point that lies diagonally from whereIAm at a distance of minimumDistanceToTravel
			//the following location will never be used - only for the sake of initialization
			Point2D locationOfSnorkelerWithMinDistance = new Point2D.Double(whereIAm.getX() + minimumDistanceToTravel/squareRootOfTwo, whereIAm.getY() + minimumDistanceToTravel/squareRootOfTwo);
			
			
			//TODO: Some snorkelers don't enter this loop. Test and find out why.
			//TODO: Fix this - WHen snorkelers are too close, they keep exchanging messages and don't move out.
			
			for (iSnorkMessage message : incomingMessages) {
				double distanceFromSender = whereIAm.distance(message.getLocation());
				if (distanceFromSender < minimumDistanceToTravel && distanceFromSender > 0 && (message.getSender() != this.getId()) ) { 
				minimumDistanceToTravel = distanceFromSender;
				int sender = Math.abs(message.getSender());
				//Observation[] locations = (Observation[]) myCopyOfSnorkelerLocations.toArray();
				locationOfSnorkelerWithMinDistance = message.getLocation();
				//GameEngine.println("locationOfSnorkelerWithMinDistance " + locationOfSnorkelerWithMinDistance);
				}
			}
			
			
			if (minimumDistanceToTravel > maxDistanceSnorkelerTravelsBasedOnMessage) return null;
			
			//move to closest sender	
			
			
			//GameEngine.println("snorkeler " + this.getId() + " traveling to location " + locationOfSnorkelerWithMinDistance.getX() + ", " + locationOfSnorkelerWithMinDistance.getY());
			
			if (whereIAm.getY() == locationOfSnorkelerWithMinDistance.getY()) {
				if (whereIAm.getX() < locationOfSnorkelerWithMinDistance.getX()) direction = Direction.E; 
				else if (whereIAm.getX() > locationOfSnorkelerWithMinDistance.getX()) direction = Direction.W;
				
			}
				
			if (whereIAm.getX() == locationOfSnorkelerWithMinDistance.getX()) {
				if (whereIAm.getY() < locationOfSnorkelerWithMinDistance.getY()) direction = Direction.S;
				else if  (whereIAm.getY() > locationOfSnorkelerWithMinDistance.getY())direction = Direction.N;
			}
			
			//Direction = null when whereIAm.getX() = 0 and whereIAm.getY() = 0
			
			
			if (whereIAm.getY() < locationOfSnorkelerWithMinDistance.getY() && whereIAm.getX() != locationOfSnorkelerWithMinDistance.getX()) direction = Direction.S;
			else if (whereIAm.getY() > locationOfSnorkelerWithMinDistance.getY() && whereIAm.getX() != locationOfSnorkelerWithMinDistance.getX()) direction = Direction.N;
			
			
			direction = checkForDangerousCreatures(whereIAm, direction);
			
			return direction;
			
		}
		
		
		
		



	}

