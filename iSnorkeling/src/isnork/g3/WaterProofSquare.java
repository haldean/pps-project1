package isnork.g3;

import isnork.sim.SeaLifePrototype;
import isnork.sim.Player;

import com.google.common.collect.*;

import java.util.Iterator;
import java.util.Set;

/**
 * Records what creatures, divers, the danger, and happiness of a square
 */
public class WaterProofSquare implements Square {
	private Set<Square.SeaLifeExpectation> creatures = Sets.newHashSet();
	private Set<Player> divers = Sets.newHashSet();
	private double danger = 0.0;
	private double happiness = 0.0;

	public void addCreature(SeaLifePrototype creature, double certainty) {
		creatures.add(new ExpectedCreature(creature, certainty));
		happiness += creature.getHappiness();
		danger += creature.isDangerous() ? 2 * creature.getHappiness() : 0;
	}

	public Set<Square.SeaLifeExpectation> getCreatures() {
		return creatures;
	}

	public void addDiver(Player diver) {
		divers.add(diver);
	}

	public Set<Player> getDivers() {
		return divers;
	}

	public void setExpectedDanger(double danger) {
		throw new RuntimeException(
				"WaterProofSquare can calculate it's own danger, thank you.");
	}

	public double getExpectedDanger() {
		return danger;
	}

	public void increaseExpectedHappinessBy(double happiness) {
		this.happiness += happiness;
	}

	public void setExpectedHappiness(double happiness) {
		this.happiness = happiness;
	}

	public double getExpectedHappiness() {
		return happiness;
	}

	public void tick() {
		danger = 0.;
		happiness = 0.;
		divers.clear();

		for (Iterator<Square.SeaLifeExpectation> iter = creatures.iterator(); iter
				.hasNext();) {
			SeaLifePrototype seaLife = iter.next().getSeaLife();
			if (seaLife.getSpeed() > 0) {
				iter.remove();
			} else {
				happiness += seaLife.getHappiness();
				if (seaLife.isDangerous()) {
					danger -= 2 * happiness;
				}
			}
		}
	}

	public class ExpectedCreature implements Square.SeaLifeExpectation {
		private SeaLifePrototype creature;
		private double certainty;

		public ExpectedCreature(SeaLifePrototype creature, double certainty) {
			this.creature = creature;
			this.certainty = certainty;
		}

		public SeaLifePrototype getSeaLife() {
			return creature;
		}

		public double getCertainty() {
			return certainty;
		}
	}

	@Override
	public void increaseExpectedDangerBy(double d) {
		this.danger += d;
	}
}
