package isnork.g3;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import java.awt.geom.Point2D;

public class MockObservation extends Observation {
	public Point2D location;
	public int id;
	public String name;
	public double happy;
	public boolean danger;
	public Direction dir;

  public MockObservation(
      Point2D location, int id, String name, double happy, boolean danger,
      Direction dir) {
    this.location = location;
    this.id = id;
    this.name = name;
    this.happy = happy;
    this.danger = danger;
    this.dir = dir;
  }

	public Point2D getLocation() {
		return location;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isDangerous() {
		return danger;
	}

	public double happinessD() {
		return happy;
	}

	public int happiness() {
		return (int) happy;
	}

	public Direction getDirection() {
		return dir;
	}
}
