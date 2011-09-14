package isnork.g2.utilities;

import isnork.sim.GameObject.Direction;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ProbabilityCell {

	public ProbabilityCell(Point2D l, double i, Direction d, int distance) {
		location = l;
		probs = new ArrayList<Probability>();
		newprobs = new ArrayList<Probability>();
		for (Direction dr : Direction.allBut(null)) {

			if (dr.equals(d)) {
				probs.add(new Probability(dr, i));
			} else
				probs.add(new Probability(dr, 0));
		}
	}

	public String toString() {
		return location + " - " + probability;
	}

	public void finish() {
		probability = 0;
		for (Probability p : probs) {
			// System.out.println("ammending: " + p.prob);
			probability += p.prob;
		}

		// System.out.println("just set prob to: " + probability);
	}

	public void update() {

		probs = new ArrayList<Probability>(newprobs);
//		if (probs.size() > 8)
//			System.out.println("len probs: " + probs.size());
		
		newprobs = new ArrayList<Probability>();
	}

	public Probability getProb(Direction d) {

		for (Probability p : probs) {
			if (p.dir.equals(d))
				return p;
		}

		return new Probability(d, 0);
	}

	public void ammendsameprob(Probability prob, Direction d, double s) {

		Boolean found = false;
		for (Probability p : newprobs) {
			if (p.dir.equals(d)) {
				p.prob += s * prob.prob;
				found = true;
			}
		}
		if (!found)
			newprobs.add(new Probability(d, s * prob.prob));
	}

	public void ammenddif(Probability prob, Direction d, double df) {
		
		Boolean found = false;
		for (Probability p : newprobs) {
			if (p.dir.equals(d)) {
				p.prob += df * prob.prob;
				found = true;
			}
		}
		if (!found)
			newprobs.add(new Probability(d, df * prob.prob));

	}
	
	/*// 
	 * 
	 * We need something like this to change the probabilities if it is on the edge
	 * check to see if on the edge
		int legaldirections = 8;
		boolean cont = true;
		for (Direction dr : Direction.allBut(null)) {
			if (l.getX() + dr.dx < 0 || l.getX() + dr.dx >= distance
					|| l.getY() + dr.dy < 0 || l.getY() + dr.dy >= distance) {
				legaldirections--;
				if (dr.equals(d))
					cont = false;
			}
		}
		if (legaldirections != 8)
			System.out.println(l + " is on the edge with only "
					+ legaldirections + " edges and cont: " + cont);

		if (legaldirections == 3) { // on the corner
			if (cont)
				diff = .07;
			else
				diff = 1 / 3;
		}

		if (legaldirections == 5) { // on the edge
			if (cont)
				diff = .042;
			else
				diff = 1 / 5;
		}
		*/

	public double probability = 0;
	public ArrayList<Probability> probs;
	public ArrayList<Probability> newprobs;
	public Point2D location;
	public double same = .79;
	public double diff = .03;

}
