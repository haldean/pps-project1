package isnork.g7;

import java.util.ArrayList;

import isnork.sim.GameObject.Direction;

public class DirectionUtil {
	public static ArrayList<Direction> getClosestDirections(Direction direction) {
		// Direction is E, NE, N, NW, W, SW, S, SE
		ArrayList<Direction> directions = new ArrayList<Direction>();
		int index = 0;
		for (int i = 0; i < Direction.values().length; i++) {
			if (Direction.values()[i].equals(direction)) {
				index = i;
				break;
			}
		}
		directions.add(Direction.values()[ (index + 1) % 8]);
		directions.add(Direction.values()[ (index + 7) % 8]);
		directions.add(Direction.values()[ (index + 2) % 8]);
		directions.add(Direction.values()[ (index + 6) % 8]);
		directions.add(Direction.values()[ (index + 3) % 8]);
		directions.add(Direction.values()[ (index + 5) % 8]);
		directions.add(Direction.values()[ (index + 4) % 8]);

		return directions;
	}
}
