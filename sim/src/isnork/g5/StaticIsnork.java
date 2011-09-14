package isnork.g5;

import isnork.sim.GameObject.Direction;
import isnork.sim.GameEngine;
import isnork.sim.Observation;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class StaticIsnork {

	public static int NUM_IMP_CREATURE = 10;

	static int d;
	static int r;
	static int n;
	static int penalty;
	static ScanInformation scan;
	static Set<SeaLifePrototype> seaLifePossibilites;
	final static int maxDangerouse = 10000000;
	final static Point2D locationOfBoat = new Point2D.Double(0, 0);
	final static int totalTimeAvailable = 480;
	static int numHighPriorityCreatures = NUM_IMP_CREATURE;
	final static SeaLifePrototype priorityCreatures[] = new SeaLifePrototype[numHighPriorityCreatures];
	final static Point2D positionOfBoat = new Point2D.Double(0, 0);

	public static Point2D getPositionofboat() {
		return positionOfBoat;
	}

	public static void setPriorityCreatures() {

		// TODO: Currently adding the first 10 creatures to list, modify to add
		// highest happiness giving creatures

		// numHighPriorityCreatures=
		// (NUM_IMP_CREATURE<seaLifePossibilites.size())? NUM_IMP_CREATURE :
		// seaLifePossibilites.size();

		@SuppressWarnings("rawtypes")
		Iterator it = seaLifePossibilites.iterator();
		SeaLifePrototype tempVar2 = new SeaLifePrototype();
		int count = 0;
		if (!seaLifePossibilites.isEmpty()) {
			while (it.hasNext()) {
				tempVar2 = (SeaLifePrototype) it.next();
				if (count < numHighPriorityCreatures)
					priorityCreatures[count] = tempVar2;
				else {
				}
				count++;
			}
		}

		// SORT THE ARRAY.
		// int happiness=priorityCreatures[0].getHappiness();
		count = 1;
		SeaLifePrototype temp = new SeaLifePrototype();
		numHighPriorityCreatures = (NUM_IMP_CREATURE < seaLifePossibilites
				.size()) ? NUM_IMP_CREATURE : seaLifePossibilites.size();
		for (int i = 0; i < numHighPriorityCreatures - 1; i++) {
			for (int j = i + 1; j < numHighPriorityCreatures; j++) {

				if (priorityCreatures[i] != null
						&& priorityCreatures[j] != null) {
					if (priorityCreatures[i].getHappiness() < priorityCreatures[j]
							.getHappiness()) {
						// ... Exchange elements
						temp = priorityCreatures[i];
						priorityCreatures[i] = priorityCreatures[j];
						priorityCreatures[j] = temp;
					}
				}
			}
		}
	}

	public static ArrayList<Direction> directions = new ArrayList<Direction>();

	private void initalDirections() {
		Direction[] direction = Direction.values();
		for (int i = 0; i < direction.length; i++) {
			directions.add(direction[i]);
		}
	}

	public static String getCreatureMessage(int count) {
		String message = null;
		switch (count) {
		case 0:
			message = "a";
			break;
		case 1:
			message = "b";
			break;
		case 2:
			message = "c";
			break;
		case 3:
			message = "d";
			break;
		case 4:
			message = "e";
			break;
		case 5:
			message = "f";
			break;
		case 6:
			message = "g";
			break;
		case 7:
			message = "h";
			break;
		case 8:
			message = "i";
			break;
		case 9:
			message = "j";
			break;
		default:

		}
		return message;
	}

	public static String getDirectiontomeMessage(Direction dir) {
		String message = null;
		switch (dir) {
		case SE:
			message = "k";
			break;

		case NE:
			message = "l";
			break;

		case SW:
			message = "m";
			break;

		case NW:
			message = "n";
			break;

		case S:
			message = "o";
			break;

		case E:
			message = "p";
			break;

		case W:
			message = "q";
			break;

		case N:
			message = "r";
			break;

		default:
		}

		return message;
	}

	public static String getDirectionMessage(Direction dir) {
		String message = null;
		switch (dir) {
		case SE:
			message = "s";
			break;

		case NE:
			message = "t";
			break;

		case SW:
			message = "u";
			break;

		case NW:
			message = "v";
			break;

		case S:
			message = "w";
			break;

		case E:
			message = "x";
			break;

		case W:
			message = "y";
			break;

		case N:
			message = "z";
			break;

		default:
		}

		return message;
	}

	public static void printPrior() {
		for (int x = 0; x < numHighPriorityCreatures; x++) {
			if (priorityCreatures[x] != null)
				GameEngine.println(priorityCreatures[x].getName());
			else
				GameEngine.println("The list has not been initialized" + x);
		}
	}

	public static SeaLifePrototype lookUpObservation(Observation o) {
		Iterator it = seaLifePossibilites.iterator();
		SeaLifePrototype temp = new SeaLifePrototype();
		SeaLifePrototype creature = new SeaLifePrototype();
		if(o!=null){
		while (it.hasNext()) {
			temp = (SeaLifePrototype) it.next();
			if(temp.getName()!=null){
			if (temp.getName().equalsIgnoreCase(o.getName()))
				creature = temp;}
		}
		}
		return creature;
	}

	public static int lookUpIndex(Observation o) {
		int index = -1;
		for (int i = 0; i < numHighPriorityCreatures; i++) {
			if (priorityCreatures[i].getName().equalsIgnoreCase(o.getName()))
				index = i;
		}

		return index;
	}
	public static SeaLifePrototype lookUpCreature(int index) {
		SeaLifePrototype creature;
		creature=priorityCreatures[index];

		return creature;
	}

	public static int getCreaturefromMessage(String msg) {
		int index = -1;
		int count = 0;
		if (msg == null)
			index = -1;
		else {
			while (count < numHighPriorityCreatures) {
				if (msg.equalsIgnoreCase(getCreatureMessage(count))) {
					index = count;
				}
				count++;
			}
		}
		return index;
	}

	public static Direction getDirectionfromMessage(String msg) {
		Direction dir = null;
		if (msg == null) {
		} else {
			for (Direction d : directions) {
				if (msg.equalsIgnoreCase(getDirectionMessage(d))) {
					dir = d;
				}
			}
		}
		return dir;
	}
	public static boolean isMessageCreatureType(String msg)
	{
		char letter;
		boolean retVal=false;
		for (letter='a'; letter <= 'j'; letter++) { 
			if(msg.charAt(0)==letter)
			{	retVal=true; }			
		} 

		return retVal;
	}
	public static boolean isMessageDirectionType(String msg)
	{
		
		char letter;
		boolean retVal=false;
		for (letter='s'; letter <= 'z'; letter++) { 
			if(msg.charAt(0)==letter)
			{	retVal=true; }			
		} 

		return retVal;
	}
}
