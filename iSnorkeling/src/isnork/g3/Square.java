package isnork.g3;

import java.util.Set;

import isnork.sim.Player;
import isnork.sim.SeaLifePrototype;

public interface Square {
	public interface SeaLifeExpectation {
		SeaLifePrototype getSeaLife();
		int getId();

		double getCertainty();
	}

	/**
	 * Add the presence of a creature to the square, along with the certainty
	 * that it is on the square.
	 * 
	 * @param certainty
	 *            The probability the creature is on the square, as a number
	 *            from 0 to 1. Stationary creatures should have a certainty of
	 *            1.
	 */
	void addCreature(SeaLifePrototype creature, double certainty, int id);

	/**
	 * Get the creatures thought to be on this square, along with the certainty
	 * of its presence.
	 */
	Set<SeaLifeExpectation> getCreatures();

	/**
	 * Add the presence of a diver to this square.
	 */
	void addDiver(Player diver);

	/**
	 * Get the divers currently on this square.
	 */
	Set<Player> getDivers();

	/**
	 * Set the expected danger value for this square.
	 */
	void setExpectedDanger(double danger);

	/**
	 * Get the expected danger value for this square.
	 */
	double getExpectedDanger();

	void increaseExpectedHappinessBy(double happiness);

	/**
	 * Set the expected happiness value for this square. Note: this includes
	 * subtracting the danger associated with this square.
	 */
	void setExpectedHappiness(double happiness);

	/**
	 * Get the expected happiness value for this square. Note: this includes
	 * subtracting the danger associated with this square.
	 */
	double getExpectedHappiness();

	/**
	 * Reset the square. This should be called at the start of each tick, and
	 * should remove all happiness on the square that is not due to the
	 * existence of stationary creatures. It should also remove all divers and
	 * nonstationary creatures from the corresponding sets.
	 */
	void tick();

	/**
	 * Increases expected danger by the amount specified.
	 * @param d
	 */
	void increaseExpectedDangerBy(double d);
}
