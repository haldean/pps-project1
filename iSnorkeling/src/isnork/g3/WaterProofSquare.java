package isnork.g3;

import isnork.sim.SeaLife;
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

	public void addCreature(SeaLife creature, double certainty) {
		creatures.add(new ExpectedCreature(creature, certainty));
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
		this.danger = danger;
	}

	public double getExpectedDanger() {
		return danger;
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

    Square.SeaLifeExpectation creature;
    for (Iterator<Square.SeaLifeExpectation> iter = creatures.iterator();
         iter.hasNext();) {
      SeaLife seaLife = iter.next().getSeaLife();
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
    private SeaLife creature;
    private double certainty;

    public ExpectedCreature(SeaLife creature, double certainty) {
      this.creature = creature;
      this.certainty = certainty;
    }

    public SeaLife getSeaLife() {
      return creature;
    }

    public double getCertainty() {
      return certainty;
    }
  }
}
