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

	public void addCreature(SeaLife creature) {
		creatures.add(creature);
	}

	public Set<SeaLife> getCreatures() {
		return creatures;
	}

	public void addDiver(Player diver) {
		divers.add(diver);
	}

	public Set<Player> getDivers() {
		return divers;
	}

	public void setDanger(double danger) {
		this.danger = danger;
	}

	public double getDanger() {
		return danger;
	}

	public void setHappiness(double happiness) {
		this.happiness = happiness;
	}

	public double getHappiness() {
		return happiness;
	}

}
