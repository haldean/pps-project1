package isnork.g3;

import isnork.sim.SeaLife;

import java.util.List;

import java.awt.geom.Point2D;

public interface Transcoder {
	/**
	 * Encodes the species, ID, and location of an observation to a 5-character message.
	 * A ' ' indicates an empty message.
	 */
	public List<String> encode(String name, String id, Point2D location);
	public List<String> encode(String name, int id, Point2D location);

	/**
	 * Decodes a 5-character message and returns a reconstructed Observation.
	 * An empty message must be represented by a ' '.
	 */
	public SeaLife decode(String msg);
}