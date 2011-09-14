package isnork.g4;

import isnork.sim.GameObject.Direction;
import isnork.sim.Observation;
import isnork.sim.SeaLife;
import isnork.sim.SeaLifePrototype;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;

import org.apache.log4j.Logger;

public class CreatureEncoding {

	private static final Logger log = Logger.getLogger(CreatureEncoding.class);
	private SeaLifePrototypeComparator sealifeComparator;
	private HashMap<SeaLifePrototype, Integer> rank;
	private SeaLifePrototype[] rankArray;
	public static final int SMALL = 5;
	public static final int LARGE = 10;
	private static final int ENCODE_SMALL = 1;
	private static final int ENCODE_MEDIUM = 2;
	private static final int ENCODE_LARGE = 3;
	private static final int ALPHABET_SIZE = 27;

	private int sightRadius;
	private int encodingLength;

	public CreatureEncoding(Set<SeaLifePrototype> seaLifePossibilites,
			int boardDimension, int sightRadius) {
		sealifeComparator = new SeaLifePrototypeComparator(boardDimension);

		this.sightRadius = sightRadius;
		buildEncoding(seaLifePossibilites);
	}

	private void buildEncoding(Set<SeaLifePrototype> seaLifePossibilites) {
		PriorityQueue<SeaLifePrototype> sealife = new PriorityQueue<SeaLifePrototype>(
				seaLifePossibilites.size(), sealifeComparator);
		sealife.addAll(seaLifePossibilites);
		rank = new HashMap<SeaLifePrototype, Integer>();
		;
		int creaturesToEncode;
		if (sightRadius <= SMALL) {
			encodingLength = ENCODE_SMALL;
			creaturesToEncode = (int) Math.pow(ALPHABET_SIZE, encodingLength);
		} else if (sightRadius >= LARGE) {
			encodingLength = ENCODE_LARGE;
			creaturesToEncode = (int) Math.pow(ALPHABET_SIZE, encodingLength);
			creaturesToEncode /= (8 * 8 * 3);
		} else {
			encodingLength = ENCODE_MEDIUM;
			creaturesToEncode = (int) Math.pow(ALPHABET_SIZE, encodingLength);
			creaturesToEncode /= (8);
		}
		rankArray = new SeaLifePrototype[creaturesToEncode];
		for (int i = 0; i < creaturesToEncode && !sealife.isEmpty(); i++) {
			SeaLifePrototype life = sealife.remove();
			rank.put(life, i);
			rankArray[i] = life;
		}
	}

	public int getCodingLength() {
		return encodingLength;
	}

	public String getEncodingForObservations(Set<Observation> whatYouSee) {
		if (null == whatYouSee || whatYouSee.isEmpty())
			return "  ";

		return encodeObservation(whatYouSee); // return.length() ==
												// getCodingLength()
	}

	public SeaLife getEncodedCreature(iSnorkTransmission transmission) {
		// message.length() == getCodingLength()
		// return has a species, estimated location, and direction (or null if
		// unknown)
		SeaLife life = null;
		if (transmission == null)
			return null;

		int message = EncToNum(transmission.getMsg());
		int[] h;
		SeaLifePrototype creatureProt = null;
		switch (encodingLength) {
		case ENCODE_SMALL:
			creatureProt = rankArray[message];
			if (null != creatureProt)
				life = new SeaLife(creatureProt);
			break;
		case ENCODE_MEDIUM:
			h = decode2(message);
			creatureProt = rankArray[h[0]];
			if (null != creatureProt) {
				life = new SeaLife(creatureProt);
				life.setDirection(dir[h[1]]);
			}
			break;
		case ENCODE_LARGE:
			h = decode4(message);
			creatureProt = rankArray[h[0]];
			if (null != creatureProt) {
				life = new SeaLife(creatureProt);
				life.setDirection(dir[h[1]]);
				life = setLocation(life, transmission.getLocation(), h[2], h[3]);
			}
		}

		return life;
	}

	private SeaLife setLocation(SeaLife life, Point2D speakerLocation,
			int distance, int direction) {
		return life;
	}

	private String encodeObservation(Set<Observation> whatYouSee) {
		PriorityQueue<SeaLife> pq = new PriorityQueue<SeaLife>();
		return encodeCreature(pq.peek());
	}

	private String encodeCreature(SeaLife s) {
		if (sightRadius <= SMALL)
			return encodeCreatureSmall(s);
		else if (sightRadius >= LARGE)
			return encodeCreatureLarge(s);
		else
			return encodeCreatureMedium(s);
	}

	private String encodeCreatureSmall(SeaLife s) {
		if (rank.containsKey(s))
			return numberToEnc(rank.get(s).intValue());
		return " ";
	}

	private String encodeCreatureMedium(SeaLife s) {
		if (rank.containsKey(s))
			return numberToEnc(encode(rank.get(s).intValue(),
					directionToInt(s.getDirection())));
		return "  ";

	}

	private String encodeCreatureLarge(SeaLife s) {
		if (rank.containsKey(s))
			return numberToEnc(encode(rank.get(s).intValue(),
					directionToInt(s.getDirection()), 0, 0));
		return "   ";
	}

	private static Direction[] dir = new Direction[] { Direction.S,
			Direction.SE, Direction.E, Direction.NE, Direction.N, Direction.NW,
			Direction.W, Direction.SW };

	private static int directionToInt(Direction d) {
		switch (d) {
		case S:
			return 0;
		case SE:
			return 1;
		case E:
			return 2;
		case NE:
			return 3;
		case N:
			return 4;
		case NW:
			return 5;
		case W:
			return 6;
		case SW:
			return 7;
		}

		return 0;

	}

	public static String numberToEnc(int num) {
		String enc = "";
		do {
			if (num < ALPHABET_SIZE && enc.length() > 0)
				num--;
			enc += num == 26 ? ' ' : (char) ('a' + num % ALPHABET_SIZE);
			num /= ALPHABET_SIZE;

		} while (num > 0);
		return enc;
	}

	public static int encode(int e1, int e2, int e3, int e4) {
		e4 = e4 << 13;
		e3 = e3 << 9;
		e2 = e2 << 5;
		return e1 + e2 + e3 + e4;
	}

	public static int[] decode4(int e) {
		int[] h = new int[4];
		h[3] = e >> 13;
		h[2] = e >> 9 & 0xf;
		h[1] = e >> 5 & 0xf;
		h[0] = e & 0xF;
		return h;
	}

	public static int encode(int e1, int e2) {
		e2 = e2 << 6;
		return e1 + e2;
	}

	public static int[] decode2(int e) {
		int[] h = new int[2];
		h[1] = e >> 6;
		h[0] = e & 0xF;
		return h;
	}

	public static int EncToNum(String enc) {
		int num = 0;
		for (int i = 0; i < enc.length(); i++) {
			int cur = enc.charAt(i) == ' ' ? 26 : enc.charAt(i) - 'a';
			if (i == enc.length() - 1 && enc.length() > 1)
				num += Math.pow(ALPHABET_SIZE, i) * (cur + 1);
			else
				num += Math.pow(ALPHABET_SIZE, i) * (cur);
		}
		return num;

	}
}
