package isnork.g3;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Queue;
import java.util.LinkedList;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;
import com.google.common.collect.ImmutableSortedSet;

public class WaterProofCartogram implements Cartogram {
	private Transcoder xcoder;
	private Messenger messenger;
	private final int sideLength;
	private final int viewRadius;
	private final int numDivers;
	private final Pokedex dex;
	private final Square[][] mapStructure;
	private final static Map<Direction, Coord> DIRECTION_MAP = ImmutableMap
			.<Direction, Coord> builder().put(Direction.E, new Coord(1, 0))
			.put(Direction.W, new Coord(-1, 0))

			.put(Direction.S, new Coord(0, 1))
			.put(Direction.N, new Coord(0, -1))

			.put(Direction.SE, new Coord(1, 1))
			.put(Direction.SW, new Coord(-1, 1))

			.put(Direction.NE, new Coord(1, -1))
			.put(Direction.NW, new Coord(-1, -1))
			.put(Direction.STAYPUT, new Coord(0, 0)).build();

	private final static Map<Direction, Coord> orthoDirectionMap = ImmutableMap
			.<Direction, Coord> builder().put(Direction.E, new Coord(1, 0))
			.put(Direction.W, new Coord(-1, 0))

			.put(Direction.S, new Coord(0, 1))
			.put(Direction.N, new Coord(0, -1)).build();

	private final static Map<Direction, Coord> diagDirectionMap = ImmutableMap
			.<Direction, Coord> builder().put(Direction.SE, new Coord(1, 1))
			.put(Direction.SW, new Coord(-1, 1))

			.put(Direction.NE, new Coord(1, -1))
			.put(Direction.NW, new Coord(-1, -1)).build();

	private Point2D currentLocation;
	private Random random;
	private int ticks;
	private List<CreatureRecord> movingCreatures;
	private static final int MAX_TICKS_PER_ROUND = 60 * 8;

	/*
	 * Tunes how quickly to forget we've seen a creature. The higher this
	 * number, the sooner a creature will be removed from the map after viewing.
	 */
	private static final double MAX_DECAY = 0.25;

	private static final double DANGER_RADIUS = 2;

	public WaterProofCartogram(int mapWidth, int viewRadius, int numDivers,
			Pokedex dex) {
		this.sideLength = mapWidth;
		this.viewRadius = viewRadius;
		this.numDivers = numDivers;
		this.mapStructure = new WaterProofSquare[sideLength][sideLength];
		for (int i = 0; i < sideLength; i++) {
			for (int j = 0; j < sideLength; j++) {
				this.mapStructure[i][j] = new WaterProofSquare();
			}
		}

		this.random = new Random();
		this.movingCreatures = Lists.newArrayList();
		this.dex = dex;
		ticks = 0;
		messenger = new WaterProofMessenger(dex, numDivers, sideLength);
	}

	@Override
	public void update(Point2D myPosition, Set<Observation> whatYouSee,
			Set<Observation> playerLocations,
			Set<iSnorkMessage> incomingMessages) {
		ticks++;
		currentLocation = myPosition;

		
		 //for (Observation location : playerLocations) {
		 //location.getLocation(); location.getId(); location.getName(); }
		 //
		 //messenger.addReceivedMessages(incomingMessages);
         // update heatmap with:
         //messenger.getDiscovered();
		 

		for (Observation observation : whatYouSee) {
			/*
			 * Note: this should not be happening on the diver's observations,
			 * but based on the other divers' observations. This is here to show
			 * how to properly update the map. TODO(haldean, hans): make this
			 * work with comm stuff.
			 */
			if (observation.getName() == null) {
				continue;
			}
			SeaLifePrototype seaLife = dex.get(observation.getName());
			if (seaLife.getSpeed() > 0) {
				movingCreatures.add(new CreatureRecord(observation
						.getLocation(), seaLife));
			} else {
				squareFor(observation.getLocation()).addCreature(seaLife, 1.);
			}
		}

		if (!whatYouSee.isEmpty()) {
			observe(whatYouSee);
		}
		updateMovingCreatures();
	}

	private void observe(Set<Observation> observations) {
		// pick highest value creature
		Ordering<Observation> happiness = new Ordering<Observation>() {
			public int compare(Observation left, Observation right) {
				return Doubles.compare(left.happinessD(), right.happinessD());
			}
		};
		ImmutableSortedSet<Observation> sortedObservations = ImmutableSortedSet
				.orderedBy(happiness).addAll(observations).build();
		Observation bestSeen = sortedObservations.first();

		messenger.addOutboundMessage(bestSeen);
	}

	private Square squareFor(Point2D location) {
		return squareFor((int) location.getX(), (int) location.getY());
	}

	private Square squareFor(int x, int y) {
		if (Math.abs(x) > sideLength / 2 || Math.abs(y) > sideLength / 2) {
			return null;
		}
		x += (sideLength / 2);
		y += (sideLength / 2);
		return mapStructure[x][y];
	}

	private void updateMovingCreatures() {
		for (Iterator<CreatureRecord> iter = movingCreatures.iterator(); iter
				.hasNext();) {
			CreatureRecord record = iter.next();

			int r = (ticks - record.confirmedAt) / 2;
			double certainty = 1 / (double) r;
			if (certainty <= MAX_DECAY) {
				iter.remove();
			}

			int x = (int) record.location.getX();
			int y = (int) record.location.getY();

			for (int dx = -r; dx <= r; dx++) {
				for (int dy = -r; dy <= r; dy++) {
					if (Math.sqrt(dx * dx + dy * dy) <= r) {
						Square thisSquare = squareFor(x + dx, y + dy);
						if (thisSquare != null) {
							thisSquare.addCreature(record.seaLife, certainty);
						}
					}
				}
			}
		}
	}

	@Override
	public String getMessage() {
		//TOOD: return Messenger.getNext()
		return "";
	}

	@Override
	public Direction getNextDirection() {
		return unOptimizedHeatmapGetNextDirection();
	}

	private Direction greedyHillClimb() {
		/*
		 * Iterate over all possible new squares you can hit next. For you to
		 * move in a diagonal direction, you need to be 1.5* as good as ortho To
		 * stay in the same square, you only need to be .5 * as good as ortho
		 */

		List<DirectionValue> lst = getExpectations(currentLocation.getX(),
				currentLocation.getY());

		Direction dir = getMaxDirection(lst);
		return dir;
	}

	private Direction getMaxDirection(List<DirectionValue> lst) {
		DirectionValue max = lst.get(0);

		for (DirectionValue dv : lst) {
			if (dv.getDub() > max.getDub()) {
				max = dv;
			}
		}

		return max.getDir();
	}

	private List<DirectionValue> getExpectations(double x, double y) {
		List<DirectionValue> lst = Lists.newArrayListWithCapacity(8);

		lst.add(new DirectionValue(Direction.STAYPUT,
				getExpectedHappinessForCoords(x, y) * 6.0));

		for (Entry<Direction, Coord> entry : orthoDirectionMap.entrySet()) {
			lst.add(new DirectionValue(entry.getKey(),
					getExpectedHappinessForCoords(entry.getValue().move(
							(int) x, (int) y)) * 3.0));
		}

		for (Entry<Direction, Coord> entry : diagDirectionMap.entrySet()) {
			lst.add(new DirectionValue(entry.getKey(),
					getExpectedHappinessForCoords(entry.getValue().move(
							(int) x, (int) y)) * 2.0));
		}
		return lst;
	}

	private double getExpectedHappinessForCoords(Coord coord) {
		return getExpectedHappinessForCoords(coord.getX(), coord.getY());
	}

	private double getExpectedHappinessForCoords(double unadjustedX,
			double unadjustedY) {
		if (unadjustedX == 0 && unadjustedY == 0) {
			return 0;
		}

		if (isInvalidCoords(unadjustedX, unadjustedY)) {
			return Double.MIN_VALUE;
		}

		double x = unadjustedX + sideLength / 2;
		double y = unadjustedY + sideLength / 2;

		int minX = (int) x - viewRadius;
		minX = ((minX > 0) ? minX : 0);

		int minY = (int) y - viewRadius;
		minY = ((minY > 0) ? minY : 0);

		int maxX = (int) x + viewRadius;
		maxX = ((maxX < sideLength) ? maxX : sideLength);

		int maxY = (int) y + viewRadius;
		maxY = ((maxY < sideLength) ? maxY : sideLength);

		double expectedHappiness = 0.0;
		for (int xCoord = minX; xCoord < maxX; xCoord++) {
			for (int yCoord = minY; yCoord < maxY; yCoord++) {
				double sqrt = Math.sqrt(square((xCoord - x)
						+ square(yCoord - y)));

				if (sqrt < viewRadius) {
					expectedHappiness += mapStructure[xCoord][yCoord]
							.getExpectedHappiness();
				}
			}
		}
		return expectedHappiness;
	}

	private final static double square(double x) {
		return x * x;
	}

	private double getExpectedDangerForCoords(double unadjustedX,
			double unadjustedY) {
		if (unadjustedX == 0 && unadjustedY == 0) {
			return 0;
		}

		if (isInvalidCoords(unadjustedX, unadjustedY)) {
			return Double.MIN_VALUE;
		}

		double x = unadjustedX + sideLength / 2;
		double y = unadjustedY + sideLength / 2;

		int minX = (int) x - viewRadius;
		minX = ((minX > 0) ? minX : 0);

		int minY = (int) y - viewRadius;
		minY = ((minY > 0) ? minY : 0);

		int maxX = (int) x + viewRadius;
		maxX = ((maxX < sideLength) ? maxX : sideLength);

		int maxY = (int) y + viewRadius;
		maxY = ((maxY < sideLength) ? maxY : sideLength);

		double expectedDanger = 0.0;
		for (int xCoord = minX; xCoord < maxX; xCoord++) {
			for (int yCoord = minY; yCoord < maxY; yCoord++) {
				double sqrt = Math.sqrt(square((xCoord - x)
						+ square(yCoord - y)));

				if (sqrt < viewRadius) {
					expectedDanger += mapStructure[xCoord][yCoord]
							.getExpectedDanger();
				}
			}
		}

		return expectedDanger;
	}

	private boolean isInvalidCoords(double x, double y) {
		if (x < -sideLength / 2) {
			return true;
		} else if (x > sideLength / 2) {
			return true;
		} else if (y < -sideLength / 2) {
			return true;
		} else if (y > sideLength / 2) {
			return true;
		} else {
			return false;
		}
	}

	private Direction unOptimizedHeatmapGetNextDirection() {
		int tickLeeway = MAX_TICKS_PER_ROUND - 3 * ticks;
		double y = currentLocation.getY();
		double x = currentLocation.getX();
		if (Math.abs(x) < tickLeeway
				&& Math.abs(y) < tickLeeway) {
			return greedyHillClimb();
		} else {
//			System.out.println("RETURN");
			return returnBoat(x, y);
		}
	}

	private Direction returnBoat(double x, double y) {
		// Move towards boat
		String direc = getReturnDirectionString();

		return avoidDanger(genList(direc), x, y);
	}

	private String getReturnDirectionString() {
		String direc = "";

		if (currentLocation.getY() < 0)
			direc = direc.concat("S");
		else if (currentLocation.getY() > 0)
			direc = direc.concat("N");

		if (currentLocation.getX() < 0)
			direc = direc.concat("E");
		else if (currentLocation.getX() > 0)
			direc = direc.concat("W");
		return direc;
	}

	private Direction avoidDanger(List<DirectionValue> genList, double x, double y) {
		for (DirectionValue dv : genList) {
			if (getExpectedDangerForCoords(DIRECTION_MAP.get(dv.getDir()).move((int) x, (int) y)) == 0) {
				return dv.getDir();
			}
		}
		return Direction.STAYPUT;
	}

	private double getExpectedDangerForCoords(Coord coord) {
		return getExpectedDangerForCoords(coord.getX(), coord.getY());
	}

	private static final List<DirectionValue> genList(String direc) {
		if (direc.equals("W")) {
			return ImmutableList.of(new DirectionValue(Direction.W, 2.0),
					new DirectionValue(Direction.NW, 1.0), new DirectionValue(
							Direction.SW, 1.0));
		} else if (direc.equals("E")) {
			return ImmutableList.of(new DirectionValue(Direction.E, 2.0),
					new DirectionValue(Direction.NE, 1.0), new DirectionValue(
							Direction.SE, 1.0));
		} else if (direc.equals("N")) {
			return ImmutableList.of(new DirectionValue(Direction.N, 2.0),
					new DirectionValue(Direction.NE, 1.0), new DirectionValue(
							Direction.NW, 1.0));
		} else if (direc.equals("S")) {
			return ImmutableList.of(new DirectionValue(Direction.S, 2.0),
					new DirectionValue(Direction.SE, 1.0), new DirectionValue(
							Direction.SW, 1.0));
		} else if (direc.equals("NE")) {
			return ImmutableList.of(new DirectionValue(Direction.NE, 2.0),
					new DirectionValue(Direction.N, 1.0), new DirectionValue(
							Direction.E, 1.0));
		} else if (direc.equals("SE")) {
			return ImmutableList.of(new DirectionValue(Direction.SE, 2.0),
					new DirectionValue(Direction.S, 1.0), new DirectionValue(
							Direction.E, 1.0));
		} else if (direc.equals("NW")) {
			return ImmutableList.of(new DirectionValue(Direction.NW, 2.0),
					new DirectionValue(Direction.W, 1.0), new DirectionValue(
							Direction.N, 1.0));
		} else if (direc.equals("SW")) {
			return ImmutableList.of(new DirectionValue(Direction.SW, 2.0),
					new DirectionValue(Direction.S, 1.0), new DirectionValue(
							Direction.W, 1.0));
		} else {
			return ImmutableList.of(new DirectionValue(Direction.STAYPUT, 1.0));
		}
	}

	public String toString() {
		StringBuilder output = new StringBuilder("Board at ");
		output.append(ticks);
		output.append("\n");

		for (int i = 0; i < mapStructure.length; i++) {
			for (int j = 0; j < mapStructure[i].length; j++) {
				output.append(mapStructure[i][j].getExpectedHappiness());
				output.append("\t");
			}
			output.append("\n");
		}

		output.append("\nWe got shit at:\n");
		for (int i = 0; i < mapStructure.length; i++) {
			for (int j = 0; j < mapStructure[i].length; j++) {
				if (mapStructure[i][j].getCreatures().size() > 0) {
					output.append(i - (sideLength / 2));
					output.append(", ");
					output.append(j - (sideLength / 2));
					output.append("\n");
				}
			}
		}
		return output.toString();
	}

	private class CreatureRecord {
		public final Point2D location;
		public final SeaLifePrototype seaLife;
		public final int confirmedAt;

		public CreatureRecord(Point2D location, SeaLifePrototype seaLife) {
			this.location = location;
			this.seaLife = seaLife;
			this.confirmedAt = ticks;
		}
	}
}
