package isnork.g3;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;
import isnork.sim.iSnorkMessage;

import com.google.common.collect.Lists;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WaterProofCartogram implements Cartogram {
	private final int sideLength;
	private final int viewRadius;
	private final int numDivers;
  private final Pokedex dex;
	private final Square[][] mapStructure;
	
	private Point2D currentLocation;
	private Random random;
	private int ticks;
	private static final int MAX_TICKS_PER_ROUND = 60 * 8;
  /* Tunes how quickly to forget we've seen a creature. The higher this number,
   * the sooner a creature will be removed from the map after viewing. */
  private static final double MAX_DECAY = 0.25;

  private List<CreatureRecord> movingCreatures;
	
	public WaterProofCartogram(
      int mapWidth, int viewRadius, int numDivers, Pokedex dex) {
		this.sideLength = mapWidth;
		this.viewRadius = viewRadius;
		this.numDivers = numDivers;
		this.mapStructure = new WaterProofSquare[sideLength][sideLength];
		this.random = new Random();
    this.movingCreatures = Lists.newArrayList();
    this.dex = dex;
		ticks = 0;
	}

	@Override
	public void update(
      Point2D myPosition, Set<Observation> whatYouSee,
			Set<Observation> playerLocations, Set<iSnorkMessage> incomingMessages) {
    System.out.println("start update");
		ticks++;
		currentLocation = myPosition;

    /*
		for (Observation location : playerLocations) {
			location.getLocation();
			location.getId();
			location.getName();
		}

		for (iSnorkMessage message : incomingMessages) {
			message.getLocation();
			message.getMsg();
			message.getSender();
		}
    */

		for (Observation observation : whatYouSee) {
      System.out.println("obvs");
      /* Note: this should not be happening on the diver's observations, but
       * based on the other divers' observations. This is here to show how to
       * properly update the map. TODO(haldean, hans): make this work with comm
       * stuff. */
      SeaLifePrototype seaLife = dex.get(observation.getName());
      System.out.println(seaLife);
      if (seaLife.getSpeed() > 0) {
        System.out.println("it moves");
        movingCreatures.add(new CreatureRecord(
              observation.getLocation(), seaLife));
      } else {
        System.out.println("no moves");
        squareFor(observation.getLocation()).addCreature(seaLife, 1.);
      }
		}

    System.out.println("update moving");
    updateMovingCreatures();
    System.out.println("end update");
    System.out.println(this.toString());
	}

  private Square squareFor(Point2D location) {
    return mapStructure[(int) location.getX()][(int) location.getY()];
  }

  private void updateMovingCreatures() {
    for (Iterator<CreatureRecord> iter = movingCreatures.iterator();
        iter.hasNext(); ) {
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
            Square thisSquare = mapStructure[x][y];
            thisSquare.addCreature(record.seaLife, certainty);
          }
        }
      }
    }
  }

	@Override
	public String getMessage() {
    return "";
	}

	@Override
	public Direction getNextDirection() {
		return randomWalk();
	}
	private Direction randomWalk() {
		if (ticks < MAX_TICKS_PER_ROUND - 3 * sideLength) {
			Direction[] dirs = Direction.values();
			return dirs[random.nextInt(dirs.length)];
		} else {
			return returnBoat();
		}
	}

	private Direction returnBoat() {
		// Move towards boat
		String direc = "";

		if (currentLocation.getY() < 0)
			direc = direc.concat("S");
		else if (currentLocation.getY() > 0)
			direc = direc.concat("N");

		if (currentLocation.getX() < 0)
			direc = direc.concat("E");
		else if (currentLocation.getX() > 0)
			direc = direc.concat("W");

		if (direc.equals("W")) {
			return Direction.W;
		} else if (direc.equals("E")) {
			return Direction.E;
		} else if (direc.equals("N")) {
			return Direction.N;
		} else if (direc.equals("S")) {
			return Direction.S;
		} else if (direc.equals("NE")) {
			return Direction.NE;
		} else if (direc.equals("SE")) {
			return Direction.SE;
		} else if (direc.equals("NW")) {
			return Direction.NW;
		} else if (direc.equals("SW")) {
			return Direction.SW;
		} else {
			return Direction.STAYPUT;
		}
	}

  public String toString() {
    StringBuilder output = new StringBuilder("Board at ");
    output.append(ticks);
    output.append("\n");

    for (int i=0; i<mapStructure.length; i++) {
      for (int j=0; j<mapStructure[i].length; j++) {
        output.append(mapStructure[i][j].getExpectedHappiness());
        output.append("\t");
      }
      output.append("\n");
    }
    return output.toString();
  }

  private class CreatureRecord {
    public final Point2D location;
    public final SeaLifePrototype seaLife;
    public final int confirmedAt;

    public CreatureRecord(
        Point2D location, SeaLifePrototype seaLife) {
      this.location = location;
      this.seaLife = seaLife;
      this.confirmedAt = ticks;
    }
  }
}
