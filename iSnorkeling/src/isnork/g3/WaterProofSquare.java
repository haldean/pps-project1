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
	private final Pokedex dex;
	private double danger = 0.0;
	private double happiness = 0.0;
	
	public WaterProofSquare(Pokedex dex) {
		super();
		this.dex = dex;
	}

	public void addCreature(SeaLifePrototype creature, double certainty, int id) {
		creatures.add(new ExpectedCreature(creature, certainty, id));
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
		return happiness - danger;
	}

	public void tick() {
		danger = 0.;
		happiness = 0.;
		divers.clear();

		for (Iterator<Square.SeaLifeExpectation> iter = creatures.iterator(); iter
				.hasNext();) {
			SeaLifeExpectation nextSeaLife = iter.next();
			int id = nextSeaLife.getId();
			SeaLifePrototype seaLife = nextSeaLife.getSeaLife();
			if (seaLife.getSpeed() > 0) {
				iter.remove();
			} else {
				if (!dex.isPersonallySeen(id)){
					happiness += seaLife.getHappiness() * 
						getModifier(dex.getPersonalSeenCount(seaLife.getName()));
				}
				if (seaLife.isDangerous()) {
					danger += 2 * seaLife.getHappiness();
				}
			}
		}
	}

	private double getModifier(int count) {
		switch (count){
		case 0:
			return 1.0;
		case 1:
			return 0.5;
		case 2:
			return 0.25;
		default:
			return 0;
		}
	}

	public class ExpectedCreature implements Square.SeaLifeExpectation {
		private SeaLifePrototype creature;
		private double certainty;
		private final int id;

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			long temp;
			temp = Double.doubleToLongBits(certainty);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			result = prime * result
					+ ((creature == null) ? 0 : creature.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ExpectedCreature other = (ExpectedCreature) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (Double.doubleToLongBits(certainty) != Double
					.doubleToLongBits(other.certainty))
				return false;
			if (creature == null) {
				if (other.creature != null)
					return false;
			} else if (!creature.equals(other.creature))
				return false;
			return true;
		}

		public ExpectedCreature(SeaLifePrototype creature, double certainty, int id) {
			this.creature = creature;
			this.certainty = certainty;
			this.id = id;
		}

		public SeaLifePrototype getSeaLife() {
			return creature;
		}

		public double getCertainty() {
			return certainty;
		}
		
		public int getId(){
			return id;
		}

		private WaterProofSquare getOuterType() {
			return WaterProofSquare.this;
		}
	}

	@Override
	public void increaseExpectedDangerBy(double d) {
		this.danger += d;
	}
}
