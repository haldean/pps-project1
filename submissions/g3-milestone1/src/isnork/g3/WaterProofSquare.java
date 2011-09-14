package isnork.g3;

import isnork.sim.SeaLife;
import isnork.sim.Player;
import java.util.Set;
import com.google.common.collect.*;

/**
 * Records what creatures, divers, the danger, and happiness of a square
 */
public class WaterProofSquare extends AbstractSquare{
	private Set<SeaLife> creatures = Sets.newHashSet();
	private Set<Player> divers = Sets.newHashSet();
	private double danger = 0.0;
	private double happiness = 0.0;

	void addCreature(SeaLife creature) {
		creatures.add(creature);
	}

	Set<SeaLife> getCreatures() {
		return creatures;
	}

	void addDiver(Player diver) {
		divers.add(diver);
	}

	Set<Player> getDivers() {
		return divers;
	}

	void setDanger(double danger) {
		this.danger = danger;
	}

	double getDanger() {
		return danger;
	}

	void setHappiness(double happiness) {
		this.happiness = happiness;
	}

	double getHappiness() {
		return happiness;
	}

}
