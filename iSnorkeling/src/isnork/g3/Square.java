package isnork.g3;

public interface Square {
	void addCreature(SeaLife creature);
	Set<SeaLife> getCreatures();
	void addDiver(Player diver);
	Set<Player> getDivers();
	void setDanger(double danger);
	double getDanger();
	void setHappiness(double happiness);
	double getHappiness();
}
